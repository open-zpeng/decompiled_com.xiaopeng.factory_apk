package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.asymmetric.BaseAsymmetric;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes.dex */
public class BaseAsymmetric<T extends BaseAsymmetric<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    protected String algorithm;
    protected final Lock lock = new ReentrantLock();
    protected PrivateKey privateKey;
    protected PublicKey publicKey;

    public BaseAsymmetric(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        init(algorithm, privateKey, publicKey);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithm = algorithm;
        if (privateKey == null && publicKey == null) {
            initKeys();
        } else {
            if (privateKey != null) {
                this.privateKey = privateKey;
            }
            if (publicKey != null) {
                this.publicKey = publicKey;
            }
        }
        return this;
    }

    public T initKeys() {
        KeyPair keyPair = KeyUtil.generateKeyPair(this.algorithm);
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
        return this;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getPublicKeyBase64() {
        PublicKey publicKey = getPublicKey();
        if (publicKey == null) {
            return null;
        }
        return Base64.encode(publicKey.getEncoded());
    }

    public T setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public String getPrivateKeyBase64() {
        PrivateKey privateKey = getPrivateKey();
        if (privateKey == null) {
            return null;
        }
        return Base64.encode(privateKey.getEncoded());
    }

    public T setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public T setKey(Key key) {
        Assert.notNull(key, "key must be not null !", new Object[0]);
        if (key instanceof PublicKey) {
            return setPublicKey((PublicKey) key);
        }
        if (key instanceof PrivateKey) {
            return setPrivateKey((PrivateKey) key);
        }
        throw new CryptoException("Unsupported key type: {}", key.getClass());
    }

    /* renamed from: cn.hutool.crypto.asymmetric.BaseAsymmetric$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$crypto$asymmetric$KeyType = new int[KeyType.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$crypto$asymmetric$KeyType[KeyType.PrivateKey.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$crypto$asymmetric$KeyType[KeyType.PublicKey.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Key getKeyByType(KeyType type) {
        int i = AnonymousClass1.$SwitchMap$cn$hutool$crypto$asymmetric$KeyType[type.ordinal()];
        if (i == 1) {
            PrivateKey privateKey = this.privateKey;
            if (privateKey == null) {
                throw new NullPointerException("Private key must not null when use it !");
            }
            return privateKey;
        } else if (i == 2) {
            PublicKey publicKey = this.publicKey;
            if (publicKey == null) {
                throw new NullPointerException("Public key must not null when use it !");
            }
            return publicKey;
        } else {
            throw new CryptoException("Unsupported key type: " + type);
        }
    }
}
