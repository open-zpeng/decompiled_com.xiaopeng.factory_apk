package io.sentry.util;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class LogUtils {
    public static void logIfNotFlushable(@NotNull ILogger logger, @Nullable Object hint) {
        SentryLevel sentryLevel = SentryLevel.DEBUG;
        Object[] objArr = new Object[1];
        objArr[0] = hint != null ? hint.getClass().getCanonicalName() : "Hint";
        logger.log(sentryLevel, "%s is not Flushable", objArr);
    }

    public static void logIfNotRetryable(@NotNull ILogger logger, @Nullable Object hint) {
        SentryLevel sentryLevel = SentryLevel.DEBUG;
        Object[] objArr = new Object[1];
        objArr[0] = hint != null ? hint.getClass().getCanonicalName() : "Hint";
        logger.log(sentryLevel, "%s is not Retryable", objArr);
    }
}
