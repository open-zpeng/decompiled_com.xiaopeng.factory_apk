package cn.hutool.core.io.copy;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Assert;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
/* loaded from: classes.dex */
public class ReaderWriterCopier extends IoCopier<Reader, Writer> {
    public ReaderWriterCopier() {
        this(8192);
    }

    public ReaderWriterCopier(int bufferSize) {
        this(bufferSize, -1L);
    }

    public ReaderWriterCopier(int bufferSize, long count) {
        this(bufferSize, count, null);
    }

    public ReaderWriterCopier(int bufferSize, long count, StreamProgress progress) {
        super(bufferSize, count, progress);
    }

    @Override // cn.hutool.core.io.copy.IoCopier
    public long copy(Reader source, Writer target) {
        Assert.notNull(source, "InputStream is null !", new Object[0]);
        Assert.notNull(target, "OutputStream is null !", new Object[0]);
        StreamProgress progress = this.progress;
        if (progress != null) {
            progress.start();
        }
        try {
            long size = doCopy(source, target, new char[bufferSize(this.count)], progress);
            target.flush();
            if (progress != null) {
                progress.finish();
            }
            return size;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    private long doCopy(Reader source, Writer target, char[] buffer, StreamProgress progress) throws IOException {
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
