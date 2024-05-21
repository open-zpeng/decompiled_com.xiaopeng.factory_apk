package com.xpeng.upso.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes2.dex */
public class DateTimeUtil {
    public static String time2String(Long l) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss").format(new Date(l.longValue()));
    }
}
