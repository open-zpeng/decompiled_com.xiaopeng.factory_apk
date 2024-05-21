package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
/* loaded from: classes.dex */
public class BooleanUtil {
    private static final Set<String> TRUE_SET = CollUtil.newHashSet("true", BooleanUtils.YES, "y", "t", "ok", "1", BooleanUtils.ON, "是", "对", "真", "對", "√");

    public static Boolean negate(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
    }

    public static boolean isTrue(Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    public static boolean isFalse(Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    public static boolean negate(boolean bool) {
        return !bool;
    }

    public static boolean toBoolean(String valueStr) {
        if (StrUtil.isNotBlank(valueStr)) {
            return TRUE_SET.contains(valueStr.trim().toLowerCase());
        }
        return false;
    }

    public static int toInt(boolean value) {
        return value ? 1 : 0;
    }

    public static Integer toInteger(boolean value) {
        return Integer.valueOf(toInt(value));
    }

    public static char toChar(boolean value) {
        return (char) toInt(value);
    }

    public static Character toCharacter(boolean value) {
        return Character.valueOf(toChar(value));
    }

    public static byte toByte(boolean value) {
        return (byte) toInt(value);
    }

    public static Byte toByteObj(boolean value) {
        return Byte.valueOf(toByte(value));
    }

    public static long toLong(boolean value) {
        return toInt(value);
    }

    public static Long toLongObj(boolean value) {
        return Long.valueOf(toLong(value));
    }

    public static short toShort(boolean value) {
        return (short) toInt(value);
    }

    public static Short toShortObj(boolean value) {
        return Short.valueOf(toShort(value));
    }

    public static float toFloat(boolean value) {
        return toInt(value);
    }

    public static Float toFloatObj(boolean value) {
        return Float.valueOf(toFloat(value));
    }

    public static double toDouble(boolean value) {
        return toInt(value);
    }

    public static Double toDoubleObj(boolean value) {
        return Double.valueOf(toDouble(value));
    }

    public static String toStringTrueFalse(boolean bool) {
        return toString(bool, "true", BooleanUtils.FALSE);
    }

    public static String toStringOnOff(boolean bool) {
        return toString(bool, BooleanUtils.ON, BooleanUtils.OFF);
    }

    public static String toStringYesNo(boolean bool) {
        return toString(bool, BooleanUtils.YES, BooleanUtils.NO);
    }

    public static String toString(boolean bool, String trueString, String falseString) {
        return bool ? trueString : falseString;
    }

    public static boolean and(boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (boolean element : array) {
            if (!element) {
                return false;
            }
        }
        return true;
    }

    public static Boolean andOfWrap(Boolean... array) {
        if (ArrayUtil.isEmpty((Object[]) array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        boolean[] primitive = (boolean[]) Convert.convert((Class<Object>) boolean[].class, (Object) array);
        return Boolean.valueOf(and(primitive));
    }

    public static boolean or(boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        for (boolean element : array) {
            if (element) {
                return true;
            }
        }
        return false;
    }

    public static Boolean orOfWrap(Boolean... array) {
        if (ArrayUtil.isEmpty((Object[]) array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        boolean[] primitive = (boolean[]) Convert.convert((Class<Object>) boolean[].class, (Object) array);
        return Boolean.valueOf(or(primitive));
    }

    public static boolean xor(boolean... array) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException("The Array must not be empty");
        }
        boolean result = false;
        for (boolean element : array) {
            result ^= element;
        }
        return result;
    }

    public static Boolean xorOfWrap(Boolean... array) {
        if (ArrayUtil.isEmpty((Object[]) array)) {
            throw new IllegalArgumentException("The Array must not be empty !");
        }
        boolean[] primitive = (boolean[]) Convert.convert((Class<Object>) boolean[].class, (Object) array);
        return Boolean.valueOf(xor(primitive));
    }

    public static boolean isBoolean(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Boolean.TYPE;
    }
}
