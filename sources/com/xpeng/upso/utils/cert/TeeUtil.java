package com.xpeng.upso.utils.cert;

import androidx.annotation.Keep;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.xpeng.upso.aesserver.AesConstants;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
@Keep
/* loaded from: classes2.dex */
public class TeeUtil {
    private static final Type KEY_MAP_TYPE = new TypeReference<Map<Integer, AesKeyBo>>() { // from class: com.xpeng.upso.utils.cert.TeeUtil.1
    }.getType();
    private static final Type KEY_MAP_STRING_TYPE = new TypeReference<Map<String, String>>() { // from class: com.xpeng.upso.utils.cert.TeeUtil.2
    }.getType();

    /* JADX WARN: Type inference failed for: r0v0, types: [com.xpeng.upso.utils.cert.TeeUtil$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.xpeng.upso.utils.cert.TeeUtil$2] */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encode(TeeEncrpytedContent teeEncrpytedContent) {
        int byteLength = teeEncrpytedContent.getDeviceId().length() + 15 + teeEncrpytedContent.getIv().length + teeEncrpytedContent.getContent().length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteLength);
        byteBuffer.put(teeEncrpytedContent.getMagicNumber().getBytes());
        byteBuffer.put(teeEncrpytedContent.getVersion().byteValue());
        byteBuffer.put(teeEncrpytedContent.getKeyIndex().byteValue());
        byteBuffer.put((byte) teeEncrpytedContent.getDeviceId().length());
        byteBuffer.put(teeEncrpytedContent.getDeviceId().getBytes());
        byteBuffer.put(teeEncrpytedContent.getNonceLength().byteValue());
        byteBuffer.putLong(teeEncrpytedContent.getTimestamp().longValue());
        byteBuffer.put((byte) teeEncrpytedContent.getIv().length);
        byteBuffer.put(teeEncrpytedContent.getIv());
        byteBuffer.put(teeEncrpytedContent.getContent());
        return byteBuffer.array();
    }

    public static TeeEncrpytedContent decode(byte[] bytes) {
        TeeEncrpytedContent teeEncrpytedContent = new TeeEncrpytedContent();
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byte[] magicNumberBytes = new byte[2];
        byteBuffer.get(magicNumberBytes);
        teeEncrpytedContent.setMagicNumber(new String(magicNumberBytes));
        int version = byteBuffer.get();
        teeEncrpytedContent.setVersion(Integer.valueOf(version));
        int keyIndex = byteBuffer.get();
        teeEncrpytedContent.setKeyIndex(Integer.valueOf(keyIndex));
        int deviceIdLength = byteBuffer.get();
        byte[] deviceIdBytes = new byte[deviceIdLength];
        byteBuffer.get(deviceIdBytes);
        teeEncrpytedContent.setDeviceId(new String(deviceIdBytes));
        int nonceLength = byteBuffer.get();
        teeEncrpytedContent.setNonceLength(Integer.valueOf(nonceLength));
        long timestamp = byteBuffer.getLong();
        teeEncrpytedContent.setTimestamp(Long.valueOf(timestamp));
        int ivLength = byteBuffer.get();
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        teeEncrpytedContent.setIv(iv);
        byte[] content = new byte[byteBuffer.remaining()];
        byteBuffer.get(content);
        teeEncrpytedContent.setContent(content);
        return teeEncrpytedContent;
    }

    public static Map<Integer, AesKeyBo> decryptSecreteKey(String deviceId, String secreteKey) throws Exception {
        byte[] enCodeFormat = md5(deviceId);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(decodeBASE64(secreteKey));
        String jsonBody = new String(decryptedBytes, StandardCharsets.UTF_8);
        return (Map) JSON.parseObject(jsonBody, KEY_MAP_TYPE, new Feature[0]);
    }

    public static byte[] encryptAESCBCPkcs7Padding(String content, String nounce, byte[] key, byte[] iv) throws Exception {
        String content2 = nounce + content;
        Cipher cipher = initCipherCBC(1, key, iv);
        return cipher.doFinal(content2.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptAESCBCPkcs7Padding(TeeEncrpytedContent content, byte[] key) throws Exception {
        Cipher cipher = initCipherCBC(2, key, content.getIv());
        byte[] decryptedBytes = cipher.doFinal(content.getContent());
        int rawDataBytesLength = decryptedBytes.length - content.getNonceLength().intValue();
        byte[] rawDataBytes = new byte[rawDataBytesLength];
        System.arraycopy(decryptedBytes, content.getNonceLength().intValue(), rawDataBytes, 0, rawDataBytesLength);
        return new String(rawDataBytes, StandardCharsets.UTF_8);
    }

    private static Cipher initCipherCBC(int mode, byte[] key, byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec parameterSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(mode, secretKeySpec, parameterSpec);
        return cipher;
    }

    private static byte[] md5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes(StandardCharsets.UTF_8));
            return messageDigest.digest();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static byte[] decodeBASE64(String value) {
        return Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String sign(String requestBody) {
        MD5 md5 = MD5.create();
        String s = requestBody + AesConstants.SERVER_SIGN_SAIT;
        return md5.digestHex(s);
    }

    public static Map<String, String> parseKeyMap(String encryptStr, String deviceId) throws Exception {
        byte[] enCodeFormat = md5(deviceId);
        SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(decodeBASE64(encryptStr));
        String jsonBody = new String(decryptedBytes, StandardCharsets.UTF_8);
        return (Map) JSON.parseObject(jsonBody, KEY_MAP_STRING_TYPE, new Feature[0]);
    }

    public static void main(String[] args) throws Exception {
        Map<Integer, AesKeyBo> aesKeyBoMap = decryptSecreteKey("test001", "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");
        TeeEncrpytedContent teeEncrpytedContent = new TeeEncrpytedContent();
        teeEncrpytedContent.setMagicNumber(AesConstants.SERVER_MAGIC_CDU);
        teeEncrpytedContent.setVersion(2);
        teeEncrpytedContent.setKeyIndex(127);
        teeEncrpytedContent.setNonceLength(32);
        teeEncrpytedContent.setDeviceId("test001");
        teeEncrpytedContent.setTimestamp(Long.valueOf(System.currentTimeMillis()));
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        teeEncrpytedContent.setIv(iv);
        String nounce = RandomStringUtils.randomAlphanumeric(32);
        byte[] aescbcNoPadding = encryptAESCBCPkcs7Padding("业务原文", nounce, decodeBASE64(aesKeyBoMap.get(teeEncrpytedContent.getKeyIndex()).getKey()), teeEncrpytedContent.getIv());
        teeEncrpytedContent.setContent(aescbcNoPadding);
        String dec = decryptAESCBCPkcs7Padding(teeEncrpytedContent, decodeBASE64(aesKeyBoMap.get(teeEncrpytedContent.getKeyIndex()).getKey()));
        PrintStream printStream = System.out;
        printStream.println("解密：" + dec);
        byte[] bytes = encode(teeEncrpytedContent);
        Base64.getEncoder().encodeToString(bytes);
        TeeEncrpytedContent aa = decode(bytes);
        System.out.println(aa.getDeviceId());
        TeeEncrpytedContent bb = decode(decodeBASE64("RUMCfwd0ZXN0MDAxIAAAAX/ZeU7GECSE884P8ZPzTlCCMiJ29b6nlFXJHBX5szVMO0sX4i+eH+iBM/HwGdIQUvaXcuROmTzSp21xxze7Fpnr7KGP6yU="));
        String rawData = decryptAESCBCPkcs7Padding(bb, decodeBASE64(aesKeyBoMap.get(bb.getKeyIndex()).getKey()));
        PrintStream printStream2 = System.out;
        printStream2.println("明文：" + rawData);
        String sign = sign("{\"deviceId\": \"test001\",\"deviceType\": \"E38_CDU\",\"version\": 2}");
        System.out.println(sign);
        Map<String, String> map = parseKeyMap("KnOBT0NWbrBukMQgzs23z9TGBe25VgjtwPWupi2fYT3tdIOwnuXRbMfXRjoJZ5GcyYUrqq3lrZXr1/l5445uBvLO9XbZ4K+pO14Ww4MNqdjHUHcAc1k9v1oHttIbFPNrBdJcTSUm4AuejdHKak8aG+VuV98p53Pk3s93SX6pQutk0HGPydOUEQY2gv3OgeBkCrR7UAuLUirmA6m6ORqJYt2fIHuZe0iaiye1QFePKIm+4cq6VfVooKGHJBDrxmvPFo4LcEpeO/a8oQCHL2W4sGtB3zEGO0vwIWcxrqk/JJ6b3ac2bjlF9Gc69+40L8HZAOml51O00+EzoscuWYQspFNMlLzm5lXnL8WVp67hQhWAkJHD7I0YOeczrvbVnIZMoRAXHQXv0ZbIAUdSjYIVhuGzlLe9TdXxnqQPzlP+f9Kvf2v4dZGg6JguX8hRhXzt+BjiVYkzmcH4EXZ43OM53mchF/QM3I9+P6xd0D7aaf6MgrPLsuPU3UGlPj+uqUdCx7OtQdzjQs2ai4VkeeBc8WjGZYyMN+6GSJKoroim869hlIpUGNI1tGIPA4/QN6yb", "test001");
        System.out.println(JSON.toJSON(map));
        Map<String, String> xpuMap = parseKeyMap("XxvVlHNRia3kpROBlaJ3nzQq4VX/VGkUv++vVOgF1rJItderbB53mG+ZCsWedUeRs8aYZgZewy1HXRjBILwtWuDJjkxtZU+zkrwF9Wa2LD/3XMvlFqF6SybynLttfCfBASRrh1Em31b6WgCcBZqLIlw6KQl3m5YcBj9Of3lsDBM=", "test001");
        System.out.println(JSON.toJSON(xpuMap));
    }
}
