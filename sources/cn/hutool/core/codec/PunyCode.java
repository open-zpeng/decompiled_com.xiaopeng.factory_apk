package cn.hutool.core.codec;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
/* loaded from: classes.dex */
public class PunyCode {
    private static final int BASE = 36;
    private static final int DAMP = 700;
    private static final char DELIMITER = '-';
    private static final int INITIAL_BIAS = 72;
    private static final int INITIAL_N = 128;
    public static final String PUNY_CODE_PREFIX = "xn--";
    private static final int SKEW = 38;
    private static final int TMAX = 26;
    private static final int TMIN = 1;

    public static String encode(String input) throws UtilException {
        return encode(input, false);
    }

    public static String encode(String input, boolean withPrefix) throws UtilException {
        int t;
        int n = 128;
        int delta = 0;
        int bias = 72;
        StringBuilder output = new StringBuilder();
        int length = input.length();
        int b = 0;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (isBasic(c)) {
                output.append(c);
                b++;
            }
        }
        if (b > 0) {
            output.append('-');
        }
        int h = b;
        while (h < length) {
            int m = Integer.MAX_VALUE;
            for (int i2 = 0; i2 < length; i2++) {
                int c2 = input.charAt(i2);
                if (c2 >= n && c2 < m) {
                    m = c2;
                }
            }
            int i3 = m - n;
            if (i3 > (Integer.MAX_VALUE - delta) / (h + 1)) {
                throw new UtilException("OVERFLOW");
            }
            int delta2 = delta + ((m - n) * (h + 1));
            int n2 = m;
            for (int j = 0; j < length; j++) {
                int c3 = input.charAt(j);
                if (c3 < n2 && (delta2 = delta2 + 1) == 0) {
                    throw new UtilException("OVERFLOW");
                }
                if (c3 == n2) {
                    int q = delta2;
                    int k = 36;
                    while (true) {
                        if (k <= bias) {
                            t = 1;
                        } else {
                            int t2 = bias + 26;
                            if (k >= t2) {
                                t = 26;
                            } else {
                                t = k - bias;
                            }
                        }
                        if (q < t) {
                            break;
                        }
                        output.append((char) digit2codepoint(t + ((q - t) % (36 - t))));
                        q = (q - t) / (36 - t);
                        k += 36;
                    }
                    int k2 = digit2codepoint(q);
                    output.append((char) k2);
                    bias = adapt(delta2, h + 1, h == b);
                    delta2 = 0;
                    h++;
                }
            }
            delta = delta2 + 1;
            n = n2 + 1;
        }
        if (withPrefix) {
            output.insert(0, PUNY_CODE_PREFIX);
        }
        return output.toString();
    }

    public static String decode(String input) throws UtilException {
        int d;
        int t;
        String input2 = StrUtil.removePrefixIgnoreCase(input, PUNY_CODE_PREFIX);
        int n = 128;
        int i = 0;
        int bias = 72;
        StringBuilder output = new StringBuilder();
        int d2 = input2.lastIndexOf(45);
        if (d2 > 0) {
            for (int j = 0; j < d2; j++) {
                char c = input2.charAt(j);
                if (isBasic(c)) {
                    output.append(c);
                }
            }
            d = d2 + 1;
        } else {
            d = 0;
        }
        int length = input2.length();
        while (d < length) {
            int oldi = i;
            int w = 1;
            int k = 36;
            while (d != length) {
                int d3 = d + 1;
                int c2 = input2.charAt(d);
                int digit = codepoint2digit(c2);
                if (digit > (Integer.MAX_VALUE - i) / w) {
                    throw new UtilException("OVERFLOW");
                }
                i += digit * w;
                if (k <= bias) {
                    t = 1;
                } else {
                    int t2 = bias + 26;
                    if (k >= t2) {
                        t = 26;
                    } else {
                        t = k - bias;
                    }
                }
                if (digit >= t) {
                    w *= 36 - t;
                    k += 36;
                    d = d3;
                } else {
                    int c3 = i - oldi;
                    bias = adapt(c3, output.length() + 1, oldi == 0);
                    if (i / (output.length() + 1) <= Integer.MAX_VALUE - n) {
                        n += i / (output.length() + 1);
                        int i2 = i % (output.length() + 1);
                        output.insert(i2, (char) n);
                        i = i2 + 1;
                        d = d3;
                    } else {
                        throw new UtilException("OVERFLOW");
                    }
                }
            }
            throw new UtilException("BAD_INPUT");
        }
        return output.toString();
    }

    private static int adapt(int delta, int numpoints, boolean first) {
        int delta2;
        if (first) {
            delta2 = delta / DAMP;
        } else {
            delta2 = delta / 2;
        }
        int delta3 = delta2 + (delta2 / numpoints);
        int k = 0;
        while (delta3 > 455) {
            delta3 /= 35;
            k += 36;
        }
        return ((delta3 * 36) / (delta3 + 38)) + k;
    }

    private static boolean isBasic(char c) {
        return c < 128;
    }

    private static int digit2codepoint(int d) throws UtilException {
        Assert.checkBetween(d, 0, 35);
        if (d < 26) {
            return d + 97;
        }
        if (d < 36) {
            return (d - 26) + 48;
        }
        throw new UtilException("BAD_INPUT");
    }

    private static int codepoint2digit(int c) throws UtilException {
        if (c - 48 < 10) {
            return (c - 48) + 26;
        }
        if (c - 97 < 26) {
            return c - 97;
        }
        throw new UtilException("BAD_INPUT");
    }
}
