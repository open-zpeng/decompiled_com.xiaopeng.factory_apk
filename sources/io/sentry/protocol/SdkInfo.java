package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SdkInfo implements IUnknownPropertiesConsumer {
    @Nullable
    private String sdkName;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private Integer versionMajor;
    @Nullable
    private Integer versionMinor;
    @Nullable
    private Integer versionPatchlevel;

    @Nullable
    public String getSdkName() {
        return this.sdkName;
    }

    public void setSdkName(@Nullable String sdkName) {
        this.sdkName = sdkName;
    }

    @Nullable
    public Integer getVersionMajor() {
        return this.versionMajor;
    }

    public void setVersionMajor(@Nullable Integer versionMajor) {
        this.versionMajor = versionMajor;
    }

    @Nullable
    public Integer getVersionMinor() {
        return this.versionMinor;
    }

    public void setVersionMinor(@Nullable Integer versionMinor) {
        this.versionMinor = versionMinor;
    }

    @Nullable
    public Integer getVersionPatchlevel() {
        return this.versionPatchlevel;
    }

    public void setVersionPatchlevel(@Nullable Integer versionPatchlevel) {
        this.versionPatchlevel = versionPatchlevel;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
