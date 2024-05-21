package io.sentry.android.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.xiaopeng.commonfunc.Constant;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import io.sentry.util.StringUtils;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class SystemEventsBreadcrumbsIntegration implements Integration, Closeable {
    @NotNull
    private final List<String> actions;
    @NotNull
    private final Context context;
    @Nullable
    private SentryAndroidOptions options;
    @TestOnly
    @Nullable
    SystemEventsBroadcastReceiver receiver;

    public SystemEventsBreadcrumbsIntegration(@NotNull Context context) {
        this(context, getDefaultActions());
    }

    public SystemEventsBreadcrumbsIntegration(@NotNull Context context, @NotNull List<String> actions) {
        this.context = (Context) Objects.requireNonNull(context, "Context is required");
        this.actions = (List) Objects.requireNonNull(actions, "Actions list is required");
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "SystemEventsBreadcrumbsIntegration enabled: %s", Boolean.valueOf(this.options.isEnableSystemEventBreadcrumbs()));
        if (this.options.isEnableSystemEventBreadcrumbs()) {
            this.receiver = new SystemEventsBroadcastReceiver(hub, this.options.getLogger());
            IntentFilter filter = new IntentFilter();
            for (String item : this.actions) {
                filter.addAction(item);
            }
            try {
                this.context.registerReceiver(this.receiver, filter);
                this.options.getLogger().log(SentryLevel.DEBUG, "SystemEventsBreadcrumbsIntegration installed.", new Object[0]);
            } catch (Throwable e) {
                this.options.setEnableSystemEventBreadcrumbs(false);
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to initialize SystemEventsBreadcrumbsIntegration.", e);
            }
        }
    }

    @NotNull
    private static List<String> getDefaultActions() {
        List<String> actions = new ArrayList<>();
        actions.add("android.appwidget.action.APPWIDGET_DELETED");
        actions.add("android.appwidget.action.APPWIDGET_DISABLED");
        actions.add("android.appwidget.action.APPWIDGET_ENABLED");
        actions.add("android.appwidget.action.APPWIDGET_HOST_RESTORED");
        actions.add("android.appwidget.action.APPWIDGET_RESTORED");
        actions.add("android.appwidget.action.APPWIDGET_UPDATE");
        actions.add("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS");
        actions.add("android.intent.action.ACTION_POWER_CONNECTED");
        actions.add("android.intent.action.ACTION_POWER_DISCONNECTED");
        actions.add("android.intent.action.ACTION_SHUTDOWN");
        actions.add("android.intent.action.AIRPLANE_MODE");
        actions.add("android.intent.action.BATTERY_LOW");
        actions.add("android.intent.action.BATTERY_OKAY");
        actions.add("android.intent.action.BOOT_COMPLETED");
        actions.add("android.intent.action.CAMERA_BUTTON");
        actions.add("android.intent.action.CONFIGURATION_CHANGED");
        actions.add("android.intent.action.CONTENT_CHANGED");
        actions.add("android.intent.action.DATE_CHANGED");
        actions.add("android.intent.action.DEVICE_STORAGE_LOW");
        actions.add("android.intent.action.DEVICE_STORAGE_OK");
        actions.add("android.intent.action.DOCK_EVENT");
        actions.add("android.intent.action.DREAMING_STARTED");
        actions.add("android.intent.action.DREAMING_STOPPED");
        actions.add("android.intent.action.INPUT_METHOD_CHANGED");
        actions.add("android.intent.action.LOCALE_CHANGED");
        actions.add("android.intent.action.REBOOT");
        actions.add("android.intent.action.SCREEN_OFF");
        actions.add("android.intent.action.SCREEN_ON");
        actions.add("android.intent.action.TIMEZONE_CHANGED");
        actions.add("android.intent.action.TIME_SET");
        actions.add("android.os.action.DEVICE_IDLE_MODE_CHANGED");
        actions.add("android.os.action.POWER_SAVE_MODE_CHANGED");
        actions.add("android.intent.action.APP_ERROR");
        actions.add("android.intent.action.BUG_REPORT");
        actions.add("android.intent.action.MEDIA_BAD_REMOVAL");
        actions.add("android.intent.action.MEDIA_MOUNTED");
        actions.add("android.intent.action.MEDIA_UNMOUNTABLE");
        actions.add("android.intent.action.MEDIA_UNMOUNTED");
        return actions;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SystemEventsBroadcastReceiver systemEventsBroadcastReceiver = this.receiver;
        if (systemEventsBroadcastReceiver != null) {
            this.context.unregisterReceiver(systemEventsBroadcastReceiver);
            this.receiver = null;
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "SystemEventsBreadcrumbsIntegration remove.", new Object[0]);
            }
        }
    }

    /* loaded from: classes2.dex */
    static final class SystemEventsBroadcastReceiver extends BroadcastReceiver {
        @NotNull
        private final IHub hub;
        @NotNull
        private final ILogger logger;

        SystemEventsBroadcastReceiver(@NotNull IHub hub, @NotNull ILogger logger) {
            this.hub = hub;
            this.logger = logger;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setType("system");
            breadcrumb.setCategory("device.event");
            String action = intent.getAction();
            String shortAction = StringUtils.getStringAfterDot(action);
            if (shortAction != null) {
                breadcrumb.setData(Constant.ACTION, shortAction);
            }
            Bundle extras = intent.getExtras();
            Map<String, String> newExtras = new HashMap<>();
            if (extras != null && !extras.isEmpty()) {
                for (String item : extras.keySet()) {
                    try {
                        Object value = extras.get(item);
                        if (value != null) {
                            newExtras.put(item, value.toString());
                        }
                    } catch (Throwable exception) {
                        this.logger.log(SentryLevel.ERROR, exception, "%s key of the %s action threw an error.", item, action);
                    }
                }
                breadcrumb.setData("extras", newExtras);
            }
            breadcrumb.setLevel(SentryLevel.INFO);
            this.hub.addBreadcrumb(breadcrumb);
        }
    }
}
