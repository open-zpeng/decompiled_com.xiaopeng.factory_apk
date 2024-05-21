package io.sentry.android.core;

import android.os.SystemClock;
import java.util.Date;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class AppStartState {
    @NotNull
    private static AppStartState instance = new AppStartState();
    @Nullable
    private Long appStartEndMillis;
    @Nullable
    private Long appStartMillis;
    @Nullable
    private Date appStartTime;
    @Nullable
    private Boolean coldStart = null;

    private AppStartState() {
    }

    @NotNull
    public static AppStartState getInstance() {
        return instance;
    }

    @TestOnly
    void resetInstance() {
        instance = new AppStartState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAppStartEnd() {
        setAppStartEnd(SystemClock.uptimeMillis());
    }

    @TestOnly
    void setAppStartEnd(long appStartEndMillis) {
        this.appStartEndMillis = Long.valueOf(appStartEndMillis);
    }

    @Nullable
    public synchronized Long getAppStartInterval() {
        if (this.appStartMillis != null && this.appStartEndMillis != null && this.coldStart != null) {
            return Long.valueOf(this.appStartEndMillis.longValue() - this.appStartMillis.longValue());
        }
        return null;
    }

    @Nullable
    public Boolean isColdStart() {
        return this.coldStart;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setColdStart(boolean coldStart) {
        if (this.coldStart != null) {
            return;
        }
        this.coldStart = Boolean.valueOf(coldStart);
    }

    @Nullable
    public Date getAppStartTime() {
        return this.appStartTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAppStartTime(long appStartMillis, @NotNull Date appStartTime) {
        if (this.appStartTime == null || this.appStartMillis == null) {
            this.appStartTime = appStartTime;
            this.appStartMillis = Long.valueOf(appStartMillis);
        }
    }
}
