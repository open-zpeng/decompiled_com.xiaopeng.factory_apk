package io.sentry.android.ndk;

import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.IDebugImagesLoader;
import io.sentry.android.core.SentryAndroidOptions;
import io.sentry.protocol.DebugImage;
import io.sentry.util.Objects;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
final class DebugImagesLoader implements IDebugImagesLoader {
    @Nullable
    private static List<DebugImage> debugImages;
    @NotNull
    private static final Object debugImagesLock = new Object();
    @NotNull
    private final NativeModuleListLoader moduleListLoader;
    @NotNull
    private final SentryOptions options;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DebugImagesLoader(@NotNull SentryAndroidOptions options, @NotNull NativeModuleListLoader moduleListLoader) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryAndroidOptions is required.");
        this.moduleListLoader = (NativeModuleListLoader) Objects.requireNonNull(moduleListLoader, "The NativeModuleListLoader is required.");
    }

    @Override // io.sentry.android.core.IDebugImagesLoader
    @Nullable
    public List<DebugImage> loadDebugImages() {
        DebugImage[] debugImagesArr;
        synchronized (debugImagesLock) {
            if (debugImages == null && (debugImagesArr = this.moduleListLoader.loadModuleList()) != null) {
                debugImages = Arrays.asList(debugImagesArr);
                this.options.getLogger().log(SentryLevel.DEBUG, "Debug images loaded: %d", Integer.valueOf(debugImages.size()));
            }
        }
        return debugImages;
    }

    @Override // io.sentry.android.core.IDebugImagesLoader
    public void clearDebugImages() {
        synchronized (debugImagesLock) {
            try {
                this.moduleListLoader.clearModuleList();
                this.options.getLogger().log(SentryLevel.INFO, "Debug images cleared.", new Object[0]);
                debugImages = null;
            }
        }
    }

    @VisibleForTesting
    @Nullable
    List<DebugImage> getCachedDebugImages() {
        return debugImages;
    }
}
