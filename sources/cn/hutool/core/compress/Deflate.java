package cn.hutool.core.compress;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterOutputStream;
/* loaded from: classes.dex */
public class Deflate implements Closeable {
    private final boolean nowrap;
    private final InputStream source;
    private OutputStream target;

    public static Deflate of(InputStream source, OutputStream target, boolean nowrap) {
        return new Deflate(source, target, nowrap);
    }

    public Deflate(InputStream source, OutputStream target, boolean nowrap) {
        this.source = source;
        this.target = target;
        this.nowrap = nowrap;
    }

    public OutputStream getTarget() {
        return this.target;
    }

    public Deflate deflater(int level) {
        OutputStream outputStream = this.target;
        this.target = outputStream instanceof DeflaterOutputStream ? (DeflaterOutputStream) outputStream : new DeflaterOutputStream(outputStream, new Deflater(level, this.nowrap));
        IoUtil.copy(this.source, this.target);
        try {
            ((DeflaterOutputStream) this.target).finish();
            return this;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public Deflate inflater() {
        OutputStream outputStream = this.target;
        this.target = outputStream instanceof InflaterOutputStream ? (InflaterOutputStream) outputStream : new InflaterOutputStream(outputStream, new Inflater(this.nowrap));
        IoUtil.copy(this.source, this.target);
        try {
            ((InflaterOutputStream) this.target).finish();
            return this;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        IoUtil.close((Closeable) this.target);
        IoUtil.close((Closeable) this.source);
    }
}
