package cn.hutool.core.lang.loader;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public abstract class AtomicLoader<T> implements Loader<T>, Serializable {
    private static final long serialVersionUID = 1;
    private final AtomicReference<T> reference = new AtomicReference<>();

    protected abstract T init();

    @Override // cn.hutool.core.lang.loader.Loader
    public T get() {
        T result = this.reference.get();
        if (result == null) {
            T result2 = init();
            if (!this.reference.compareAndSet(null, result2)) {
                return this.reference.get();
            }
            return result2;
        }
        return result;
    }
}
