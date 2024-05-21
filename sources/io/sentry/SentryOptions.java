package io.sentry;

import io.sentry.cache.IEnvelopeCache;
import io.sentry.config.PropertiesProvider;
import io.sentry.protocol.SdkVersion;
import io.sentry.transport.ITransportGate;
import io.sentry.transport.NoOpEnvelopeCache;
import io.sentry.transport.NoOpTransportGate;
import io.sentry.util.Platform;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.net.tftp.TFTP;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public class SentryOptions {
    static final SentryLevel DEFAULT_DIAGNOSTIC_LEVEL = SentryLevel.DEBUG;
    private static final String PROXY_PORT_DEFAULT = "80";
    private boolean attachServerName;
    private boolean attachStacktrace;
    private boolean attachThreads;
    @Nullable
    private BeforeBreadcrumbCallback beforeBreadcrumb;
    @Nullable
    private BeforeSendCallback beforeSend;
    @Nullable
    private String cacheDirPath;
    private int connectionTimeoutMillis;
    @Nullable
    private Boolean debug;
    @NotNull
    private SentryLevel diagnosticLevel;
    @Nullable
    private String dist;
    @Nullable
    private String distinctId;
    @Nullable
    private String dsn;
    private boolean enableAutoSessionTracking;
    @Nullable
    private Boolean enableDeduplication;
    private boolean enableExternalConfiguration;
    private boolean enableNdk;
    private boolean enableScopeSync;
    private boolean enableShutdownHook;
    @Nullable
    private Boolean enableUncaughtExceptionHandler;
    @NotNull
    private IEnvelopeCache envelopeDiskCache;
    @NotNull
    private IEnvelopeReader envelopeReader;
    @Nullable
    private String environment;
    @NotNull
    private final List<EventProcessor> eventProcessors;
    @NotNull
    private ISentryExecutorService executorService;
    private long flushTimeoutMillis;
    @Nullable
    private HostnameVerifier hostnameVerifier;
    @NotNull
    private final Set<Class<? extends Throwable>> ignoredExceptionsForType;
    @NotNull
    private final List<String> inAppExcludes;
    @NotNull
    private final List<String> inAppIncludes;
    @NotNull
    private final List<Integration> integrations;
    @NotNull
    private ILogger logger;
    private long maxAttachmentSize;
    private int maxBreadcrumbs;
    private int maxCacheItems;
    private int maxQueueSize;
    @NotNull
    private RequestSize maxRequestBodySize;
    private int maxSpans;
    @NotNull
    private final List<IScopeObserver> observers;
    @Nullable
    private Boolean printUncaughtStackTrace;
    @Nullable
    private String proguardUuid;
    @Nullable
    private Proxy proxy;
    private int readTimeoutMillis;
    @Nullable
    private String release;
    @Nullable
    private Double sampleRate;
    @Nullable
    private SdkVersion sdkVersion;
    private boolean sendDefaultPii;
    @Nullable
    private String sentryClientName;
    @NotNull
    private ISerializer serializer;
    @Nullable
    private String serverName;
    private long sessionTrackingIntervalMillis;
    private long shutdownTimeout;
    @Nullable
    private SSLSocketFactory sslSocketFactory;
    @NotNull
    private final Map<String, String> tags;
    private boolean traceSampling;
    @Nullable
    private Double tracesSampleRate;
    @Nullable
    private TracesSamplerCallback tracesSampler;
    @NotNull
    private final List<String> tracingOrigins;
    @NotNull
    private ITransportFactory transportFactory;
    @NotNull
    private ITransportGate transportGate;

    /* loaded from: classes2.dex */
    public interface BeforeBreadcrumbCallback {
        @Nullable
        Breadcrumb execute(@NotNull Breadcrumb breadcrumb, @Nullable Object obj);
    }

    /* loaded from: classes2.dex */
    public interface BeforeSendCallback {
        @Nullable
        SentryEvent execute(@NotNull SentryEvent sentryEvent, @Nullable Object obj);
    }

    /* loaded from: classes2.dex */
    public enum RequestSize {
        NONE,
        SMALL,
        MEDIUM,
        ALWAYS
    }

    /* loaded from: classes2.dex */
    public interface TracesSamplerCallback {
        @Nullable
        Double sample(@NotNull SamplingContext samplingContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static SentryOptions from(@NotNull PropertiesProvider propertiesProvider, @NotNull ILogger logger) {
        SentryOptions options = new SentryOptions();
        options.setDsn(propertiesProvider.getProperty("dsn"));
        options.setEnvironment(propertiesProvider.getProperty("environment"));
        options.setRelease(propertiesProvider.getProperty("release"));
        options.setDist(propertiesProvider.getProperty("dist"));
        options.setServerName(propertiesProvider.getProperty("servername"));
        options.setEnableUncaughtExceptionHandler(propertiesProvider.getBooleanProperty("uncaught.handler.enabled"));
        options.setPrintUncaughtStackTrace(propertiesProvider.getBooleanProperty("uncaught.handler.print-stacktrace"));
        options.setTracesSampleRate(propertiesProvider.getDoubleProperty("traces-sample-rate"));
        options.setDebug(propertiesProvider.getBooleanProperty(com.xiaopeng.lib.framework.ipcmodule.BuildConfig.BUILD_TYPE));
        options.setEnableDeduplication(propertiesProvider.getBooleanProperty("enable-deduplication"));
        String maxRequestBodySize = propertiesProvider.getProperty("max-request-body-size");
        if (maxRequestBodySize != null) {
            options.setMaxRequestBodySize(RequestSize.valueOf(maxRequestBodySize.toUpperCase(Locale.ROOT)));
        }
        Map<String, String> tags = propertiesProvider.getMap("tags");
        for (Map.Entry<String, String> tag : tags.entrySet()) {
            options.setTag(tag.getKey(), tag.getValue());
        }
        String proxyHost = propertiesProvider.getProperty("proxy.host");
        String proxyUser = propertiesProvider.getProperty("proxy.user");
        String proxyPass = propertiesProvider.getProperty("proxy.pass");
        String proxyPort = propertiesProvider.getProperty("proxy.port", PROXY_PORT_DEFAULT);
        if (proxyHost != null) {
            options.setProxy(new Proxy(proxyHost, proxyPort, proxyUser, proxyPass));
        }
        for (String inAppInclude : propertiesProvider.getList("in-app-includes")) {
            options.addInAppInclude(inAppInclude);
        }
        for (String inAppExclude : propertiesProvider.getList("in-app-excludes")) {
            options.addInAppExclude(inAppExclude);
        }
        for (String tracingOrigin : propertiesProvider.getList("tracing-origins")) {
            options.addTracingOrigin(tracingOrigin);
        }
        options.setProguardUuid(propertiesProvider.getProperty("proguard-uuid"));
        for (String ignoredExceptionType : propertiesProvider.getList("ignored-exceptions-for-type")) {
            try {
                Class<?> clazz = Class.forName(ignoredExceptionType);
                if (Throwable.class.isAssignableFrom(clazz)) {
                    options.addIgnoredExceptionForType(clazz);
                } else {
                    logger.log(SentryLevel.WARNING, "Skipping setting %s as ignored-exception-for-type. Reason: %s does not extend Throwable", ignoredExceptionType, ignoredExceptionType);
                }
            } catch (ClassNotFoundException e) {
                logger.log(SentryLevel.WARNING, "Skipping setting %s as ignored-exception-for-type. Reason: %s class is not found", ignoredExceptionType, ignoredExceptionType);
            }
        }
        return options;
    }

    public void addEventProcessor(@NotNull EventProcessor eventProcessor) {
        this.eventProcessors.add(eventProcessor);
    }

    @NotNull
    public List<EventProcessor> getEventProcessors() {
        return this.eventProcessors;
    }

    public void addIntegration(@NotNull Integration integration) {
        this.integrations.add(integration);
    }

    @NotNull
    public List<Integration> getIntegrations() {
        return this.integrations;
    }

    @Nullable
    public String getDsn() {
        return this.dsn;
    }

    public void setDsn(@Nullable String dsn) {
        this.dsn = dsn;
    }

    public boolean isDebug() {
        return Boolean.TRUE.equals(this.debug);
    }

    public void setDebug(@Nullable Boolean debug) {
        this.debug = debug;
    }

    @Nullable
    private Boolean getDebug() {
        return this.debug;
    }

    @NotNull
    public ILogger getLogger() {
        return this.logger;
    }

    public void setLogger(@Nullable ILogger logger) {
        this.logger = logger == null ? NoOpLogger.getInstance() : new DiagnosticLogger(this, logger);
    }

    @NotNull
    public SentryLevel getDiagnosticLevel() {
        return this.diagnosticLevel;
    }

    public void setDiagnosticLevel(@Nullable SentryLevel diagnosticLevel) {
        this.diagnosticLevel = diagnosticLevel != null ? diagnosticLevel : DEFAULT_DIAGNOSTIC_LEVEL;
    }

    @NotNull
    public ISerializer getSerializer() {
        return this.serializer;
    }

    public void setSerializer(@Nullable ISerializer serializer) {
        this.serializer = serializer != null ? serializer : NoOpSerializer.getInstance();
    }

    @NotNull
    public IEnvelopeReader getEnvelopeReader() {
        return this.envelopeReader;
    }

    public void setEnvelopeReader(@Nullable IEnvelopeReader envelopeReader) {
        this.envelopeReader = envelopeReader != null ? envelopeReader : NoOpEnvelopeReader.getInstance();
    }

    public boolean isEnableNdk() {
        return this.enableNdk;
    }

    public void setEnableNdk(boolean enableNdk) {
        this.enableNdk = enableNdk;
    }

    public long getShutdownTimeout() {
        return this.shutdownTimeout;
    }

    public void setShutdownTimeout(long shutdownTimeoutMillis) {
        this.shutdownTimeout = shutdownTimeoutMillis;
    }

    @Nullable
    public String getSentryClientName() {
        return this.sentryClientName;
    }

    public void setSentryClientName(@Nullable String sentryClientName) {
        this.sentryClientName = sentryClientName;
    }

    @Nullable
    public BeforeSendCallback getBeforeSend() {
        return this.beforeSend;
    }

    public void setBeforeSend(@Nullable BeforeSendCallback beforeSend) {
        this.beforeSend = beforeSend;
    }

    @Nullable
    public BeforeBreadcrumbCallback getBeforeBreadcrumb() {
        return this.beforeBreadcrumb;
    }

    public void setBeforeBreadcrumb(@Nullable BeforeBreadcrumbCallback beforeBreadcrumb) {
        this.beforeBreadcrumb = beforeBreadcrumb;
    }

    @Nullable
    public String getCacheDirPath() {
        return this.cacheDirPath;
    }

    @Nullable
    public String getOutboxPath() {
        String str = this.cacheDirPath;
        if (str == null || str.isEmpty()) {
            return null;
        }
        return this.cacheDirPath + File.separator + "outbox";
    }

    public void setCacheDirPath(@Nullable String cacheDirPath) {
        this.cacheDirPath = cacheDirPath;
    }

    @Deprecated
    public int getCacheDirSize() {
        return this.maxCacheItems;
    }

    @Deprecated
    public void setCacheDirSize(int cacheDirSize) {
        this.maxCacheItems = cacheDirSize;
    }

    public int getMaxBreadcrumbs() {
        return this.maxBreadcrumbs;
    }

    public void setMaxBreadcrumbs(int maxBreadcrumbs) {
        this.maxBreadcrumbs = maxBreadcrumbs;
    }

    @Nullable
    public String getRelease() {
        return this.release;
    }

    public void setRelease(@Nullable String release) {
        this.release = release;
    }

    @Nullable
    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(@Nullable String environment) {
        this.environment = environment;
    }

    @Nullable
    public Proxy getProxy() {
        return this.proxy;
    }

    public void setProxy(@Nullable Proxy proxy) {
        this.proxy = proxy;
    }

    @Nullable
    public Double getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(Double sampleRate) {
        if (sampleRate != null && (sampleRate.doubleValue() > 1.0d || sampleRate.doubleValue() <= 0.0d)) {
            throw new IllegalArgumentException("The value " + sampleRate + " is not valid. Use null to disable or values > 0.0 and <= 1.0.");
        }
        this.sampleRate = sampleRate;
    }

    @Nullable
    public Double getTracesSampleRate() {
        return this.tracesSampleRate;
    }

    public void setTracesSampleRate(@Nullable Double tracesSampleRate) {
        if (tracesSampleRate != null && (tracesSampleRate.doubleValue() > 1.0d || tracesSampleRate.doubleValue() < 0.0d)) {
            throw new IllegalArgumentException("The value " + tracesSampleRate + " is not valid. Use null to disable or values between 0.0 and 1.0.");
        }
        this.tracesSampleRate = tracesSampleRate;
    }

    @Nullable
    public TracesSamplerCallback getTracesSampler() {
        return this.tracesSampler;
    }

    public void setTracesSampler(@Nullable TracesSamplerCallback tracesSampler) {
        this.tracesSampler = tracesSampler;
    }

    @NotNull
    public List<String> getInAppExcludes() {
        return this.inAppExcludes;
    }

    public void addInAppExclude(@NotNull String exclude) {
        this.inAppExcludes.add(exclude);
    }

    @NotNull
    public List<String> getInAppIncludes() {
        return this.inAppIncludes;
    }

    public void addInAppInclude(@NotNull String include) {
        this.inAppIncludes.add(include);
    }

    @NotNull
    public ITransportFactory getTransportFactory() {
        return this.transportFactory;
    }

    public void setTransportFactory(@Nullable ITransportFactory transportFactory) {
        this.transportFactory = transportFactory != null ? transportFactory : NoOpTransportFactory.getInstance();
    }

    @Nullable
    public String getDist() {
        return this.dist;
    }

    public void setDist(@Nullable String dist) {
        this.dist = dist;
    }

    @NotNull
    public ITransportGate getTransportGate() {
        return this.transportGate;
    }

    public void setTransportGate(@Nullable ITransportGate transportGate) {
        this.transportGate = transportGate != null ? transportGate : NoOpTransportGate.getInstance();
    }

    public boolean isAttachStacktrace() {
        return this.attachStacktrace;
    }

    public void setAttachStacktrace(boolean attachStacktrace) {
        this.attachStacktrace = attachStacktrace;
    }

    public boolean isAttachThreads() {
        return this.attachThreads;
    }

    public void setAttachThreads(boolean attachThreads) {
        this.attachThreads = attachThreads;
    }

    public boolean isEnableAutoSessionTracking() {
        return this.enableAutoSessionTracking;
    }

    public void setEnableAutoSessionTracking(boolean enableAutoSessionTracking) {
        this.enableAutoSessionTracking = enableAutoSessionTracking;
    }

    @ApiStatus.ScheduledForRemoval
    @Deprecated
    public boolean isEnableSessionTracking() {
        return this.enableAutoSessionTracking;
    }

    @ApiStatus.ScheduledForRemoval
    @Deprecated
    public void setEnableSessionTracking(boolean enableSessionTracking) {
        setEnableAutoSessionTracking(enableSessionTracking);
    }

    @Nullable
    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    public boolean isAttachServerName() {
        return this.attachServerName;
    }

    public void setAttachServerName(boolean attachServerName) {
        this.attachServerName = attachServerName;
    }

    public long getSessionTrackingIntervalMillis() {
        return this.sessionTrackingIntervalMillis;
    }

    public void setSessionTrackingIntervalMillis(long sessionTrackingIntervalMillis) {
        this.sessionTrackingIntervalMillis = sessionTrackingIntervalMillis;
    }

    @ApiStatus.Internal
    @Nullable
    public String getDistinctId() {
        return this.distinctId;
    }

    @ApiStatus.Internal
    public void setDistinctId(@Nullable String distinctId) {
        this.distinctId = distinctId;
    }

    public long getFlushTimeoutMillis() {
        return this.flushTimeoutMillis;
    }

    public void setFlushTimeoutMillis(long flushTimeoutMillis) {
        this.flushTimeoutMillis = flushTimeoutMillis;
    }

    public boolean isEnableUncaughtExceptionHandler() {
        return Boolean.TRUE.equals(this.enableUncaughtExceptionHandler);
    }

    @Nullable
    public Boolean getEnableUncaughtExceptionHandler() {
        return this.enableUncaughtExceptionHandler;
    }

    public void setEnableUncaughtExceptionHandler(@Nullable Boolean enableUncaughtExceptionHandler) {
        this.enableUncaughtExceptionHandler = enableUncaughtExceptionHandler;
    }

    public boolean isPrintUncaughtStackTrace() {
        return Boolean.TRUE.equals(this.printUncaughtStackTrace);
    }

    @Nullable
    public Boolean getPrintUncaughtStackTrace() {
        return this.printUncaughtStackTrace;
    }

    public void setPrintUncaughtStackTrace(@Nullable Boolean printUncaughtStackTrace) {
        this.printUncaughtStackTrace = printUncaughtStackTrace;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public ISentryExecutorService getExecutorService() {
        return this.executorService;
    }

    void setExecutorService(@NotNull ISentryExecutorService executorService) {
        if (executorService != null) {
            this.executorService = executorService;
        }
    }

    public int getConnectionTimeoutMillis() {
        return this.connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return this.readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    @NotNull
    public IEnvelopeCache getEnvelopeDiskCache() {
        return this.envelopeDiskCache;
    }

    public void setEnvelopeDiskCache(@Nullable IEnvelopeCache envelopeDiskCache) {
        this.envelopeDiskCache = envelopeDiskCache != null ? envelopeDiskCache : NoOpEnvelopeCache.getInstance();
    }

    public int getMaxQueueSize() {
        return this.maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        if (maxQueueSize > 0) {
            this.maxQueueSize = maxQueueSize;
        }
    }

    @Nullable
    public SdkVersion getSdkVersion() {
        return this.sdkVersion;
    }

    @Nullable
    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public void setSslSocketFactory(@Nullable SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    @Nullable
    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public void setHostnameVerifier(@Nullable HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    @ApiStatus.Internal
    public void setSdkVersion(@Nullable SdkVersion sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public boolean isSendDefaultPii() {
        return this.sendDefaultPii;
    }

    public void setSendDefaultPii(boolean sendDefaultPii) {
        this.sendDefaultPii = sendDefaultPii;
    }

    public void addScopeObserver(@NotNull IScopeObserver observer) {
        this.observers.add(observer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public List<IScopeObserver> getScopeObservers() {
        return this.observers;
    }

    public boolean isEnableScopeSync() {
        return this.enableScopeSync;
    }

    public void setEnableScopeSync(boolean enableScopeSync) {
        this.enableScopeSync = enableScopeSync;
    }

    public boolean isEnableExternalConfiguration() {
        return this.enableExternalConfiguration;
    }

    public void setEnableExternalConfiguration(boolean enableExternalConfiguration) {
        this.enableExternalConfiguration = enableExternalConfiguration;
    }

    @NotNull
    public Map<String, String> getTags() {
        return this.tags;
    }

    public void setTag(@NotNull String key, @NotNull String value) {
        this.tags.put(key, value);
    }

    public long getMaxAttachmentSize() {
        return this.maxAttachmentSize;
    }

    public void setMaxAttachmentSize(long maxAttachmentSize) {
        this.maxAttachmentSize = maxAttachmentSize;
    }

    public boolean isEnableDeduplication() {
        return Boolean.TRUE.equals(this.enableDeduplication);
    }

    @Nullable
    private Boolean getEnableDeduplication() {
        return this.enableDeduplication;
    }

    public void setEnableDeduplication(@Nullable Boolean enableDeduplication) {
        this.enableDeduplication = enableDeduplication;
    }

    public boolean isTracingEnabled() {
        return (getTracesSampleRate() == null && getTracesSampler() == null) ? false : true;
    }

    @NotNull
    public Set<Class<? extends Throwable>> getIgnoredExceptionsForType() {
        return this.ignoredExceptionsForType;
    }

    public void addIgnoredExceptionForType(@NotNull Class<? extends Throwable> exceptionType) {
        this.ignoredExceptionsForType.add(exceptionType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean containsIgnoredExceptionForType(@NotNull Throwable throwable) {
        return this.ignoredExceptionsForType.contains(throwable.getClass());
    }

    @ApiStatus.Experimental
    public int getMaxSpans() {
        return this.maxSpans;
    }

    @ApiStatus.Experimental
    public void setMaxSpans(int maxSpans) {
        this.maxSpans = maxSpans;
    }

    public boolean isEnableShutdownHook() {
        return this.enableShutdownHook;
    }

    public void setEnableShutdownHook(boolean enableShutdownHook) {
        this.enableShutdownHook = enableShutdownHook;
    }

    public int getMaxCacheItems() {
        return this.maxCacheItems;
    }

    public void setMaxCacheItems(int maxCacheItems) {
        this.maxCacheItems = maxCacheItems;
    }

    @NotNull
    public RequestSize getMaxRequestBodySize() {
        return this.maxRequestBodySize;
    }

    public void setMaxRequestBodySize(@NotNull RequestSize maxRequestBodySize) {
        this.maxRequestBodySize = maxRequestBodySize;
    }

    @ApiStatus.Experimental
    public boolean isTraceSampling() {
        return this.traceSampling;
    }

    @ApiStatus.Experimental
    public void setTraceSampling(boolean traceSampling) {
        this.traceSampling = traceSampling;
    }

    @NotNull
    public List<String> getTracingOrigins() {
        return this.tracingOrigins;
    }

    public void addTracingOrigin(@NotNull String tracingOrigin) {
        this.tracingOrigins.add(tracingOrigin);
    }

    @Nullable
    public String getProguardUuid() {
        return this.proguardUuid;
    }

    public void setProguardUuid(@Nullable String proguardUuid) {
        this.proguardUuid = proguardUuid;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public static SentryOptions empty() {
        return new SentryOptions(true);
    }

    public SentryOptions() {
        this(false);
    }

    private SentryOptions(boolean empty) {
        this.eventProcessors = new CopyOnWriteArrayList();
        this.ignoredExceptionsForType = new CopyOnWriteArraySet();
        this.integrations = new CopyOnWriteArrayList();
        this.shutdownTimeout = 2000L;
        this.flushTimeoutMillis = 15000L;
        this.enableNdk = true;
        this.logger = NoOpLogger.getInstance();
        this.diagnosticLevel = DEFAULT_DIAGNOSTIC_LEVEL;
        this.envelopeReader = new EnvelopeReader();
        this.serializer = new GsonSerializer(this);
        this.maxCacheItems = 30;
        this.maxQueueSize = this.maxCacheItems;
        this.maxBreadcrumbs = 100;
        this.inAppExcludes = new CopyOnWriteArrayList();
        this.inAppIncludes = new CopyOnWriteArrayList();
        this.transportFactory = NoOpTransportFactory.getInstance();
        this.transportGate = NoOpTransportGate.getInstance();
        this.attachStacktrace = true;
        this.enableAutoSessionTracking = true;
        this.sessionTrackingIntervalMillis = 30000L;
        this.attachServerName = true;
        this.enableUncaughtExceptionHandler = true;
        this.printUncaughtStackTrace = false;
        this.executorService = NoOpSentryExecutorService.getInstance();
        this.connectionTimeoutMillis = TFTP.DEFAULT_TIMEOUT;
        this.readTimeoutMillis = TFTP.DEFAULT_TIMEOUT;
        this.envelopeDiskCache = NoOpEnvelopeCache.getInstance();
        this.sendDefaultPii = false;
        this.observers = new ArrayList();
        this.tags = new ConcurrentHashMap();
        this.maxAttachmentSize = 20971520L;
        this.enableDeduplication = true;
        this.maxSpans = 1000;
        this.enableShutdownHook = true;
        this.maxRequestBodySize = RequestSize.NONE;
        this.tracingOrigins = new CopyOnWriteArrayList();
        if (!empty) {
            this.executorService = new SentryExecutorService();
            this.integrations.add(new UncaughtExceptionHandlerIntegration());
            this.integrations.add(new ShutdownHookIntegration());
            this.eventProcessors.add(new MainEventProcessor(this));
            this.eventProcessors.add(new DuplicateEventDetectionEventProcessor(this));
            if (Platform.isJvm()) {
                this.eventProcessors.add(new SentryRuntimeEventProcessor());
            }
            setSentryClientName("sentry.java/5.7.3");
            setSdkVersion(createSdkVersion());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void merge(@NotNull SentryOptions options) {
        if (options.getDsn() != null) {
            setDsn(options.getDsn());
        }
        if (options.getEnvironment() != null) {
            setEnvironment(options.getEnvironment());
        }
        if (options.getRelease() != null) {
            setRelease(options.getRelease());
        }
        if (options.getDist() != null) {
            setDist(options.getDist());
        }
        if (options.getServerName() != null) {
            setServerName(options.getServerName());
        }
        if (options.getProxy() != null) {
            setProxy(options.getProxy());
        }
        if (options.getEnableUncaughtExceptionHandler() != null) {
            setEnableUncaughtExceptionHandler(options.getEnableUncaughtExceptionHandler());
        }
        if (options.getPrintUncaughtStackTrace() != null) {
            setPrintUncaughtStackTrace(options.getPrintUncaughtStackTrace());
        }
        if (options.getTracesSampleRate() != null) {
            setTracesSampleRate(options.getTracesSampleRate());
        }
        if (options.getDebug() != null) {
            setDebug(options.getDebug());
        }
        if (options.getEnableDeduplication() != null) {
            setEnableDeduplication(options.getEnableDeduplication());
        }
        Map<String, String> tags = new HashMap<>(options.getTags());
        for (Map.Entry<String, String> tag : tags.entrySet()) {
            this.tags.put(tag.getKey(), tag.getValue());
        }
        List<String> inAppIncludes = new ArrayList<>(options.getInAppIncludes());
        for (String inAppInclude : inAppIncludes) {
            addInAppInclude(inAppInclude);
        }
        List<String> inAppExcludes = new ArrayList<>(options.getInAppExcludes());
        for (String inAppExclude : inAppExcludes) {
            addInAppExclude(inAppExclude);
        }
        Iterator it = new HashSet(options.getIgnoredExceptionsForType()).iterator();
        while (it.hasNext()) {
            Class<? extends Throwable> exceptionType = (Class) it.next();
            addIgnoredExceptionForType(exceptionType);
        }
        List<String> tracingOrigins = new ArrayList<>(options.getTracingOrigins());
        for (String tracingOrigin : tracingOrigins) {
            addTracingOrigin(tracingOrigin);
        }
        if (options.getProguardUuid() != null) {
            setProguardUuid(options.getProguardUuid());
        }
    }

    @NotNull
    private SdkVersion createSdkVersion() {
        SdkVersion sdkVersion = new SdkVersion(BuildConfig.SENTRY_JAVA_SDK_NAME, "5.7.3");
        sdkVersion.setVersion("5.7.3");
        sdkVersion.addPackage("maven:io.sentry:sentry", "5.7.3");
        return sdkVersion;
    }

    /* loaded from: classes2.dex */
    public static final class Proxy {
        @Nullable
        private String host;
        @Nullable
        private String pass;
        @Nullable
        private String port;
        @Nullable
        private String user;

        public Proxy(@Nullable String host, @Nullable String port, @Nullable String user, @Nullable String pass) {
            this.host = host;
            this.port = port;
            this.user = user;
            this.pass = pass;
        }

        public Proxy() {
            this(null, null, null, null);
        }

        public Proxy(@Nullable String host, @Nullable String port) {
            this(host, port, null, null);
        }

        @Nullable
        public String getHost() {
            return this.host;
        }

        public void setHost(@Nullable String host) {
            this.host = host;
        }

        @Nullable
        public String getPort() {
            return this.port;
        }

        public void setPort(@Nullable String port) {
            this.port = port;
        }

        @Nullable
        public String getUser() {
            return this.user;
        }

        public void setUser(@Nullable String user) {
            this.user = user;
        }

        @Nullable
        public String getPass() {
            return this.pass;
        }

        public void setPass(@Nullable String pass) {
            this.pass = pass;
        }
    }
}
