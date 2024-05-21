package cn.hutool.core.convert.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.map.MapProxy;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import java.lang.reflect.Type;
import java.util.Map;
/* loaded from: classes.dex */
public class BeanConverter<T> extends AbstractConverter<T> {
    private static final long serialVersionUID = 1;
    private final Class<T> beanClass;
    private final Type beanType;
    private final CopyOptions copyOptions;

    public BeanConverter(Type beanType) {
        this(beanType, CopyOptions.create().setIgnoreError(true));
    }

    public BeanConverter(Class<T> beanClass) {
        this(beanClass, CopyOptions.create().setIgnoreError(true));
    }

    public BeanConverter(Type beanType, CopyOptions copyOptions) {
        this.beanType = beanType;
        this.beanClass = (Class<T>) TypeUtil.getClass(beanType);
        this.copyOptions = copyOptions;
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    protected T convertInternal(Object value) {
        if ((value instanceof Map) || (value instanceof ValueProvider) || BeanUtil.isBean(value.getClass())) {
            if ((value instanceof Map) && this.beanClass.isInterface()) {
                return (T) MapProxy.create((Map) value).toProxyBean(this.beanClass);
            }
            return (T) BeanCopier.create(value, ReflectUtil.newInstanceIfPossible(this.beanClass), this.beanType, this.copyOptions).copy();
        } else if (value instanceof byte[]) {
            return (T) ObjectUtil.deserialize((byte[]) value);
        } else {
            throw new ConvertException("Unsupported source type: {}", value.getClass());
        }
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<T> getTargetType() {
        return this.beanClass;
    }
}
