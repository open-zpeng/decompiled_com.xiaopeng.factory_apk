package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryException implements IUnknownPropertiesConsumer {
    @Nullable
    private Mechanism mechanism;
    @Nullable
    private String module;
    @Nullable
    private SentryStackTrace stacktrace;
    @Nullable
    private Long threadId;
    @Nullable
    private String type;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private String value;

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getValue() {
        return this.value;
    }

    public void setValue(@Nullable String value) {
        this.value = value;
    }

    @Nullable
    public String getModule() {
        return this.module;
    }

    public void setModule(@Nullable String module) {
        this.module = module;
    }

    @Nullable
    public Long getThreadId() {
        return this.threadId;
    }

    public void setThreadId(@Nullable Long threadId) {
        this.threadId = threadId;
    }

    @Nullable
    public SentryStackTrace getStacktrace() {
        return this.stacktrace;
    }

    public void setStacktrace(@Nullable SentryStackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Nullable
    public Mechanism getMechanism() {
        return this.mechanism;
    }

    public void setMechanism(@Nullable Mechanism mechanism) {
        this.mechanism = mechanism;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
