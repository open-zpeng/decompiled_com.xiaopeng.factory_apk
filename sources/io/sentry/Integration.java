package io.sentry;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public interface Integration {
    void register(@NotNull IHub iHub, @NotNull SentryOptions sentryOptions);
}
