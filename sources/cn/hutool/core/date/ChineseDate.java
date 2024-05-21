package cn.hutool.core.date;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.chinese.ChineseMonth;
import cn.hutool.core.date.chinese.GanZhi;
import cn.hutool.core.date.chinese.LunarFestival;
import cn.hutool.core.date.chinese.LunarInfo;
import cn.hutool.core.date.chinese.SolarTerms;
import cn.hutool.core.util.StrUtil;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;
/* loaded from: classes.dex */
public class ChineseDate {
    private final int day;
    private final int gday;
    private final int gmonth;
    private final int gyear;
    private boolean leap;
    private final int month;
    private final int year;

    public ChineseDate(Date date) {
        int daysOfYear;
        int offset = (int) ((DateUtil.beginOfDay(date).getTime() / DateUnit.DAY.getMillis()) - LunarInfo.BASE_DAY);
        int iYear = 1900;
        while (iYear <= LunarInfo.MAX_YEAR && offset >= (daysOfYear = LunarInfo.yearDays(iYear))) {
            offset -= daysOfYear;
            iYear++;
        }
        this.year = iYear;
        int leapMonth = LunarInfo.leapMonth(iYear);
        int daysOfMonth = 0;
        int iMonth = 1;
        while (iMonth < 13 && offset > 0) {
            if (leapMonth > 0 && iMonth == leapMonth + 1 && !this.leap) {
                iMonth--;
                this.leap = true;
                daysOfMonth = LunarInfo.leapDays(this.year);
            } else {
                daysOfMonth = LunarInfo.monthDays(this.year, iMonth);
            }
            offset -= daysOfMonth;
            if (this.leap && iMonth == leapMonth + 1) {
                this.leap = false;
            }
            iMonth++;
        }
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (this.leap) {
                this.leap = false;
            } else {
                this.leap = true;
                iMonth--;
            }
        }
        if (offset < 0) {
            offset += daysOfMonth;
            iMonth--;
        }
        this.month = iMonth;
        this.day = offset + 1;
        DateTime dt = DateUtil.date(date);
        this.gyear = dt.year();
        this.gmonth = dt.month() + 1;
        this.gday = dt.dayOfMonth();
    }

    public ChineseDate(int chineseYear, int chineseMonth, int chineseDay) {
        this.day = chineseDay;
        this.month = chineseMonth;
        this.year = chineseYear;
        this.leap = DateUtil.isLeapYear(chineseYear);
        int leapMonth = LunarInfo.leapMonth(chineseYear);
        DateTime dateTime = lunar2solar(chineseYear, chineseMonth, chineseDay, chineseMonth == leapMonth);
        if (dateTime != null) {
            this.gday = dateTime.dayOfMonth();
            this.gmonth = dateTime.month() + 1;
            this.gyear = dateTime.year();
            return;
        }
        this.gday = -1;
        this.gmonth = -1;
        this.gyear = -1;
    }

    public int getChineseYear() {
        return this.year;
    }

    public int getGregorianYear() {
        return this.gyear;
    }

    public int getMonth() {
        return this.month;
    }

    public int getGregorianMonthBase1() {
        return this.gmonth;
    }

    public int getGregorianMonth() {
        return this.gmonth - 1;
    }

    public boolean isLeapMonth() {
        return ChineseMonth.isLeapMonth(this.year, this.month);
    }

    public String getChineseMonth() {
        return ChineseMonth.getChineseMonthName(isLeapMonth(), this.month, false);
    }

    public String getChineseMonthName() {
        return ChineseMonth.getChineseMonthName(isLeapMonth(), this.month, true);
    }

    public int getDay() {
        return this.day;
    }

    public int getGregorianDay() {
        return this.gday;
    }

    public String getChineseDay() {
        String[] chineseTen = {"初", "十", "廿", "卅"};
        int i = this.day;
        int n = i % 10 == 0 ? 9 : (i % 10) - 1;
        int i2 = this.day;
        if (i2 > 30) {
            return "";
        }
        if (i2 != 10) {
            if (i2 != 20) {
                if (i2 == 30) {
                    return "三十";
                }
                return chineseTen[this.day / 10] + NumberChineseFormatter.format(n + 1, false);
            }
            return "二十";
        }
        return "初十";
    }

    public Date getGregorianDate() {
        return DateUtil.date(getGregorianCalendar());
    }

    public Calendar getGregorianCalendar() {
        Calendar calendar = CalendarUtil.calendar();
        calendar.set(this.gyear, getGregorianMonth(), this.gday, 0, 0, 0);
        return calendar;
    }

    public String getFestivals() {
        return StrUtil.join(",", LunarFestival.getFestivals(this.year, this.month, this.day));
    }

    public String getChineseZodiac() {
        return Zodiac.getChineseZodiac(this.year);
    }

    public String getCyclical() {
        return GanZhi.getGanzhiOfYear(this.year);
    }

    public String getCyclicalYMD() {
        int i;
        int i2;
        int i3 = this.gyear;
        if (i3 >= 1900 && (i = this.gmonth) > 0 && (i2 = this.gday) > 0) {
            return cyclicalm(i3, i, i2);
        }
        return null;
    }

    public String getTerm() {
        return SolarTerms.getTerm(this.gyear, this.gmonth, this.gday);
    }

    public String toStringNormal() {
        return String.format("%04d-%02d-%02d", Integer.valueOf(this.year), Integer.valueOf(this.month), Integer.valueOf(this.day));
    }

    public String toString() {
        return String.format("%s%s年 %s%s", getCyclical(), getChineseZodiac(), getChineseMonthName(), getChineseDay());
    }

    private String cyclicalm(int year, int month, int day) {
        return StrUtil.format("{}年{}月{}日", GanZhi.getGanzhiOfYear(this.year), GanZhi.getGanzhiOfMonth(year, month, day), GanZhi.getGanzhiOfDay(year, month, day));
    }

    private DateTime lunar2solar(int chineseYear, int chineseMonth, int chineseDay, boolean isLeapMonth) {
        if ((chineseYear == 2100 && chineseMonth == 12 && chineseDay > 1) || (chineseYear == 1900 && chineseMonth == 1 && chineseDay < 31)) {
            return null;
        }
        int day = LunarInfo.monthDays(chineseYear, chineseMonth);
        int _day = day;
        if (isLeapMonth) {
            _day = LunarInfo.leapDays(chineseYear);
        }
        if (chineseYear < 1900 || chineseYear > 2100 || chineseDay > _day) {
            return null;
        }
        int offset = 0;
        for (int i = 1900; i < chineseYear; i++) {
            offset += LunarInfo.yearDays(i);
        }
        boolean isAdd = false;
        for (int i2 = 1; i2 < chineseMonth; i2++) {
            int leap = LunarInfo.leapMonth(chineseYear);
            if (!isAdd && leap <= i2 && leap > 0) {
                offset += LunarInfo.leapDays(chineseYear);
                isAdd = true;
            }
            offset += LunarInfo.monthDays(chineseYear, i2);
        }
        if (isLeapMonth) {
            offset += day;
        }
        return DateUtil.date((((offset + chineseDay) - 31) * DateUtils.MILLIS_PER_DAY) - 2203804800000L);
    }
}
