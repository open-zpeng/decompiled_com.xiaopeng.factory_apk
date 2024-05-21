package com.jcraft.jsch;
/* loaded from: classes.dex */
public class SftpException extends Exception {
    private Throwable cause;
    public int id;

    public SftpException(int id, String message) {
        super(message);
        this.cause = null;
        this.id = id;
    }

    public SftpException(int id, String message, Throwable e) {
        super(message);
        this.cause = null;
        this.id = id;
        this.cause = e;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return this.id + ": " + getMessage();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
