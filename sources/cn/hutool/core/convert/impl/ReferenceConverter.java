package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
/* loaded from: classes.dex */
public class ReferenceConverter extends AbstractConverter<Reference> {
    private static final long serialVersionUID = 1;
    private final Class<? extends Reference> targetType;

    public ReferenceConverter(Class<? extends Reference> targetType) {
        this.targetType = targetType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Reference convertInternal(Object value) {
        Object targetValue = null;
        Type paramType = TypeUtil.getTypeArgument(this.targetType);
        if (!TypeUtil.isUnknown(paramType)) {
            targetValue = ConverterRegistry.getInstance().convert(paramType, value);
        }
        if (targetValue == null) {
            targetValue = value;
        }
        Class<? extends Reference> cls = this.targetType;
        if (cls == WeakReference.class) {
            return new WeakReference(targetValue);
        }
        if (cls == SoftReference.class) {
            return new SoftReference(targetValue);
        }
        throw new UnsupportedOperationException(StrUtil.format("Unsupport Reference type: {}", cls.getName()));
    }
}
