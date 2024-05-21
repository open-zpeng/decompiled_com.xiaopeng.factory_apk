package com.xpeng.upso.selftest.tboxClientTest;

import android.content.Context;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.proxy.ProxyConstants;
import com.xpeng.upso.proxy.ProxyParamWrapper;
import com.xpeng.upso.utils.LogUtils;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
/* loaded from: classes2.dex */
public class TboxClientThread extends Thread {
    public static final String TAG = "Upso-TBOX_CT";
    private XpengCarModel carModel;
    private XpengCarChipModel chipModel;
    private Context context;
    int op;
    private ProxyParamWrapper paramWrapper;
    private Socket socket;
    boolean canceled = false;
    boolean completed = false;

    public TboxClientThread(Context context, int op, ProxyParamWrapper paramIn) {
        this.context = context;
        this.op = op;
        this.paramWrapper = paramIn;
        this.carModel = this.paramWrapper.getCarModel();
        this.chipModel = this.paramWrapper.getChipModel();
    }

    public void cancel() {
        this.canceled = true;
        closeSocket();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        while (true) {
            if (!connect()) {
                LogUtils.d(TAG, "connect timeout, please try again");
                break;
            } else if (!this.canceled) {
                communicate();
                if (!this.canceled) {
                    if (this.completed) {
                        break;
                    }
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        closeSocket();
        if (this.completed) {
            LogUtils.d(TAG, "Thread End! OP OK");
        } else {
            LogUtils.e(TAG, "Thread End! OP failed");
        }
    }

    private boolean connect() {
        int retry = 0;
        while (true) {
            try {
                this.socket = new Socket("127.0.0.1", ProxyConstants.SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (this.socket != null) {
                break;
            }
            sleep(1000);
            int retry2 = retry + 1;
            if (retry > 120) {
                break;
            }
            LogUtils.d(TAG, "connecting ... (" + retry2 + ")");
            retry = retry2;
        }
        return this.socket != null;
    }

    private void communicate() {
        Socket socket = this.socket;
        if (socket == null) {
            LogUtils.e(TAG, "communicate return, socket is not connected");
            return;
        }
        try {
            socket.setSoTimeout(30000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        TboxClientRequestHandler cduClientRequestHandler = new TboxClientRequestHandler(this.context, this.socket, this.carModel, this.chipModel, this.op);
        do {
            cduClientRequestHandler.parseFrom(this.socket);
        } while (!cduClientRequestHandler.isStoped());
        this.completed = true;
        LogUtils.d(TAG, "communication done");
    }

    private synchronized void closeSocket() {
        if (this.socket != null) {
            LogUtils.d(TAG, "clode socket");
            try {
                this.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.socket = null;
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
