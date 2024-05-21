package io.sentry.android.core;

import android.annotation.SuppressLint;
import android.content.Context;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.ANRWatchDog;
import io.sentry.exception.ExceptionMechanismException;
import io.sentry.protocol.Mechanism;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class AnrIntegration implements Integration, Closeable {
    @SuppressLint({"StaticFieldLeak"})
    @Nullable
    private static ANRWatchDog anrWatchDog;
    @NotNull
    private static final Object watchDogLock = new Object();
    @NotNull
    private final Context context;
    @Nullable
    private SentryOptions options;

    public AnrIntegration(@NotNull Context context) {
        this.context = context;
    }

    @Override // io.sentry.Integration
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required");
        register(hub, (SentryAndroidOptions) options);
    }

    private void register(@NotNull final IHub hub, @NotNull final SentryAndroidOptions options) {
        options.getLogger().log(SentryLevel.DEBUG, "AnrIntegration enabled: %s", Boolean.valueOf(options.isAnrEnabled()));
        if (options.isAnrEnabled()) {
            synchronized (watchDogLock) {
                try {
                    try {
                        if (anrWatchDog == null) {
                            options.getLogger().log(SentryLevel.DEBUG, "ANR timeout in milliseconds: %d", Long.valueOf(options.getAnrTimeoutIntervalMillis()));
                            anrWatchDog = new ANRWatchDog(options.getAnrTimeoutIntervalMillis(), options.isAnrReportInDebug(), new ANRWatchDog.ANRListener() { // from class: io.sentry.android.core.-$$Lambda$AnrIntegration$hRvUVYRYnOZD36jN40KfQaw4dLY
                                @Override // io.sentry.android.core.ANRWatchDog.ANRListener
                                public final void onAppNotResponding(ApplicationNotResponding applicationNotResponding) {
                                    AnrIntegration.this.lambda$register$0$AnrIntegration(hub, options, applicationNotResponding);
                                }
                            }, options.getLogger(), this.context);
                            anrWatchDog.start();
                            options.getLogger().log(SentryLevel.DEBUG, "AnrIntegration installed.", new Object[0]);
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    public /* synthetic */ void lambda$register$0$AnrIntegration(IHub hub, SentryAndroidOptions options, ApplicationNotResponding error) {
        reportANR(hub, options.getLogger(), error);
    }

    @TestOnly
    void reportANR(@NotNull IHub hub, @NotNull ILogger logger, @NotNull ApplicationNotResponding error) {
        logger.log(SentryLevel.INFO, "ANR triggered with message: %s", error.getMessage());
        Mechanism mechanism = new Mechanism();
        mechanism.setType("ANR");
        ExceptionMechanismException throwable = new ExceptionMechanismException(mechanism, error, error.getThread(), true);
        hub.captureException(throwable);
    }

    @TestOnly
    @Nullable
    ANRWatchDog getANRWatchDog() {
        return anrWatchDog;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (watchDogLock) {
            if (anrWatchDog != null) {
                anrWatchDog.interrupt();
                anrWatchDog = null;
                if (this.options != null) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "AnrIntegration removed.", new Object[0]);
                }
            }
        }
    }
}
