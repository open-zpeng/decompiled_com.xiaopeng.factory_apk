package cn.hutool.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.DESede;
import cn.hutool.crypto.symmetric.PBKDF2;
import cn.hutool.crypto.symmetric.RC4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.crypto.symmetric.ZUC;
import cn.hutool.crypto.symmetric.fpe.FPE;
import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import org.bouncycastle.crypto.AlphabetMapper;
/* loaded from: classes.dex */
public class SecureUtil {
    public static final int DEFAULT_KEY_SIZE = 1024;

    public static SecretKey generateKey(String algorithm) {
        return KeyUtil.generateKey(algorithm);
    }

    public static SecretKey generateKey(String algorithm, int keySize) {
        return KeyUtil.generateKey(algorithm, keySize);
    }

    public static SecretKey generateKey(String algorithm, byte[] key) {
        return KeyUtil.generateKey(algorithm, key);
    }

    public static SecretKey generateDESKey(String algorithm, byte[] key) {
        return KeyUtil.generateDESKey(algorithm, key);
    }

    public static SecretKey generatePBEKey(String algorithm, char[] key) {
        return KeyUtil.generatePBEKey(algorithm, key);
    }

    public static SecretKey generateKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generateKey(algorithm, keySpec);
    }

    public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
        return KeyUtil.generatePrivateKey(algorithm, key);
    }

    public static PrivateKey generatePrivateKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generatePrivateKey(algorithm, keySpec);
    }

    public static PrivateKey generatePrivateKey(KeyStore keyStore, String alias, char[] password) {
        return KeyUtil.generatePrivateKey(keyStore, alias, password);
    }

    public static PublicKey generatePublicKey(String algorithm, byte[] key) {
        return KeyUtil.generatePublicKey(algorithm, key);
    }

    public static PublicKey generatePublicKey(String algorithm, KeySpec keySpec) {
        return KeyUtil.generatePublicKey(algorithm, keySpec);
    }

    public static KeyPair generateKeyPair(String algorithm) {
        return KeyUtil.generateKeyPair(algorithm);
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize) {
        return KeyUtil.generateKeyPair(algorithm, keySize);
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed) {
        return KeyUtil.generateKeyPair(algorithm, keySize, seed);
    }

    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params) {
        return KeyUtil.generateKeyPair(algorithm, params);
    }

    public static KeyPair generateKeyPair(String algorithm, byte[] seed, AlgorithmParameterSpec params) {
        return KeyUtil.generateKeyPair(algorithm, seed, params);
    }

    public static String getAlgorithmAfterWith(String algorithm) {
        return KeyUtil.getAlgorithmAfterWith(algorithm);
    }

    public static String generateAlgorithm(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
        String digestPart = digestAlgorithm == null ? "NONE" : digestAlgorithm.name();
        return StrUtil.format("{}with{}", digestPart, asymmetricAlgorithm.getValue());
    }

    public static Signature generateSignature(AsymmetricAlgorithm asymmetricAlgorithm, DigestAlgorithm digestAlgorithm) {
        try {
            return Signature.getInstance(generateAlgorithm(asymmetricAlgorithm, digestAlgorithm));
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static KeyStore readJKSKeyStore(InputStream in, char[] password) {
        return KeyUtil.readJKSKeyStore(in, password);
    }

    public static KeyStore readKeyStore(String type, InputStream in, char[] password) {
        return KeyUtil.readKeyStore(type, in, password);
    }

    public static Certificate readX509Certificate(InputStream in, char[] password, String alias) {
        return KeyUtil.readX509Certificate(in, password, alias);
    }

    public static Certificate readX509Certificate(InputStream in) {
        return KeyUtil.readX509Certificate(in);
    }

    public static Certificate readCertificate(String type, InputStream in, char[] password, String alias) {
        return KeyUtil.readCertificate(type, in, password, alias);
    }

    public static Certificate readCertificate(String type, InputStream in) {
        return KeyUtil.readCertificate(type, in);
    }

    public static Certificate getCertificate(KeyStore keyStore, String alias) {
        return KeyUtil.getCertificate(keyStore, alias);
    }

    public static AES aes() {
        return new AES();
    }

    public static AES aes(byte[] key) {
        return new AES(key);
    }

    public static DES des() {
        return new DES();
    }

    public static DES des(byte[] key) {
        return new DES(key);
    }

    public static DESede desede() {
        return new DESede();
    }

    public static DESede desede(byte[] key) {
        return new DESede(key);
    }

    public static MD5 md5() {
        return new MD5();
    }

    public static String md5(String data) {
        return new MD5().digestHex(data);
    }

    public static String md5(InputStream data) {
        return new MD5().digestHex(data);
    }

    public static String md5(File dataFile) {
        return new MD5().digestHex(dataFile);
    }

    public static Digester sha1() {
        return new Digester(DigestAlgorithm.SHA1);
    }

    public static String sha1(String data) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(data);
    }

    public static String sha1(InputStream data) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(data);
    }

    public static String sha1(File dataFile) {
        return new Digester(DigestAlgorithm.SHA1).digestHex(dataFile);
    }

    public static Digester sha256() {
        return new Digester(DigestAlgorithm.SHA256);
    }

    public static String sha256(String data) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(data);
    }

    public static String sha256(InputStream data) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(data);
    }

    public static String sha256(File dataFile) {
        return new Digester(DigestAlgorithm.SHA256).digestHex(dataFile);
    }

    public static HMac hmac(HmacAlgorithm algorithm, String key) {
        return new HMac(algorithm, StrUtil.utf8Bytes(key));
    }

    public static HMac hmac(HmacAlgorithm algorithm, byte[] key) {
        return new HMac(algorithm, key);
    }

    public static HMac hmac(HmacAlgorithm algorithm, SecretKey key) {
        return new HMac(algorithm, key);
    }

    public static HMac hmacMd5(String key) {
        return hmacMd5(StrUtil.utf8Bytes(key));
    }

    public static HMac hmacMd5(byte[] key) {
        return new HMac(HmacAlgorithm.HmacMD5, key);
    }

    public static HMac hmacMd5() {
        return new HMac(HmacAlgorithm.HmacMD5);
    }

    public static HMac hmacSha1(String key) {
        return hmacSha1(StrUtil.utf8Bytes(key));
    }

    public static HMac hmacSha1(byte[] key) {
        return new HMac(HmacAlgorithm.HmacSHA1, key);
    }

    public static HMac hmacSha1() {
        return new HMac(HmacAlgorithm.HmacSHA1);
    }

    public static HMac hmacSha256(String key) {
        return hmacSha256(StrUtil.utf8Bytes(key));
    }

    public static HMac hmacSha256(byte[] key) {
        return new HMac(HmacAlgorithm.HmacSHA256, key);
    }

    public static HMac hmacSha256() {
        return new HMac(HmacAlgorithm.HmacSHA256);
    }

    public static RSA rsa() {
        return new RSA();
    }

    public static RSA rsa(String privateKeyBase64, String publicKeyBase64) {
        return new RSA(privateKeyBase64, publicKeyBase64);
    }

    public static RSA rsa(byte[] privateKey, byte[] publicKey) {
        return new RSA(privateKey, publicKey);
    }

    public static Sign sign(SignAlgorithm algorithm) {
        return new Sign(algorithm);
    }

    public static Sign sign(SignAlgorithm algorithm, String privateKeyBase64, String publicKeyBase64) {
        return new Sign(algorithm, privateKeyBase64, publicKeyBase64);
    }

    public static Sign sign(SignAlgorithm algorithm, byte[] privateKey, byte[] publicKey) {
        return new Sign(algorithm, privateKey, publicKey);
    }

    public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String... otherParams) {
        return signParams(crypto, params, "", "", true, otherParams);
    }

    public static String signParams(SymmetricCrypto crypto, Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        return crypto.encryptHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
    }

    public static String signParamsMd5(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.MD5, params, otherParams);
    }

    public static String signParamsSha1(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.SHA1, params, otherParams);
    }

    public static String signParamsSha256(Map<?, ?> params, String... otherParams) {
        return signParams(DigestAlgorithm.SHA256, params, otherParams);
    }

    public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String... otherParams) {
        return signParams(digestAlgorithm, params, "", "", true, otherParams);
    }

    public static String signParams(DigestAlgorithm digestAlgorithm, Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        return new Digester(digestAlgorithm).digestHex(MapUtil.sortJoin(params, separator, keyValueSeparator, isIgnoreNull, otherParams));
    }

    public static void addProvider(Provider provider) {
        Security.insertProviderAt(provider, 0);
    }

    public static byte[] decode(String key) {
        return Validator.isHex(key) ? HexUtil.decodeHex(key) : Base64.decode(key);
    }

    public static Cipher createCipher(String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        try {
            if (provider == null) {
                Cipher cipher = Cipher.getInstance(algorithm);
                return cipher;
            }
            Cipher cipher2 = Cipher.getInstance(algorithm, provider);
            return cipher2;
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    public static MessageDigest createMessageDigest(String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        try {
            if (provider == null) {
                MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
                return messageDigest;
            }
            MessageDigest messageDigest2 = MessageDigest.getInstance(algorithm, provider);
            return messageDigest2;
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static Mac createMac(String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        try {
            if (provider == null) {
                Mac mac = Mac.getInstance(algorithm);
                return mac;
            }
            Mac mac2 = Mac.getInstance(algorithm, provider);
            return mac2;
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static Signature createSignature(String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        try {
            if (provider == null) {
                Signature signature = Signature.getInstance(algorithm);
                return signature;
            }
            Signature signature2 = Signature.getInstance(algorithm, provider);
            return signature2;
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static RC4 rc4(String key) {
        return new RC4(key);
    }

    public static void disableBouncyCastle() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
    }

    public static String pbkdf2(char[] password, byte[] salt) {
        return new PBKDF2().encryptHex(password, salt);
    }

    public static FPE fpe(FPE.FPEMode mode, byte[] key, AlphabetMapper mapper, byte[] tweak) {
        return new FPE(mode, key, mapper, tweak);
    }

    public static ZUC zuc128(byte[] key, byte[] iv) {
        return new ZUC(ZUC.ZUCAlgorithm.ZUC_128, key, iv);
    }

    public static ZUC zuc256(byte[] key, byte[] iv) {
        return new ZUC(ZUC.ZUCAlgorithm.ZUC_256, key, iv);
    }
}
