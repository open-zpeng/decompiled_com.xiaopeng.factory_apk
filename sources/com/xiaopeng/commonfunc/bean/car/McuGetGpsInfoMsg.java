package com.xiaopeng.commonfunc.bean.car;
/* loaded from: classes.dex */
public class McuGetGpsInfoMsg {
    public int mGpsMsg;

    public McuGetGpsInfoMsg(int data) {
        this.mGpsMsg = data;
    }

    public String toString() {
        String str = "mGpsMsg=" + this.mGpsMsg;
        return str;
    }
}
