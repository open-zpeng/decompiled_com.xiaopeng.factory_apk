package cn.hutool.core.codec;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class Base32 {
    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int[] BASE32_LOOKUP = {255, 255, 26, 27, 28, 29, 30, 31, 255, 255, 255, 255, 255, 255, 255, 255, 255, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 255, 255, 255, 255, 255, 255, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 255, 255, 255, 255, 255};

    private Base32() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static String encode(byte[] bytes) {
        int nextByte;
        int nextByte2;
        int i = 0;
        int digit = 0;
        StringBuilder base32 = new StringBuilder(((bytes.length + 7) * 8) / 5);
        while (i < bytes.length) {
            int currByte = bytes[i] >= 0 ? bytes[i] : bytes[i] + 256;
            if (digit > 3) {
                if (i + 1 < bytes.length) {
                    nextByte2 = bytes[i + 1] >= 0 ? bytes[i + 1] : bytes[i + 1] + 256;
                } else {
                    nextByte2 = 0;
                }
                int index = (digit + 5) % 8;
                int digit2 = 8 - index;
                i++;
                nextByte = (((255 >> digit) & currByte) << index) | (nextByte2 >> digit2);
                digit = index;
            } else {
                nextByte = (currByte >> (8 - (digit + 5))) & 31;
                int index2 = (digit + 5) % 8;
                if (index2 != 0) {
                    digit = index2;
                } else {
                    i++;
                    digit = index2;
                }
            }
            base32.append(BASE32_CHARS.charAt(nextByte));
        }
        return base32.toString();
    }

    public static String encode(String source) {
        return encode(source, CharsetUtil.CHARSET_UTF_8);
    }

    public static String encode(String source, String charset) {
        return encode(StrUtil.bytes(source, charset));
    }

    public static String encode(String source, Charset charset) {
        return encode(StrUtil.bytes(source, charset));
    }

    public static byte[] decode(String base32) {
        int digit;
        byte[] bytes = new byte[(base32.length() * 5) / 8];
        int index = 0;
        int offset = 0;
        for (int i = 0; i < base32.length(); i++) {
            int lookup = base32.charAt(i) - '0';
            if (lookup >= 0) {
                int[] iArr = BASE32_LOOKUP;
                if (lookup < iArr.length && (digit = iArr[lookup]) != 255) {
                    if (index <= 3) {
                        index = (index + 5) % 8;
                        if (index == 0) {
                            bytes[offset] = (byte) (bytes[offset] | digit);
                            offset++;
                            if (offset >= bytes.length) {
                                break;
                            }
                        } else {
                            bytes[offset] = (byte) (bytes[offset] | (digit << (8 - index)));
                        }
                    } else {
                        index = (index + 5) % 8;
                        bytes[offset] = (byte) (bytes[offset] | (digit >>> index));
                        offset++;
                        if (offset >= bytes.length) {
                            break;
                        }
                        bytes[offset] = (byte) (bytes[offset] | (digit << (8 - index)));
                    }
                }
            }
        }
        return bytes;
    }

    public static String decodeStr(String source) {
        return decodeStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    public static String decodeStr(String source, String charset) {
        return StrUtil.str(decode(source), charset);
    }

    public static String decodeStr(String source, Charset charset) {
        return StrUtil.str(decode(source), charset);
    }
}
