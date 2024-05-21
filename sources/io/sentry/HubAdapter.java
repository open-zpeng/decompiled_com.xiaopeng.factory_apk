package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class HubAdapter implements IHub {
    private static final HubAdapter INSTANCE = new HubAdapter();

    private HubAdapter() {
    }

    public static HubAdapter getInstance() {
        return INSTANCE;
    }

    @Override // io.sentry.IHub
    public boolean isEnabled() {
        return Sentry.isEnabled();
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        return Sentry.captureEvent(event, hint);
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        return Sentry.captureMessage(message, level);
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        return Sentry.getCurrentHub().captureEnvelope(envelope, hint);
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        return Sentry.captureException(throwable, hint);
    }

    @Override // io.sentry.IHub
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
        Sentry.captureUserFeedback(userFeedback);
    }

    @Override // io.sentry.IHub
    public void startSession() {
        Sentry.startSession();
    }

    @Override // io.sentry.IHub
    public void endSession() {
        Sentry.endSession();
    }

    @Override // io.sentry.IHub
    public void close() {
        Sentry.close();
    }

    @Override // io.sentry.IHub
    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        Sentry.addBreadcrumb(breadcrumb, hint);
    }

    @Override // io.sentry.IHub
    public void setLevel(@Nullable SentryLevel level) {
        Sentry.setLevel(level);
    }

    @Override // io.sentry.IHub
    public void setTransaction(@Nullable String transaction) {
        Sentry.setTransaction(transaction);
    }

    @Override // io.sentry.IHub
    public void setUser(@Nullable User user) {
        Sentry.setUser(user);
    }

    @Override // io.sentry.IHub
    public void setFingerprint(@NotNull List<String> fingerprint) {
        Sentry.setFingerprint(fingerprint);
    }

    @Override // io.sentry.IHub
    public void clearBreadcrumbs() {
        Sentry.clearBreadcrumbs();
    }

    @Override // io.sentry.IHub
    public void setTag(@NotNull String key, @NotNull String value) {
        Sentry.setTag(key, value);
    }

    @Override // io.sentry.IHub
    public void removeTag(@NotNull String key) {
        Sentry.removeTag(key);
    }

    @Override // io.sentry.IHub
    public void setExtra(@NotNull String key, @NotNull String value) {
        Sentry.setExtra(key, value);
    }

    @Override // io.sentry.IHub
    public void removeExtra(@NotNull String key) {
        Sentry.removeExtra(key);
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId getLastEventId() {
        return Sentry.getLastEventId();
    }

    @Override // io.sentry.IHub
    public void pushScope() {
        Sentry.pushScope();
    }

    @Override // io.sentry.IHub
    public void popScope() {
        Sentry.popScope();
    }

    @Override // io.sentry.IHub
    public void withScope(@NotNull ScopeCallback callback) {
        Sentry.withScope(callback);
    }

    @Override // io.sentry.IHub
    public void configureScope(@NotNull ScopeCallback callback) {
        Sentry.configureScope(callback);
    }

    @Override // io.sentry.IHub
    public void bindClient(@NotNull ISentryClient client) {
        Sentry.bindClient(client);
    }

    @Override // io.sentry.IHub
    public void flush(long timeoutMillis) {
        Sentry.flush(timeoutMillis);
    }

    @Override // io.sentry.IHub
    @NotNull
    /* renamed from: clone */
    public IHub m124clone() {
        return Sentry.getCurrentHub().clone();
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Object hint) {
        return Sentry.getCurrentHub().captureTransaction(transaction, traceState, hint);
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts) {
        return Sentry.startTransaction(transactionContexts);
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return Sentry.startTransaction(transactionContexts, customSamplingContext, bindToScope);
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp) {
        return Sentry.startTransaction(transactionContexts, customSamplingContext, bindToScope, startTimestamp);
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        return Sentry.startTransaction(transactionContexts, customSamplingContext, bindToScope, startTimestamp, waitForChildren, transactionFinishedCallback);
    }

    @Override // io.sentry.IHub
    @Nullable
    public SentryTraceHeader traceHeaders() {
        return Sentry.traceHeaders();
    }

    @Override // io.sentry.IHub
    public void setSpanContext(@NotNull Throwable throwable, @NotNull ISpan span, @NotNull String transactionName) {
        Sentry.getCurrentHub().setSpanContext(throwable, span, transactionName);
    }

    @Override // io.sentry.IHub
    @Nullable
    public ISpan getSpan() {
        return Sentry.getCurrentHub().getSpan();
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryOptions getOptions() {
        return Sentry.getCurrentHub().getOptions();
    }

    @Override // io.sentry.IHub
    @Nullable
    public Boolean isCrashedLastRun() {
        return Sentry.isCrashedLastRun();
    }
}
