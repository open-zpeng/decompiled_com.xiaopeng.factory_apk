package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.util.Objects;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class Span implements ISpan {
    @NotNull
    private final SpanContext context;
    @NotNull
    private final Map<String, Object> data;
    @NotNull
    private final AtomicBoolean finished;
    @NotNull
    private final IHub hub;
    @Nullable
    private final SpanFinishedCallback spanFinishedCallback;
    @NotNull
    private final Date startTimestamp;
    @Nullable
    private Throwable throwable;
    @Nullable
    private Date timestamp;
    @NotNull
    private final SentryTracer transaction;

    Span(@NotNull SentryId traceId, @Nullable SpanId parentSpanId, @NotNull SentryTracer transaction, @NotNull String operation, @NotNull IHub hub) {
        this(traceId, parentSpanId, transaction, operation, hub, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Span(@NotNull SentryId traceId, @Nullable SpanId parentSpanId, @NotNull SentryTracer transaction, @NotNull String operation, @NotNull IHub hub, @Nullable Date startTimestamp, @Nullable SpanFinishedCallback spanFinishedCallback) {
        this.finished = new AtomicBoolean(false);
        this.data = new ConcurrentHashMap();
        this.context = new SpanContext(traceId, new SpanId(), operation, parentSpanId, transaction.isSampled());
        this.transaction = (SentryTracer) Objects.requireNonNull(transaction, "transaction is required");
        this.startTimestamp = startTimestamp != null ? startTimestamp : DateUtils.getCurrentDateTime();
        this.hub = (IHub) Objects.requireNonNull(hub, "hub is required");
        this.spanFinishedCallback = spanFinishedCallback;
    }

    @VisibleForTesting
    public Span(@NotNull TransactionContext context, @NotNull SentryTracer sentryTracer, @NotNull IHub hub, @Nullable Date startTimestamp) {
        this.finished = new AtomicBoolean(false);
        this.data = new ConcurrentHashMap();
        this.context = (SpanContext) Objects.requireNonNull(context, "context is required");
        this.transaction = (SentryTracer) Objects.requireNonNull(sentryTracer, "sentryTracer is required");
        this.hub = (IHub) Objects.requireNonNull(hub, "hub is required");
        this.startTimestamp = startTimestamp != null ? startTimestamp : DateUtils.getCurrentDateTime();
        this.spanFinishedCallback = null;
    }

    @NotNull
    public Date getStartTimestamp() {
        return this.startTimestamp;
    }

    @Nullable
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation) {
        return startChild(operation, null);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        if (this.finished.get()) {
            return NoOpSpan.getInstance();
        }
        return this.transaction.startChild(this.context.getSpanId(), operation, description, timestamp);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description) {
        if (this.finished.get()) {
            return NoOpSpan.getInstance();
        }
        return this.transaction.startChild(this.context.getSpanId(), operation, description);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SentryTraceHeader toSentryTrace() {
        return new SentryTraceHeader(this.context.getTraceId(), this.context.getSpanId(), this.context.getSampled());
    }

    @Override // io.sentry.ISpan
    @Nullable
    public TraceState traceState() {
        return this.transaction.traceState();
    }

    @Override // io.sentry.ISpan
    @Nullable
    public TraceStateHeader toTraceStateHeader() {
        return this.transaction.toTraceStateHeader();
    }

    @Override // io.sentry.ISpan
    public void finish() {
        finish(this.context.getStatus());
    }

    @Override // io.sentry.ISpan
    public void finish(@Nullable SpanStatus status) {
        finish(status, DateUtils.getCurrentDateTime());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finish(@Nullable SpanStatus status, Date timestamp) {
        if (!this.finished.compareAndSet(false, true)) {
            return;
        }
        this.context.setStatus(status);
        this.timestamp = timestamp;
        Throwable th = this.throwable;
        if (th != null) {
            this.hub.setSpanContext(th, this, this.transaction.getName());
        }
        SpanFinishedCallback spanFinishedCallback = this.spanFinishedCallback;
        if (spanFinishedCallback != null) {
            spanFinishedCallback.execute(this);
        }
    }

    @Override // io.sentry.ISpan
    public void setOperation(@NotNull String operation) {
        if (this.finished.get()) {
            return;
        }
        this.context.setOperation(operation);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public String getOperation() {
        return this.context.getOperation();
    }

    @Override // io.sentry.ISpan
    public void setDescription(@Nullable String description) {
        if (this.finished.get()) {
            return;
        }
        this.context.setDescription(description);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getDescription() {
        return this.context.getDescription();
    }

    @Override // io.sentry.ISpan
    public void setStatus(@Nullable SpanStatus status) {
        if (this.finished.get()) {
            return;
        }
        this.context.setStatus(status);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public SpanStatus getStatus() {
        return this.context.getStatus();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SpanContext getSpanContext() {
        return this.context;
    }

    @Override // io.sentry.ISpan
    public void setTag(@NotNull String key, @NotNull String value) {
        if (this.finished.get()) {
            return;
        }
        this.context.setTag(key, value);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getTag(@NotNull String key) {
        return this.context.getTags().get(key);
    }

    @Override // io.sentry.ISpan
    public boolean isFinished() {
        return this.finished.get();
    }

    @NotNull
    public Map<String, Object> getData() {
        return this.data;
    }

    @Nullable
    public Boolean isSampled() {
        return this.context.getSampled();
    }

    @Override // io.sentry.ISpan
    public void setThrowable(@Nullable Throwable throwable) {
        if (this.finished.get()) {
            return;
        }
        this.throwable = throwable;
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Throwable getThrowable() {
        return this.throwable;
    }

    @NotNull
    public SentryId getTraceId() {
        return this.context.getTraceId();
    }

    @NotNull
    public SpanId getSpanId() {
        return this.context.getSpanId();
    }

    @Nullable
    public SpanId getParentSpanId() {
        return this.context.getParentSpanId();
    }

    public Map<String, String> getTags() {
        return this.context.getTags();
    }

    @Override // io.sentry.ISpan
    public void setData(@NotNull String key, @NotNull Object value) {
        if (this.finished.get()) {
            return;
        }
        this.data.put(key, value);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Object getData(@NotNull String key) {
        return this.data.get(key);
    }
}
