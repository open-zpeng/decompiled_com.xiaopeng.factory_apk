package cn.hutool.core.collection;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public class LineIter implements Iterator<String>, Iterable<String>, Closeable, Serializable {
    private static final long serialVersionUID = 1;
    private final BufferedReader bufferedReader;
    private String cachedLine;
    private boolean finished;

    public LineIter(InputStream in, Charset charset) throws IllegalArgumentException {
        this(IoUtil.getReader(in, charset));
    }

    public LineIter(Reader reader) throws IllegalArgumentException {
        this.finished = false;
        Assert.notNull(reader, "Reader must not be null", new Object[0]);
        this.bufferedReader = IoUtil.getReader(reader);
    }

    @Override // java.util.Iterator
    public boolean hasNext() throws IORuntimeException {
        String line;
        if (this.cachedLine != null) {
            return true;
        }
        if (this.finished) {
            return false;
        }
        do {
            try {
                line = this.bufferedReader.readLine();
                if (line == null) {
                    this.finished = true;
                    return false;
                }
            } catch (IOException ioe) {
                close();
                throw new IORuntimeException(ioe);
            }
        } while (!isValidLine(line));
        this.cachedLine = line;
        return true;
    }

    @Override // java.util.Iterator
    public String next() throws NoSuchElementException {
        return nextLine();
    }

    public String nextLine() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        String currentLine = this.cachedLine;
        this.cachedLine = null;
        return currentLine;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.finished = true;
        IoUtil.close((Closeable) this.bufferedReader);
        this.cachedLine = null;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Remove unsupported on LineIterator");
    }

    protected boolean isValidLine(String line) {
        return true;
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return this;
    }
}
