package io.sentry.android.core;

import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.OutboxSender;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import java.io.Closeable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public abstract class EnvelopeFileObserverIntegration implements Integration, Closeable {
    @Nullable
    private ILogger logger;
    @Nullable
    private EnvelopeFileObserver observer;

    @TestOnly
    @Nullable
    abstract String getPath(@NotNull SentryOptions sentryOptions);

    @NotNull
    public static EnvelopeFileObserverIntegration getOutboxFileObserver() {
        return new OutboxEnvelopeFileObserverIntegration();
    }

    @Override // io.sentry.Integration
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        Objects.requireNonNull(options, "SentryOptions is required");
        this.logger = options.getLogger();
        String path = getPath(options);
        if (path == null) {
            this.logger.log(SentryLevel.WARNING, "Null given as a path to EnvelopeFileObserverIntegration. Nothing will be registered.", new Object[0]);
            return;
        }
        this.logger.log(SentryLevel.DEBUG, "Registering EnvelopeFileObserverIntegration for path: %s", path);
        OutboxSender outboxSender = new OutboxSender(hub, options.getEnvelopeReader(), options.getSerializer(), this.logger, options.getFlushTimeoutMillis());
        this.observer = new EnvelopeFileObserver(path, outboxSender, this.logger, options.getFlushTimeoutMillis());
        try {
            this.observer.startWatching();
            this.logger.log(SentryLevel.DEBUG, "EnvelopeFileObserverIntegration installed.", new Object[0]);
        } catch (Throwable e) {
            options.getLogger().log(SentryLevel.ERROR, "Failed to initialize EnvelopeFileObserverIntegration.", e);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        EnvelopeFileObserver envelopeFileObserver = this.observer;
        if (envelopeFileObserver != null) {
            envelopeFileObserver.stopWatching();
            ILogger iLogger = this.logger;
            if (iLogger != null) {
                iLogger.log(SentryLevel.DEBUG, "EnvelopeFileObserverIntegration removed.", new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class OutboxEnvelopeFileObserverIntegration extends EnvelopeFileObserverIntegration {
        private OutboxEnvelopeFileObserverIntegration() {
        }

        @Override // io.sentry.android.core.EnvelopeFileObserverIntegration
        @Nullable
        protected String getPath(@NotNull SentryOptions options) {
            return options.getOutboxPath();
        }
    }
}
