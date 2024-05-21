package io.sentry;

import io.sentry.exception.InvalidSentryTraceHeaderException;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryTraceHeader {
    public static final String SENTRY_TRACE_HEADER = "sentry-trace";
    @Nullable
    private final Boolean sampled;
    @NotNull
    private final SpanId spanId;
    @NotNull
    private final SentryId traceId;

    public SentryTraceHeader(@NotNull SentryId traceId, @NotNull SpanId spanId, @Nullable Boolean sampled) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.sampled = sampled;
    }

    public SentryTraceHeader(@NotNull String value) throws InvalidSentryTraceHeaderException {
        String[] parts = value.split("-", -1);
        if (parts.length < 2) {
            throw new InvalidSentryTraceHeaderException(value);
        }
        if (parts.length == 3) {
            this.sampled = Boolean.valueOf("1".equals(parts[2]));
        } else {
            this.sampled = null;
        }
        try {
            this.traceId = new SentryId(parts[0]);
            this.spanId = new SpanId(parts[1]);
        } catch (Throwable e) {
            throw new InvalidSentryTraceHeaderException(value, e);
        }
    }

    @NotNull
    public String getName() {
        return SENTRY_TRACE_HEADER;
    }

    @NotNull
    public String getValue() {
        Boolean bool = this.sampled;
        if (bool != null) {
            Object[] objArr = new Object[3];
            objArr[0] = this.traceId;
            objArr[1] = this.spanId;
            objArr[2] = bool.booleanValue() ? "1" : "0";
            return String.format("%s-%s-%s", objArr);
        }
        return String.format("%s-%s", this.traceId, this.spanId);
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
    public Boolean isSampled() {
        return this.sampled;
    }
}
