package io.sentry.android.core;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.xiaopeng.commonfunc.Constant;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.core.internal.util.Permissions;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class PhoneStateBreadcrumbsIntegration implements Integration, Closeable {
    @NotNull
    private final Context context;
    @TestOnly
    @Nullable
    PhoneStateChangeListener listener;
    @Nullable
    private SentryAndroidOptions options;
    @Nullable
    private TelephonyManager telephonyManager;

    public PhoneStateBreadcrumbsIntegration(@NotNull Context context) {
        this.context = (Context) Objects.requireNonNull(context, "Context is required");
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "enableSystemEventBreadcrumbs enabled: %s", Boolean.valueOf(this.options.isEnableSystemEventBreadcrumbs()));
        if (this.options.isEnableSystemEventBreadcrumbs() && Permissions.hasPermission(this.context, "android.permission.READ_PHONE_STATE")) {
            this.telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
            if (this.telephonyManager != null) {
                try {
                    this.listener = new PhoneStateChangeListener(hub);
                    this.telephonyManager.listen(this.listener, 32);
                    options.getLogger().log(SentryLevel.DEBUG, "PhoneStateBreadcrumbsIntegration installed.", new Object[0]);
                    return;
                } catch (Throwable e) {
                    this.options.getLogger().log(SentryLevel.INFO, e, "TelephonyManager is not available or ready to use.", new Object[0]);
                    return;
                }
            }
            this.options.getLogger().log(SentryLevel.INFO, "TelephonyManager is not available", new Object[0]);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        PhoneStateChangeListener phoneStateChangeListener;
        TelephonyManager telephonyManager = this.telephonyManager;
        if (telephonyManager != null && (phoneStateChangeListener = this.listener) != null) {
            telephonyManager.listen(phoneStateChangeListener, 0);
            this.listener = null;
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "PhoneStateBreadcrumbsIntegration removed.", new Object[0]);
            }
        }
    }

    /* loaded from: classes2.dex */
    static final class PhoneStateChangeListener extends PhoneStateListener {
        @NotNull
        private final IHub hub;

        PhoneStateChangeListener(@NotNull IHub hub) {
            this.hub = hub;
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == 1) {
                Breadcrumb breadcrumb = new Breadcrumb();
                breadcrumb.setType("system");
                breadcrumb.setCategory("device.event");
                breadcrumb.setData(Constant.ACTION, "CALL_STATE_RINGING");
                breadcrumb.setMessage("Device ringing");
                breadcrumb.setLevel(SentryLevel.INFO);
                this.hub.addBreadcrumb(breadcrumb);
            }
        }
    }
}
