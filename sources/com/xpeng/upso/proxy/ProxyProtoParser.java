package com.xpeng.upso.proxy;

import com.xpeng.upso.proxy.PSOProtocol;
import com.xpeng.upso.utils.LogUtils;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class ProxyProtoParser {
    private static final int BUFFER_LENTGH = 2097152;
    private static final String TAG = "Upso-Parser";
    private static final byte[] receivedBuffer = new byte[2097152];
    private static int offset = 0;
    private int receiveCont = 0;
    private IProtoParseCallback mIProtoParseCallback = null;

    /* loaded from: classes2.dex */
    public interface IProtoParseCallback {
        void onProtoParseSuccess(PSOProtocol.RequestResponse requestResponse);
    }

    public ProxyProtoParser() {
        Arrays.fill(receivedBuffer, (byte) 0);
        offset = 0;
    }

    public void setmProtoParseCallback(IProtoParseCallback callback) {
        this.mIProtoParseCallback = callback;
    }

    public void parseFrom(Socket socket) {
        if (socket == null || socket.isClosed()) {
            return;
        }
        try {
            InputStream inputStream = socket.getInputStream();
            if (inputStream.available() <= 0) {
                return;
            }
            try {
                Thread.sleep(200L);
            } catch (Exception e) {
            }
            int num = inputStream.available();
            inputStream.read(receivedBuffer, offset, num);
            offset += num;
            this.receiveCont += num;
            byte[] praseBuffer = new byte[this.receiveCont];
            for (int i = 0; i < this.receiveCont; i++) {
                praseBuffer[i] = receivedBuffer[i];
            }
            LogUtils.d(TAG, "parseFrom: received data length is: " + num);
            PSOProtocol.RequestResponse requestResponse = PSOProtocol.RequestResponse.parseFrom(praseBuffer);
            if (requestResponse == null) {
                LogUtils.e(TAG, "parseFrom: protobuf parse request reponse packet fail");
                return;
            }
            LogUtils.d(TAG, "parseFrom: parse success");
            this.receiveCont = 0;
            Arrays.fill(receivedBuffer, (byte) 0);
            offset = 0;
            processProtobufPacket(requestResponse);
        } catch (Exception e2) {
            e2.printStackTrace();
            LogUtils.e(TAG, e2.toString());
        }
    }

    private void processProtobufPacket(PSOProtocol.RequestResponse requestResponse) {
        if (requestResponse == null) {
            LogUtils.e(TAG, "processProtobufPacket: requestResponse fail");
            return;
        }
        IProtoParseCallback iProtoParseCallback = this.mIProtoParseCallback;
        if (iProtoParseCallback != null) {
            iProtoParseCallback.onProtoParseSuccess(requestResponse);
        }
    }
}
