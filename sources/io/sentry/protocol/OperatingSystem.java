package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class OperatingSystem implements IUnknownPropertiesConsumer {
    public static final String TYPE = "os";
    @Nullable
    private String build;
    @Nullable
    private String kernelVersion;
    @Nullable
    private String name;
    @Nullable
    private String rawDescription;
    @Nullable
    private Boolean rooted;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private String version;

    public OperatingSystem() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OperatingSystem(@NotNull OperatingSystem operatingSystem) {
        this.name = operatingSystem.name;
        this.version = operatingSystem.version;
        this.rawDescription = operatingSystem.rawDescription;
        this.build = operatingSystem.build;
        this.kernelVersion = operatingSystem.kernelVersion;
        this.rooted = operatingSystem.rooted;
        this.unknown = CollectionUtils.newConcurrentHashMap(operatingSystem.unknown);
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    @Nullable
    public String getRawDescription() {
        return this.rawDescription;
    }

    public void setRawDescription(@Nullable String rawDescription) {
        this.rawDescription = rawDescription;
    }

    @Nullable
    public String getBuild() {
        return this.build;
    }

    public void setBuild(@Nullable String build) {
        this.build = build;
    }

    @Nullable
    public String getKernelVersion() {
        return this.kernelVersion;
    }

    public void setKernelVersion(@Nullable String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }

    @Nullable
    public Boolean isRooted() {
        return this.rooted;
    }

    public void setRooted(@Nullable Boolean rooted) {
        this.rooted = rooted;
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
