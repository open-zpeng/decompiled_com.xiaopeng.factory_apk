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
public interface IHub {
    void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object obj);

    void bindClient(@NotNull ISentryClient iSentryClient);

    @NotNull
    SentryId captureEnvelope(@NotNull SentryEnvelope sentryEnvelope, @Nullable Object obj);

    @NotNull
    SentryId captureEvent(@NotNull SentryEvent sentryEvent, @Nullable Object obj);

    @NotNull
    SentryId captureException(@NotNull Throwable th, @Nullable Object obj);

    @NotNull
    SentryId captureMessage(@NotNull String str, @NotNull SentryLevel sentryLevel);

    @ApiStatus.Internal
    @NotNull
    SentryId captureTransaction(@NotNull SentryTransaction sentryTransaction, @Nullable TraceState traceState, @Nullable Object obj);

    void captureUserFeedback(@NotNull UserFeedback userFeedback);

    void clearBreadcrumbs();

    @NotNull
    IHub clone();

    void close();

    void configureScope(@NotNull ScopeCallback scopeCallback);

    void endSession();

    void flush(long j);

    @NotNull
    SentryId getLastEventId();

    @NotNull
    SentryOptions getOptions();

    @Nullable
    ISpan getSpan();

    @Nullable
    Boolean isCrashedLastRun();

    boolean isEnabled();

    void popScope();

    void pushScope();

    void removeExtra(@NotNull String str);

    void removeTag(@NotNull String str);

    void setExtra(@NotNull String str, @NotNull String str2);

    void setFingerprint(@NotNull List<String> list);

    void setLevel(@Nullable SentryLevel sentryLevel);

    @ApiStatus.Internal
    void setSpanContext(@NotNull Throwable th, @NotNull ISpan iSpan, @NotNull String str);

    void setTag(@NotNull String str, @NotNull String str2);

    void setTransaction(@Nullable String str);

    void setUser(@Nullable User user);

    void startSession();

    @NotNull
    ITransaction startTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean z);

    @ApiStatus.Internal
    @NotNull
    ITransaction startTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean z, @Nullable Date date);

    @ApiStatus.Internal
    @NotNull
    ITransaction startTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean z, @Nullable Date date, boolean z2, @Nullable TransactionFinishedCallback transactionFinishedCallback);

    @Nullable
    SentryTraceHeader traceHeaders();

    void withScope(@NotNull ScopeCallback scopeCallback);

    @NotNull
    default SentryId captureEvent(@NotNull SentryEvent event) {
        return captureEvent(event, null);
    }

    @NotNull
    default SentryId captureMessage(@NotNull String message) {
        return captureMessage(message, SentryLevel.INFO);
    }

    @NotNull
    default SentryId captureEnvelope(@NotNull SentryEnvelope envelope) {
        return captureEnvelope(envelope, null);
    }

    @NotNull
    default SentryId captureException(@NotNull Throwable throwable) {
        return captureException(throwable, null);
    }

    default void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        addBreadcrumb(breadcrumb, (Object) null);
    }

    default void addBreadcrumb(@NotNull String message) {
        addBreadcrumb(new Breadcrumb(message));
    }

    default void addBreadcrumb(@NotNull String message, @NotNull String category) {
        Breadcrumb breadcrumb = new Breadcrumb(message);
        breadcrumb.setCategory(category);
        addBreadcrumb(breadcrumb);
    }

    @ApiStatus.Internal
    @NotNull
    default SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        return captureTransaction(transaction, null, hint);
    }

    @ApiStatus.Internal
    @NotNull
    default SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState) {
        return captureTransaction(transaction, traceState, null);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull TransactionContext transactionContexts) {
        return startTransaction(transactionContexts, false);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull TransactionContext transactionContexts, boolean bindToScope) {
        return startTransaction(transactionContexts, (CustomSamplingContext) null, bindToScope);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull String name, @NotNull String operation, @Nullable CustomSamplingContext customSamplingContext) {
        return startTransaction(name, operation, customSamplingContext, false);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull String name, @NotNull String operation, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return startTransaction(new TransactionContext(name, operation), customSamplingContext, bindToScope);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext) {
        return startTransaction(transactionContexts, customSamplingContext, false);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull String name, @NotNull String operation) {
        return startTransaction(name, operation, (CustomSamplingContext) null);
    }

    @ApiStatus.Internal
    @NotNull
    default ITransaction startTransaction(@NotNull String name, @NotNull String operation, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        return startTransaction(new TransactionContext(name, operation), null, false, startTimestamp, waitForChildren, transactionFinishedCallback);
    }

    @NotNull
    default ITransaction startTransaction(@NotNull String name, @NotNull String operation, boolean bindToScope) {
        return startTransaction(name, operation, (CustomSamplingContext) null, bindToScope);
    }
}
