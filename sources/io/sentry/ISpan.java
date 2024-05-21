package io.sentry;

import java.util.Date;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface ISpan {
    void finish();

    void finish(@Nullable SpanStatus spanStatus);

    @Nullable
    Object getData(@NotNull String str);

    @Nullable
    String getDescription();

    @NotNull
    String getOperation();

    @NotNull
    SpanContext getSpanContext();

    @Nullable
    SpanStatus getStatus();

    @Nullable
    String getTag(@NotNull String str);

    @Nullable
    Throwable getThrowable();

    boolean isFinished();

    void setData(@NotNull String str, @NotNull Object obj);

    void setDescription(@Nullable String str);

    void setOperation(@NotNull String str);

    void setStatus(@Nullable SpanStatus spanStatus);

    void setTag(@NotNull String str, @NotNull String str2);

    void setThrowable(@Nullable Throwable th);

    @NotNull
    ISpan startChild(@NotNull String str);

    @NotNull
    ISpan startChild(@NotNull String str, @Nullable String str2);

    @ApiStatus.Internal
    @NotNull
    ISpan startChild(@NotNull String str, @Nullable String str2, @Nullable Date date);

    @NotNull
    SentryTraceHeader toSentryTrace();

    @ApiStatus.Experimental
    @Nullable
    TraceStateHeader toTraceStateHeader();

    @ApiStatus.Experimental
    @Nullable
    TraceState traceState();
}
