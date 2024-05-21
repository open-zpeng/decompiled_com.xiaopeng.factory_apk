package cn.hutool.core.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
/* loaded from: classes.dex */
public class UserPassAuthenticator extends Authenticator {
    private final char[] pass;
    private final String user;

    public UserPassAuthenticator(String user, char[] pass) {
        this.user = user;
        this.pass = pass;
    }

    @Override // java.net.Authenticator
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.pass);
    }
}
