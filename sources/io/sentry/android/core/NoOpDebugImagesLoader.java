package io.sentry.android.core;

import io.sentry.protocol.DebugImage;
import java.util.List;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class NoOpDebugImagesLoader implements IDebugImagesLoader {
    private static final NoOpDebugImagesLoader instance = new NoOpDebugImagesLoader();

    private NoOpDebugImagesLoader() {
    }

    public static NoOpDebugImagesLoader getInstance() {
        return instance;
    }

    @Override // io.sentry.android.core.IDebugImagesLoader
    @Nullable
    public List<DebugImage> loadDebugImages() {
        return null;
    }

    @Override // io.sentry.android.core.IDebugImagesLoader
    public void clearDebugImages() {
    }
}
