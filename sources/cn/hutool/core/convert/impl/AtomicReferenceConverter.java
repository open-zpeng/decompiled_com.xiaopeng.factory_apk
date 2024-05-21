package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.TypeUtil;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: classes.dex */
public class AtomicReferenceConverter extends AbstractConverter<AtomicReference> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public AtomicReference convertInternal(Object value) {
        Object targetValue = null;
        Type paramType = TypeUtil.getTypeArgument(AtomicReference.class);
        if (!TypeUtil.isUnknown(paramType)) {
            targetValue = ConverterRegistry.getInstance().convert(paramType, value);
        }
        if (targetValue == null) {
            targetValue = value;
        }
        return new AtomicReference(targetValue);
    }
}
