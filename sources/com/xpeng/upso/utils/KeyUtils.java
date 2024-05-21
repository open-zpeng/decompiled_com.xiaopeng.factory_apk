package com.xpeng.upso.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes2.dex */
public class KeyUtils {
    public static byte[] decodeBASE64(String str) {
        return Base64Util.decode(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String genAlias(String str) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "HmacMD5");
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Base64Util.encodeToString(mac.doFinal(str.getBytes()));
    }

    public static byte[] md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
