package io.sentry.android.core;

import android.os.Handler;
import android.os.Looper;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
final class MainLooperHandler implements IHandler {
    @NotNull
    private final Handler handler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MainLooperHandler() {
        this(Looper.getMainLooper());
    }

    MainLooperHandler(@NotNull Looper looper) {
        this.handler = new Handler(looper);
    }

    @Override // io.sentry.android.core.IHandler
    public void post(@NotNull Runnable runnable) {
        this.handler.post(runnable);
    }

    @Override // io.sentry.android.core.IHandler
    @NotNull
    public Thread getThread() {
        return this.handler.getLooper().getThread();
    }
}
