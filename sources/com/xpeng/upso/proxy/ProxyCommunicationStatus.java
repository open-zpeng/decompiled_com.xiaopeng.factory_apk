package com.xpeng.upso.proxy;
/* loaded from: classes2.dex */
public enum ProxyCommunicationStatus {
    PCS_INIT(0, "PCS_INIT"),
    PCS_PB_REQ_INFO(1, "PCS_PB_REQ_INFO"),
    PCS_HTTPS_REQ_SECRET(2, "PCS_HTTPS_REQ_SECRET"),
    PCS_PB_PRESET(3, "PCS_PB_PRESET"),
    PCS_HTTPS_REQ_ENCRYPT(4, "PCS_HTTPS_REQ_ENCRYPT"),
    PCS_PB_REQ_DECRYPT(5, "PCS_PB_REQ_DECRYPT"),
    PCS_PB_REQ_ENCRYPT(6, "PCS_PB_REQ_ENCRYPT"),
    PCS_HTTPS_REQ_DECRYPT(7, "PCS_HTTPS_REQ_DECRYPT"),
    PCS_DONE(8, "PCS_DONE");
    
    private String desc;
    private int index;

    ProxyCommunicationStatus(int index, String desc) {
        this.index = index;
        this.desc = desc;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
