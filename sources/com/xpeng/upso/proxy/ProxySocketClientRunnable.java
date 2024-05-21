package com.xpeng.upso.proxy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.gson.JsonObject;
import com.google.protobuf.ProtocolStringList;
import com.xiaopeng.commonfunc.Constant;
import com.xpeng.upso.PsoDone.OptionMaps;
import com.xpeng.upso.PsoException.ExceptionMaps;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengCarModelTboxNk;
import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.aesserver.AesConstants;
import com.xpeng.upso.aesserver.AesDecryptResponse;
import com.xpeng.upso.aesserver.AesEncryptMultiResponse;
import com.xpeng.upso.aesserver.AesEncryptResponse;
import com.xpeng.upso.aesserver.AesPresetServer;
import com.xpeng.upso.aesserver.AesSecretResponse;
import com.xpeng.upso.aesserver.IPsuResponseCallback;
import com.xpeng.upso.aesserver.PresetParam;
import com.xpeng.upso.cduc.LocalCarManager;
import com.xpeng.upso.common.CommonConstants;
import com.xpeng.upso.http.UpsoCertResponse;
import com.xpeng.upso.proxy.PSOProtocol;
import com.xpeng.upso.proxy.ProxyProtoParser;
import com.xpeng.upso.proxy.ProxySocketClientRunnable;
import com.xpeng.upso.sentry.SentryReporter;
import com.xpeng.upso.utils.HexUtils;
import com.xpeng.upso.utils.KeyUtils;
import com.xpeng.upso.utils.LogUtils;
import com.xpeng.upso.utils.SshUtils;
import com.xpeng.upso.utils.SysPropUtils;
import com.xpeng.upso.utils.ThreadUtil;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes2.dex */
public class ProxySocketClientRunnable implements Runnable {
    private static final int COMMUNICATION_TIMEOUT_MS = 1200000;
    private static final int MSG_BASE = 5000;
    private static final int MSG_CLIENT_EVENT_CONNECTED = 5005;
    private static final int MSG_CLIENT_EVENT_DONE = 5003;
    private static final int MSG_CLIENT_EVENT_FAILED = 5004;
    private static final int MSG_COMMUNICATION_TIMEOUT = 5002;
    private static final int MSG_TEST_TIMEOUT = 5001;
    private static final String TAG = "Upso-ClientRunnable";
    private static final int XPU_CERT_TOTAL_BASE_LIMITED = 10;
    private static PSOProtocol.RequestResponse.Builder requestResponseBuilderEncrypt;
    private static List<String> requestServerEncryptDataList = new ArrayList();
    private static List<String> requestTboxEncryptDataList = new ArrayList();
    private ProxyCommunicationStatus communicationStatus;
    private String ip;
    private boolean isShutdown;
    private ProxyParamWrapper paramWrapper;
    private int presetSecretCount;
    private AesPresetServer presetServer;
    private ProxyProtoParser proxyProtoParser;
    private int runnableID;
    private Socket socket;
    private boolean started;
    private int op = -1;
    private IClientEventCallback clientEventCallback = null;
    private ProxyProtoParser.IProtoParseCallback mIProtoParseCallback = new ProxyProtoParser.IProtoParseCallback() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$Fi1iVd2peNRjQSukPHkOwt7i1iE
        @Override // com.xpeng.upso.proxy.ProxyProtoParser.IProtoParseCallback
        public final void onProtoParseSuccess(PSOProtocol.RequestResponse requestResponse) {
            ProxySocketClientRunnable.this.lambda$new$0$ProxySocketClientRunnable(requestResponse);
        }
    };
    private IPsuResponseCallback presetResultCallback = new IPsuResponseCallback() { // from class: com.xpeng.upso.proxy.ProxySocketClientRunnable.1
        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onResponseNull(int requestType) {
            ProxySocketClientRunnable.this.removeTimeoutMsg();
            LogUtils.e(ProxySocketClientRunnable.TAG, "onPresetSecretResult response is null!! requestType=" + requestType);
            ProxySocketClientRunnable.this.onFailed(102);
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onRequestTypeError(int type) {
            LogUtils.e(ProxySocketClientRunnable.TAG, "onRequestTypeError: type=" + type);
            ProxySocketClientRunnable.this.onFailed(102);
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onSecretResponse(AesSecretResponse response) {
            String str;
            ProxySocketClientRunnable.this.removeTimeoutMsg();
            ProxySocketClientRunnable.this.presetSecretCount = 0;
            List<String> secrets = response.getKeys(ProxySocketClientRunnable.this.getClientDeviceId());
            if (secrets == null || secrets.size() == 0 || secrets.size() != AesConstants.getKeyIndexCountDefault(ProxySocketClientRunnable.this.getClientEcuDeviceType())) {
                StringBuilder sb = new StringBuilder();
                sb.append("onPresetSecretResult, decryptSecreteKey aes-32 failed");
                if (secrets == null) {
                    str = "secrets == null";
                } else {
                    str = "secrets.size()=" + secrets.size();
                }
                sb.append(str);
                LogUtils.e(ProxySocketClientRunnable.TAG, sb.toString());
                ProxySocketClientRunnable.this.onFailed(102);
                return;
            }
            ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, secrets.size());
            Map<String, String> tboxSecrets = null;
            if (ProxySocketClientRunnable.this.paramWrapper.getClientType() == XpengPsoClientType.TBOX) {
                tboxSecrets = response.getTboxKeys(ProxySocketClientRunnable.this.getClientDeviceId());
                if (tboxSecrets == null || tboxSecrets.size() == 0) {
                    try {
                        if (XpengCarModelTboxNk.checkValid(ProxySocketClientRunnable.this.getClientCarType())) {
                            LogUtils.e(ProxySocketClientRunnable.TAG, "Tbox need Nk carModel:" + ProxySocketClientRunnable.this.getClientCarType());
                            LogUtils.e(ProxySocketClientRunnable.TAG, "onPresetSecretResult, decryptSecreteKey tbox failed");
                            ProxySocketClientRunnable.this.onFailed(102);
                            return;
                        }
                        LogUtils.i(ProxySocketClientRunnable.TAG, "tbox nk unsupported ...");
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.i(ProxySocketClientRunnable.TAG, "check if need tbox n k,e:" + e.toString());
                    }
                } else {
                    ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, tboxSecrets.size());
                }
            }
            Map<String, String> xpuSecrets = null;
            if (ProxySocketClientRunnable.this.paramWrapper.getClientType() == XpengPsoClientType.XPU) {
                xpuSecrets = response.getXpuKeys(ProxySocketClientRunnable.this.getClientDeviceId());
                if (xpuSecrets == null || xpuSecrets.size() == 0) {
                    LogUtils.e(ProxySocketClientRunnable.TAG, "onPresetSecretResult, decryptSecreteKey xpu failed");
                    ProxySocketClientRunnable.this.onFailed(102);
                    return;
                }
                ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, xpuSecrets.size());
            }
            ProxySocketClientRunnable.this.send2ClientSecrets(secrets, tboxSecrets, xpuSecrets);
            LogUtils.d(ProxySocketClientRunnable.TAG, "onPresetSecretResult, sendSecretsToClient");
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onEncryptResponse(AesEncryptResponse response) {
            LogUtils.d(ProxySocketClientRunnable.TAG, "onEncryptResult: ");
            ProxySocketClientRunnable.this.removeTimeoutMsg();
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onDencryptResponse(AesDecryptResponse response) {
            LogUtils.d(ProxySocketClientRunnable.TAG, "onDencryptResult: ");
            ProxySocketClientRunnable.this.removeTimeoutMsg();
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onEncryptMultiResponse(AesEncryptMultiResponse response) {
            ProxySocketClientRunnable.this.removeTimeoutMsg();
            List<String> results = response.GetResult();
            if (results == null || results.size() <= 0) {
                ProxySocketClientRunnable.this.onFailed(102);
                return;
            }
            LogUtils.d(ProxySocketClientRunnable.TAG, "onEncryptMultiResult, find keys : " + results.size());
            ProxySocketClientRunnable.this.send2ClientDecrypt(results);
        }

        /* JADX WARN: Code restructure failed: missing block: B:21:0x006c, code lost:
            r6.this$0.updateCommunicationStatus(com.xpeng.upso.proxy.ProxyCommunicationStatus.PCS_DONE);
            r6.this$0.onDone(3);
         */
        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onDecryptMultiResponse(com.xpeng.upso.aesserver.AesDecryptMultiResponse r7) {
            /*
                r6 = this;
                com.xpeng.upso.proxy.ProxySocketClientRunnable r0 = com.xpeng.upso.proxy.ProxySocketClientRunnable.this
                com.xpeng.upso.proxy.ProxySocketClientRunnable.access$000(r0)
                java.util.List r0 = r7.GetResult()
                if (r0 == 0) goto L87
                int r1 = r0.size()
                if (r1 > 0) goto L13
                goto L87
            L13:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "onDecryptMultiResult: size = "
                r1.append(r2)
                int r2 = r0.size()
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                java.lang.String r2 = "Upso-ClientRunnable"
                com.xpeng.upso.utils.LogUtils.d(r2, r1)
                r1 = 1
                r3 = 0
            L2f:
                int r4 = r0.size()
                if (r3 >= r4) goto L6a
                java.util.List r4 = com.xpeng.upso.proxy.ProxySocketClientRunnable.access$600()
                java.lang.Object r4 = r4.get(r3)
                if (r4 != 0) goto L40
                goto L6a
            L40:
                java.lang.Object r4 = r0.get(r3)
                if (r4 != 0) goto L47
                goto L6a
            L47:
                java.util.List r4 = com.xpeng.upso.proxy.ProxySocketClientRunnable.access$600()
                java.lang.Object r4 = r4.get(r3)
                java.lang.String r4 = (java.lang.String) r4
                java.lang.String r4 = r4.trim()
                java.lang.Object r5 = r0.get(r3)
                java.lang.String r5 = (java.lang.String) r5
                java.lang.String r5 = r5.trim()
                boolean r4 = r4.equals(r5)
                if (r4 != 0) goto L67
                r1 = 0
                goto L6a
            L67:
                int r3 = r3 + 1
                goto L2f
            L6a:
                if (r1 == 0) goto L7a
                com.xpeng.upso.proxy.ProxySocketClientRunnable r3 = com.xpeng.upso.proxy.ProxySocketClientRunnable.this
                com.xpeng.upso.proxy.ProxyCommunicationStatus r4 = com.xpeng.upso.proxy.ProxyCommunicationStatus.PCS_DONE
                com.xpeng.upso.proxy.ProxySocketClientRunnable.access$700(r3, r4)
                com.xpeng.upso.proxy.ProxySocketClientRunnable r3 = com.xpeng.upso.proxy.ProxySocketClientRunnable.this
                r4 = 3
                com.xpeng.upso.proxy.ProxySocketClientRunnable.access$800(r3, r4)
                goto L81
            L7a:
                com.xpeng.upso.proxy.ProxySocketClientRunnable r3 = com.xpeng.upso.proxy.ProxySocketClientRunnable.this
                r4 = 107(0x6b, float:1.5E-43)
                com.xpeng.upso.proxy.ProxySocketClientRunnable.access$100(r3, r4)
            L81:
                java.lang.String r3 = "onDecryptMultiResult: end"
                com.xpeng.upso.utils.LogUtils.d(r2, r3)
                return
            L87:
                com.xpeng.upso.proxy.ProxySocketClientRunnable r1 = com.xpeng.upso.proxy.ProxySocketClientRunnable.this
                r2 = 102(0x66, float:1.43E-43)
                com.xpeng.upso.proxy.ProxySocketClientRunnable.access$100(r1, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.proxy.ProxySocketClientRunnable.AnonymousClass1.onDecryptMultiResponse(com.xpeng.upso.aesserver.AesDecryptMultiResponse):void");
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onCertListResponse(UpsoCertResponse response) {
            ProxySocketClientRunnable.this.removeTimeoutMsg();
            ProxySocketClientRunnable.this.requestServerAesKeyAfterXpuCert(response);
        }

        @Override // com.xpeng.upso.aesserver.IPsuResponseCallback
        public void onSecretAesCertResponse(AesSecretResponse resAes, UpsoCertResponse resCert) {
            String str;
            String str2;
            LogUtils.e(ProxySocketClientRunnable.TAG, "onSecretAesCertResponse");
            ProxySocketClientRunnable.this.removeTimeoutMsg();
            if (resAes != null && resCert != null) {
                ProxySocketClientRunnable.this.presetSecretCount = 0;
                List<String> secrets = resAes.getKeys(ProxySocketClientRunnable.this.getClientDeviceId());
                if (secrets == null || secrets.size() == 0 || secrets.size() != AesConstants.getKeyIndexCountDefault(ProxySocketClientRunnable.this.getClientEcuDeviceType())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onSecretAesCertResponse, decryptSecreteKey aes-32 failed");
                    if (secrets == null) {
                        str = "secrets == null";
                    } else {
                        str = "secrets.size()=" + secrets.size();
                    }
                    sb.append(str);
                    LogUtils.e(ProxySocketClientRunnable.TAG, sb.toString());
                    ProxySocketClientRunnable.this.onFailed(102);
                    return;
                }
                ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, secrets.size());
                Map<String, String> tboxSecrets = null;
                if (ProxySocketClientRunnable.this.paramWrapper.getClientType() == XpengPsoClientType.TBOX) {
                    tboxSecrets = resAes.getTboxKeys(ProxySocketClientRunnable.this.getClientDeviceId());
                    if (tboxSecrets == null || tboxSecrets.size() == 0) {
                        try {
                            if (XpengCarModelTboxNk.checkValid(ProxySocketClientRunnable.this.getClientCarType())) {
                                LogUtils.e(ProxySocketClientRunnable.TAG, "Tbox need Nk carModel:" + ProxySocketClientRunnable.this.getClientCarType());
                                LogUtils.e(ProxySocketClientRunnable.TAG, "onSecretAesCertResponse, decryptSecreteKey tbox failed");
                                ProxySocketClientRunnable.this.onFailed(102);
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.i(ProxySocketClientRunnable.TAG, "check if need tbox n k,e:" + e.toString());
                        }
                    } else {
                        ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, tboxSecrets.size());
                    }
                }
                Map<String, String> xpuSecrets = null;
                if (ProxySocketClientRunnable.this.paramWrapper.getClientType() == XpengPsoClientType.XPU) {
                    xpuSecrets = resAes.getXpuKeys(ProxySocketClientRunnable.this.getClientDeviceId());
                    if (xpuSecrets == null || xpuSecrets.size() == 0) {
                        LogUtils.e(ProxySocketClientRunnable.TAG, "onSecretAesCertResponse, decryptSecreteKey xpu failed");
                        ProxySocketClientRunnable.this.onFailed(102);
                        return;
                    }
                    ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, xpuSecrets.size());
                }
                List<String> certs = UpsoCertResponse.GetResult();
                if (certs == null || certs.size() == 0 || certs.size() != 10) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onSecretAesCertResponse, get cert list  failed");
                    if (certs == null) {
                        str2 = "certs = null";
                    } else {
                        str2 = "certs.size()=" + certs.size();
                    }
                    sb2.append(str2);
                    LogUtils.e(ProxySocketClientRunnable.TAG, sb2.toString());
                    ProxySocketClientRunnable.this.onFailed(102);
                    return;
                }
                ProxySocketClientRunnable.access$212(ProxySocketClientRunnable.this, certs.size());
                ProxySocketClientRunnable.this.send2ClientSecrets(secrets, certs, tboxSecrets, xpuSecrets);
                return;
            }
            LogUtils.e(ProxySocketClientRunnable.TAG, "onSecretAesCertResponse: esAes == null||resCert == null");
            ProxySocketClientRunnable.this.onFailed(102);
        }
    };
    private long taskStartTime = 0;
    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$PHjPmdkob7kYsWyczClVHsZ-aLE
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            return ProxySocketClientRunnable.this.lambda$new$6$ProxySocketClientRunnable(message);
        }
    });
    private PsoClientInfo clientIdentifyInfo = null;
    private int sendSequence = 0;

    /* loaded from: classes2.dex */
    public interface IClientEventCallback {
        void onClientPresetStatusCallback(int clientId, String deviceId, int statusCode);
    }

    /* loaded from: classes2.dex */
    public interface Send2clientCallBack {
        void onSended(boolean result);
    }

    static /* synthetic */ int access$212(ProxySocketClientRunnable x0, int x1) {
        int i = x0.presetSecretCount + x1;
        x0.presetSecretCount = i;
        return i;
    }

    public ProxySocketClientRunnable(int runnableID, ProxyParamWrapper paramWrapper, Socket socket) {
        this.runnableID = runnableID;
        this.paramWrapper = paramWrapper;
        this.socket = socket;
        this.ip = socket.getInetAddress().toString();
        resetWorkingStatus();
        this.isShutdown = false;
        this.presetSecretCount = 0;
        this.proxyProtoParser = new ProxyProtoParser();
        this.proxyProtoParser.setmProtoParseCallback(this.mIProtoParseCallback);
        this.presetServer = new AesPresetServer();
        this.presetServer.setPresetResultCallback(this.presetResultCallback);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_INIT);
    }

    public void setClientEventCallback(IClientEventCallback callback) {
        this.clientEventCallback = callback;
    }

    @Override // java.lang.Runnable
    public void run() {
        Socket socket;
        try {
        } catch (SocketException e) {
            LogUtils.e(TAG, "setSoTimeout, error");
            e.printStackTrace();
        }
        if (this.socket == null) {
            LogUtils.e(TAG, "socket = null,stop this thread");
            return;
        }
        this.socket.setSoTimeout(30000);
        while (!this.isShutdown && (socket = this.socket) != null) {
            this.proxyProtoParser.parseFrom(socket);
        }
        LogUtils.d(TAG, "Thread End");
    }

    public /* synthetic */ void lambda$new$0$ProxySocketClientRunnable(PSOProtocol.RequestResponse requestResponse) {
        LogUtils.d(TAG, "onProtoParseSuccess: ");
        try {
            if (checkInfoClientType(requestResponse.getIdentityInfo(), 1)) {
                processClientProtobufPacket(requestResponse);
            } else if (checkInfoClientType(getClientIdentifyInfo(), 2)) {
                processClientProtobufPacket(requestResponse);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    private boolean checkInfoClientType(PSOProtocol.IdentityInfo identityInfo, int number) {
        if (identityInfo == null) {
            LogUtils.e(TAG, "onProtoParseSuccess: clientIdentifyInfo" + number + "=null");
            return false;
        } else if (StringUtils.isEmpty(identityInfo.getDeviceType())) {
            LogUtils.e(TAG, "onProtoParseSuccess: client type" + number + " is empty");
            return false;
        } else if (!identityInfo.getDeviceType().equals(this.paramWrapper.getClientType().toString())) {
            LogUtils.e(TAG, "onProtoParseSuccess: client type" + number + "," + identityInfo.getDeviceType() + "!=" + this.paramWrapper.getClientType().toString());
            return false;
        } else {
            LogUtils.d(TAG, "onProtoParseSuccess: client type" + number + " check ok!");
            return true;
        }
    }

    public int getOp() {
        return this.op;
    }

    public boolean opIsIdle() {
        return this.op == -1;
    }

    public void startOp(int op) {
        this.taskStartTime = System.currentTimeMillis();
        this.op = op;
        this.started = true;
        send2ClientIdentityInfo();
    }

    public void stop() {
        resetWorkingStatus();
        shutdown();
    }

    public void resetWorkingStatus() {
        this.started = false;
        this.op = -1;
    }

    public boolean isStarted() {
        LogUtils.i(TAG, "isStarted=" + this.started);
        return this.started;
    }

    private void processClientProtobufPacket(PSOProtocol.RequestResponse requestResponse) {
        if (requestResponse == null) {
            LogUtils.e(TAG, "processProtobufPacket: requestResponse fail");
            return;
        }
        int messageType = requestResponse.getMessageTypeValue();
        LogUtils.d(TAG, "processProtobufPacket: message type: " + messageType);
        switch (messageType) {
            case 5:
                LogUtils.d(TAG, "processProtobufPacket: XP_INFO_RESPONSE_VALUE");
                handleClientInfoResponse(requestResponse);
                return;
            case 6:
                LogUtils.d(TAG, "processProtobufPacket: XP_SECRET_PRESET_RESPONSE_VALUE");
                handleClientSecretPresetReponse(requestResponse);
                return;
            case 7:
                LogUtils.d(TAG, "processProtobufPacket: XP_SECRET_ENCRYPT_AUTH_RESPONSE_VALUE");
                handleClientSecretEncryptResponse(requestResponse);
                return;
            case 8:
                LogUtils.d(TAG, "processProtobufPacket: XP_SECRET_DECRYPT_AUTH_RESPONSE_VALUE");
                handleClientSecretDecryptResponse(requestResponse);
                return;
            case 9:
                LogUtils.d(TAG, "processProtobufPacket: XP_SECRET_DELETE_RESPONSE_VALUE");
                handleClientSecretDeleteResponse(requestResponse);
                return;
            default:
                return;
        }
    }

    private void handleClientSecretPresetReponse(PSOProtocol.RequestResponse requestResponse) {
        removeTimeoutMsg();
        LogUtils.d(TAG, "handleClientSecretPresetReponse: ");
        PSOProtocol.SecretPresetResponse response = requestResponse.getSecretPresetResponse();
        if (response == null) {
            onFailed(105);
            return;
        }
        int index = response.getIndex();
        LogUtils.d(TAG, "handleClientSecretPresetReponse: index = " + index);
        if (index == this.presetSecretCount) {
            onDone(2);
            return;
        }
        LogUtils.e(TAG, "preset secret size = " + this.presetSecretCount + ", but resp size = " + index);
        onFailed(105);
    }

    private void handleClientInfoResponse(PSOProtocol.RequestResponse requestResponse) {
        removeTimeoutMsg();
        PSOProtocol.IdentityInfo identityInfo = requestResponse.getIdentityInfo();
        if (identityInfo == null) {
            LogUtils.e(TAG, "handleClientInfoResponse null");
            onFailed(103);
            return;
        }
        setClientIdentifyInfo(identityInfo);
        if (EcuIsUserVer() && this.op == 1) {
            LogUtils.e(TAG, "error,IS_USER_VER_PROTECTED...");
            onFailed(110);
        } else if (identityInfo.getDeviceId() == null || identityInfo.getDeviceId().length() <= 0) {
            LogUtils.e(TAG, "handleClientInfoResponse null device");
            onFailed(103);
        } else if (!clientTypeWorking()) {
        } else {
            clientEvent(MSG_CLIENT_EVENT_CONNECTED, 1);
            int hasSecret = identityInfo.getHasSecret();
            if (this.op == 1) {
                hasSecret = 0;
            }
            if (hasSecret != 0) {
                LogUtils.e(TAG, "handleClientInfoResponse: has secret in client");
                LogUtils.e(TAG, "handleClientInfoResponse: role = " + identityInfo.getRole() + " version = " + identityInfo.getVersion() + " mask = " + identityInfo.getMask());
                int i = this.op;
                if (i == 1) {
                    onDone(2);
                    return;
                } else if (i == 2) {
                    LogUtils.d(TAG, "handleClientInfoResponse: start verify");
                    requestServerEncryptMulti();
                    return;
                } else {
                    return;
                }
            }
            LogUtils.d(TAG, "handleClientInfoResponse: get aes key from server, clientIdentifyInfo" + this.clientIdentifyInfo);
            int i2 = this.op;
            if (i2 == 1) {
                LogUtils.d(TAG, "handleClientInfoResponse: start preset");
                LogUtils.d(TAG, "handleClientInfoResponse: XpengPsoClientType:" + getClientEcuDeviceType());
                if (getClientEcuDeviceType().equals(XpengPsoClientType.XPU.toString())) {
                    requestServerXpuCerts();
                } else {
                    requestServerAesKey();
                }
            } else if (i2 == 2) {
                onFailed(ProxyStatusDescription.PSD_ERR_VERIFY_KEY_MISSING);
            }
        }
    }

    private void handleClientSecretEncryptResponse(PSOProtocol.RequestResponse requestResponse) {
        removeTimeoutMsg();
        PSOProtocol.SecretAuth secretAuth = requestResponse.getSecrettAuth();
        if (secretAuth == null) {
            LogUtils.e(TAG, "handleClientSecretEncryptResponse secretAuth null");
            onFailed(103);
            return;
        }
        int clientEncrypted = secretAuth.getCont();
        LogUtils.d(TAG, "handleClientSecretEncryptResponse clientEncrypted " + clientEncrypted);
        if (clientEncrypted <= 0) {
            LogUtils.e(TAG, "handleClientSecretEncryptResponse client index 0");
            onFailed(103);
            return;
        }
        List<String> list = secretAuth.getResultList();
        if (list == null || list.size() <= 0) {
            LogUtils.e(TAG, "handleClientSecretEncryptResponse list null");
            onFailed(103);
            return;
        }
        List<String> encryptDataList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            try {
                byte[] data = list.get(i).getBytes();
                if (data != null && data.length > 0) {
                    byte[] data2 = new byte[data.length];
                    System.arraycopy(data, 0, data2, 0, data2.length);
                    encryptDataList.add(new String(data2, "UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.d(TAG, "handleClientSecretEncryptResponse: client encrypt success");
        requestServerDecryptMulti(encryptDataList);
    }

    private void handleClientSecretDecryptResponse(PSOProtocol.RequestResponse requestResponse) {
        removeTimeoutMsg();
        LogUtils.d(TAG, "handleClientSecretDecryptResponse: ");
        PSOProtocol.SecretAuth secretAuth = requestResponse.getSecrettAuth();
        if (secretAuth == null) {
            LogUtils.e(TAG, "handleClientSecretDecryptResponse: secretAuth null");
            onFailed(103);
            return;
        }
        ProtocolStringList list = secretAuth.getResultList();
        if (list == null || list.size() <= 0) {
            LogUtils.e(TAG, "handleClientSecretDecryptResponse: getResultList null");
            onFailed(103);
        } else if (requestServerEncryptDataList.size() != list.size()) {
            LogUtils.e(TAG, "handleClientSecretDecryptResponse: list size is not equal");
            onFailed(103);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (requestServerEncryptDataList.get(i) == null || list.get(i) == null) {
                    return;
                }
                if (!requestServerEncryptDataList.get(i).trim().equals(list.get(i).trim())) {
                    LogUtils.e(TAG, "handleClientSecretDecryptResponse: client decrypt auth fail");
                    onFailed(109);
                    return;
                }
            }
            send2ClientEncrypt();
        }
    }

    private void handleClientSecretDeleteResponse(PSOProtocol.RequestResponse requestResponse) {
        LogUtils.d(TAG, "handleClientSecretDeleteResponse: ");
    }

    private void send2ClientIdentityInfo() {
        LogUtils.d(TAG, "sendXpIdentityInfoRequest: ");
        PSOProtocol.IdentityInfo.Builder identityInfoBuilder = PSOProtocol.IdentityInfo.newBuilder().clear();
        identityInfoBuilder.setMask(0);
        identityInfoBuilder.setRole(0);
        identityInfoBuilder.setVersion(3);
        final PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_INFO);
        requestResponseBuilder.setIdentityInfo(identityInfoBuilder.build());
        send2ClientDataByThread(requestResponseBuilder.build(), new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$G5QSxtfuJ-oxf4aAOHLWygdNy-8
            @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
            public final void onSended(boolean z) {
                PSOProtocol.RequestResponse.Builder.this.clear();
            }
        });
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_PB_REQ_INFO);
        sendTimeoutMsg();
    }

    void send2ClientSecrets(List<String> secrets, Map<String, String> tbox, Map<String, String> xpu) {
        PSOProtocol.SecretPreset.Builder secretGetResponseBuilder = getNewAesSecretResponseBuilder(secrets, tbox, xpu);
        send2ClientSecrets(secretGetResponseBuilder);
        sendTimeoutMsg();
    }

    void send2ClientSecrets(List<String> secrets, List<String> certs, Map<String, String> tbox, Map<String, String> xpu) {
        LogUtils.e(TAG, "send2ClientSecrets(secrets, certs,tboxSecrets,xpuSecrets)");
        PSOProtocol.SecretPreset.Builder secretGetResponseBuilder = getNewAesSecretResponseBuilder(secrets, certs, tbox, xpu);
        LogUtils.e(TAG, "send2ClientSecrets(secrets, certs,tboxSecrets,xpuSecrets),secretGetResponseBuilder.getIndex()" + secretGetResponseBuilder.getIndex());
        send2ClientSecrets(secretGetResponseBuilder);
        sendTimeoutMsg();
    }

    void send2ClientSecrets(PSOProtocol.SecretPreset.Builder secretGetResponseBuilder) {
        final PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_PRESET);
        requestResponseBuilder.setSecretPreset(secretGetResponseBuilder.build());
        send2ClientDataByThread(requestResponseBuilder.build(), new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$4jXJFYsJtWhr3sI1fALs8Qk0r-M
            @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
            public final void onSended(boolean z) {
                PSOProtocol.RequestResponse.Builder.this.clear();
            }
        });
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_PB_PRESET);
        sendTimeoutMsg();
    }

    PSOProtocol.SecretPreset.Builder getNewAesSecretResponseBuilder(List<String> secrets, Map<String, String> tbox, Map<String, String> xpu) {
        PSOProtocol.SecretPreset.Builder secretGetResponseBuilder = PSOProtocol.SecretPreset.newBuilder().clear();
        int size = secrets.size();
        LogUtils.d(TAG, "sendSecrets to" + this.paramWrapper.getClientType() + ", size = " + size);
        secretGetResponseBuilder.setIndex(size);
        for (int i = 0; i < size; i++) {
            secretGetResponseBuilder.addSecret(getNewSecret(i, secrets.get(i), PSOProtocol.Secret.SecretType.KEY_TYPE_AES));
        }
        if (tbox != null) {
            for (String name : tbox.keySet()) {
                secretGetResponseBuilder.addSecret(getNewSecret(name, tbox.get(name)));
            }
            secretGetResponseBuilder.setIndex(secretGetResponseBuilder.getIndex() + tbox.size());
        }
        if (xpu != null) {
            for (String name2 : xpu.keySet()) {
                if (name2.equals("xpuBaseKey")) {
                    secretGetResponseBuilder.addSecret(0, getNewSecret(name2, xpu.get(name2)));
                } else {
                    secretGetResponseBuilder.addSecret(getNewSecret(name2, xpu.get(name2)));
                }
            }
            secretGetResponseBuilder.setIndex(secretGetResponseBuilder.getIndex() + xpu.size());
        }
        return secretGetResponseBuilder;
    }

    PSOProtocol.SecretPreset.Builder getNewAesSecretResponseBuilder(List<String> secrets, List<String> certs, Map<String, String> tbox, Map<String, String> xpu) {
        PSOProtocol.SecretPreset.Builder secretGetResponseBuilder = PSOProtocol.SecretPreset.newBuilder().clear();
        int size = secrets.size();
        LogUtils.d(TAG, "sendSecrets to" + this.paramWrapper.getClientType() + ", size = " + size);
        secretGetResponseBuilder.setIndex(size);
        for (int i = 0; i < size; i++) {
            secretGetResponseBuilder.addSecret(getNewSecret(i, secrets.get(i), PSOProtocol.Secret.SecretType.KEY_TYPE_AES));
        }
        if (tbox != null) {
            for (String name : tbox.keySet()) {
                secretGetResponseBuilder.addSecret(getNewSecret(name, tbox.get(name)));
            }
            secretGetResponseBuilder.setIndex(secretGetResponseBuilder.getIndex() + tbox.size());
        }
        if (xpu != null) {
            for (String name2 : xpu.keySet()) {
                if (name2.equals("xpuBaseKey")) {
                    secretGetResponseBuilder.addSecret(0, getNewSecret(name2, xpu.get(name2)));
                } else {
                    secretGetResponseBuilder.addSecret(getNewSecret(name2, xpu.get(name2)));
                }
            }
            secretGetResponseBuilder.setIndex(secretGetResponseBuilder.getIndex() + xpu.size());
        }
        if (certs != null) {
            for (int i2 = 0; i2 < certs.size(); i2++) {
                secretGetResponseBuilder.addSecret(getNewSecret(i2, certs.get(i2), PSOProtocol.Secret.SecretType.KEY_TYPE_CERTIFICATE));
            }
            secretGetResponseBuilder.setIndex(secretGetResponseBuilder.getIndex() + certs.size());
        }
        return secretGetResponseBuilder;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send2ClientDecrypt(List<String> encrypts) {
        PSOProtocol.SecretAuth.Builder secretAuthBuilder = PSOProtocol.SecretAuth.newBuilder().clear();
        secretAuthBuilder.setCont(encrypts.size());
        secretAuthBuilder.setIndex(Integer.MAX_VALUE);
        for (int i = 0; i < encrypts.size(); i++) {
            secretAuthBuilder.addResult(encrypts.get(i));
        }
        final PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_DECRYPT_AUTH);
        requestResponseBuilder.setSecrettAuth(secretAuthBuilder.build());
        send2ClientDataByThread(requestResponseBuilder.build(), new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$RMi8hjaievadF_OtiN3R04IPoYo
            @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
            public final void onSended(boolean z) {
                PSOProtocol.RequestResponse.Builder.this.clear();
            }
        });
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_PB_REQ_DECRYPT);
        sendTimeoutMsg();
    }

    private void send2ClientEncrypt() {
        if (requestResponseBuilderEncrypt == null) {
            PSOProtocol.SecretAuth.Builder secretAuthBuilder = PSOProtocol.SecretAuth.newBuilder().clear();
            secretAuthBuilder.setCont(AesConstants.getKeyIndexCountDefault(getClientEcuDeviceType()));
            secretAuthBuilder.setIndex(Integer.MAX_VALUE);
            if (requestTboxEncryptDataList.isEmpty()) {
                for (int i = 0; i < AesConstants.getKeyIndexCountDefault(getClientEcuDeviceType()); i++) {
                    List<String> list = requestTboxEncryptDataList;
                    list.add("decrypt data " + i);
                }
            }
            for (int i2 = 0; i2 < AesConstants.getKeyIndexCountDefault(getClientEcuDeviceType()); i2++) {
                secretAuthBuilder.addResult(requestTboxEncryptDataList.get(i2));
            }
            requestResponseBuilderEncrypt = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_ENCRYPT_AUTH);
            requestResponseBuilderEncrypt.setSecrettAuth(secretAuthBuilder.build());
        }
        send2ClientDataByThread(requestResponseBuilderEncrypt.build(), new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$sOyfjQWlXoieqtl2VNcUhmS6MD8
            @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
            public final void onSended(boolean z) {
                ProxySocketClientRunnable.lambda$send2ClientEncrypt$4(z);
            }
        });
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_PB_REQ_ENCRYPT);
        sendTimeoutMsg();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$send2ClientEncrypt$4(boolean result) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send2ClientOptionResult(int result, Send2clientCallBack callBack) {
        LogUtils.d(TAG, "send2ClientOptionResult: ");
        PSOProtocol.IdentityInfo.Builder identityInfoBuilder = PSOProtocol.IdentityInfo.newBuilder().clear();
        identityInfoBuilder.setMask(0);
        identityInfoBuilder.setRole(0);
        identityInfoBuilder.setVersion(3);
        PSOProtocol.RequestResponse.Builder requestResponseBuilder = getNewBuilder(PSOProtocol.RequestResponse.MessageType.XP_SECRET_RESULT, result);
        requestResponseBuilder.setIdentityInfo(identityInfoBuilder.build());
        send2ClientDataByThread(requestResponseBuilder.build(), callBack);
    }

    private void send2ClientDataByThread(final PSOProtocol.RequestResponse requestResponse, final Send2clientCallBack callBack) {
        if (requestResponse == null) {
            LogUtils.e(TAG, "sendDataByThread: requestResponse is null");
        } else {
            new Thread(new Runnable() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$rz1enO_TCKd_lg3jwJOcVdwGgq4
                @Override // java.lang.Runnable
                public final void run() {
                    ProxySocketClientRunnable.this.lambda$send2ClientDataByThread$5$ProxySocketClientRunnable(requestResponse, callBack);
                }
            }).start();
        }
    }

    public /* synthetic */ void lambda$send2ClientDataByThread$5$ProxySocketClientRunnable(PSOProtocol.RequestResponse requestResponse, Send2clientCallBack callBack) {
        synchronized (this) {
            send2ClientImpl(requestResponse, callBack);
        }
    }

    private void send2ClientImpl(PSOProtocol.RequestResponse requestResponse, Send2clientCallBack callBack) {
        try {
            LogUtils.d(TAG, "send2ClientImpl length : " + requestResponse.toByteArray().length);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
        if (this.socket == null) {
            LogUtils.e(TAG, "send2ClientImpl,socket = null,return");
        } else if (this.socket.isClosed()) {
            LogUtils.e(TAG, "send2ClientImpl,socket closed,return");
        } else {
            LogUtils.d(TAG, "SEND2Client : msgType=" + requestResponse.getMessageType());
            LogUtils.d(TAG, "SEND2Client data: " + UpsoConfig.logLimited(requestResponse.toString()));
            if (!clientTypeWorking()) {
                LogUtils.d(TAG, "SEND2Client : cancel");
                return;
            }
            OutputStream os = this.socket.getOutputStream();
            if (os != null) {
                byte[] data = requestResponse.toByteArray();
                int offset = 0;
                while (true) {
                    if (0 < data.length) {
                        if (offset + 1024 < data.length) {
                            os.write(data, offset, 1024);
                            offset += 1024;
                        } else {
                            os.write(data, offset, data.length - offset);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                os.flush();
                if (callBack != null) {
                    callBack.onSended(true);
                    return;
                }
                return;
            }
            if (callBack != null) {
                callBack.onSended(false);
            }
        }
    }

    private void requestServerAesKey() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_ID, getClientDeviceId());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_TYPE, getClientCarDeviceType());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_VERSION, (Number) 2);
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_TO_UPDATE_SECRET, Boolean.valueOf(toUpdateSecret()));
        if (!StringUtils.isEmpty(getEcuPlatform())) {
            jsonObject.addProperty(AesConstants.REQUEST_PARAM_ECU_PLATFORM, getEcuPlatform());
        }
        String urlPath = CommonConstants.getAesBaseUrl(getClientCarModel()) + AesConstants.SERVER_PATH_PRESET;
        PresetParam aesPresetParam = new PresetParam(1, urlPath, jsonObject);
        aesPresetParam.setDeviceId(getClientDeviceId());
        this.presetServer.requestFromNetwork(aesPresetParam);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_HTTPS_REQ_SECRET);
        sendTimeoutMsg();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestServerAesKeyAfterXpuCert(UpsoCertResponse certResponse) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_ID, getClientDeviceId());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_TYPE, getClientCarDeviceType());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_VERSION, (Number) 2);
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_TO_UPDATE_SECRET, Boolean.valueOf(toUpdateSecret()));
        String urlPath = CommonConstants.getAesBaseUrl(getClientCarModel()) + AesConstants.SERVER_PATH_PRESET;
        PresetParam aesPresetParam = new PresetParam(10, urlPath, jsonObject);
        aesPresetParam.setDeviceId(getClientDeviceId());
        aesPresetParam.setDeviceType(getClientEcuDeviceType());
        this.presetServer.requestFromNetworkAfterXpuCert(aesPresetParam, certResponse);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_HTTPS_REQ_SECRET);
        sendTimeoutMsg();
    }

    private void requestServerXpuCerts() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_ID, getClientDeviceId());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_TYPE, getClientCarDeviceType());
        jsonObject.addProperty(AesConstants.REQUEST_PARAM_VERSION, (Number) 2);
        String urlPath = CommonConstants.getCertUrl();
        PresetParam aesPresetParam = new PresetParam(11, urlPath, jsonObject);
        aesPresetParam.setDeviceId(getClientDeviceId());
        aesPresetParam.setDeviceType(XpengPsoClientType.XPU.toString());
        this.presetServer.requestFromNetwork(aesPresetParam);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_HTTPS_REQ_SECRET);
        sendTimeoutMsg();
    }

    private void requestServerEncryptMulti() {
        JsonObject root = new JsonObject();
        if (requestServerEncryptDataList.isEmpty()) {
            for (int i = 0; i < AesConstants.getKeyIndexCountDefault(getClientEcuDeviceType()); i++) {
                requestServerEncryptDataList.add("encrypt data " + i);
            }
        }
        for (int i2 = 0; i2 < AesConstants.getKeyIndexCountDefault(getClientEcuDeviceType()); i2++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AesConstants.REQUEST_PARAM_DEVICE_ID, getClientDeviceId());
            jsonObject.addProperty(AesConstants.REQUEST_PARAM_TEE_DEVICE_TYPE, getClientCarDeviceType());
            jsonObject.addProperty(AesConstants.REQUEST_PARAM_VERSION, (Number) 2);
            jsonObject.addProperty("keyIndex", Integer.valueOf(i2));
            jsonObject.addProperty(Constant.KEY_RAW_DATA, requestServerEncryptDataList.get(i2));
            root.add("" + i2, jsonObject);
        }
        String urlPath = CommonConstants.getAesBaseUrl(getClientCarModel()) + AesConstants.SERVER_PATH_ENCRYPT_MULTI;
        PresetParam aesPresetParam = new PresetParam(4, urlPath, root);
        aesPresetParam.setDeviceId(getClientDeviceId());
        this.presetServer.requestFromNetwork(aesPresetParam);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_HTTPS_REQ_ENCRYPT);
        sendTimeoutMsg();
    }

    private void requestServerDecryptMulti(List<String> encrypts) {
        JsonObject root = new JsonObject();
        for (int i = 0; i < encrypts.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("teeData", new String(encrypts.get(i).getBytes(StandardCharsets.UTF_8)));
            root.add("" + i, jsonObject);
        }
        String urlPath = CommonConstants.getAesBaseUrl(getClientCarModel()) + AesConstants.SERVER_PATH_DECRYPT_MULTI;
        PresetParam aesPresetParam = new PresetParam(5, urlPath, root);
        aesPresetParam.setDeviceId(getClientDeviceId());
        this.presetServer.requestFromNetwork(aesPresetParam);
        updateCommunicationStatus(ProxyCommunicationStatus.PCS_HTTPS_REQ_DECRYPT);
        sendTimeoutMsg();
    }

    private PSOProtocol.RequestResponse.Builder getNewBuilder(PSOProtocol.RequestResponse.MessageType msgType) {
        PSOProtocol.RequestResponse.Builder builder = PSOProtocol.RequestResponse.newBuilder().clear();
        int i = this.sendSequence;
        this.sendSequence = i + 1;
        builder.setSequence(i);
        builder.setSnId(LocalCarManager.getCduid());
        builder.setMessageType(msgType);
        return builder;
    }

    private PSOProtocol.RequestResponse.Builder getNewBuilder(PSOProtocol.RequestResponse.MessageType msgType, int result) {
        PSOProtocol.RequestResponse.Builder builder = PSOProtocol.RequestResponse.newBuilder().clear();
        int i = this.sendSequence;
        this.sendSequence = i + 1;
        builder.setSequence(i);
        builder.setSnId(LocalCarManager.getCduid());
        builder.setMessageType(msgType);
        builder.setResult(result);
        return builder;
    }

    private PSOProtocol.Secret getNewSecret(int index, String key, PSOProtocol.Secret.SecretType type) {
        PSOProtocol.Secret.Builder secretBuilder = PSOProtocol.Secret.newBuilder().clear();
        String aeskey = HexUtils.bytesToHexString(KeyUtils.decodeBASE64(key));
        secretBuilder.setSecret(aeskey);
        secretBuilder.setIndex(index);
        secretBuilder.setName("");
        secretBuilder.setSign(HexUtils.bytesToHexString(KeyUtils.md5(key)));
        secretBuilder.setType(type);
        return secretBuilder.build();
    }

    private PSOProtocol.Secret getNewSecret(String name, String key) {
        PSOProtocol.Secret.Builder secretBuilder = PSOProtocol.Secret.newBuilder().clear();
        String aeskey = HexUtils.bytesToHexString(KeyUtils.decodeBASE64(key));
        secretBuilder.setSecret(aeskey);
        secretBuilder.setIndex(-1L);
        secretBuilder.setName(name);
        secretBuilder.setSign(HexUtils.bytesToHexString(KeyUtils.md5(key)));
        secretBuilder.setType(PSOProtocol.Secret.SecretType.KEY_TYPE_AES);
        return secretBuilder.build();
    }

    private void setClientIdentifyInfo(PSOProtocol.IdentityInfo info) {
        this.clientIdentifyInfo = new PsoClientInfo(info);
    }

    private PSOProtocol.IdentityInfo getClientIdentifyInfo() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.getClientIdentifyInfo();
        }
        return null;
    }

    private boolean hasSecret() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.hasSecret();
        }
        return false;
    }

    private boolean toUpdateSecret() {
        return !hasSecret();
    }

    private String getEcuPlatform() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null && !StringUtils.isEmpty(psoClientInfo.getEcuPlatform())) {
            return this.clientIdentifyInfo.getEcuPlatform();
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getClientDeviceId() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo == null) {
            return AesConstants.UNKNOWN_DEVICE_ID;
        }
        return psoClientInfo.getClientDeviceId();
    }

    public String getClientCarDeviceType() {
        try {
            if (this.clientIdentifyInfo != null && !StringUtils.isEmpty(this.clientIdentifyInfo.getClientCarType()) && !StringUtils.isEmpty(this.clientIdentifyInfo.getClientEcuDeviceType())) {
                String clientDeviceType = this.clientIdentifyInfo.getClientCarType() + "_" + this.clientIdentifyInfo.getClientEcuDeviceType();
                return clientDeviceType;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getClientEcuDeviceType() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.getClientEcuDeviceType();
        }
        return null;
    }

    public String getClientCarType() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.getClientCarType();
        }
        return null;
    }

    public XpengCarModel getClientCarModel() {
        try {
            if (this.clientIdentifyInfo != null) {
                return this.clientIdentifyInfo.getClientCarModel();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    public boolean EcuIsUserVer() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.EcuIsUserVer();
        }
        return false;
    }

    public String getEcuRomVer() {
        PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
        if (psoClientInfo != null) {
            return psoClientInfo.getEcuRomVer();
        }
        return null;
    }

    public boolean clientTypeWorking() {
        if (getClientEcuDeviceType() != null && !getClientEcuDeviceType().equals(this.paramWrapper.getClientType().toString())) {
            LogUtils.d(TAG, "client type is not working in " + this.paramWrapper.getClientType().toString() + " mode ");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCommunicationStatus(ProxyCommunicationStatus status) {
        this.communicationStatus = status;
        LogUtils.d(TAG, "updateStatus : " + status.toString());
    }

    public /* synthetic */ boolean lambda$new$6$ProxySocketClientRunnable(Message msg) {
        switch (msg.what) {
            case 5001:
                LogUtils.d(TAG, "handleMessage, MSG_TEST_TIMEOUT");
                return true;
            case MSG_COMMUNICATION_TIMEOUT /* 5002 */:
                handleTimeoutMsg();
                return true;
            case MSG_CLIENT_EVENT_DONE /* 5003 */:
                if (this.op == 2) {
                    resetWorkingStatus();
                } else {
                    this.op = -1;
                }
                clientEventCallback(((Integer) msg.obj).intValue());
                return true;
            case MSG_CLIENT_EVENT_FAILED /* 5004 */:
                clientEventCallback(((Integer) msg.obj).intValue());
                stop();
                return true;
            case MSG_CLIENT_EVENT_CONNECTED /* 5005 */:
                clientEventCallback(((Integer) msg.obj).intValue());
                return true;
            default:
                return true;
        }
    }

    private void clientEventCallback(int statusDescription) {
        if (this.clientEventCallback != null) {
            String deviceID = AesConstants.UNKNOWN_DEVICE_ID;
            PsoClientInfo psoClientInfo = this.clientIdentifyInfo;
            if (psoClientInfo != null) {
                deviceID = psoClientInfo.getClientDeviceId();
            }
            this.clientEventCallback.onClientPresetStatusCallback(this.runnableID, deviceID, statusDescription);
        }
    }

    private void sendTimeoutMsg() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeMessages(MSG_COMMUNICATION_TIMEOUT);
            this.handler.sendEmptyMessageDelayed(MSG_COMMUNICATION_TIMEOUT, 1200000L);
            LogUtils.d(TAG, "sendTimeoutMsg, Delay : 1200000");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeTimeoutMsg() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeMessages(MSG_COMMUNICATION_TIMEOUT);
        }
    }

    private void handleTimeoutMsg() {
        LogUtils.d(TAG, "handleTimeoutMsg, currentStatus : " + this.communicationStatus.toString());
        onFailed(101);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFailed(int statusDescription) {
        LogUtils.e(TAG, "onFailed status = " + statusDescription);
        PsoClientInfo cInfo = new PsoClientInfo(getClientIdentifyInfo());
        ThreadUtil.runOnChildThread(new AnonymousClass2(statusDescription, cInfo));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xpeng.upso.proxy.ProxySocketClientRunnable$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ PsoClientInfo val$cInfo;
        final /* synthetic */ int val$statusDescription;

        AnonymousClass2(final int val$statusDescription, final PsoClientInfo val$cInfo) {
            this.val$statusDescription = val$statusDescription;
            this.val$cInfo = val$cInfo;
        }

        @Override // java.lang.Runnable
        public void run() {
            ProxySocketClientRunnable proxySocketClientRunnable = ProxySocketClientRunnable.this;
            proxySocketClientRunnable.onPresetFailedReport(this.val$statusDescription, proxySocketClientRunnable.op, this.val$cInfo);
            ProxySocketClientRunnable proxySocketClientRunnable2 = ProxySocketClientRunnable.this;
            final int i = this.val$statusDescription;
            proxySocketClientRunnable2.send2ClientOptionResult(i, new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$2$y8sB47RLVMTyDs4xNN6v3ZqEhH4
                @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
                public final void onSended(boolean z) {
                    ProxySocketClientRunnable.AnonymousClass2.this.lambda$run$1$ProxySocketClientRunnable$2(i, z);
                }
            });
        }

        public /* synthetic */ void lambda$run$1$ProxySocketClientRunnable$2(final int statusDescription, boolean res) {
            try {
                ThreadUtil.runOnMainThreadDelay(new Runnable() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$2$-aUcp6f2APaHTVTIvYQQ_DgSYpc
                    @Override // java.lang.Runnable
                    public final void run() {
                        ProxySocketClientRunnable.AnonymousClass2.this.lambda$run$0$ProxySocketClientRunnable$2(statusDescription);
                    }
                }, 1502L);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(ProxySocketClientRunnable.TAG, e.toString());
            }
        }

        public /* synthetic */ void lambda$run$0$ProxySocketClientRunnable$2(int statusDescription) {
            ProxySocketClientRunnable.this.clientEvent(ProxySocketClientRunnable.MSG_CLIENT_EVENT_FAILED, statusDescription);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPresetFailedReport(final int statusDescription, final int op, final PsoClientInfo cInfo) {
        LogUtils.e(TAG, "onPresetFailedReport");
        try {
            ThreadUtil.runOnChildThread(new Runnable() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$Be8CXnbFjyMlMGMBu5g4LQdJnTs
                @Override // java.lang.Runnable
                public final void run() {
                    ProxySocketClientRunnable.this.lambda$onPresetFailedReport$7$ProxySocketClientRunnable(cInfo, op, statusDescription);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public /* synthetic */ void lambda$onPresetFailedReport$7$ProxySocketClientRunnable(PsoClientInfo cInfo, int op, int statusDescription) {
        String clientLog;
        ProxyParamWrapper proxyParamWrapper;
        String local_log = LogUtils.getLog();
        byte[] logData = null;
        String clientType = null;
        if (cInfo != null) {
            clientType = cInfo.getClientEcuDeviceType();
        }
        if (clientType == null && (proxyParamWrapper = this.paramWrapper) != null && proxyParamWrapper.getClientType() != null) {
            clientType = this.paramWrapper.getClientType().toString();
        }
        try {
            if (clientType.equals(XpengPsoClientType.TBOX)) {
                logData = SshUtils.getTboxLog();
            } else if (clientType.equals(XpengPsoClientType.XPU)) {
                logData = SshUtils.getXpuLog(this.socket.getInetAddress().toString());
            }
            if (logData != null) {
                clientLog = new String(logData, "UTF-8");
            } else {
                clientLog = "get client log failed...";
            }
        } catch (Exception e) {
            e.printStackTrace();
            clientLog = "get client log failed:" + e.toString();
        }
        Throwable throwable = ExceptionMaps.getThrowable(op, cInfo.getClientCarDeviceType());
        SentryReporter errorCode = SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setClientType(clientType).setClientCarType(cInfo.getClientCarType()).setUiCarType(this.paramWrapper.getCarModel().toString()).setUiClientType(this.paramWrapper.getClientType().toString()).setEcuId(cInfo.getClientDeviceId()).setEcuIsUserVer(cInfo.EcuIsUserVer()).setEcuRomVer(cInfo.getEcuRomVer()).setOption(op == 1 ? "OP_START_PRESET" : "OP_START_VERIFY").setErrorCode(statusDescription);
        StringBuilder sb = new StringBuilder();
        sb.append(cInfo.getClientCarDeviceType());
        sb.append("_");
        sb.append(op != 1 ? "OP_START_VERIFY" : "OP_START_PRESET");
        sb.append("_failed");
        errorCode.setErrorDisc(sb.toString()).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibServerProxyVer("2.3.7").setProxyLog(local_log).setClientLog(clientLog).setDuration(System.currentTimeMillis() - this.taskStartTime).report(throwable);
    }

    private void onDoneReport(final int op, final PsoClientInfo cInfo) {
        LogUtils.e(TAG, "onPresetFailedReport");
        try {
            ThreadUtil.runOnChildThread(new Runnable() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$JMqBVd8ITvTBsa8tsUbL2QIm7nU
                @Override // java.lang.Runnable
                public final void run() {
                    ProxySocketClientRunnable.this.lambda$onDoneReport$8$ProxySocketClientRunnable(cInfo, op);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public /* synthetic */ void lambda$onDoneReport$8$ProxySocketClientRunnable(PsoClientInfo cInfo, int op) {
        ProxyParamWrapper proxyParamWrapper;
        String clientType = cInfo.getClientEcuDeviceType();
        if (clientType == null && (proxyParamWrapper = this.paramWrapper) != null && proxyParamWrapper.getClientType() != null) {
            clientType = this.paramWrapper.getClientType().toString();
        }
        Throwable option = OptionMaps.getOption(op, cInfo.getClientCarDeviceType());
        SentryReporter option2 = SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setClientType(clientType).setClientCarType(cInfo.getClientCarType()).setUiCarType(this.paramWrapper.getCarModel().toString()).setUiClientType(this.paramWrapper.getClientType().toString()).setEcuId(cInfo.getClientDeviceId()).setEcuIsUserVer(cInfo.EcuIsUserVer()).setEcuRomVer(cInfo.getEcuRomVer()).setOption(op == 1 ? "OP_START_PRESET" : "OP_START_VERIFY");
        StringBuilder sb = new StringBuilder();
        sb.append(cInfo.getClientCarDeviceType());
        sb.append("_");
        sb.append(op != 1 ? "OP_START_VERIFY" : "OP_START_PRESET");
        sb.append("_Done");
        option2.setErrorDisc(sb.toString()).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibServerProxyVer("2.3.7").setDuration(System.currentTimeMillis() - this.taskStartTime).report(option, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDone(final int statusDescription) {
        LogUtils.d(TAG, "onDone, status = " + statusDescription);
        PsoClientInfo cInfo = new PsoClientInfo(getClientIdentifyInfo());
        if (statusDescription == 3) {
            send2ClientOptionResult(3, new Send2clientCallBack() { // from class: com.xpeng.upso.proxy.-$$Lambda$ProxySocketClientRunnable$pCGDoCXCO6iu2AP_dPI_vw6AE54
                @Override // com.xpeng.upso.proxy.ProxySocketClientRunnable.Send2clientCallBack
                public final void onSended(boolean z) {
                    ProxySocketClientRunnable.this.lambda$onDone$9$ProxySocketClientRunnable(statusDescription, z);
                }
            });
        } else {
            clientEvent(MSG_CLIENT_EVENT_DONE, statusDescription);
        }
        if (statusDescription == 3 || statusDescription == 2) {
            onDoneReport(this.op, cInfo);
        }
    }

    public /* synthetic */ void lambda$onDone$9$ProxySocketClientRunnable(int statusDescription, boolean res) {
        clientEvent(MSG_CLIENT_EVENT_DONE, statusDescription);
    }

    private synchronized void shutdown() {
        LogUtils.d(TAG, "shutdown, close socket");
        this.isShutdown = true;
        if (this.socket == null) {
            return;
        }
        try {
            this.socket.getInputStream().close();
        } catch (Exception e) {
        }
        try {
            this.socket.getOutputStream().close();
        } catch (Exception e2) {
        }
        try {
            if (!this.socket.isClosed()) {
                this.socket.setSoTimeout(1);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            this.socket.close();
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        this.socket = null;
        try {
            this.handler.removeMessages(MSG_COMMUNICATION_TIMEOUT);
            this.handler.removeCallbacksAndMessages(null);
        } catch (Exception e5) {
            e5.printStackTrace();
            LogUtils.e(TAG, "handler remove " + e5.toString());
        }
        this.clientIdentifyInfo = null;
        this.handler = null;
        LogUtils.d(TAG, "\n----------Shut Down----------\n");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clientEvent(int even, int statusDescription) {
        Handler handler = this.handler;
        if (handler != null) {
            Message msg = handler.obtainMessage(even, Integer.valueOf(statusDescription));
            this.handler.sendMessage(msg);
        }
    }
}
