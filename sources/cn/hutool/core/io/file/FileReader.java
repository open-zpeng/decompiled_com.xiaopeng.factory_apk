package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public class FileReader extends FileWrapper {
    private static final long serialVersionUID = 1;

    /* loaded from: classes.dex */
    public interface ReaderHandler<T> {
        T handle(BufferedReader bufferedReader) throws IOException;
    }

    public static FileReader create(File file, Charset charset) {
        return new FileReader(file, charset);
    }

    public static FileReader create(File file) {
        return new FileReader(file);
    }

    public FileReader(File file, Charset charset) {
        super(file, charset);
        checkFile();
    }

    public FileReader(File file, String charset) {
        this(file, CharsetUtil.charset(charset));
    }

    public FileReader(String filePath, Charset charset) {
        this(FileUtil.file(filePath), charset);
    }

    public FileReader(String filePath, String charset) {
        this(FileUtil.file(filePath), CharsetUtil.charset(charset));
    }

    public FileReader(File file) {
        this(file, DEFAULT_CHARSET);
    }

    public FileReader(String filePath) {
        this(filePath, DEFAULT_CHARSET);
    }

    public byte[] readBytes() throws IORuntimeException {
        long len = this.file.length();
        if (len >= 2147483647L) {
            throw new IORuntimeException("File is larger then max array size");
        }
        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        try {
            try {
                in = new FileInputStream(this.file);
                int readLength = in.read(bytes);
                if (readLength < len) {
                    throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", Long.valueOf(len), Integer.valueOf(readLength)));
                }
                return bytes;
            } catch (Exception e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public String readString() throws IORuntimeException {
        return new String(readBytes(), this.charset);
    }

    public <T extends Collection<String>> T readLines(T collection) throws IORuntimeException {
        BufferedReader reader = null;
        try {
            try {
                reader = FileUtil.getReader(this.file, this.charset);
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        collection.add(line);
                    } else {
                        return collection;
                    }
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }

    public void readLines(LineHandler lineHandler) throws IORuntimeException {
        BufferedReader reader = null;
        try {
            reader = FileUtil.getReader(this.file, this.charset);
            IoUtil.readLines(reader, lineHandler);
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }

    public List<String> readLines() throws IORuntimeException {
        return (List) readLines((FileReader) new ArrayList());
    }

    public <T> T read(ReaderHandler<T> readerHandler) throws IORuntimeException {
        BufferedReader reader = null;
        try {
            try {
                reader = FileUtil.getReader(this.file, this.charset);
                T result = readerHandler.handle(reader);
                return result;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) reader);
        }
    }

    public BufferedReader getReader() throws IORuntimeException {
        return IoUtil.getReader(getInputStream(), this.charset);
    }

    public BufferedInputStream getInputStream() throws IORuntimeException {
        try {
            return new BufferedInputStream(new FileInputStream(this.file));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public long writeToStream(OutputStream out) throws IORuntimeException {
        return writeToStream(out, false);
    }

    public long writeToStream(OutputStream out, boolean isCloseOut) throws IORuntimeException {
        try {
            try {
                FileInputStream in = new FileInputStream(this.file);
                try {
                    long copy = IoUtil.copy(in, out);
                    in.close();
                    return copy;
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        try {
                            in.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                        throw th2;
                    }
                }
            } finally {
                if (isCloseOut) {
                    IoUtil.close((Closeable) out);
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private void checkFile() throws IORuntimeException {
        if (!this.file.exists()) {
            throw new IORuntimeException("File not exist: " + this.file);
        } else if (!this.file.isFile()) {
            throw new IORuntimeException("Not a file:" + this.file);
        }
    }
}
