package io.sentry.transport;

import java.net.Authenticator;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
final class AuthenticatorWrapper {
    private static final AuthenticatorWrapper instance = new AuthenticatorWrapper();

    public static AuthenticatorWrapper getInstance() {
        return instance;
    }

    private AuthenticatorWrapper() {
    }

    public void setDefault(@NotNull Authenticator authenticator) {
        Authenticator.setDefault(authenticator);
    }
}
