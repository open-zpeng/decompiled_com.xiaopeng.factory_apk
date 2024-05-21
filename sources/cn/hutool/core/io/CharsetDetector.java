package cn.hutool.core.io;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import com.google.zxing.common.StringUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.apache.commons.lang3.CharEncoding;
/* loaded from: classes.dex */
public class CharsetDetector {
    private static final Charset[] DEFAULT_CHARSETS;

    static {
        String[] names = {"UTF-8", CharsetUtil.GBK, StringUtils.GB2312, "GB18030", CharEncoding.UTF_16BE, CharEncoding.UTF_16LE, CharEncoding.UTF_16, "BIG5", "UNICODE", CharEncoding.US_ASCII};
        DEFAULT_CHARSETS = (Charset[]) Convert.convert((Class<Object>) Charset[].class, (Object) names);
    }

    public static Charset detect(File file, Charset... charsets) {
        return detect(FileUtil.getInputStream(file), charsets);
    }

    public static Charset detect(InputStream in, Charset... charsets) {
        return detect(8192, in, charsets);
    }

    public static Charset detect(int bufferSize, InputStream in, Charset... charsets) {
        if (ArrayUtil.isEmpty((Object[]) charsets)) {
            charsets = DEFAULT_CHARSETS;
        }
        byte[] buffer = new byte[bufferSize];
        while (in.read(buffer) > -1) {
            try {
                try {
                    int length = charsets.length;
                    int i = 0;
                    while (true) {
                        if (i < length) {
                            Charset charset = charsets[i];
                            CharsetDecoder decoder = charset.newDecoder();
                            if (identify(buffer, decoder)) {
                                return charset;
                            }
                            i++;
                        }
                    }
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            } finally {
                IoUtil.close((Closeable) in);
            }
        }
        IoUtil.close((Closeable) in);
        return null;
    }

    private static boolean identify(byte[] bytes, CharsetDecoder decoder) {
        try {
            decoder.decode(ByteBuffer.wrap(bytes));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }
}
