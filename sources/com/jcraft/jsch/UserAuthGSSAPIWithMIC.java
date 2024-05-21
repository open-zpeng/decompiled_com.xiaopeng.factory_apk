package com.jcraft.jsch;
/* loaded from: classes.dex */
public class UserAuthGSSAPIWithMIC extends UserAuth {
    private static final int SSH_MSG_USERAUTH_GSSAPI_ERROR = 64;
    private static final int SSH_MSG_USERAUTH_GSSAPI_ERRTOK = 65;
    private static final int SSH_MSG_USERAUTH_GSSAPI_EXCHANGE_COMPLETE = 63;
    private static final int SSH_MSG_USERAUTH_GSSAPI_MIC = 66;
    private static final int SSH_MSG_USERAUTH_GSSAPI_RESPONSE = 60;
    private static final int SSH_MSG_USERAUTH_GSSAPI_TOKEN = 61;
    private static final byte[][] supported_oid = {new byte[]{6, 9, 42, -122, 72, -122, -9, 18, 1, 2, 2}};
    private static final String[] supported_method = {"gssapi-with-mic.krb5"};

    @Override // com.jcraft.jsch.UserAuth
    public boolean start(Session session) throws Exception {
        super.start(session);
        byte[] _username = Util.str2byte(this.username);
        this.packet.reset();
        this.buf.putByte((byte) 50);
        this.buf.putString(_username);
        this.buf.putString(Util.str2byte("ssh-connection"));
        this.buf.putString(Util.str2byte("gssapi-with-mic"));
        this.buf.putInt(supported_oid.length);
        for (int i = 0; i < supported_oid.length; i++) {
            this.buf.putString(supported_oid[i]);
        }
        session.write(this.packet);
        String method = null;
        while (true) {
            this.buf = session.read(this.buf);
            int command = this.buf.getCommand() & 255;
            if (command == 51) {
                return false;
            }
            if (command == 60) {
                this.buf.getInt();
                this.buf.getByte();
                this.buf.getByte();
                byte[] message = this.buf.getString();
                int i2 = 0;
                while (true) {
                    byte[][] bArr = supported_oid;
                    if (i2 < bArr.length) {
                        if (!Util.array_equals(message, bArr[i2])) {
                            i2++;
                        } else {
                            method = supported_method[i2];
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (method == null) {
                    return false;
                }
                try {
                    Class c = Class.forName(session.getConfig(method));
                    GSSContext context = (GSSContext) c.newInstance();
                    try {
                        context.create(this.username, session.host);
                        byte[] token = new byte[0];
                        while (!context.isEstablished()) {
                            try {
                                token = context.init(token, 0, token.length);
                                if (token != null) {
                                    this.packet.reset();
                                    this.buf.putByte((byte) 61);
                                    this.buf.putString(token);
                                    session.write(this.packet);
                                }
                                if (!context.isEstablished()) {
                                    this.buf = session.read(this.buf);
                                    int command2 = this.buf.getCommand() & 255;
                                    if (command2 == 64) {
                                        this.buf = session.read(this.buf);
                                        command2 = this.buf.getCommand() & 255;
                                    } else if (command2 == 65) {
                                        this.buf = session.read(this.buf);
                                        command2 = this.buf.getCommand() & 255;
                                    }
                                    if (command2 == 51) {
                                        return false;
                                    }
                                    this.buf.getInt();
                                    this.buf.getByte();
                                    this.buf.getByte();
                                    token = this.buf.getString();
                                }
                            } catch (JSchException e) {
                                return false;
                            }
                        }
                        Buffer mbuf = new Buffer();
                        mbuf.putString(session.getSessionId());
                        mbuf.putByte((byte) 50);
                        mbuf.putString(_username);
                        mbuf.putString(Util.str2byte("ssh-connection"));
                        mbuf.putString(Util.str2byte("gssapi-with-mic"));
                        byte[] mic = context.getMIC(mbuf.buffer, 0, mbuf.getLength());
                        if (mic == null) {
                            return false;
                        }
                        this.packet.reset();
                        this.buf.putByte((byte) 66);
                        this.buf.putString(mic);
                        session.write(this.packet);
                        context.dispose();
                        this.buf = session.read(this.buf);
                        int command3 = this.buf.getCommand() & 255;
                        if (command3 == 52) {
                            return true;
                        }
                        if (command3 == 51) {
                            this.buf.getInt();
                            this.buf.getByte();
                            this.buf.getByte();
                            byte[] foo = this.buf.getString();
                            int partial_success = this.buf.getByte();
                            if (partial_success != 0) {
                                throw new JSchPartialAuthException(Util.byte2str(foo));
                            }
                        }
                        return false;
                    } catch (JSchException e2) {
                        return false;
                    }
                } catch (Exception e3) {
                    return false;
                }
            } else if (command != 53) {
                return false;
            } else {
                this.buf.getInt();
                this.buf.getByte();
                this.buf.getByte();
                byte[] _message = this.buf.getString();
                this.buf.getString();
                String message2 = Util.byte2str(_message);
                if (this.userinfo != null) {
                    this.userinfo.showMessage(message2);
                }
            }
        }
    }
}
