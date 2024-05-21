package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class NoOpSentryClient implements ISentryClient {
    private static final NoOpSentryClient instance = new NoOpSentryClient();

    private NoOpSentryClient() {
    }

    public static NoOpSentryClient getInstance() {
        return instance;
    }

    @Override // io.sentry.ISentryClient
    public boolean isEnabled() {
        return false;
    }

    @Override // io.sentry.ISentryClient
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Scope scope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.ISentryClient
    public void close() {
    }

    @Override // io.sentry.ISentryClient
    public void flush(long timeoutMillis) {
    }

    @Override // io.sentry.ISentryClient
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
    }

    @Override // io.sentry.ISentryClient
    public void captureSession(@NotNull Session session, @Nullable Object hint) {
    }

    @Override // io.sentry.ISentryClient
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.ISentryClient
    @NotNull
    public SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Scope scope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }
}
