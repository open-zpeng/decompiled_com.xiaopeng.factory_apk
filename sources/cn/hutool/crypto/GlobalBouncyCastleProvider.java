package cn.hutool.crypto;

import java.security.Provider;
/* loaded from: classes.dex */
public enum GlobalBouncyCastleProvider {
    INSTANCE;
    
    private static boolean useBouncyCastle = true;
    private Provider provider;

    GlobalBouncyCastleProvider() {
        try {
            this.provider = ProviderFactory.createBouncyCastleProvider();
        } catch (NoClassDefFoundError e) {
        }
    }

    public Provider getProvider() {
        if (useBouncyCastle) {
            return this.provider;
        }
        return null;
    }

    public static void setUseBouncyCastle(boolean isUseBouncyCastle) {
        useBouncyCastle = isUseBouncyCastle;
    }
}
