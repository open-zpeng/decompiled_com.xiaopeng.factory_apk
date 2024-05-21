package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class App implements IUnknownPropertiesConsumer {
    public static final String TYPE = "app";
    @Nullable
    private String appBuild;
    @Nullable
    private String appIdentifier;
    @Nullable
    private String appName;
    @Nullable
    private Date appStartTime;
    @Nullable
    private String appVersion;
    @Nullable
    private String buildType;
    @Nullable
    private String deviceAppHash;
    @Nullable
    private Map<String, Object> unknown;

    public App() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public App(@NotNull App app) {
        this.appBuild = app.appBuild;
        this.appIdentifier = app.appIdentifier;
        this.appName = app.appName;
        this.appStartTime = app.appStartTime;
        this.appVersion = app.appVersion;
        this.buildType = app.buildType;
        this.deviceAppHash = app.deviceAppHash;
        this.unknown = CollectionUtils.newConcurrentHashMap(app.unknown);
    }

    @Nullable
    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public void setAppIdentifier(@Nullable String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    @Nullable
    public Date getAppStartTime() {
        Date appStartTimeRef = this.appStartTime;
        if (appStartTimeRef != null) {
            return (Date) appStartTimeRef.clone();
        }
        return null;
    }

    public void setAppStartTime(@Nullable Date appStartTime) {
        this.appStartTime = appStartTime;
    }

    @Nullable
    public String getDeviceAppHash() {
        return this.deviceAppHash;
    }

    public void setDeviceAppHash(@Nullable String deviceAppHash) {
        this.deviceAppHash = deviceAppHash;
    }

    @Nullable
    public String getBuildType() {
        return this.buildType;
    }

    public void setBuildType(@Nullable String buildType) {
        this.buildType = buildType;
    }

    @Nullable
    public String getAppName() {
        return this.appName;
    }

    public void setAppName(@Nullable String appName) {
        this.appName = appName;
    }

    @Nullable
    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(@Nullable String appVersion) {
        this.appVersion = appVersion;
    }

    @Nullable
    public String getAppBuild() {
        return this.appBuild;
    }

    public void setAppBuild(@Nullable String appBuild) {
        this.appBuild = appBuild;
    }

    @TestOnly
    @Nullable
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap(unknown);
    }
}
