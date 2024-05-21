package com.xiaopeng.commonfunc.bean;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class ParamEncryptSh {
    private String download_path;
    private String md5;

    public ParamEncryptSh(String download_path, String md5) {
        this.download_path = download_path;
        this.md5 = md5;
    }

    public String getDownload_path() {
        return this.download_path;
    }

    public void setDownload_path(String download_path) {
        this.download_path = download_path;
    }

    public String getMd5() {
        return this.md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String toString() {
        return "ParamEncryptSh{download_path='" + this.download_path + CharPool.SINGLE_QUOTE + ", md5='" + this.md5 + CharPool.SINGLE_QUOTE + '}';
    }
}
