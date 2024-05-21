package com.jcraft.jsch;
/* loaded from: classes.dex */
class UserAuthPassword extends UserAuth {
    private final int SSH_MSG_USERAUTH_PASSWD_CHANGEREQ = 60;

    UserAuthPassword() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:?, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:?, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x01b0, code lost:
        if (r19.userinfo == null) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01b2, code lost:
        r19.userinfo.showMessage("Password must be changed.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x01ba, code lost:
        if (r3 == null) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01bc, code lost:
        com.jcraft.jsch.Util.bzero(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x01c0, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x01c5, code lost:
        if (r15 != 51) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x01c7, code lost:
        r19.buf.getInt();
        r19.buf.getByte();
        r19.buf.getByte();
        r4 = r19.buf.getString();
        r5 = r19.buf.getByte();
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01e2, code lost:
        if (r5 != 0) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01e4, code lost:
        r20.auth_failures++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01ea, code lost:
        if (r3 == null) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01ec, code lost:
        com.jcraft.jsch.Util.bzero(r3);
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01fb, code lost:
        throw new com.jcraft.jsch.JSchPartialAuthException(com.jcraft.jsch.Util.byte2str(r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01fd, code lost:
        if (r3 == null) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01ff, code lost:
        com.jcraft.jsch.Util.bzero(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0203, code lost:
        return false;
     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jcraft.jsch.UserAuth
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean start(com.jcraft.jsch.Session r20) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 525
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.UserAuthPassword.start(com.jcraft.jsch.Session):boolean");
    }
}
