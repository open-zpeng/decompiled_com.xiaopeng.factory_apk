package com.xpeng.upso;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum XpengCarChipModel {
    QUALCOMM_8155(0, "QUALCOMM_8155"),
    QUALCOMM_8155p(1, "QUALCOMM_8155p"),
    QUALCOMM_820A(2, "QUALCOMM_820A"),
    QUALCOMM_8295(3, "QUALCOMM_8295"),
    QUALCOMM_8295p(4, "QUALCOMM_8295p");
    
    private String desc;
    private int index;

    XpengCarChipModel(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
