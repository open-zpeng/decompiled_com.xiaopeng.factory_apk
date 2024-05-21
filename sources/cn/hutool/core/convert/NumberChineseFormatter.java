package cn.hutool.core.convert;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
/* loaded from: classes.dex */
public class NumberChineseFormatter {
    private static final char[] DIGITS = {38646, 19968, 22777, 20108, 36144, 19977, 21441, 22235, 32902, 20116, 20237, 20845, 38470, 19971, 26578, 20843, 25420, 20061, 29590};
    private static final ChineseUnit[] CHINESE_NAME_VALUE = {new ChineseUnit(' ', 1, false), new ChineseUnit(21313, 10, false), new ChineseUnit(25342, 10, false), new ChineseUnit(30334, 100, false), new ChineseUnit(20336, 100, false), new ChineseUnit(21315, 1000, false), new ChineseUnit(20191, 1000, false), new ChineseUnit(19975, 10000, true), new ChineseUnit(20159, 100000000, true)};

    public static String format(double amount, boolean isUseTraditional) {
        return format(amount, isUseTraditional, false);
    }

    public static String format(double amount, boolean isUseTraditional, boolean isMoneyMode) {
        if (amount > 9.999999999999998E13d || amount < -9.999999999999998E13d) {
            throw new IllegalArgumentException("Number support only: (-99999999999999.99 ～ 99999999999999.99)！");
        }
        boolean negative = false;
        if (amount < 0.0d) {
            negative = true;
            amount = -amount;
        }
        long temp = Math.round(100.0d * amount);
        int numFen = (int) (temp % 10);
        long temp2 = temp / 10;
        int numJiao = (int) (temp2 % 10);
        StringBuilder chineseStr = new StringBuilder(longToChinese(temp2 / 10, isUseTraditional));
        if (negative) {
            chineseStr.insert(0, "负");
        }
        if (numFen != 0 || numJiao != 0) {
            if (numFen == 0) {
                chineseStr.append(isMoneyMode ? "元" : "点");
                chineseStr.append(numberToChinese(numJiao, isUseTraditional));
                chineseStr.append(isMoneyMode ? "角" : "");
            } else if (numJiao == 0) {
                chineseStr.append(isMoneyMode ? "元零" : "点零");
                chineseStr.append(numberToChinese(numFen, isUseTraditional));
                chineseStr.append(isMoneyMode ? "分" : "");
            } else {
                chineseStr.append(isMoneyMode ? "元" : "点");
                chineseStr.append(numberToChinese(numJiao, isUseTraditional));
                chineseStr.append(isMoneyMode ? "角" : "");
                chineseStr.append(numberToChinese(numFen, isUseTraditional));
                chineseStr.append(isMoneyMode ? "分" : "");
            }
        } else if (isMoneyMode) {
            chineseStr.append("元整");
        }
        return chineseStr.toString();
    }

    public static String numberCharToChinese(char c, boolean isUseTraditional) {
        if (c < '0' || c > '9') {
            return String.valueOf(c);
        }
        return String.valueOf(numberToChinese(c - '0', isUseTraditional));
    }

    private static String longToChinese(long amount, boolean isUseTraditional) {
        if (0 == amount) {
            return "零";
        }
        int[] parts = new int[4];
        int i = 0;
        while (amount != 0) {
            parts[i] = (int) (amount % 10000);
            amount /= 10000;
            i++;
        }
        StringBuilder chineseStr = new StringBuilder();
        int partValue = parts[0];
        if (partValue > 0) {
            String partChinese = thousandToChinese(partValue, isUseTraditional);
            chineseStr.insert(0, partChinese);
            if (partValue < 1000) {
                addPreZero(chineseStr);
            }
        }
        int partValue2 = parts[1];
        if (partValue2 > 0) {
            if (partValue2 % 10 == 0 && parts[0] > 0) {
                addPreZero(chineseStr);
            }
            String partChinese2 = thousandToChinese(partValue2, isUseTraditional);
            chineseStr.insert(0, partChinese2 + "万");
            if (partValue2 < 1000) {
                addPreZero(chineseStr);
            }
        } else {
            addPreZero(chineseStr);
        }
        int partValue3 = parts[2];
        if (partValue3 > 0) {
            if (partValue3 % 10 == 0 && parts[1] > 0) {
                addPreZero(chineseStr);
            }
            String partChinese3 = thousandToChinese(partValue3, isUseTraditional);
            chineseStr.insert(0, partChinese3 + "亿");
            if (partValue3 < 1000) {
                addPreZero(chineseStr);
            }
        } else {
            addPreZero(chineseStr);
        }
        int partValue4 = parts[3];
        if (partValue4 > 0) {
            if (parts[2] == 0) {
                chineseStr.insert(0, "亿");
            }
            String partChinese4 = thousandToChinese(partValue4, isUseTraditional);
            chineseStr.insert(0, partChinese4 + "万");
        }
        if (StrUtil.isNotEmpty(chineseStr) && 38646 == chineseStr.charAt(0)) {
            return chineseStr.substring(1);
        }
        return chineseStr.toString();
    }

    private static String thousandToChinese(int amountPart, boolean isUseTraditional) {
        int temp = amountPart;
        StringBuilder chineseStr = new StringBuilder();
        boolean lastIsZero = true;
        int i = 0;
        while (temp > 0) {
            int digit = temp % 10;
            if (digit == 0) {
                if (!lastIsZero) {
                    chineseStr.insert(0, "零");
                }
                lastIsZero = true;
            } else {
                chineseStr.insert(0, numberToChinese(digit, isUseTraditional) + getUnitName(i, isUseTraditional));
                lastIsZero = false;
            }
            temp /= 10;
            i++;
        }
        return chineseStr.toString();
    }

    public static int chineseToNumber(String chinese) {
        int length = chinese.length();
        int result = 0;
        int section = 0;
        int number = 0;
        ChineseUnit unit = null;
        for (int i = 0; i < length; i++) {
            char c = chinese.charAt(i);
            int num = chineseToNumber(c);
            if (num >= 0) {
                if (num == 0) {
                    if (number > 0 && unit != null) {
                        section += (unit.value / 10) * number;
                    }
                    unit = null;
                } else if (number > 0) {
                    throw new IllegalArgumentException(StrUtil.format("Bad number '{}{}' at: {}", Character.valueOf(chinese.charAt(i - 1)), Character.valueOf(c), Integer.valueOf(i)));
                }
                number = num;
            } else {
                unit = chineseToUnit(c);
                if (unit == null) {
                    throw new IllegalArgumentException(StrUtil.format("Unknown unit '{}' at: {}", Character.valueOf(c), Integer.valueOf(i)));
                }
                if (unit.secUnit) {
                    result += (section + number) * unit.value;
                    section = 0;
                } else {
                    int unitNumber = number;
                    if (number == 0 && i == 0) {
                        unitNumber = 1;
                    }
                    section += unit.value * unitNumber;
                }
                number = 0;
            }
        }
        if (number > 0 && unit != null) {
            number *= unit.value / 10;
        }
        return result + section + number;
    }

    private static ChineseUnit chineseToUnit(char chinese) {
        ChineseUnit[] chineseUnitArr;
        for (ChineseUnit chineseNameValue : CHINESE_NAME_VALUE) {
            if (chineseNameValue.name == chinese) {
                return chineseNameValue;
            }
        }
        return null;
    }

    private static int chineseToNumber(char chinese) {
        if (20004 == chinese) {
            chinese = 20108;
        }
        int i = ArrayUtil.indexOf(DIGITS, chinese);
        if (i > 0) {
            return (i + 1) / 2;
        }
        return i;
    }

    private static char numberToChinese(int number, boolean isUseTraditional) {
        if (number == 0) {
            return DIGITS[0];
        }
        return DIGITS[(number * 2) - (!isUseTraditional ? 1 : 0)];
    }

    private static String getUnitName(int index, boolean isUseTraditional) {
        if (index == 0) {
            return "";
        }
        return String.valueOf(CHINESE_NAME_VALUE[(index * 2) - (!isUseTraditional ? 1 : 0)].name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ChineseUnit {
        private final char name;
        private final boolean secUnit;
        private final int value;

        public ChineseUnit(char name, int value, boolean secUnit) {
            this.name = name;
            this.value = value;
            this.secUnit = secUnit;
        }
    }

    private static void addPreZero(StringBuilder chineseStr) {
        if (StrUtil.isEmpty(chineseStr)) {
            return;
        }
        char c = chineseStr.charAt(0);
        if (38646 != c) {
            chineseStr.insert(0, (char) 38646);
        }
    }
}
