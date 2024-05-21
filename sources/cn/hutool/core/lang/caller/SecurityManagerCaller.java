package cn.hutool.core.lang.caller;

import cn.hutool.core.util.ArrayUtil;
import java.io.Serializable;
/* loaded from: classes.dex */
public class SecurityManagerCaller extends SecurityManager implements Caller, Serializable {
    private static final int OFFSET = 1;
    private static final long serialVersionUID = 1;

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCaller() {
        Class<?>[] context = getClassContext();
        if (context != null && 2 < context.length) {
            return context[2];
        }
        return null;
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCallerCaller() {
        Class<?>[] context = getClassContext();
        if (context != null && 3 < context.length) {
            return context[3];
        }
        return null;
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public Class<?> getCaller(int depth) {
        Class<?>[] context = getClassContext();
        if (context != null && depth + 1 < context.length) {
            return context[depth + 1];
        }
        return null;
    }

    @Override // cn.hutool.core.lang.caller.Caller
    public boolean isCalledBy(Class<?> clazz) {
        Class<?>[] classes = getClassContext();
        if (ArrayUtil.isNotEmpty((Object[]) classes)) {
            for (Class<?> contextClass : classes) {
                if (contextClass.equals(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }
}
