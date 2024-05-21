package cn.hutool.core.codec;
/* loaded from: classes.dex */
public class Rot {
    private static final char ACHAR = 'A';
    private static final char CHAR0 = '0';
    private static final char CHAR9 = '9';
    private static final char ZCHAR = 'Z';
    private static final char aCHAR = 'a';
    private static final char zCHAR = 'z';

    public static String encode13(String message) {
        return encode13(message, true);
    }

    public static String encode13(String message, boolean isEnocdeNumber) {
        return encode(message, 13, isEnocdeNumber);
    }

    public static String encode(String message, int offset, boolean isEnocdeNumber) {
        int len = message.length();
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = encodeChar(message.charAt(i), offset, isEnocdeNumber);
        }
        return new String(chars);
    }

    public static String decode13(String rot) {
        return decode13(rot, true);
    }

    public static String decode13(String rot, boolean isDecodeNumber) {
        return decode(rot, 13, isDecodeNumber);
    }

    public static String decode(String rot, int offset, boolean isDecodeNumber) {
        int len = rot.length();
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = decodeChar(rot.charAt(i), offset, isDecodeNumber);
        }
        return new String(chars);
    }

    private static char encodeChar(char c, int offset, boolean isDecodeNumber) {
        if (isDecodeNumber && c >= '0' && c <= '9') {
            c = (char) (((char) ((((char) (c - '0')) + offset) % 10)) + CHAR0);
        }
        if (c >= 'A' && c <= 'Z') {
            return (char) (((char) ((((char) (c - 'A')) + offset) % 26)) + ACHAR);
        }
        if (c >= 'a' && c <= 'z') {
            return (char) (((char) ((((char) (c - 'a')) + offset) % 26)) + aCHAR);
        }
        return c;
    }

    private static char decodeChar(char c, int offset, boolean isDecodeNumber) {
        int temp = c;
        if (isDecodeNumber && temp >= 48 && temp <= 57) {
            int temp2 = (temp - 48) - offset;
            while (temp2 < 0) {
                temp2 += 10;
            }
            temp = temp2 + 48;
        }
        if (temp >= 65 && temp <= 90) {
            int temp3 = (temp - 65) - offset;
            while (temp3 < 0) {
                temp3 += 26;
            }
            temp = temp3 + 65;
        } else if (temp >= 97 && temp <= 122) {
            int temp4 = (temp - 97) - offset;
            if (temp4 < 0) {
                temp4 += 26;
            }
            temp = temp4 + 97;
        }
        return (char) temp;
    }
}
