package com.xpeng.upso.aesserver;

import com.xpeng.upso.http.UpsoCertResponse;
/* loaded from: classes2.dex */
public interface IPsuResponseCallback {
    void onCertListResponse(UpsoCertResponse response);

    void onDecryptMultiResponse(AesDecryptMultiResponse response);

    void onDencryptResponse(AesDecryptResponse response);

    void onEncryptMultiResponse(AesEncryptMultiResponse response);

    void onEncryptResponse(AesEncryptResponse response);

    void onRequestTypeError(int type);

    void onResponseNull(int requestType);

    void onSecretAesCertResponse(AesSecretResponse resAes, UpsoCertResponse resCert);

    void onSecretResponse(AesSecretResponse response);
}
