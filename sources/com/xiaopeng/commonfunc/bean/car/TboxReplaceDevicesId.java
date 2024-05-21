package com.xiaopeng.commonfunc.bean.car;

import cn.hutool.core.text.CharPool;
/* loaded from: classes.dex */
public class TboxReplaceDevicesId {
    public String ble_mac;
    public String iccid;
    public String nfc_seid;

    public TboxReplaceDevicesId(String ble_mac, String nfc_seid, String iccid) {
        this.ble_mac = ble_mac;
        this.nfc_seid = nfc_seid;
        this.iccid = iccid;
    }

    public String getBle_mac() {
        return this.ble_mac;
    }

    public String getNfc_seid() {
        return this.nfc_seid;
    }

    public String getIccid() {
        return this.iccid;
    }

    public String toString() {
        return "TboxReplaceDevicesId{ble_mac='" + this.ble_mac + CharPool.SINGLE_QUOTE + ", nfc_seid='" + this.nfc_seid + CharPool.SINGLE_QUOTE + ", iccid='" + this.iccid + CharPool.SINGLE_QUOTE + '}';
    }
}
