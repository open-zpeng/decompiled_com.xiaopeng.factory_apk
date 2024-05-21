package com.xpeng.upso;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public enum XpengSecretType {
    AES_32(0, "AES_32"),
    CERT_P12(1, "CERT_P12"),
    RSA_PUB(2, "RSA_PUB"),
    RSA_PRI(3, "RSA_PRI");
    
    private String desc;
    private int index;

    XpengSecretType(int i, String str) {
        this.index = i;
        this.desc = str;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.desc;
    }
}
