package io.sentry.android.core;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import com.xiaopeng.libtheme.ThemeManager;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.SentryLevel;
import io.sentry.cache.EnvelopeCache;
import io.sentry.transport.CurrentDateProvider;
import io.sentry.transport.ICurrentDateProvider;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class LifecycleWatcher implements DefaultLifecycleObserver {
    @NotNull
    private final ICurrentDateProvider currentDateProvider;
    private final boolean enableAppLifecycleBreadcrumbs;
    private final boolean enableSessionTracking;
    @NotNull
    private final IHub hub;
    private final AtomicLong lastUpdatedSession;
    @NotNull
    private final AtomicBoolean runningSession;
    private final long sessionIntervalMillis;
    @NotNull
    private final Timer timer;
    @Nullable
    private TimerTask timerTask;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LifecycleWatcher(@NotNull IHub hub, long sessionIntervalMillis, boolean enableSessionTracking, boolean enableAppLifecycleBreadcrumbs) {
        this(hub, sessionIntervalMillis, enableSessionTracking, enableAppLifecycleBreadcrumbs, CurrentDateProvider.getInstance());
    }

    LifecycleWatcher(@NotNull IHub hub, long sessionIntervalMillis, boolean enableSessionTracking, boolean enableAppLifecycleBreadcrumbs, @NotNull ICurrentDateProvider currentDateProvider) {
        this.lastUpdatedSession = new AtomicLong(0L);
        this.timer = new Timer(true);
        this.runningSession = new AtomicBoolean();
        this.sessionIntervalMillis = sessionIntervalMillis;
        this.enableSessionTracking = enableSessionTracking;
        this.enableAppLifecycleBreadcrumbs = enableAppLifecycleBreadcrumbs;
        this.hub = hub;
        this.currentDateProvider = currentDateProvider;
    }

    public void onStart(@NotNull LifecycleOwner owner) {
        startSession();
        addAppBreadcrumb(ThemeManager.AttributeSet.FOREGROUND);
    }

    private void startSession() {
        if (this.enableSessionTracking) {
            cancelTask();
            long currentTimeMillis = this.currentDateProvider.getCurrentTimeMillis();
            long lastUpdatedSession = this.lastUpdatedSession.get();
            if (lastUpdatedSession == 0 || this.sessionIntervalMillis + lastUpdatedSession <= currentTimeMillis) {
                addSessionBreadcrumb(AccountConfig.FaceIDRegisterAction.STATUS_START);
                this.hub.startSession();
                this.runningSession.set(true);
            }
            this.lastUpdatedSession.set(currentTimeMillis);
        }
    }

    public void onStop(@NotNull LifecycleOwner owner) {
        if (this.enableSessionTracking) {
            long currentTimeMillis = this.currentDateProvider.getCurrentTimeMillis();
            this.lastUpdatedSession.set(currentTimeMillis);
            scheduleEndSession();
        }
        addAppBreadcrumb(ThemeManager.AttributeSet.BACKGROUND);
    }

    private void scheduleEndSession() {
        cancelTask();
        this.timerTask = new TimerTask() { // from class: io.sentry.android.core.LifecycleWatcher.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                LifecycleWatcher.this.addSessionBreadcrumb("end");
                LifecycleWatcher.this.hub.endSession();
                LifecycleWatcher.this.runningSession.set(false);
            }
        };
        this.timer.schedule(this.timerTask, this.sessionIntervalMillis);
    }

    private void cancelTask() {
        TimerTask timerTask = this.timerTask;
        if (timerTask != null) {
            timerTask.cancel();
            this.timerTask = null;
        }
    }

    private void addAppBreadcrumb(@NotNull String state) {
        if (this.enableAppLifecycleBreadcrumbs) {
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setType(NotificationCompat.CATEGORY_NAVIGATION);
            breadcrumb.setData("state", state);
            breadcrumb.setCategory("app.lifecycle");
            breadcrumb.setLevel(SentryLevel.INFO);
            this.hub.addBreadcrumb(breadcrumb);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSessionBreadcrumb(@NotNull String state) {
        Breadcrumb breadcrumb = new Breadcrumb();
        breadcrumb.setType(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE);
        breadcrumb.setData("state", state);
        breadcrumb.setCategory("app.lifecycle");
        breadcrumb.setLevel(SentryLevel.INFO);
        this.hub.addBreadcrumb(breadcrumb);
    }

    @TestOnly
    @NotNull
    AtomicBoolean isRunningSession() {
        return this.runningSession;
    }

    @TestOnly
    @Nullable
    TimerTask getTimerTask() {
        return this.timerTask;
    }
}
