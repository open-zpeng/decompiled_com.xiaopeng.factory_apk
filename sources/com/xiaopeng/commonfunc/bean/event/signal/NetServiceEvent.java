package com.xiaopeng.commonfunc.bean.event.signal;
/* loaded from: classes.dex */
public class NetServiceEvent {
    private final String mNetService;

    public NetServiceEvent(String netService) {
        this.mNetService = netService;
    }

    public String getNetService() {
        return this.mNetService;
    }
}
