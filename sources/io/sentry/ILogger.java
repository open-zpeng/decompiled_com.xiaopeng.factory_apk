package io.sentry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface ILogger {
    boolean isEnabled(@Nullable SentryLevel sentryLevel);

    void log(@NotNull SentryLevel sentryLevel, @NotNull String str, @Nullable Throwable th);

    void log(@NotNull SentryLevel sentryLevel, @NotNull String str, @Nullable Object... objArr);

    void log(@NotNull SentryLevel sentryLevel, @Nullable Throwable th, @NotNull String str, @Nullable Object... objArr);
}
