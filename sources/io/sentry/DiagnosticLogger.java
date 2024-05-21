package io.sentry;

import io.sentry.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class DiagnosticLogger implements ILogger {
    @Nullable
    private final ILogger logger;
    @NotNull
    private final SentryOptions options;

    public DiagnosticLogger(@NotNull SentryOptions options, @Nullable ILogger logger) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required.");
        this.logger = logger;
    }

    @Override // io.sentry.ILogger
    public boolean isEnabled(@Nullable SentryLevel level) {
        SentryLevel diagLevel = this.options.getDiagnosticLevel();
        return level != null && this.options.isDebug() && level.ordinal() >= diagLevel.ordinal();
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
        if (this.logger != null && isEnabled(level)) {
            this.logger.log(level, message, args);
        }
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
        if (this.logger != null && isEnabled(level)) {
            this.logger.log(level, message, throwable);
        }
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
        if (this.logger != null && isEnabled(level)) {
            this.logger.log(level, throwable, message, args);
        }
    }

    @TestOnly
    @Nullable
    public ILogger getLogger() {
        return this.logger;
    }
}
