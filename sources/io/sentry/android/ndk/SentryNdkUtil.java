package io.sentry.android.ndk;

import io.sentry.protocol.SdkVersion;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class SentryNdkUtil {
    private SentryNdkUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addPackage(@Nullable SdkVersion sdkVersion) {
        if (sdkVersion == null) {
            return;
        }
        sdkVersion.addPackage("maven:io.sentry:sentry-android-ndk", "5.7.3");
    }
}
