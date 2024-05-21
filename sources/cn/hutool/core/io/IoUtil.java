package cn.hutool.core.io;

import cn.hutool.core.collection.LineIter;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.copy.ReaderWriterCopier;
import cn.hutool.core.io.copy.StreamCopier;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
/* loaded from: classes.dex */
public class IoUtil extends NioUtil {
    public static long copy(Reader reader, Writer writer) throws IORuntimeException {
        return copy(reader, writer, 8192);
    }

    public static long copy(Reader reader, Writer writer, int bufferSize) throws IORuntimeException {
        return copy(reader, writer, bufferSize, (StreamProgress) null);
    }

    public static long copy(Reader reader, Writer writer, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        return copy(reader, writer, bufferSize, -1L, streamProgress);
    }

    public static long copy(Reader reader, Writer writer, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
        return new ReaderWriterCopier(bufferSize, count, streamProgress).copy(reader, writer);
    }

    public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
        return copy(in, out, 8192);
    }

    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
        return copy(in, out, bufferSize, (StreamProgress) null);
    }

    public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        return copy(in, out, bufferSize, -1L, streamProgress);
    }

    public static long copy(InputStream in, OutputStream out, int bufferSize, long count, StreamProgress streamProgress) throws IORuntimeException {
        return new StreamCopier(bufferSize, count, streamProgress).copy(in, out);
    }

    public static long copy(FileInputStream in, FileOutputStream out) throws IORuntimeException {
        Assert.notNull(in, "FileInputStream is null!", new Object[0]);
        Assert.notNull(out, "FileOutputStream is null!", new Object[0]);
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = in.getChannel();
            outChannel = out.getChannel();
            return copy(inChannel, outChannel);
        } finally {
            close((Closeable) outChannel);
            close((Closeable) inChannel);
        }
    }

    public static BufferedReader getUtf8Reader(InputStream in) {
        return getReader(in, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static BufferedReader getReader(InputStream in, String charsetName) {
        return getReader(in, Charset.forName(charsetName));
    }

    public static BufferedReader getReader(BOMInputStream in) {
        return getReader(in, in.getCharset());
    }

    public static BufferedReader getReader(InputStream in, Charset charset) {
        InputStreamReader reader;
        if (in == null) {
            return null;
        }
        if (charset == null) {
            reader = new InputStreamReader(in);
        } else {
            reader = new InputStreamReader(in, charset);
        }
        return new BufferedReader(reader);
    }

    public static BufferedReader getReader(Reader reader) {
        if (reader == null) {
            return null;
        }
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static PushbackReader getPushBackReader(Reader reader, int pushBackSize) {
        return reader instanceof PushbackReader ? (PushbackReader) reader : new PushbackReader(reader, pushBackSize);
    }

    public static OutputStreamWriter getUtf8Writer(OutputStream out) {
        return getWriter(out, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static OutputStreamWriter getWriter(OutputStream out, String charsetName) {
        return getWriter(out, Charset.forName(charsetName));
    }

    public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
        if (out == null) {
            return null;
        }
        if (charset == null) {
            return new OutputStreamWriter(out);
        }
        return new OutputStreamWriter(out, charset);
    }

    public static String readUtf8(InputStream in) throws IORuntimeException {
        return read(in, CharsetUtil.CHARSET_UTF_8);
    }

    @Deprecated
    public static String read(InputStream in, String charsetName) throws IORuntimeException {
        FastByteArrayOutputStream out = read(in);
        return StrUtil.isBlank(charsetName) ? out.toString() : out.toString(charsetName);
    }

    public static String read(InputStream in, Charset charset) throws IORuntimeException {
        return StrUtil.str(readBytes(in), charset);
    }

    public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
        return read(in, true);
    }

    public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
        FastByteArrayOutputStream out;
        if (in instanceof FileInputStream) {
            try {
                out = new FastByteArrayOutputStream(in.available());
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } else {
            out = new FastByteArrayOutputStream();
        }
        try {
            copy(in, out);
            return out;
        } finally {
            if (isClose) {
                close((Closeable) in);
            }
        }
    }

    public static String read(Reader reader) throws IORuntimeException {
        return read(reader, true);
    }

    public static String read(Reader reader, boolean isClose) throws IORuntimeException {
        StringBuilder builder = StrUtil.builder();
        CharBuffer buffer = CharBuffer.allocate(8192);
        while (-1 != reader.read(buffer)) {
            try {
                try {
                    builder.append(buffer.flip());
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            } finally {
                if (isClose) {
                    close((Closeable) reader);
                }
            }
        }
        return builder.toString();
    }

    public static byte[] readBytes(InputStream in) throws IORuntimeException {
        return readBytes(in, true);
    }

    public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
        try {
            if (in instanceof FileInputStream) {
                try {
                    int available = in.available();
                    byte[] result = new byte[available];
                    int readLength = in.read(result);
                    if (readLength != available) {
                        throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", Integer.valueOf(available), Integer.valueOf(readLength)));
                    }
                    return result;
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
            return read(in, isClose).toByteArray();
        } finally {
            if (isClose) {
                close((Closeable) in);
            }
        }
    }

    public static byte[] readBytes(InputStream in, int length) throws IORuntimeException {
        if (in == null) {
            return null;
        }
        if (length <= 0) {
            return new byte[0];
        }
        byte[] b = new byte[length];
        try {
            int readLength = in.read(b);
            if (readLength > 0 && readLength < length) {
                byte[] b2 = new byte[readLength];
                System.arraycopy(b, 0, b2, 0, readLength);
                return b2;
            }
            return b;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static String readHex(InputStream in, int length, boolean toLowerCase) throws IORuntimeException {
        return HexUtil.encodeHexStr(readBytes(in, length), toLowerCase);
    }

    public static String readHex28Upper(InputStream in) throws IORuntimeException {
        return readHex(in, 28, false);
    }

    public static String readHex28Lower(InputStream in) throws IORuntimeException {
        return readHex(in, 28, true);
    }

    public static <T> T readObj(InputStream in) throws IORuntimeException, UtilException {
        return (T) readObj(in, (Class<Object>) null);
    }

    public static <T> T readObj(InputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
        try {
            return (T) readObj(in instanceof ValidateObjectInputStream ? (ValidateObjectInputStream) in : new ValidateObjectInputStream(in, new Class[0]), (Class<Object>) clazz);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static <T> T readObj(ValidateObjectInputStream in, Class<T> clazz) throws IORuntimeException, UtilException {
        if (in == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        try {
            return (T) in.readObject();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (ClassNotFoundException e2) {
            throw new UtilException(e2);
        }
    }

    public static <T extends Collection<String>> T readUtf8Lines(InputStream in, T collection) throws IORuntimeException {
        return (T) readLines(in, CharsetUtil.CHARSET_UTF_8, collection);
    }

    @Deprecated
    public static <T extends Collection<String>> T readLines(InputStream in, String charsetName, T collection) throws IORuntimeException {
        return (T) readLines(in, CharsetUtil.charset(charsetName), collection);
    }

    public static <T extends Collection<String>> T readLines(InputStream in, Charset charset, T collection) throws IORuntimeException {
        return (T) readLines(getReader(in, charset), collection);
    }

    public static <T extends Collection<String>> T readLines(Reader reader, final T collection) throws IORuntimeException {
        collection.getClass();
        readLines(reader, new LineHandler() { // from class: cn.hutool.core.io.-$$Lambda$Sy5lDAhdNY6Kr_S2ufEy6tsFWok
            @Override // cn.hutool.core.io.LineHandler
            public final void handle(String str) {
                collection.add(str);
            }
        });
        return collection;
    }

    public static void readUtf8Lines(InputStream in, LineHandler lineHandler) throws IORuntimeException {
        readLines(in, CharsetUtil.CHARSET_UTF_8, lineHandler);
    }

    public static void readLines(InputStream in, Charset charset, LineHandler lineHandler) throws IORuntimeException {
        readLines(getReader(in, charset), lineHandler);
    }

    public static void readLines(Reader reader, LineHandler lineHandler) throws IORuntimeException {
        Assert.notNull(reader);
        Assert.notNull(lineHandler);
        Iterator<String> it = lineIter(reader).iterator();
        while (it.hasNext()) {
            String line = it.next();
            lineHandler.handle(line);
        }
    }

    @Deprecated
    public static ByteArrayInputStream toStream(String content, String charsetName) {
        return toStream(content, CharsetUtil.charset(charsetName));
    }

    public static ByteArrayInputStream toStream(String content, Charset charset) {
        if (content == null) {
            return null;
        }
        return toStream(StrUtil.bytes(content, charset));
    }

    public static ByteArrayInputStream toUtf8Stream(String content) {
        return toStream(content, CharsetUtil.CHARSET_UTF_8);
    }

    public static FileInputStream toStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IORuntimeException(e);
        }
    }

    public static ByteArrayInputStream toStream(byte[] content) {
        if (content == null) {
            return null;
        }
        return new ByteArrayInputStream(content);
    }

    public static ByteArrayInputStream toStream(ByteArrayOutputStream out) {
        if (out == null) {
            return null;
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static BufferedInputStream toBuffered(InputStream in) {
        Assert.notNull(in, "InputStream must be not null!", new Object[0]);
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public static BufferedInputStream toBuffered(InputStream in, int bufferSize) {
        Assert.notNull(in, "InputStream must be not null!", new Object[0]);
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in, bufferSize);
    }

    public static BufferedOutputStream toBuffered(OutputStream out) {
        Assert.notNull(out, "OutputStream must be not null!", new Object[0]);
        return out instanceof BufferedOutputStream ? (BufferedOutputStream) out : new BufferedOutputStream(out);
    }

    public static BufferedOutputStream toBuffered(OutputStream out, int bufferSize) {
        Assert.notNull(out, "OutputStream must be not null!", new Object[0]);
        return out instanceof BufferedOutputStream ? (BufferedOutputStream) out : new BufferedOutputStream(out, bufferSize);
    }

    public static BufferedReader toBuffered(Reader reader) {
        Assert.notNull(reader, "Reader must be not null!", new Object[0]);
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static BufferedReader toBuffered(Reader reader, int bufferSize) {
        Assert.notNull(reader, "Reader must be not null!", new Object[0]);
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, bufferSize);
    }

    public static BufferedWriter toBuffered(Writer writer) {
        Assert.notNull(writer, "Writer must be not null!", new Object[0]);
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public static BufferedWriter toBuffered(Writer writer, int bufferSize) {
        Assert.notNull(writer, "Writer must be not null!", new Object[0]);
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, bufferSize);
    }

    public static InputStream toMarkSupportStream(InputStream in) {
        if (in == null) {
            return null;
        }
        if (!in.markSupported()) {
            return new BufferedInputStream(in);
        }
        return in;
    }

    public static PushbackInputStream toPushbackStream(InputStream in, int pushBackSize) {
        return in instanceof PushbackInputStream ? (PushbackInputStream) in : new PushbackInputStream(in, pushBackSize);
    }

    public static InputStream toAvailableStream(InputStream in) {
        if (in instanceof FileInputStream) {
            return in;
        }
        PushbackInputStream pushbackInputStream = toPushbackStream(in, 1);
        try {
            int available = pushbackInputStream.available();
            if (available <= 0) {
                int b = pushbackInputStream.read();
                pushbackInputStream.unread(b);
            }
            return pushbackInputStream;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void write(OutputStream out, boolean isCloseOut, byte[] content) throws IORuntimeException {
        try {
            try {
                out.write(content);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            if (isCloseOut) {
                close((Closeable) out);
            }
        }
    }

    public static void writeUtf8(OutputStream out, boolean isCloseOut, Object... contents) throws IORuntimeException {
        write(out, CharsetUtil.CHARSET_UTF_8, isCloseOut, contents);
    }

    @Deprecated
    public static void write(OutputStream out, String charsetName, boolean isCloseOut, Object... contents) throws IORuntimeException {
        write(out, CharsetUtil.charset(charsetName), isCloseOut, contents);
    }

    public static void write(OutputStream out, Charset charset, boolean isCloseOut, Object... contents) throws IORuntimeException {
        OutputStreamWriter osw = null;
        try {
            try {
                osw = getWriter(out, charset);
                for (Object content : contents) {
                    if (content != null) {
                        osw.write(Convert.toStr(content, ""));
                    }
                }
                osw.flush();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            if (isCloseOut) {
                close((Closeable) osw);
            }
        }
    }

    public static void writeObj(OutputStream out, boolean isCloseOut, Serializable obj) throws IORuntimeException {
        writeObjects(out, isCloseOut, obj);
    }

    public static void writeObjects(OutputStream out, boolean isCloseOut, Serializable... contents) throws IORuntimeException {
        ObjectOutputStream osw = null;
        try {
            try {
                osw = out instanceof ObjectOutputStream ? (ObjectOutputStream) out : new ObjectOutputStream(out);
                for (Serializable serializable : contents) {
                    if (serializable != null) {
                        osw.writeObject(serializable);
                    }
                }
                osw.flush();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            if (isCloseOut) {
                close((Closeable) osw);
            }
        }
    }

    public static void flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (Exception e) {
            }
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeIfPosible(Object obj) {
        if (obj instanceof AutoCloseable) {
            close((AutoCloseable) obj);
        }
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IORuntimeException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }
        try {
            for (int ch2 = input1.read(); -1 != ch2; ch2 = input1.read()) {
                int ch22 = input2.read();
                if (ch2 != ch22) {
                    return false;
                }
            }
            int ch23 = input2.read();
            return ch23 == -1;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IORuntimeException {
        Reader input12 = getReader(input1);
        Reader input22 = getReader(input2);
        try {
            for (int ch2 = input12.read(); -1 != ch2; ch2 = input12.read()) {
                int ch22 = input22.read();
                if (ch2 != ch22) {
                    return false;
                }
            }
            int ch23 = input22.read();
            return ch23 == -1;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IORuntimeException {
        BufferedReader br1 = getReader(input1);
        BufferedReader br2 = getReader(input2);
        try {
            String line1 = br1.readLine();
            String line2 = br2.readLine();
            while (line1 != null && line1.equals(line2)) {
                line1 = br1.readLine();
                line2 = br2.readLine();
            }
            return Objects.equals(line1, line2);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static long checksumCRC32(InputStream in) throws IORuntimeException {
        return checksum(in, new CRC32()).getValue();
    }

    public static Checksum checksum(InputStream in, Checksum checksum) throws IORuntimeException {
        Assert.notNull(in, "InputStream is null !", new Object[0]);
        if (checksum == null) {
            checksum = new CRC32();
        }
        try {
            in = new CheckedInputStream(in, checksum);
            copy(in, new NullOutputStream());
            return checksum;
        } finally {
            close((Closeable) in);
        }
    }

    public static long checksumValue(InputStream in, Checksum checksum) {
        return checksum(in, checksum).getValue();
    }

    public static LineIter lineIter(Reader reader) {
        return new LineIter(reader);
    }

    public static LineIter lineIter(InputStream in, Charset charset) {
        return new LineIter(in, charset);
    }
}
