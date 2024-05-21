package io.sentry;

import io.sentry.protocol.Contexts;
import io.sentry.protocol.Request;
import io.sentry.protocol.SentryId;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public interface ITransaction extends ISpan {
    @ApiStatus.ScheduledForRemoval
    @Deprecated
    @NotNull
    Contexts getContexts();

    @NotNull
    SentryId getEventId();

    @Nullable
    Span getLatestActiveSpan();

    @NotNull
    String getName();

    @ApiStatus.ScheduledForRemoval
    @Deprecated
    @Nullable
    Request getRequest();

    @TestOnly
    @NotNull
    List<Span> getSpans();

    @Nullable
    Boolean isSampled();

    void setName(@NotNull String str);

    @ApiStatus.ScheduledForRemoval
    @Deprecated
    void setRequest(@Nullable Request request);
}
