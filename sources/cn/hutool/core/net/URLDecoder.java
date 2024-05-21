package cn.hutool.core.net;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class URLDecoder implements Serializable {
    private static final byte ESCAPE_CHAR = 37;
    private static final long serialVersionUID = 1;

    public static String decodeForPath(String str, Charset charset) {
        return decode(str, charset, false);
    }

    public static String decode(String str, Charset charset) {
        return decode(str, charset, true);
    }

    public static String decode(String str, Charset charset, boolean isPlusToSpace) {
        return StrUtil.str(decode(StrUtil.bytes(str, charset), isPlusToSpace), charset);
    }

    public static byte[] decode(byte[] bytes) {
        return decode(bytes, true);
    }

    public static byte[] decode(byte[] bytes, boolean isPlusToSpace) {
        int u;
        int l;
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            int b = bytes[i];
            if (b == 43) {
                buffer.write(isPlusToSpace ? 32 : b);
            } else if (b == 37) {
                if (i + 1 < bytes.length && (u = CharUtil.digit16(bytes[i + 1])) >= 0 && i + 2 < bytes.length && (l = CharUtil.digit16(bytes[i + 2])) >= 0) {
                    buffer.write((char) ((u << 4) + l));
                    i += 2;
                } else {
                    buffer.write(b);
                }
            } else {
                buffer.write(b);
            }
            i++;
        }
        return buffer.toByteArray();
    }
}
