package com.xpeng.upso;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum XpengCarModel {
    E38(0, "E38"),
    E38V(1, "E38V"),
    E28A(2, "E28A"),
    E28AV(3, "E28AV"),
    F30(4, "F30"),
    F30V(5, "F30V"),
    F62(6, "F62"),
    F62V(7, "F62V"),
    H93(8, "H93"),
    H93V(9, "H93V"),
    D55(10, "D55"),
    E28(11, "E28"),
    D21(12, "D21"),
    D22(13, "D22");
    
    private String desc;
    private int index;

    XpengCarModel(int i, String str) {
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
