package io.sentry.android.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.util.Objects;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class ManifestMetadataReader {
    static final String ANR_ENABLE = "io.sentry.anr.enable";
    static final String ANR_REPORT_DEBUG = "io.sentry.anr.report-debug";
    static final String ANR_TIMEOUT_INTERVAL_MILLIS = "io.sentry.anr.timeout-interval-millis";
    static final String ATTACH_THREADS = "io.sentry.attach-threads";
    static final String AUTO_INIT = "io.sentry.auto-init";
    static final String AUTO_SESSION_TRACKING_ENABLE = "io.sentry.auto-session-tracking.enable";
    static final String BREADCRUMBS_ACTIVITY_LIFECYCLE_ENABLE = "io.sentry.breadcrumbs.activity-lifecycle";
    static final String BREADCRUMBS_APP_COMPONENTS_ENABLE = "io.sentry.breadcrumbs.app-components";
    static final String BREADCRUMBS_APP_LIFECYCLE_ENABLE = "io.sentry.breadcrumbs.app-lifecycle";
    static final String BREADCRUMBS_SYSTEM_EVENTS_ENABLE = "io.sentry.breadcrumbs.system-events";
    static final String BREADCRUMBS_USER_INTERACTION_ENABLE = "io.sentry.breadcrumbs.user-interaction";
    static final String DEBUG = "io.sentry.debug";
    static final String DEBUG_LEVEL = "io.sentry.debug.level";
    static final String DSN = "io.sentry.dsn";
    static final String ENVIRONMENT = "io.sentry.environment";
    static final String NDK_ENABLE = "io.sentry.ndk.enable";
    static final String NDK_SCOPE_SYNC_ENABLE = "io.sentry.ndk.scope-sync.enable";
    static final String PROGUARD_UUID = "io.sentry.proguard-uuid";
    static final String RELEASE = "io.sentry.release";
    static final String SAMPLE_RATE = "io.sentry.sample-rate";
    static final String SESSION_TRACKING_ENABLE = "io.sentry.session-tracking.enable";
    static final String SESSION_TRACKING_TIMEOUT_INTERVAL_MILLIS = "io.sentry.session-tracking.timeout-interval-millis";
    static final String TRACES_ACTIVITY_AUTO_FINISH_ENABLE = "io.sentry.traces.activity.auto-finish.enable";
    static final String TRACES_ACTIVITY_ENABLE = "io.sentry.traces.activity.enable";
    static final String TRACES_SAMPLE_RATE = "io.sentry.traces.sample-rate";
    @ApiStatus.Experimental
    static final String TRACE_SAMPLING = "io.sentry.traces.trace-sampling";
    static final String TRACING_ORIGINS = "io.sentry.traces.tracing-origins";
    static final String UNCAUGHT_EXCEPTION_HANDLER_ENABLE = "io.sentry.uncaught-exception-handler.enable";

    private ManifestMetadataReader() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void applyMetadata(@NotNull Context context, @NotNull SentryAndroidOptions options) {
        String level;
        Objects.requireNonNull(context, "The application context is required.");
        Objects.requireNonNull(options, "The options object is required.");
        try {
            Bundle metadata = getMetadata(context);
            ILogger logger = options.getLogger();
            if (metadata != null) {
                options.setDebug(Boolean.valueOf(readBool(metadata, logger, DEBUG, options.isDebug())));
                if (options.isDebug() && (level = readString(metadata, logger, DEBUG_LEVEL, options.getDiagnosticLevel().name().toLowerCase(Locale.ROOT))) != null) {
                    options.setDiagnosticLevel(SentryLevel.valueOf(level.toUpperCase(Locale.ROOT)));
                }
                options.setAnrEnabled(readBool(metadata, logger, ANR_ENABLE, options.isAnrEnabled()));
                boolean enableSessionTracking = readBool(metadata, logger, SESSION_TRACKING_ENABLE, options.isEnableAutoSessionTracking());
                options.setEnableAutoSessionTracking(readBool(metadata, logger, AUTO_SESSION_TRACKING_ENABLE, enableSessionTracking));
                if (options.getSampleRate() == null) {
                    Double sampleRate = readDouble(metadata, logger, SAMPLE_RATE);
                    if (sampleRate.doubleValue() != -1.0d) {
                        options.setSampleRate(sampleRate);
                    }
                }
                options.setAnrReportInDebug(readBool(metadata, logger, ANR_REPORT_DEBUG, options.isAnrReportInDebug()));
                options.setAnrTimeoutIntervalMillis(readLong(metadata, logger, ANR_TIMEOUT_INTERVAL_MILLIS, options.getAnrTimeoutIntervalMillis()));
                String dsn = readString(metadata, logger, DSN, options.getDsn());
                if (dsn == null) {
                    options.getLogger().log(SentryLevel.FATAL, "DSN is required. Use empty string to disable SDK.", new Object[0]);
                } else if (dsn.isEmpty()) {
                    options.getLogger().log(SentryLevel.DEBUG, "DSN is empty, disabling sentry-android", new Object[0]);
                }
                options.setDsn(dsn);
                options.setEnableNdk(readBool(metadata, logger, NDK_ENABLE, options.isEnableNdk()));
                options.setEnableScopeSync(readBool(metadata, logger, NDK_SCOPE_SYNC_ENABLE, options.isEnableScopeSync()));
                options.setRelease(readString(metadata, logger, RELEASE, options.getRelease()));
                options.setEnvironment(readString(metadata, logger, ENVIRONMENT, options.getEnvironment()));
                options.setSessionTrackingIntervalMillis(readLong(metadata, logger, SESSION_TRACKING_TIMEOUT_INTERVAL_MILLIS, options.getSessionTrackingIntervalMillis()));
                options.setEnableActivityLifecycleBreadcrumbs(readBool(metadata, logger, BREADCRUMBS_ACTIVITY_LIFECYCLE_ENABLE, options.isEnableActivityLifecycleBreadcrumbs()));
                options.setEnableAppLifecycleBreadcrumbs(readBool(metadata, logger, BREADCRUMBS_APP_LIFECYCLE_ENABLE, options.isEnableAppComponentBreadcrumbs()));
                options.setEnableSystemEventBreadcrumbs(readBool(metadata, logger, BREADCRUMBS_SYSTEM_EVENTS_ENABLE, options.isEnableSystemEventBreadcrumbs()));
                options.setEnableAppComponentBreadcrumbs(readBool(metadata, logger, BREADCRUMBS_APP_COMPONENTS_ENABLE, options.isEnableAppComponentBreadcrumbs()));
                options.setEnableUserInteractionBreadcrumbs(readBool(metadata, logger, BREADCRUMBS_USER_INTERACTION_ENABLE, options.isEnableUserInteractionBreadcrumbs()));
                options.setEnableUncaughtExceptionHandler(Boolean.valueOf(readBool(metadata, logger, UNCAUGHT_EXCEPTION_HANDLER_ENABLE, options.isEnableUncaughtExceptionHandler())));
                options.setAttachThreads(readBool(metadata, logger, ATTACH_THREADS, options.isAttachThreads()));
                if (options.getTracesSampleRate() == null) {
                    Double tracesSampleRate = readDouble(metadata, logger, TRACES_SAMPLE_RATE);
                    if (tracesSampleRate.doubleValue() != -1.0d) {
                        options.setTracesSampleRate(tracesSampleRate);
                    }
                }
                options.setTraceSampling(readBool(metadata, logger, TRACE_SAMPLING, options.isTraceSampling()));
                options.setEnableAutoActivityLifecycleTracing(readBool(metadata, logger, TRACES_ACTIVITY_ENABLE, options.isEnableAutoActivityLifecycleTracing()));
                options.setEnableActivityLifecycleTracingAutoFinish(readBool(metadata, logger, TRACES_ACTIVITY_AUTO_FINISH_ENABLE, options.isEnableActivityLifecycleTracingAutoFinish()));
                List<String> tracingOrigins = readList(metadata, logger, TRACING_ORIGINS);
                if (tracingOrigins != null) {
                    for (String tracingOrigin : tracingOrigins) {
                        options.addTracingOrigin(tracingOrigin);
                    }
                }
                options.setProguardUuid(readString(metadata, logger, PROGUARD_UUID, options.getProguardUuid()));
            }
            options.getLogger().log(SentryLevel.INFO, "Retrieving configuration from AndroidManifest.xml", new Object[0]);
        } catch (Throwable e) {
            options.getLogger().log(SentryLevel.ERROR, "Failed to read configuration from android manifest metadata.", e);
        }
    }

    private static boolean readBool(@NotNull Bundle metadata, @NotNull ILogger logger, @NotNull String key, boolean defaultValue) {
        boolean value = metadata.getBoolean(key, defaultValue);
        logger.log(SentryLevel.DEBUG, "%s read: %s", key, Boolean.valueOf(value));
        return value;
    }

    @Nullable
    private static String readString(@NotNull Bundle metadata, @NotNull ILogger logger, @NotNull String key, @Nullable String defaultValue) {
        String value = metadata.getString(key, defaultValue);
        logger.log(SentryLevel.DEBUG, "%s read: %s", key, value);
        return value;
    }

    @Nullable
    private static List<String> readList(@NotNull Bundle metadata, @NotNull ILogger logger, @NotNull String key) {
        String value = metadata.getString(key);
        logger.log(SentryLevel.DEBUG, "%s read: %s", key, value);
        if (value != null) {
            return Arrays.asList(value.split(",", -1));
        }
        return null;
    }

    @NotNull
    private static Double readDouble(@NotNull Bundle metadata, @NotNull ILogger logger, @NotNull String key) {
        Double value = Double.valueOf(Float.valueOf(metadata.getFloat(key, -1.0f)).doubleValue());
        logger.log(SentryLevel.DEBUG, "%s read: %s", key, value);
        return value;
    }

    private static long readLong(@NotNull Bundle metadata, @NotNull ILogger logger, @NotNull String key, long defaultValue) {
        long value = metadata.getInt(key, (int) defaultValue);
        logger.log(SentryLevel.DEBUG, "%s read: %s", key, Long.valueOf(value));
        return value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAutoInit(@NotNull Context context, @NotNull ILogger logger) {
        Objects.requireNonNull(context, "The application context is required.");
        boolean autoInit = true;
        try {
            Bundle metadata = getMetadata(context);
            if (metadata != null) {
                autoInit = readBool(metadata, logger, AUTO_INIT, true);
            }
            logger.log(SentryLevel.INFO, "Retrieving auto-init from AndroidManifest.xml", new Object[0]);
        } catch (Throwable e) {
            logger.log(SentryLevel.ERROR, "Failed to read auto-init from android manifest metadata.", e);
        }
        return autoInit;
    }

    @Nullable
    private static Bundle getMetadata(@NotNull Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        return app.metaData;
    }
}
