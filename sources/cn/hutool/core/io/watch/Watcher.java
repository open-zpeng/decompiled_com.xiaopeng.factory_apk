package cn.hutool.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
/* loaded from: classes.dex */
public interface Watcher {
    void onCreate(WatchEvent<?> watchEvent, Path path);

    void onDelete(WatchEvent<?> watchEvent, Path path);

    void onModify(WatchEvent<?> watchEvent, Path path);

    void onOverflow(WatchEvent<?> watchEvent, Path path);
}
