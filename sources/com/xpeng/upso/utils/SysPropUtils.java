package com.xpeng.upso.utils;

import android.os.SystemProperties;
import java.lang.reflect.Method;
/* loaded from: classes2.dex */
public class SysPropUtils {
    public static final String SYS_PROP_CDUID = "persist.sys.mcu.hardwareId";
    public static final String SYS_PROP_ICCID = "sys.xiaopeng.iccid";
    private static Method propStringGet;
    private static Method propStringSet;

    public static String get(String str, String str2) {
        try {
            return SystemProperties.get(str);
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public static void set(String str, String str2) {
        try {
            SystemProperties.set(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
