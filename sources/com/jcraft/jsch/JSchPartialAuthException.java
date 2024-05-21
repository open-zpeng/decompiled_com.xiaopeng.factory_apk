package com.jcraft.jsch;
/* loaded from: classes.dex */
class JSchPartialAuthException extends JSchException {
    String methods;

    public JSchPartialAuthException() {
    }

    public JSchPartialAuthException(String s) {
        super(s);
        this.methods = s;
    }

    public String getMethods() {
        return this.methods;
    }
}
