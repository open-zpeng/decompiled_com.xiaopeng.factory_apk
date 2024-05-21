package com.xiaopeng.commonfunc.bean.http;

import cn.hutool.core.text.CharPool;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class CellularServerBean {
    @SerializedName("respCode")
    private String mRespCode;
    @SerializedName("respDesc")
    private String mRespDesc;
    @SerializedName("respInfo")
    private String mRespInfo;

    public String getRespCode() {
        return this.mRespCode;
    }

    public void setRespCode(String code) {
        this.mRespCode = code;
    }

    public int getIntCode() {
        try {
            return Integer.valueOf(this.mRespCode).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getRespDesc() {
        return this.mRespDesc;
    }

    public void setRespDesc(String msg) {
        this.mRespDesc = msg;
    }

    public String getRespInfo() {
        return this.mRespInfo;
    }

    public void setRespInfo(String data) {
        this.mRespInfo = data;
    }

    public String toString() {
        return "CellularServerBean{mRespCode='" + this.mRespCode + CharPool.SINGLE_QUOTE + ", mRespDesc='" + this.mRespDesc + CharPool.SINGLE_QUOTE + ", mRespInfo=" + this.mRespInfo + '}';
    }
}
