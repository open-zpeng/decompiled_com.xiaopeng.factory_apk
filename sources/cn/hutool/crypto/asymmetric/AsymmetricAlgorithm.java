package cn.hutool.crypto.asymmetric;

import com.irdeto.securesdk.upgrade.O00000Oo;
/* loaded from: classes.dex */
public enum AsymmetricAlgorithm {
    RSA(O00000Oo.O000000o),
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),
    RSA_ECB("RSA/ECB/NoPadding"),
    RSA_None("RSA/None/NoPadding");
    
    private final String value;

    AsymmetricAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
