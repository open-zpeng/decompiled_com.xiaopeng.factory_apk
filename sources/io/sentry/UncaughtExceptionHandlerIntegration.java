package io.sentry;

import io.sentry.UncaughtExceptionHandler;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.hints.Flushable;
import io.sentry.hints.SessionEnd;
import io.sentry.protocol.Mechanism;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.lang.Thread;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class UncaughtExceptionHandlerIntegration implements Integration, Thread.UncaughtExceptionHandler, Closeable {
    @Nullable
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    @Nullable
    private IHub hub;
    @Nullable
    private SentryOptions options;
    private boolean registered;
    @NotNull
    private final UncaughtExceptionHandler threadAdapter;

    public UncaughtExceptionHandlerIntegration() {
        this(UncaughtExceptionHandler.Adapter.getInstance());
    }

    UncaughtExceptionHandlerIntegration(@NotNull UncaughtExceptionHandler threadAdapter) {
        this.registered = false;
        this.threadAdapter = (UncaughtExceptionHandler) Objects.requireNonNull(threadAdapter, "threadAdapter is required.");
    }

    @Override // io.sentry.Integration
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        if (this.registered) {
            options.getLogger().log(SentryLevel.ERROR, "Attempt to register a UncaughtExceptionHandlerIntegration twice.", new Object[0]);
            return;
        }
        this.registered = true;
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration enabled: %s", Boolean.valueOf(this.options.isEnableUncaughtExceptionHandler()));
        if (this.options.isEnableUncaughtExceptionHandler()) {
            Thread.UncaughtExceptionHandler currentHandler = this.threadAdapter.getDefaultUncaughtExceptionHandler();
            if (currentHandler != null) {
                ILogger logger = this.options.getLogger();
                SentryLevel sentryLevel = SentryLevel.DEBUG;
                logger.log(sentryLevel, "default UncaughtExceptionHandler class='" + currentHandler.getClass().getName() + "'", new Object[0]);
                this.defaultExceptionHandler = currentHandler;
            }
            this.threadAdapter.setDefaultUncaughtExceptionHandler(this);
            this.options.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration installed.", new Object[0]);
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable thrown) {
        SentryOptions sentryOptions = this.options;
        if (sentryOptions != null && this.hub != null) {
            sentryOptions.getLogger().log(SentryLevel.INFO, "Uncaught exception received.", new Object[0]);
            try {
                UncaughtExceptionHint hint = new UncaughtExceptionHint(this.options.getFlushTimeoutMillis(), this.options.getLogger());
                Throwable throwable = getUnhandledThrowable(thread, thrown);
                SentryEvent event = new SentryEvent(throwable);
                event.setLevel(SentryLevel.FATAL);
                this.hub.captureEvent(event, hint);
                if (!hint.waitFlush()) {
                    this.options.getLogger().log(SentryLevel.WARNING, "Timed out waiting to flush event to disk before crashing. Event: %s", event.getEventId());
                }
            } catch (Throwable e) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error sending uncaught exception to Sentry.", e);
            }
            if (this.defaultExceptionHandler != null) {
                this.options.getLogger().log(SentryLevel.INFO, "Invoking inner uncaught exception handler.", new Object[0]);
                this.defaultExceptionHandler.uncaughtException(thread, thrown);
            } else if (this.options.isPrintUncaughtStackTrace()) {
                thrown.printStackTrace();
            }
        }
    }

    @TestOnly
    @NotNull
    static Throwable getUnhandledThrowable(@NotNull Thread thread, @NotNull Throwable thrown) {
        Mechanism mechanism = new Mechanism();
        mechanism.setHandled(false);
        mechanism.setType("UncaughtExceptionHandler");
        return new ExceptionMechanismException(mechanism, thrown, thread);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this == this.threadAdapter.getDefaultUncaughtExceptionHandler()) {
            this.threadAdapter.setDefaultUncaughtExceptionHandler(this.defaultExceptionHandler);
            SentryOptions sentryOptions = this.options;
            if (sentryOptions != null) {
                sentryOptions.getLogger().log(SentryLevel.DEBUG, "UncaughtExceptionHandlerIntegration removed.", new Object[0]);
            }
        }
    }

    /* loaded from: classes2.dex */
    private static final class UncaughtExceptionHint implements DiskFlushNotification, Flushable, SessionEnd {
        private final long flushTimeoutMillis;
        private final CountDownLatch latch = new CountDownLatch(1);
        @NotNull
        private final ILogger logger;

        UncaughtExceptionHint(long flushTimeoutMillis, @NotNull ILogger logger) {
            this.flushTimeoutMillis = flushTimeoutMillis;
            this.logger = logger;
        }

        @Override // io.sentry.hints.Flushable
        public boolean waitFlush() {
            try {
                return this.latch.await(this.flushTimeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.logger.log(SentryLevel.ERROR, "Exception while awaiting for flush in UncaughtExceptionHint", e);
                return false;
            }
        }

        @Override // io.sentry.hints.DiskFlushNotification
        public void markFlushed() {
            this.latch.countDown();
        }
    }
}
