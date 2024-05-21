package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import io.sentry.util.CollectionUtils;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class Request implements IUnknownPropertiesConsumer {
    @Nullable
    private String cookies;
    @Nullable
    private Object data;
    @Nullable
    private Map<String, String> env;
    @Nullable
    private Map<String, String> headers;
    @Nullable
    private String method;
    @Nullable
    private Map<String, String> other;
    @Nullable
    private String queryString;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private String url;

    public Request() {
    }

    public Request(@NotNull Request request) {
        this.url = request.url;
        this.cookies = request.cookies;
        this.method = request.method;
        this.queryString = request.queryString;
        this.headers = CollectionUtils.newConcurrentHashMap(request.headers);
        this.env = CollectionUtils.newConcurrentHashMap(request.env);
        this.other = CollectionUtils.newConcurrentHashMap(request.other);
        this.unknown = CollectionUtils.newConcurrentHashMap(request.unknown);
        this.data = request.data;
    }

    @Nullable
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getMethod() {
        return this.method;
    }

    public void setMethod(@Nullable String method) {
        this.method = method;
    }

    @Nullable
    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(@Nullable String queryString) {
        this.queryString = queryString;
    }

    @Nullable
    public Object getData() {
        return this.data;
    }

    public void setData(@Nullable Object data) {
        this.data = data;
    }

    @Nullable
    public String getCookies() {
        return this.cookies;
    }

    public void setCookies(@Nullable String cookies) {
        this.cookies = cookies;
    }

    @Nullable
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(@Nullable Map<String, String> headers) {
        this.headers = CollectionUtils.newConcurrentHashMap(headers);
    }

    @Nullable
    public Map<String, String> getEnvs() {
        return this.env;
    }

    public void setEnvs(@Nullable Map<String, String> env) {
        this.env = CollectionUtils.newConcurrentHashMap(env);
    }

    @Nullable
    public Map<String, String> getOthers() {
        return this.other;
    }

    public void setOthers(@Nullable Map<String, String> other) {
        this.other = CollectionUtils.newConcurrentHashMap(other);
    }

    @TestOnly
    @Nullable
    Map<String, Object> getUnknown() {
        return this.unknown;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
