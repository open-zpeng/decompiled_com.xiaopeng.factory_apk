package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.Convert;
import java.util.concurrent.atomic.AtomicLongArray;
/* loaded from: classes.dex */
public class AtomicLongArrayConverter extends AbstractConverter<AtomicLongArray> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public AtomicLongArray convertInternal(Object value) {
        return new AtomicLongArray((long[]) Convert.convert((Class<Object>) long[].class, value));
    }
}
