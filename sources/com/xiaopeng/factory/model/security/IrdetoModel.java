package com.xiaopeng.factory.model.security;

import com.irdeto.securesdk.isf;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes2.dex */
public class IrdetoModel {
    public static final String CERT_PATH = "/mnt/vendor/private/sec/xp0109.png";
    private static final String CYPTO_IV = "00000000000000000000000000000000";
    private static final String CYPTO_KEY = "6368656e677a69636172303130393137";
    public static final String OLD_CERT_PATH = "/private/xp0109.png";

    public static byte[] cryptoDecode(byte[] data) {
        try {
            return isf.isfCryptoOperate("IR_CRYPTO_AES_CBC_DECRYPT", "aes_id1", CYPTO_IV, data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] cryptoEncode(byte[] data) {
        try {
            return aesCbcEncrypt(data, CYPTO_KEY, CYPTO_IV);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] aesCbcEncrypt(byte[] sSrc, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = hexStringToByteArray(sKey);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(hexStringToByteArray(ivParameter));
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc);
        return encrypted;
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
