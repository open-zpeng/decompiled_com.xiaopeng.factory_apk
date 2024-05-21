package cn.hutool.core.compress;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ZipUtil;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
/* loaded from: classes.dex */
public class ZipReader implements Closeable {
    private ZipInputStream in;
    private ZipFile zipFile;

    public static ZipReader of(File zipFile, Charset charset) {
        return new ZipReader(zipFile, charset);
    }

    public static ZipReader of(InputStream in, Charset charset) {
        return new ZipReader(in, charset);
    }

    public ZipReader(File zipFile, Charset charset) {
        this.zipFile = ZipUtil.toZipFile(zipFile, charset);
    }

    public ZipReader(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    public ZipReader(InputStream in, Charset charset) {
        this.in = new ZipInputStream(in, charset);
    }

    public ZipReader(ZipInputStream zin) {
        this.in = zin;
    }

    public InputStream get(String path) {
        ZipEntry zipEntry;
        if (this.zipFile != null) {
            ZipFile zipFile = this.zipFile;
            ZipEntry entry = zipFile.getEntry(path);
            if (entry != null) {
                return ZipUtil.getStream(zipFile, entry);
            }
            return null;
        }
        try {
            this.in.reset();
            do {
                zipEntry = this.in.getNextEntry();
                if (zipEntry == null) {
                    return null;
                }
            } while (!zipEntry.getName().equals(path));
            return this.in;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public File readTo(File outFile) throws IORuntimeException {
        return readTo(outFile, null);
    }

    public File readTo(final File outFile, final Filter<ZipEntry> entryFilter) throws IORuntimeException {
        read(new Consumer() { // from class: cn.hutool.core.compress.-$$Lambda$ZipReader$BeppoxQURKKPDQmfj2STHPDtZ0Y
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ZipReader.this.lambda$readTo$0$ZipReader(entryFilter, outFile, (ZipEntry) obj);
            }
        });
        return outFile;
    }

    public /* synthetic */ void lambda$readTo$0$ZipReader(Filter entryFilter, File outFile, ZipEntry zipEntry) {
        InputStream in;
        if (entryFilter == null || entryFilter.accept(zipEntry)) {
            File outItemFile = FileUtil.file(outFile, zipEntry.getName());
            if (zipEntry.isDirectory()) {
                outItemFile.mkdirs();
                return;
            }
            ZipFile zipFile = this.zipFile;
            if (zipFile != null) {
                in = ZipUtil.getStream(zipFile, zipEntry);
            } else {
                in = this.in;
            }
            FileUtil.writeFromStream(in, outItemFile, false);
        }
    }

    public ZipReader read(Consumer<ZipEntry> consumer) throws IORuntimeException {
        if (this.zipFile != null) {
            readFromZipFile(consumer);
        } else {
            readFromStream(consumer);
        }
        return this;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IORuntimeException {
        ZipFile zipFile = this.zipFile;
        if (zipFile != null) {
            IoUtil.close((Closeable) zipFile);
        } else {
            IoUtil.close((Closeable) this.in);
        }
    }

    private void readFromZipFile(Consumer<ZipEntry> consumer) {
        Enumeration<? extends ZipEntry> em = this.zipFile.entries();
        while (em.hasMoreElements()) {
            consumer.accept(em.nextElement());
        }
    }

    private void readFromStream(Consumer<ZipEntry> consumer) throws IORuntimeException {
        while (true) {
            try {
                ZipEntry zipEntry = this.in.getNextEntry();
                if (zipEntry != null) {
                    consumer.accept(zipEntry);
                } else {
                    return;
                }
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
    }
}
