package io.sentry.android.core;

import io.sentry.SentryOptions;
import io.sentry.protocol.SdkVersion;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SentryAndroidOptions extends SentryOptions {
    private boolean anrEnabled = true;
    private long anrTimeoutIntervalMillis = 5000;
    private boolean anrReportInDebug = false;
    private boolean enableActivityLifecycleBreadcrumbs = true;
    private boolean enableAppLifecycleBreadcrumbs = true;
    private boolean enableSystemEventBreadcrumbs = true;
    private boolean enableAppComponentBreadcrumbs = true;
    private boolean enableUserInteractionBreadcrumbs = true;
    private boolean enableAutoActivityLifecycleTracing = true;
    private boolean enableActivityLifecycleTracingAutoFinish = true;
    @NotNull
    private IDebugImagesLoader debugImagesLoader = NoOpDebugImagesLoader.getInstance();

    public SentryAndroidOptions() {
        setSentryClientName("sentry.java.android/5.7.3");
        setSdkVersion(createSdkVersion());
        setAttachServerName(false);
    }

    @NotNull
    private SdkVersion createSdkVersion() {
        SdkVersion sdkVersion = SdkVersion.updateSdkVersion(getSdkVersion(), BuildConfig.SENTRY_ANDROID_SDK_NAME, "5.7.3");
        sdkVersion.addPackage("maven:io.sentry:sentry-android-core", "5.7.3");
        return sdkVersion;
    }

    public boolean isAnrEnabled() {
        return this.anrEnabled;
    }

    public void setAnrEnabled(boolean anrEnabled) {
        this.anrEnabled = anrEnabled;
    }

    public long getAnrTimeoutIntervalMillis() {
        return this.anrTimeoutIntervalMillis;
    }

    public void setAnrTimeoutIntervalMillis(long anrTimeoutIntervalMillis) {
        this.anrTimeoutIntervalMillis = anrTimeoutIntervalMillis;
    }

    public boolean isAnrReportInDebug() {
        return this.anrReportInDebug;
    }

    public void setAnrReportInDebug(boolean anrReportInDebug) {
        this.anrReportInDebug = anrReportInDebug;
    }

    public boolean isEnableActivityLifecycleBreadcrumbs() {
        return this.enableActivityLifecycleBreadcrumbs;
    }

    public void setEnableActivityLifecycleBreadcrumbs(boolean enableActivityLifecycleBreadcrumbs) {
        this.enableActivityLifecycleBreadcrumbs = enableActivityLifecycleBreadcrumbs;
    }

    public boolean isEnableAppLifecycleBreadcrumbs() {
        return this.enableAppLifecycleBreadcrumbs;
    }

    public void setEnableAppLifecycleBreadcrumbs(boolean enableAppLifecycleBreadcrumbs) {
        this.enableAppLifecycleBreadcrumbs = enableAppLifecycleBreadcrumbs;
    }

    public boolean isEnableSystemEventBreadcrumbs() {
        return this.enableSystemEventBreadcrumbs;
    }

    public void setEnableSystemEventBreadcrumbs(boolean enableSystemEventBreadcrumbs) {
        this.enableSystemEventBreadcrumbs = enableSystemEventBreadcrumbs;
    }

    public boolean isEnableAppComponentBreadcrumbs() {
        return this.enableAppComponentBreadcrumbs;
    }

    public void setEnableAppComponentBreadcrumbs(boolean enableAppComponentBreadcrumbs) {
        this.enableAppComponentBreadcrumbs = enableAppComponentBreadcrumbs;
    }

    public boolean isEnableUserInteractionBreadcrumbs() {
        return this.enableUserInteractionBreadcrumbs;
    }

    public void setEnableUserInteractionBreadcrumbs(boolean enableUserInteractionBreadcrumbs) {
        this.enableUserInteractionBreadcrumbs = enableUserInteractionBreadcrumbs;
    }

    public void enableAllAutoBreadcrumbs(boolean enable) {
        this.enableActivityLifecycleBreadcrumbs = enable;
        this.enableAppComponentBreadcrumbs = enable;
        this.enableSystemEventBreadcrumbs = enable;
        this.enableAppLifecycleBreadcrumbs = enable;
        this.enableUserInteractionBreadcrumbs = enable;
    }

    @NotNull
    public IDebugImagesLoader getDebugImagesLoader() {
        return this.debugImagesLoader;
    }

    public void setDebugImagesLoader(@NotNull IDebugImagesLoader debugImagesLoader) {
        this.debugImagesLoader = debugImagesLoader != null ? debugImagesLoader : NoOpDebugImagesLoader.getInstance();
    }

    public boolean isEnableAutoActivityLifecycleTracing() {
        return this.enableAutoActivityLifecycleTracing;
    }

    public void setEnableAutoActivityLifecycleTracing(boolean enableAutoActivityLifecycleTracing) {
        this.enableAutoActivityLifecycleTracing = enableAutoActivityLifecycleTracing;
    }

    public boolean isEnableActivityLifecycleTracingAutoFinish() {
        return this.enableActivityLifecycleTracingAutoFinish;
    }

    public void setEnableActivityLifecycleTracingAutoFinish(boolean enableActivityLifecycleTracingAutoFinish) {
        this.enableActivityLifecycleTracingAutoFinish = enableActivityLifecycleTracingAutoFinish;
    }
}
