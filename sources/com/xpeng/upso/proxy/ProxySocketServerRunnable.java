package com.xpeng.upso.proxy;

import com.xpeng.upso.utils.LogUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
@Deprecated
/* loaded from: classes2.dex */
public class ProxySocketServerRunnable implements Runnable {
    private static final String TAG = "Upso-SecretSocketS";
    private volatile boolean isShutdown = false;
    public String name;
    private ProxyParamWrapper paramWrapper;
    protected int port;
    private ServerSocket serverSocket;

    public ProxySocketServerRunnable(int port, String serverName, ProxyParamWrapper wrapper) {
        this.port = port;
        this.name = serverName;
        this.paramWrapper = wrapper;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.isShutdown = false;
        while (!this.isShutdown) {
            if (!createServerSocket()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
            } else {
                LogUtils.i(TAG, "Server is Running, Waiting for new connections, port = " + ProxyConstants.SERVER_PORT);
                while (!this.isShutdown && listeningLoop()) {
                }
            }
        }
        close();
    }

    public synchronized void shutdown() {
        LogUtils.d(TAG, "shutdown");
        this.isShutdown = true;
    }

    private void close() {
        ServerSocket serverSocket = this.serverSocket;
        if (serverSocket == null) {
            return;
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
        this.serverSocket = null;
    }

    private boolean createServerSocket() {
        boolean serverOk = false;
        try {
            this.serverSocket = new ServerSocket(this.port);
            serverOk = true;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
        if (!serverOk) {
            LogUtils.e(TAG, "create server socket exception, retrying....");
            return false;
        }
        return true;
    }

    private boolean listeningLoop() {
        boolean acceptError;
        Socket sock = null;
        try {
            sock = this.serverSocket.accept();
            LogUtils.i(TAG, "*** New connection from " + sock.getInetAddress() + ":" + sock.getPort() + "***");
            acceptError = false;
        } catch (IOException e) {
            e.printStackTrace();
            acceptError = true;
            LogUtils.e(TAG, e.toString());
        }
        if (acceptError) {
            LogUtils.e(TAG, "server sock error, reCreate");
            close();
            return false;
        }
        ProxySocketClientRunnable newClient = new ProxySocketClientRunnable(0, this.paramWrapper, sock);
        ProxyThreadPool.getInstance().getClientPool().execute(newClient);
        return true;
    }
}
