package cn.hutool.core.io.copy;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Assert;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class StreamCopier extends IoCopier<InputStream, OutputStream> {
    public StreamCopier() {
        this(8192);
    }

    public StreamCopier(int bufferSize) {
        this(bufferSize, -1L);
    }

    public StreamCopier(int bufferSize, long count) {
        this(bufferSize, count, null);
    }

    public StreamCopier(int bufferSize, long count, StreamProgress progress) {
        super(bufferSize, count, progress);
    }

    @Override // cn.hutool.core.io.copy.IoCopier
    public long copy(InputStream source, OutputStream target) {
        Assert.notNull(source, "InputStream is null !", new Object[0]);
        Assert.notNull(target, "OutputStream is null !", new Object[0]);
        StreamProgress progress = this.progress;
        if (progress != null) {
            progress.start();
        }
        try {
            long size = doCopy(source, target, new byte[bufferSize(this.count)], progress);
            target.flush();
            if (progress != null) {
                progress.finish();
            }
            return size;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private long doCopy(InputStream source, OutputStream target, byte[] buffer, StreamProgress progress) throws IOException {
        int read;
        long numToRead = this.count > 0 ? this.count : Long.MAX_VALUE;
        long total = 0;
        while (numToRead > 0 && (read = source.read(buffer, 0, bufferSize(numToRead))) >= 0) {
            target.write(buffer, 0, read);
            numToRead -= read;
            total += read;
            if (progress != null) {
                progress.progress(total);
            }
        }
        return total;
    }
}
