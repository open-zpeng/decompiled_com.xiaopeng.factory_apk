package cn.hutool.core.io.watch;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
@FunctionalInterface
/* loaded from: classes.dex */
public interface WatchAction {
    void doAction(WatchEvent<?> watchEvent, Path path);
}
