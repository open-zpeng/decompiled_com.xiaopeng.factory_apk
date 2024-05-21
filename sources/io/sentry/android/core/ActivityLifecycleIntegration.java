package io.sentry.android.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import androidx.core.app.NotificationCompat;
import io.sentry.Breadcrumb;
import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.ITransaction;
import io.sentry.Integration;
import io.sentry.Scope;
import io.sentry.ScopeCallback;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SpanStatus;
import io.sentry.TransactionFinishedCallback;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
public final class ActivityLifecycleIntegration implements Integration, Closeable, Application.ActivityLifecycleCallbacks {
    static final String APP_START_COLD = "app.start.cold";
    static final String APP_START_WARM = "app.start.warm";
    static final String UI_LOAD_OP = "ui.load";
    @NotNull
    private final ActivityFramesTracker activityFramesTracker;
    @Nullable
    private ISpan appStartSpan;
    @NotNull
    private final Application application;
    private boolean foregroundImportance;
    @Nullable
    private IHub hub;
    private boolean isAllActivityCallbacksAvailable;
    @Nullable
    private SentryAndroidOptions options;
    private boolean performanceEnabled = false;
    private boolean firstActivityCreated = false;
    private boolean firstActivityResumed = false;
    @NotNull
    private final WeakHashMap<Activity, ITransaction> activitiesWithOngoingTransactions = new WeakHashMap<>();

    public ActivityLifecycleIntegration(@NotNull Application application, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull ActivityFramesTracker activityFramesTracker) {
        this.foregroundImportance = false;
        this.application = (Application) Objects.requireNonNull(application, "Application is required");
        Objects.requireNonNull(buildInfoProvider, "BuildInfoProvider is required");
        this.activityFramesTracker = (ActivityFramesTracker) Objects.requireNonNull(activityFramesTracker, "ActivityFramesTracker is required");
        if (buildInfoProvider.getSdkInfoVersion() >= 29) {
            this.isAllActivityCallbacksAvailable = true;
        }
        this.foregroundImportance = isForegroundImportance(this.application);
    }

    @Override // io.sentry.Integration
    public void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required");
        this.options.getLogger().log(SentryLevel.DEBUG, "ActivityLifecycleIntegration enabled: %s", Boolean.valueOf(this.options.isEnableActivityLifecycleBreadcrumbs()));
        this.performanceEnabled = isPerformanceEnabled(this.options);
        if (this.options.isEnableActivityLifecycleBreadcrumbs() || this.performanceEnabled) {
            this.application.registerActivityLifecycleCallbacks(this);
            this.options.getLogger().log(SentryLevel.DEBUG, "ActivityLifecycleIntegration installed.", new Object[0]);
        }
    }

    private boolean isPerformanceEnabled(@NotNull SentryAndroidOptions options) {
        return options.isTracingEnabled() && options.isEnableAutoActivityLifecycleTracing();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.application.unregisterActivityLifecycleCallbacks(this);
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions != null) {
            sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "ActivityLifecycleIntegration removed.", new Object[0]);
        }
        this.activityFramesTracker.stop();
    }

    private void addBreadcrumb(@NotNull Activity activity, @NotNull String state) {
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions != null && this.hub != null && sentryAndroidOptions.isEnableActivityLifecycleBreadcrumbs()) {
            Breadcrumb breadcrumb = new Breadcrumb();
            breadcrumb.setType(NotificationCompat.CATEGORY_NAVIGATION);
            breadcrumb.setData("state", state);
            breadcrumb.setData("screen", getActivityName(activity));
            breadcrumb.setCategory("ui.lifecycle");
            breadcrumb.setLevel(SentryLevel.INFO);
            this.hub.addBreadcrumb(breadcrumb);
        }
    }

    @NotNull
    private String getActivityName(@NotNull Activity activity) {
        return activity.getClass().getSimpleName();
    }

    private void stopPreviousTransactions() {
        for (Map.Entry<Activity, ITransaction> entry : this.activitiesWithOngoingTransactions.entrySet()) {
            ITransaction transaction = entry.getValue();
            finishTransaction(transaction);
        }
    }

    private void startTracing(@NotNull final Activity activity) {
        final ITransaction transaction;
        if (this.performanceEnabled && !isRunningTransaction(activity) && this.hub != null) {
            stopPreviousTransactions();
            String activityName = getActivityName(activity);
            Date appStartTime = this.foregroundImportance ? AppStartState.getInstance().getAppStartTime() : null;
            Boolean coldStart = AppStartState.getInstance().isColdStart();
            if (this.firstActivityCreated || appStartTime == null || coldStart == null) {
                transaction = this.hub.startTransaction(activityName, UI_LOAD_OP, null, true, new TransactionFinishedCallback() { // from class: io.sentry.android.core.-$$Lambda$ActivityLifecycleIntegration$4nPJTfJPmYicrRoHyRXR7SUDAok
                    @Override // io.sentry.TransactionFinishedCallback
                    public final void execute(ITransaction iTransaction) {
                        ActivityLifecycleIntegration.this.lambda$startTracing$0$ActivityLifecycleIntegration(activity, iTransaction);
                    }
                });
            } else {
                transaction = this.hub.startTransaction(activityName, UI_LOAD_OP, appStartTime, true, new TransactionFinishedCallback() { // from class: io.sentry.android.core.-$$Lambda$ActivityLifecycleIntegration$1Xa20D8rbI_RfOTCST8-8mCjmDU
                    @Override // io.sentry.TransactionFinishedCallback
                    public final void execute(ITransaction iTransaction) {
                        ActivityLifecycleIntegration.this.lambda$startTracing$1$ActivityLifecycleIntegration(activity, iTransaction);
                    }
                });
                this.appStartSpan = transaction.startChild(getAppStartOp(coldStart.booleanValue()), getAppStartDesc(coldStart.booleanValue()), appStartTime);
            }
            this.hub.configureScope(new ScopeCallback() { // from class: io.sentry.android.core.-$$Lambda$ActivityLifecycleIntegration$m5wK_FFyhcFBSSy1Y5wE3bEmRwg
                @Override // io.sentry.ScopeCallback
                public final void run(Scope scope) {
                    ActivityLifecycleIntegration.this.lambda$startTracing$2$ActivityLifecycleIntegration(transaction, scope);
                }
            });
            this.activitiesWithOngoingTransactions.put(activity, transaction);
        }
    }

    public /* synthetic */ void lambda$startTracing$0$ActivityLifecycleIntegration(Activity activity, ITransaction finishingTransaction) {
        this.activityFramesTracker.setMetrics(activity, finishingTransaction.getEventId());
    }

    public /* synthetic */ void lambda$startTracing$1$ActivityLifecycleIntegration(Activity activity, ITransaction finishingTransaction) {
        this.activityFramesTracker.setMetrics(activity, finishingTransaction.getEventId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* renamed from: applyScope */
    public void lambda$startTracing$2$ActivityLifecycleIntegration(@NotNull final Scope scope, @NotNull final ITransaction transaction) {
        scope.withTransaction(new Scope.IWithTransaction() { // from class: io.sentry.android.core.-$$Lambda$ActivityLifecycleIntegration$6fT25agdPIZfrwWfpumYS-6eQKQ
            @Override // io.sentry.Scope.IWithTransaction
            public final void accept(ITransaction iTransaction) {
                ActivityLifecycleIntegration.this.lambda$applyScope$3$ActivityLifecycleIntegration(scope, transaction, iTransaction);
            }
        });
    }

    public /* synthetic */ void lambda$applyScope$3$ActivityLifecycleIntegration(Scope scope, ITransaction transaction, ITransaction scopeTransaction) {
        if (scopeTransaction == null) {
            scope.setTransaction(transaction);
            return;
        }
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions != null) {
            sentryAndroidOptions.getLogger().log(SentryLevel.DEBUG, "Transaction '%s' won't be bound to the Scope since there's one already in there.", transaction.getName());
        }
    }

    private boolean isRunningTransaction(@NotNull Activity activity) {
        return this.activitiesWithOngoingTransactions.containsKey(activity);
    }

    private void stopTracing(@NotNull Activity activity, boolean shouldFinishTracing) {
        if (this.performanceEnabled && shouldFinishTracing) {
            ITransaction transaction = this.activitiesWithOngoingTransactions.get(activity);
            finishTransaction(transaction);
        }
    }

    private void finishTransaction(@Nullable ITransaction transaction) {
        if (transaction == null || transaction.isFinished()) {
            return;
        }
        SpanStatus status = transaction.getStatus();
        if (status == null) {
            status = SpanStatus.OK;
        }
        transaction.finish(status);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityCreated(@NotNull Activity activity, @Nullable Bundle savedInstanceState) {
        setColdStart(savedInstanceState);
        addBreadcrumb(activity, "created");
        startTracing(activity);
        this.firstActivityCreated = true;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityStarted(@NotNull Activity activity) {
        this.activityFramesTracker.addActivity(activity);
        addBreadcrumb(activity, "started");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityResumed(@NotNull Activity activity) {
        if (!this.firstActivityResumed) {
            if (this.foregroundImportance) {
                AppStartState.getInstance().setAppStartEnd();
            } else if (this.options != null) {
                this.options.getLogger().log(SentryLevel.DEBUG, "App Start won't be reported because Process wasn't of foregroundImportance.", new Object[0]);
            }
            if (this.performanceEnabled && this.appStartSpan != null) {
                this.appStartSpan.finish();
            }
            this.firstActivityResumed = true;
        }
        addBreadcrumb(activity, "resumed");
        if (!this.isAllActivityCallbacksAvailable && this.options != null) {
            stopTracing(activity, this.options.isEnableActivityLifecycleTracingAutoFinish());
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityPostResumed(@NotNull Activity activity) {
        if (this.isAllActivityCallbacksAvailable && this.options != null) {
            stopTracing(activity, this.options.isEnableActivityLifecycleTracingAutoFinish());
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityPaused(@NotNull Activity activity) {
        addBreadcrumb(activity, "paused");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityStopped(@NotNull Activity activity) {
        addBreadcrumb(activity, "stopped");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {
        addBreadcrumb(activity, "saveInstanceState");
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public synchronized void onActivityDestroyed(@NotNull Activity activity) {
        addBreadcrumb(activity, "destroyed");
        if (this.appStartSpan != null && !this.appStartSpan.isFinished()) {
            this.appStartSpan.finish(SpanStatus.CANCELLED);
        }
        stopTracing(activity, true);
        this.appStartSpan = null;
        if (this.performanceEnabled) {
            this.activitiesWithOngoingTransactions.remove(activity);
        }
    }

    @TestOnly
    @NotNull
    WeakHashMap<Activity, ITransaction> getActivitiesWithOngoingTransactions() {
        return this.activitiesWithOngoingTransactions;
    }

    @TestOnly
    @Nullable
    ISpan getAppStartSpan() {
        return this.appStartSpan;
    }

    private void setColdStart(@Nullable Bundle savedInstanceState) {
        if (!this.firstActivityCreated) {
            AppStartState.getInstance().setColdStart(savedInstanceState == null);
        }
    }

    @NotNull
    private String getAppStartDesc(boolean coldStart) {
        if (coldStart) {
            return "Cold Start";
        }
        return "Warm Start";
    }

    @NotNull
    private String getAppStartOp(boolean coldStart) {
        if (coldStart) {
            return APP_START_COLD;
        }
        return APP_START_WARM;
    }

    private boolean isForegroundImportance(@NotNull Context context) {
        try {
            Object service = context.getSystemService("activity");
            if (service instanceof ActivityManager) {
                ActivityManager activityManager = (ActivityManager) service;
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    int myPid = Process.myPid();
                    for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
                        if (processInfo.pid == myPid) {
                            if (processInfo.importance == 100) {
                                return true;
                            }
                            return false;
                        }
                    }
                    return false;
                }
                return false;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }
}
