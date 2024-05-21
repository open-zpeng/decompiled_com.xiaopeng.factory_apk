package io.sentry.protocol;

import io.sentry.SentryBaseEvent;
import io.sentry.SentryTracer;
import io.sentry.Span;
import io.sentry.SpanContext;
import io.sentry.SpanStatus;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryTransaction extends SentryBaseEvent {
    @NotNull
    private final Map<String, MeasurementValue> measurements;
    @NotNull
    private final List<SentrySpan> spans;
    @NotNull
    private final Date startTimestamp;
    @Nullable
    private Date timestamp;
    @Nullable
    private String transaction;
    @NotNull
    private final String type;

    public SentryTransaction(@NotNull SentryTracer sentryTracer) {
        super(sentryTracer.getEventId());
        this.spans = new ArrayList();
        this.type = "transaction";
        this.measurements = new HashMap();
        Objects.requireNonNull(sentryTracer, "sentryTracer is required");
        this.startTimestamp = sentryTracer.getStartTimestamp();
        this.timestamp = sentryTracer.getTimestamp();
        this.transaction = sentryTracer.getName();
        for (Span span : sentryTracer.getChildren()) {
            if (Boolean.TRUE.equals(span.isSampled())) {
                this.spans.add(new SentrySpan(span));
            }
        }
        Contexts contexts = getContexts();
        for (Map.Entry<String, Object> entry : sentryTracer.getContexts().entrySet()) {
            contexts.put(entry.getKey(), entry.getValue());
        }
        setRequest(sentryTracer.getRequest());
        SpanContext tracerContext = sentryTracer.getSpanContext();
        contexts.setTrace(new SpanContext(tracerContext.getTraceId(), tracerContext.getSpanId(), tracerContext.getParentSpanId(), tracerContext.getOperation(), tracerContext.getDescription(), tracerContext.getSampled(), tracerContext.getStatus()));
        for (Map.Entry<String, String> tag : tracerContext.getTags().entrySet()) {
            setTag(tag.getKey(), tag.getValue());
        }
        Map<String, Object> data = sentryTracer.getData();
        if (data != null) {
            for (Map.Entry<String, Object> tag2 : data.entrySet()) {
                setExtra(tag2.getKey(), tag2.getValue());
            }
        }
    }

    @NotNull
    public List<SentrySpan> getSpans() {
        return this.spans;
    }

    public boolean isFinished() {
        return this.timestamp != null;
    }

    @Nullable
    public String getTransaction() {
        return this.transaction;
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
    public String getType() {
        return "transaction";
    }

    @Nullable
    public SpanStatus getStatus() {
        SpanContext trace = getContexts().getTrace();
        if (trace != null) {
            return trace.getStatus();
        }
        return null;
    }

    public boolean isSampled() {
        SpanContext trace = getContexts().getTrace();
        return trace != null && Boolean.TRUE.equals(trace.getSampled());
    }

    @NotNull
    public Map<String, MeasurementValue> getMeasurements() {
        return this.measurements;
    }
}
