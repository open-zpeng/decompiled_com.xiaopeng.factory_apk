package com.xpeng.upso.proxy;

import androidx.annotation.Keep;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.aesserver.AesConstants;
import com.xpeng.upso.proxy.PSOProtocol;
import com.xpeng.upso.utils.LogUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
@Keep
/* loaded from: classes2.dex */
public class PsoClientInfo {
    private static final String TAG = "PsoClientInfo";
    private PSOProtocol.IdentityInfo clientIdentifyInfo;

    public PsoClientInfo(PSOProtocol.IdentityInfo cInfo) {
        init(cInfo);
    }

    public void init(PSOProtocol.IdentityInfo cInfo) {
        this.clientIdentifyInfo = copyClientIdentifyInfo(cInfo);
    }

    public PSOProtocol.IdentityInfo copyClientIdentifyInfo(PSOProtocol.IdentityInfo info) {
        if (info != null) {
            try {
                if (info.isInitialized() && !ArrayUtils.isEmpty(info.toByteArray())) {
                    byte[] data = new byte[info.toByteArray().length];
                    System.arraycopy(info.toByteArray(), 0, data, 0, data.length);
                    PSOProtocol.IdentityInfo onfo_t = PSOProtocol.IdentityInfo.parseFrom(data);
                    return onfo_t;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "setClientIdentifyInfo:" + e.toString());
                return null;
            }
        }
        return null;
    }

    public PSOProtocol.IdentityInfo getClientIdentifyInfo() {
        return this.clientIdentifyInfo;
    }

    public boolean hasSecret() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        return identityInfo != null && identityInfo.getHasSecret() == 1;
    }

    public boolean toUpdateSecret() {
        return !hasSecret();
    }

    public String getClientDeviceId() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        if (identityInfo == null) {
            return AesConstants.UNKNOWN_DEVICE_ID;
        }
        return identityInfo.getDeviceId();
    }

    public String getClientCarDeviceType() {
        try {
            if (this.clientIdentifyInfo != null && !StringUtils.isEmpty(this.clientIdentifyInfo.getCarType()) && !StringUtils.isEmpty(this.clientIdentifyInfo.getDeviceType())) {
                String clientDeviceType = this.clientIdentifyInfo.getCarType() + "_" + this.clientIdentifyInfo.getDeviceType();
                return clientDeviceType;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getClientEcuDeviceType() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        if (identityInfo != null) {
            return identityInfo.getDeviceType();
        }
        return null;
    }

    public String getClientCarType() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        if (identityInfo != null) {
            return identityInfo.getCarType();
        }
        return null;
    }

    public String getEcuPlatform() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        if (identityInfo != null) {
            return identityInfo.getPlatform();
        }
        return null;
    }

    public XpengCarModel getClientCarModel() {
        try {
            String carType = getClientCarType();
            if (carType == null) {
                return null;
            }
            return XpengCarModel.valueOf(carType);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    public boolean EcuIsUserVer() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        return identityInfo != null && identityInfo.getUser() == 1;
    }

    public String getEcuRomVer() {
        PSOProtocol.IdentityInfo identityInfo = this.clientIdentifyInfo;
        if (identityInfo != null) {
            return identityInfo.getRomInfo();
        }
        return null;
    }
}
