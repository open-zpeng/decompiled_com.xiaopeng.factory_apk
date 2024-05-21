package com.jcraft.jsch;
/* loaded from: classes.dex */
class UserAuthKeyboardInteractive extends UserAuth {
    UserAuthKeyboardInteractive() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00dc, code lost:
        r23.buf.getInt();
        r23.buf.getByte();
        r23.buf.getByte();
        r4 = r23.buf.getString();
        r7 = r23.buf.getByte();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00f7, code lost:
        if (r7 != 0) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00f9, code lost:
        if (r6 == false) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00fb, code lost:
        return r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00fc, code lost:
        r24.auth_failures++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0102, code lost:
        if (r13 != false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0104, code lost:
        r4 = r12;
        r5 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x010d, code lost:
        throw new com.jcraft.jsch.JSchAuthCancelException("keyboard-interactive");
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0117, code lost:
        throw new com.jcraft.jsch.JSchPartialAuthException(com.jcraft.jsch.Util.byte2str(r4));
     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jcraft.jsch.UserAuth
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean start(com.jcraft.jsch.Session r24) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 567
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.UserAuthKeyboardInteractive.start(com.jcraft.jsch.Session):boolean");
    }
}
