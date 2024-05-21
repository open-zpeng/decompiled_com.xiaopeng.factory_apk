package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import java.util.Currency;
/* loaded from: classes.dex */
public class CurrencyConverter extends AbstractConverter<Currency> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Currency convertInternal(Object value) {
        return Currency.getInstance(convertToStr(value));
    }
}
