package com.xiaopeng.commonfunc.bean;

import cn.hutool.core.text.CharPool;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class ParamCheckMode {
    @SerializedName("download_path")
    private String mDownloadPath;
    @SerializedName("jobNum")
    private String mJobNum;
    @SerializedName("vin")
    private String mVin;

    public ParamCheckMode(String mDownloadPath, String mJobNum, String mVin) {
        this.mDownloadPath = mDownloadPath;
        this.mJobNum = mJobNum;
        this.mVin = mVin;
    }

    public String getDownloadPath() {
        return this.mDownloadPath;
    }

    public void setDownloadPath(String mDownloadPath) {
        this.mDownloadPath = mDownloadPath;
    }

    public String getJobNum() {
        return this.mJobNum;
    }

    public void setJobNum(String mJobNum) {
        this.mJobNum = mJobNum;
    }

    public String getVin() {
        return this.mVin;
    }

    public void setVin(String mVin) {
        this.mVin = mVin;
    }

    public String toString() {
        return "ParamCheckMode{mDownloadPath='" + this.mDownloadPath + CharPool.SINGLE_QUOTE + ", mJobNum='" + this.mJobNum + CharPool.SINGLE_QUOTE + ", mVin='" + this.mVin + CharPool.SINGLE_QUOTE + '}';
    }
}
