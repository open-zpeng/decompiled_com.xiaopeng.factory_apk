package io.sentry;

import io.sentry.Scope;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Request;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryTracer implements ITransaction {
    @NotNull
    private final List<Span> children;
    @NotNull
    private final Contexts contexts;
    @NotNull
    private final SentryId eventId;
    @NotNull
    private FinishStatus finishStatus;
    @NotNull
    private final IHub hub;
    @NotNull
    private String name;
    @Nullable
    private Request request;
    @NotNull
    private final Span root;
    @Nullable
    private TraceState traceState;
    @Nullable
    private final TransactionFinishedCallback transactionFinishedCallback;
    private final boolean waitForChildren;

    public SentryTracer(@NotNull TransactionContext context, @NotNull IHub hub) {
        this(context, hub, null);
    }

    public SentryTracer(@NotNull TransactionContext context, @NotNull IHub hub, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        this(context, hub, null, waitForChildren, transactionFinishedCallback);
    }

    SentryTracer(@NotNull TransactionContext context, @NotNull IHub hub, @Nullable Date startTimestamp) {
        this(context, hub, startTimestamp, false, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryTracer(@NotNull TransactionContext context, @NotNull IHub hub, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        this.eventId = new SentryId();
        this.children = new CopyOnWriteArrayList();
        this.contexts = new Contexts();
        this.finishStatus = FinishStatus.NOT_FINISHED;
        Objects.requireNonNull(context, "context is required");
        Objects.requireNonNull(hub, "hub is required");
        this.root = new Span(context, this, hub, startTimestamp);
        this.name = context.getName();
        this.hub = hub;
        this.waitForChildren = waitForChildren;
        this.transactionFinishedCallback = transactionFinishedCallback;
    }

    @NotNull
    public List<Span> getChildren() {
        return this.children;
    }

    @NotNull
    public Date getStartTimestamp() {
        return this.root.getStartTimestamp();
    }

    @Nullable
    public Date getTimestamp() {
        return this.root.getTimestamp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public ISpan startChild(@NotNull SpanId parentSpanId, @NotNull String operation, @Nullable String description) {
        ISpan span = createChild(parentSpanId, operation);
        span.setDescription(description);
        return span;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public ISpan startChild(@NotNull SpanId parentSpanId, @NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        return createChild(parentSpanId, operation, description, timestamp);
    }

    @NotNull
    private ISpan createChild(@NotNull SpanId parentSpanId, @NotNull String operation) {
        return createChild(parentSpanId, operation, null, null);
    }

    @NotNull
    private ISpan createChild(@NotNull SpanId parentSpanId, @NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        if (this.root.isFinished()) {
            return NoOpSpan.getInstance();
        }
        Objects.requireNonNull(parentSpanId, "parentSpanId is required");
        Objects.requireNonNull(operation, "operation is required");
        Span span = new Span(this.root.getTraceId(), parentSpanId, this, operation, this.hub, timestamp, new SpanFinishedCallback() { // from class: io.sentry.-$$Lambda$SentryTracer$f3vcQARxHmoGXt8ELM-5aZrdklg
            @Override // io.sentry.SpanFinishedCallback
            public final void execute(Span span2) {
                SentryTracer.this.lambda$createChild$0$SentryTracer(span2);
            }
        });
        span.setDescription(description);
        this.children.add(span);
        return span;
    }

    public /* synthetic */ void lambda$createChild$0$SentryTracer(Span __) {
        FinishStatus finishStatus = this.finishStatus;
        if (!finishStatus.isFinishing) {
            return;
        }
        finish(finishStatus.spanStatus);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation) {
        return startChild(operation, null);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        return createChild(operation, description, timestamp);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description) {
        return createChild(operation, description, null);
    }

    @NotNull
    private ISpan createChild(@NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        if (this.root.isFinished()) {
            return NoOpSpan.getInstance();
        }
        if (this.children.size() < this.hub.getOptions().getMaxSpans()) {
            return this.root.startChild(operation, description, timestamp);
        }
        this.hub.getOptions().getLogger().log(SentryLevel.WARNING, "Span operation: %s, description: %s dropped due to limit reached. Returning NoOpSpan.", operation, description);
        return NoOpSpan.getInstance();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SentryTraceHeader toSentryTrace() {
        return this.root.toSentryTrace();
    }

    @Override // io.sentry.ISpan
    public void finish() {
        finish(getStatus());
    }

    @Override // io.sentry.ISpan
    public void finish(@Nullable SpanStatus status) {
        this.finishStatus = FinishStatus.finishing(status);
        if (this.root.isFinished()) {
            return;
        }
        if (this.waitForChildren && !hasAllChildrenFinished()) {
            return;
        }
        this.root.finish(this.finishStatus.spanStatus);
        Date finishTimestamp = this.root.getTimestamp();
        if (finishTimestamp == null) {
            this.hub.getOptions().getLogger().log(SentryLevel.WARNING, "Root span - op: %s, description: %s - has no timestamp set, when finishing unfinished spans.", this.root.getOperation(), this.root.getDescription());
            finishTimestamp = DateUtils.getCurrentDateTime();
        }
        for (Span child : this.children) {
            if (!child.isFinished()) {
                child.finish(SpanStatus.DEADLINE_EXCEEDED, finishTimestamp);
            }
        }
        this.hub.configureScope(new ScopeCallback() { // from class: io.sentry.-$$Lambda$SentryTracer$fsRFT3NJh4Ac7U27cI9laNkiO_A
            @Override // io.sentry.ScopeCallback
            public final void run(Scope scope) {
                SentryTracer.this.lambda$finish$2$SentryTracer(scope);
            }
        });
        SentryTransaction transaction = new SentryTransaction(this);
        TransactionFinishedCallback transactionFinishedCallback = this.transactionFinishedCallback;
        if (transactionFinishedCallback != null) {
            transactionFinishedCallback.execute(this);
        }
        this.hub.captureTransaction(transaction, traceState());
    }

    public /* synthetic */ void lambda$finish$2$SentryTracer(final Scope scope) {
        scope.withTransaction(new Scope.IWithTransaction() { // from class: io.sentry.-$$Lambda$SentryTracer$9mivLnl9DMJbdRbldck18pIk3Oc
            @Override // io.sentry.Scope.IWithTransaction
            public final void accept(ITransaction iTransaction) {
                SentryTracer.this.lambda$finish$1$SentryTracer(scope, iTransaction);
            }
        });
    }

    public /* synthetic */ void lambda$finish$1$SentryTracer(Scope scope, ITransaction transaction) {
        if (transaction == this) {
            scope.clearTransaction();
        }
    }

    @Override // io.sentry.ISpan
    @Nullable
    public TraceState traceState() {
        TraceState traceState;
        if (this.hub.getOptions().isTraceSampling()) {
            synchronized (this) {
                if (this.traceState == null) {
                    final AtomicReference<User> userAtomicReference = new AtomicReference<>();
                    this.hub.configureScope(new ScopeCallback() { // from class: io.sentry.-$$Lambda$SentryTracer$_kmJUs3D51c4mCuNE8H2TCakJL8
                        @Override // io.sentry.ScopeCallback
                        public final void run(Scope scope) {
                            userAtomicReference.set(scope.getUser());
                        }
                    });
                    this.traceState = new TraceState(this, userAtomicReference.get(), this.hub.getOptions());
                }
                traceState = this.traceState;
            }
            return traceState;
        }
        return null;
    }

    @Override // io.sentry.ISpan
    @Nullable
    public TraceStateHeader toTraceStateHeader() {
        TraceState traceState = traceState();
        if (this.hub.getOptions().isTraceSampling() && traceState != null) {
            return TraceStateHeader.fromTraceState(traceState, this.hub.getOptions().getSerializer(), this.hub.getOptions().getLogger());
        }
        return null;
    }

    private boolean hasAllChildrenFinished() {
        List<Span> spans = new ArrayList<>(this.children);
        if (!spans.isEmpty()) {
            for (Span span : spans) {
                if (!span.isFinished()) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    @Override // io.sentry.ISpan
    public void setOperation(@NotNull String operation) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setOperation(operation);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public String getOperation() {
        return this.root.getOperation();
    }

    @Override // io.sentry.ISpan
    public void setDescription(@Nullable String description) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setDescription(description);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getDescription() {
        return this.root.getDescription();
    }

    @Override // io.sentry.ISpan
    public void setStatus(@Nullable SpanStatus status) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setStatus(status);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public SpanStatus getStatus() {
        return this.root.getStatus();
    }

    @Override // io.sentry.ISpan
    public void setThrowable(@Nullable Throwable throwable) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setThrowable(throwable);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Throwable getThrowable() {
        return this.root.getThrowable();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SpanContext getSpanContext() {
        return this.root.getSpanContext();
    }

    @Override // io.sentry.ISpan
    public void setTag(@NotNull String key, @NotNull String value) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setTag(key, value);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getTag(@NotNull String key) {
        return this.root.getTag(key);
    }

    @Override // io.sentry.ISpan
    public boolean isFinished() {
        return this.root.isFinished();
    }

    @Override // io.sentry.ISpan
    public void setData(@NotNull String key, @NotNull Object value) {
        if (this.root.isFinished()) {
            return;
        }
        this.root.setData(key, value);
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Object getData(@NotNull String key) {
        return this.root.getData(key);
    }

    @Nullable
    public Map<String, Object> getData() {
        return this.root.getData();
    }

    @Override // io.sentry.ITransaction
    @Nullable
    public Boolean isSampled() {
        return this.root.isSampled();
    }

    @Override // io.sentry.ITransaction
    public void setName(@NotNull String name) {
        if (this.root.isFinished()) {
            return;
        }
        this.name = name;
    }

    @Override // io.sentry.ITransaction
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override // io.sentry.ITransaction
    @ApiStatus.ScheduledForRemoval
    @Deprecated
    public void setRequest(@Nullable Request request) {
        if (this.root.isFinished()) {
            return;
        }
        this.request = request;
    }

    @Override // io.sentry.ITransaction
    @ApiStatus.ScheduledForRemoval
    @Deprecated
    @Nullable
    public Request getRequest() {
        return this.request;
    }

    @Override // io.sentry.ITransaction
    @ApiStatus.ScheduledForRemoval
    @Deprecated
    @NotNull
    public Contexts getContexts() {
        return this.contexts;
    }

    @Override // io.sentry.ITransaction
    @NotNull
    public List<Span> getSpans() {
        return this.children;
    }

    @Override // io.sentry.ITransaction
    @Nullable
    public Span getLatestActiveSpan() {
        List<Span> spans = new ArrayList<>(this.children);
        if (!spans.isEmpty()) {
            for (int i = spans.size() - 1; i >= 0; i--) {
                if (!spans.get(i).isFinished()) {
                    return spans.get(i);
                }
            }
            return null;
        }
        return null;
    }

    @Override // io.sentry.ITransaction
    @NotNull
    public SentryId getEventId() {
        return this.eventId;
    }

    @NotNull
    Span getRoot() {
        return this.root;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class FinishStatus {
        static final FinishStatus NOT_FINISHED = notFinished();
        private final boolean isFinishing;
        @Nullable
        private final SpanStatus spanStatus;

        @NotNull
        static FinishStatus finishing(@Nullable SpanStatus finishStatus) {
            return new FinishStatus(true, finishStatus);
        }

        @NotNull
        private static FinishStatus notFinished() {
            return new FinishStatus(false, null);
        }

        private FinishStatus(boolean isFinishing, @Nullable SpanStatus spanStatus) {
            this.isFinishing = isFinishing;
            this.spanStatus = spanStatus;
        }
    }
}
