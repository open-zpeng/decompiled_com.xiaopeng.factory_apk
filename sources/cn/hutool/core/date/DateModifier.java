package cn.hutool.core.date;

import cn.hutool.core.util.ArrayUtil;
import java.util.Calendar;
/* loaded from: classes.dex */
public class DateModifier {
    private static final int[] IGNORE_FIELDS = {11, 9, 8, 6, 4, 3};

    /* loaded from: classes.dex */
    public enum ModifyType {
        TRUNCATE,
        ROUND,
        CEILING
    }

    public static Calendar modify(Calendar calendar, int dateField, ModifyType modifyType) {
        return modify(calendar, dateField, modifyType, false);
    }

    public static Calendar modify(Calendar calendar, int dateField, ModifyType modifyType, boolean truncateMillisecond) {
        if (9 == dateField) {
            boolean isAM = DateUtil.isAM(calendar);
            int i = AnonymousClass1.$SwitchMap$cn$hutool$core$date$DateModifier$ModifyType[modifyType.ordinal()];
            if (i == 1) {
                calendar.set(11, isAM ? 0 : 12);
            } else if (i != 2) {
                if (i == 3) {
                    int min = isAM ? 0 : 12;
                    int max = isAM ? 11 : 23;
                    int href = ((max - min) / 2) + 1;
                    int value = calendar.get(11);
                    calendar.set(11, value < href ? min : max);
                }
            } else {
                calendar.set(11, isAM ? 11 : 23);
            }
            return modify(calendar, dateField + 1, modifyType);
        }
        int endField = truncateMillisecond ? 13 : 14;
        for (int i2 = dateField + 1; i2 <= endField; i2++) {
            if (!ArrayUtil.contains(IGNORE_FIELDS, i2)) {
                if (4 == dateField || 3 == dateField) {
                    if (5 == i2) {
                    }
                    modifyField(calendar, i2, modifyType);
                } else {
                    if (7 == i2) {
                    }
                    modifyField(calendar, i2, modifyType);
                }
            }
        }
        if (truncateMillisecond) {
            calendar.set(14, 0);
        }
        return calendar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.hutool.core.date.DateModifier$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$core$date$DateModifier$ModifyType = new int[ModifyType.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$core$date$DateModifier$ModifyType[ModifyType.TRUNCATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$core$date$DateModifier$ModifyType[ModifyType.CEILING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cn$hutool$core$date$DateModifier$ModifyType[ModifyType.ROUND.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private static void modifyField(Calendar calendar, int field, ModifyType modifyType) {
        int href;
        if (10 == field) {
            field = 11;
        }
        int i = AnonymousClass1.$SwitchMap$cn$hutool$core$date$DateModifier$ModifyType[modifyType.ordinal()];
        if (i == 1) {
            calendar.set(field, DateUtil.getBeginValue(calendar, field));
        } else if (i == 2) {
            calendar.set(field, DateUtil.getEndValue(calendar, field));
        } else if (i == 3) {
            int min = DateUtil.getBeginValue(calendar, field);
            int max = DateUtil.getEndValue(calendar, field);
            if (7 == field) {
                href = (min + 3) % 7;
            } else {
                href = 1 + ((max - min) / 2);
            }
            int value = calendar.get(field);
            calendar.set(field, value < href ? min : max);
        }
    }
}
