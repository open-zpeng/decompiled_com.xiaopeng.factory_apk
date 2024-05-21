package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class TransactionContext extends SpanContext {
    @NotNull
    private final String name;
    @Nullable
    private Boolean parentSampled;

    @NotNull
    public static TransactionContext fromSentryTrace(@NotNull String name, @NotNull String operation, @NotNull SentryTraceHeader sentryTrace) {
        return new TransactionContext(name, operation, sentryTrace.getTraceId(), new SpanId(), sentryTrace.getSpanId(), sentryTrace.isSampled());
    }

    public TransactionContext(@NotNull String name, @NotNull String operation) {
        super(operation);
        this.name = (String) Objects.requireNonNull(name, "name is required");
        this.parentSampled = null;
    }

    public TransactionContext(@NotNull String name, @NotNull String operation, @Nullable Boolean sampled) {
        super(operation);
        this.name = (String) Objects.requireNonNull(name, "name is required");
        setSampled(sampled);
    }

    private TransactionContext(@NotNull String name, @NotNull String operation, @NotNull SentryId traceId, @NotNull SpanId spanId, @Nullable SpanId parentSpanId, @Nullable Boolean parentSampled) {
        super(traceId, spanId, operation, parentSpanId, null);
        this.name = (String) Objects.requireNonNull(name, "name is required");
        this.parentSampled = parentSampled;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public Boolean getParentSampled() {
        return this.parentSampled;
    }

    public void setParentSampled(@Nullable Boolean parentSampled) {
        this.parentSampled = parentSampled;
    }
}
