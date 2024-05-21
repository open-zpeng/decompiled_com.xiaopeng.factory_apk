package cn.hutool.core.io.watch;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.attribute.FileAttribute;
/* loaded from: classes.dex */
public class WatchMonitor extends WatchServer {
    private static final long serialVersionUID = 1;
    private Path filePath;
    private int maxDepth;
    private Path path;
    private Watcher watcher;
    public static final WatchEvent.Kind<?> OVERFLOW = WatchKind.OVERFLOW.getValue();
    public static final WatchEvent.Kind<?> ENTRY_MODIFY = WatchKind.MODIFY.getValue();
    public static final WatchEvent.Kind<?> ENTRY_CREATE = WatchKind.CREATE.getValue();
    public static final WatchEvent.Kind<?> ENTRY_DELETE = WatchKind.DELETE.getValue();
    public static final WatchEvent.Kind<?>[] EVENTS_ALL = WatchKind.ALL;

    public static WatchMonitor create(URL url, WatchEvent.Kind<?>... events) {
        return create(url, 0, events);
    }

    public static WatchMonitor create(URL url, int maxDepth, WatchEvent.Kind<?>... events) {
        return create(URLUtil.toURI(url), maxDepth, events);
    }

    public static WatchMonitor create(URI uri, WatchEvent.Kind<?>... events) {
        return create(uri, 0, events);
    }

    public static WatchMonitor create(URI uri, int maxDepth, WatchEvent.Kind<?>... events) {
        return create(Paths.get(uri), maxDepth, events);
    }

    public static WatchMonitor create(File file, WatchEvent.Kind<?>... events) {
        return create(file, 0, events);
    }

    public static WatchMonitor create(File file, int maxDepth, WatchEvent.Kind<?>... events) {
        return create(file.toPath(), maxDepth, events);
    }

    public static WatchMonitor create(String path, WatchEvent.Kind<?>... events) {
        return create(path, 0, events);
    }

    public static WatchMonitor create(String path, int maxDepth, WatchEvent.Kind<?>... events) {
        return create(Paths.get(path, new String[0]), maxDepth, events);
    }

    public static WatchMonitor create(Path path, WatchEvent.Kind<?>... events) {
        return create(path, 0, events);
    }

    public static WatchMonitor create(Path path, int maxDepth, WatchEvent.Kind<?>... events) {
        return new WatchMonitor(path, maxDepth, events);
    }

    public static WatchMonitor createAll(URI uri, Watcher watcher) {
        return createAll(Paths.get(uri), watcher);
    }

    public static WatchMonitor createAll(URL url, Watcher watcher) {
        try {
            return createAll(Paths.get(url.toURI()), watcher);
        } catch (URISyntaxException e) {
            throw new WatchException(e);
        }
    }

    public static WatchMonitor createAll(File file, Watcher watcher) {
        return createAll(file.toPath(), watcher);
    }

    public static WatchMonitor createAll(String path, Watcher watcher) {
        return createAll(Paths.get(path, new String[0]), watcher);
    }

    public static WatchMonitor createAll(Path path, Watcher watcher) {
        WatchMonitor watchMonitor = create(path, EVENTS_ALL);
        watchMonitor.setWatcher(watcher);
        return watchMonitor;
    }

    public WatchMonitor(File file, WatchEvent.Kind<?>... events) {
        this(file.toPath(), events);
    }

    public WatchMonitor(String path, WatchEvent.Kind<?>... events) {
        this(Paths.get(path, new String[0]), events);
    }

    public WatchMonitor(Path path, WatchEvent.Kind<?>... events) {
        this(path, 0, events);
    }

    public WatchMonitor(Path path, int maxDepth, WatchEvent.Kind<?>... events) {
        this.path = path;
        this.maxDepth = maxDepth;
        this.events = events;
        init();
    }

    @Override // cn.hutool.core.io.watch.WatchServer
    public void init() throws WatchException {
        if (!Files.exists(this.path, LinkOption.NOFOLLOW_LINKS)) {
            Path lastPathEle = FileUtil.getLastPathEle(this.path);
            if (lastPathEle != null) {
                String lastPathEleStr = lastPathEle.toString();
                if (StrUtil.contains((CharSequence) lastPathEleStr, '.') && !StrUtil.endWithIgnoreCase(lastPathEleStr, ".d")) {
                    this.filePath = this.path;
                    this.path = this.filePath.getParent();
                }
            }
            try {
                Files.createDirectories(this.path, new FileAttribute[0]);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } else if (Files.isRegularFile(this.path, LinkOption.NOFOLLOW_LINKS)) {
            this.filePath = this.path;
            this.path = this.filePath.getParent();
        }
        super.init();
    }

    public WatchMonitor setWatcher(Watcher watcher) {
        this.watcher = watcher;
        return this;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        watch();
    }

    public void watch() {
        watch(this.watcher);
    }

    public void watch(Watcher watcher) throws WatchException {
        if (this.isClosed) {
            throw new WatchException("Watch Monitor is closed !");
        }
        registerPath();
        while (!this.isClosed) {
            doTakeAndWatch(watcher);
        }
    }

    public WatchMonitor setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
        return this;
    }

    private void doTakeAndWatch(Watcher watcher) {
        super.watch(watcher, new Filter() { // from class: cn.hutool.core.io.watch.-$$Lambda$WatchMonitor$aXOzxRPIcSguIHW77EMqTUdoHZs
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return WatchMonitor.this.lambda$doTakeAndWatch$0$WatchMonitor((WatchEvent) obj);
            }
        });
    }

    public /* synthetic */ boolean lambda$doTakeAndWatch$0$WatchMonitor(WatchEvent watchEvent) {
        Path path = this.filePath;
        return path == null || path.endsWith(watchEvent.context().toString());
    }

    private void registerPath() {
        registerPath(this.path, this.filePath != null ? 0 : this.maxDepth);
    }
}
