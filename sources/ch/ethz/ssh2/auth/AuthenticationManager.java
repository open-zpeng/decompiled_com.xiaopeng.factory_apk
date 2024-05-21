package ch.ethz.ssh2.auth;

import ch.ethz.ssh2.InteractiveCallback;
import ch.ethz.ssh2.crypto.PEMDecoder;
import ch.ethz.ssh2.packets.PacketServiceAccept;
import ch.ethz.ssh2.packets.PacketServiceRequest;
import ch.ethz.ssh2.packets.PacketUserauthBanner;
import ch.ethz.ssh2.packets.PacketUserauthFailure;
import ch.ethz.ssh2.packets.PacketUserauthInfoRequest;
import ch.ethz.ssh2.packets.PacketUserauthInfoResponse;
import ch.ethz.ssh2.packets.PacketUserauthRequestInteractive;
import ch.ethz.ssh2.packets.PacketUserauthRequestNone;
import ch.ethz.ssh2.packets.PacketUserauthRequestPassword;
import ch.ethz.ssh2.packets.PacketUserauthRequestPublicKey;
import ch.ethz.ssh2.packets.TypesWriter;
import ch.ethz.ssh2.signature.DSAPrivateKey;
import ch.ethz.ssh2.signature.DSASHA1Verify;
import ch.ethz.ssh2.signature.DSASignature;
import ch.ethz.ssh2.signature.RSAPrivateKey;
import ch.ethz.ssh2.signature.RSASHA1Verify;
import ch.ethz.ssh2.signature.RSASignature;
import ch.ethz.ssh2.transport.MessageHandler;
import ch.ethz.ssh2.transport.TransportManager;
import com.xiaopeng.commonfunc.Constant;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Vector;
/* loaded from: classes.dex */
public class AuthenticationManager implements MessageHandler {
    String banner;
    TransportManager tm;
    Vector packets = new Vector();
    boolean connectionClosed = false;
    String[] remainingMethods = null;
    boolean isPartialSuccess = false;
    boolean authenticated = false;
    boolean initDone = false;

    public AuthenticationManager(TransportManager tm) {
        this.tm = tm;
    }

    boolean methodPossible(String methName) {
        if (this.remainingMethods == null) {
            return false;
        }
        int i = 0;
        while (true) {
            String[] strArr = this.remainingMethods;
            if (i >= strArr.length) {
                return false;
            }
            if (strArr[i].compareTo(methName) != 0) {
                i++;
            } else {
                return true;
            }
        }
    }

    byte[] deQueue() throws IOException {
        synchronized (this.packets) {
            while (this.packets.size() == 0) {
                try {
                    try {
                        if (this.connectionClosed) {
                            throw ((IOException) new IOException("The connection is closed.").initCause(this.tm.getReasonClosedCause()));
                        }
                        try {
                            this.packets.wait();
                        } catch (InterruptedException e) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
            byte[] res = (byte[]) this.packets.firstElement();
            this.packets.removeElementAt(0);
            return res;
        }
    }

    byte[] getNextMessage() throws IOException {
        while (true) {
            byte[] msg = deQueue();
            if (msg[0] != 53) {
                return msg;
            }
            PacketUserauthBanner sb = new PacketUserauthBanner(msg, 0, msg.length);
            this.banner = sb.getBanner();
        }
    }

    public String[] getRemainingMethods(String user) throws IOException {
        initialize(user);
        return this.remainingMethods;
    }

    public boolean getPartialSuccess() {
        return this.isPartialSuccess;
    }

    private boolean initialize(String user) throws IOException {
        if (!this.initDone) {
            this.tm.registerMessageHandler(this, 0, 255);
            PacketServiceRequest sr = new PacketServiceRequest("ssh-userauth");
            this.tm.sendMessage(sr.getPayload());
            PacketUserauthRequestNone urn = new PacketUserauthRequestNone("ssh-connection", user);
            this.tm.sendMessage(urn.getPayload());
            byte[] msg = getNextMessage();
            new PacketServiceAccept(msg, 0, msg.length);
            byte[] msg2 = getNextMessage();
            this.initDone = true;
            if (msg2[0] == 52) {
                this.authenticated = true;
                return true;
            } else if (msg2[0] == 51) {
                PacketUserauthFailure puf = new PacketUserauthFailure(msg2, 0, msg2.length);
                this.remainingMethods = puf.getAuthThatCanContinue();
                this.isPartialSuccess = puf.isPartialSuccess();
                return false;
            } else {
                StringBuffer stringBuffer = new StringBuffer("Unexpected SSH message (type ");
                stringBuffer.append((int) msg2[0]);
                stringBuffer.append(")");
                throw new IOException(stringBuffer.toString());
            }
        }
        return this.authenticated;
    }

    public boolean authenticatePublicKey(String user, char[] PEMPrivateKey, String password, SecureRandom rnd) throws IOException {
        try {
            initialize(user);
            if (!methodPossible("publickey")) {
                throw new IOException("Authentication method publickey not supported by the server at this stage.");
            }
            Object key = PEMDecoder.decode(PEMPrivateKey, password);
            if (key instanceof DSAPrivateKey) {
                DSAPrivateKey pk = (DSAPrivateKey) key;
                byte[] pk_enc = DSASHA1Verify.encodeSSHDSAPublicKey(pk.getPublicKey());
                TypesWriter tw = new TypesWriter();
                byte[] H = this.tm.getSessionIdentifier();
                tw.writeString(H, 0, H.length);
                tw.writeByte(50);
                tw.writeString(user);
                tw.writeString("ssh-connection");
                tw.writeString("publickey");
                tw.writeBoolean(true);
                tw.writeString("ssh-dss");
                tw.writeString(pk_enc, 0, pk_enc.length);
                byte[] msg = tw.getBytes();
                DSASignature ds = DSASHA1Verify.generateSignature(msg, pk, rnd);
                byte[] ds_enc = DSASHA1Verify.encodeSSHDSASignature(ds);
                PacketUserauthRequestPublicKey ua = new PacketUserauthRequestPublicKey("ssh-connection", user, "ssh-dss", pk_enc, ds_enc);
                this.tm.sendMessage(ua.getPayload());
            } else if (key instanceof RSAPrivateKey) {
                RSAPrivateKey pk2 = (RSAPrivateKey) key;
                byte[] pk_enc2 = RSASHA1Verify.encodeSSHRSAPublicKey(pk2.getPublicKey());
                TypesWriter tw2 = new TypesWriter();
                byte[] H2 = this.tm.getSessionIdentifier();
                tw2.writeString(H2, 0, H2.length);
                tw2.writeByte(50);
                tw2.writeString(user);
                tw2.writeString("ssh-connection");
                tw2.writeString("publickey");
                tw2.writeBoolean(true);
                tw2.writeString("ssh-rsa");
                tw2.writeString(pk_enc2, 0, pk_enc2.length);
                byte[] msg2 = tw2.getBytes();
                RSASignature ds2 = RSASHA1Verify.generateSignature(msg2, pk2);
                byte[] rsa_sig_enc = RSASHA1Verify.encodeSSHRSASignature(ds2);
                PacketUserauthRequestPublicKey ua2 = new PacketUserauthRequestPublicKey("ssh-connection", user, "ssh-rsa", pk_enc2, rsa_sig_enc);
                this.tm.sendMessage(ua2.getPayload());
            } else {
                throw new IOException("Unknown private key type returned by the PEM decoder.");
            }
            byte[] ar = getNextMessage();
            if (ar[0] == 52) {
                this.authenticated = true;
                this.tm.removeMessageHandler(this, 0, 255);
                return true;
            } else if (ar[0] == 51) {
                PacketUserauthFailure puf = new PacketUserauthFailure(ar, 0, ar.length);
                this.remainingMethods = puf.getAuthThatCanContinue();
                this.isPartialSuccess = puf.isPartialSuccess();
                return false;
            } else {
                StringBuffer stringBuffer = new StringBuffer("Unexpected SSH message (type ");
                stringBuffer.append((int) ar[0]);
                stringBuffer.append(")");
                throw new IOException(stringBuffer.toString());
            }
        } catch (IOException e) {
            this.tm.close(e, false);
            throw ((IOException) new IOException("Publickey authentication failed.").initCause(e));
        }
    }

    public boolean authenticatePassword(String user, String pass) throws IOException {
        try {
            initialize(user);
            if (!methodPossible(Constant.HTTP_KEY_PASSWORD)) {
                throw new IOException("Authentication method password not supported by the server at this stage.");
            }
            PacketUserauthRequestPassword ua = new PacketUserauthRequestPassword("ssh-connection", user, pass);
            this.tm.sendMessage(ua.getPayload());
            byte[] ar = getNextMessage();
            if (ar[0] == 52) {
                this.authenticated = true;
                this.tm.removeMessageHandler(this, 0, 255);
                return true;
            } else if (ar[0] != 51) {
                StringBuffer stringBuffer = new StringBuffer("Unexpected SSH message (type ");
                stringBuffer.append((int) ar[0]);
                stringBuffer.append(")");
                throw new IOException(stringBuffer.toString());
            } else {
                PacketUserauthFailure puf = new PacketUserauthFailure(ar, 0, ar.length);
                this.remainingMethods = puf.getAuthThatCanContinue();
                this.isPartialSuccess = puf.isPartialSuccess();
                return false;
            }
        } catch (IOException e) {
            this.tm.close(e, false);
            throw ((IOException) new IOException("Password authentication failed.").initCause(e));
        }
    }

    public boolean authenticateInteractive(String user, String[] submethods, InteractiveCallback cb) throws IOException {
        try {
            initialize(user);
            if (!methodPossible("keyboard-interactive")) {
                throw new IOException("Authentication method keyboard-interactive not supported by the server at this stage.");
            }
            if (submethods == null) {
                submethods = new String[0];
            }
            PacketUserauthRequestInteractive ua = new PacketUserauthRequestInteractive("ssh-connection", user, submethods);
            this.tm.sendMessage(ua.getPayload());
            while (true) {
                byte[] ar = getNextMessage();
                if (ar[0] == 52) {
                    this.authenticated = true;
                    this.tm.removeMessageHandler(this, 0, 255);
                    return true;
                } else if (ar[0] == 51) {
                    PacketUserauthFailure puf = new PacketUserauthFailure(ar, 0, ar.length);
                    this.remainingMethods = puf.getAuthThatCanContinue();
                    this.isPartialSuccess = puf.isPartialSuccess();
                    return false;
                } else if (ar[0] != 60) {
                    StringBuffer stringBuffer = new StringBuffer("Unexpected SSH message (type ");
                    stringBuffer.append((int) ar[0]);
                    stringBuffer.append(")");
                    throw new IOException(stringBuffer.toString());
                } else {
                    PacketUserauthInfoRequest pui = new PacketUserauthInfoRequest(ar, 0, ar.length);
                    try {
                        String[] responses = cb.replyToChallenge(pui.getName(), pui.getInstruction(), pui.getNumPrompts(), pui.getPrompt(), pui.getEcho());
                        if (responses == null) {
                            throw new IOException("Your callback may not return NULL!");
                        }
                        PacketUserauthInfoResponse puir = new PacketUserauthInfoResponse(responses);
                        this.tm.sendMessage(puir.getPayload());
                    } catch (Exception e) {
                        throw ((IOException) new IOException("Exception in callback.").initCause(e));
                    }
                }
            }
        } catch (IOException e2) {
            this.tm.close(e2, false);
            throw ((IOException) new IOException("Keyboard-interactive authentication failed.").initCause(e2));
        }
    }

    @Override // ch.ethz.ssh2.transport.MessageHandler
    public void handleMessage(byte[] msg, int msglen) throws IOException {
        synchronized (this.packets) {
            if (msg == null) {
                this.connectionClosed = true;
            } else {
                byte[] tmp = new byte[msglen];
                System.arraycopy(msg, 0, tmp, 0, msglen);
                this.packets.addElement(tmp);
            }
            this.packets.notifyAll();
            if (this.packets.size() > 5) {
                this.connectionClosed = true;
                throw new IOException("Error, peer is flooding us with authentication packets.");
            }
        }
    }
}
