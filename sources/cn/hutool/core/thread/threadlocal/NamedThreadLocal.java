package cn.hutool.core.thread.threadlocal;
/* loaded from: classes.dex */
public class NamedThreadLocal<T> extends ThreadLocal<T> {
    private final String name;

    public NamedThreadLocal(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
