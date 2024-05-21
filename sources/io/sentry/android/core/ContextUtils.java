package io.sentry.android.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class ContextUtils {
    private ContextUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static PackageInfo getPackageInfo(@NotNull Context context, @NotNull ILogger logger) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Throwable e) {
            logger.log(SentryLevel.ERROR, "Error getting package info.", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public static String getVersionCode(@NotNull PackageInfo packageInfo) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Long.toString(packageInfo.getLongVersionCode());
        }
        return getVersionCodeDep(packageInfo);
    }

    @NotNull
    private static String getVersionCodeDep(@NotNull PackageInfo packageInfo) {
        return Integer.toString(packageInfo.versionCode);
    }
}
