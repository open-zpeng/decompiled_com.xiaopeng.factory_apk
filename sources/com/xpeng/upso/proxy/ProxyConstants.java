package com.xpeng.upso.proxy;
/* loaded from: classes2.dex */
public class ProxyConstants {
    public static final String ALIAS_PLAIN_POSTFIX = "sym_keys_";
    public static final String ALIAS_PLAIN_PREFIX = "xpeng_alias_";
    public static final int CLIENT_THREAD_KEEP_ALIVE_TIME = 60000;
    public static final int CLIENT_THREAD_POOL_SIZE = 100;
    public static int SERVER_PORT = 8765;
    public static final int SERVER_THREAD_POOL_SIZE = 1;
    public static final int SOCKET_TIMEOUT = 30000;

    public static void setServerPort(int port) {
        SERVER_PORT = port;
    }
}
