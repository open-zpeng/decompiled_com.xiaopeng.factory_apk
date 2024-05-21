package com.xiaopeng.xmlconfig;

import android.util.Log;
/* loaded from: classes2.dex */
public class XmlUtil {
    private static final String CLASS_NAME = "XmlUtil";
    private static final int LOG_LEVEL_D = 1;
    private static final int LOG_LEVEL_E = 4;
    private static final int LOG_LEVEL_F = 5;
    private static final int LOG_LEVEL_I = 2;
    private static final int LOG_LEVEL_V = 0;
    private static final int LOG_LEVEL_W = 3;
    public static String TAG = "XmlData";

    public static void log_d(String className, String methodName) {
        Log.d(TAG, getLogMessage(className, methodName, null, 1));
    }

    public static void log_d(String className, String methodName, String message) {
        Log.d(TAG, getLogMessage(className, methodName, message, 1));
    }

    public static void log_e(String className, String methodName) {
        Log.e(TAG, getLogMessage(className, methodName, null, 4));
    }

    public static void log_e(String className, String methodName, String message) {
        Log.e(TAG, getLogMessage(className, methodName, message, 4));
    }

    public static void log_i(String className, String methodName) {
        Log.i(TAG, getLogMessage(className, methodName, null, 2));
    }

    public static void log_i(String className, String methodName, String message) {
        Log.i(TAG, getLogMessage(className, methodName, message, 2));
    }

    public static void log_v(String className, String methodName) {
        Log.v(TAG, getLogMessage(className, methodName, null, 0));
    }

    public static void log_v(String className, String methodName, String message) {
        Log.v(TAG, getLogMessage(className, methodName, message, 0));
    }

    public static void log_w(String className, String methodName) {
        Log.w(TAG, getLogMessage(className, methodName, null, 3));
    }

    public static void log_w(String className, String methodName, String message) {
        Log.w(TAG, getLogMessage(className, methodName, message, 3));
    }

    public static void log_wtf(String className, String methodName) {
        Log.wtf(TAG, getLogMessage(className, methodName, null, 5));
    }

    public static void log_wtf(String className, String methodName, String message) {
        Log.wtf(TAG, getLogMessage(className, methodName, message, 5));
    }

    private static String getLogMessage(String className, String methodName, String message, int logLevel) {
        className = (className == null || className.equals("")) ? " " : " ";
        methodName = (methodName == null || methodName.equals("")) ? " " : " ";
        message = (message == null || message.equals("")) ? " " : " ";
        String logMessage = "[" + className + "$" + methodName + "]" + message;
        return logMessage;
    }

    public static void log_e(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String str = TAG;
        Log.w(str, "WARNNING: " + e.toString());
        for (int i = 0; i < stackTraceElements.length; i++) {
            String str2 = TAG;
            Log.w(str2, "WARNNING:     " + stackTraceElements[i]);
        }
    }
}
