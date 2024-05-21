package com.xpeng.upso.utils;

import net.lingala.zip4j.crypto.PBKDF2.BinTools;
/* loaded from: classes2.dex */
public class HexUtils {
    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bArr.length; i++) {
            sb.append(String.format("%02x", Byte.valueOf(bArr[i])));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByte(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        char[] charArray = str.toCharArray();
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = int16ToByte(hexStringToInt(charArray[i2]), hexStringToInt(charArray[i2 + 1]));
        }
        return bArr;
    }

    public static int hexStringToInt(char c) {
        return BinTools.hex.indexOf(Character.toUpperCase(c));
    }

    public static byte int16ToByte(int i, int i2) {
        if (i <= -1 || i2 >= 16 || i2 <= -1 || i2 >= 16) {
            return (byte) 0;
        }
        return (byte) ((((byte) i) << 4) | ((byte) i2));
    }

    public static byte int16ToByte(String str) {
        if (str.length() == 2) {
            return hexStringToByte(str)[0];
        }
        return (byte) 0;
    }
}
