package cn.hutool.core.net;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
/* loaded from: classes.dex */
public class SSLContextBuilder implements SSLProtocols {
    private KeyManager[] keyManagers;
    private String protocol = "TLS";
    private TrustManager[] trustManagers = {DefaultTrustManager.INSTANCE};
    private SecureRandom secureRandom = new SecureRandom();

    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    public SSLContextBuilder setProtocol(String protocol) {
        if (StrUtil.isNotBlank(protocol)) {
            this.protocol = protocol;
        }
        return this;
    }

    public SSLContextBuilder setTrustManagers(TrustManager... trustManagers) {
        if (ArrayUtil.isNotEmpty((Object[]) trustManagers)) {
            this.trustManagers = trustManagers;
        }
        return this;
    }

    public SSLContextBuilder setKeyManagers(KeyManager... keyManagers) {
        if (ArrayUtil.isNotEmpty((Object[]) keyManagers)) {
            this.keyManagers = keyManagers;
        }
        return this;
    }

    public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.secureRandom = secureRandom;
        }
        return this;
    }

    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(this.protocol);
        sslContext.init(this.keyManagers, this.trustManagers, this.secureRandom);
        return sslContext;
    }

    public SSLContext buildQuietly() throws IORuntimeException {
        try {
            return build();
        } catch (GeneralSecurityException e) {
            throw new IORuntimeException(e);
        }
    }
}
