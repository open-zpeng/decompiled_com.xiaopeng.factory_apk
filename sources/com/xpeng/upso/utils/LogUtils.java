package com.xpeng.upso.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import com.xiaopeng.commonfunc.Constant;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class LogUtils {
    private static final int MAX_SIZE = 20480;
    private static final int READ_LOG_LEN_LIMITED_10K = 10240;
    private static final int REMAIN_SIZE = 10240;
    private static final String TAG = "LogUtils";
    private static byte[] keepLogBuf = null;
    private static String logAbsDir = null;
    private static final String logDir = "logs";
    private static String logFileAbsPath = null;
    private static final String logName = "pso.log";

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum Level {
        d(1, "D"),
        i(2, "I"),
        v(3, "V"),
        w(4, "W"),
        e(5, "E");
        
        String desc;
        int index;

        Level(int i2, String str) {
            this.index = i2;
            this.desc = str;
        }

        public int getIndex() {
            return this.index;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.desc;
        }
    }

    public static void d(String str, String str2) {
        Log.d(str, str2);
        writeLog(Level.d, str, str2);
    }

    public static void e(String str, String str2) {
        Log.e(str, str2);
        writeLog(Level.e, str, str2);
    }

    public static String getLog() {
        byte[] readLogBytes = readLogBytes(logFileAbsPath);
        if (readLogBytes != null && readLogBytes.length > 0) {
            try {
                return new String(readLogBytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static File getRootDir(Context context) {
        if (hasSDCardMounted().booleanValue()) {
            File externalFilesDir = context.getExternalFilesDir(logDir);
            if (externalFilesDir.canWrite()) {
                return externalFilesDir;
            }
        }
        return context.getFilesDir();
    }

    private static Boolean hasSDCardMounted() {
        String externalStorageState = Environment.getExternalStorageState();
        return Boolean.valueOf(externalStorageState != null && externalStorageState.equals("mounted"));
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
        writeLog(Level.i, str, str2);
    }

    public static void init(Context context) {
        synchronized (LogUtils.class) {
            try {
                File rootDir = getRootDir(context);
                logAbsDir = rootDir.getAbsolutePath();
                if (!rootDir.exists()) {
                    rootDir.mkdirs();
                }
                logFileAbsPath = logAbsDir + "/" + logName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void limitLogSize() {
        try {
            File file = new File(logFileAbsPath);
            if (!file.exists() || file.length() <= 20480) {
                return;
            }
            long length = file.length();
            Log.e(TAG, "LogSize=" + file.length());
            byte[] bArr = keepLogBuf;
            if (bArr == null) {
                keepLogBuf = new byte[10240];
            } else {
                Arrays.fill(bArr, (byte) 0);
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.skip(length - 10240);
            fileInputStream.read(keepLogBuf);
            fileInputStream.close();
            file.delete();
            File file2 = new File(logFileAbsPath);
            file2.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(keepLogBuf);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] readLogBytes(String str) {
        byte[] bArr;
        try {
            File file = new File(str);
            Log.i(TAG, str + ",AbsolutePath=" + file.getAbsolutePath());
            Log.i(TAG, str + ",file.exists=" + file.exists() + ",file.canRead=" + file.canRead() + ",file.length=" + file.length());
            if (file.exists() && file.canRead() && file.length() > 0) {
                FileInputStream fileInputStream = new FileInputStream(file);
                if (file.length() > 10240) {
                    bArr = new byte[10240];
                    fileInputStream.skip(file.length() - 10240);
                } else {
                    bArr = new byte[(int) file.length()];
                }
                fileInputStream.read(bArr);
                return bArr;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public static void removeLog() {
        try {
            File file = new File(logFileAbsPath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void v(String str, String str2) {
        Log.v(str, str2);
        writeLog(Level.v, str, str2);
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
        writeLog(Level.w, str, str2);
    }

    private static void writeLog(Level level, String str, String str2) {
        synchronized (LogUtils.class) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (logFileAbsPath == null) {
                return;
            }
            File file = new File(logFileAbsPath);
            if (file.getParentFile().exists()) {
                String str3 = "" + DateTimeUtil.time2String(Long.valueOf(System.currentTimeMillis())) + Constant.SPACE_2_STRING + Process.myPid() + Constant.SPACE_2_STRING + level.toString() + "/" + str + Constant.SPACE_2_STRING + str2 + "\n";
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.write(str3);
                bufferedWriter.flush();
                bufferedWriter.close();
                limitLogSize();
            }
        }
    }
}
