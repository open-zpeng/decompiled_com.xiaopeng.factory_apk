package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class User implements IUnknownPropertiesConsumer {
    @Nullable
    private String email;
    @Nullable
    private String id;
    @Nullable
    private String ipAddress;
    @Nullable
    private Map<String, String> other;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private String username;

    public User() {
    }

    public User(@NotNull User user) {
        this.email = user.email;
        this.username = user.username;
        this.id = user.id;
        this.ipAddress = user.ipAddress;
        this.other = CollectionUtils.newConcurrentHashMap(user.other);
        this.unknown = CollectionUtils.newConcurrentHashMap(user.unknown);
    }

    @Nullable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    @Nullable
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(@Nullable String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Nullable
    public Map<String, String> getOthers() {
        return this.other;
    }

    public void setOthers(@Nullable Map<String, String> other) {
        this.other = CollectionUtils.newConcurrentHashMap(other);
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = new ConcurrentHashMap(unknown);
    }

    @TestOnly
    @Nullable
    Map<String, Object> getUnknown() {
        return this.unknown;
    }
}
