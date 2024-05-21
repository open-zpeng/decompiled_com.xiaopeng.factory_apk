package io.sentry;

import io.sentry.protocol.SentryId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class NoOpSpan implements ISpan {
    private static final NoOpSpan instance = new NoOpSpan();

    private NoOpSpan() {
    }

    public static NoOpSpan getInstance() {
        return instance;
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation) {
        return getInstance();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description, @Nullable Date timestamp) {
        return getInstance();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public ISpan startChild(@NotNull String operation, @Nullable String description) {
        return getInstance();
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SentryTraceHeader toSentryTrace() {
        return new SentryTraceHeader(SentryId.EMPTY_ID, SpanId.EMPTY_ID, false);
    }

    @Override // io.sentry.ISpan
    @NotNull
    public TraceState traceState() {
        return new TraceState(SentryId.EMPTY_ID, "");
    }

    @Override // io.sentry.ISpan
    @NotNull
    public TraceStateHeader toTraceStateHeader() {
        return new TraceStateHeader("");
    }

    @Override // io.sentry.ISpan
    public void finish() {
    }

    @Override // io.sentry.ISpan
    public void finish(@Nullable SpanStatus status) {
    }

    @Override // io.sentry.ISpan
    public void setOperation(@NotNull String operation) {
    }

    @Override // io.sentry.ISpan
    @NotNull
    public String getOperation() {
        return "";
    }

    @Override // io.sentry.ISpan
    public void setDescription(@Nullable String description) {
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getDescription() {
        return null;
    }

    @Override // io.sentry.ISpan
    public void setStatus(@Nullable SpanStatus status) {
    }

    @Override // io.sentry.ISpan
    @Nullable
    public SpanStatus getStatus() {
        return null;
    }

    @Override // io.sentry.ISpan
    public void setThrowable(@Nullable Throwable throwable) {
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Throwable getThrowable() {
        return null;
    }

    @Override // io.sentry.ISpan
    @NotNull
    public SpanContext getSpanContext() {
        return new SpanContext(SentryId.EMPTY_ID, SpanId.EMPTY_ID, "op", null, null);
    }

    @Override // io.sentry.ISpan
    public void setTag(@NotNull String key, @NotNull String value) {
    }

    @Override // io.sentry.ISpan
    @Nullable
    public String getTag(@NotNull String key) {
        return null;
    }

    @Override // io.sentry.ISpan
    public boolean isFinished() {
        return false;
    }

    @Override // io.sentry.ISpan
    public void setData(@NotNull String key, @NotNull Object value) {
    }

    @Override // io.sentry.ISpan
    @Nullable
    public Object getData(@NotNull String key) {
        return null;
    }
}
