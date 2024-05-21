package io.sentry.android.core;

import io.sentry.EventProcessor;
import io.sentry.SentryEvent;
import io.sentry.SpanContext;
import io.sentry.protocol.MeasurementValue;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.util.Objects;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class PerformanceAndroidEventProcessor implements EventProcessor {
    @NotNull
    private final ActivityFramesTracker activityFramesTracker;
    @NotNull
    private final SentryAndroidOptions options;
    private boolean sentStartMeasurement = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PerformanceAndroidEventProcessor(@NotNull SentryAndroidOptions options, @NotNull ActivityFramesTracker activityFramesTracker) {
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options, "SentryAndroidOptions is required");
        this.activityFramesTracker = (ActivityFramesTracker) Objects.requireNonNull(activityFramesTracker, "ActivityFramesTracker is required");
    }

    @Override // io.sentry.EventProcessor
    @Nullable
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        return event;
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public synchronized SentryTransaction process(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        Map<String, MeasurementValue> framesMetrics;
        Long appStartUpInterval;
        if (this.options.isTracingEnabled()) {
            if (!this.sentStartMeasurement && hasAppStartSpan(transaction.getSpans()) && (appStartUpInterval = AppStartState.getInstance().getAppStartInterval()) != null) {
                MeasurementValue value = new MeasurementValue((float) appStartUpInterval.longValue());
                String appStartKey = AppStartState.getInstance().isColdStart().booleanValue() ? "app_start_cold" : "app_start_warm";
                transaction.getMeasurements().put(appStartKey, value);
                this.sentStartMeasurement = true;
            }
            SentryId eventId = transaction.getEventId();
            SpanContext spanContext = transaction.getContexts().getTrace();
            if (eventId != null && spanContext != null && spanContext.getOperation().contentEquals("ui.load") && (framesMetrics = this.activityFramesTracker.takeMetrics(eventId)) != null) {
                transaction.getMeasurements().putAll(framesMetrics);
            }
            return transaction;
        }
        return transaction;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean hasAppStartSpan(@org.jetbrains.annotations.NotNull java.util.List<io.sentry.protocol.SentrySpan> r5) {
        /*
            r4 = this;
            java.util.Iterator r0 = r5.iterator()
        L4:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L2c
            java.lang.Object r1 = r0.next()
            io.sentry.protocol.SentrySpan r1 = (io.sentry.protocol.SentrySpan) r1
            java.lang.String r2 = r1.getOp()
            java.lang.String r3 = "app.start.cold"
            boolean r2 = r2.contentEquals(r3)
            if (r2 != 0) goto L2a
            java.lang.String r2 = r1.getOp()
            java.lang.String r3 = "app.start.warm"
            boolean r2 = r2.contentEquals(r3)
            if (r2 == 0) goto L29
            goto L2a
        L29:
            goto L4
        L2a:
            r0 = 1
            return r0
        L2c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.android.core.PerformanceAndroidEventProcessor.hasAppStartSpan(java.util.List):boolean");
    }
}
