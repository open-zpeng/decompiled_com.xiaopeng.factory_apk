package com.xiaopeng.factory.model.security;

import android.pso.PsoCrypto;
import android.pso.XpPso;
import com.xiaopeng.commonfunc.utils.SystemPropertyUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes2.dex */
public class PsoModel {
    private static final String TAG = "PsoModel";
    private final PsoCrypto mPsoCrypto = new XpPso(MyApplication.getContext());

    public PsoModel() {
        this.mPsoCrypto.set_carType(Support.Feature.getInt(Support.Feature.PSO_CAR_TYPE));
    }

    public String getPsuId() {
        String psuid = null;
        try {
            psuid = this.mPsoCrypto.GetPSUID(SystemPropertyUtil.getHardwareId());
        } catch (Exception e) {
            LogUtils.e(TAG, "GetPSUID Failed... Descrition:" + e);
        }
        LogUtils.e(TAG, "GetPSUID:" + psuid);
        return psuid;
    }

    public boolean presetCertKey(String certPath) {
        boolean res = false;
        try {
            res = this.mPsoCrypto.CertKeyPreset(certPath);
        } catch (Exception e) {
            LogUtils.e(TAG, "CertKeyPreset Failed...Descrition:" + e);
        }
        LogUtils.e(TAG, "presetCertKey:" + res);
        return res;
    }

    public boolean presetMcuKey() {
        boolean res = false;
        try {
            res = this.mPsoCrypto.McuKeyPreset(SystemPropertyUtil.getHardwareId());
        } catch (Exception e) {
            LogUtils.e(TAG, "McuKeyPreset Failed...Descrition:" + e);
        }
        LogUtils.e(TAG, "presetMcuKey:" + res);
        return res;
    }

    public boolean verifyCertKey(String path) {
        LogUtils.i(TAG, "verifyCertKey");
        boolean res = false;
        try {
            res = this.mPsoCrypto.CertKeyVerify(path);
        } catch (Exception e) {
            LogUtils.e(TAG, "CertKeyVerify Failed...Descrition:" + e);
        }
        LogUtils.e(TAG, "verifyCertKey:" + res);
        return res;
    }

    public boolean checkIfPreseted() {
        boolean res = false;
        try {
            res = this.mPsoCrypto.checkIfPreseted();
        } catch (Exception e) {
            LogUtils.e(TAG, "checkIfPreseted Failed...Descrition:" + e);
        }
        LogUtils.e(TAG, "checkIfPreseted:" + res);
        return res;
    }
}
