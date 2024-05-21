package cn.hutool.core.util;

import cn.hutool.core.lang.PatternPool;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class CreditCodeUtil {
    public static final Pattern CREDIT_CODE_PATTERN = PatternPool.CREDIT_CODE;
    private static final int[] WEIGHT = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
    private static final char[] BASE_CODE_ARRAY = "0123456789ABCDEFGHJKLMNPQRTUWXY".toCharArray();
    private static final Map<Character, Integer> CODE_INDEX_MAP = new ConcurrentHashMap();

    static {
        int i = 0;
        while (true) {
            char[] cArr = BASE_CODE_ARRAY;
            if (i < cArr.length) {
                CODE_INDEX_MAP.put(Character.valueOf(cArr[i]), Integer.valueOf(i));
                i++;
            } else {
                return;
            }
        }
    }

    public static boolean isCreditCodeSimple(CharSequence creditCode) {
        if (StrUtil.isBlank(creditCode)) {
            return false;
        }
        return ReUtil.isMatch(CREDIT_CODE_PATTERN, creditCode);
    }

    public static boolean isCreditCode(CharSequence creditCode) {
        int parityBit;
        return isCreditCodeSimple(creditCode) && (parityBit = getParityBit(creditCode)) >= 0 && creditCode.charAt(17) == BASE_CODE_ARRAY[parityBit];
    }

    public static String randomCreditCode() {
        StringBuilder buf = new StringBuilder(18);
        for (int i = 0; i < 2; i++) {
            int num = RandomUtil.randomInt(BASE_CODE_ARRAY.length - 1);
            buf.append(Character.toUpperCase(BASE_CODE_ARRAY[num]));
        }
        for (int i2 = 2; i2 < 8; i2++) {
            int num2 = RandomUtil.randomInt(10);
            buf.append(BASE_CODE_ARRAY[num2]);
        }
        for (int i3 = 8; i3 < 17; i3++) {
            int num3 = RandomUtil.randomInt(BASE_CODE_ARRAY.length - 1);
            buf.append(BASE_CODE_ARRAY[num3]);
        }
        String code = buf.toString();
        return code + BASE_CODE_ARRAY[getParityBit(code)];
    }

    private static int getParityBit(CharSequence creditCode) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            Integer codeIndex = CODE_INDEX_MAP.get(Character.valueOf(creditCode.charAt(i)));
            if (codeIndex == null) {
                return -1;
            }
            sum += codeIndex.intValue() * WEIGHT[i];
        }
        int i2 = sum % 31;
        int result = 31 - i2;
        if (result == 31) {
            return 0;
        }
        return result;
    }
}
