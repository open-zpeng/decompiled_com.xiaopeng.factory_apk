package cn.hutool.crypto.digest;

import net.lingala.zip4j.util.InternalZipConstants;
/* loaded from: classes.dex */
public enum HmacAlgorithm {
    HmacMD5("HmacMD5"),
    HmacSHA1(InternalZipConstants.AES_MAC_ALGORITHM),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),
    HmacSM3("HmacSM3");
    
    private final String value;

    HmacAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
