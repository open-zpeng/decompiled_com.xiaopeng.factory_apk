package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class TimeZoneConverter extends AbstractConverter<TimeZone> {
    private static final long serialVersionUID = 1;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public TimeZone convertInternal(Object value) {
        return TimeZone.getTimeZone(convertToStr(value));
    }
}
