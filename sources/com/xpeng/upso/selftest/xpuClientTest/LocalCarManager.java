package com.xpeng.upso.selftest.xpuClientTest;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.xpeng.upso.proxy.ProxyService;
import com.xpeng.upso.utils.SysPropUtils;
/* loaded from: classes2.dex */
public class LocalCarManager {
    private static final String TAG = "XPU_LocalCarManager";
    private static final String TBOX_SERVICE_NAME = "XP_TBOX_SERVICE";
    private static boolean sIsPreseted = false;
    private Context context;

    public LocalCarManager(Context context) {
        this.context = context;
    }

    public static synchronized void initCarService(Context appContext) {
        synchronized (LocalCarManager.class) {
        }
    }

    public static boolean isPresetTemp() {
        return sIsPreseted;
    }

    public static void setPresetedTemp(boolean preseted) {
        sIsPreseted = preseted;
    }

    public static String getTboxIccid() {
        String result = SysPropUtils.get(SysPropUtils.SYS_PROP_ICCID, "");
        return result;
    }

    public static String getCduid() {
        if (ProxyService.isUsingAndroidKeystore()) {
            return "XPUP" + Build.ID;
        }
        String result = "XPU_" + SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "");
        return result;
    }

    public boolean sendCertString(String cert) {
        if (!sendKeyString(cert)) {
            return false;
        }
        return true;
    }

    public boolean sendKeyString(String keys) {
        if (!isCarServiceReady()) {
            Log.d(TAG, "car service is not inited");
            return false;
        }
        return true;
    }

    public String getKeyString() {
        if (isCarServiceReady()) {
            return null;
        }
        Log.d(TAG, "car service is not inited");
        return null;
    }

    public void startCertInstall() {
        if (!isCarServiceReady()) {
            Log.d(TAG, "car service is not inited");
        }
    }

    public void startCertVerify() {
        if (!isCarServiceReady()) {
            Log.d(TAG, "car service is not inited");
        }
    }

    private boolean isCarServiceReady() {
        return true;
    }
}
