package io.sentry;

import java.lang.Thread;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
interface UncaughtExceptionHandler {
    Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler();

    void setDefaultUncaughtExceptionHandler(@Nullable Thread.UncaughtExceptionHandler uncaughtExceptionHandler);

    /* loaded from: classes2.dex */
    public static final class Adapter implements UncaughtExceptionHandler {
        private static final Adapter INSTANCE = new Adapter();

        /* JADX INFO: Access modifiers changed from: package-private */
        public static UncaughtExceptionHandler getInstance() {
            return INSTANCE;
        }

        private Adapter() {
        }

        @Override // io.sentry.UncaughtExceptionHandler
        public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
            return Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override // io.sentry.UncaughtExceptionHandler
        public void setDefaultUncaughtExceptionHandler(@Nullable Thread.UncaughtExceptionHandler handler) {
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }
    }
}
