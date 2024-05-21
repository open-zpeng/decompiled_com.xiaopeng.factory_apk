package io.sentry.android.core;

import android.content.Context;
import android.os.SystemClock;
import io.sentry.DateUtils;
import io.sentry.ILogger;
import io.sentry.Integration;
import io.sentry.OptionsContainer;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.android.fragment.FragmentLifecycleIntegration;
import io.sentry.android.timber.SentryTimberIntegration;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SentryAndroid {
    static final String SENTRY_FRAGMENT_INTEGRATION_CLASS_NAME = "io.sentry.android.fragment.FragmentLifecycleIntegration";
    static final String SENTRY_TIMBER_INTEGRATION_CLASS_NAME = "io.sentry.android.timber.SentryTimberIntegration";
    @NotNull
    private static final Date appStartTime = DateUtils.getCurrentDateTime();
    private static final long appStart = SystemClock.uptimeMillis();

    private SentryAndroid() {
    }

    public static void init(@NotNull Context context) {
        init(context, new AndroidLogger());
    }

    public static void init(@NotNull Context context, @NotNull ILogger logger) {
        init(context, logger, new Sentry.OptionsConfiguration() { // from class: io.sentry.android.core.-$$Lambda$SentryAndroid$1tDpu4BrZ9ZUeZ0tE52kgN3Zx10
            @Override // io.sentry.Sentry.OptionsConfiguration
            public final void configure(SentryOptions sentryOptions) {
                SentryAndroid.lambda$init$0((SentryAndroidOptions) sentryOptions);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$init$0(SentryAndroidOptions options) {
    }

    public static void init(@NotNull Context context, @NotNull Sentry.OptionsConfiguration<SentryAndroidOptions> configuration) {
        init(context, new AndroidLogger(), configuration);
    }

    public static synchronized void init(@NotNull final Context context, @NotNull final ILogger logger, @NotNull final Sentry.OptionsConfiguration<SentryAndroidOptions> configuration) {
        synchronized (SentryAndroid.class) {
            AppStartState.getInstance().setAppStartTime(appStart, appStartTime);
            try {
                try {
                    try {
                        try {
                            Sentry.init(OptionsContainer.create(SentryAndroidOptions.class), new Sentry.OptionsConfiguration() { // from class: io.sentry.android.core.-$$Lambda$SentryAndroid$g99QaqNwcEFxja-3CMUNWZ36_g4
                                @Override // io.sentry.Sentry.OptionsConfiguration
                                public final void configure(SentryOptions sentryOptions) {
                                    SentryAndroid.lambda$init$1(context, logger, configuration, (SentryAndroidOptions) sentryOptions);
                                }
                            }, true);
                        } catch (IllegalAccessException e) {
                            logger.log(SentryLevel.FATAL, "Fatal error during SentryAndroid.init(...)", e);
                            throw new RuntimeException("Failed to initialize Sentry's SDK", e);
                        }
                    } catch (InstantiationException e2) {
                        logger.log(SentryLevel.FATAL, "Fatal error during SentryAndroid.init(...)", e2);
                        throw new RuntimeException("Failed to initialize Sentry's SDK", e2);
                    }
                } catch (InvocationTargetException e3) {
                    logger.log(SentryLevel.FATAL, "Fatal error during SentryAndroid.init(...)", e3);
                    throw new RuntimeException("Failed to initialize Sentry's SDK", e3);
                }
            } catch (NoSuchMethodException e4) {
                logger.log(SentryLevel.FATAL, "Fatal error during SentryAndroid.init(...)", e4);
                throw new RuntimeException("Failed to initialize Sentry's SDK", e4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$init$1(Context context, ILogger logger, Sentry.OptionsConfiguration configuration, SentryAndroidOptions options) {
        LoadClass classLoader = new LoadClass();
        boolean isFragmentAvailable = classLoader.isClassAvailable(SENTRY_FRAGMENT_INTEGRATION_CLASS_NAME, options);
        boolean isTimberAvailable = classLoader.isClassAvailable(SENTRY_TIMBER_INTEGRATION_CLASS_NAME, options);
        AndroidOptionsInitializer.init(options, context, logger, isFragmentAvailable, isTimberAvailable);
        configuration.configure(options);
        deduplicateIntegrations(options, isFragmentAvailable, isTimberAvailable);
    }

    private static void deduplicateIntegrations(@NotNull SentryOptions options, boolean isFragmentAvailable, boolean isTimberAvailable) {
        List<Integration> timberIntegrations = new ArrayList<>();
        List<Integration> fragmentIntegrations = new ArrayList<>();
        for (Integration integration : options.getIntegrations()) {
            if (isFragmentAvailable && (integration instanceof FragmentLifecycleIntegration)) {
                fragmentIntegrations.add(integration);
            }
            if (isTimberAvailable && (integration instanceof SentryTimberIntegration)) {
                timberIntegrations.add(integration);
            }
        }
        if (fragmentIntegrations.size() > 1) {
            for (int i = 0; i < fragmentIntegrations.size() - 1; i++) {
                Integration integration2 = fragmentIntegrations.get(i);
                options.getIntegrations().remove(integration2);
            }
        }
        int i2 = timberIntegrations.size();
        if (i2 > 1) {
            for (int i3 = 0; i3 < timberIntegrations.size() - 1; i3++) {
                Integration integration3 = timberIntegrations.get(i3);
                options.getIntegrations().remove(integration3);
            }
        }
    }
}
