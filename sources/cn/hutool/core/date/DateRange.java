package cn.hutool.core.date;

import cn.hutool.core.lang.Range;
import java.util.Date;
/* loaded from: classes.dex */
public class DateRange extends Range<DateTime> {
    private static final long serialVersionUID = 1;

    public DateRange(Date start, Date end, DateField unit) {
        this(start, end, unit, 1);
    }

    public DateRange(Date start, Date end, DateField unit, int step) {
        this(start, end, unit, step, true, true);
    }

    public DateRange(final Date start, Date end, final DateField unit, final int step, boolean isIncludeStart, boolean isIncludeEnd) {
        super(DateUtil.date(start), DateUtil.date(end), new Range.Stepper() { // from class: cn.hutool.core.date.-$$Lambda$DateRange$E8hfhWE00e89HaCY9lDvX33vMlc
            @Override // cn.hutool.core.lang.Range.Stepper
            public final Object step(Object obj, Object obj2, int i) {
                return DateRange.lambda$new$0(start, unit, step, (DateTime) obj, (DateTime) obj2, i);
            }
        }, isIncludeStart, isIncludeEnd);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ DateTime lambda$new$0(Date start, DateField unit, int step, DateTime current, DateTime end1, int index) {
        DateTime dt = DateUtil.date(start).offsetNew(unit, (index + 1) * step);
        if (dt.isAfter(end1)) {
            return null;
        }
        return dt;
    }
}
