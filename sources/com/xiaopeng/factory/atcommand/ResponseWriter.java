package com.xiaopeng.factory.atcommand;

import com.xiaopeng.lib.utils.LogUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
/* loaded from: classes.dex */
public class ResponseWriter {
    private static final String TAG = "ResponseWriter";
    private static boolean mWritable = true;
    private final ConcurrentLinkedQueue<String> mStringQueue;
    private BufferedWriter out;

    public ResponseWriter(Socket mSocket) {
        LogUtils.i(TAG, "Create ResponseWriter");
        this.mStringQueue = new ConcurrentLinkedQueue<>();
        updateSocket(mSocket);
    }

    public synchronized boolean getWritable() {
        return mWritable;
    }

    public synchronized void setWritable(boolean w) {
        LogUtils.i(TAG, "ResponseWriter setWritable = " + w);
        mWritable = w;
    }

    public void updateSocket(Socket mSocket) {
        if (mSocket.isConnected()) {
            try {
                if (this.out != null) {
                    this.out.close();
                }
                this.out = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        LogUtils.i(TAG, "ResponseWriter Socket is closed");
    }

    public synchronized boolean write(String msg) {
        if (this.mStringQueue == null) {
            LogUtils.i(TAG, "ResponseWriter write - Queue is null");
            return false;
        }
        boolean result = false;
        if (msg != null) {
            LogUtils.i(TAG, "write response :" + msg);
            if (getWritable()) {
                while (!this.mStringQueue.isEmpty()) {
                    LogUtils.i(TAG, "ResponseWriter write - poll queue");
                    writeToSocket(this.mStringQueue.poll());
                }
                result = writeToSocket(msg);
            } else {
                LogUtils.i(TAG, "ResponseWriter write - need to queueing");
                result = this.mStringQueue.add(msg);
            }
        }
        return result;
    }

    public boolean writeToSocket(String msg) {
        BufferedWriter bufferedWriter = this.out;
        if (bufferedWriter == null || msg == null) {
            LogUtils.i(TAG, "write", "out is null");
            return false;
        }
        try {
            bufferedWriter.write(msg);
            this.out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        BufferedWriter bufferedWriter = this.out;
        if (bufferedWriter != null) {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.out = null;
        }
    }
}
