package cn.hutool.core.lang.loader;

import cn.hutool.core.lang.Assert;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class LazyFunLoader<T> extends LazyLoader<T> {
    private static final long serialVersionUID = 1;
    private Supplier<T> supplier;

    public LazyFunLoader(Supplier<T> supplier) {
        Assert.notNull(supplier);
        this.supplier = supplier;
    }

    @Override // cn.hutool.core.lang.loader.LazyLoader
    protected T init() {
        T t = this.supplier.get();
        this.supplier = null;
        return t;
    }

    public boolean isInitialize() {
        return this.supplier == null;
    }

    public void ifInitialized(Consumer<T> consumer) {
        Assert.notNull(consumer);
        if (isInitialize()) {
            consumer.accept(get());
        }
    }
}
