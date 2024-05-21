package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryThread implements IUnknownPropertiesConsumer {
    @Nullable
    private Boolean crashed;
    @Nullable
    private Boolean current;
    @Nullable
    private Boolean daemon;
    @Nullable
    private Long id;
    @Nullable
    private String name;
    @Nullable
    private Integer priority;
    @Nullable
    private SentryStackTrace stacktrace;
    @Nullable
    private String state;
    @Nullable
    private Map<String, Object> unknown;

    @Nullable
    public Long getId() {
        return this.id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Boolean isCrashed() {
        return this.crashed;
    }

    public void setCrashed(@Nullable Boolean crashed) {
        this.crashed = crashed;
    }

    @Nullable
    public Boolean isCurrent() {
        return this.current;
    }

    public void setCurrent(@Nullable Boolean current) {
        this.current = current;
    }

    @Nullable
    public SentryStackTrace getStacktrace() {
        return this.stacktrace;
    }

    public void setStacktrace(@Nullable SentryStackTrace stacktrace) {
        this.stacktrace = stacktrace;
    }

    @Nullable
    public Integer getPriority() {
        return this.priority;
    }

    public void setPriority(@Nullable Integer priority) {
        this.priority = priority;
    }

    @Nullable
    public Boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(@Nullable Boolean daemon) {
        this.daemon = daemon;
    }

    @Nullable
    public String getState() {
        return this.state;
    }

    public void setState(@Nullable String state) {
        this.state = state;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
