package io.sentry.android.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.xiaopeng.commonfunc.Constant;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class TempSensorBreadcrumbsIntegration implements Integration, Closeable, SensorEventListener {
    @NotNull
    private final Context context;
    @Nullable
    private IHub hub;
    @Nullable
    private SentryAndroidOptions options;
    @TestOnly
    @Nullable
    SensorManager sensorManager;

    public TempSensorBreadcrumbsIntegration(@NotNull Context context) {
        this.context = (Context) Objects.requireNonNull(context, "Context is required");
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "enableSystemEventsBreadcrumbs enabled: %s", Boolean.valueOf(this.options.isEnableSystemEventBreadcrumbs()));
        if (this.options.isEnableSystemEventBreadcrumbs()) {
            try {
                this.sensorManager = (SensorManager) this.context.getSystemService("sensor");
                if (this.sensorManager != null) {
                    Sensor defaultSensor = this.sensorManager.getDefaultSensor(13);
                    if (defaultSensor != null) {
                        this.sensorManager.registerListener(this, defaultSensor, 3);
                        options.getLogger().log(SentryLevel.DEBUG, "TempSensorBreadcrumbsIntegration installed.", new Object[0]);
                    } else {
                        this.options.getLogger().log(SentryLevel.INFO, "TYPE_AMBIENT_TEMPERATURE is not available.", new Object[0]);
                    }
                    return;
                }
                this.options.getLogger().log(SentryLevel.INFO, "SENSOR_SERVICE is not available.", new Object[0]);
            } catch (Throwable e) {
                options.getLogger().log(SentryLevel.ERROR, e, "Failed to init. the SENSOR_SERVICE.", new Object[0]);
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SensorManager sensorManager = this.sensorManager;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            this.sensorManager = null;
            SentryAndroidOptions sentryAndroidOptions = this.options;
            if (sentryAndroidOptions != null) {
                sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "TempSensorBreadcrumbsIntegration removed.", new Object[0]);
            }
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(@NotNull SensorEvent event) {
        float[] values = event.values;
        if (values != null && values.length != 0 && values[0] != 0.0f && this.hub != null) {
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setType("system");
            breadcrumb.setCategory("device.event");
            breadcrumb.setData(Constant.ACTION, "TYPE_AMBIENT_TEMPERATURE");
            breadcrumb.setData("accuracy", Integer.valueOf(event.accuracy));
            breadcrumb.setData("timestamp", Long.valueOf(event.timestamp));
            breadcrumb.setLevel(SentryLevel.INFO);
            breadcrumb.setData("degree", Float.valueOf(event.values[0]));
            this.hub.addBreadcrumb(breadcrumb);
        }
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
