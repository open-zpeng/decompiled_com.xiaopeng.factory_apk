package com.xpeng.upso;
/* loaded from: classes2.dex */
public class UpsoConfig {
    private static boolean a = false;

    public static boolean isLogEnabled() {
        return a;
    }

    public static String logLimited(String str) {
        try {
            if (a || str.length() <= 200) {
                return str;
            }
            return str.substring(0, 200) + "...";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void setLogEnabled(boolean z) {
        a = z;
    }
}
