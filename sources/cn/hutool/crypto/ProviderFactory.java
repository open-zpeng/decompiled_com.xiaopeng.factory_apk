package cn.hutool.crypto;

import java.security.Provider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/* loaded from: classes.dex */
public class ProviderFactory {
    public static Provider createBouncyCastleProvider() {
        return new BouncyCastleProvider();
    }
}
