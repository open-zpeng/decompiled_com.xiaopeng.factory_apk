package cn.hutool.core.io.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.watch.SimpleWatcher;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
/* loaded from: classes.dex */
public class LineReadWatcher extends SimpleWatcher implements Runnable {
    private final Charset charset;
    private final LineHandler lineHandler;
    private final RandomAccessFile randomAccessFile;

    public LineReadWatcher(RandomAccessFile randomAccessFile, Charset charset, LineHandler lineHandler) {
        this.randomAccessFile = randomAccessFile;
        this.charset = charset;
        this.lineHandler = lineHandler;
    }

    @Override // java.lang.Runnable
    public void run() {
        onModify(null, null);
    }

    @Override // cn.hutool.core.io.watch.watchers.IgnoreWatcher, cn.hutool.core.io.watch.Watcher
    public void onModify(WatchEvent<?> event, Path currentPath) {
        RandomAccessFile randomAccessFile = this.randomAccessFile;
        Charset charset = this.charset;
        LineHandler lineHandler = this.lineHandler;
        try {
            long currentLength = randomAccessFile.length();
            long position = randomAccessFile.getFilePointer();
            if (0 != currentLength && position != currentLength) {
                if (currentLength < position) {
                    randomAccessFile.seek(currentLength);
                    return;
                }
                FileUtil.readLines(randomAccessFile, charset, lineHandler);
                randomAccessFile.seek(currentLength);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
