package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Message implements IUnknownPropertiesConsumer {
    @Nullable
    private String formatted;
    @Nullable
    private String message;
    @Nullable
    private List<String> params;
    @Nullable
    private Map<String, Object> unknown;

    @Nullable
    public String getFormatted() {
        return this.formatted;
    }

    public void setFormatted(@Nullable String formatted) {
        this.formatted = formatted;
    }

    @Nullable
    public String getMessage() {
        return this.message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public List<String> getParams() {
        return this.params;
    }

    public void setParams(@Nullable List<String> params) {
        this.params = CollectionUtils.newArrayList(params);
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
