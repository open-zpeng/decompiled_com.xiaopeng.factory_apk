package cn.hutool.core.io.watch;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class WatchServer extends Thread implements Closeable, Serializable {
    private static final long serialVersionUID = 1;
    protected WatchEvent.Kind<?>[] events;
    protected boolean isClosed;
    private WatchEvent.Modifier[] modifiers;
    private final Map<WatchKey, Path> watchKeyPathMap = new HashMap();
    private WatchService watchService;

    public void init() throws WatchException {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            this.isClosed = false;
        } catch (IOException e) {
            throw new WatchException(e);
        }
    }

    public void setModifiers(WatchEvent.Modifier[] modifiers) {
        this.modifiers = modifiers;
    }

    public void registerPath(Path path, int maxDepth) {
        WatchKey key;
        WatchEvent.Kind<?>[] kinds = (WatchEvent.Kind[]) ArrayUtil.defaultIfEmpty(this.events, WatchKind.ALL);
        try {
            if (ArrayUtil.isEmpty((Object[]) this.modifiers)) {
                key = path.register(this.watchService, kinds);
            } else {
                key = path.register(this.watchService, kinds, this.modifiers);
            }
            this.watchKeyPathMap.put(key, path);
            if (maxDepth > 1) {
                Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() { // from class: cn.hutool.core.io.watch.WatchServer.1
                    @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        WatchServer.this.registerPath(dir, 0);
                        return super.postVisitDirectory((AnonymousClass1) dir, exc);
                    }
                });
            }
        } catch (IOException e) {
            if (!(e instanceof AccessDeniedException)) {
                throw new WatchException(e);
            }
        }
    }

    public void watch(WatchAction action, Filter<WatchEvent<?>> watchFilter) {
        try {
            WatchKey wk = this.watchService.take();
            Path currentPath = this.watchKeyPathMap.get(wk);
            for (WatchEvent<?> event : wk.pollEvents()) {
                if (watchFilter == null || watchFilter.accept(event)) {
                    action.doAction(event, currentPath);
                }
            }
            wk.reset();
        } catch (InterruptedException | ClosedWatchServiceException e) {
            close();
        }
    }

    public void watch(final Watcher watcher, Filter<WatchEvent<?>> watchFilter) {
        watch(new WatchAction() { // from class: cn.hutool.core.io.watch.-$$Lambda$WatchServer$QsgBIBzItEio9eMRiJlQgONf7Eo
            @Override // cn.hutool.core.io.watch.WatchAction
            public final void doAction(WatchEvent watchEvent, Path path) {
                WatchServer.lambda$watch$0(Watcher.this, watchEvent, path);
            }
        }, watchFilter);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$watch$0(Watcher watcher, WatchEvent event, Path currentPath) {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind == WatchKind.CREATE.getValue()) {
            watcher.onCreate(event, currentPath);
        } else if (kind == WatchKind.MODIFY.getValue()) {
            watcher.onModify(event, currentPath);
        } else if (kind == WatchKind.DELETE.getValue()) {
            watcher.onDelete(event, currentPath);
        } else if (kind == WatchKind.OVERFLOW.getValue()) {
            watcher.onOverflow(event, currentPath);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.isClosed = true;
        IoUtil.close((Closeable) this.watchService);
    }
}
