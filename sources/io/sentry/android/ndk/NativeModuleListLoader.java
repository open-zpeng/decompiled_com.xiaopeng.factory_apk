package io.sentry.android.ndk;

import io.sentry.protocol.DebugImage;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class NativeModuleListLoader {
    public static native void nativeClearModuleList();

    public static native DebugImage[] nativeLoadModuleList();

    @Nullable
    public DebugImage[] loadModuleList() {
        return nativeLoadModuleList();
    }

    public void clearModuleList() {
        nativeClearModuleList();
    }
}
