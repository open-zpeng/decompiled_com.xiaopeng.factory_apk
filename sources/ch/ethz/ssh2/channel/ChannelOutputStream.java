package ch.ethz.ssh2.channel;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: classes.dex */
public final class ChannelOutputStream extends OutputStream {
    Channel c;
    boolean isClosed = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelOutputStream(Channel c) {
        this.c = c;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        byte[] buff = {(byte) b};
        write(buff, 0, 1);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.c.cm.sendEOF(this.c);
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.isClosed) {
            throw new IOException("This OutputStream is closed.");
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        if (this.isClosed) {
            throw new IOException("This OutputStream is closed.");
        }
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || off + len > b.length || off + len < 0 || off > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        this.c.cm.sendData(this.c, b, off, len);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
}
