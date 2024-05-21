package cn.hutool.core.codec;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class Base64 {
    public static byte[] encode(byte[] arr, boolean lineSep) {
        return Base64Encoder.encode(arr, lineSep);
    }

    public static byte[] encodeUrlSafe(byte[] arr, boolean lineSep) {
        return Base64Encoder.encodeUrlSafe(arr, lineSep);
    }

    public static String encode(CharSequence source) {
        return Base64Encoder.encode(source);
    }

    public static String encodeUrlSafe(CharSequence source) {
        return Base64Encoder.encodeUrlSafe(source);
    }

    public static String encode(CharSequence source, String charset) {
        return encode(source, CharsetUtil.charset(charset));
    }

    public static String encodeWithoutPadding(CharSequence source, String charset) {
        return encodeWithoutPadding(StrUtil.bytes(source, charset));
    }

    public static String encodeUrlSafe(CharSequence source, String charset) {
        return encodeUrlSafe(source, CharsetUtil.charset(charset));
    }

    public static String encode(CharSequence source, Charset charset) {
        return Base64Encoder.encode(source, charset);
    }

    public static String encodeUrlSafe(CharSequence source, Charset charset) {
        return Base64Encoder.encodeUrlSafe(source, charset);
    }

    public static String encode(byte[] source) {
        return Base64Encoder.encode(source);
    }

    public static String encodeWithoutPadding(byte[] source) {
        return java.util.Base64.getEncoder().withoutPadding().encodeToString(source);
    }

    public static String encodeUrlSafe(byte[] source) {
        return Base64Encoder.encodeUrlSafe(source);
    }

    public static String encode(InputStream in) {
        return Base64Encoder.encode(IoUtil.readBytes(in));
    }

    public static String encodeUrlSafe(InputStream in) {
        return Base64Encoder.encodeUrlSafe(IoUtil.readBytes(in));
    }

    public static String encode(File file) {
        return Base64Encoder.encode(FileUtil.readBytes(file));
    }

    public static String encodeUrlSafe(File file) {
        return Base64Encoder.encodeUrlSafe(FileUtil.readBytes(file));
    }

    public static String encodeStr(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
        return Base64Encoder.encodeStr(arr, isMultiLine, isUrlSafe);
    }

    public static byte[] encode(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
        return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
    }

    public static String decodeStrGbk(CharSequence source) {
        return Base64Decoder.decodeStr(source, CharsetUtil.CHARSET_GBK);
    }

    public static String decodeStr(CharSequence source) {
        return Base64Decoder.decodeStr(source);
    }

    public static String decodeStr(CharSequence source, String charset) {
        return decodeStr(source, CharsetUtil.charset(charset));
    }

    public static String decodeStr(CharSequence source, Charset charset) {
        return Base64Decoder.decodeStr(source, charset);
    }

    public static File decodeToFile(CharSequence base64, File destFile) {
        return FileUtil.writeBytes(Base64Decoder.decode(base64), destFile);
    }

    public static void decodeToStream(CharSequence base64, OutputStream out, boolean isCloseOut) {
        IoUtil.write(out, isCloseOut, Base64Decoder.decode(base64));
    }

    public static byte[] decode(CharSequence base64) {
        return Base64Decoder.decode(base64);
    }

    public static byte[] decode(byte[] in) {
        return Base64Decoder.decode(in);
    }

    public static boolean isBase64(CharSequence base64) {
        return isBase64(StrUtil.utf8Bytes(base64));
    }

    public static boolean isBase64(byte[] base64Bytes) {
        int length = base64Bytes.length;
        boolean hasPadding = false;
        int i = 0;
        while (true) {
            boolean z = true;
            if (i >= length) {
                return true;
            }
            byte base64Byte = base64Bytes[i];
            if (hasPadding) {
                if (61 != base64Byte) {
                    return false;
                }
            } else if (61 == base64Byte) {
                hasPadding = true;
            }
            if (!Base64Decoder.isBase64Code(base64Byte) && !isWhiteSpace(base64Byte)) {
                z = false;
            }
            if (!z) {
                return false;
            }
            i++;
        }
    }

    private static boolean isWhiteSpace(byte byteToCheck) {
        if (byteToCheck == 9 || byteToCheck == 10 || byteToCheck == 13 || byteToCheck == 32) {
            return true;
        }
        return false;
    }
}
