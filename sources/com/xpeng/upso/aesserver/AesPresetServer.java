package com.xpeng.upso.aesserver;

import com.xpeng.upso.aesserver.PresetTask;
import com.xpeng.upso.http.UpsoCertResponse;
import com.xpeng.upso.http.UpsoResponse;
import com.xpeng.upso.utils.LogUtils;
/* loaded from: classes2.dex */
public class AesPresetServer {
    private static final String TAG = "AesPresetServer";
    private IPsuResponseCallback presetResultCallback;

    public void setPresetResultCallback(IPsuResponseCallback callback) {
        this.presetResultCallback = callback;
    }

    public boolean requestFromNetwork(PresetParam requestParma) {
        if (requestParma == null) {
            return false;
        }
        PresetTask<UpsoResponse> presetTask = new PresetTask<>();
        presetTask.setTboxPresetSecretCallback(new PresetTask.IPsuServerResponseCallback() { // from class: com.xpeng.upso.aesserver.AesPresetServer.1
            @Override // com.xpeng.upso.aesserver.PresetTask.IPsuServerResponseCallback
            public void onPsuServerResponse(int requestType, Object response) {
                AesPresetServer.this.AesResHandle(requestType, response);
            }

            @Override // com.xpeng.upso.aesserver.PresetTask.IPsuServerResponseCallback
            public void onPsuServerResponseIsNull(int requestType) {
                AesPresetServer.this.onHttpResponseIsNull(requestType);
            }
        });
        presetTask.execute(requestParma);
        return true;
    }

    public boolean requestFromNetworkAfterXpuCert(PresetParam requestParma, final UpsoCertResponse certResponse) {
        LogUtils.i(TAG, "AesPresetTaskAfterXpuCert");
        PresetTask<UpsoResponse> presetTask = new PresetTask<>();
        presetTask.setTboxPresetSecretCallback(new PresetTask.IPsuServerResponseCallback() { // from class: com.xpeng.upso.aesserver.AesPresetServer.2
            @Override // com.xpeng.upso.aesserver.PresetTask.IPsuServerResponseCallback
            public void onPsuServerResponse(int requestType, Object response) {
                if (requestType == 10) {
                    AesPresetServer.this.presetResultCallback.onSecretAesCertResponse((AesSecretResponse) response, certResponse);
                    return;
                }
                LogUtils.i(AesPresetServer.TAG, "requestType error:" + requestType);
            }

            @Override // com.xpeng.upso.aesserver.PresetTask.IPsuServerResponseCallback
            public void onPsuServerResponseIsNull(int requestType) {
                AesPresetServer.this.onHttpResponseIsNull(requestType);
            }
        });
        presetTask.execute(requestParma);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void AesResHandle(int requestType, Object aesResp) {
        LogUtils.d(TAG, "PresetTask,AesResHandle: requestType=" + requestType);
        IPsuResponseCallback iPsuResponseCallback = this.presetResultCallback;
        if (iPsuResponseCallback == null) {
            LogUtils.e(TAG, "PresetTask presetResultCallback == null");
        } else if (requestType == 1) {
            iPsuResponseCallback.onSecretResponse((AesSecretResponse) aesResp);
        } else if (requestType == 2) {
            iPsuResponseCallback.onEncryptResponse((AesEncryptResponse) aesResp);
        } else if (requestType == 3) {
            iPsuResponseCallback.onDencryptResponse((AesDecryptResponse) aesResp);
        } else if (requestType == 4) {
            iPsuResponseCallback.onEncryptMultiResponse((AesEncryptMultiResponse) aesResp);
        } else if (requestType == 5) {
            iPsuResponseCallback.onDecryptMultiResponse((AesDecryptMultiResponse) aesResp);
        } else if (requestType == 11) {
            iPsuResponseCallback.onCertListResponse((UpsoCertResponse) aesResp);
        } else {
            iPsuResponseCallback.onRequestTypeError(requestType);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onHttpResponseIsNull(int requestType) {
        this.presetResultCallback.onResponseNull(requestType);
    }
}
