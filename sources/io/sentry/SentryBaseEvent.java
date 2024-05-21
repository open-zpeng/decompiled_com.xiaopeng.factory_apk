package io.sentry;

import io.sentry.exception.ExceptionMechanismException;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Request;
import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import io.sentry.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public abstract class SentryBaseEvent {
    public static final String DEFAULT_PLATFORM = "java";
    @Nullable
    private List<Breadcrumb> breadcrumbs;
    @NotNull
    private final Contexts contexts;
    @Nullable
    private String dist;
    @Nullable
    private String environment;
    @Nullable
    private SentryId eventId;
    @Nullable
    private Map<String, Object> extra;
    @Nullable
    private String platform;
    @Nullable
    private String release;
    @Nullable
    private Request request;
    @Nullable
    private SdkVersion sdk;
    @Nullable
    private String serverName;
    @Nullable
    private Map<String, String> tags;
    @Nullable
    protected transient Throwable throwable;
    @Nullable
    private User user;

    /* JADX INFO: Access modifiers changed from: protected */
    public SentryBaseEvent(@NotNull SentryId eventId) {
        this.contexts = new Contexts();
        this.eventId = eventId;
    }

    protected SentryBaseEvent() {
        this(new SentryId());
    }

    @Nullable
    public SentryId getEventId() {
        return this.eventId;
    }

    public void setEventId(@Nullable SentryId eventId) {
        this.eventId = eventId;
    }

    @NotNull
    public Contexts getContexts() {
        return this.contexts;
    }

    @Nullable
    public SdkVersion getSdk() {
        return this.sdk;
    }

    public void setSdk(@Nullable SdkVersion sdk) {
        this.sdk = sdk;
    }

    @Nullable
    public Request getRequest() {
        return this.request;
    }

    public void setRequest(@Nullable Request request) {
        this.request = request;
    }

    @Nullable
    public Throwable getThrowable() {
        Throwable ex = this.throwable;
        if (ex instanceof ExceptionMechanismException) {
            return ((ExceptionMechanismException) ex).getThrowable();
        }
        return ex;
    }

    @Deprecated
    @Nullable
    public Throwable getOriginThrowable() {
        return getThrowable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Throwable getThrowableMechanism() {
        return this.throwable;
    }

    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }

    @ApiStatus.Internal
    @Nullable
    public Map<String, String> getTags() {
        return this.tags;
    }

    public void setTags(@Nullable Map<String, String> tags) {
        this.tags = CollectionUtils.newHashMap(tags);
    }

    public void removeTag(@NotNull String key) {
        Map<String, String> map = this.tags;
        if (map != null) {
            map.remove(key);
        }
    }

    @Nullable
    public String getTag(@NotNull String key) {
        Map<String, String> map = this.tags;
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public void setTag(@NotNull String key, @NotNull String value) {
        if (this.tags == null) {
            this.tags = new HashMap();
        }
        this.tags.put(key, value);
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
    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(@Nullable String platform) {
        this.platform = platform;
    }

    @Nullable
    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    @Nullable
    public String getDist() {
        return this.dist;
    }

    public void setDist(@Nullable String dist) {
        this.dist = dist;
    }

    @Nullable
    public User getUser() {
        return this.user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
    }

    @Nullable
    public List<Breadcrumb> getBreadcrumbs() {
        return this.breadcrumbs;
    }

    public void setBreadcrumbs(@Nullable List<Breadcrumb> breadcrumbs) {
        this.breadcrumbs = CollectionUtils.newArrayList(breadcrumbs);
    }

    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        if (this.breadcrumbs == null) {
            this.breadcrumbs = new ArrayList();
        }
        this.breadcrumbs.add(breadcrumb);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Map<String, Object> getExtras() {
        return this.extra;
    }

    public void setExtras(@Nullable Map<String, Object> extra) {
        this.extra = CollectionUtils.newHashMap(extra);
    }

    public void setExtra(@NotNull String key, @NotNull Object value) {
        if (this.extra == null) {
            this.extra = new HashMap();
        }
        this.extra.put(key, value);
    }

    public void removeExtra(@NotNull String key) {
        Map<String, Object> map = this.extra;
        if (map != null) {
            map.remove(key);
        }
    }

    @Nullable
    public Object getExtra(@NotNull String key) {
        Map<String, Object> map = this.extra;
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    public void addBreadcrumb(@Nullable String message) {
        addBreadcrumb(new Breadcrumb(message));
    }
}
