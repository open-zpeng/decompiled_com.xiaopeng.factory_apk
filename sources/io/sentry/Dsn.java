package io.sentry;

import java.net.URI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class Dsn {
    @Nullable
    private final String path;
    @NotNull
    private final String projectId;
    @NotNull
    private final String publicKey;
    @Nullable
    private final String secretKey;
    @NotNull
    private final URI sentryUri;

    @NotNull
    public String getProjectId() {
        return this.projectId;
    }

    @Nullable
    public String getPath() {
        return this.path;
    }

    @Nullable
    public String getSecretKey() {
        return this.secretKey;
    }

    @NotNull
    public String getPublicKey() {
        return this.publicKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public URI getSentryUri() {
        return this.sentryUri;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Dsn(@Nullable String dsn) throws IllegalArgumentException {
        try {
            try {
                URI uri = new URI(dsn).normalize();
                String userInfo = uri.getUserInfo();
                if (userInfo == null || userInfo.isEmpty()) {
                    throw new IllegalArgumentException("Invalid DSN: No public key provided.");
                }
                String[] keys = userInfo.split(":", -1);
                this.publicKey = keys[0];
                if (this.publicKey != null && !this.publicKey.isEmpty()) {
                    this.secretKey = keys.length > 1 ? keys[1] : null;
                    String uriPath = uri.getPath();
                    uriPath = uriPath.endsWith("/") ? uriPath.substring(0, uriPath.length() - 1) : uriPath;
                    int projectIdStart = uriPath.lastIndexOf("/") + 1;
                    String path = uriPath.substring(0, projectIdStart);
                    if (!path.endsWith("/")) {
                        path = path + "/";
                    }
                    this.path = path;
                    this.projectId = uriPath.substring(projectIdStart);
                    if (this.projectId.isEmpty()) {
                        throw new IllegalArgumentException("Invalid DSN: A Project Id is required.");
                    }
                    this.sentryUri = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), path + "api/" + this.projectId, null, null);
                    return;
                }
                throw new IllegalArgumentException("Invalid DSN: No public key provided.");
            } catch (Throwable th) {
                e = th;
                throw new IllegalArgumentException(e);
            }
        } catch (Throwable th2) {
            e = th2;
        }
    }
}
