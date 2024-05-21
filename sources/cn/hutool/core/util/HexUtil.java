package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import java.awt.Color;
import java.math.BigInteger;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class HexUtil {
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static boolean isHexNumber(String value) {
        boolean startsWith = value.startsWith("-");
        int index = startsWith ? 1 : 0;
        if (!value.startsWith("0x", index)) {
            int index2 = startsWith ? 1 : 0;
            if (!value.startsWith("0X", index2)) {
                int index3 = startsWith ? 1 : 0;
                if (!value.startsWith("#", index3)) {
                    return false;
                }
            }
        }
        try {
            Long.decode(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(String str, Charset charset) {
        return encodeHex(StrUtil.bytes(str, charset), true);
    }

    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static String encodeHexStr(byte[] data) {
        return encodeHexStr(data, true);
    }

    public static String encodeHexStr(String data, Charset charset) {
        return encodeHexStr(StrUtil.bytes(data, charset), true);
    }

    public static String encodeHexStr(String data) {
        return encodeHexStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encodeHexStr(byte[] data, boolean toLowerCase) {
        return encodeHexStr(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    public static String decodeHexStr(String hexStr) {
        return decodeHexStr(hexStr, CharsetUtil.CHARSET_UTF_8);
    }

    public static String decodeHexStr(String hexStr, Charset charset) {
        if (StrUtil.isEmpty(hexStr)) {
            return hexStr;
        }
        return StrUtil.str(decodeHex(hexStr), charset);
    }

    public static String decodeHexStr(char[] hexData, Charset charset) {
        return StrUtil.str(decodeHex(hexData), charset);
    }

    public static byte[] decodeHex(String hexStr) {
        return decodeHex((CharSequence) hexStr);
    }

    public static byte[] decodeHex(char[] hexData) {
        return decodeHex(String.valueOf(hexData));
    }

    public static byte[] decodeHex(CharSequence hexData) {
        if (StrUtil.isEmpty(hexData)) {
            return null;
        }
        CharSequence hexData2 = StrUtil.cleanBlank(hexData);
        int len = hexData2.length();
        if ((len & 1) != 0) {
            hexData2 = "0" + ((Object) hexData2);
            len = hexData2.length();
        }
        byte[] out = new byte[len >> 1];
        int i = 0;
        int j = 0;
        while (j < len) {
            int f = toDigit(hexData2.charAt(j), j) << 4;
            int j2 = j + 1;
            j = j2 + 1;
            out[i] = (byte) ((f | toDigit(hexData2.charAt(j2), j2)) & 255);
            i++;
        }
        return out;
    }

    public static String encodeColor(Color color) {
        return encodeColor(color, "#");
    }

    public static String encodeColor(Color color, String prefix) {
        StringBuilder builder = new StringBuilder(prefix);
        String colorHex = Integer.toHexString(color.getRed());
        if (1 == colorHex.length()) {
            builder.append('0');
        }
        builder.append(colorHex);
        String colorHex2 = Integer.toHexString(color.getGreen());
        if (1 == colorHex2.length()) {
            builder.append('0');
        }
        builder.append(colorHex2);
        String colorHex3 = Integer.toHexString(color.getBlue());
        if (1 == colorHex3.length()) {
            builder.append('0');
        }
        builder.append(colorHex3);
        return builder.toString();
    }

    public static Color decodeColor(String hexColor) {
        return Color.decode(hexColor);
    }

    public static String toUnicodeHex(int value) {
        StringBuilder builder = new StringBuilder(6);
        builder.append("\\u");
        String hex = toHex(value);
        int len = hex.length();
        if (len < 4) {
            builder.append((CharSequence) "0000", 0, 4 - len);
        }
        builder.append(hex);
        return builder.toString();
    }

    public static String toUnicodeHex(char ch2) {
        return "\\u" + DIGITS_LOWER[(ch2 >> '\f') & 15] + DIGITS_LOWER[(ch2 >> '\b') & 15] + DIGITS_LOWER[(ch2 >> 4) & 15] + DIGITS_LOWER[ch2 & 15];
    }

    public static String toHex(int value) {
        return Integer.toHexString(value);
    }

    public static int hexToInt(String value) {
        return Integer.parseInt(value, 16);
    }

    public static String toHex(long value) {
        return Long.toHexString(value);
    }

    public static long hexToLong(String value) {
        return Long.parseLong(value, 16);
    }

    public static void appendHex(StringBuilder builder, byte b, boolean toLowerCase) {
        char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
        int high = (b & 240) >>> 4;
        int low = b & 15;
        builder.append(toDigits[high]);
        builder.append(toDigits[low]);
    }

    public static BigInteger toBigInteger(String hexStr) {
        if (hexStr == null) {
            return null;
        }
        return new BigInteger(hexStr, 16);
    }

    public static String format(String hexStr) {
        int length = hexStr.length();
        StringBuilder builder = StrUtil.builder((length / 2) + length);
        builder.append(hexStr.charAt(0));
        builder.append(hexStr.charAt(1));
        for (int i = 2; i < length - 1; i += 2) {
            builder.append(' ');
            builder.append(hexStr.charAt(i));
            builder.append(hexStr.charAt(i + 1));
        }
        return builder.toString();
    }

    private static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int len = data.length;
        char[] out = new char[len << 1];
        int j = 0;
        for (int i = 0; i < len; i++) {
            int j2 = j + 1;
            out[j] = toDigits[(data[i] & 240) >>> 4];
            j = j2 + 1;
            out[j2] = toDigits[data[i] & 15];
        }
        return out;
    }

    private static int toDigit(char ch2, int index) {
        int digit = Character.digit(ch2, 16);
        if (digit < 0) {
            throw new UtilException("Illegal hexadecimal character {} at index {}", Character.valueOf(ch2), Integer.valueOf(index));
        }
        return digit;
    }
}
