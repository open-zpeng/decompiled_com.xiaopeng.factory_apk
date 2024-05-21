package io.sentry.android.core;

import android.util.Log;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class AndroidLogger implements ILogger {
    private static final String tag = "Sentry";

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Object... args) {
        Log.println(toLogcatLevel(level), tag, String.format(message, args));
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @Nullable Throwable throwable, @NotNull String message, @Nullable Object... args) {
        log(level, String.format(message, args), throwable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.sentry.android.core.AndroidLogger$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$sentry$SentryLevel = new int[SentryLevel.values().length];

        static {
            try {
                $SwitchMap$io$sentry$SentryLevel[SentryLevel.INFO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$io$sentry$SentryLevel[SentryLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$io$sentry$SentryLevel[SentryLevel.ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$io$sentry$SentryLevel[SentryLevel.FATAL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$io$sentry$SentryLevel[SentryLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    @Override // io.sentry.ILogger
    public void log(@NotNull SentryLevel level, @NotNull String message, @Nullable Throwable throwable) {
        int i = AnonymousClass1.$SwitchMap$io$sentry$SentryLevel[level.ordinal()];
        if (i == 1) {
            Log.i(tag, message, throwable);
        } else if (i == 2) {
            Log.w(tag, message, throwable);
        } else if (i == 3) {
            Log.e(tag, message, throwable);
        } else if (i == 4) {
            Log.wtf(tag, message, throwable);
        } else {
            Log.d(tag, message, throwable);
        }
    }

    @Override // io.sentry.ILogger
    public boolean isEnabled(@Nullable SentryLevel level) {
        return true;
    }

    private int toLogcatLevel(@NotNull SentryLevel sentryLevel) {
        int i = AnonymousClass1.$SwitchMap$io$sentry$SentryLevel[sentryLevel.ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i == 4) {
                    return 7;
                }
                return 3;
            }
            return 5;
        }
        return 4;
    }
}
