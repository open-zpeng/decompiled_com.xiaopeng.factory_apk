package cn.hutool.core.lang.caller;

import cn.hutool.core.exceptions.UtilException;
import java.io.Serializable;
/* loaded from: classes.dex */
public class StackTraceCaller implements Caller, Serializable {
    private static final int OFFSET = 2;
    private static final long serialVersionUID = 1;

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (3 >= stackTrace.length) {
            return null;
        }
        String className = stackTrace[3].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCallerCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (4 >= stackTrace.length) {
            return null;
        }
        String className = stackTrace[4].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCaller(int depth) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (depth + 2 >= stackTrace.length) {
            return null;
        }
        String className = stackTrace[depth + 2].getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new UtilException(e, "[{}] not found!", className);
        }
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public boolean isCalledBy(Class<?> clazz) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }
}
