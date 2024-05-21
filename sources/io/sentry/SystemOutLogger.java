package io.sentry;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SystemOutLogger implements ILogger {
    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
        System.out.println(String.format("%s: %s", level, String.format(message, args)));
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            log(level, message, new Object[0]);
        } else {
            System.out.println(String.format("%s: %s\n%s", level, String.format(message, throwable.toString()), captureStackTrace(throwable)));
        }
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
        if (throwable == null) {
            log(level, message, args);
        } else {
            System.out.println(String.format("%s: %s \n %s\n%s", level, String.format(message, args), throwable.toString(), captureStackTrace(throwable)));
        }
    }

    @Override // io.sentry.ILogger
    public boolean isEnabled(@Nullable SentryLevel level) {
        return true;
    }

    @NotNull
    private String captureStackTrace(@NotNull Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
