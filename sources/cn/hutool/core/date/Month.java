package cn.hutool.core.date;
/* loaded from: classes.dex */
public enum Month {
    JANUARY(0),
    FEBRUARY(1),
    MARCH(2),
    APRIL(3),
    MAY(4),
    JUNE(5),
    JULY(6),
    AUGUST(7),
    SEPTEMBER(8),
    OCTOBER(9),
    NOVEMBER(10),
    DECEMBER(11),
    UNDECIMBER(12);
    
    private static final int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, -1};
    private final int value;

    Month(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public int getLastDay(boolean isLeapYear) {
        return getLastDay(this.value, isLeapYear);
    }

    public static Month of(int calendarMonthIntValue) {
        switch (calendarMonthIntValue) {
            case 0:
                return JANUARY;
            case 1:
                return FEBRUARY;
            case 2:
                return MARCH;
            case 3:
                return APRIL;
            case 4:
                return MAY;
            case 5:
                return JUNE;
            case 6:
                return JULY;
            case 7:
                return AUGUST;
            case 8:
                return SEPTEMBER;
            case 9:
                return OCTOBER;
            case 10:
                return NOVEMBER;
            case 11:
                return DECEMBER;
            case 12:
                return UNDECIMBER;
            default:
                return null;
        }
    }

    public static int getLastDay(int month, boolean isLeapYear) {
        int lastDay = DAYS_OF_MONTH[month];
        if (isLeapYear && 1 == month) {
            return lastDay + 1;
        }
        return lastDay;
    }
}
