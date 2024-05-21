package cn.hutool.core.date;

import cn.hutool.core.date.format.GlobalCustomFormat;
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
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
/* loaded from: classes.dex */
public class TemporalAccessorUtil extends TemporalUtil {
    public static int get(TemporalAccessor temporalAccessor, TemporalField field) {
        if (temporalAccessor.isSupported(field)) {
            return temporalAccessor.get(field);
        }
        return (int) field.range().getMinimum();
    }

    public static String format(TemporalAccessor time, DateTimeFormatter formatter) {
        if (time == null) {
            return null;
        }
        if (formatter == null) {
            formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        }
        try {
            return formatter.format(time);
        } catch (UnsupportedTemporalTypeException e) {
            if ((time instanceof LocalDate) && e.getMessage().contains("HourOfDay")) {
                return formatter.format(((LocalDate) time).atStartOfDay());
            }
            if ((time instanceof LocalTime) && e.getMessage().contains("YearOfEra")) {
                return formatter.format(((LocalTime) time).atDate(LocalDate.now()));
            }
            if (time instanceof Instant) {
                return formatter.format(((Instant) time).atZone(ZoneId.systemDefault()));
            }
            throw e;
        }
    }

    public static String format(TemporalAccessor time, String format) {
        DateTimeFormatter formatter = null;
        if (time == null) {
            return null;
        }
        if (GlobalCustomFormat.isCustomFormat(format)) {
            return GlobalCustomFormat.format(time, format);
        }
        if (!StrUtil.isBlank(format)) {
            formatter = DateTimeFormatter.ofPattern(format);
        }
        return format(time, formatter);
    }

    public static long toEpochMilli(TemporalAccessor temporalAccessor) {
        return toInstant(temporalAccessor).toEpochMilli();
    }

    public static Instant toInstant(TemporalAccessor temporalAccessor) {
        if (temporalAccessor == null) {
            return null;
        }
        if (temporalAccessor instanceof Instant) {
            Instant result = (Instant) temporalAccessor;
            return result;
        } else if (temporalAccessor instanceof LocalDateTime) {
            Instant result2 = ((LocalDateTime) temporalAccessor).atZone(ZoneId.systemDefault()).toInstant();
            return result2;
        } else if (temporalAccessor instanceof ZonedDateTime) {
            Instant result3 = ((ZonedDateTime) temporalAccessor).toInstant();
            return result3;
        } else if (temporalAccessor instanceof OffsetDateTime) {
            Instant result4 = ((OffsetDateTime) temporalAccessor).toInstant();
            return result4;
        } else if (temporalAccessor instanceof LocalDate) {
            Instant result5 = ((LocalDate) temporalAccessor).atStartOfDay(ZoneId.systemDefault()).toInstant();
            return result5;
        } else if (temporalAccessor instanceof LocalTime) {
            Instant result6 = ((LocalTime) temporalAccessor).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
            return result6;
        } else if (temporalAccessor instanceof OffsetTime) {
            Instant result7 = ((OffsetTime) temporalAccessor).atDate(LocalDate.now()).toInstant();
            return result7;
        } else {
            Instant result8 = Instant.from(temporalAccessor);
            return result8;
        }
    }
}
