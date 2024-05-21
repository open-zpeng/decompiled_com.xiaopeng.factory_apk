package cn.hutool.core.date;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/* loaded from: classes.dex */
public class DateTime extends Date {
    private static final long serialVersionUID = -5395712593979185936L;
    private Week firstDayOfWeek;
    private boolean mutable;
    private TimeZone timeZone;

    public static DateTime of(long timeMillis) {
        return new DateTime(timeMillis);
    }

    public static DateTime of(Date date) {
        if (date instanceof DateTime) {
            return (DateTime) date;
        }
        return new DateTime(date);
    }

    public static DateTime of(Calendar calendar) {
        return new DateTime(calendar);
    }

    public static DateTime of(String dateStr, String format) {
        return new DateTime(dateStr, format);
    }

    public static DateTime now() {
        return new DateTime();
    }

    public DateTime() {
        this(TimeZone.getDefault());
    }

    public DateTime(TimeZone timeZone) {
        this(System.currentTimeMillis(), timeZone);
    }

    public DateTime(Date date) {
        this(date, date instanceof DateTime ? ((DateTime) date).timeZone : TimeZone.getDefault());
    }

    public DateTime(Date date, TimeZone timeZone) {
        this(((Date) ObjectUtil.defaultIfNull(date, new Date())).getTime(), timeZone);
    }

    public DateTime(Calendar calendar) {
        this(calendar.getTime(), calendar.getTimeZone());
        setFirstDayOfWeek(Week.of(calendar.getFirstDayOfWeek()));
    }

    public DateTime(Instant instant) {
        this(instant.toEpochMilli());
    }

    public DateTime(Instant instant, ZoneId zoneId) {
        this(instant.toEpochMilli(), TimeZone.getTimeZone((ZoneId) ObjectUtil.defaultIfNull(zoneId, ZoneId.systemDefault())));
    }

    public DateTime(TemporalAccessor temporalAccessor) {
        this(DateUtil.toInstant(temporalAccessor));
    }

    public DateTime(ZonedDateTime zonedDateTime) {
        this(zonedDateTime.toInstant(), zonedDateTime.getZone());
    }

    public DateTime(long timeMillis) {
        this(timeMillis, TimeZone.getDefault());
    }

    public DateTime(long timeMillis, TimeZone timeZone) {
        super(timeMillis);
        this.mutable = true;
        this.firstDayOfWeek = Week.MONDAY;
        this.timeZone = (TimeZone) ObjectUtil.defaultIfNull(timeZone, TimeZone.getDefault());
    }

    public DateTime(CharSequence dateStr) {
        this(DateUtil.parse(dateStr));
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DateTime(java.lang.CharSequence r2, java.lang.String r3) {
        /*
            r1 = this;
            boolean r0 = cn.hutool.core.date.format.GlobalCustomFormat.isCustomFormat(r3)
            if (r0 == 0) goto Lb
            java.util.Date r0 = cn.hutool.core.date.format.GlobalCustomFormat.parse(r2, r3)
            goto L13
        Lb:
            java.text.SimpleDateFormat r0 = cn.hutool.core.date.DateUtil.newSimpleFormat(r3)
            java.util.Date r0 = parse(r2, r0)
        L13:
            r1.<init>(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.core.date.DateTime.<init>(java.lang.CharSequence, java.lang.String):void");
    }

    public DateTime(CharSequence dateStr, DateFormat dateFormat) {
        this(parse(dateStr, dateFormat), dateFormat.getTimeZone());
    }

    public DateTime(CharSequence dateStr, DateTimeFormatter formatter) {
        this(Instant.from(formatter.parse(dateStr)), formatter.getZone());
    }

    public DateTime(CharSequence dateStr, DateParser dateParser) {
        this(parse(dateStr, dateParser), dateParser.getTimeZone());
    }

    public DateTime offset(DateField datePart, int offset) {
        if (DateField.ERA == datePart) {
            throw new IllegalArgumentException("ERA is not support offset!");
        }
        Calendar cal = toCalendar();
        cal.add(datePart.getValue(), offset);
        DateTime dt = this.mutable ? this : (DateTime) ObjectUtil.clone(this);
        return dt.setTimeInternal(cal.getTimeInMillis());
    }

    public DateTime offsetNew(DateField datePart, int offset) {
        Calendar cal = toCalendar();
        cal.add(datePart.getValue(), offset);
        return ((DateTime) ObjectUtil.clone(this)).setTimeInternal(cal.getTimeInMillis());
    }

    public int getField(DateField field) {
        return getField(field.getValue());
    }

    public int getField(int field) {
        return toCalendar().get(field);
    }

    public DateTime setField(DateField field, int value) {
        return setField(field.getValue(), value);
    }

    public DateTime setField(int field, int value) {
        Calendar calendar = toCalendar();
        calendar.set(field, value);
        DateTime dt = this;
        if (!this.mutable) {
            dt = (DateTime) ObjectUtil.clone(this);
        }
        return dt.setTimeInternal(calendar.getTimeInMillis());
    }

    @Override // java.util.Date
    public void setTime(long time) {
        if (this.mutable) {
            super.setTime(time);
            return;
        }
        throw new DateException("This is not a mutable object !");
    }

    public int year() {
        return getField(DateField.YEAR);
    }

    public int quarter() {
        return (month() / 3) + 1;
    }

    public Quarter quarterEnum() {
        return Quarter.of(quarter());
    }

    public int month() {
        return getField(DateField.MONTH);
    }

    public int monthBaseOne() {
        return month() + 1;
    }

    public int monthStartFromOne() {
        return month() + 1;
    }

    public Month monthEnum() {
        return Month.of(month());
    }

    public int weekOfYear() {
        return getField(DateField.WEEK_OF_YEAR);
    }

    public int weekOfMonth() {
        return getField(DateField.WEEK_OF_MONTH);
    }

    public int dayOfMonth() {
        return getField(DateField.DAY_OF_MONTH);
    }

    public int dayOfYear() {
        return getField(DateField.DAY_OF_YEAR);
    }

    public int dayOfWeek() {
        return getField(DateField.DAY_OF_WEEK);
    }

    public int dayOfWeekInMonth() {
        return getField(DateField.DAY_OF_WEEK_IN_MONTH);
    }

    public Week dayOfWeekEnum() {
        return Week.of(dayOfWeek());
    }

    public int hour(boolean is24HourClock) {
        return getField(is24HourClock ? DateField.HOUR_OF_DAY : DateField.HOUR);
    }

    public int minute() {
        return getField(DateField.MINUTE);
    }

    public int second() {
        return getField(DateField.SECOND);
    }

    public int millisecond() {
        return getField(DateField.MILLISECOND);
    }

    public boolean isAM() {
        return getField(DateField.AM_PM) == 0;
    }

    public boolean isPM() {
        return 1 == getField(DateField.AM_PM);
    }

    public boolean isWeekend() {
        int dayOfWeek = dayOfWeek();
        return 7 == dayOfWeek || 1 == dayOfWeek;
    }

    public boolean isLeapYear() {
        return DateUtil.isLeapYear(year());
    }

    public Calendar toCalendar() {
        return toCalendar(Locale.getDefault(Locale.Category.FORMAT));
    }

    public Calendar toCalendar(Locale locale) {
        return toCalendar(this.timeZone, locale);
    }

    public Calendar toCalendar(TimeZone zone) {
        return toCalendar(zone, Locale.getDefault(Locale.Category.FORMAT));
    }

    public Calendar toCalendar(TimeZone zone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault(Locale.Category.FORMAT);
        }
        Calendar cal = zone != null ? Calendar.getInstance(zone, locale) : Calendar.getInstance(locale);
        cal.setFirstDayOfWeek(this.firstDayOfWeek.getValue());
        cal.setTime(this);
        return cal;
    }

    public Date toJdkDate() {
        return new Date(getTime());
    }

    public Timestamp toTimestamp() {
        return new Timestamp(getTime());
    }

    public java.sql.Date toSqlDate() {
        return new java.sql.Date(getTime());
    }

    public DateBetween between(Date date) {
        return new DateBetween(this, date);
    }

    public long between(Date date, DateUnit unit) {
        return new DateBetween(this, date).between(unit);
    }

    public String between(Date date, DateUnit unit, BetweenFormatter.Level formatLevel) {
        return new DateBetween(this, date).toString(formatLevel);
    }

    public boolean isIn(Date beginDate, Date endDate) {
        long beginMills = beginDate.getTime();
        long endMills = endDate.getTime();
        long thisMills = getTime();
        return thisMills >= Math.min(beginMills, endMills) && thisMills <= Math.max(beginMills, endMills);
    }

    public boolean isBefore(Date date) {
        if (date != null) {
            return compareTo(date) < 0;
        }
        throw new NullPointerException("Date to compare is null !");
    }

    public boolean isBeforeOrEquals(Date date) {
        if (date != null) {
            return compareTo(date) <= 0;
        }
        throw new NullPointerException("Date to compare is null !");
    }

    public boolean isAfter(Date date) {
        if (date != null) {
            return compareTo(date) > 0;
        }
        throw new NullPointerException("Date to compare is null !");
    }

    public boolean isAfterOrEquals(Date date) {
        if (date != null) {
            return compareTo(date) >= 0;
        }
        throw new NullPointerException("Date to compare is null !");
    }

    public boolean isMutable() {
        return this.mutable;
    }

    public DateTime setMutable(boolean mutable) {
        this.mutable = mutable;
        return this;
    }

    public Week getFirstDayOfWeek() {
        return this.firstDayOfWeek;
    }

    public DateTime setFirstDayOfWeek(Week firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        return this;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public ZoneId getZoneId() {
        return this.timeZone.toZoneId();
    }

    public DateTime setTimeZone(TimeZone timeZone) {
        this.timeZone = (TimeZone) ObjectUtil.defaultIfNull(timeZone, TimeZone.getDefault());
        return this;
    }

    @Override // java.util.Date
    public String toString() {
        return toString(this.timeZone);
    }

    public String toStringDefaultTimeZone() {
        return toString(TimeZone.getDefault());
    }

    public String toString(TimeZone timeZone) {
        if (timeZone != null) {
            return toString(DateUtil.newSimpleFormat("yyyy-MM-dd HH:mm:ss", null, timeZone));
        }
        return toString(DatePattern.NORM_DATETIME_FORMAT);
    }

    public String toDateStr() {
        TimeZone timeZone = this.timeZone;
        if (timeZone != null) {
            return toString(DateUtil.newSimpleFormat(DatePattern.NORM_DATE_PATTERN, null, timeZone));
        }
        return toString(DatePattern.NORM_DATE_FORMAT);
    }

    public String toTimeStr() {
        TimeZone timeZone = this.timeZone;
        if (timeZone != null) {
            return toString(DateUtil.newSimpleFormat(DatePattern.NORM_TIME_PATTERN, null, timeZone));
        }
        return toString(DatePattern.NORM_TIME_FORMAT);
    }

    public String toString(String format) {
        TimeZone timeZone = this.timeZone;
        if (timeZone != null) {
            return toString(DateUtil.newSimpleFormat(format, null, timeZone));
        }
        return toString(FastDateFormat.getInstance(format));
    }

    public String toString(DatePrinter format) {
        return format.format(this);
    }

    public String toString(DateFormat format) {
        return format.format((Date) this);
    }

    public String toMsStr() {
        return toString(DatePattern.NORM_DATETIME_MS_FORMAT);
    }

    private static Date parse(CharSequence dateStr, DateFormat dateFormat) {
        String pattern;
        Assert.notBlank(dateStr, "Date String must be not blank !", new Object[0]);
        try {
            return dateFormat.parse(dateStr.toString());
        } catch (Exception e) {
            if (dateFormat instanceof SimpleDateFormat) {
                pattern = ((SimpleDateFormat) dateFormat).toPattern();
            } else {
                pattern = dateFormat.toString();
            }
            throw new DateException(StrUtil.format("Parse [{}] with format [{}] error!", dateStr, pattern), e);
        }
    }

    private static Date parse(CharSequence dateStr, DateParser parser) {
        Assert.notNull(parser, "Parser or DateFromat must be not null !", new Object[0]);
        Assert.notBlank(dateStr, "Date String must be not blank !", new Object[0]);
        try {
            return parser.parse(dateStr.toString());
        } catch (Exception e) {
            throw new DateException("Parse [{}] with format [{}] error!", dateStr, parser.getPattern(), e);
        }
    }

    private DateTime setTimeInternal(long time) {
        super.setTime(time);
        return this;
    }
}
