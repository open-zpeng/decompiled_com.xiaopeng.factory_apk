package cn.hutool.core.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaopeng.commonfunc.Constant;
/* loaded from: classes.dex */
public class NumberWordFormatter {
    private static final String[] NUMBER = {"", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"};
    private static final String[] NUMBER_TEEN = {"TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
    private static final String[] NUMBER_TEN = {"TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY"};
    private static final String[] NUMBER_MORE = {"", "THOUSAND", "MILLION", "BILLION"};
    private static final String[] NUMBER_SUFFIX = {"k", "w", "", "m", "", "", "b", "", "", "t", "", "", "p", "", "", Constant.ALPHA_LOWCASE_E};

    public static String format(Object x) {
        if (x != null) {
            return format(x.toString());
        }
        return "";
    }

    public static String formatSimple(long value) {
        return formatSimple(value, true);
    }

    public static String formatSimple(long value, boolean isTwo) {
        if (value < 1000) {
            return String.valueOf(value);
        }
        int index = -1;
        double res = value;
        while (res > 10.0d && (!isTwo || index < 1)) {
            if (res > 1000.0d) {
                res /= 1000.0d;
                index++;
            }
            if (res > 10.0d) {
                res /= 10.0d;
                index++;
            }
        }
        return String.format("%s%s", NumberUtil.decimalFormat("#.##", res), NUMBER_SUFFIX[index]);
    }

    private static String format(String x) {
        String lstr;
        int z = x.indexOf(".");
        String rstr = "";
        if (z > -1) {
            lstr = x.substring(0, z);
            rstr = x.substring(z + 1);
        } else {
            lstr = x;
        }
        String lstrrev = StrUtil.reverse(lstr);
        String[] a = new String[5];
        int length = lstrrev.length() % 3;
        if (length == 1) {
            lstrrev = lstrrev + "00";
        } else if (length == 2) {
            lstrrev = lstrrev + "0";
        }
        StringBuilder lm = new StringBuilder();
        for (int i = 0; i < lstrrev.length() / 3; i++) {
            a[i] = StrUtil.reverse(lstrrev.substring(i * 3, (i * 3) + 3));
            if (!"000".equals(a[i])) {
                if (i != 0) {
                    lm.insert(0, transThree(a[i]) + " " + parseMore(i) + " ");
                } else {
                    lm = new StringBuilder(transThree(a[i]));
                }
            } else {
                lm.append(transThree(a[i]));
            }
        }
        String xs = z > -1 ? "AND CENTS " + transTwo(rstr) + " " : "";
        return lm.toString().trim() + " " + xs + "ONLY";
    }

    private static String parseFirst(String s) {
        return NUMBER[Integer.parseInt(s.substring(s.length() - 1))];
    }

    private static String parseTeen(String s) {
        return NUMBER_TEEN[Integer.parseInt(s) - 10];
    }

    private static String parseTen(String s) {
        return NUMBER_TEN[Integer.parseInt(s.substring(0, 1)) - 1];
    }

    private static String parseMore(int i) {
        return NUMBER_MORE[i];
    }

    private static String transTwo(String s) {
        if (s.length() > 2) {
            s = s.substring(0, 2);
        } else if (s.length() < 2) {
            s = "0" + s;
        }
        if (s.startsWith("0")) {
            String value = parseFirst(s);
            return value;
        } else if (s.startsWith("1")) {
            String value2 = parseTeen(s);
            return value2;
        } else if (s.endsWith("0")) {
            String value3 = parseTen(s);
            return value3;
        } else {
            String value4 = parseTen(s) + " " + parseFirst(s);
            return value4;
        }
    }

    private static String transThree(String s) {
        if (s.startsWith("0")) {
            String value = transTwo(s.substring(1));
            return value;
        }
        String value2 = s.substring(1);
        if ("00".equals(value2)) {
            String value3 = parseFirst(s.substring(0, 1)) + " HUNDRED";
            return value3;
        }
        String value4 = parseFirst(s.substring(0, 1)) + " HUNDRED AND " + transTwo(s.substring(1));
        return value4;
    }
}
