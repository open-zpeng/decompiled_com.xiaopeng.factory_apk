package com.xiaopeng.commonfunc.bean.event.signal;
/* loaded from: classes.dex */
public class BtRssiEvent {
    private final String rssi;

    public BtRssiEvent(String rssi) {
        this.rssi = rssi;
    }

    public String getRssi() {
        return this.rssi;
    }
}
