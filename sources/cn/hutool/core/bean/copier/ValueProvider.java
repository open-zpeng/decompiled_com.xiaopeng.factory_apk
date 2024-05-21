package cn.hutool.core.bean.copier;

import java.lang.reflect.Type;
/* loaded from: classes.dex */
public interface ValueProvider<T> {
    boolean containsKey(T t);

    Object value(T t, Type type);
}
