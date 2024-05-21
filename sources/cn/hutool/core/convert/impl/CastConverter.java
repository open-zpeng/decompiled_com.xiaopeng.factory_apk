package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
/* loaded from: classes.dex */
public class CastConverter<T> extends AbstractConverter<T> {
    private static final long serialVersionUID = 1;
    private Class<T> targetType;

    @Override // cn.hutool.core.convert.AbstractConverter
    protected T convertInternal(Object value) {
        throw new ConvertException("Can not cast value to [{}]", this.targetType);
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<T> getTargetType() {
        return this.targetType;
    }
}
