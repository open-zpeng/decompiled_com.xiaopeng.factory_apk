package com.xpeng.upso.proxy;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
/* loaded from: classes2.dex */
public class EncryptAndDecrypt {
    public static String encodeBASE64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeBASE64(String value) {
        return Base64.getMimeDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
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
}
