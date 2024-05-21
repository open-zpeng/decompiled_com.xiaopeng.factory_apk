package io.sentry.android.core;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class LoadClass {
    @Nullable
    public Class<?> loadClass(@NotNull String clazz, @Nullable ILogger logger) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            if (logger != null) {
                SentryLevel sentryLevel = SentryLevel.DEBUG;
                logger.log(sentryLevel, "Class not available:" + clazz, e);
                return null;
            }
            return null;
        } catch (UnsatisfiedLinkError e2) {
            if (logger != null) {
                SentryLevel sentryLevel2 = SentryLevel.ERROR;
                logger.log(sentryLevel2, "Failed to load (UnsatisfiedLinkError) " + clazz, e2);
                return null;
            }
            return null;
        } catch (Throwable e3) {
            if (logger != null) {
                SentryLevel sentryLevel3 = SentryLevel.ERROR;
                logger.log(sentryLevel3, "Failed to initialize " + clazz, e3);
                return null;
            }
            return null;
        }
    }

    public boolean isClassAvailable(@NotNull String clazz, @Nullable ILogger logger) {
        return loadClass(clazz, logger) != null;
    }

    public boolean isClassAvailable(@NotNull String clazz, @Nullable SentryOptions options) {
        return isClassAvailable(clazz, options != null ? options.getLogger() : null);
    }
}
