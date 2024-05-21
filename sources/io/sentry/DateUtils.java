package io.sentry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class DateUtils {
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String UTC = "UTC";
    @NotNull
    private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone(UTC);
    @NotNull
    private static final ThreadLocal<SimpleDateFormat> SDF_ISO_FORMAT_WITH_MILLIS_UTC = new ThreadLocal<SimpleDateFormat>() { // from class: io.sentry.DateUtils.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);
            simpleDateFormat.setTimeZone(DateUtils.UTC_TIMEZONE);
            return simpleDateFormat;
        }
    };
    @NotNull
    private static final ThreadLocal<SimpleDateFormat> SDF_ISO_FORMAT_UTC = new ThreadLocal<SimpleDateFormat>() { // from class: io.sentry.DateUtils.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
            simpleDateFormat.setTimeZone(DateUtils.UTC_TIMEZONE);
            return simpleDateFormat;
        }
    };

    private DateUtils() {
    }

    @NotNull
    public static Date getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance(UTC_TIMEZONE);
        return calendar.getTime();
    }

    @NotNull
    public static Date getDateTime(@NotNull String timestamp) throws IllegalArgumentException {
        try {
            return SDF_ISO_FORMAT_WITH_MILLIS_UTC.get().parse(timestamp);
        } catch (ParseException e) {
            try {
                return SDF_ISO_FORMAT_UTC.get().parse(timestamp);
            } catch (ParseException e2) {
                throw new IllegalArgumentException("timestamp is not ISO format " + timestamp);
            }
        }
    }

    @NotNull
    public static Date getDateTimeWithMillisPrecision(@NotNull String timestamp) throws IllegalArgumentException {
        try {
            return getDateTime(new BigDecimal(timestamp).setScale(3, RoundingMode.DOWN).movePointRight(3).longValue());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("timestamp is not millis format " + timestamp);
        }
    }

    @NotNull
    public static String getTimestamp(@NotNull Date date) {
        DateFormat df = SDF_ISO_FORMAT_WITH_MILLIS_UTC.get();
        return df.format(date);
    }

    @NotNull
    public static Date getDateTime(long millis) {
        Calendar calendar = Calendar.getInstance(UTC_TIMEZONE);
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
