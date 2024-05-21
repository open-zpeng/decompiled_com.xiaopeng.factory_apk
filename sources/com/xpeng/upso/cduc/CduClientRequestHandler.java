package com.xpeng.upso.cduc;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.protobuf.InvalidProtocolBufferException;
import com.xiaopeng.lib.security.xmartv1.RandomKeySecurity;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.proxy.EncryptAndDecrypt;
import com.xpeng.upso.proxy.PSOProtocol;
import com.xpeng.upso.proxy.TeeEncrpytedContent;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.HexUtils;
import com.xpeng.upso.utils.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class CduClientRequestHandler implements Handler.Callback {
    private static final int COMMUNICATION_TIMEOUT_MS = 40000;
    private static final int MSG_BASE = 3000;
    private static final int MSG_COMMUNICATION_TIMEOUT = 3002;
    private static final int MSG_TEST_TIMEOUT = 3001;
    private static final String TAG = "Upso-CduCH";
    private XpengCarModel carModel;
    private XpengCarChipModel chipModel;
    private Context context;
    private CduClientCommunicationStatus currentStatus;
    private int op;
    private Socket socket;
    private byte[] receivedBuffer = new byte[2097152];
    private int receiveCont = 0;
    private int sendSequence = 0;
    private boolean closed = false;
    private CduClientPreset cduClientPreset = new CduClientPreset();
    private Handler handler = new Handler(Looper.getMainLooper(), this);

    public CduClientRequestHandler(Context context, Socket socket, XpengCarModel carModel, XpengCarChipModel chipModel, int op) {
        this.context = context;
        this.carModel = carModel;
        this.chipModel = chipModel;
        this.op = op;
        this.socket = socket;
        updateStatus(CduClientCommunicationStatus.CCCS_INIT);
        updateStatus(CduClientCommunicationStatus.CCCS_PB_WAIT_REQ_INFO);
        sendTimeoutMsg();
    }

    public boolean isStoped() {
        return this.closed;
    }

    public void parseFrom(Socket socket) {
        if (socket == null || socket.isClosed()) {
            return;
        }
        PSOProtocol.RequestResponse requestResponse = null;
        byte[] praseBuffer = null;
        try {
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.d(TAG, "socket exception");
            onFailed();
        }
        if (socket.isClosed()) {
            return;
        }
        InputStream inputStream = socket.getInputStream();
        if (inputStream.available() <= 0) {
            return;
        }
        try {
            Thread.sleep(200L);
        } catch (Exception e2) {
        }
        int num = inputStream.available();
        byte[] buf = new byte[num];
        inputStream.read(buf);
        System.arraycopy(buf, 0, this.receivedBuffer, this.receiveCont, num);
        this.receiveCont += num;
        praseBuffer = new byte[this.receiveCont];
        System.arraycopy(this.receivedBuffer, 0, praseBuffer, 0, this.receiveCont);
        LogUtils.d(TAG, "parseFrom: received data length is: " + num);
        if (praseBuffer != null) {
            try {
                requestResponse = PSOProtocol.RequestResponse.parseFrom(praseBuffer);
            } catch (InvalidProtocolBufferException e3) {
                e3.printStackTrace();
            }
        }
        if (requestResponse == null) {
            LogUtils.e(TAG, "parseFrom: protobuf parse request reponse packet fail");
            return;
        }
        LogUtils.d(TAG, "parseFrom: parse success");
        this.receiveCont = 0;
        processProtobufPacket(requestResponse);
    }

    private void processProtobufPacket(PSOProtocol.RequestResponse requestResponse) {
        if (requestResponse == null) {
            LogUtils.e(TAG, "processProtobufPacket: requestResponse fail");
            return;
        }
        int messageType = requestResponse.getMessageTypeValue();
        LogUtils.d(TAG, "processProtobufPacket: message type: " + messageType);
        if (messageType == 0) {
            Log.d(TAG, "processProtobufPacket: XP_INFO_VALUE");
            handleInfo(requestResponse);
        } else if (messageType == 1) {
            Log.d(TAG, "processProtobufPacket: XP_SECRET_PRESET_VALUE");
            handleSecretPreset(requestResponse);
        } else if (messageType == 2) {
            Log.d(TAG, "processProtobufPacket: XP_SECRET_ENCRYPT_AUTH_VALUE");
            handleSecretEncrypt(requestResponse);
        } else if (messageType == 3) {
            Log.d(TAG, "processProtobufPacket: XP_SECRET_DECRYPT_AUTH_VALUE");
            handleSecretDecrypt(requestResponse);
        } else if (messageType == 4) {
            Log.d(TAG, "processProtobufPacket: XP_SECRET_DELETE_VALUE");
            handleSecretDelete(requestResponse);
        }
    }

    public boolean parse(ByteBuffer buffer) {
        PSOProtocol.RequestResponse request = null;
        try {
            request = PSOProtocol.RequestResponse.parseFrom(buffer);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            return false;
        }
        Log.d(TAG, "reve request:" + request);
        int msgType = request.getMessageTypeValue();
        if (msgType != 0) {
            if (msgType == 1) {
                Log.d(TAG, "parse: XP_SECRET_PRESET_VALUE");
                handleSecretPreset(request);
            } else if (msgType == 2) {
                Log.d(TAG, "parse: XP_SECRET_ENCRYPT_AUTH_VALUE");
                handleSecretEncrypt(request);
            } else if (msgType == 3) {
                Log.d(TAG, "parse: XP_SECRET_DECRYPT_AUTH_VALUE");
                handleSecretDecrypt(request);
            } else if (msgType == 4) {
                Log.d(TAG, "parse: XP_SECRET_DELETE_VALUE");
                handleSecretDelete(request);
            }
        } else {
            Log.d(TAG, "parse: XP_INFO_VALUE");
            handleInfo(request);
        }
        return true;
    }

    private String getDecryptResult(byte[] buffer) {
        TeeEncrpytedContent teeEncrpytedContent = new TeeEncrpytedContent();
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        byte[] magicNumberBytes = new byte[2];
        byteBuffer.get(magicNumberBytes);
        teeEncrpytedContent.setMagicNumber(new String(magicNumberBytes));
        int version = byteBuffer.get();
        teeEncrpytedContent.setVersion(Integer.valueOf(version));
        int keyIndex = byteBuffer.get();
        teeEncrpytedContent.setKeyIndex(Integer.valueOf(keyIndex));
        int deviceIdLength = byteBuffer.get();
        byte[] deviceIdBytes = new byte[deviceIdLength];
        byteBuffer.get(deviceIdBytes);
        teeEncrpytedContent.setDeviceId(new String(deviceIdBytes));
        int nonceLength = byteBuffer.get();
        teeEncrpytedContent.setNonceLength(Integer.valueOf(nonceLength));
        long timestamp = byteBuffer.getLong();
        teeEncrpytedContent.setTimestamp(Long.valueOf(timestamp));
        int ivLength = byteBuffer.get();
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        teeEncrpytedContent.setIv(iv);
        byte[] content = new byte[byteBuffer.remaining()];
        byteBuffer.get(content);
        teeEncrpytedContent.setContent(content);
        try {
            try {
                try {
                    String alais = this.cduClientPreset.genAlias(teeEncrpytedContent.getKeyIndex().intValue());
                    String resultString = this.cduClientPreset.decryptAesGcm(alais, teeEncrpytedContent);
                    return resultString;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    private List<String> decryptServerEncryptData(List<String> encryptList) {
        List<String> resultStrings = new ArrayList<>();
        int i = 0;
        for (String encrypt : encryptList) {
            byte[] decodeData = EncryptAndDecrypt.decodeBASE64(encrypt);
            String rawString = getDecryptResult(decodeData);
            if (UpsoConfig.isLogEnabled()) {
                Log.d(TAG, "index " + i + " en : " + encrypt);
                Log.d(TAG, "index " + i + " raw : " + rawString);
            }
            if (rawString == null) {
                break;
            }
            resultStrings.add(rawString);
            i++;
        }
        return resultStrings;
    }

    private boolean handleInfo(PSOProtocol.RequestResponse request) {
        PSOProtocol.IdentityInfo.Builder identityInfoBuilder = PSOProtocol.IdentityInfo.newBuilder().clear();
        identityInfoBuilder.setMask(0);
        identityInfoBuilder.setRole(1);
        identityInfoBuilder.setVersion(2);
        identityInfoBuilder.setDeviceId(getDeviceId());
        identityInfoBuilder.setDeviceType(getDeviceType());
        identityInfoBuilder.setCarType(this.carModel.toString());
        if (this.op == 1) {
            boolean isPresetTemp = LocalCarManager.isPresetTemp();
            StringBuilder sb = new StringBuilder();
            sb.append("preset flow, has secret ");
            int hasSecret = isPresetTemp ? 1 : 0;
            sb.append(hasSecret);
            Log.d(TAG, sb.toString());
            int hasSecret2 = isPresetTemp ? 1 : 0;
            identityInfoBuilder.setHasSecret(hasSecret2);
        } else {
            boolean isPreseted = this.cduClientPreset.isPreseted(this.carModel, this.chipModel);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("verify flow, has secret ");
            int hasSecret3 = isPreseted ? 1 : 0;
            sb2.append(hasSecret3);
            Log.d(TAG, sb2.toString());
            int hasSecret4 = isPreseted ? 1 : 0;
            identityInfoBuilder.setHasSecret(hasSecret4);
        }
        PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_INFO_RESPONSE);
        requestResponseBuilder.setIdentityInfo(identityInfoBuilder.build());
        sendResponse(requestResponseBuilder.build());
        updateStatus(CduClientCommunicationStatus.CCCS_PB_WAIT_PRESET);
        sendTimeoutMsg();
        return true;
    }

    private boolean handleSecretPreset(PSOProtocol.RequestResponse request) {
        removeTimeoutMsg();
        PSOProtocol.SecretPreset getResponse = request.getSecretPreset();
        List<PSOProtocol.Secret> secrets = getResponse.getSecretList();
        int keyCount = 0;
        int index = 0;
        if (secrets != null) {
            keyCount = secrets.size();
            for (PSOProtocol.Secret secret : secrets) {
                try {
                    String alias = this.cduClientPreset.genAlias(secret.getIndex());
                    this.cduClientPreset.delAesKey(alias);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JSONObject root = new JSONObject();
            for (PSOProtocol.Secret secret2 : secrets) {
                try {
                    if (secret2.getType() == PSOProtocol.Secret.SecretType.KEY_TYPE_AES) {
                        JSONObject key = new JSONObject();
                        key.putOpt("index", Long.valueOf(secret2.getIndex()));
                        key.putOpt("key", Base64Util.encodeToString(HexUtils.hexStringToByte(secret2.getSecret())));
                        root.putOpt("" + secret2.getIndex(), key);
                        index++;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            try {
                if (!RandomKeySecurity.getInstance().individualWithData(root.toString())) {
                    index = 0;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                index = 0;
            }
        }
        Log.d(TAG, "preset key size = " + index);
        PSOProtocol.SecretPresetResponse.Builder presetResponseBuilder = PSOProtocol.SecretPresetResponse.newBuilder().clear();
        presetResponseBuilder.setIndex(index);
        PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_PRESET_RESPONSE);
        requestResponseBuilder.setSecretPresetResponse(presetResponseBuilder.build());
        sendResponse(requestResponseBuilder.build());
        if (keyCount <= 0 || index != keyCount) {
            LocalCarManager.setPresetedTemp(false);
            Log.d(TAG, "preset key failed" + String.format("index=%d,keyCount=%d", Integer.valueOf(index), Integer.valueOf(keyCount)));
            onFailed();
        } else {
            LocalCarManager.setPresetedTemp(true);
            updateStatus(CduClientCommunicationStatus.CCCS_PB_WAIT_DECRYPT);
            sendTimeoutMsg();
        }
        return true;
    }

    private boolean handleSecretDecrypt(PSOProtocol.RequestResponse request) {
        removeTimeoutMsg();
        PSOProtocol.SecretAuth secretAuth = request.getSecrettAuth();
        List<String> decryptDataList = null;
        boolean error = false;
        if (secretAuth == null) {
            error = true;
        } else {
            List<String> serverEncryptDataList = secretAuth.getResultList();
            if (serverEncryptDataList == null) {
                error = true;
            }
            int deSize = serverEncryptDataList.size();
            decryptDataList = decryptServerEncryptData(serverEncryptDataList);
            if (decryptDataList == null || decryptDataList.size() < deSize) {
                error = true;
            }
        }
        PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_DECRYPT_AUTH_RESPONSE);
        PSOProtocol.SecretAuth.Builder secretAuthBuilder = PSOProtocol.SecretAuth.newBuilder().clear();
        secretAuthBuilder.setIndex(0);
        secretAuthBuilder.setCont(0);
        if (!error) {
            secretAuthBuilder.setIndex(Integer.MAX_VALUE);
            secretAuthBuilder.setCont(decryptDataList.size());
            for (int i = 0; i < decryptDataList.size(); i++) {
                secretAuthBuilder.addResult(decryptDataList.get(i));
            }
            updateStatus(CduClientCommunicationStatus.CCCS_PB_WAIT_ENCRYPT);
            sendTimeoutMsg();
        }
        requestResponseBuilder.setSecrettAuth(secretAuthBuilder.build());
        sendResponse(requestResponseBuilder.build());
        if (error) {
            Log.e(TAG, "handleSecretDecrypt failed");
            onFailed();
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x013f  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x015d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean handleSecretEncrypt(com.xpeng.upso.proxy.PSOProtocol.RequestResponse r22) {
        /*
            Method dump skipped, instructions count: 387
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.cduc.CduClientRequestHandler.handleSecretEncrypt(com.xpeng.upso.proxy.PSOProtocol$RequestResponse):boolean");
    }

    private boolean handleSecretDelete(PSOProtocol.RequestResponse request) {
        return true;
    }

    private void updateStatus(CduClientCommunicationStatus s) {
        this.currentStatus = s;
        Log.d(TAG, "updateStatus : " + this.currentStatus.toString());
    }

    private void sendTimeoutMsg() {
        this.handler.removeMessages(3002);
        this.handler.sendEmptyMessageDelayed(3002, 40000L);
        Log.d(TAG, "sendTimeoutMsg, Delay : 40000");
    }

    private void removeTimeoutMsg() {
        this.handler.removeMessages(3002);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(@NonNull Message msg) {
        int i = msg.what;
        if (i == 3001) {
            Log.d(TAG, "handleMessage, MSG_TEST_TIMEOUT");
            return true;
        } else if (i == 3002) {
            handleTimeoutMsg();
            return true;
        } else {
            return true;
        }
    }

    private void handleTimeoutMsg() {
        Log.d(TAG, "handleTimeoutMsg, currentStatus : " + this.currentStatus.toString());
        onFailed();
    }

    private void onFailed() {
        this.handler.removeMessages(3002);
        Log.e(TAG, "onFailed");
        closeSocket();
    }

    private void onDone() {
        this.handler.removeMessages(3002);
        closeSocket();
    }

    private void closeSocket() {
        Log.d(TAG, "closeSocket");
        this.closed = true;
        Socket socket = this.socket;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
            this.socket = null;
        }
    }

    private PSOProtocol.RequestResponse.Builder getNewBuilder(PSOProtocol.RequestResponse.MessageType msgType) {
        PSOProtocol.RequestResponse.Builder builder = PSOProtocol.RequestResponse.newBuilder().clear();
        int i = this.sendSequence;
        this.sendSequence = i + 1;
        builder.setSequence(i);
        builder.setSnId(getDeviceId());
        builder.setMessageType(msgType);
        return builder;
    }

    private String getDeviceType() {
        return XpengPsoClientType.CDU.toString();
    }

    private String getDeviceId() {
        return LocalCarManager.getCduid();
    }

    private void sendResponse(PSOProtocol.RequestResponse response) {
        try {
            OutputStream outputStream = this.socket.getOutputStream();
            response.writeTo(outputStream);
            outputStream.flush();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "send failed");
            onFailed();
        }
    }
}
