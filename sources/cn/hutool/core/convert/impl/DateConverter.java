package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public class DateConverter extends AbstractConverter<Date> {
    private static final long serialVersionUID = 1;
    private String format;
    private final Class<? extends Date> targetType;

    public DateConverter(Class<? extends Date> targetType) {
        this.targetType = targetType;
    }

    public DateConverter(Class<? extends Date> targetType, String format) {
        this.targetType = targetType;
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // cn.hutool.core.convert.AbstractConverter
    public Date convertInternal(Object value) {
        Date date;
        if (value != null) {
            if ((value instanceof CharSequence) && StrUtil.isBlank(value.toString())) {
                return null;
            }
            if (value instanceof TemporalAccessor) {
                return wrap(DateUtil.date((TemporalAccessor) value));
            }
            if (value instanceof Calendar) {
                return wrap(DateUtil.date((Calendar) value));
            }
            if (value instanceof Number) {
                return wrap(((Number) value).longValue());
            }
            String valueStr = convertToStr(value);
            if (StrUtil.isBlank(this.format)) {
                date = DateUtil.parse(valueStr);
            } else {
                date = DateUtil.parse(valueStr, this.format);
            }
            if (date != null) {
                return wrap(date);
            }
            throw new ConvertException("Can not convert {}:[{}] to {}", value.getClass().getName(), value, this.targetType.getName());
        }
        return null;
    }

    private Date wrap(Date date) {
        Class<? extends Date> cls = this.targetType;
        if (Date.class == cls) {
            return date;
        }
        if (DateTime.class == cls) {
            return DateUtil.date(date);
        }
        if (java.sql.Date.class == cls) {
            return new java.sql.Date(date.getTime());
        }
        if (Time.class == cls) {
            return new Time(date.getTime());
        }
        if (Timestamp.class == cls) {
            return new Timestamp(date.getTime());
        }
        throw new UnsupportedOperationException(StrUtil.format("Unsupported target Date type: {}", cls.getName()));
    }

    private Date wrap(long mills) {
        Class<? extends Date> cls = this.targetType;
        if (Date.class == cls) {
            return new Date(mills);
        }
        if (DateTime.class == cls) {
            return DateUtil.date(mills);
        }
        if (java.sql.Date.class == cls) {
            return new java.sql.Date(mills);
        }
        if (Time.class == cls) {
            return new Time(mills);
        }
        if (Timestamp.class == cls) {
            return new Timestamp(mills);
        }
        throw new UnsupportedOperationException(StrUtil.format("Unsupported target Date type: {}", cls.getName()));
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Date> getTargetType() {
        return this.targetType;
    }
}
