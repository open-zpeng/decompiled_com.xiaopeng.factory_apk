package io.sentry.android.core;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import androidx.core.app.NotificationCompat;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.xiaopeng.commonfunc.Constant;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.internal.util.DeviceOrientations;
import io.sentry.protocol.Device;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class AppComponentsBreadcrumbsIntegration implements Integration, Closeable, ComponentCallbacks2 {
    @NotNull
    private final Context context;
    @Nullable
    private IHub hub;
    @Nullable
    private SentryAndroidOptions options;

    public AppComponentsBreadcrumbsIntegration(@NotNull Context context) {
        this.context = (Context) Objects.requireNonNull(context, "Context is required");
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "AppComponentsBreadcrumbsIntegration enabled: %s", Boolean.valueOf(this.options.isEnableAppComponentBreadcrumbs()));
        if (this.options.isEnableAppComponentBreadcrumbs()) {
            try {
                this.context.registerComponentCallbacks(this);
                options.getLogger().log(SentryLevel.DEBUG, "AppComponentsBreadcrumbsIntegration installed.", new Object[0]);
            } catch (Throwable e) {
                this.options.setEnableAppComponentBreadcrumbs(false);
                options.getLogger().log(SentryLevel.INFO, e, "ComponentCallbacks2 is not available.", new Object[0]);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        try {
            this.context.unregisterComponentCallbacks(this);
        } catch (Throwable ignored) {
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, ignored, "It was not possible to unregisterComponentCallbacks", new Object[0]);
            }
        }
        SentryAndroidOptions sentryAndroidOptions2 = this.options;
        if (sentryAndroidOptions2 != null) {
            sentryAndroidOptions2.getLogger().log(SentryLevel.DEBUG, "AppComponentsBreadcrumbsIntegration removed.", new Object[0]);
        }
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        String orientation;
        if (this.hub != null) {
            Device.DeviceOrientation deviceOrientation = DeviceOrientations.getOrientation(this.context.getResources().getConfiguration().orientation);
            if (deviceOrientation != null) {
                orientation = deviceOrientation.name().toLowerCase(Locale.ROOT);
            } else {
                orientation = "undefined";
            }
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setType(NotificationCompat.CATEGORY_NAVIGATION);
            breadcrumb.setCategory("device.orientation");
            breadcrumb.setData(RequestParameters.POSITION, orientation);
            breadcrumb.setLevel(SentryLevel.INFO);
            this.hub.addBreadcrumb(breadcrumb);
        }
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        createLowMemoryBreadcrumb(null);
    }

    @Override // android.content.ComponentCallbacks2
    public void onTrimMemory(int level) {
        createLowMemoryBreadcrumb(Integer.valueOf(level));
    }

    private void createLowMemoryBreadcrumb(@Nullable Integer level) {
        if (this.hub != null) {
            Breadcrumb breadcrumb = new Breadcrumb();
            if (level != null) {
                if (level.intValue() < 40) {
                    return;
                }
                breadcrumb.setData("level", level);
            }
            breadcrumb.setType("system");
            breadcrumb.setCategory("device.event");
            breadcrumb.setMessage("Low memory");
            breadcrumb.setData(Constant.ACTION, "LOW_MEMORY");
            breadcrumb.setLevel(SentryLevel.WARNING);
            this.hub.addBreadcrumb(breadcrumb);
        }
    }
}
