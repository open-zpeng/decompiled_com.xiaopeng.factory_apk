package io.sentry;

import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface EventProcessor {
    @Nullable
    default SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        return event;
    }

    @Nullable
    default SentryTransaction process(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        return transaction;
    }
}
