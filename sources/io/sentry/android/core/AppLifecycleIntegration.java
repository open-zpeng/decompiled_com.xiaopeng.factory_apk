package io.sentry.android.core;

import androidx.lifecycle.ProcessLifecycleOwner;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.internal.util.MainThreadChecker;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class AppLifecycleIntegration implements Integration, Closeable {
    @NotNull
    private final IHandler handler;
    @Nullable
    private SentryAndroidOptions options;
    @TestOnly
    @Nullable
    LifecycleWatcher watcher;

    public AppLifecycleIntegration() {
        this(new MainLooperHandler());
    }

    AppLifecycleIntegration(@NotNull IHandler handler) {
        this.handler = handler;
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0082 -> B:21:0x009c). Please submit an issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x0090 -> B:21:0x009c). Please submit an issue!!! */
    @Override // io.sentry.Integration
    public void register(@NotNull final IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "enableSessionTracking enabled: %s", Boolean.valueOf(this.options.isEnableAutoSessionTracking()));
        this.options.getLogger().log(SentryLevel.DEBUG, "enableAppLifecycleBreadcrumbs enabled: %s", Boolean.valueOf(this.options.isEnableAppLifecycleBreadcrumbs()));
        if (this.options.isEnableAutoSessionTracking() || this.options.isEnableAppLifecycleBreadcrumbs()) {
            try {
                Class.forName("androidx.lifecycle.DefaultLifecycleObserver");
                Class.forName("androidx.lifecycle.ProcessLifecycleOwner");
                if (MainThreadChecker.isMainThread()) {
                    lambda$register$0$AppLifecycleIntegration(hub);
                } else {
                    this.handler.post(new Runnable() { // from class: io.sentry.android.core.-$$Lambda$AppLifecycleIntegration$q1qzdtzMh5E4OH5R-VItJ38UlZ8
                        @Override // java.lang.Runnable
                        public final void run() {
                            AppLifecycleIntegration.this.lambda$register$0$AppLifecycleIntegration(hub);
                        }
                    });
                }
            } catch (ClassNotFoundException e) {
                options.getLogger().log(SentryLevel.INFO, "androidx.lifecycle is not available, AppLifecycleIntegration won't be installed", e);
            } catch (IllegalStateException e2) {
                options.getLogger().log(SentryLevel.ERROR, "AppLifecycleIntegration could not be installed", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: addObserver */
    public void lambda$register$0$AppLifecycleIntegration(@NotNull IHub hub) {
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions == null) {
            return;
        }
        this.watcher = new LifecycleWatcher(hub, sentryAndroidOptions.getSessionTrackingIntervalMillis(), this.options.isEnableAutoSessionTracking(), this.options.isEnableAppLifecycleBreadcrumbs());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this.watcher);
        this.options.getLogger().log(SentryLevel.DEBUG, "AppLifecycleIntegration installed.", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeObserver */
    public void lambda$close$1$AppLifecycleIntegration() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this.watcher);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.watcher != null) {
            if (MainThreadChecker.isMainThread()) {
                lambda$close$1$AppLifecycleIntegration();
            } else {
                this.handler.post(new Runnable() { // from class: io.sentry.android.core.-$$Lambda$AppLifecycleIntegration$8Bm-91OsZn3s945u6qBp4djC2FE
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppLifecycleIntegration.this.lambda$close$1$AppLifecycleIntegration();
                    }
                });
            }
            this.watcher = null;
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "AppLifecycleIntegration removed.", new Object[0]);
            }
        }
    }
}
