package cn.hutool.crypto.symmetric;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.KeyUtil;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
/* loaded from: classes.dex */
public class PBKDF2 {
    private String algorithm;
    private int iterationCount;
    private int keyLength;

    public PBKDF2() {
        this.algorithm = "PBKDF2WithHmacSHA1";
        this.keyLength = 512;
        this.iterationCount = 1000;
    }

    public PBKDF2(String algorithm, int keyLength, int iterationCount) {
        this.algorithm = "PBKDF2WithHmacSHA1";
        this.keyLength = 512;
        this.iterationCount = 1000;
        this.algorithm = algorithm;
        this.keyLength = keyLength;
        this.iterationCount = iterationCount;
    }

    public byte[] encrypt(char[] password, byte[] salt) {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, this.iterationCount, this.keyLength);
        SecretKey secretKey = KeyUtil.generateKey(this.algorithm, pbeKeySpec);
        return secretKey.getEncoded();
    }

    public String encryptHex(char[] password, byte[] salt) {
        return HexUtil.encodeHexStr(encrypt(password, salt));
    }
}
