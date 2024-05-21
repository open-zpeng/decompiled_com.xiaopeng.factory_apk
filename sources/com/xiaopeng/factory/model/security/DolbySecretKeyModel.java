package com.xiaopeng.factory.model.security;

import android.pso.XpPsoException;
import android.text.TextUtils;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.xmlconfig.Support;
import com.xpeng.dolbypresetpso.DolbyErrorCode;
import com.xpeng.dolbypresetpso.DolbyPresetImpl;
import com.xpeng.dolbypresetpso.IDolbyPreset;
import com.xpeng.dolbypresetpso.IDolbyPresetResultCallback;
import com.xpeng.dolbyverifypso.DolbyVerifyImpl;
import com.xpeng.dolbyverifypso.IDolbyVerify;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import java.security.KeyStore;
import java.security.KeyStoreException;
/* loaded from: classes2.dex */
public class DolbySecretKeyModel {
    private static final String PSO_SERVER_CAR_TYPE = Support.Feature.getString(Support.Feature.PSO_SERVER_CAR_TYPE);
    private static final String TAG = "DolbySecretKeyModel";
    private static final String XPENG_ANDROID_KEY_STORE = "XpengAndroidKeyStore";
    private XpengCarChipModel mCarChipModel;
    private XpengCarModel mCarModel;
    private KeyStore mKeyStore;
    private IDolbyVerify mDolbyVerify = new DolbyVerifyImpl(false, MyApplication.getContext());
    private IDolbyPreset mDolbyPreset = new DolbyPresetImpl(false, MyApplication.getContext());

    public DolbySecretKeyModel(XpengCarModel mCarModel, XpengCarChipModel mCarChipModel) {
        this.mDolbyVerify.setParameters(mCarModel, mCarChipModel);
        this.mDolbyPreset.setParameters(mCarModel, mCarChipModel);
        this.mCarModel = mCarModel;
        this.mCarChipModel = mCarChipModel;
        try {
            this.mKeyStore = KeyStore.getInstance("XpengAndroidKeyStore");
            this.mKeyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDolbySecretKeyExist() {
        KeyStore keyStore;
        boolean res = false;
        try {
            res = this.mDolbyVerify.checkPreset();
        } catch (XpPsoException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "is Dolby SecretKey Exist? res:" + res);
        if (res) {
            String alias = this.mDolbyPreset.genAlias(this.mCarModel, this.mCarChipModel);
            if (!TextUtils.isEmpty(alias) && (keyStore = this.mKeyStore) != null) {
                boolean flag = false;
                try {
                    flag = keyStore.isKeyEntry(alias);
                } catch (KeyStoreException e2) {
                    e2.printStackTrace();
                }
                LogUtils.i(TAG, "Is Dolby SecretKey Alias Exist:" + flag);
            }
        }
        return res;
    }

    public boolean verifyDolbySecretKey() {
        boolean res = false;
        try {
            res = this.mDolbyVerify.checkCrypo();
        } catch (XpPsoException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "verify Dolby SecretKey res:" + res);
        return res;
    }

    public void asyncGetKey(final Runnable callback) {
        this.mDolbyPreset.presetAesKeyFromNetwork(new IDolbyPresetResultCallback() { // from class: com.xiaopeng.factory.model.security.DolbySecretKeyModel.1
            @Override // com.xpeng.dolbypresetpso.IDolbyPresetResultCallback
            public void onPresetResult(boolean res, DolbyErrorCode err) {
                LogUtils.i(DolbySecretKeyModel.TAG, "preset SecretKey res:" + res);
                ThreadUtils.execute(callback);
            }
        });
    }
}
