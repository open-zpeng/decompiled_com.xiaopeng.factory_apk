package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SdkVersion implements IUnknownPropertiesConsumer {
    @Nullable
    private List<String> integrations;
    @NotNull
    private String name;
    @Nullable
    private List<SentryPackage> packages;
    @Nullable
    private Map<String, Object> unknown;
    @NotNull
    private String version;

    public SdkVersion(@NotNull String name, @NotNull String version) {
        this.name = (String) Objects.requireNonNull(name, "name is required.");
        this.version = (String) Objects.requireNonNull(version, "version is required.");
    }

    @Deprecated
    public SdkVersion() {
        this("", "");
    }

    @NotNull
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@NotNull String version) {
        this.version = (String) Objects.requireNonNull(version, "version is required.");
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = (String) Objects.requireNonNull(name, "name is required.");
    }

    public void addPackage(@NotNull String name, @NotNull String version) {
        Objects.requireNonNull(name, "name is required.");
        Objects.requireNonNull(version, "version is required.");
        SentryPackage newPackage = new SentryPackage(name, version);
        if (this.packages == null) {
            this.packages = new ArrayList();
        }
        this.packages.add(newPackage);
    }

    public void addIntegration(@NotNull String integration) {
        Objects.requireNonNull(integration, "integration is required.");
        if (this.integrations == null) {
            this.integrations = new ArrayList();
        }
        this.integrations.add(integration);
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @Nullable
    public List<SentryPackage> getPackages() {
        return this.packages;
    }

    @Nullable
    public List<String> getIntegrations() {
        return this.integrations;
    }

    @NotNull
    public static SdkVersion updateSdkVersion(@Nullable SdkVersion sdk, @NotNull String name, @NotNull String version) {
        Objects.requireNonNull(name, "name is required.");
        Objects.requireNonNull(version, "version is required.");
        if (sdk == null) {
            return new SdkVersion(name, version);
        }
        sdk.setName(name);
        sdk.setVersion(version);
        return sdk;
    }
}
