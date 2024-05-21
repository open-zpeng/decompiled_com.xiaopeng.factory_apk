package io.sentry;

import java.util.concurrent.Future;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
interface ISentryExecutorService {
    void close(long j);

    @NotNull
    Future<?> submit(@NotNull Runnable runnable);
}
