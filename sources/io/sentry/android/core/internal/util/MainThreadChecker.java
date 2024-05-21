package io.sentry.android.core.internal.util;

import android.os.Looper;
import io.sentry.protocol.SentryThread;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class MainThreadChecker {
    private MainThreadChecker() {
    }

    public static boolean isMainThread(@NotNull Thread thread) {
        return isMainThread(thread.getId());
    }

    public static boolean isMainThread() {
        return isMainThread(Thread.currentThread());
    }

    public static boolean isMainThread(@NotNull SentryThread sentryThread) {
        Long threadId = sentryThread.getId();
        return threadId != null && isMainThread(threadId.longValue());
    }

    private static boolean isMainThread(long threadId) {
        return Looper.getMainLooper().getThread().getId() == threadId;
    }
}
