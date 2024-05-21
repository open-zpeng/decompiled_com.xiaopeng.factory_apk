package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
/* loaded from: classes.dex */
public class TemporalAccessorConverter extends AbstractConverter<TemporalAccessor> {
    private static final long serialVersionUID = 1;
    private String format;
    private final Class<?> targetType;

    public TemporalAccessorConverter(Class<?> targetType) {
        this(targetType, null);
    }

    public TemporalAccessorConverter(Class<?> targetType, String format) {
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
    public TemporalAccessor convertInternal(Object value) {
        if (value instanceof Long) {
            return parseFromLong((Long) value);
        }
        if (value instanceof TemporalAccessor) {
            return parseFromTemporalAccessor((TemporalAccessor) value);
        }
        if (value instanceof Date) {
            DateTime dateTime = DateUtil.date((Date) value);
            return parseFromInstant(dateTime.toInstant(), dateTime.getZoneId());
        } else if (value instanceof Calendar) {
            Calendar calendar = (Calendar) value;
            return parseFromInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
        } else {
            return parseFromCharSequence(convertToStr(value));
        }
    }

    private TemporalAccessor parseFromCharSequence(CharSequence value) {
        Instant instant;
        ZoneId zoneId;
        if (StrUtil.isBlank(value)) {
            return null;
        }
        String str = this.format;
        if (str != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(str);
            instant = (Instant) formatter.parse(value, new TemporalQuery() { // from class: cn.hutool.core.convert.impl.-$$Lambda$PTL8WkLA4o-1z4zIUBjrvwi808w
                @Override // java.time.temporal.TemporalQuery
                public final Object queryFrom(TemporalAccessor temporalAccessor) {
                    return Instant.from(temporalAccessor);
                }
            });
            zoneId = formatter.getZone();
        } else {
            DateTime dateTime = DateUtil.parse(value);
            instant = ((DateTime) Objects.requireNonNull(dateTime)).toInstant();
            zoneId = dateTime.getZoneId();
        }
        return parseFromInstant(instant, zoneId);
    }

    private TemporalAccessor parseFromLong(Long time) {
        return parseFromInstant(Instant.ofEpochMilli(time.longValue()), null);
    }

    private TemporalAccessor parseFromTemporalAccessor(TemporalAccessor temporalAccessor) {
        TemporalAccessor result = null;
        if (temporalAccessor instanceof LocalDateTime) {
            result = parseFromLocalDateTime((LocalDateTime) temporalAccessor);
        } else if (temporalAccessor instanceof ZonedDateTime) {
            result = parseFromZonedDateTime((ZonedDateTime) temporalAccessor);
        }
        if (result == null) {
            TemporalAccessor result2 = parseFromInstant(DateUtil.toInstant(temporalAccessor), null);
            return result2;
        }
        return result;
    }

    private TemporalAccessor parseFromLocalDateTime(LocalDateTime localDateTime) {
        if (Instant.class.equals(this.targetType)) {
            return DateUtil.toInstant(localDateTime);
        }
        if (LocalDate.class.equals(this.targetType)) {
            return localDateTime.toLocalDate();
        }
        if (LocalTime.class.equals(this.targetType)) {
            return localDateTime.toLocalTime();
        }
        if (ZonedDateTime.class.equals(this.targetType)) {
            return localDateTime.atZone(ZoneId.systemDefault());
        }
        if (OffsetDateTime.class.equals(this.targetType)) {
            return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
        if (OffsetTime.class.equals(this.targetType)) {
            return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime();
        }
        return null;
    }

    private TemporalAccessor parseFromZonedDateTime(ZonedDateTime zonedDateTime) {
        if (Instant.class.equals(this.targetType)) {
            return DateUtil.toInstant(zonedDateTime);
        }
        if (LocalDateTime.class.equals(this.targetType)) {
            return zonedDateTime.toLocalDateTime();
        }
        if (LocalDate.class.equals(this.targetType)) {
            return zonedDateTime.toLocalDate();
        }
        if (LocalTime.class.equals(this.targetType)) {
            return zonedDateTime.toLocalTime();
        }
        if (OffsetDateTime.class.equals(this.targetType)) {
            return zonedDateTime.toOffsetDateTime();
        }
        if (OffsetTime.class.equals(this.targetType)) {
            return zonedDateTime.toOffsetDateTime().toOffsetTime();
        }
        return null;
    }

    private TemporalAccessor parseFromInstant(Instant instant, ZoneId zoneId) {
        if (Instant.class.equals(this.targetType)) {
            return instant;
        }
        ZoneId zoneId2 = (ZoneId) ObjectUtil.defaultIfNull(zoneId, ZoneId.systemDefault());
        if (LocalDateTime.class.equals(this.targetType)) {
            TemporalAccessor result = LocalDateTime.ofInstant(instant, zoneId2);
            return result;
        } else if (LocalDate.class.equals(this.targetType)) {
            TemporalAccessor result2 = instant.atZone(zoneId2).toLocalDate();
            return result2;
        } else if (LocalTime.class.equals(this.targetType)) {
            TemporalAccessor result3 = instant.atZone(zoneId2).toLocalTime();
            return result3;
        } else if (ZonedDateTime.class.equals(this.targetType)) {
            TemporalAccessor result4 = instant.atZone(zoneId2);
            return result4;
        } else if (OffsetDateTime.class.equals(this.targetType)) {
            TemporalAccessor result5 = OffsetDateTime.ofInstant(instant, zoneId2);
            return result5;
        } else if (!OffsetTime.class.equals(this.targetType)) {
            return null;
        } else {
            TemporalAccessor result6 = OffsetTime.ofInstant(instant, zoneId2);
            return result6;
        }
    }
}
