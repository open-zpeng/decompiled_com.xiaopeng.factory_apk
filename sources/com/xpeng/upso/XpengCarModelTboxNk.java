package com.xpeng.upso;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum XpengCarModelTboxNk {
    E38(0, "E38"),
    E38V(1, "E38V"),
    E28A(2, "E28A"),
    E28AV(3, "E28AV"),
    F30(4, "F30"),
    F30V(5, "F30V");
    
    private String desc;
    private int index;

    XpengCarModelTboxNk(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    public static boolean checkValid(String str) {
        try {
            for (XpengCarModelTboxNk xpengCarModelTboxNk : values()) {
                if (xpengCarModelTboxNk.toString().equals(str)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
