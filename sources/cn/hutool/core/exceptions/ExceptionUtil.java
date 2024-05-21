package cn.hutool.core.exceptions;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class ExceptionUtil {
    public static String getMessage(Throwable e) {
        return e == null ? CharSequenceUtil.NULL : StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }

    public static String getSimpleMessage(Throwable e) {
        return e == null ? CharSequenceUtil.NULL : e.getMessage();
    }

    public static RuntimeException wrapRuntime(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(throwable);
    }

    public static RuntimeException wrapRuntime(String message) {
        return new RuntimeException(message);
    }

    public static <T extends Throwable> T wrap(Throwable throwable, Class<T> wrapThrowable) {
        return wrapThrowable.isInstance(throwable) ? throwable : (T) ReflectUtil.newInstance(wrapThrowable, throwable);
    }

    public static void wrapAndThrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw ((RuntimeException) throwable);
        }
        if (throwable instanceof Error) {
            throw ((Error) throwable);
        }
        throw new UndeclaredThrowableException(throwable);
    }

    public static void wrapRuntimeAndThrow(String message) {
        throw new RuntimeException(message);
    }

    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    public static StackTraceElement[] getStackElements() {
        return Thread.currentThread().getStackTrace();
    }

    public static StackTraceElement getStackElement(int i) {
        return Thread.currentThread().getStackTrace()[i];
    }

    public static StackTraceElement getStackElement(final String fqcn, int i) {
        StackTraceElement[] stackTraceArray = Thread.currentThread().getStackTrace();
        int index = ArrayUtil.matchIndex(new Matcher() { // from class: cn.hutool.core.exceptions.-$$Lambda$ExceptionUtil$-tC40kVOVW0WVrUhWrKzKE05YLI
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equals;
                equals = StrUtil.equals(fqcn, ((StackTraceElement) obj).getClassName());
                return equals;
            }
        }, stackTraceArray);
        if (index > 0) {
            return stackTraceArray[index + i];
        }
        return null;
    }

    public static StackTraceElement getRootStackElement() {
        StackTraceElement[] stackElements = Thread.currentThread().getStackTrace();
        return Thread.currentThread().getStackTrace()[stackElements.length - 1];
    }

    public static String stacktraceToOneLineString(Throwable throwable) {
        return stacktraceToOneLineString(throwable, 3000);
    }

    public static String stacktraceToOneLineString(Throwable throwable, int limit) {
        Map<Character, String> replaceCharToStrMap = new HashMap<>();
        replaceCharToStrMap.put('\r', " ");
        replaceCharToStrMap.put('\n', " ");
        replaceCharToStrMap.put('\t', " ");
        return stacktraceToString(throwable, limit, replaceCharToStrMap);
    }

    public static String stacktraceToString(Throwable throwable) {
        return stacktraceToString(throwable, 3000);
    }

    public static String stacktraceToString(Throwable throwable, int limit) {
        return stacktraceToString(throwable, limit, null);
    }

    public static String stacktraceToString(Throwable throwable, int limit, Map<Character, String> replaceCharToStrMap) {
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
        throwable.printStackTrace(new PrintStream(baos));
        String exceptionStr = baos.toString();
        int length = exceptionStr.length();
        if (limit > 0 && limit < length) {
            length = limit;
        }
        if (MapUtil.isNotEmpty(replaceCharToStrMap)) {
            StringBuilder sb = StrUtil.builder();
            for (int i = 0; i < length; i++) {
                char c = exceptionStr.charAt(i);
                String value = replaceCharToStrMap.get(Character.valueOf(c));
                if (value != null) {
                    sb.append(value);
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return StrUtil.subPre(exceptionStr, limit);
    }

    public static boolean isCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
        return getCausedBy(throwable, causeClasses) != null;
    }

    public static Throwable getCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
        for (Throwable cause = throwable; cause != null; cause = cause.getCause()) {
            for (Class<? extends Exception> causeClass : causeClasses) {
                if (causeClass.isInstance(cause)) {
                    return cause;
                }
            }
        }
        return null;
    }

    public static boolean isFromOrSuppressedThrowable(Throwable throwable, Class<? extends Throwable> exceptionClass) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, true) != null;
    }

    public static boolean isFromOrSuppressedThrowable(Throwable throwable, Class<? extends Throwable> exceptionClass, boolean checkCause) {
        return convertFromOrSuppressedThrowable(throwable, exceptionClass, checkCause) != null;
    }

    public static <T extends Throwable> T convertFromOrSuppressedThrowable(Throwable throwable, Class<T> exceptionClass) {
        return (T) convertFromOrSuppressedThrowable(throwable, exceptionClass, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends Throwable> T convertFromOrSuppressedThrowable(Throwable throwable, Class<T> exceptionClass, boolean checkCause) {
        T t;
        if (throwable == 0 || exceptionClass == null) {
            return null;
        }
        if (exceptionClass.isAssignableFrom(throwable.getClass())) {
            return throwable;
        }
        if (checkCause && (t = (T) throwable.getCause()) != null && exceptionClass.isAssignableFrom(t.getClass())) {
            return t;
        }
        Throwable[] throwables = throwable.getSuppressed();
        if (ArrayUtil.isNotEmpty((Object[]) throwables)) {
            for (Throwable th : throwables) {
                T t2 = (T) th;
                if (exceptionClass.isAssignableFrom(t2.getClass())) {
                    return t2;
                }
            }
        }
        return null;
    }

    public static List<Throwable> getThrowableList(Throwable throwable) {
        List<Throwable> list = new ArrayList<>();
        while (throwable != null && !list.contains(throwable)) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

    public static Throwable getRootCause(Throwable throwable) {
        List<Throwable> list = getThrowableList(throwable);
        if (list.size() < 1) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static String getRootCauseMessage(Throwable th) {
        return getMessage(getRootCause(th));
    }
}
