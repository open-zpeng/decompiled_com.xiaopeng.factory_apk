package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.util.function.Function;
/* loaded from: classes.dex */
public class PrimitiveConverter extends AbstractConverter<Object> {
    private static final long serialVersionUID = 1;
    private final Class<?> targetType;

    public PrimitiveConverter(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("PrimitiveConverter not allow null target type!");
        }
        if (!clazz.isPrimitive()) {
            throw new IllegalArgumentException("[" + clazz + "] is not a primitive class!");
        }
        this.targetType = clazz;
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    protected Object convertInternal(Object value) {
        return convert(value, this.targetType, new Function() { // from class: cn.hutool.core.convert.impl.-$$Lambda$2sS3r4Pa9GreAO19_nzGx00CNPA
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return PrimitiveConverter.this.convertToStr(obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public String convertToStr(Object value) {
        return StrUtil.trim(super.convertToStr(value));
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Object> getTargetType() {
        return this.targetType;
    }

    protected static Object convert(Object value, Class<?> primitiveClass, Function<Object, String> toStringFunc) {
        if (Byte.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Byte.class, toStringFunc), 0);
        }
        if (Short.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Short.class, toStringFunc), 0);
        }
        if (Integer.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Integer.class, toStringFunc), 0);
        }
        if (Long.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Long.class, toStringFunc), 0);
        }
        if (Float.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Float.class, toStringFunc), 0);
        }
        if (Double.TYPE == primitiveClass) {
            return ObjectUtil.defaultIfNull(NumberConverter.convert(value, Double.class, toStringFunc), 0);
        }
        if (Character.TYPE == primitiveClass) {
            return Convert.convert((Class<Object>) Character.class, value);
        }
        if (Boolean.TYPE == primitiveClass) {
            return Convert.convert((Class<Object>) Boolean.class, value);
        }
        throw new ConvertException("Unsupported target type: {}", primitiveClass);
    }
}
