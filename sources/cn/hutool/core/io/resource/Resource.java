package cn.hutool.core.io.resource;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public interface Resource {
    String getName();

    InputStream getStream();

    URL getUrl();

    default void writeTo(OutputStream out) throws IORuntimeException {
        try {
            InputStream in = getStream();
            IoUtil.copy(in, out);
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    default BufferedReader getReader(Charset charset) {
        return IoUtil.getReader(getStream(), charset);
    }

    default String readStr(Charset charset) throws IORuntimeException {
        return IoUtil.read(getReader(charset));
    }

    default String readUtf8Str() throws IORuntimeException {
        return readStr(CharsetUtil.CHARSET_UTF_8);
    }

    default byte[] readBytes() throws IORuntimeException {
        return IoUtil.readBytes(getStream());
    }
}
