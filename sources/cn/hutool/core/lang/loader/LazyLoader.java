package cn.hutool.core.lang.loader;

import java.io.Serializable;
/* loaded from: classes.dex */
public abstract class LazyLoader<T> implements Loader<T>, Serializable {
    private static final long serialVersionUID = 1;
    private volatile T object;

    protected abstract T init();

    @Override // cn.hutool.core.lang.loader.Loader
    public T get() {
        T result = this.object;
        if (result == null) {
            synchronized (this) {
                result = this.object;
                if (result == null) {
                    T init = init();
                    result = init;
                    this.object = init;
                }
            }
        }
        return result;
    }
}
