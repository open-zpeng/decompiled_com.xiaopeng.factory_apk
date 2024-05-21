package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.Objects;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryPackage implements IUnknownPropertiesConsumer {
    @NotNull
    private String name;
    @Nullable
    private Map<String, Object> unknown;
    @NotNull
    private String version;

    public SentryPackage(@NotNull String name, @NotNull String version) {
        this.name = (String) Objects.requireNonNull(name, "name is required.");
        this.version = (String) Objects.requireNonNull(version, "version is required.");
    }

    @Deprecated
    public SentryPackage() {
        this("", "");
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = (String) Objects.requireNonNull(name, "name is required.");
    }

    @NotNull
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@NotNull String version) {
        this.version = (String) Objects.requireNonNull(version, "version is required.");
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
