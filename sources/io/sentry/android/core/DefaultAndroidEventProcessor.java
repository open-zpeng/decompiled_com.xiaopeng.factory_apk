package io.sentry.android.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import io.sentry.DateUtils;
import io.sentry.EventProcessor;
import io.sentry.ILogger;
import io.sentry.SentryBaseEvent;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import io.sentry.android.core.internal.util.ConnectivityChecker;
import io.sentry.android.core.internal.util.DeviceOrientations;
import io.sentry.android.core.internal.util.MainThreadChecker;
import io.sentry.android.core.internal.util.RootChecker;
import io.sentry.protocol.App;
import io.sentry.protocol.Device;
import io.sentry.protocol.OperatingSystem;
import io.sentry.protocol.SentryThread;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import io.sentry.util.ApplyScopeUtils;
import io.sentry.util.Objects;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class DefaultAndroidEventProcessor implements EventProcessor {
    @TestOnly
    static final String EMULATOR = "emulator";
    @TestOnly
    static final String KERNEL_VERSION = "kernelVersion";
    @TestOnly
    static final String ROOTED = "rooted";
    @TestOnly
    static final String SIDE_LOADED = "sideLoaded";
    @NotNull
    private final IBuildInfoProvider buildInfoProvider;
    @TestOnly
    final Context context;
    @TestOnly
    final Future<Map<String, Object>> contextData;
    @NotNull
    private final ILogger logger;
    @NotNull
    private final RootChecker rootChecker;

    public DefaultAndroidEventProcessor(@NotNull Context context, @NotNull ILogger logger, @NotNull IBuildInfoProvider buildInfoProvider) {
        this(context, logger, buildInfoProvider, new RootChecker(context, buildInfoProvider, logger));
    }

    DefaultAndroidEventProcessor(@NotNull Context context, @NotNull ILogger logger, @NotNull IBuildInfoProvider buildInfoProvider, @NotNull RootChecker rootChecker) {
        this.context = (Context) Objects.requireNonNull(context, "The application context is required.");
        this.logger = (ILogger) Objects.requireNonNull(logger, "The Logger is required.");
        this.buildInfoProvider = (IBuildInfoProvider) Objects.requireNonNull(buildInfoProvider, "The BuildInfoProvider is required.");
        this.rootChecker = (RootChecker) Objects.requireNonNull(rootChecker, "The RootChecker is required.");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.contextData = executorService.submit(new Callable() { // from class: io.sentry.android.core.-$$Lambda$DefaultAndroidEventProcessor$ZsPdDrAaOs30gov_5eSNgy_KYe8
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return DefaultAndroidEventProcessor.this.lambda$new$0$DefaultAndroidEventProcessor();
            }
        });
        executorService.shutdown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @NotNull
    /* renamed from: loadContextData */
    public Map<String, Object> lambda$new$0$DefaultAndroidEventProcessor() {
        Map<String, Object> map = new HashMap<>();
        map.put(ROOTED, Boolean.valueOf(this.rootChecker.isDeviceRooted()));
        String kernelVersion = getKernelVersion();
        if (kernelVersion != null) {
            map.put(KERNEL_VERSION, kernelVersion);
        }
        map.put(EMULATOR, isEmulator());
        Map<String, String> sideLoadedInfo = getSideLoadedInfo();
        if (sideLoadedInfo != null) {
            map.put(SIDE_LOADED, sideLoadedInfo);
        }
        return map;
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        boolean applyScopeData = shouldApplyScopeData(event, hint);
        if (applyScopeData) {
            processNonCachedEvent(event);
            setThreads(event);
        }
        setCommons(event, true, applyScopeData);
        return event;
    }

    private void setCommons(@NotNull SentryBaseEvent event, boolean errorEvent, boolean applyScopeData) {
        mergeUser(event);
        setDevice(event, errorEvent, applyScopeData);
        mergeOS(event);
        setSideLoadedInfo(event);
    }

    private boolean shouldApplyScopeData(@NotNull SentryBaseEvent event, @Nullable Object hint) {
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            return true;
        }
        this.logger.log(SentryLevel.DEBUG, "Event was cached so not applying data relevant to the current app execution/version: %s", event.getEventId());
        return false;
    }

    private void mergeUser(@NotNull SentryBaseEvent event) {
        User user = event.getUser();
        if (user == null) {
            event.setUser(getDefaultUser());
        } else if (user.getId() == null) {
            user.setId(getDeviceId());
        }
    }

    private void setDevice(@NotNull SentryBaseEvent event, boolean errorEvent, boolean applyScopeData) {
        if (event.getContexts().getDevice() == null) {
            event.getContexts().setDevice(getDevice(errorEvent, applyScopeData));
        }
    }

    private void mergeOS(@NotNull SentryBaseEvent event) {
        String osNameKey;
        OperatingSystem currentOS = event.getContexts().getOperatingSystem();
        OperatingSystem androidOS = getOperatingSystem();
        event.getContexts().setOperatingSystem(androidOS);
        if (currentOS != null) {
            String osNameKey2 = currentOS.getName();
            if (osNameKey2 != null && !osNameKey2.isEmpty()) {
                osNameKey = "os_" + osNameKey2.trim().toLowerCase(Locale.ROOT);
            } else {
                osNameKey = "os_1";
            }
            event.getContexts().put(osNameKey, currentOS);
        }
    }

    private void processNonCachedEvent(@NotNull SentryBaseEvent event) {
        App app = event.getContexts().getApp();
        if (app == null) {
            app = new App();
        }
        setAppExtras(app);
        setPackageInfo(event, app);
        event.getContexts().setApp(app);
    }

    private void setThreads(@NotNull SentryEvent event) {
        if (event.getThreads() != null) {
            for (SentryThread thread : event.getThreads()) {
                thread.setCurrent(Boolean.valueOf(MainThreadChecker.isMainThread(thread)));
            }
        }
    }

    private void setPackageInfo(@NotNull SentryBaseEvent event, @NotNull App app) {
        PackageInfo packageInfo = ContextUtils.getPackageInfo(this.context, this.logger);
        if (packageInfo != null) {
            String versionCode = ContextUtils.getVersionCode(packageInfo);
            setDist(event, versionCode);
            setAppPackageInfo(app, packageInfo);
        }
    }

    private void setDist(@NotNull SentryBaseEvent event, @NotNull String versionCode) {
        if (event.getDist() == null) {
            event.setDist(versionCode);
        }
    }

    private void setAppExtras(@NotNull App app) {
        app.setAppName(getApplicationName());
        app.setAppStartTime(AppStartState.getInstance().getAppStartTime());
    }

    @NotNull
    private String getAbi() {
        return Build.CPU_ABI;
    }

    @NotNull
    private String getAbi2() {
        return Build.CPU_ABI2;
    }

    private void setArchitectures(@NotNull Device device) {
        if (Build.VERSION.SDK_INT >= 21) {
            device.setArchs(Build.SUPPORTED_ABIS);
            return;
        }
        String[] supportedAbis = {getAbi(), getAbi2()};
        device.setArchs(supportedAbis);
    }

    @NotNull
    private Long getMemorySize(@NotNull ActivityManager.MemoryInfo memInfo) {
        if (Build.VERSION.SDK_INT >= 16) {
            return Long.valueOf(memInfo.totalMem);
        }
        return Long.valueOf(Runtime.getRuntime().totalMemory());
    }

    @NotNull
    private Device getDevice(boolean errorEvent, boolean applyScopeData) {
        Device device = new Device();
        device.setName(getDeviceName());
        device.setManufacturer(Build.MANUFACTURER);
        device.setBrand(Build.BRAND);
        device.setFamily(getFamily());
        device.setModel(Build.MODEL);
        device.setModelId(Build.ID);
        setArchitectures(device);
        if (errorEvent) {
            setDeviceIO(device, applyScopeData);
        }
        device.setOrientation(getOrientation());
        try {
            Object emulator = this.contextData.get().get(EMULATOR);
            if (emulator != null) {
                device.setSimulator((Boolean) emulator);
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting emulator.", e);
        }
        DisplayMetrics displayMetrics = getDisplayMetrics();
        if (displayMetrics != null) {
            device.setScreenWidthPixels(Integer.valueOf(displayMetrics.widthPixels));
            device.setScreenHeightPixels(Integer.valueOf(displayMetrics.heightPixels));
            device.setScreenDensity(Float.valueOf(displayMetrics.density));
            device.setScreenDpi(Integer.valueOf(displayMetrics.densityDpi));
        }
        device.setBootTime(getBootTime());
        device.setTimezone(getTimeZone());
        if (device.getId() == null) {
            device.setId(getDeviceId());
        }
        Locale locale = Locale.getDefault();
        if (device.getLanguage() == null) {
            device.setLanguage(locale.getLanguage());
        }
        if (device.getLocale() == null) {
            device.setLocale(locale.toString());
        }
        return device;
    }

    private void setDeviceIO(@NotNull Device device, boolean applyScopeData) {
        Boolean connected;
        Intent batteryIntent = getBatteryIntent();
        if (batteryIntent != null) {
            device.setBatteryLevel(getBatteryLevel(batteryIntent));
            device.setCharging(isCharging(batteryIntent));
            device.setBatteryTemperature(getBatteryTemperature(batteryIntent));
        }
        int i = AnonymousClass1.$SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.getConnectionStatus(this.context, this.logger).ordinal()];
        if (i == 1) {
            connected = false;
        } else if (i == 2) {
            connected = true;
        } else {
            connected = null;
        }
        device.setOnline(connected);
        ActivityManager.MemoryInfo memInfo = getMemInfo();
        if (memInfo != null) {
            device.setMemorySize(getMemorySize(memInfo));
            if (applyScopeData) {
                device.setFreeMemory(Long.valueOf(memInfo.availMem));
                device.setLowMemory(Boolean.valueOf(memInfo.lowMemory));
            }
        }
        File internalStorageFile = this.context.getExternalFilesDir(null);
        if (internalStorageFile != null) {
            StatFs internalStorageStat = new StatFs(internalStorageFile.getPath());
            device.setStorageSize(getTotalInternalStorage(internalStorageStat));
            device.setFreeStorage(getUnusedInternalStorage(internalStorageStat));
        }
        StatFs externalStorageStat = getExternalStorageStat(internalStorageFile);
        if (externalStorageStat != null) {
            device.setExternalStorageSize(getTotalExternalStorage(externalStorageStat));
            device.setExternalFreeStorage(getUnusedExternalStorage(externalStorageStat));
        }
        if (device.getConnectionType() == null) {
            device.setConnectionType(ConnectivityChecker.getConnectionType(this.context, this.logger, this.buildInfoProvider));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.sentry.android.core.DefaultAndroidEventProcessor$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status = new int[ConnectivityChecker.Status.values().length];

        static {
            try {
                $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.Status.NOT_CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.Status.CONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    @Nullable
    private String getDeviceName() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Settings.Global.getString(this.context.getContentResolver(), "device_name");
        }
        return null;
    }

    private TimeZone getTimeZone() {
        if (Build.VERSION.SDK_INT >= 24) {
            LocaleList locales = this.context.getResources().getConfiguration().getLocales();
            if (!locales.isEmpty()) {
                Locale locale = locales.get(0);
                return Calendar.getInstance(locale).getTimeZone();
            }
        }
        return Calendar.getInstance().getTimeZone();
    }

    @Nullable
    private Date getBootTime() {
        try {
            return DateUtils.getDateTime(System.currentTimeMillis() - SystemClock.elapsedRealtime());
        } catch (IllegalArgumentException e) {
            this.logger.log(SentryLevel.ERROR, e, "Error getting the device's boot time.", new Object[0]);
            return null;
        }
    }

    @Nullable
    private ActivityManager.MemoryInfo getMemInfo() {
        try {
            ActivityManager actManager = (ActivityManager) this.context.getSystemService("activity");
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            if (actManager == null) {
                this.logger.log(SentryLevel.INFO, "Error getting MemoryInfo.", new Object[0]);
                return null;
            }
            actManager.getMemoryInfo(memInfo);
            return memInfo;
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting MemoryInfo.", e);
            return null;
        }
    }

    @Nullable
    private Intent getBatteryIntent() {
        return this.context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    @Nullable
    private String getFamily() {
        try {
            return Build.MODEL.split(" ", -1)[0];
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting device family.", e);
            return null;
        }
    }

    @Nullable
    private Float getBatteryLevel(@NotNull Intent batteryIntent) {
        try {
            int level = batteryIntent.getIntExtra("level", -1);
            int scale = batteryIntent.getIntExtra("scale", -1);
            if (level != -1 && scale != -1) {
                return Float.valueOf((level / scale) * 100.0f);
            }
            return null;
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting device battery level.", e);
            return null;
        }
    }

    @Nullable
    private Boolean isCharging(@NotNull Intent batteryIntent) {
        try {
            int plugged = batteryIntent.getIntExtra("plugged", -1);
            boolean z = true;
            if (plugged != 1 && plugged != 2) {
                z = false;
            }
            return Boolean.valueOf(z);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting device charging state.", e);
            return null;
        }
    }

    @Nullable
    private Float getBatteryTemperature(@NotNull Intent batteryIntent) {
        try {
            int temperature = batteryIntent.getIntExtra("temperature", -1);
            if (temperature != -1) {
                return Float.valueOf(temperature / 10.0f);
            }
            return null;
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting battery temperature.", e);
            return null;
        }
    }

    @Nullable
    private Device.DeviceOrientation getOrientation() {
        Device.DeviceOrientation deviceOrientation = null;
        try {
            deviceOrientation = DeviceOrientations.getOrientation(this.context.getResources().getConfiguration().orientation);
            if (deviceOrientation == null) {
                this.logger.log(SentryLevel.INFO, "No device orientation available (ORIENTATION_SQUARE|ORIENTATION_UNDEFINED)", new Object[0]);
                return null;
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting device orientation.", e);
        }
        return deviceOrientation;
    }

    @Nullable
    private Boolean isEmulator() {
        boolean z;
        try {
            if ((!Build.BRAND.startsWith("generic") || !Build.DEVICE.startsWith("generic")) && !Build.FINGERPRINT.startsWith("generic") && !Build.FINGERPRINT.startsWith("unknown") && !Build.HARDWARE.contains("goldfish") && !Build.HARDWARE.contains("ranchu") && !Build.MODEL.contains("google_sdk") && !Build.MODEL.contains("Emulator") && !Build.MODEL.contains("Android SDK built for x86") && !Build.MANUFACTURER.contains("Genymotion") && !Build.PRODUCT.contains("sdk_google") && !Build.PRODUCT.contains("google_sdk") && !Build.PRODUCT.contains("sdk") && !Build.PRODUCT.contains("sdk_x86") && !Build.PRODUCT.contains("vbox86p") && !Build.PRODUCT.contains(EMULATOR) && !Build.PRODUCT.contains("simulator")) {
                z = false;
                return Boolean.valueOf(z);
            }
            z = true;
            return Boolean.valueOf(z);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error checking whether application is running in an emulator.", e);
            return null;
        }
    }

    @Nullable
    private Long getTotalInternalStorage(@NotNull StatFs stat) {
        try {
            long blockSize = getBlockSizeLong(stat);
            long totalBlocks = getBlockCountLong(stat);
            return Long.valueOf(totalBlocks * blockSize);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting total internal storage amount.", e);
            return null;
        }
    }

    private long getBlockSizeLong(@NotNull StatFs stat) {
        if (Build.VERSION.SDK_INT >= 18) {
            return stat.getBlockSizeLong();
        }
        return getBlockSizeDep(stat);
    }

    private int getBlockSizeDep(@NotNull StatFs stat) {
        return stat.getBlockSize();
    }

    private long getBlockCountLong(@NotNull StatFs stat) {
        if (Build.VERSION.SDK_INT >= 18) {
            return stat.getBlockCountLong();
        }
        return getBlockCountDep(stat);
    }

    private int getBlockCountDep(@NotNull StatFs stat) {
        return stat.getBlockCount();
    }

    private long getAvailableBlocksLong(@NotNull StatFs stat) {
        if (Build.VERSION.SDK_INT >= 18) {
            return stat.getAvailableBlocksLong();
        }
        return getAvailableBlocksDep(stat);
    }

    private int getAvailableBlocksDep(@NotNull StatFs stat) {
        return stat.getAvailableBlocks();
    }

    @Nullable
    private Long getUnusedInternalStorage(@NotNull StatFs stat) {
        try {
            long blockSize = getBlockSizeLong(stat);
            long availableBlocks = getAvailableBlocksLong(stat);
            return Long.valueOf(availableBlocks * blockSize);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting unused internal storage amount.", e);
            return null;
        }
    }

    @Nullable
    private StatFs getExternalStorageStat(@Nullable File internalStorage) {
        if (!isExternalStorageMounted()) {
            File path = getExternalStorageDep(internalStorage);
            if (path != null) {
                return new StatFs(path.getPath());
            }
            this.logger.log(SentryLevel.INFO, "Not possible to read external files directory", new Object[0]);
            return null;
        }
        this.logger.log(SentryLevel.INFO, "External storage is not mounted or emulated.", new Object[0]);
        return null;
    }

    @Nullable
    private File[] getExternalFilesDirs() {
        if (Build.VERSION.SDK_INT >= 19) {
            return this.context.getExternalFilesDirs(null);
        }
        File single = this.context.getExternalFilesDir(null);
        if (single != null) {
            return new File[]{single};
        }
        return null;
    }

    @Nullable
    private File getExternalStorageDep(@Nullable File internalStorage) {
        File[] externalFilesDirs = getExternalFilesDirs();
        if (externalFilesDirs != null) {
            String internalStoragePath = internalStorage != null ? internalStorage.getAbsolutePath() : null;
            for (File file : externalFilesDirs) {
                if (file != null) {
                    if (internalStoragePath == null || internalStoragePath.isEmpty()) {
                        return file;
                    }
                    if (!file.getAbsolutePath().contains(internalStoragePath)) {
                        return file;
                    }
                }
            }
        } else {
            this.logger.log(SentryLevel.INFO, "Not possible to read getExternalFilesDirs", new Object[0]);
        }
        return null;
    }

    @Nullable
    private Long getTotalExternalStorage(@NotNull StatFs stat) {
        try {
            long blockSize = getBlockSizeLong(stat);
            long totalBlocks = getBlockCountLong(stat);
            return Long.valueOf(totalBlocks * blockSize);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting total external storage amount.", e);
            return null;
        }
    }

    private boolean isExternalStorageMounted() {
        String storageState = Environment.getExternalStorageState();
        return ("mounted".equals(storageState) || "mounted_ro".equals(storageState)) && !Environment.isExternalStorageEmulated();
    }

    @Nullable
    private Long getUnusedExternalStorage(@NotNull StatFs stat) {
        try {
            long blockSize = getBlockSizeLong(stat);
            long availableBlocks = getAvailableBlocksLong(stat);
            return Long.valueOf(availableBlocks * blockSize);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting unused external storage amount.", e);
            return null;
        }
    }

    @Nullable
    private DisplayMetrics getDisplayMetrics() {
        try {
            return this.context.getResources().getDisplayMetrics();
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting DisplayMetrics.", e);
            return null;
        }
    }

    @NotNull
    private OperatingSystem getOperatingSystem() {
        OperatingSystem os = new OperatingSystem();
        os.setName("Android");
        os.setVersion(Build.VERSION.RELEASE);
        os.setBuild(Build.DISPLAY);
        try {
            Object kernelVersion = this.contextData.get().get(KERNEL_VERSION);
            if (kernelVersion != null) {
                os.setKernelVersion((String) kernelVersion);
            }
            Object rooted = this.contextData.get().get(ROOTED);
            if (rooted != null) {
                os.setRooted((Boolean) rooted);
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting OperatingSystem.", e);
        }
        return os;
    }

    private void setAppPackageInfo(@NotNull App app, @NotNull PackageInfo packageInfo) {
        app.setAppIdentifier(packageInfo.packageName);
        app.setAppVersion(packageInfo.versionName);
        app.setAppBuild(ContextUtils.getVersionCode(packageInfo));
    }

    @Nullable
    private String getKernelVersion() {
        String defaultVersion = System.getProperty("os.version");
        File file = new File("/proc/version");
        if (!file.canRead()) {
            return defaultVersion;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = br.readLine();
            br.close();
            return readLine;
        } catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, "Exception while attempting to read kernel information", e);
            return defaultVersion;
        }
    }

    @Nullable
    private String getApplicationName() {
        try {
            ApplicationInfo applicationInfo = this.context.getApplicationInfo();
            int stringId = applicationInfo.labelRes;
            if (stringId == 0) {
                if (applicationInfo.nonLocalizedLabel != null) {
                    return applicationInfo.nonLocalizedLabel.toString();
                }
                return this.context.getPackageManager().getApplicationLabel(applicationInfo).toString();
            }
            return this.context.getString(stringId);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting application name.", e);
            return null;
        }
    }

    @NotNull
    public User getDefaultUser() {
        User user = new User();
        user.setId(getDeviceId());
        return user;
    }

    @Nullable
    private String getDeviceId() {
        try {
            return Installation.id(this.context);
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting installationId.", e);
            return null;
        }
    }

    @Nullable
    private Map<String, String> getSideLoadedInfo() {
        try {
            PackageInfo packageInfo = ContextUtils.getPackageInfo(this.context, this.logger);
            PackageManager packageManager = this.context.getPackageManager();
            if (packageInfo != null && packageManager != null) {
                String packageName = packageInfo.packageName;
                String installerPackageName = packageManager.getInstallerPackageName(packageName);
                Map<String, String> sideLoadedInfo = new HashMap<>();
                if (installerPackageName != null) {
                    sideLoadedInfo.put("isSideLoaded", BooleanUtils.FALSE);
                    sideLoadedInfo.put("installerStore", installerPackageName);
                } else {
                    sideLoadedInfo.put("isSideLoaded", "true");
                }
                return sideLoadedInfo;
            }
            return null;
        } catch (IllegalArgumentException e) {
            this.logger.log(SentryLevel.DEBUG, "%s package isn't installed.", null);
            return null;
        }
    }

    private void setSideLoadedInfo(@NotNull SentryBaseEvent event) {
        try {
            Object sideLoadedInfo = this.contextData.get().get(SIDE_LOADED);
            if (sideLoadedInfo instanceof Map) {
                for (Map.Entry<String, String> entry : ((Map) sideLoadedInfo).entrySet()) {
                    event.setTag(entry.getKey(), entry.getValue());
                }
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, "Error getting side loaded info.", e);
        }
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryTransaction process(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        boolean applyScopeData = shouldApplyScopeData(transaction, hint);
        if (applyScopeData) {
            processNonCachedEvent(transaction);
        }
        setCommons(transaction, false, applyScopeData);
        return transaction;
    }
}
