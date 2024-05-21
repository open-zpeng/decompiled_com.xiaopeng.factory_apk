package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.util.CollectionUtils;
import io.sentry.util.Objects;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public class SpanContext {
    public static final String TYPE = "trace";
    @Nullable
    protected String description;
    @NotNull
    protected String op;
    @Nullable
    private final SpanId parentSpanId;
    @Nullable
    private transient Boolean sampled;
    @NotNull
    private final SpanId spanId;
    @Nullable
    protected SpanStatus status;
    @NotNull
    protected Map<String, String> tags;
    @NotNull
    private final SentryId traceId;

    public SpanContext(@NotNull String operation, @Nullable Boolean sampled) {
        this(new SentryId(), new SpanId(), operation, null, sampled);
    }

    public SpanContext(@NotNull String operation) {
        this(new SentryId(), new SpanId(), operation, null, null);
    }

    public SpanContext(@NotNull SentryId traceId, @NotNull SpanId spanId, @NotNull String operation, @Nullable SpanId parentSpanId, @Nullable Boolean sampled) {
        this(traceId, spanId, parentSpanId, operation, null, sampled, null);
    }

    @ApiStatus.Internal
    public SpanContext(@NotNull SentryId traceId, @NotNull SpanId spanId, @Nullable SpanId parentSpanId, @NotNull String operation, @Nullable String description, @Nullable Boolean sampled, @Nullable SpanStatus status) {
        this.tags = new ConcurrentHashMap();
        this.traceId = (SentryId) Objects.requireNonNull(traceId, "traceId is required");
        this.spanId = (SpanId) Objects.requireNonNull(spanId, "spanId is required");
        this.op = (String) Objects.requireNonNull(operation, "operation is required");
        this.parentSpanId = parentSpanId;
        this.sampled = sampled;
        this.description = description;
        this.status = status;
    }

    public SpanContext(@NotNull SpanContext spanContext) {
        this.tags = new ConcurrentHashMap();
        this.traceId = spanContext.traceId;
        this.spanId = spanContext.spanId;
        this.parentSpanId = spanContext.parentSpanId;
        this.sampled = spanContext.sampled;
        this.op = spanContext.op;
        this.description = spanContext.description;
        this.status = spanContext.status;
        Map<String, String> copiedTags = CollectionUtils.newConcurrentHashMap(spanContext.tags);
        if (copiedTags != null) {
            this.tags = copiedTags;
        }
    }

    public void setOperation(@NotNull String operation) {
        this.op = (String) Objects.requireNonNull(operation, "operation is required");
    }

    public void setTag(@NotNull String name, @NotNull String value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        this.tags.put(name, value);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public void setStatus(@Nullable SpanStatus status) {
        this.status = status;
    }

    @NotNull
    public SentryId getTraceId() {
        return this.traceId;
    }

    @NotNull
    public SpanId getSpanId() {
        return this.spanId;
    }

    @TestOnly
    @Nullable
    public SpanId getParentSpanId() {
        return this.parentSpanId;
    }

    @NotNull
    public String getOperation() {
        return this.op;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    @Nullable
    public SpanStatus getStatus() {
        return this.status;
    }

    @NotNull
    public Map<String, String> getTags() {
        return this.tags;
    }

    @Nullable
    public Boolean getSampled() {
        return this.sampled;
    }

    @ApiStatus.Internal
    public void setSampled(@Nullable Boolean sampled) {
        this.sampled = sampled;
    }
}
