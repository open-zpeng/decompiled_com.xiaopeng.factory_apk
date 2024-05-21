package io.sentry;

import io.sentry.protocol.DebugMeta;
import io.sentry.protocol.Message;
import io.sentry.protocol.SentryException;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryThread;
import io.sentry.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class SentryEvent extends SentryBaseEvent implements IUnknownPropertiesConsumer {
    @Nullable
    private DebugMeta debugMeta;
    @Nullable
    private SentryValues<SentryException> exception;
    @Nullable
    private List<String> fingerprint;
    @Nullable
    private SentryLevel level;
    @Nullable
    private String logger;
    @Nullable
    private Message message;
    @Nullable
    private Map<String, String> modules;
    @Nullable
    private SentryValues<SentryThread> threads;
    @NotNull
    private final Date timestamp;
    @Nullable
    private String transaction;
    @Nullable
    private Map<String, Object> unknown;

    SentryEvent(@NotNull SentryId eventId, @NotNull Date timestamp) {
        super(eventId);
        this.timestamp = timestamp;
    }

    public SentryEvent(@Nullable Throwable throwable) {
        this();
        this.throwable = throwable;
    }

    public SentryEvent() {
        this(new SentryId(), DateUtils.getCurrentDateTime());
    }

    @TestOnly
    public SentryEvent(@NotNull Date timestamp) {
        this(new SentryId(), timestamp);
    }

    public Date getTimestamp() {
        return (Date) this.timestamp.clone();
    }

    @Nullable
    public Message getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable Message message) {
        this.message = message;
    }

    @Nullable
    public String getLogger() {
        return this.logger;
    }

    public void setLogger(@Nullable String logger) {
        this.logger = logger;
    }

    @Nullable
    public List<SentryThread> getThreads() {
        SentryValues<SentryThread> sentryValues = this.threads;
        if (sentryValues != null) {
            return sentryValues.getValues();
        }
        return null;
    }

    public void setThreads(@Nullable List<SentryThread> threads) {
        this.threads = new SentryValues<>(threads);
    }

    @Nullable
    public List<SentryException> getExceptions() {
        SentryValues<SentryException> sentryValues = this.exception;
        if (sentryValues == null) {
            return null;
        }
        return sentryValues.getValues();
    }

    public void setExceptions(@Nullable List<SentryException> exception) {
        this.exception = new SentryValues<>(exception);
    }

    @Nullable
    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(@Nullable SentryLevel level) {
        this.level = level;
    }

    @Nullable
    public String getTransaction() {
        return this.transaction;
    }

    public void setTransaction(@Nullable String transaction) {
        this.transaction = transaction;
    }

    @Nullable
    public List<String> getFingerprints() {
        return this.fingerprint;
    }

    public void setFingerprints(@Nullable List<String> fingerprint) {
        this.fingerprint = fingerprint != null ? new ArrayList(fingerprint) : null;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @TestOnly
    @Nullable
    public Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @Nullable
    Map<String, String> getModules() {
        return this.modules;
    }

    public void setModules(@Nullable Map<String, String> modules) {
        this.modules = CollectionUtils.newHashMap(modules);
    }

    public void setModule(@NotNull String key, @NotNull String value) {
        if (this.modules == null) {
            this.modules = new HashMap();
        }
        this.modules.put(key, value);
    }

    public void removeModule(@NotNull String key) {
        Map<String, String> map = this.modules;
        if (map != null) {
            map.remove(key);
        }
    }

    @Nullable
    public String getModule(@NotNull String key) {
        Map<String, String> map = this.modules;
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    @Nullable
    public DebugMeta getDebugMeta() {
        return this.debugMeta;
    }

    public void setDebugMeta(@Nullable DebugMeta debugMeta) {
        this.debugMeta = debugMeta;
    }

    public boolean isCrashed() {
        SentryValues<SentryException> sentryValues = this.exception;
        if (sentryValues != null) {
            for (SentryException e : sentryValues.getValues()) {
                if (e.getMechanism() != null && e.getMechanism().isHandled() != null && !e.getMechanism().isHandled().booleanValue()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean isErrored() {
        SentryValues<SentryException> sentryValues = this.exception;
        return (sentryValues == null || sentryValues.getValues().isEmpty()) ? false : true;
    }
}
