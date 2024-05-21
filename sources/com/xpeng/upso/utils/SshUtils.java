package com.xpeng.upso.utils;

import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public class SshUtils {
    public static byte[] getTboxLog() {
        return getTboxLog("172.20.1.44");
    }

    public static byte[] getTboxLog(String host) {
        byte[] data = getTboxLogByGM(host);
        if (data == null || data.length <= 0) {
            return getTboxLogByJSch(host);
        }
        return data;
    }

    public static byte[] getXpuLog(String host) {
        byte[] data = getXpuLogByJSch(host);
        if (data == null || data.length <= 0) {
            return getXpuLogByGM(host);
        }
        return data;
    }

    public static byte[] getXpuLogA() {
        return getXpuLog(ScpConfig.xpuIPa);
    }

    public static byte[] getXpuLogB() {
        return getXpuLog(ScpConfig.xpuIPb);
    }

    public static byte[] getTboxLogByGM(String host) {
        try {
            return GmScp.get(host, "root", ScpConfig.tboxPsw, 22, ScpConfig.tboxClientLogPath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getXpuLogByGM(String host) {
        try {
            return GmScp.get(host, "nvidia", "nvidia", 22, ScpConfig.xpuClientLogPath);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getTboxLogByJSch(String host) {
        return JSchSSH.get(host, "root", ScpConfig.tboxPsw, 22, ScpConfig.tboxClientLogDir, "psoclient.log");
    }

    public static byte[] getXpuLogByJSch(String host) {
        return JSchSSH.get(host, "nvidia", "nvidia", 22, ScpConfig.xpuClientLogDir, "psoclient.log");
    }
}
