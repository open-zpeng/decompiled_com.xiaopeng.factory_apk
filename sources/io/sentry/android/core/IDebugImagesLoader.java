package io.sentry.android.core;

import io.sentry.protocol.DebugImage;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public interface IDebugImagesLoader {
    void clearDebugImages();

    @Nullable
    List<DebugImage> loadDebugImages();
}
