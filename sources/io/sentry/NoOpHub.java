package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class NoOpHub implements IHub {
    private static final NoOpHub instance = new NoOpHub();
    @NotNull
    private final SentryOptions emptyOptions = SentryOptions.empty();

    private NoOpHub() {
    }

    public static NoOpHub getInstance() {
        return instance;
    }

    @Override // io.sentry.IHub
    public boolean isEnabled() {
        return false;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
    }

    @Override // io.sentry.IHub
    public void startSession() {
    }

    @Override // io.sentry.IHub
    public void endSession() {
    }

    @Override // io.sentry.IHub
    public void close() {
    }

    @Override // io.sentry.IHub
    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
    }

    @Override // io.sentry.IHub
    public void setLevel(@Nullable SentryLevel level) {
    }

    @Override // io.sentry.IHub
    public void setTransaction(@Nullable String transaction) {
    }

    @Override // io.sentry.IHub
    public void setUser(@Nullable User user) {
    }

    @Override // io.sentry.IHub
    public void setFingerprint(@NotNull List<String> fingerprint) {
    }

    @Override // io.sentry.IHub
    public void clearBreadcrumbs() {
    }

    @Override // io.sentry.IHub
    public void setTag(@NotNull String key, @NotNull String value) {
    }

    @Override // io.sentry.IHub
    public void removeTag(@NotNull String key) {
    }

    @Override // io.sentry.IHub
    public void setExtra(@NotNull String key, @NotNull String value) {
    }

    @Override // io.sentry.IHub
    public void removeExtra(@NotNull String key) {
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId getLastEventId() {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    public void pushScope() {
    }

    @Override // io.sentry.IHub
    public void popScope() {
    }

    @Override // io.sentry.IHub
    public void withScope(@NotNull ScopeCallback callback) {
    }

    @Override // io.sentry.IHub
    public void configureScope(@NotNull ScopeCallback callback) {
    }

    @Override // io.sentry.IHub
    public void bindClient(@NotNull ISentryClient client) {
    }

    @Override // io.sentry.IHub
    public void flush(long timeoutMillis) {
    }

    @Override // io.sentry.IHub
    @NotNull
    /* renamed from: clone */
    public IHub m125clone() {
        return instance;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Object hint) {
        return SentryId.EMPTY_ID;
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts) {
        return NoOpTransaction.getInstance();
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return NoOpTransaction.getInstance();
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp) {
        return NoOpTransaction.getInstance();
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        return NoOpTransaction.getInstance();
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryTraceHeader traceHeaders() {
        return new SentryTraceHeader(SentryId.EMPTY_ID, SpanId.EMPTY_ID, true);
    }

    @Override // io.sentry.IHub
    public void setSpanContext(@NotNull Throwable throwable, @NotNull ISpan spanContext, @NotNull String transactionName) {
    }

    @Override // io.sentry.IHub
    @Nullable
    public ISpan getSpan() {
        return null;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryOptions getOptions() {
        return this.emptyOptions;
    }

    @Override // io.sentry.IHub
    @Nullable
    public Boolean isCrashedLastRun() {
        return null;
    }
}
