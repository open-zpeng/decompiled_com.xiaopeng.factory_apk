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
public final class Gpu implements IUnknownPropertiesConsumer {
    public static final String TYPE = "gpu";
    @Nullable
    private String apiType;
    @Nullable
    private Integer id;
    @Nullable
    private Integer memorySize;
    @Nullable
    private Boolean multiThreadedRendering;
    @Nullable
    private String name;
    @Nullable
    private String npotSupport;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private Integer vendorId;
    @Nullable
    private String vendorName;
    @Nullable
    private String version;

    public Gpu() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Gpu(@NotNull Gpu gpu) {
        this.name = gpu.name;
        this.id = gpu.id;
        this.vendorId = gpu.vendorId;
        this.vendorName = gpu.vendorName;
        this.memorySize = gpu.memorySize;
        this.apiType = gpu.apiType;
        this.multiThreadedRendering = gpu.multiThreadedRendering;
        this.version = gpu.version;
        this.npotSupport = gpu.npotSupport;
        this.unknown = CollectionUtils.newConcurrentHashMap(gpu.unknown);
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Nullable
    public Integer getVendorId() {
        return this.vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    @Nullable
    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(@Nullable String vendorName) {
        this.vendorName = vendorName;
    }

    @Nullable
    public Integer getMemorySize() {
        return this.memorySize;
    }

    public void setMemorySize(@Nullable Integer memorySize) {
        this.memorySize = memorySize;
    }

    @Nullable
    public String getApiType() {
        return this.apiType;
    }

    public void setApiType(@Nullable String apiType) {
        this.apiType = apiType;
    }

    @Nullable
    public Boolean isMultiThreadedRendering() {
        return this.multiThreadedRendering;
    }

    public void setMultiThreadedRendering(@Nullable Boolean multiThreadedRendering) {
        this.multiThreadedRendering = multiThreadedRendering;
    }

    @Nullable
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    @Nullable
    public String getNpotSupport() {
        return this.npotSupport;
    }

    public void setNpotSupport(@Nullable String npotSupport) {
        this.npotSupport = npotSupport;
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
