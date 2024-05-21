package ch.ethz.ssh2.channel;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public final class ChannelInputStream extends InputStream {
    Channel c;
    boolean extendedFlag;
    boolean isClosed = false;
    boolean isEOF = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelInputStream(Channel c, boolean isExtended) {
        this.extendedFlag = false;
        this.c = c;
        this.extendedFlag = isExtended;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int avail;
        if (!this.isEOF && (avail = this.c.cm.getAvailable(this.c, this.extendedFlag)) > 0) {
            return avail;
        }
        return 0;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.isClosed = true;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (off < 0 || len < 0 || off + len > b.length || off + len < 0 || off > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        if (this.isEOF) {
            return -1;
        }
        int ret = this.c.cm.getChannelData(this.c, this.extendedFlag, b, off, len);
        if (ret == -1) {
            this.isEOF = true;
        }
        return ret;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        byte[] b = new byte[1];
        int ret = read(b, 0, 1);
        if (ret != 1) {
            return -1;
        }
        return b[0] & 255;
    }
}
