package cn.hutool.core.bean;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.lang.func.Func0;
/* loaded from: classes.dex */
public enum BeanDescCache {
    INSTANCE;
    
    private final SimpleCache<Class<?>, BeanDesc> bdCache = new SimpleCache<>();

    BeanDescCache() {
    }

    public BeanDesc getBeanDesc(Class<?> beanClass, Func0<BeanDesc> supplier) {
        return this.bdCache.get(beanClass, supplier);
    }
}
