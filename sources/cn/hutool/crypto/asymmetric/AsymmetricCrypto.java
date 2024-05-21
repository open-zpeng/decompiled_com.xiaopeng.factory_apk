package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
/* loaded from: classes.dex */
public class AsymmetricCrypto extends AbstractAsymmetricCrypto<AsymmetricCrypto> {
    private static final long serialVersionUID = 1;
    private AlgorithmParameterSpec algorithmParameterSpec;
    protected Cipher cipher;
    protected int decryptBlockSize;
    protected int encryptBlockSize;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AsymmetricCrypto(cn.hutool.crypto.asymmetric.AsymmetricAlgorithm r2) {
        /*
            r1 = this;
            r0 = 0
            byte[] r0 = (byte[]) r0
            r1.<init>(r2, r0, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.crypto.asymmetric.AsymmetricCrypto.<init>(cn.hutool.crypto.asymmetric.AsymmetricAlgorithm):void");
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AsymmetricCrypto(java.lang.String r2) {
        /*
            r1 = this;
            r0 = 0
            byte[] r0 = (byte[]) r0
            r1.<init>(r2, r0, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.crypto.asymmetric.AsymmetricCrypto.<init>(java.lang.String):void");
    }

    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, String privateKeyStr, String publicKeyStr) {
        this(algorithm.getValue(), SecureUtil.decode(privateKeyStr), SecureUtil.decode(publicKeyStr));
    }

    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
        this(algorithm.getValue(), privateKey, publicKey);
    }

    public AsymmetricCrypto(AsymmetricAlgorithm algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this(algorithm.getValue(), privateKey, publicKey);
    }

    public AsymmetricCrypto(String algorithm, String privateKeyBase64, String publicKeyBase64) {
        this(algorithm, Base64.decode(privateKeyBase64), Base64.decode(publicKeyBase64));
    }

    public AsymmetricCrypto(String algorithm, byte[] privateKey, byte[] publicKey) {
        this(algorithm, KeyUtil.generatePrivateKey(algorithm, privateKey), KeyUtil.generatePublicKey(algorithm, publicKey));
    }

    public AsymmetricCrypto(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithm, privateKey, publicKey);
        this.encryptBlockSize = -1;
        this.decryptBlockSize = -1;
    }

    public int getEncryptBlockSize() {
        return this.encryptBlockSize;
    }

    public void setEncryptBlockSize(int encryptBlockSize) {
        this.encryptBlockSize = encryptBlockSize;
    }

    public int getDecryptBlockSize() {
        return this.decryptBlockSize;
    }

    public void setDecryptBlockSize(int decryptBlockSize) {
        this.decryptBlockSize = decryptBlockSize;
    }

    public AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return this.algorithmParameterSpec;
    }

    public void setAlgorithmParameterSpec(AlgorithmParameterSpec algorithmParameterSpec) {
        this.algorithmParameterSpec = algorithmParameterSpec;
    }

    @Override // cn.hutool.crypto.asymmetric.BaseAsymmetric
    public AsymmetricCrypto init(String algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super.init(algorithm, privateKey, publicKey);
        initCipher();
        return this;
    }

    @Override // cn.hutool.crypto.asymmetric.AsymmetricEncryptor
    public byte[] encrypt(byte[] data, KeyType keyType) {
        int blockSize;
        Key key = getKeyByType(keyType);
        this.lock.lock();
        try {
            try {
                initMode(1, key);
                if (this.encryptBlockSize < 0 && (blockSize = this.cipher.getBlockSize()) > 0) {
                    this.encryptBlockSize = blockSize;
                }
                return doFinal(data, this.encryptBlockSize < 0 ? data.length : this.encryptBlockSize);
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override // cn.hutool.crypto.asymmetric.AsymmetricDecryptor
    public byte[] decrypt(byte[] data, KeyType keyType) {
        int blockSize;
        Key key = getKeyByType(keyType);
        this.lock.lock();
        try {
            try {
                initMode(2, key);
                if (this.decryptBlockSize < 0 && (blockSize = this.cipher.getBlockSize()) > 0) {
                    this.decryptBlockSize = blockSize;
                }
                return doFinal(data, this.decryptBlockSize < 0 ? data.length : this.decryptBlockSize);
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initCipher() {
        this.cipher = SecureUtil.createCipher(this.algorithm);
    }

    private byte[] doFinal(byte[] data, int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int dataLength = data.length;
        if (dataLength <= maxBlockSize) {
            return this.cipher.doFinal(data, 0, dataLength);
        }
        return doFinalWithBlock(data, maxBlockSize);
    }

    private byte[] doFinalWithBlock(byte[] data, int maxBlockSize) throws IllegalBlockSizeException, BadPaddingException, IOException {
        int dataLength = data.length;
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        int offSet = 0;
        int remainLength = dataLength;
        while (remainLength > 0) {
            int blockSize = Math.min(remainLength, maxBlockSize);
            out.write(this.cipher.doFinal(data, offSet, blockSize));
            offSet += blockSize;
            remainLength = dataLength - offSet;
        }
        return out.toByteArray();
    }

    private void initMode(int mode, Key key) throws InvalidAlgorithmParameterException, InvalidKeyException {
        AlgorithmParameterSpec algorithmParameterSpec = this.algorithmParameterSpec;
        if (algorithmParameterSpec != null) {
            this.cipher.init(mode, key, algorithmParameterSpec);
        } else {
            this.cipher.init(mode, key);
        }
    }
}
