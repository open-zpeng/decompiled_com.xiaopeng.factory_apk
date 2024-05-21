package com.xpeng.upso.utils;

import android.util.Base64;
/* loaded from: classes2.dex */
public class Base64Util {
    public static byte[] decode(byte[] bArr) {
        return Base64.decode(bArr, 2);
    }

    public static String encodeToString(byte[] bArr) {
        return Base64.encodeToString(bArr, 2).replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", "");
    }

    public static byte[] decode(String str) {
        return Base64.decode(str, 2);
    }
}
