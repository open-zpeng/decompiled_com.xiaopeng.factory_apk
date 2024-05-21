package io.sentry;

import io.sentry.Scope;
import io.sentry.Stack;
import io.sentry.hints.SessionEndHint;
import io.sentry.hints.SessionStartHint;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import io.sentry.util.ExceptionUtils;
import io.sentry.util.Objects;
import io.sentry.util.Pair;
import java.io.Closeable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Hub implements IHub {
    private volatile boolean isEnabled;
    @NotNull
    private volatile SentryId lastEventId;
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final Stack stack;
    @NotNull
    private final Map<Throwable, Pair<ISpan, String>> throwableToSpan;
    @NotNull
    private final TracesSampler tracesSampler;

    public Hub(@NotNull SentryOptions options) {
        this(options, createRootStackItem(options));
    }

    private Hub(@NotNull SentryOptions options, @NotNull Stack stack) {
        this.throwableToSpan = Collections.synchronizedMap(new WeakHashMap());
        validateOptions(options);
        this.options = options;
        this.tracesSampler = new TracesSampler(options);
        this.stack = stack;
        this.lastEventId = SentryId.EMPTY_ID;
        this.isEnabled = true;
    }

    private Hub(@NotNull SentryOptions options, @NotNull Stack.StackItem rootStackItem) {
        this(options, new Stack(options.getLogger(), rootStackItem));
    }

    private static void validateOptions(@NotNull SentryOptions options) {
        Objects.requireNonNull(options, "SentryOptions is required.");
        if (options.getDsn() == null || options.getDsn().isEmpty()) {
            throw new IllegalArgumentException("Hub requires a DSN to be instantiated. Considering using the NoOpHub is no DSN is available.");
        }
    }

    private static Stack.StackItem createRootStackItem(@NotNull SentryOptions options) {
        validateOptions(options);
        Scope scope = new Scope(options);
        ISentryClient client = new SentryClient(options);
        return new Stack.StackItem(options, client, scope);
    }

    @Override // io.sentry.IHub
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureEvent' call is a no-op.", new Object[0]);
            return sentryId;
        } else if (event == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureEvent called with null parameter.", new Object[0]);
            return sentryId;
        } else {
            try {
                assignTraceContext(event);
                Stack.StackItem item = this.stack.peek();
                sentryId = item.getClient().captureEvent(event, item.getScope(), hint);
                this.lastEventId = sentryId;
                return sentryId;
            } catch (Throwable e) {
                ILogger logger = this.options.getLogger();
                SentryLevel sentryLevel = SentryLevel.ERROR;
                logger.log(sentryLevel, "Error while capturing event with id: " + event.getEventId(), e);
                return sentryId;
            }
        }
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureMessage' call is a no-op.", new Object[0]);
        } else if (message == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureMessage called with null parameter.", new Object[0]);
        } else {
            try {
                Stack.StackItem item = this.stack.peek();
                sentryId = item.getClient().captureMessage(message, level, item.getScope());
            } catch (Throwable e) {
                ILogger logger = this.options.getLogger();
                SentryLevel sentryLevel = SentryLevel.ERROR;
                logger.log(sentryLevel, "Error while capturing message: " + message, e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        Objects.requireNonNull(envelope, "SentryEnvelope is required.");
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureEnvelope' call is a no-op.", new Object[0]);
            return sentryId;
        }
        try {
            SentryId capturedEnvelopeId = this.stack.peek().getClient().captureEnvelope(envelope, hint);
            if (capturedEnvelopeId != null) {
                return capturedEnvelopeId;
            }
            return sentryId;
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error while capturing envelope.", e);
            return sentryId;
        }
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureException' call is a no-op.", new Object[0]);
        } else if (throwable == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "captureException called with null parameter.", new Object[0]);
        } else {
            try {
                Stack.StackItem item = this.stack.peek();
                SentryEvent event = new SentryEvent(throwable);
                assignTraceContext(event);
                sentryId = item.getClient().captureEvent(event, item.getScope(), hint);
            } catch (Throwable e) {
                ILogger logger = this.options.getLogger();
                SentryLevel sentryLevel = SentryLevel.ERROR;
                logger.log(sentryLevel, "Error while capturing exception: " + throwable.getMessage(), e);
            }
        }
        this.lastEventId = sentryId;
        return sentryId;
    }

    private void assignTraceContext(@NotNull SentryEvent event) {
        Pair<ISpan, String> pair;
        if (this.options.isTracingEnabled() && event.getThrowable() != null && (pair = this.throwableToSpan.get(ExceptionUtils.findRootCause(event.getThrowable()))) != null) {
            ISpan span = pair.getFirst();
            if (event.getContexts().getTrace() == null && span != null) {
                event.getContexts().setTrace(span.getSpanContext());
            }
            String transactionName = pair.getSecond();
            if (event.getTransaction() == null && transactionName != null) {
                event.setTransaction(transactionName);
            }
        }
    }

    @Override // io.sentry.IHub
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureUserFeedback' call is a no-op.", new Object[0]);
            return;
        }
        try {
            Stack.StackItem item = this.stack.peek();
            item.getClient().captureUserFeedback(userFeedback);
        } catch (Throwable e) {
            ILogger logger = this.options.getLogger();
            SentryLevel sentryLevel = SentryLevel.ERROR;
            logger.log(sentryLevel, "Error while capturing captureUserFeedback: " + userFeedback.toString(), e);
        }
    }

    @Override // io.sentry.IHub
    public void startSession() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'startSession' call is a no-op.", new Object[0]);
            return;
        }
        Stack.StackItem item = this.stack.peek();
        Scope.SessionPair pair = item.getScope().startSession();
        if (pair != null) {
            if (pair.getPrevious() != null) {
                item.getClient().captureSession(pair.getPrevious(), new SessionEndHint());
            }
            item.getClient().captureSession(pair.getCurrent(), new SessionStartHint());
            return;
        }
        this.options.getLogger().log(SentryLevel.WARNING, "Session could not be started.", new Object[0]);
    }

    @Override // io.sentry.IHub
    public void endSession() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'endSession' call is a no-op.", new Object[0]);
            return;
        }
        Stack.StackItem item = this.stack.peek();
        Session previousSession = item.getScope().endSession();
        if (previousSession != null) {
            item.getClient().captureSession(previousSession, new SessionEndHint());
        }
    }

    @Override // io.sentry.IHub
    public void close() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'close' call is a no-op.", new Object[0]);
            return;
        }
        try {
            for (Integration integration : this.options.getIntegrations()) {
                if (integration instanceof Closeable) {
                    ((Closeable) integration).close();
                }
            }
            this.options.getExecutorService().close(this.options.getShutdownTimeout());
            Stack.StackItem item = this.stack.peek();
            item.getClient().close();
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error while closing the Hub.", e);
        }
        this.isEnabled = false;
    }

    @Override // io.sentry.IHub
    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'addBreadcrumb' call is a no-op.", new Object[0]);
        } else if (breadcrumb == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "addBreadcrumb called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().addBreadcrumb(breadcrumb, hint);
        }
    }

    @Override // io.sentry.IHub
    public void setLevel(@Nullable SentryLevel level) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setLevel' call is a no-op.", new Object[0]);
        } else {
            this.stack.peek().getScope().setLevel(level);
        }
    }

    @Override // io.sentry.IHub
    public void setTransaction(@Nullable String transaction) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setTransaction' call is a no-op.", new Object[0]);
        } else if (transaction != null) {
            this.stack.peek().getScope().setTransaction(transaction);
        } else {
            this.options.getLogger().log(SentryLevel.WARNING, "Transaction cannot be null", new Object[0]);
        }
    }

    @Override // io.sentry.IHub
    public void setUser(@Nullable User user) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setUser' call is a no-op.", new Object[0]);
        } else {
            this.stack.peek().getScope().setUser(user);
        }
    }

    @Override // io.sentry.IHub
    public void setFingerprint(@NotNull List<String> fingerprint) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setFingerprint' call is a no-op.", new Object[0]);
        } else if (fingerprint == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setFingerprint called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().setFingerprint(fingerprint);
        }
    }

    @Override // io.sentry.IHub
    public void clearBreadcrumbs() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'clearBreadcrumbs' call is a no-op.", new Object[0]);
        } else {
            this.stack.peek().getScope().clearBreadcrumbs();
        }
    }

    @Override // io.sentry.IHub
    public void setTag(@NotNull String key, @NotNull String value) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setTag' call is a no-op.", new Object[0]);
        } else if (key == null || value == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setTag called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().setTag(key, value);
        }
    }

    @Override // io.sentry.IHub
    public void removeTag(@NotNull String key) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'removeTag' call is a no-op.", new Object[0]);
        } else if (key == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "removeTag called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().removeTag(key);
        }
    }

    @Override // io.sentry.IHub
    public void setExtra(@NotNull String key, @NotNull String value) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'setExtra' call is a no-op.", new Object[0]);
        } else if (key == null || value == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "setExtra called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().setExtra(key, value);
        }
    }

    @Override // io.sentry.IHub
    public void removeExtra(@NotNull String key) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'removeExtra' call is a no-op.", new Object[0]);
        } else if (key == null) {
            this.options.getLogger().log(SentryLevel.WARNING, "removeExtra called with null parameter.", new Object[0]);
        } else {
            this.stack.peek().getScope().removeExtra(key);
        }
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryId getLastEventId() {
        return this.lastEventId;
    }

    @Override // io.sentry.IHub
    public void pushScope() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'pushScope' call is a no-op.", new Object[0]);
            return;
        }
        Stack.StackItem item = this.stack.peek();
        Stack.StackItem newItem = new Stack.StackItem(this.options, item.getClient(), new Scope(item.getScope()));
        this.stack.push(newItem);
    }

    @Override // io.sentry.IHub
    @NotNull
    public SentryOptions getOptions() {
        return this.stack.peek().getOptions();
    }

    @Override // io.sentry.IHub
    @Nullable
    public Boolean isCrashedLastRun() {
        return SentryCrashLastRunState.getInstance().isCrashedLastRun(this.options.getCacheDirPath(), !this.options.isEnableAutoSessionTracking());
    }

    @Override // io.sentry.IHub
    public void popScope() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'popScope' call is a no-op.", new Object[0]);
        } else {
            this.stack.pop();
        }
    }

    @Override // io.sentry.IHub
    public void withScope(@NotNull ScopeCallback callback) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'withScope' call is a no-op.", new Object[0]);
            return;
        }
        pushScope();
        try {
            callback.run(this.stack.peek().getScope());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'withScope' callback.", e);
        }
        popScope();
    }

    @Override // io.sentry.IHub
    public void configureScope(@NotNull ScopeCallback callback) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'configureScope' call is a no-op.", new Object[0]);
            return;
        }
        try {
            callback.run(this.stack.peek().getScope());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'configureScope' callback.", e);
        }
    }

    @Override // io.sentry.IHub
    public void bindClient(@NotNull ISentryClient client) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'bindClient' call is a no-op.", new Object[0]);
            return;
        }
        Stack.StackItem item = this.stack.peek();
        if (client != null) {
            this.options.getLogger().log(SentryLevel.DEBUG, "New client bound to scope.", new Object[0]);
            item.setClient(client);
            return;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "NoOp client bound to scope.", new Object[0]);
        item.setClient(NoOpSentryClient.getInstance());
    }

    @Override // io.sentry.IHub
    public void flush(long timeoutMillis) {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'flush' call is a no-op.", new Object[0]);
            return;
        }
        try {
            this.stack.peek().getClient().flush(timeoutMillis);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error in the 'client.flush'.", e);
        }
    }

    @Override // io.sentry.IHub
    @NotNull
    /* renamed from: clone */
    public IHub m123clone() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Disabled Hub cloned.", new Object[0]);
        }
        return new Hub(this.options, new Stack(this.stack));
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Object hint) {
        Objects.requireNonNull(transaction, "transaction is required");
        SentryId sentryId = SentryId.EMPTY_ID;
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'captureTransaction' call is a no-op.", new Object[0]);
            return sentryId;
        } else if (!transaction.isFinished()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Transaction: %s is not finished and this 'captureTransaction' call is a no-op.", transaction.getEventId());
            return sentryId;
        } else if (!Boolean.TRUE.equals(Boolean.valueOf(transaction.isSampled()))) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Transaction %s was dropped due to sampling decision.", transaction.getEventId());
            return sentryId;
        } else {
            try {
                Stack.StackItem item = this.stack.peek();
                return item.getClient().captureTransaction(transaction, traceState, item.getScope(), hint);
            } catch (Throwable e) {
                ILogger logger = this.options.getLogger();
                SentryLevel sentryLevel = SentryLevel.ERROR;
                logger.log(sentryLevel, "Error while capturing transaction with id: " + transaction.getEventId(), e);
                return sentryId;
            }
        }
    }

    @Override // io.sentry.IHub
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return createTransaction(transactionContext, customSamplingContext, bindToScope, null, false, null);
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp) {
        return createTransaction(transactionContext, customSamplingContext, bindToScope, startTimestamp, false, null);
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    @NotNull
    public ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        return createTransaction(transactionContexts, customSamplingContext, bindToScope, startTimestamp, waitForChildren, transactionFinishedCallback);
    }

    @NotNull
    private ITransaction createTransaction(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        final ITransaction transaction;
        Objects.requireNonNull(transactionContext, "transactionContext is required");
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'startTransaction' returns a no-op.", new Object[0]);
            transaction = NoOpTransaction.getInstance();
        } else if (!this.options.isTracingEnabled()) {
            this.options.getLogger().log(SentryLevel.INFO, "Tracing is disabled and this 'startTransaction' returns a no-op.", new Object[0]);
            transaction = NoOpTransaction.getInstance();
        } else {
            SamplingContext samplingContext = new SamplingContext(transactionContext, customSamplingContext);
            boolean samplingDecision = this.tracesSampler.sample(samplingContext);
            transactionContext.setSampled(Boolean.valueOf(samplingDecision));
            transaction = new SentryTracer(transactionContext, this, startTimestamp, waitForChildren, transactionFinishedCallback);
        }
        if (bindToScope) {
            configureScope(new ScopeCallback() { // from class: io.sentry.-$$Lambda$Hub$SAG7H6UXRzrtIDg5IJMhf3ye3Yw
                @Override // io.sentry.ScopeCallback
                public final void run(Scope scope) {
                    scope.setTransaction(ITransaction.this);
                }
            });
        }
        return transaction;
    }

    @Override // io.sentry.IHub
    @Nullable
    public SentryTraceHeader traceHeaders() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'traceHeaders' call is a no-op.", new Object[0]);
            return null;
        }
        ISpan span = this.stack.peek().getScope().getSpan();
        if (span == null) {
            return null;
        }
        SentryTraceHeader traceHeader = span.toSentryTrace();
        return traceHeader;
    }

    @Override // io.sentry.IHub
    @Nullable
    public ISpan getSpan() {
        if (!isEnabled()) {
            this.options.getLogger().log(SentryLevel.WARNING, "Instance is disabled and this 'getSpan' call is a no-op.", new Object[0]);
            return null;
        }
        ISpan span = this.stack.peek().getScope().getSpan();
        return span;
    }

    @Override // io.sentry.IHub
    @ApiStatus.Internal
    public void setSpanContext(@NotNull Throwable throwable, @NotNull ISpan span, @NotNull String transactionName) {
        Objects.requireNonNull(throwable, "throwable is required");
        Objects.requireNonNull(span, "span is required");
        Objects.requireNonNull(transactionName, "transactionName is required");
        Throwable rootCause = ExceptionUtils.findRootCause(throwable);
        if (!this.throwableToSpan.containsKey(rootCause)) {
            this.throwableToSpan.put(rootCause, new Pair<>(span, transactionName));
        }
    }

    @Nullable
    SpanContext getSpanContext(@NotNull Throwable throwable) {
        ISpan span;
        Objects.requireNonNull(throwable, "throwable is required");
        Throwable rootCause = ExceptionUtils.findRootCause(throwable);
        Pair<ISpan, String> pair = this.throwableToSpan.get(rootCause);
        if (pair != null && (span = pair.getFirst()) != null) {
            return span.getSpanContext();
        }
        return null;
    }
}
