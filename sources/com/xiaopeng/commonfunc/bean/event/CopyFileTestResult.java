package com.xiaopeng.commonfunc.bean.event;
/* loaded from: classes.dex */
public class CopyFileTestResult {
    public boolean mPass;
    public long mUsedTime;

    public CopyFileTestResult(long time, boolean result) {
        this.mUsedTime = time;
        this.mPass = result;
    }
}
