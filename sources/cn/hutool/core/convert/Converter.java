package cn.hutool.core.convert;
/* loaded from: classes.dex */
public interface Converter<T> {
    T convert(Object obj, T t) throws IllegalArgumentException;
}
