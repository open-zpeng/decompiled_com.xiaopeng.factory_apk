package io.sentry.android.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class ANRWatchDog extends Thread {
    private final ANRListener anrListener;
    @NotNull
    private final Context context;
    @NotNull
    private final ILogger logger;
    private final boolean reportInDebug;
    private final AtomicBoolean reported;
    private final AtomicLong tick;
    private final Runnable ticker;
    private final long timeoutIntervalMillis;
    private final IHandler uiHandler;

    /* loaded from: classes2.dex */
    public interface ANRListener {
        void onAppNotResponding(@NotNull ApplicationNotResponding applicationNotResponding);
    }

    public /* synthetic */ void lambda$new$0$ANRWatchDog() {
        this.tick.set(0L);
        this.reported.set(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ANRWatchDog(long timeoutIntervalMillis, boolean reportInDebug, @NotNull ANRListener listener, @NotNull ILogger logger, @NotNull Context context) {
        this(timeoutIntervalMillis, reportInDebug, listener, logger, new MainLooperHandler(), context);
    }

    @TestOnly
    ANRWatchDog(long timeoutIntervalMillis, boolean reportInDebug, @NotNull ANRListener listener, @NotNull ILogger logger, @NotNull IHandler uiHandler, @NotNull Context context) {
        this.tick = new AtomicLong(0L);
        this.reported = new AtomicBoolean(false);
        this.ticker = new Runnable() { // from class: io.sentry.android.core.-$$Lambda$ANRWatchDog$UchzNuU9k3vxaQe_dptU1R-eol8
            @Override // java.lang.Runnable
            public final void run() {
                ANRWatchDog.this.lambda$new$0$ANRWatchDog();
            }
        };
        this.reportInDebug = reportInDebug;
        this.anrListener = listener;
        this.timeoutIntervalMillis = timeoutIntervalMillis;
        this.logger = logger;
        this.uiHandler = uiHandler;
        this.context = context;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        setName("|ANR-WatchDog|");
        long interval = this.timeoutIntervalMillis;
        while (!isInterrupted()) {
            boolean needPost = this.tick.get() == 0;
            this.tick.addAndGet(interval);
            if (needPost) {
                this.uiHandler.post(this.ticker);
            }
            try {
                Thread.sleep(interval);
                if (this.tick.get() != 0 && !this.reported.get()) {
                    if (!this.reportInDebug && (Debug.isDebuggerConnected() || Debug.waitingForDebugger())) {
                        this.logger.log(SentryLevel.DEBUG, "An ANR was detected but ignored because the debugger is connected.", new Object[0]);
                        this.reported.set(true);
                    } else {
                        ActivityManager am = (ActivityManager) this.context.getSystemService("activity");
                        if (am != null) {
                            List<ActivityManager.ProcessErrorStateInfo> processesInErrorState = am.getProcessesInErrorState();
                            if (processesInErrorState != null) {
                                boolean isAnr = false;
                                Iterator<ActivityManager.ProcessErrorStateInfo> it = processesInErrorState.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    ActivityManager.ProcessErrorStateInfo item = it.next();
                                    if (item.condition == 2) {
                                        isAnr = true;
                                        break;
                                    }
                                }
                                if (!isAnr) {
                                }
                            }
                        }
                        this.logger.log(SentryLevel.INFO, "Raising ANR", new Object[0]);
                        String message = "Application Not Responding for at least " + this.timeoutIntervalMillis + " ms.";
                        ApplicationNotResponding error = new ApplicationNotResponding(message, this.uiHandler.getThread());
                        this.anrListener.onAppNotResponding(error);
                        interval = this.timeoutIntervalMillis;
                        this.reported.set(true);
                    }
                }
            } catch (InterruptedException e) {
                try {
                    Thread.currentThread().interrupt();
                    this.logger.log(SentryLevel.WARNING, "Interrupted: %s", e.getMessage());
                    return;
                } catch (SecurityException e2) {
                    this.logger.log(SentryLevel.WARNING, "Failed to interrupt due to SecurityException: %s", e.getMessage());
                    return;
                }
            }
        }
    }
}
