package io.sentry.android.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.internal.gestures.NoOpWindowCallback;
import io.sentry.android.core.internal.gestures.SentryGestureListener;
import io.sentry.android.core.internal.gestures.SentryWindowCallback;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class UserInteractionIntegration implements Integration, Closeable, Application.ActivityLifecycleCallbacks {
    @NotNull
    private final Application application;
    @Nullable
    private IHub hub;
    private final boolean isAndroidXAvailable;
    private final boolean isAndroidXScrollViewAvailable;
    @Nullable
    private SentryAndroidOptions options;

    public UserInteractionIntegration(@NotNull Application application, @NotNull LoadClass classLoader) {
        this.application = (Application) Objects.requireNonNull(application, "Application is required");
        this.isAndroidXAvailable = classLoader.isClassAvailable("androidx.core.view.GestureDetectorCompat", this.options);
        this.isAndroidXScrollViewAvailable = classLoader.isClassAvailable("androidx.core.view.ScrollingView", this.options);
    }

    private void startTracking(@Nullable Window window, @NotNull Context context) {
        if (window == null) {
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.INFO, "Window was null in startTracking", new Object[0]);
            }
        } else if (this.hub != null && this.options != null) {
            Window.Callback delegate = window.getCallback();
            if (delegate == null) {
                delegate = new NoOpWindowCallback();
            }
            SentryGestureListener gestureListener = new SentryGestureListener(new WeakReference(window), this.hub, this.options, this.isAndroidXScrollViewAvailable);
            window.setCallback(new SentryWindowCallback(delegate, context, gestureListener, this.options));
        }
    }

    private void stopTracking(@Nullable Window window) {
        if (window == null) {
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.INFO, "Window was null in stopTracking", new Object[0]);
                return;
            }
            return;
        }
        Window.Callback current = window.getCallback();
        if (current instanceof SentryWindowCallback) {
            if (((SentryWindowCallback) current).getDelegate() instanceof NoOpWindowCallback) {
                window.setCallback(null);
            } else {
                window.setCallback(((SentryWindowCallback) current).getDelegate());
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(@NotNull Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(@NotNull Activity activity) {
        startTracking(activity.getWindow(), activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(@NotNull Activity activity) {
        stopTracking(activity.getWindow());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(@NotNull Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(@NotNull Activity activity) {
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "UserInteractionIntegration enabled: %s", Boolean.valueOf(this.options.isEnableUserInteractionBreadcrumbs()));
        if (this.options.isEnableUserInteractionBreadcrumbs()) {
            if (this.isAndroidXAvailable) {
                this.application.registerActivityLifecycleCallbacks(this);
                this.options.getLogger().log(SentryLevel.DEBUG, "UserInteractionIntegration installed.", new Object[0]);
                return;
            }
            options.getLogger().log(SentryLevel.INFO, "androidx.core is not available, UserInteractionIntegration won't be installed", new Object[0]);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.application.unregisterActivityLifecycleCallbacks(this);
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions != null) {
            sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "UserInteractionIntegration removed.", new Object[0]);
        }
    }
}
