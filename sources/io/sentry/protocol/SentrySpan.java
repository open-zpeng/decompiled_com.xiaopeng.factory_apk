package io.sentry.protocol;

import io.sentry.Span;
import io.sentry.SpanId;
import io.sentry.SpanStatus;
import io.sentry.util.CollectionUtils;
import io.sentry.util.Objects;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentrySpan {
    @Nullable
    private final Map<String, Object> data;
    @Nullable
    private final String description;
    @NotNull
    private final String op;
    @Nullable
    private final SpanId parentSpanId;
    @NotNull
    private final SpanId spanId;
    @NotNull
    private final Date startTimestamp;
    @Nullable
    private final SpanStatus status;
    @NotNull
    private final Map<String, String> tags;
    @Nullable
    private final Date timestamp;
    @NotNull
    private final SentryId traceId;

    public SentrySpan(@NotNull Span span) {
        this(span, span.getData());
    }

    @ApiStatus.Internal
    public SentrySpan(@NotNull Span span, @Nullable Map<String, Object> data) {
        Objects.requireNonNull(span, "span is required");
        this.description = span.getDescription();
        this.op = span.getOperation();
        this.spanId = span.getSpanId();
        this.parentSpanId = span.getParentSpanId();
        this.traceId = span.getTraceId();
        this.status = span.getStatus();
        Map<String, String> tagsCopy = CollectionUtils.newConcurrentHashMap(span.getTags());
        this.tags = tagsCopy != null ? tagsCopy : new ConcurrentHashMap<>();
        this.timestamp = span.getTimestamp();
        this.startTimestamp = span.getStartTimestamp();
        this.data = data;
    }

    public boolean isFinished() {
        return this.timestamp != null;
    }

    @NotNull
    public Date getStartTimestamp() {
        return this.startTimestamp;
    }

    @Nullable
    public Date getTimestamp() {
        return this.timestamp;
    }

    @NotNull
    public SentryId getTraceId() {
        return this.traceId;
    }

    @NotNull
    public SpanId getSpanId() {
        return this.spanId;
    }

    @Nullable
    public SpanId getParentSpanId() {
        return this.parentSpanId;
    }

    @NotNull
    public String getOp() {
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
    public Map<String, Object> getData() {
        return this.data;
    }
}
