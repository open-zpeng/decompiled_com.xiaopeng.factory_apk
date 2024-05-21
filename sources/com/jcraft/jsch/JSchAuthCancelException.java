package com.jcraft.jsch;
/* loaded from: classes.dex */
class JSchAuthCancelException extends JSchException {
    String method;

    JSchAuthCancelException() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JSchAuthCancelException(String s) {
        super(s);
        this.method = s;
    }

    public String getMethod() {
        return this.method;
    }
}
