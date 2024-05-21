package cn.hutool.core.io;

import cn.hutool.core.io.copy.ChannelCopier;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class NioUtil {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 32768;
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 16384;
    public static final int EOF = -1;

    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        return copyByNIO(in, out, bufferSize, -1L, streamProgress);
    }

    public static long copyByNIO(InputStream in, OutputStream out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
        return copy(Channels.newChannel(in), Channels.newChannel(out), bufferSize, count, streamProgress);
    }

    public static long copy(FileChannel inChannel, FileChannel outChannel) throws IORuntimeException {
        Assert.notNull(inChannel, "In channel is null!", new Object[0]);
        Assert.notNull(outChannel, "Out channel is null!", new Object[0]);
        try {
            return inChannel.transferTo(0L, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static long copy(ReadableByteChannel in, WritableByteChannel out) throws IORuntimeException {
        return copy(in, out, 8192);
    }

    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize) throws IORuntimeException {
        return copy(in, out, bufferSize, null);
    }

    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        return copy(in, out, bufferSize, -1L, streamProgress);
    }

    public static long copy(ReadableByteChannel in, WritableByteChannel out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
        return new ChannelCopier(bufferSize, count, streamProgress).copy(in, out);
    }

    public static String read(ReadableByteChannel channel, Charset charset) throws IORuntimeException {
        FastByteArrayOutputStream out = read(channel);
        return charset == null ? out.toString() : out.toString(charset);
    }

    public static FastByteArrayOutputStream read(ReadableByteChannel channel) throws IORuntimeException {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(channel, Channels.newChannel(out));
        return out;
    }

    public static String readUtf8(FileChannel fileChannel) throws IORuntimeException {
        return read(fileChannel, CharsetUtil.CHARSET_UTF_8);
    }

    public static String read(FileChannel fileChannel, String charsetName) throws IORuntimeException {
        return read(fileChannel, CharsetUtil.charset(charsetName));
    }

    public static String read(FileChannel fileChannel, Charset charset) throws IORuntimeException {
        try {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size()).load();
            return StrUtil.str((ByteBuffer) buffer, charset);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
