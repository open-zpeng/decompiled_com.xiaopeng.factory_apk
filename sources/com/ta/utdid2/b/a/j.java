package com.ta.utdid2.b.a;

import android.util.Log;
import org.apache.commons.lang3.time.DateUtils;
/* compiled from: TimeUtils.java */
/* loaded from: classes.dex */
public class j {
    public static final String TAG = j.class.getName();

    public static boolean a(long j, int i) {
        boolean z = (System.currentTimeMillis() - j) / DateUtils.MILLIS_PER_DAY < ((long) i);
        if (d.e) {
            String str = TAG;
            Log.d(str, "isUpToDate: " + z + "; oldTimestamp: " + j + "; currentTimestamp" + System.currentTimeMillis());
        }
        return z;
    }
}
