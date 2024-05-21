package com.xiaopeng.commonfunc.bean.http;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class XpuActivateStatus {
    private long lastReplaceTime;
    private String newXpuSn;
    private int state = -99;
    private int stateCode = -99;
    private String stateDesc;
    private String stateMsg;
    private String xpuSn;

    public long getLastReplaceTime() {
        return this.lastReplaceTime;
    }

    public String getXpuSn() {
        return this.xpuSn;
    }

    public String getNewXpuSn() {
        return this.newXpuSn;
    }

    public int getState() {
        return this.state;
    }

    public String getStateMsg() {
        return this.stateMsg;
    }

    public int getStateCode() {
        return this.stateCode;
    }

    public String getStateDesc() {
        return this.stateDesc;
    }

    public String toString() {
        return "XpuActivateStatus{lastReplaceTime=" + this.lastReplaceTime + ", xpuSn='" + this.xpuSn + CharPool.SINGLE_QUOTE + ", newXpuSn='" + this.newXpuSn + CharPool.SINGLE_QUOTE + ", state=" + this.state + ", stateMsg='" + this.stateMsg + CharPool.SINGLE_QUOTE + ", stateCode=" + this.stateCode + ", stateDesc='" + this.stateDesc + CharPool.SINGLE_QUOTE + '}';
    }
}
