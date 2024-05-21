package cn.hutool.core.date.chinese;
/* loaded from: classes.dex */
public class ChineseMonth {
    private static final String[] MONTH_NAME = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static final String[] MONTH_NAME_TRADITIONAL = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "寒", "冬", "腊"};

    public static boolean isLeapMonth(int year, int month) {
        return month == LunarInfo.leapMonth(year);
    }

    public static String getChineseMonthName(boolean isLeapMonth, int month, boolean isTraditional) {
        StringBuilder sb = new StringBuilder();
        sb.append(isLeapMonth ? "闰" : "");
        sb.append((isTraditional ? MONTH_NAME_TRADITIONAL : MONTH_NAME)[month - 1]);
        sb.append("月");
        return sb.toString();
    }
}
