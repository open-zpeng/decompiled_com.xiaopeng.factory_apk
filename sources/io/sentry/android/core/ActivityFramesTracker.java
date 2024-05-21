package io.sentry.android.core;

import android.app.Activity;
import android.util.SparseIntArray;
import androidx.core.app.FrameMetricsAggregator;
import io.sentry.ILogger;
import io.sentry.protocol.MeasurementValue;
import io.sentry.protocol.SentryId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class ActivityFramesTracker {
    @NotNull
    private final Map<SentryId, Map<String, MeasurementValue>> activityMeasurements;
    private boolean androidXAvailable;
    @Nullable
    private FrameMetricsAggregator frameMetricsAggregator;

    public ActivityFramesTracker(@NotNull LoadClass loadClass, @Nullable ILogger logger) {
        this.frameMetricsAggregator = null;
        this.androidXAvailable = true;
        this.activityMeasurements = new ConcurrentHashMap();
        this.androidXAvailable = loadClass.isClassAvailable("androidx.core.app.FrameMetricsAggregator", logger);
        if (this.androidXAvailable) {
            this.frameMetricsAggregator = new FrameMetricsAggregator();
        }
    }

    public ActivityFramesTracker(@NotNull LoadClass loadClass) {
        this(loadClass, null);
    }

    @TestOnly
    ActivityFramesTracker(@Nullable FrameMetricsAggregator frameMetricsAggregator) {
        this.frameMetricsAggregator = null;
        this.androidXAvailable = true;
        this.activityMeasurements = new ConcurrentHashMap();
        this.frameMetricsAggregator = frameMetricsAggregator;
    }

    private boolean isFrameMetricsAggregatorAvailable() {
        return this.androidXAvailable && this.frameMetricsAggregator != null;
    }

    public synchronized void addActivity(@NotNull Activity activity) {
        if (isFrameMetricsAggregatorAvailable()) {
            this.frameMetricsAggregator.add(activity);
        }
    }

    public synchronized void setMetrics(@NotNull Activity activity, @NotNull SentryId sentryId) {
        if (isFrameMetricsAggregatorAvailable()) {
            int totalFrames = 0;
            int slowFrames = 0;
            int frozenFrames = 0;
            SparseIntArray[] framesRates = null;
            try {
                framesRates = this.frameMetricsAggregator.remove(activity);
            } catch (Throwable th) {
            }
            if (framesRates != null) {
                SparseIntArray totalIndexArray = framesRates[0];
                if (totalIndexArray != null) {
                    for (int i = 0; i < totalIndexArray.size(); i++) {
                        int frameTime = totalIndexArray.keyAt(i);
                        int numFrames = totalIndexArray.valueAt(i);
                        totalFrames += numFrames;
                        if (frameTime > 700) {
                            frozenFrames += numFrames;
                        } else if (frameTime > 16) {
                            slowFrames += numFrames;
                        }
                    }
                }
            }
            if (totalFrames == 0 && slowFrames == 0 && frozenFrames == 0) {
                return;
            }
            MeasurementValue tfValues = new MeasurementValue(totalFrames);
            MeasurementValue sfValues = new MeasurementValue(slowFrames);
            MeasurementValue ffValues = new MeasurementValue(frozenFrames);
            Map<String, MeasurementValue> measurements = new HashMap<>();
            measurements.put("frames_total", tfValues);
            measurements.put("frames_slow", sfValues);
            measurements.put("frames_frozen", ffValues);
            this.activityMeasurements.put(sentryId, measurements);
        }
    }

    @Nullable
    public synchronized Map<String, MeasurementValue> takeMetrics(@NotNull SentryId sentryId) {
        if (!isFrameMetricsAggregatorAvailable()) {
            return null;
        }
        Map<String, MeasurementValue> stringMeasurementValueMap = this.activityMeasurements.get(sentryId);
        this.activityMeasurements.remove(sentryId);
        return stringMeasurementValueMap;
    }

    public synchronized void stop() {
        if (isFrameMetricsAggregatorAvailable()) {
            this.frameMetricsAggregator.stop();
        }
        this.activityMeasurements.clear();
    }
}
