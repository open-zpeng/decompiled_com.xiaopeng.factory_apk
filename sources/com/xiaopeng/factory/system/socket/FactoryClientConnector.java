package com.xiaopeng.factory.system.socket;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes2.dex */
public class FactoryClientConnector {
    private static final String FACTORY_SOCKET_NAME = "com.xiaopeng.factorysocket";
    private static final String TAG = "FactoryClientConnector";
    private static final String TAG_RECEIVE_DATA = "data";
    private static final int WHAT_RECEIVE_DATA = 100;
    private static FactoryClientConnector sFactoryClientConnector;
    private LocalSocket mFactoryClientSocket;
    Handler mHandler = new Handler() { // from class: com.xiaopeng.factory.system.socket.FactoryClientConnector.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100 && FactoryClientConnector.this.mListener != null) {
                FactoryClientConnector.this.mListener.onReceiveData(msg.getData().getByteArray("data"));
            }
        }
    };
    private ReceiverListener mListener;

    /* loaded from: classes2.dex */
    public interface ReceiverListener {
        void onReceiveData(byte[] bArr);
    }

    public static synchronized FactoryClientConnector getInstance() {
        FactoryClientConnector factoryClientConnector;
        synchronized (FactoryClientConnector.class) {
            if (sFactoryClientConnector == null) {
                sFactoryClientConnector = new FactoryClientConnector();
            }
            factoryClientConnector = sFactoryClientConnector;
        }
        return factoryClientConnector;
    }

    public void setReceiverListener(ReceiverListener listener) {
        this.mListener = listener;
    }

    public void creatConnection() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.system.socket.FactoryClientConnector.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    FactoryClientConnector.this.connectFactorySocket();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectFactorySocket() throws IOException {
        LogUtils.i(TAG, "connect factory socket");
        if (this.mFactoryClientSocket == null) {
            this.mFactoryClientSocket = new LocalSocket();
        }
        if (!this.mFactoryClientSocket.isConnected()) {
            LocalSocketAddress address = new LocalSocketAddress(FACTORY_SOCKET_NAME, LocalSocketAddress.Namespace.ABSTRACT);
            this.mFactoryClientSocket.connect(address);
        }
        InputStream inputStream = this.mFactoryClientSocket.getInputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int len = inputStream.read(buffer);
            if (len != -1) {
                new String(buffer, 0, len);
                Message message = new Message();
                message.what = 100;
                Bundle bundle = new Bundle();
                bundle.putByteArray("data", buffer);
                message.setData(bundle);
                this.mHandler.sendMessage(message);
            } else {
                return;
            }
        }
    }

    public synchronized void send(byte[] buffer) {
        try {
            LogUtils.i(TAG, "send to factory server buffer:" + DataHelp.byteArrayToHexStr(buffer, " "));
            if (this.mFactoryClientSocket != null && this.mFactoryClientSocket.isConnected()) {
                OutputStream outputStream = this.mFactoryClientSocket.getOutputStream();
                outputStream.write(buffer);
                LogUtils.i(TAG, "buffer sent");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            LogUtils.i(TAG, "disconnect factory socket");
            if (this.mFactoryClientSocket != null) {
                this.mFactoryClientSocket.shutdownInput();
                this.mFactoryClientSocket.shutdownOutput();
                this.mFactoryClientSocket.close();
                this.mFactoryClientSocket = null;
            }
            this.mListener = null;
            sFactoryClientConnector = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
