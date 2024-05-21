package io.sentry.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class InvalidSentryTraceHeaderException extends Exception {
    private static final long serialVersionUID = 1;
    @NotNull
    private final String sentryTraceHeader;

    public InvalidSentryTraceHeaderException(@NotNull String sentryTraceHeader) {
        this(sentryTraceHeader, null);
    }

    public InvalidSentryTraceHeaderException(@NotNull String sentryTraceHeader, @Nullable Throwable cause) {
        super("sentry-trace header does not conform to expected format: " + sentryTraceHeader, cause);
        this.sentryTraceHeader = sentryTraceHeader;
    }

    @NotNull
    public String getSentryTraceHeader() {
        return this.sentryTraceHeader;
    }
}
