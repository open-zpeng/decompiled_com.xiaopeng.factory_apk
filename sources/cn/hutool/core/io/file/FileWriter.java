package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
/* loaded from: classes.dex */
public class FileWriter extends FileWrapper {
    private static final long serialVersionUID = 1;

    public static FileWriter create(File file, Charset charset) {
        return new FileWriter(file, charset);
    }

    public static FileWriter create(File file) {
        return new FileWriter(file);
    }

    public FileWriter(File file, Charset charset) {
        super(file, charset);
        checkFile();
    }

    public FileWriter(File file, String charset) {
        this(file, CharsetUtil.charset(charset));
    }

    public FileWriter(String filePath, Charset charset) {
        this(FileUtil.file(filePath), charset);
    }

    public FileWriter(String filePath, String charset) {
        this(FileUtil.file(filePath), CharsetUtil.charset(charset));
    }

    public FileWriter(File file) {
        this(file, DEFAULT_CHARSET);
    }

    public FileWriter(String filePath) {
        this(filePath, DEFAULT_CHARSET);
    }

    public File write(String content, boolean isAppend) throws IORuntimeException {
        BufferedWriter writer = null;
        try {
            try {
                writer = getWriter(isAppend);
                writer.write(content);
                writer.flush();
                IoUtil.close((Closeable) writer);
                return this.file;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } catch (Throwable th) {
            IoUtil.close((Closeable) writer);
            throw th;
        }
    }

    public File write(String content) throws IORuntimeException {
        return write(content, false);
    }

    public File append(String content) throws IORuntimeException {
        return write(content, true);
    }

    public <T> File writeLines(Collection<T> list) throws IORuntimeException {
        return writeLines(list, false);
    }

    public <T> File appendLines(Collection<T> list) throws IORuntimeException {
        return writeLines(list, true);
    }

    public <T> File writeLines(Collection<T> list, boolean isAppend) throws IORuntimeException {
        return writeLines(list, null, isAppend);
    }

    public <T> File writeLines(Collection<T> list, LineSeparator lineSeparator, boolean isAppend) throws IORuntimeException {
        PrintWriter writer = getPrintWriter(isAppend);
        try {
            for (T t : list) {
                if (t != null) {
                    writer.print(t);
                    printNewLine(writer, lineSeparator);
                    writer.flush();
                }
            }
            if (writer != null) {
                writer.close();
            }
            return this.file;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    public File writeMap(Map<?, ?> map, String kvSeparator, boolean isAppend) throws IORuntimeException {
        return writeMap(map, null, kvSeparator, isAppend);
    }

    public File writeMap(Map<?, ?> map, LineSeparator lineSeparator, String kvSeparator, boolean isAppend) throws IORuntimeException {
        if (kvSeparator == null) {
            kvSeparator = " = ";
        }
        PrintWriter writer = getPrintWriter(isAppend);
        try {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry != null) {
                    writer.print(StrUtil.format("{}{}{}", entry.getKey(), kvSeparator, entry.getValue()));
                    printNewLine(writer, lineSeparator);
                    writer.flush();
                }
            }
            if (writer != null) {
                writer.close();
            }
            return this.file;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    public File write(byte[] data, int off, int len) throws IORuntimeException {
        return write(data, off, len, false);
    }

    public File append(byte[] data, int off, int len) throws IORuntimeException {
        return write(data, off, len, true);
    }

    public File write(byte[] data, int off, int len, boolean isAppend) throws IORuntimeException {
        try {
            FileOutputStream out = new FileOutputStream(FileUtil.touch(this.file), isAppend);
            out.write(data, off, len);
            out.flush();
            out.close();
            return this.file;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public File writeFromStream(InputStream in) throws IORuntimeException {
        return writeFromStream(in, true);
    }

    public File writeFromStream(InputStream in, boolean isCloseIn) throws IORuntimeException {
        FileOutputStream out = null;
        try {
            try {
                out = new FileOutputStream(FileUtil.touch(this.file));
                IoUtil.copy(in, out);
                return this.file;
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) out);
            if (isCloseIn) {
                IoUtil.close((Closeable) in);
            }
        }
    }

    public BufferedOutputStream getOutputStream() throws IORuntimeException {
        try {
            return new BufferedOutputStream(new FileOutputStream(FileUtil.touch(this.file)));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public BufferedWriter getWriter(boolean isAppend) throws IORuntimeException {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileUtil.touch(this.file), isAppend), this.charset));
        } catch (Exception e) {
            throw new IORuntimeException(e);
        }
    }

    public PrintWriter getPrintWriter(boolean isAppend) throws IORuntimeException {
        return new PrintWriter(getWriter(isAppend));
    }

    private void checkFile() throws IORuntimeException {
        Assert.notNull(this.file, "File to write content is null !", new Object[0]);
        if (this.file.exists() && !this.file.isFile()) {
            throw new IORuntimeException("File [{}] is not a file !", this.file.getAbsoluteFile());
        }
    }

    private void printNewLine(PrintWriter writer, LineSeparator lineSeparator) {
        if (lineSeparator == null) {
            writer.println();
        } else {
            writer.print(lineSeparator.getValue());
        }
    }
}
