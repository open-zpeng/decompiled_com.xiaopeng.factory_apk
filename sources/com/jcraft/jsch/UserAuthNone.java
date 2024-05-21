package com.jcraft.jsch;

import com.xiaopeng.libconfig.ipc.AccountConfig;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UserAuthNone extends UserAuth {
    private static final int SSH_MSG_SERVICE_ACCEPT = 6;
    private String methods = null;

    UserAuthNone() {
    }

    @Override // com.jcraft.jsch.UserAuth
    public boolean start(Session session) throws Exception {
        super.start(session);
        this.packet.reset();
        this.buf.putByte((byte) 5);
        this.buf.putString(Util.str2byte("ssh-userauth"));
        session.write(this.packet);
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "SSH_MSG_SERVICE_REQUEST sent");
        }
        this.buf = session.read(this.buf);
        boolean result = this.buf.getCommand() == 6;
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "SSH_MSG_SERVICE_ACCEPT received");
        }
        if (!result) {
            return false;
        }
        byte[] _username = Util.str2byte(this.username);
        this.packet.reset();
        this.buf.putByte((byte) 50);
        this.buf.putString(_username);
        this.buf.putString(Util.str2byte("ssh-connection"));
        this.buf.putString(Util.str2byte(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE));
        session.write(this.packet);
        while (true) {
            this.buf = session.read(this.buf);
            int command = this.buf.getCommand() & 255;
            if (command == 52) {
                return true;
            }
            if (command == 53) {
                this.buf.getInt();
                this.buf.getByte();
                this.buf.getByte();
                byte[] _message = this.buf.getString();
                this.buf.getString();
                String message = Util.byte2str(_message);
                if (this.userinfo != null) {
                    try {
                        this.userinfo.showMessage(message);
                    } catch (RuntimeException e) {
                    }
                }
            } else if (command == 51) {
                this.buf.getInt();
                this.buf.getByte();
                this.buf.getByte();
                byte[] foo = this.buf.getString();
                this.buf.getByte();
                this.methods = Util.byte2str(foo);
                return false;
            } else {
                throw new JSchException("USERAUTH fail (" + command + ")");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getMethods() {
        return this.methods;
    }
}
