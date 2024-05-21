package io.sentry.android.ndk;

import io.sentry.android.core.SentryAndroidOptions;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryNdk {
    private static native void initSentryNative(@NotNull SentryAndroidOptions sentryAndroidOptions);

    private static native void shutdown();

    private SentryNdk() {
    }

    static {
        System.loadLibrary("log");
        System.loadLibrary("sentry");
        System.loadLibrary("sentry-android");
    }

    public static void init(@NotNull SentryAndroidOptions options) {
        SentryNdkUtil.addPackage(options.getSdkVersion());
        initSentryNative(options);
        options.addScopeObserver(new NdkScopeObserver(options));
        options.setDebugImagesLoader(new DebugImagesLoader(options, new NativeModuleListLoader()));
    }

    public static void close() {
        shutdown();
    }
}
