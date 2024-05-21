package com.xpeng.upso.proxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class ProxyThreadPool {
    private static ExecutorService clientPool;
    private static ProxyThreadPool sInstance;
    private static ExecutorService serverPool;

    private ProxyThreadPool() {
        checkInit();
    }

    private void checkInit() {
        if (serverPool == null) {
            serverPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        }
        if (clientPool == null) {
            clientPool = new ThreadPoolExecutor(100, 100, 60000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        }
    }

    public static synchronized ProxyThreadPool getInstance() {
        ProxyThreadPool proxyThreadPool;
        synchronized (ProxyThreadPool.class) {
            if (sInstance == null) {
                sInstance = new ProxyThreadPool();
            }
            proxyThreadPool = sInstance;
        }
        return proxyThreadPool;
    }

    public ExecutorService getServicePool() {
        return serverPool;
    }

    public ExecutorService getClientPool() {
        return clientPool;
    }

    public static void shutdown() {
        serverPool.shutdown();
        clientPool.shutdown();
        serverPool = null;
        clientPool = null;
    }
}
