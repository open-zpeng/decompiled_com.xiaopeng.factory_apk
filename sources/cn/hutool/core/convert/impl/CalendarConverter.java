package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public class CalendarConverter extends AbstractConverter<Calendar> {
    private static final long serialVersionUID = 1;
    private String format;

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Calendar convertInternal(Object value) {
        if (value instanceof Date) {
            return DateUtil.calendar((Date) value);
        }
        if (value instanceof Long) {
            return DateUtil.calendar(((Long) value).longValue());
        }
        String valueStr = convertToStr(value);
        return DateUtil.calendar(StrUtil.isBlank(this.format) ? DateUtil.parse(valueStr) : DateUtil.parse(valueStr, this.format));
    }
}
