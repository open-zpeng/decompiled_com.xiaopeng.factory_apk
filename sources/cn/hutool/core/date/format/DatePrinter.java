package cn.hutool.core.date.format;

import java.util.Calendar;
import java.util.Date;
/* loaded from: classes.dex */
public interface DatePrinter extends DateBasic {
    <B extends Appendable> B format(long j, B b);

    <B extends Appendable> B format(Calendar calendar, B b);

    <B extends Appendable> B format(Date date, B b);

    String format(long j);

    String format(Calendar calendar);

    String format(Date date);
}
