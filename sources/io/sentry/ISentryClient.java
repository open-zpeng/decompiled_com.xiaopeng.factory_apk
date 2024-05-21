package io.sentry;

import io.sentry.protocol.Message;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface ISentryClient {
    @Nullable
    SentryId captureEnvelope(@NotNull SentryEnvelope sentryEnvelope, @Nullable Object obj);

    @NotNull
    SentryId captureEvent(@NotNull SentryEvent sentryEvent, @Nullable Scope scope, @Nullable Object obj);

    void captureSession(@NotNull Session session, @Nullable Object obj);

    @ApiStatus.Experimental
    @NotNull
    SentryId captureTransaction(@NotNull SentryTransaction sentryTransaction, @Nullable TraceState traceState, @Nullable Scope scope, @Nullable Object obj);

    void captureUserFeedback(@NotNull UserFeedback userFeedback);

    void close();

    void flush(long j);

    boolean isEnabled();

    @NotNull
    default SentryId captureEvent(@NotNull SentryEvent event) {
        return captureEvent(event, null, null);
    }

    @NotNull
    default SentryId captureEvent(@NotNull SentryEvent event, @Nullable Scope scope) {
        return captureEvent(event, scope, null);
    }

    @NotNull
    default SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        return captureEvent(event, null, hint);
    }

    @NotNull
    default SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level, @Nullable Scope scope) {
        SentryEvent event = new SentryEvent();
        Message sentryMessage = new Message();
        sentryMessage.setFormatted(message);
        event.setMessage(sentryMessage);
        event.setLevel(level);
        return captureEvent(event, scope);
    }

    @NotNull
    default SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        return captureMessage(message, level, null);
    }

    @NotNull
    default SentryId captureException(@NotNull Throwable throwable) {
        return captureException(throwable, null, null);
    }

    @NotNull
    default SentryId captureException(@NotNull Throwable throwable, @Nullable Scope scope, @Nullable Object hint) {
        SentryEvent event = new SentryEvent(throwable);
        return captureEvent(event, scope, hint);
    }

    @NotNull
    default SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        return captureException(throwable, null, hint);
    }

    @NotNull
    default SentryId captureException(@NotNull Throwable throwable, @Nullable Scope scope) {
        return captureException(throwable, scope, null);
    }

    default void captureSession(@NotNull Session session) {
        captureSession(session, null);
    }

    @Nullable
    default SentryId captureEnvelope(@NotNull SentryEnvelope envelope) {
        return captureEnvelope(envelope, null);
    }

    @NotNull
    default SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable Scope scope, @Nullable Object hint) {
        return captureTransaction(transaction, null, scope, hint);
    }

    @ApiStatus.Experimental
    @NotNull
    default SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState) {
        return captureTransaction(transaction, traceState, null, null);
    }

    @NotNull
    default SentryId captureTransaction(@NotNull SentryTransaction transaction) {
        return captureTransaction(transaction, null, null, null);
    }
}
