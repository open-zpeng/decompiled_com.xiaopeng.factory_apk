package io.sentry;

import io.sentry.cache.EnvelopeCache;
import io.sentry.config.PropertiesProviderFactory;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Sentry {
    private static final boolean GLOBAL_HUB_DEFAULT_MODE = false;
    @NotNull
    private static final ThreadLocal<IHub> currentHub = new ThreadLocal<>();
    @NotNull
    private static volatile IHub mainHub = NoOpHub.getInstance();
    private static volatile boolean globalHubMode = false;

    /* loaded from: classes2.dex */
    public interface OptionsConfiguration<T extends SentryOptions> {
        void configure(@NotNull T t);
    }

    private Sentry() {
    }

    @ApiStatus.Internal
    @NotNull
    public static IHub getCurrentHub() {
        if (globalHubMode) {
            return mainHub;
        }
        IHub hub = currentHub.get();
        if (hub == null) {
            IHub hub2 = mainHub.clone();
            currentHub.set(hub2);
            return hub2;
        }
        return hub;
    }

    @ApiStatus.Internal
    public static void setCurrentHub(@NotNull IHub hub) {
        currentHub.set(hub);
    }

    public static boolean isEnabled() {
        return getCurrentHub().isEnabled();
    }

    public static void init() {
        init((OptionsConfiguration<SentryOptions>) new OptionsConfiguration() { // from class: io.sentry.-$$Lambda$Sentry$NEC-zGTNdEG1rkjO4iWgcQQzKvU
            @Override // io.sentry.Sentry.OptionsConfiguration
            public final void configure(SentryOptions sentryOptions) {
                sentryOptions.setEnableExternalConfiguration(true);
            }
        }, false);
    }

    public static void init(@NotNull final String dsn) {
        init(new OptionsConfiguration() { // from class: io.sentry.-$$Lambda$Sentry$Vwz5K293jbGZzoyIk4Rn8GG9_DE
            @Override // io.sentry.Sentry.OptionsConfiguration
            public final void configure(SentryOptions sentryOptions) {
                sentryOptions.setDsn(dsn);
            }
        });
    }

    public static <T extends SentryOptions> void init(@NotNull OptionsContainer<T> clazz, @NotNull OptionsConfiguration<T> optionsConfiguration) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        init(clazz, optionsConfiguration, false);
    }

    public static <T extends SentryOptions> void init(@NotNull OptionsContainer<T> clazz, @NotNull OptionsConfiguration<T> optionsConfiguration, boolean globalHubMode2) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        T options = clazz.createInstance();
        optionsConfiguration.configure(options);
        init(options, globalHubMode2);
    }

    public static void init(@NotNull OptionsConfiguration<SentryOptions> optionsConfiguration) {
        init(optionsConfiguration, false);
    }

    public static void init(@NotNull OptionsConfiguration<SentryOptions> optionsConfiguration, boolean globalHubMode2) {
        SentryOptions options = new SentryOptions();
        optionsConfiguration.configure(options);
        init(options, globalHubMode2);
    }

    @ApiStatus.Internal
    public static void init(@NotNull SentryOptions options) {
        init(options, false);
    }

    private static synchronized void init(@NotNull SentryOptions options, boolean globalHubMode2) {
        synchronized (Sentry.class) {
            if (isEnabled()) {
                options.getLogger().log(SentryLevel.WARNING, "Sentry has been already initialized. Previous configuration will be overwritten.", new Object[0]);
            }
            if (initConfigurations(options)) {
                options.getLogger().log(SentryLevel.INFO, "GlobalHubMode: '%s'", String.valueOf(globalHubMode2));
                globalHubMode = globalHubMode2;
                IHub hub = getCurrentHub();
                mainHub = new Hub(options);
                currentHub.set(mainHub);
                hub.close();
                for (Integration integration : options.getIntegrations()) {
                    integration.register(HubAdapter.getInstance(), options);
                }
            }
        }
    }

    private static boolean initConfigurations(@NotNull SentryOptions options) {
        if (options.isEnableExternalConfiguration()) {
            options.merge(SentryOptions.from(PropertiesProviderFactory.create(), options.getLogger()));
        }
        String dsn = options.getDsn();
        if (dsn == null) {
            throw new IllegalArgumentException("DSN is required. Use empty string to disable SDK.");
        }
        if (dsn.isEmpty()) {
            close();
            return false;
        }
        new Dsn(dsn);
        ILogger logger = options.getLogger();
        if (options.isDebug() && (logger instanceof NoOpLogger)) {
            options.setLogger(new SystemOutLogger());
            logger = options.getLogger();
        }
        logger.log(SentryLevel.INFO, "Initializing SDK with DSN: '%s'", options.getDsn());
        if (options.getOutboxPath() != null) {
            File outboxDir = new File(options.getOutboxPath());
            outboxDir.mkdirs();
        } else {
            logger.log(SentryLevel.INFO, "No outbox dir path is defined in options.", new Object[0]);
        }
        if (options.getCacheDirPath() != null && !options.getCacheDirPath().isEmpty()) {
            File cacheDir = new File(options.getCacheDirPath());
            cacheDir.mkdirs();
            options.setEnvelopeDiskCache(EnvelopeCache.create(options));
        }
        return true;
    }

    public static synchronized void close() {
        synchronized (Sentry.class) {
            IHub hub = getCurrentHub();
            mainHub = NoOpHub.getInstance();
            currentHub.remove();
            hub.close();
        }
    }

    @NotNull
    public static SentryId captureEvent(@NotNull SentryEvent event) {
        return getCurrentHub().captureEvent(event);
    }

    @NotNull
    public static SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        return getCurrentHub().captureEvent(event, hint);
    }

    @NotNull
    public static SentryId captureMessage(@NotNull String message) {
        return getCurrentHub().captureMessage(message);
    }

    @NotNull
    public static SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        return getCurrentHub().captureMessage(message, level);
    }

    @NotNull
    public static SentryId captureException(@NotNull Throwable throwable) {
        return getCurrentHub().captureException(throwable);
    }

    @NotNull
    public static SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        return getCurrentHub().captureException(throwable, hint);
    }

    public static void captureUserFeedback(@NotNull UserFeedback userFeedback) {
        getCurrentHub().captureUserFeedback(userFeedback);
    }

    public static void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        getCurrentHub().addBreadcrumb(breadcrumb, hint);
    }

    public static void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        getCurrentHub().addBreadcrumb(breadcrumb);
    }

    public static void addBreadcrumb(@NotNull String message) {
        getCurrentHub().addBreadcrumb(message);
    }

    public static void addBreadcrumb(@NotNull String message, @NotNull String category) {
        getCurrentHub().addBreadcrumb(message, category);
    }

    public static void setLevel(@Nullable SentryLevel level) {
        getCurrentHub().setLevel(level);
    }

    public static void setTransaction(@Nullable String transaction) {
        getCurrentHub().setTransaction(transaction);
    }

    public static void setUser(@Nullable User user) {
        getCurrentHub().setUser(user);
    }

    public static void setFingerprint(@NotNull List<String> fingerprint) {
        getCurrentHub().setFingerprint(fingerprint);
    }

    public static void clearBreadcrumbs() {
        getCurrentHub().clearBreadcrumbs();
    }

    public static void setTag(@NotNull String key, @NotNull String value) {
        getCurrentHub().setTag(key, value);
    }

    public static void removeTag(@NotNull String key) {
        getCurrentHub().removeTag(key);
    }

    public static void setExtra(@NotNull String key, @NotNull String value) {
        getCurrentHub().setExtra(key, value);
    }

    public static void removeExtra(@NotNull String key) {
        getCurrentHub().removeExtra(key);
    }

    @NotNull
    public static SentryId getLastEventId() {
        return getCurrentHub().getLastEventId();
    }

    public static void pushScope() {
        if (!globalHubMode) {
            getCurrentHub().pushScope();
        }
    }

    public static void popScope() {
        if (!globalHubMode) {
            getCurrentHub().popScope();
        }
    }

    public static void withScope(@NotNull ScopeCallback callback) {
        getCurrentHub().withScope(callback);
    }

    public static void configureScope(@NotNull ScopeCallback callback) {
        getCurrentHub().configureScope(callback);
    }

    public static void bindClient(@NotNull ISentryClient client) {
        getCurrentHub().bindClient(client);
    }

    public static void flush(long timeoutMillis) {
        getCurrentHub().flush(timeoutMillis);
    }

    public static void startSession() {
        getCurrentHub().startSession();
    }

    public static void endSession() {
        getCurrentHub().endSession();
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation) {
        return getCurrentHub().startTransaction(name, operation);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation, boolean bindToScope) {
        return getCurrentHub().startTransaction(name, operation, bindToScope);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation, @Nullable String description) {
        return startTransaction(name, operation, description, false);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation, @Nullable String description, boolean bindToScope) {
        ITransaction transaction = getCurrentHub().startTransaction(name, operation, bindToScope);
        transaction.setDescription(description);
        return transaction;
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts) {
        return getCurrentHub().startTransaction(transactionContexts);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts, boolean bindToScope) {
        return getCurrentHub().startTransaction(transactionContexts, bindToScope);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation, @NotNull CustomSamplingContext customSamplingContext) {
        return getCurrentHub().startTransaction(name, operation, customSamplingContext);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull String name, @NotNull String operation, @NotNull CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return getCurrentHub().startTransaction(name, operation, customSamplingContext, bindToScope);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @NotNull CustomSamplingContext customSamplingContext) {
        return getCurrentHub().startTransaction(transactionContexts, customSamplingContext);
    }

    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        return getCurrentHub().startTransaction(transactionContexts, customSamplingContext, bindToScope);
    }

    @ApiStatus.Internal
    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp) {
        return getCurrentHub().startTransaction(transactionContexts, customSamplingContext, bindToScope, startTimestamp);
    }

    @ApiStatus.Internal
    @NotNull
    public static ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        return getCurrentHub().startTransaction(transactionContexts, customSamplingContext, bindToScope, startTimestamp, waitForChildren, transactionFinishedCallback);
    }

    @Nullable
    public static SentryTraceHeader traceHeaders() {
        return getCurrentHub().traceHeaders();
    }

    @Nullable
    public static ISpan getSpan() {
        return getCurrentHub().getSpan();
    }

    @Nullable
    public static Boolean isCrashedLastRun() {
        return getCurrentHub().isCrashedLastRun();
    }
}
