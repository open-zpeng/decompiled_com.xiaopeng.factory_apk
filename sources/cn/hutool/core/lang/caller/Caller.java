package cn.hutool.core.lang.caller;
/* loaded from: classes.dex */
public interface Caller {
    Class<?> getCaller();

    Class<?> getCaller(int i);

    Class<?> getCallerCaller();

    boolean isCalledBy(Class<?> cls);
}
