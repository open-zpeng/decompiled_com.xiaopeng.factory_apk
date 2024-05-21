package cn.hutool.core.convert.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.TypeUtil;
import java.lang.reflect.Type;
import java.util.Collection;
/* loaded from: classes.dex */
public class CollectionConverter implements Converter<Collection<?>> {
    private final Type collectionType;
    private final Type elementType;

    public CollectionConverter() {
        this((Class<?>) Collection.class);
    }

    public CollectionConverter(Type collectionType) {
        this(collectionType, TypeUtil.getTypeArgument(collectionType));
    }

    public CollectionConverter(Class<?> collectionType) {
        this(collectionType, TypeUtil.getTypeArgument(collectionType));
    }

    public CollectionConverter(Type collectionType, Type elementType) {
        this.collectionType = collectionType;
        this.elementType = elementType;
    }

    @Override // cn.hutool.core.convert.Converter
    public Collection<?> convert(Object value, Collection<?> defaultValue) throws IllegalArgumentException {
        Collection<?> result = convertInternal(value);
        return (Collection) ObjectUtil.defaultIfNull(result, defaultValue);
    }

    protected Collection<?> convertInternal(Object value) {
        Collection<Object> collection = CollUtil.create(TypeUtil.getClass(this.collectionType));
        return CollUtil.addAll(collection, value, this.elementType);
    }
}
