package com.xpeng.upso.sentry;

import android.os.Process;
import com.xiaopeng.commonfunc.Constant;
import com.xpeng.upso.utils.LogUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/* loaded from: classes2.dex */
public class LogsReader {
    private static final String a = "Upso-" + LogsReader.class.getSimpleName();
    private static final String[] b = {"TestSecurity", "Upso-"};

    private static String a(String str, String[] strArr) {
        File file;
        File[] listFiles;
        String a2;
        String str2 = "";
        try {
            file = new File(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.isDirectory() && file.exists()) {
            for (File file2 : file.listFiles()) {
                try {
                    if ((file2.getName().equals(Constant.DATA_LOG_MAIN) || file2.getName().equals("main.txt.01") || file2.getName().equals("main.txt.02") || file2.getName().equals("main.txt.03") || file2.getName().equals("main.txt.04") || file2.getName().equals("main.txt.05")) && (a2 = a(file2, strArr)) != null && a2.length() > 0) {
                        str2 = str2 + a2;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (str2 == null || str2.length() <= 0) {
                return null;
            }
            LogUtils.i(a, "readLog,len=" + str2.length());
            return str2;
        }
        return null;
    }

    public static String getDolbyLocalLog() {
        try {
            return a("/sdcard/Android/data/com.xpeng.upsosample/files/log0/", b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProxyLog() {
        try {
            return a("/sdcard/Android/data/com.xpeng.upsosample/files/log0/", b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String a(File file, String[] strArr) {
        int myPid;
        BufferedReader bufferedReader;
        String str;
        String[] strArr2;
        try {
            myPid = Process.myPid();
            String str2 = a;
            LogUtils.e(str2, "myPid=" + myPid);
            LogUtils.e(str2, "readLogWithFilter,AbsolutePath=" + file.getAbsolutePath());
            LogUtils.e(str2, file.getAbsolutePath() + ",file.exists=" + file.exists() + ",file.canRead=" + file.canRead() + ",file.length=" + file.length());
            bufferedReader = new BufferedReader(new FileReader(file));
            str = "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null || readLine.length() <= 0) {
                break;
            } else if (!readLine.contains("LogsReader")) {
                try {
                    strArr2 = readLine.split(Constant.SPACE_2_STRING);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    strArr2 = null;
                }
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        try {
                            if (readLine.contains(strArr[i]) && strArr2 != null && strArr2.length > 1) {
                                if (strArr2[1].equals("" + myPid)) {
                                    str = str + readLine;
                                    LogUtils.d(a, "get line=" + readLine);
                                    break;
                                }
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                        i++;
                    }
                }
            }
            e.printStackTrace();
            return null;
        }
        bufferedReader.close();
        if (str != null) {
            if (str.length() > 0) {
                return str;
            }
        }
        return null;
    }
}
