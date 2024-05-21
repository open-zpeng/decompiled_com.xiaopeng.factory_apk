package cn.hutool.core.thread.threadlocal;
/* loaded from: classes.dex */
public class NamedInheritableThreadLocal<T> extends InheritableThreadLocal<T> {
    private final String name;

    public NamedInheritableThreadLocal(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
