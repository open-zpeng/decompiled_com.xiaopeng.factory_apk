package cn.hutool.core.date.format;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
/* loaded from: classes.dex */
public class GlobalCustomFormat {
    public static final String FORMAT_MILLISECONDS = "#SSS";
    public static final String FORMAT_SECONDS = "#sss";
    private static final Map<CharSequence, Function<Date, String>> formatterMap = new ConcurrentHashMap();
    private static final Map<CharSequence, Function<CharSequence, Date>> parserMap = new ConcurrentHashMap();

    static {
        putFormatter(FORMAT_SECONDS, new Function() { // from class: cn.hutool.core.date.format.-$$Lambda$GlobalCustomFormat$BQT1hgVnTB5Eo2GPzaT0ptQ8wDo
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String valueOf;
                valueOf = String.valueOf(Math.floorDiv(((Date) obj).getTime(), 1000L));
                return valueOf;
            }
        });
        putParser(FORMAT_SECONDS, new Function() { // from class: cn.hutool.core.date.format.-$$Lambda$GlobalCustomFormat$w6QiGe6leWdxk8K8_D90v6WFkXc
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Date date;
                date = DateUtil.date(Math.multiplyExact(Long.parseLong(((CharSequence) obj).toString()), 1000L));
                return date;
            }
        });
        putFormatter(FORMAT_MILLISECONDS, new Function() { // from class: cn.hutool.core.date.format.-$$Lambda$GlobalCustomFormat$YNsDkPNtqAX24zjUSboZ7OUABMI
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String valueOf;
                valueOf = String.valueOf(((Date) obj).getTime());
                return valueOf;
            }
        });
        putParser(FORMAT_MILLISECONDS, new Function() { // from class: cn.hutool.core.date.format.-$$Lambda$GlobalCustomFormat$yQ_OsFNUqcJuhVejB7yuABVtD-4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Date date;
                date = DateUtil.date(Long.parseLong(((CharSequence) obj).toString()));
                return date;
            }
        });
    }

    public static void putFormatter(String format, Function<Date, String> func) {
        Assert.notNull(format, "Format must be not null !", new Object[0]);
        Assert.notNull(func, "Function must be not null !", new Object[0]);
        formatterMap.put(format, func);
    }

    public static void putParser(String format, Function<CharSequence, Date> func) {
        Assert.notNull(format, "Format must be not null !", new Object[0]);
        Assert.notNull(func, "Function must be not null !", new Object[0]);
        parserMap.put(format, func);
    }

    public static boolean isCustomFormat(String format) {
        return formatterMap.containsKey(format);
    }

    public static String format(Date date, CharSequence format) {
        Function<Date, String> func;
        Map<CharSequence, Function<Date, String>> map = formatterMap;
        if (map != null && (func = map.get(format)) != null) {
            return func.apply(date);
        }
        return null;
    }

    public static String format(TemporalAccessor temporalAccessor, CharSequence format) {
        return format(DateUtil.date(temporalAccessor), format);
    }

    public static Date parse(CharSequence dateStr, String format) {
        Function<CharSequence, Date> func;
        Map<CharSequence, Function<CharSequence, Date>> map = parserMap;
        if (map != null && (func = map.get(format)) != null) {
            return func.apply(dateStr);
        }
        return null;
    }
}
