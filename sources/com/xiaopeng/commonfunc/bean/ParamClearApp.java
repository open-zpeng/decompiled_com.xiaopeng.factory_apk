package com.xiaopeng.commonfunc.bean;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class ParamClearApp {
    private String package_name;

    public ParamClearApp(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_name() {
        return this.package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String toString() {
        return "ParamClearApp{package_name='" + this.package_name + CharPool.SINGLE_QUOTE + '}';
    }
}
