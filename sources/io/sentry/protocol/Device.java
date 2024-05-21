package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class Device implements IUnknownPropertiesConsumer {
    public static final String TYPE = "device";
    @Nullable
    private String[] archs;
    @Nullable
    private Float batteryLevel;
    @Nullable
    private Float batteryTemperature;
    @Nullable
    private Date bootTime;
    @Nullable
    private String brand;
    @Nullable
    private Boolean charging;
    @Nullable
    private String connectionType;
    @Nullable
    private Long externalFreeStorage;
    @Nullable
    private Long externalStorageSize;
    @Nullable
    private String family;
    @Nullable
    private Long freeMemory;
    @Nullable
    private Long freeStorage;
    @Nullable
    private String id;
    @Deprecated
    @Nullable
    private String language;
    @Nullable
    private String locale;
    @Nullable
    private Boolean lowMemory;
    @Nullable
    private String manufacturer;
    @Nullable
    private Long memorySize;
    @Nullable
    private String model;
    @Nullable
    private String modelId;
    @Nullable
    private String name;
    @Nullable
    private Boolean online;
    @Nullable
    private DeviceOrientation orientation;
    @Nullable
    private Float screenDensity;
    @Nullable
    private Integer screenDpi;
    @Nullable
    private Integer screenHeightPixels;
    @Nullable
    private Integer screenWidthPixels;
    @Nullable
    private Boolean simulator;
    @Nullable
    private Long storageSize;
    @Nullable
    private TimeZone timezone;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private Long usableMemory;

    /* loaded from: classes2.dex */
    public enum DeviceOrientation {
        PORTRAIT,
        LANDSCAPE
    }

    public Device() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Device(@NotNull Device device) {
        this.name = device.name;
        this.manufacturer = device.manufacturer;
        this.brand = device.brand;
        this.family = device.family;
        this.model = device.model;
        this.modelId = device.modelId;
        this.charging = device.charging;
        this.online = device.online;
        this.orientation = device.orientation;
        this.simulator = device.simulator;
        this.memorySize = device.memorySize;
        this.freeMemory = device.freeMemory;
        this.usableMemory = device.usableMemory;
        this.lowMemory = device.lowMemory;
        this.storageSize = device.storageSize;
        this.freeStorage = device.freeStorage;
        this.externalStorageSize = device.externalStorageSize;
        this.externalFreeStorage = device.externalFreeStorage;
        this.screenWidthPixels = device.screenWidthPixels;
        this.screenHeightPixels = device.screenHeightPixels;
        this.screenDensity = device.screenDensity;
        this.screenDpi = device.screenDpi;
        this.bootTime = device.bootTime;
        this.id = device.id;
        this.language = device.language;
        this.connectionType = device.connectionType;
        this.batteryTemperature = device.batteryTemperature;
        this.batteryLevel = device.batteryLevel;
        String[] archsRef = device.archs;
        this.archs = archsRef != null ? (String[]) archsRef.clone() : null;
        this.locale = device.locale;
        TimeZone timezoneRef = device.timezone;
        this.timezone = timezoneRef != null ? (TimeZone) timezoneRef.clone() : null;
        this.unknown = CollectionUtils.newConcurrentHashMap(device.unknown);
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(@Nullable String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Nullable
    public String getBrand() {
        return this.brand;
    }

    public void setBrand(@Nullable String brand) {
        this.brand = brand;
    }

    @Nullable
    public String getFamily() {
        return this.family;
    }

    public void setFamily(@Nullable String family) {
        this.family = family;
    }

    @Nullable
    public String getModel() {
        return this.model;
    }

    public void setModel(@Nullable String model) {
        this.model = model;
    }

    @Nullable
    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(@Nullable String modelId) {
        this.modelId = modelId;
    }

    @Nullable
    public Float getBatteryLevel() {
        return this.batteryLevel;
    }

    public void setBatteryLevel(@Nullable Float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Nullable
    public Boolean isCharging() {
        return this.charging;
    }

    public void setCharging(@Nullable Boolean charging) {
        this.charging = charging;
    }

    @Nullable
    public Boolean isOnline() {
        return this.online;
    }

    public void setOnline(@Nullable Boolean online) {
        this.online = online;
    }

    @Nullable
    public DeviceOrientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(@Nullable DeviceOrientation orientation) {
        this.orientation = orientation;
    }

    @Nullable
    public Boolean isSimulator() {
        return this.simulator;
    }

    public void setSimulator(@Nullable Boolean simulator) {
        this.simulator = simulator;
    }

    @Nullable
    public Long getMemorySize() {
        return this.memorySize;
    }

    public void setMemorySize(@Nullable Long memorySize) {
        this.memorySize = memorySize;
    }

    @Nullable
    public Long getFreeMemory() {
        return this.freeMemory;
    }

    public void setFreeMemory(@Nullable Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    @Nullable
    public Long getUsableMemory() {
        return this.usableMemory;
    }

    public void setUsableMemory(@Nullable Long usableMemory) {
        this.usableMemory = usableMemory;
    }

    @Nullable
    public Boolean isLowMemory() {
        return this.lowMemory;
    }

    public void setLowMemory(@Nullable Boolean lowMemory) {
        this.lowMemory = lowMemory;
    }

    @Nullable
    public Long getStorageSize() {
        return this.storageSize;
    }

    public void setStorageSize(@Nullable Long storageSize) {
        this.storageSize = storageSize;
    }

    @Nullable
    public Long getFreeStorage() {
        return this.freeStorage;
    }

    public void setFreeStorage(@Nullable Long freeStorage) {
        this.freeStorage = freeStorage;
    }

    @Nullable
    public Long getExternalStorageSize() {
        return this.externalStorageSize;
    }

    public void setExternalStorageSize(@Nullable Long externalStorageSize) {
        this.externalStorageSize = externalStorageSize;
    }

    @Nullable
    public Long getExternalFreeStorage() {
        return this.externalFreeStorage;
    }

    public void setExternalFreeStorage(@Nullable Long externalFreeStorage) {
        this.externalFreeStorage = externalFreeStorage;
    }

    @Nullable
    public Float getScreenDensity() {
        return this.screenDensity;
    }

    public void setScreenDensity(@Nullable Float screenDensity) {
        this.screenDensity = screenDensity;
    }

    @Nullable
    public Integer getScreenDpi() {
        return this.screenDpi;
    }

    public void setScreenDpi(@Nullable Integer screenDpi) {
        this.screenDpi = screenDpi;
    }

    @Nullable
    public Date getBootTime() {
        Date bootTimeRef = this.bootTime;
        if (bootTimeRef != null) {
            return (Date) bootTimeRef.clone();
        }
        return null;
    }

    public void setBootTime(@Nullable Date bootTime) {
        this.bootTime = bootTime;
    }

    @Nullable
    public TimeZone getTimezone() {
        return this.timezone;
    }

    public void setTimezone(@Nullable TimeZone timezone) {
        this.timezone = timezone;
    }

    @Nullable
    public String[] getArchs() {
        return this.archs;
    }

    public void setArchs(@Nullable String[] archs) {
        this.archs = archs;
    }

    @Nullable
    public Integer getScreenWidthPixels() {
        return this.screenWidthPixels;
    }

    public void setScreenWidthPixels(@Nullable Integer screenWidthPixels) {
        this.screenWidthPixels = screenWidthPixels;
    }

    @Nullable
    public Integer getScreenHeightPixels() {
        return this.screenHeightPixels;
    }

    public void setScreenHeightPixels(@Nullable Integer screenHeightPixels) {
        this.screenHeightPixels = screenHeightPixels;
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(@Nullable String language) {
        this.language = language;
    }

    @Nullable
    public String getConnectionType() {
        return this.connectionType;
    }

    public void setConnectionType(@Nullable String connectionType) {
        this.connectionType = connectionType;
    }

    @Nullable
    public Float getBatteryTemperature() {
        return this.batteryTemperature;
    }

    public void setBatteryTemperature(@Nullable Float batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
    }

    @Nullable
    public String getLocale() {
        return this.locale;
    }

    public void setLocale(@Nullable String locale) {
        this.locale = locale;
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
