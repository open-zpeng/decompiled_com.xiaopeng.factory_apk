package io.sentry.transport;

import io.sentry.util.Objects;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class ProxyAuthenticator extends Authenticator {
    @NotNull
    private final String password;
    @NotNull
    private final String user;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProxyAuthenticator(@NotNull String user, @NotNull String password) {
        this.user = (String) Objects.requireNonNull(user, "user is required");
        this.password = (String) Objects.requireNonNull(password, "password is required");
    }

    @Override // java.net.Authenticator
    @Nullable
    protected PasswordAuthentication getPasswordAuthentication() {
        if (getRequestorType() == Authenticator.RequestorType.PROXY) {
            return new PasswordAuthentication(this.user, this.password.toCharArray());
        }
        return null;
    }

    @NotNull
    String getUser() {
        return this.user;
    }

    @NotNull
    String getPassword() {
        return this.password;
    }
}
