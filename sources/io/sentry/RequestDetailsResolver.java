package io.sentry;

import io.sentry.util.Objects;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
final class RequestDetailsResolver {
    private static final String SENTRY_AUTH = "X-Sentry-Auth";
    private static final String USER_AGENT = "User-Agent";
    @NotNull
    private final SentryOptions options;

    public RequestDetailsResolver(@NotNull SentryOptions options) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "options is required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public RequestDetails resolve() {
        String str;
        Dsn dsn = new Dsn(this.options.getDsn());
        URI sentryUri = dsn.getSentryUri();
        String envelopeUrl = sentryUri.resolve(sentryUri.getPath() + "/envelope/").toString();
        String publicKey = dsn.getPublicKey();
        String secretKey = dsn.getSecretKey();
        StringBuilder sb = new StringBuilder();
        sb.append("Sentry sentry_version=7,sentry_client=");
        sb.append(this.options.getSentryClientName());
        sb.append(",sentry_key=");
        sb.append(publicKey);
        if (secretKey == null || secretKey.length() <= 0) {
            str = "";
        } else {
            str = ",sentry_secret=" + secretKey;
        }
        sb.append(str);
        String authHeader = sb.toString();
        String userAgent = this.options.getSentryClientName();
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", userAgent);
        headers.put(SENTRY_AUTH, authHeader);
        return new RequestDetails(envelopeUrl, headers);
    }
}
