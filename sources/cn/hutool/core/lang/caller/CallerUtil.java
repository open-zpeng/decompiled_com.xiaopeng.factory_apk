package cn.hutool.core.lang.caller;
/* loaded from: classes.dex */
public class CallerUtil {
    private static final Caller INSTANCE = tryCreateCaller();

    public static Class<?> getCaller() {
        return INSTANCE.getCaller();
    }

    public static Class<?> getCallerCaller() {
        return INSTANCE.getCallerCaller();
    }

    public static Class<?> getCaller(int depth) {
        return INSTANCE.getCaller(depth);
    }

    public static boolean isCalledBy(Class<?> clazz) {
        return INSTANCE.isCalledBy(clazz);
    }

    public static String getCallerMethodName(boolean isFullName) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
        String methodName = stackTraceElement.getMethodName();
        if (!isFullName) {
            return methodName;
        }
        return stackTraceElement.getClassName() + "." + methodName;
    }

    private static Caller tryCreateCaller() {
        try {
            Caller caller = new SecurityManagerCaller();
            if (caller.getCaller() != null) {
                if (caller.getCallerCaller() != null) {
                    return caller;
                }
            }
        } catch (Throwable th) {
        }
        return new StackTraceCaller();
    }
}
