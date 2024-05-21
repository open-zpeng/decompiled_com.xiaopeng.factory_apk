package com.xpeng.upso.utils;

import java.io.IOException;
/* loaded from: classes2.dex */
public class CertFileUtils {
    public static byte[] pemStringFormat2Der(byte[] bArr) throws IOException {
        return pemStringFormat2Der(new String(bArr));
    }

    public static byte[] pemStringFormat2Der(String str) throws IOException {
        int i;
        int i2;
        byte[] bytes = str.replace(" ", "").replace("\r", "").replace("\n", "").getBytes();
        int length = bytes.length / 2;
        while (true) {
            if (length < 0) {
                i = 0;
                break;
            } else if (bytes[length] == 45) {
                i = length + 1;
                break;
            } else {
                length--;
            }
        }
        int length2 = bytes.length / 2;
        while (true) {
            if (length2 >= bytes.length) {
                i2 = 0;
                break;
            } else if (bytes[length2] == 45) {
                i2 = length2 - 1;
                break;
            } else {
                length2++;
            }
        }
        int i3 = i2 > i ? (i2 - i) + 1 : 0;
        byte[] bArr = new byte[i3];
        System.arraycopy(bytes, i, bArr, 0, i3);
        return Base64Util.decode(bArr);
    }
}
