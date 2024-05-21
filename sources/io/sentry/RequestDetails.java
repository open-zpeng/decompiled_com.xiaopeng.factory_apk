package io.sentry;

import io.sentry.util.Objects;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class RequestDetails {
    @NotNull
    private final Map<String, String> headers;
    @NotNull
    private final URL url;

    public RequestDetails(@NotNull String url, @NotNull Map<String, String> headers) {
        Objects.requireNonNull(url, "url is required");
        Objects.requireNonNull(headers, "headers is required");
        try {
            this.url = URI.create(url).toURL();
            this.headers = headers;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to compose the Sentry's server URL.", e);
        }
    }

    @NotNull
    public URL getUrl() {
        return this.url;
    }

    @NotNull
    public Map<String, String> getHeaders() {
        return this.headers;
    }
}
