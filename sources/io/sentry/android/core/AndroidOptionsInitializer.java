package io.sentry.android.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import io.sentry.ILogger;
import io.sentry.SendCachedEnvelopeFireAndForgetIntegration;
import io.sentry.SendFireAndForgetEnvelopeSender;
import io.sentry.SendFireAndForgetOutboxSender;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.fragment.FragmentLifecycleIntegration;
import io.sentry.android.timber.SentryTimberIntegration;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class AndroidOptionsInitializer {
    private AndroidOptionsInitializer() {
    }

    static void init(@NotNull SentryAndroidOptions options, @NotNull Context context) {
        Objects.requireNonNull(context, "The application context is required.");
        Objects.requireNonNull(options, "The options object is required.");
        init(options, context, new AndroidLogger(), false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void init(@NotNull SentryAndroidOptions options, @NotNull Context context, @NotNull ILogger logger, boolean isFragmentAvailable, boolean isTimberAvailable) {
        init(options, context, logger, new BuildInfoProvider(), isFragmentAvailable, isTimberAvailable);
    }

    static void init(@NotNull SentryAndroidOptions options, @NotNull Context context, @NotNull ILogger logger, @NotNull IBuildInfoProvider buildInfoProvider, boolean isFragmentAvailable, boolean isTimberAvailable) {
        init(options, context, logger, buildInfoProvider, new LoadClass(), isFragmentAvailable, isTimberAvailable);
    }

    static void init(@NotNull SentryAndroidOptions options, @NotNull Context context, @NotNull ILogger logger, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull LoadClass loadClass, boolean isFragmentAvailable, boolean isTimberAvailable) {
        Objects.requireNonNull(context, "The context is required.");
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        Objects.requireNonNull(options, "The options object is required.");
        Objects.requireNonNull(logger, "The ILogger object is required.");
        options.setLogger(logger);
        ManifestMetadataReader.applyMetadata(context, options);
        initializeCacheDirs(context, options);
        ActivityFramesTracker activityFramesTracker = new ActivityFramesTracker(loadClass, options.getLogger());
        installDefaultIntegrations(context, options, buildInfoProvider, loadClass, activityFramesTracker, isFragmentAvailable, isTimberAvailable);
        readDefaultOptionValues(options, context);
        options.addEventProcessor(new DefaultAndroidEventProcessor(context, logger, buildInfoProvider));
        options.addEventProcessor(new PerformanceAndroidEventProcessor(options, activityFramesTracker));
        options.setTransportGate(new AndroidTransportGate(context, options.getLogger()));
    }

    private static void installDefaultIntegrations(@NotNull Context context, @NotNull final SentryOptions options, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull LoadClass loadClass, @NotNull ActivityFramesTracker activityFramesTracker, boolean isFragmentAvailable, boolean isTimberAvailable) {
        Class<?> sentryNdkClass;
        options.addIntegration(new SendCachedEnvelopeFireAndForgetIntegration(new SendFireAndForgetEnvelopeSender(new SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath() { // from class: io.sentry.android.core.-$$Lambda$AndroidOptionsInitializer$kM-qsbd50H1L5pqZvtR3lA02sgk
            @Override // io.sentry.SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath
            public final String getDirPath() {
                String cacheDirPath;
                cacheDirPath = SentryOptions.this.getCacheDirPath();
                return cacheDirPath;
            }
        })));
        if (isNdkAvailable(buildInfoProvider)) {
            sentryNdkClass = loadClass.loadClass(NdkIntegration.SENTRY_NDK_CLASS_NAME, options.getLogger());
        } else {
            sentryNdkClass = null;
        }
        options.addIntegration(new NdkIntegration(sentryNdkClass));
        options.addIntegration(EnvelopeFileObserverIntegration.getOutboxFileObserver());
        options.addIntegration(new SendCachedEnvelopeFireAndForgetIntegration(new SendFireAndForgetOutboxSender(new SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath() { // from class: io.sentry.android.core.-$$Lambda$AndroidOptionsInitializer$D4zvv_NA_KabqNWy8rp5CCc_RTM
            @Override // io.sentry.SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath
            public final String getDirPath() {
                String outboxPath;
                outboxPath = SentryOptions.this.getOutboxPath();
                return outboxPath;
            }
        })));
        options.addIntegration(new AnrIntegration(context));
        options.addIntegration(new AppLifecycleIntegration());
        if (context instanceof Application) {
            options.addIntegration(new ActivityLifecycleIntegration((Application) context, buildInfoProvider, activityFramesTracker));
            options.addIntegration(new UserInteractionIntegration((Application) context, loadClass));
            if (isFragmentAvailable) {
                options.addIntegration(new FragmentLifecycleIntegration((Application) context, true, true));
            }
        } else {
            options.getLogger().log(SentryLevel.WARNING, "ActivityLifecycle, FragmentLifecycle and UserInteraction Integrations need an Application class to be installed.", new Object[0]);
        }
        if (isTimberAvailable) {
            options.addIntegration(new SentryTimberIntegration());
        }
        options.addIntegration(new AppComponentsBreadcrumbsIntegration(context));
        options.addIntegration(new SystemEventsBreadcrumbsIntegration(context));
        options.addIntegration(new TempSensorBreadcrumbsIntegration(context));
        options.addIntegration(new PhoneStateBreadcrumbsIntegration(context));
    }

    private static void readDefaultOptionValues(@NotNull SentryAndroidOptions options, @NotNull Context context) {
        PackageInfo packageInfo = ContextUtils.getPackageInfo(context, options.getLogger());
        if (packageInfo != null) {
            if (options.getRelease() == null) {
                options.setRelease(getSentryReleaseVersion(packageInfo, ContextUtils.getVersionCode(packageInfo)));
            }
            String packageName = packageInfo.packageName;
            if (packageName != null && !packageName.startsWith("android.")) {
                options.addInAppInclude(packageName);
            }
        }
        if (options.getDistinctId() == null) {
            try {
                options.setDistinctId(Installation.id(context));
            } catch (RuntimeException e) {
                options.getLogger().log(SentryLevel.ERROR, "Could not generate distinct Id.", e);
            }
        }
        if (options.getProguardUuid() == null) {
            options.setProguardUuid(getProguardUUID(context, options.getLogger()));
        }
    }

    @Nullable
    private static String getProguardUUID(@NotNull Context context, @NotNull ILogger logger) {
        AssetManager assets = context.getAssets();
        try {
            InputStream is = new BufferedInputStream(assets.open("sentry-debug-meta.properties"));
            try {
                Properties properties = new Properties();
                properties.load(is);
                String uuid = properties.getProperty("io.sentry.ProguardUuids");
                logger.log(SentryLevel.DEBUG, "Proguard UUID found: %s", uuid);
                is.close();
                return uuid;
            } catch (Throwable th) {
                try {
                    is.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (FileNotFoundException e) {
            logger.log(SentryLevel.INFO, "sentry-debug-meta.properties file was not found.", new Object[0]);
            return null;
        } catch (IOException e2) {
            logger.log(SentryLevel.ERROR, "Error getting Proguard UUIDs.", e2);
            return null;
        } catch (RuntimeException e3) {
            logger.log(SentryLevel.ERROR, "sentry-debug-meta.properties file is malformed.", e3);
            return null;
        }
    }

    @NotNull
    private static String getSentryReleaseVersion(@NotNull PackageInfo packageInfo, @NotNull String versionCode) {
        return packageInfo.packageName + "@" + packageInfo.versionName + "+" + versionCode;
    }

    private static void initializeCacheDirs(@NotNull Context context, @NotNull SentryOptions options) {
        File cacheDir = new File(context.getCacheDir(), "sentry");
        options.setCacheDirPath(cacheDir.getAbsolutePath());
    }

    private static boolean isNdkAvailable(@NotNull IBuildInfoProvider buildInfoProvider) {
        return buildInfoProvider.getSdkInfoVersion() >= 16;
    }
}
