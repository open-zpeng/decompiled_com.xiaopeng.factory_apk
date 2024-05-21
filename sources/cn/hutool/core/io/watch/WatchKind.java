package cn.hutool.core.io.watch;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
/* loaded from: classes.dex */
public enum WatchKind {
    OVERFLOW(StandardWatchEventKinds.OVERFLOW),
    MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),
    CREATE(StandardWatchEventKinds.ENTRY_CREATE),
    DELETE(StandardWatchEventKinds.ENTRY_DELETE);
    
    private final WatchEvent.Kind<?> value;
    public static final WatchEvent.Kind<?>[] ALL = {OVERFLOW.getValue(), MODIFY.getValue(), CREATE.getValue(), DELETE.getValue()};

    WatchKind(WatchEvent.Kind kind) {
        this.value = kind;
    }

    public WatchEvent.Kind<?> getValue() {
        return this.value;
    }
}
