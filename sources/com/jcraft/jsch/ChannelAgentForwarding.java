package com.jcraft.jsch;

import java.io.IOException;
import java.util.Vector;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChannelAgentForwarding extends Channel {
    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 16384;
    private static final int LOCAL_WINDOW_SIZE_MAX = 131072;
    private Buffer mbuf;
    private Buffer rbuf;
    private final byte SSH_AGENTC_REQUEST_RSA_IDENTITIES = 1;
    private final byte SSH_AGENT_RSA_IDENTITIES_ANSWER = 2;
    private final byte SSH_AGENTC_RSA_CHALLENGE = 3;
    private final byte SSH_AGENT_RSA_RESPONSE = 4;
    private final byte SSH_AGENT_FAILURE = 5;
    private final byte SSH_AGENT_SUCCESS = 6;
    private final byte SSH_AGENTC_ADD_RSA_IDENTITY = 7;
    private final byte SSH_AGENTC_REMOVE_RSA_IDENTITY = 8;
    private final byte SSH_AGENTC_REMOVE_ALL_RSA_IDENTITIES = 9;
    private final byte SSH2_AGENTC_REQUEST_IDENTITIES = 11;
    private final byte SSH2_AGENT_IDENTITIES_ANSWER = 12;
    private final byte SSH2_AGENTC_SIGN_REQUEST = 13;
    private final byte SSH2_AGENT_SIGN_RESPONSE = 14;
    private final byte SSH2_AGENTC_ADD_IDENTITY = 17;
    private final byte SSH2_AGENTC_REMOVE_IDENTITY = 18;
    private final byte SSH2_AGENTC_REMOVE_ALL_IDENTITIES = 19;
    private final byte SSH2_AGENT_FAILURE = 30;
    boolean init = true;
    private Buffer wbuf = null;
    private Packet packet = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelAgentForwarding() {
        this.rbuf = null;
        this.mbuf = null;
        setLocalWindowSizeMax(131072);
        setLocalWindowSize(131072);
        setLocalPacketSize(16384);
        this.type = Util.str2byte("auth-agent@openssh.com");
        this.rbuf = new Buffer();
        this.rbuf.reset();
        this.mbuf = new Buffer();
        this.connected = true;
    }

    @Override // com.jcraft.jsch.Channel, java.lang.Runnable
    public void run() {
        try {
            sendOpenConfirmation();
        } catch (Exception e) {
            this.close = true;
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void write(byte[] foo, int s, int l) throws IOException {
        Identity _identity;
        if (this.packet == null) {
            this.wbuf = new Buffer(this.rmpsize);
            this.packet = new Packet(this.wbuf);
        }
        this.rbuf.shift();
        int i = 0;
        if (this.rbuf.buffer.length < this.rbuf.index + l) {
            byte[] newbuf = new byte[this.rbuf.s + l];
            System.arraycopy(this.rbuf.buffer, 0, newbuf, 0, this.rbuf.buffer.length);
            this.rbuf.buffer = newbuf;
        }
        this.rbuf.putByte(foo, s, l);
        int mlen = this.rbuf.getInt();
        if (mlen > this.rbuf.getLength()) {
            Buffer buffer = this.rbuf;
            buffer.s -= 4;
            return;
        }
        int typ = this.rbuf.getByte();
        try {
            Session _session = getSession();
            IdentityRepository irepo = _session.getIdentityRepository();
            UserInfo userinfo = _session.getUserInfo();
            this.mbuf.reset();
            if (typ == 11) {
                this.mbuf.putByte((byte) 12);
                Vector identities = irepo.getIdentities();
                synchronized (identities) {
                    int count = 0;
                    for (int i2 = 0; i2 < identities.size(); i2++) {
                        Identity identity = (Identity) identities.elementAt(i2);
                        if (identity.getPublicKeyBlob() != null) {
                            count++;
                        }
                    }
                    this.mbuf.putInt(count);
                    while (i < identities.size()) {
                        Identity identity2 = (Identity) identities.elementAt(i);
                        byte[] pubkeyblob = identity2.getPublicKeyBlob();
                        if (pubkeyblob != null) {
                            this.mbuf.putString(pubkeyblob);
                            this.mbuf.putString(Util.empty);
                        }
                        i++;
                    }
                }
            } else if (typ == 1) {
                this.mbuf.putByte((byte) 2);
                this.mbuf.putInt(0);
            } else if (typ == 13) {
                byte[] blob = this.rbuf.getString();
                byte[] data = this.rbuf.getString();
                this.rbuf.getInt();
                Vector identities2 = irepo.getIdentities();
                Identity identity3 = null;
                synchronized (identities2) {
                    while (true) {
                        if (i >= identities2.size()) {
                            break;
                        }
                        Identity _identity2 = (Identity) identities2.elementAt(i);
                        if (_identity2.getPublicKeyBlob() != null && Util.array_equals(blob, _identity2.getPublicKeyBlob())) {
                            if (!_identity2.isEncrypted()) {
                                _identity = _identity2;
                            } else if (userinfo != null) {
                                while (true) {
                                    if (!_identity2.isEncrypted()) {
                                        _identity = _identity2;
                                        break;
                                    }
                                    if (!userinfo.promptPassphrase("Passphrase for " + _identity2.getName())) {
                                        _identity = _identity2;
                                        break;
                                    }
                                    String _passphrase = userinfo.getPassphrase();
                                    if (_passphrase == null) {
                                        _identity = _identity2;
                                        break;
                                    }
                                    byte[] passphrase = Util.str2byte(_passphrase);
                                    _identity = _identity2;
                                    try {
                                        if (_identity.setPassphrase(passphrase)) {
                                            break;
                                        }
                                        _identity2 = _identity;
                                    } catch (JSchException e) {
                                    }
                                }
                            }
                            if (!_identity.isEncrypted()) {
                                identity3 = _identity;
                                break;
                            }
                        }
                        i++;
                    }
                }
                byte[] signature = null;
                if (identity3 != null) {
                    signature = identity3.getSignature(data);
                }
                if (signature == null) {
                    this.mbuf.putByte((byte) 30);
                } else {
                    this.mbuf.putByte((byte) 14);
                    this.mbuf.putString(signature);
                }
            } else if (typ == 18) {
                byte[] blob2 = this.rbuf.getString();
                irepo.remove(blob2);
                this.mbuf.putByte((byte) 6);
            } else if (typ == 9) {
                this.mbuf.putByte((byte) 6);
            } else if (typ == 19) {
                irepo.removeAll();
                this.mbuf.putByte((byte) 6);
            } else if (typ == 17) {
                int fooo = this.rbuf.getLength();
                byte[] tmp = new byte[fooo];
                this.rbuf.getByte(tmp);
                boolean result = irepo.add(tmp);
                this.mbuf.putByte(result ? (byte) 6 : (byte) 5);
            } else {
                Buffer buffer2 = this.rbuf;
                buffer2.skip(buffer2.getLength() - 1);
                this.mbuf.putByte((byte) 5);
            }
            byte[] response = new byte[this.mbuf.getLength()];
            this.mbuf.getByte(response);
            send(response);
        } catch (JSchException e2) {
            throw new IOException(e2.toString());
        }
    }

    private void send(byte[] message) {
        this.packet.reset();
        this.wbuf.putByte((byte) 94);
        this.wbuf.putInt(this.recipient);
        this.wbuf.putInt(message.length + 4);
        this.wbuf.putString(message);
        try {
            getSession().write(this.packet, this, message.length + 4);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void eof_remote() {
        super.eof_remote();
        eof();
    }
}
