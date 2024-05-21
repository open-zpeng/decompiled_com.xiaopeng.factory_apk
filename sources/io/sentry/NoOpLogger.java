package io.sentry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class NoOpLogger implements ILogger {
    private static final NoOpLogger instance = new NoOpLogger();

    public static NoOpLogger getInstance() {
        return instance;
    }

    private NoOpLogger() {
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
    }

    @Override // io.sentry.ILogger
    public boolean isEnabled(@Nullable SentryLevel level) {
        return false;
    }
}
