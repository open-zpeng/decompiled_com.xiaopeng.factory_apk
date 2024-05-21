package cn.hutool.core.codec;

import cn.hutool.core.lang.Assert;
/* loaded from: classes.dex */
public class BCD {
    public static byte[] strToBcd(String asc) {
        int j;
        int k;
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        if (len >= 2) {
            len >>= 1;
        }
        byte[] bbt = new byte[len];
        byte[] abt = asc.getBytes();
        for (int p = 0; p < asc.length() / 2; p++) {
            if (abt[p * 2] >= 48 && abt[p * 2] <= 57) {
                j = abt[p * 2] - 48;
            } else {
                int j2 = p * 2;
                if (abt[j2] >= 97 && abt[p * 2] <= 122) {
                    j = (abt[p * 2] - 97) + 10;
                } else {
                    int j3 = p * 2;
                    j = (abt[j3] - 65) + 10;
                }
            }
            if (abt[(p * 2) + 1] >= 48 && abt[(p * 2) + 1] <= 57) {
                k = abt[(p * 2) + 1] - 48;
            } else if (abt[(p * 2) + 1] >= 97 && abt[(p * 2) + 1] <= 122) {
                k = (abt[(p * 2) + 1] - 97) + 10;
            } else {
                int k2 = p * 2;
                k = (abt[k2 + 1] - 65) + 10;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static byte[] ascToBcd(byte[] ascii) {
        Assert.notNull(ascii, "Ascii must be not null!", new Object[0]);
        return ascToBcd(ascii, ascii.length);
    }

    public static byte[] ascToBcd(byte[] ascii, int ascLength) {
        int j;
        Assert.notNull(ascii, "Ascii must be not null!", new Object[0]);
        byte[] bcd = new byte[ascLength / 2];
        int j2 = 0;
        for (int i = 0; i < (ascLength + 1) / 2; i++) {
            int j3 = j2 + 1;
            bcd[i] = ascToBcd(ascii[j2]);
            if (j3 >= ascLength) {
                j2 = j3;
                j = 0;
            } else {
                j2 = j3 + 1;
                j = ascToBcd(ascii[j3]);
            }
            bcd[i] = (byte) (j + (bcd[i] << 4));
        }
        return bcd;
    }

    public static String bcdToStr(byte[] bytes) {
        Assert.notNull(bytes, "Bcd bytes must be not null!", new Object[0]);
        char[] temp = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            char val = (char) (((bytes[i] & 240) >> 4) & 15);
            temp[i * 2] = (char) (val > '\t' ? (val + 'A') - 10 : val + '0');
            char val2 = (char) (bytes[i] & 15);
            temp[(i * 2) + 1] = (char) (val2 > '\t' ? (val2 + 'A') - 10 : val2 + '0');
        }
        return new String(temp);
    }

    private static byte ascToBcd(byte asc) {
        if (asc >= 48 && asc <= 57) {
            byte bcd = (byte) (asc - 48);
            return bcd;
        } else if (asc >= 65 && asc <= 70) {
            byte bcd2 = (byte) ((asc - 65) + 10);
            return bcd2;
        } else if (asc >= 97 && asc <= 102) {
            byte bcd3 = (byte) ((asc - 97) + 10);
            return bcd3;
        } else {
            byte bcd4 = (byte) (asc - 48);
            return bcd4;
        }
    }
}
