package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface Logger {
    public static final int DEBUG = 0;
    public static final int ERROR = 3;
    public static final int FATAL = 4;
    public static final int INFO = 1;
    public static final int WARN = 2;

    boolean isEnabled(int i);

    void log(int i, String str);
}
