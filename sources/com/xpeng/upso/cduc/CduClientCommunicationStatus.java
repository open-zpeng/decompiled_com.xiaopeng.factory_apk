package com.xpeng.upso.cduc;
/* loaded from: classes2.dex */
public enum CduClientCommunicationStatus {
    CCCS_INIT(0, "CCCS_INIT"),
    CCCS_PB_WAIT_REQ_INFO(1, "CCCS_PB_WAIT_REQ_INFO"),
    CCCS_PB_WAIT_PRESET(2, "CCCS_PB_WAIT_PRESET"),
    CCCS_PB_WAIT_DECRYPT(3, "CCCS_PB_WAIT_DECRYPT"),
    CCCS_PB_WAIT_ENCRYPT(4, "CCCS_PB_WAIT_ENCRYPT"),
    CCCS_DONE(5, "CCCS_DONE");
    
    private String desc;
    private int index;

    CduClientCommunicationStatus(int index, String desc) {
        this.index = index;
        this.desc = desc;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
