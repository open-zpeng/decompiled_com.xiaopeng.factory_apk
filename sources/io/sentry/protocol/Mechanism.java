package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Mechanism implements IUnknownPropertiesConsumer {
    @Nullable
    private Map<String, Object> data;
    @Nullable
    private String description;
    @Nullable
    private Boolean handled;
    @Nullable
    private String helpLink;
    @Nullable
    private Map<String, Object> meta;
    @Nullable
    private Boolean synthetic;
    @Nullable
    private final transient Thread thread;
    @Nullable
    private String type;
    @Nullable
    private Map<String, Object> unknown;

    public Mechanism() {
        this(null);
    }

    public Mechanism(@Nullable Thread thread) {
        this.thread = thread;
    }

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getHelpLink() {
        return this.helpLink;
    }

    public void setHelpLink(@Nullable String helpLink) {
        this.helpLink = helpLink;
    }

    @Nullable
    public Boolean isHandled() {
        return this.handled;
    }

    public void setHandled(@Nullable Boolean handled) {
        this.handled = handled;
    }

    @Nullable
    public Map<String, Object> getMeta() {
        return this.meta;
    }

    public void setMeta(@Nullable Map<String, Object> meta) {
        this.meta = CollectionUtils.newHashMap(meta);
    }

    @Nullable
    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(@Nullable Map<String, Object> data) {
        this.data = CollectionUtils.newHashMap(data);
    }

    @Nullable
    Thread getThread() {
        return this.thread;
    }

    @Nullable
    public Boolean getSynthetic() {
        return this.synthetic;
    }

    public void setSynthetic(@Nullable Boolean synthetic) {
        this.synthetic = synthetic;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
