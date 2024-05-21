package xp.hardware.dab;

import android.util.Log;
/* loaded from: classes2.dex */
public class UtilLog {
    private static final String TAG = "RadioService";

    public static void d(String subTag, String msg) {
        Log.d(TAG, String.format("%s_%s", subTag, msg));
    }

    public static void e(String subTag, String msg) {
        Log.e(TAG, String.format("%s_%s", subTag, msg));
    }

    public static void v(String subTag, String msg) {
        Log.v(TAG, String.format("%s_%s", subTag, msg));
    }

    public static void w(String subTag, String msg) {
        Log.w(TAG, String.format("%s_%s", subTag, msg));
    }

    public static void i(String subTag, String msg) {
        Log.i(TAG, String.format("%s_%s", subTag, msg));
    }
}
