package cn.hutool.crypto.asymmetric;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.SecureUtil;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.crypto.signers.PlainDSAEncoding;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
/* loaded from: classes.dex */
public class SM2 extends AbstractAsymmetricCrypto<SM2> {
    private static final String ALGORITHM_SM2 = "SM2";
    private static final long serialVersionUID = 1;
    private Digest digest;
    private DSAEncoding encoding;
    protected SM2Engine engine;
    private SM2Engine.Mode mode;
    private ECPrivateKeyParameters privateKeyParams;
    private ECPublicKeyParameters publicKeyParams;
    protected SM2Signer signer;

    public SM2() {
        this((byte[]) null, (byte[]) null);
    }

    public SM2(String privateKeyStr, String publicKeyStr) {
        this(SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
    }

    public SM2(byte[] privateKey, byte[] publicKey) {
        this(ECKeyUtil.decodePrivateKeyParams(privateKey), ECKeyUtil.decodePublicKeyParams(publicKey));
    }

    public SM2(PrivateKey privateKey, PublicKey publicKey) {
        this(BCUtil.toParams(privateKey), BCUtil.toParams(publicKey));
        if (privateKey != null) {
            this.privateKey = privateKey;
        }
        if (publicKey != null) {
            this.publicKey = publicKey;
        }
    }

    public SM2(String privateKeyHex, String publicKeyPointXHex, String publicKeyPointYHex) {
        this(BCUtil.toSm2Params(privateKeyHex), BCUtil.toSm2Params(publicKeyPointXHex, publicKeyPointYHex));
    }

    public SM2(byte[] privateKey, byte[] publicKeyPointX, byte[] publicKeyPointY) {
        this(BCUtil.toSm2Params(privateKey), BCUtil.toSm2Params(publicKeyPointX, publicKeyPointY));
    }

    public SM2(ECPrivateKeyParameters privateKeyParams, ECPublicKeyParameters publicKeyParams) {
        super(ALGORITHM_SM2, null, null);
        this.encoding = StandardDSAEncoding.INSTANCE;
        this.digest = new SM3Digest();
        this.mode = SM2Engine.Mode.C1C3C2;
        this.privateKeyParams = privateKeyParams;
        this.publicKeyParams = publicKeyParams;
        init();
    }

    public SM2 init() {
        if (this.privateKeyParams == null && this.publicKeyParams == null) {
            super.initKeys();
            this.privateKeyParams = BCUtil.toParams(this.privateKey);
            this.publicKeyParams = BCUtil.toParams(this.publicKey);
        }
        return this;
    }

    @Override // cn.hutool.crypto.asymmetric.BaseAsymmetric
    public SM2 initKeys() {
        return this;
    }

    public byte[] encrypt(byte[] data) throws CryptoException {
        return encrypt(data, KeyType.PublicKey);
    }

    @Override // cn.hutool.crypto.asymmetric.AsymmetricEncryptor
    public byte[] encrypt(byte[] data, KeyType keyType) throws CryptoException {
        if (KeyType.PublicKey != keyType) {
            throw new IllegalArgumentException("Encrypt is only support by public key");
        }
        return encrypt(data, (CipherParameters) new ParametersWithRandom(getCipherParameters(keyType)));
    }

    public byte[] encrypt(byte[] data, CipherParameters pubKeyParameters) throws CryptoException {
        this.lock.lock();
        SM2Engine engine = getEngine();
        try {
            try {
                engine.init(true, pubKeyParameters);
                return engine.processBlock(data, 0, data.length);
            } catch (InvalidCipherTextException e) {
                throw new CryptoException((Throwable) e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public byte[] decrypt(byte[] data) throws CryptoException {
        return decrypt(data, KeyType.PrivateKey);
    }

    @Override // cn.hutool.crypto.asymmetric.AsymmetricDecryptor
    public byte[] decrypt(byte[] data, KeyType keyType) throws CryptoException {
        if (KeyType.PrivateKey != keyType) {
            throw new IllegalArgumentException("Decrypt is only support by private key");
        }
        return decrypt(data, getCipherParameters(keyType));
    }

    public byte[] decrypt(byte[] data, CipherParameters privateKeyParameters) throws CryptoException {
        this.lock.lock();
        SM2Engine engine = getEngine();
        try {
            try {
                engine.init(false, privateKeyParameters);
                return engine.processBlock(data, 0, data.length);
            } catch (InvalidCipherTextException e) {
                throw new CryptoException((Throwable) e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public String signHex(String dataHex) {
        return signHex(dataHex, null);
    }

    public byte[] sign(byte[] data) {
        return sign(data, null);
    }

    public String signHex(String dataHex, String idHex) {
        return HexUtil.encodeHexStr(sign(HexUtil.decodeHex(dataHex), HexUtil.decodeHex(idHex)));
    }

    public byte[] sign(byte[] data, byte[] id) {
        this.lock.lock();
        SM2Signer signer = getSigner();
        try {
            try {
                CipherParameters param = new ParametersWithRandom(getCipherParameters(KeyType.PrivateKey));
                if (id != null) {
                    param = new ParametersWithID(param, id);
                }
                signer.init(true, param);
                signer.update(data, 0, data.length);
                return signer.generateSignature();
            } catch (org.bouncycastle.crypto.CryptoException e) {
                throw new CryptoException((Throwable) e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public boolean verifyHex(String dataHex, String signHex) {
        return verifyHex(dataHex, signHex, null);
    }

    public boolean verify(byte[] data, byte[] sign) {
        return verify(data, sign, null);
    }

    public boolean verifyHex(String dataHex, String signHex, String idHex) {
        return verify(HexUtil.decodeHex(dataHex), HexUtil.decodeHex(signHex), HexUtil.decodeHex(idHex));
    }

    public boolean verify(byte[] data, byte[] sign, byte[] id) {
        this.lock.lock();
        SM2Signer signer = getSigner();
        try {
            CipherParameters param = getCipherParameters(KeyType.PublicKey);
            if (id != null) {
                param = new ParametersWithID(param, id);
            }
            signer.init(false, param);
            signer.update(data, 0, data.length);
            return signer.verifySignature(sign);
        } finally {
            this.lock.unlock();
        }
    }

    @Override // cn.hutool.crypto.asymmetric.BaseAsymmetric
    public SM2 setPrivateKey(PrivateKey privateKey) {
        super.setPrivateKey(privateKey);
        this.privateKeyParams = BCUtil.toParams(privateKey);
        return this;
    }

    public SM2 setPrivateKeyParams(ECPrivateKeyParameters privateKeyParams) {
        this.privateKeyParams = privateKeyParams;
        return this;
    }

    @Override // cn.hutool.crypto.asymmetric.BaseAsymmetric
    public SM2 setPublicKey(PublicKey publicKey) {
        super.setPublicKey(publicKey);
        this.publicKeyParams = BCUtil.toParams(publicKey);
        return this;
    }

    public SM2 setPublicKeyParams(ECPublicKeyParameters publicKeyParams) {
        this.publicKeyParams = publicKeyParams;
        return this;
    }

    public SM2 usePlainEncoding() {
        return setEncoding(PlainDSAEncoding.INSTANCE);
    }

    public SM2 setEncoding(DSAEncoding encoding) {
        this.encoding = encoding;
        this.signer = null;
        return this;
    }

    public SM2 setDigest(Digest digest) {
        this.digest = digest;
        this.engine = null;
        this.signer = null;
        return this;
    }

    public SM2 setMode(SM2Engine.Mode mode) {
        this.mode = mode;
        this.engine = null;
        return this;
    }

    public byte[] getD() {
        return this.privateKeyParams.getD().toByteArray();
    }

    public byte[] getQ(boolean isCompressed) {
        return this.publicKeyParams.getQ().getEncoded(isCompressed);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.hutool.crypto.asymmetric.SM2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$crypto$asymmetric$KeyType = new int[KeyType.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$crypto$asymmetric$KeyType[KeyType.PublicKey.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$crypto$asymmetric$KeyType[KeyType.PrivateKey.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private CipherParameters getCipherParameters(KeyType keyType) {
        int i = AnonymousClass1.$SwitchMap$cn$hutool$crypto$asymmetric$KeyType[keyType.ordinal()];
        if (i == 1) {
            Assert.notNull(this.publicKeyParams, "PublicKey must be not null !", new Object[0]);
            return this.publicKeyParams;
        } else if (i == 2) {
            Assert.notNull(this.privateKeyParams, "PrivateKey must be not null !", new Object[0]);
            return this.privateKeyParams;
        } else {
            return null;
        }
    }

    private SM2Engine getEngine() {
        if (this.engine == null) {
            Assert.notNull(this.digest, "digest must be not null !", new Object[0]);
            this.engine = new SM2Engine(this.digest, this.mode);
        }
        this.digest.reset();
        return this.engine;
    }

    private SM2Signer getSigner() {
        if (this.signer == null) {
            Assert.notNull(this.digest, "digest must be not null !", new Object[0]);
            this.signer = new SM2Signer(this.encoding, this.digest);
        }
        this.digest.reset();
        return this.signer;
    }
}
