package com.xpeng.upso.aesserver;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum MagicNumber {
    E38_CDU(AesConstants.SERVER_MAGIC_CDU),
    E38_TBOX(AesConstants.SERVER_MAGIC_TBOX),
    E38_XPU(AesConstants.SERVER_MAGIC_XDU),
    E38V_TBOX("EA"),
    E38V_XPU("EB"),
    E28A_TBOX("EG"),
    E28A_XPU("ED"),
    E28AV_TBOX("EE"),
    E28AV_XPU("EF"),
    F30_TBOX("FT"),
    F30_XPU("FX"),
    H93_TBOX("HT"),
    H93_XPU("HX");
    
    private String value;

    MagicNumber(String value) {
        this.value = value;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.value;
    }

    public String getValue() {
        return this.value;
    }
}
