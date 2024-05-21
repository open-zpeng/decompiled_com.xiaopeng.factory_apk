package cn.hutool.core.io.watch.watchers;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.io.watch.Watcher;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.Set;
/* loaded from: classes.dex */
public class DelayWatcher implements Watcher {
    private final long delay;
    private final Set<Path> eventSet = new ConcurrentHashSet();
    private final Watcher watcher;

    public DelayWatcher(Watcher watcher, long delay) {
        Assert.notNull(watcher);
        if (watcher instanceof DelayWatcher) {
            throw new IllegalArgumentException("Watcher must not be a DelayWatcher");
        }
        this.watcher = watcher;
        this.delay = delay;
    }

    @Override // cn.hutool.core.io.watch.Watcher
    public void onModify(WatchEvent<?> event, Path currentPath) {
        if (this.delay < 1) {
            this.watcher.onModify(event, currentPath);
        } else {
            onDelayModify(event, currentPath);
        }
    }

    @Override // cn.hutool.core.io.watch.Watcher
    public void onCreate(WatchEvent<?> event, Path currentPath) {
        this.watcher.onCreate(event, currentPath);
    }

    @Override // cn.hutool.core.io.watch.Watcher
    public void onDelete(WatchEvent<?> event, Path currentPath) {
        this.watcher.onDelete(event, currentPath);
    }

    @Override // cn.hutool.core.io.watch.Watcher
    public void onOverflow(WatchEvent<?> event, Path currentPath) {
        this.watcher.onOverflow(event, currentPath);
    }

    private void onDelayModify(WatchEvent<?> event, Path currentPath) {
        Path eventPath = Paths.get(currentPath.toString(), event.context().toString());
        if (this.eventSet.contains(eventPath)) {
            return;
        }
        this.eventSet.add(eventPath);
        startHandleModifyThread(event, currentPath);
    }

    private void startHandleModifyThread(final WatchEvent<?> event, final Path currentPath) {
        ThreadUtil.execute(new Runnable() { // from class: cn.hutool.core.io.watch.watchers.-$$Lambda$DelayWatcher$MpAlNaGSouw0SlKL67hF4iv6dxY
            @Override // java.lang.Runnable
            public final void run() {
                DelayWatcher.this.lambda$startHandleModifyThread$0$DelayWatcher(currentPath, event);
            }
        });
    }

    public /* synthetic */ void lambda$startHandleModifyThread$0$DelayWatcher(Path currentPath, WatchEvent event) {
        ThreadUtil.sleep(this.delay);
        this.eventSet.remove(Paths.get(currentPath.toString(), event.context().toString()));
        this.watcher.onModify(event, currentPath);
    }
}
