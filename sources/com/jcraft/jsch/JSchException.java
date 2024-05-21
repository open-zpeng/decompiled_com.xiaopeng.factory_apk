package com.jcraft.jsch;
/* loaded from: classes.dex */
public class JSchException extends Exception {
    private Throwable cause;

    public JSchException() {
        this.cause = null;
    }

    public JSchException(String s) {
        super(s);
        this.cause = null;
    }

    public JSchException(String s, Throwable e) {
        super(s);
        this.cause = null;
        this.cause = e;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
