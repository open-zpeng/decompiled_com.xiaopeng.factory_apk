package io.sentry;

import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
public final class ShutdownHookIntegration implements Integration, Closeable {
    @NotNull
    private final Runtime runtime;
    @Nullable
    private Thread thread;

    @TestOnly
    public ShutdownHookIntegration(@NotNull Runtime runtime) {
        this.runtime = (Runtime) Objects.requireNonNull(runtime, "Runtime is required");
    }

    public ShutdownHookIntegration() {
        this(Runtime.getRuntime());
    }

    @Override // io.sentry.Integration
    public void register(@NotNull final IHub hub, @NotNull final SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        Objects.requireNonNull(options, "SentryOptions is required");
        if (options.isEnableShutdownHook()) {
            this.thread = new Thread(new Runnable() { // from class: io.sentry.-$$Lambda$ShutdownHookIntegration$rAAwevLH0nb6Wau1Wt9dg8tjAmM
                @Override // java.lang.Runnable
                public final void run() {
                    IHub.this.flush(options.getFlushTimeoutMillis());
                }
            });
            this.runtime.addShutdownHook(this.thread);
            options.getLogger().log(SentryLevel.DEBUG, "ShutdownHookIntegration installed.", new Object[0]);
            return;
        }
        options.getLogger().log(SentryLevel.INFO, "enableShutdownHook is disabled.", new Object[0]);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Thread thread = this.thread;
        if (thread != null) {
            this.runtime.removeShutdownHook(thread);
        }
    }

    @VisibleForTesting
    @Nullable
    Thread getHook() {
        return this.thread;
    }
}
