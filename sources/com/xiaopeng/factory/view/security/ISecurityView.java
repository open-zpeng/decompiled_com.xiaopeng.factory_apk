package com.xiaopeng.factory.view.security;
/* loaded from: classes2.dex */
public interface ISecurityView {
    void updateCduAesKeyExistView(boolean z);

    void updateCduAesKeyVerifiedView(boolean z);

    void updateCduKeyFileView();

    void updateCduKeyVerifiedView(boolean z);

    void updateCduKeyView();

    void updateDolbySecretKeyExistView();

    void updateDolbySecretKeyVerifiedView(boolean z);

    void updateNeedEfuseEnabledView();

    void updatePsuKeyExistView();

    void updatePsuKeyVerifiedView(boolean z);

    void updateTboxAesKeyExistView(boolean z);

    void updateTboxAesKeyVerifiedView(boolean z);

    void updateTboxKeyVerifiedView(boolean z);

    void updateTboxKeyView(boolean z);

    void updateWifiConnectionStatus(boolean z);

    void updateWifiKeyExistView();

    void updateWifiKeyVerifiedView(boolean z);
}
