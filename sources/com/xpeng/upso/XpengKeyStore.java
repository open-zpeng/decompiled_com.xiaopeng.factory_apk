package com.xpeng.upso;

import android.content.Context;
import android.os.Build;
import android.pso.XpPsoException;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProtection;
import android.util.Log;
import androidx.annotation.Keep;
import cn.hutool.crypto.KeyUtil;
import com.irdeto.securesdk.upgrade.O00000Oo;
import com.xpeng.upso.aesserver.AesConstants;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Enumeration;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
@Keep
/* loaded from: classes2.dex */
public class XpengKeyStore {
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final int DEC_BLOCK_SIZE = 10240;
    private static final String TAG = "UniformedKeyStorePsoBase";
    private static final String XPENG_ANDROID_KEY_STORE = "XpengAndroidKeyStore";
    private static KeyStore keyStore;
    private String StoreProvider;
    Context context;

    public XpengKeyStore() {
        this(false);
    }

    private static AlgorithmParameterSpec getParamsSpec(byte[] bArr) {
        if (Build.VERSION.SDK_INT < 21) {
            return new IvParameterSpec(bArr, 0, bArr.length);
        }
        return new GCMParameterSpec(128, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] DolbyAesDecrypt(String str, byte[] bArr, byte[] bArr2) {
        try {
            SecretKey secretKey = getSecretKey(str);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(2, secretKey, ivParameterSpec);
            return cipher.doFinal(bArr2);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    protected String InitialKeystore() {
        String exc;
        Log.e(TAG, "InitialKeystore : " + this.StoreProvider);
        try {
            KeyStore keyStore2 = KeyStore.getInstance(this.StoreProvider);
            keyStore = keyStore2;
            keyStore2.load(null);
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
        }
        if (exc != null) {
            keyStore = null;
            Log.e(TAG, "Xp Keystore initial failed...\r\n(" + exc + ")");
        }
        return exc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesCrypt(Cipher cipher, byte[] bArr) throws ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        int length;
        int length2 = bArr.length;
        byte[] bArr2 = new byte[cipher.getOutputSize(length2)];
        int i = 10240 > length2 ? length2 : 10240;
        int i2 = (length2 / i) + (length2 % i == 0 ? 0 : 1);
        if (i2 > 1) {
            int i3 = 0;
            length = 0;
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = i3 + i;
                if (i5 <= length2) {
                    int update = cipher.update(bArr, i3, i, bArr2, length);
                    if (update > 0) {
                        length += update;
                    }
                    i3 = i5;
                } else {
                    int i6 = length2 - i3;
                    byte[] doFinal = cipher.doFinal(bArr, i3, i6);
                    if (doFinal != null) {
                        System.arraycopy(doFinal, 0, bArr2, length, doFinal.length);
                        length += doFinal.length;
                    }
                    i3 += i6;
                }
            }
        } else {
            byte[] doFinal2 = cipher.doFinal(bArr);
            length = doFinal2.length + 0;
            System.arraycopy(doFinal2, 0, bArr2, 0, doFinal2.length);
        }
        ByteBuffer allocate = ByteBuffer.allocate(length);
        allocate.put(bArr2, 0, length);
        return allocate.array();
    }

    protected byte[] aesDecrypt(String str, byte[] bArr, byte[] bArr2, String str2, String str3) throws NoSuchAlgorithmException, KeyStoreException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, UnrecoverableKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        return aesDecrypt(secretKey, bArr, bArr2, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesDecryptWithCbc(SecretKey secretKey, byte[] bArr, byte[] bArr2) throws XpPsoException {
        String exc;
        byte[] bArr3;
        try {
            bArr3 = aesDecrypt(secretKey, bArr, bArr2, "CBC", "NoPadding");
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
            e.printStackTrace();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Cbc Decrypt Failed...\r\n(" + exc + ")", -3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesDecryptWithCbcPkcs7(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        String exc;
        byte[] bArr3;
        SecretKey secretKey;
        try {
            secretKey = getSecretKey(str);
        } catch (Exception e) {
            exc = e.toString();
            e.printStackTrace();
            bArr3 = null;
        }
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        bArr3 = aesDecrypt(secretKey, bArr, bArr2, "CBC", "PKCS7Padding");
        exc = null;
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Cbc Decrypt Failed...\r\n(" + exc + ")", -3);
    }

    protected byte[] aesDecryptWithCbcPkcs7Temp(String str, byte[] bArr, byte[] bArr2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, UnrecoverableKeyException, KeyStoreException {
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        System.arraycopy(bArr2, 0, bArr2, 0, bArr2.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(2, secretKey, ivParameterSpec);
        int length = bArr.length;
        int outputSize = cipher.getOutputSize(length);
        Log.d(TAG, "aesDecryptWithCbcPkcs7 in.size " + length + ", out.size " + outputSize);
        byte[] doFinal = cipher.doFinal(bArr);
        StringBuilder sb = new StringBuilder();
        sb.append("aesDecryptWithCbcPkcs7 final ");
        sb.append(bArr.length);
        Log.d(TAG, sb.toString());
        return doFinal;
    }

    protected byte[] aesDecryptWithCtr(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str2 = null;
        try {
            bArr3 = aesDecrypt(str, bArr, bArr2, "CTR", "NoPadding");
        } catch (Exception e) {
            str2 = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Ctr Decrypt Failed...\r\n(" + str2 + ")", -3);
    }

    protected byte[] aesDecryptWithGcm(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        String exc;
        byte[] bArr3;
        SecretKey secretKey;
        try {
            secretKey = getSecretKey(str);
        } catch (Exception e) {
            exc = e.toString();
            e.printStackTrace();
            bArr3 = null;
        }
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        bArr3 = aesGcmDecrypt(secretKey, bArr, bArr2, "GCM", "NoPadding");
        exc = null;
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Gcm Decrypt Failed...\r\n(" + exc + ")", -3);
    }

    protected byte[] aesEncrypt(String str, byte[] bArr, byte[] bArr2, String str2, String str3) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, XpPsoException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        int length = bArr2.length;
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr2, 0, bArr3, 0, length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr3);
        Cipher cipher = Cipher.getInstance("AES/" + str2 + "/" + str3);
        cipher.init(1, secretKey, ivParameterSpec);
        return aesCrypt(cipher, bArr);
    }

    protected byte[] aesEncryptWithCbc(String str, byte[] bArr, byte[] bArr2, String str2) throws XpPsoException {
        String shortBufferException;
        byte[] bArr3 = null;
        try {
            bArr3 = aesEncrypt(str, bArr, bArr2, "CBC", str2);
            shortBufferException = null;
        } catch (InvalidAlgorithmParameterException e) {
            shortBufferException = e.toString();
        } catch (InvalidKeyException e2) {
            shortBufferException = e2.toString();
        } catch (KeyStoreException e3) {
            shortBufferException = e3.toString();
        } catch (NoSuchAlgorithmException e4) {
            shortBufferException = e4.toString();
        } catch (UnrecoverableKeyException e5) {
            shortBufferException = e5.toString();
        } catch (BadPaddingException e6) {
            shortBufferException = e6.toString();
        } catch (IllegalBlockSizeException e7) {
            shortBufferException = e7.toString();
        } catch (NoSuchPaddingException e8) {
            shortBufferException = e8.toString();
        } catch (ShortBufferException e9) {
            shortBufferException = e9.toString();
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Cbc Encrypt Fail\r\n" + shortBufferException, -31);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesEncryptWithCbcPkcs7(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str2 = null;
        try {
            bArr3 = aesEncrypt(bArr, getSecretKey(str), bArr2, "CBC", "PKCS7Padding");
        } catch (Exception e) {
            str2 = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Cbc Encrypt Fail\r\n(" + str2 + ")", -31);
    }

    protected byte[] aesEncryptWithCbcPkcs7Temp(String str, byte[] bArr, byte[] bArr2) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, XpPsoException, BadPaddingException, IllegalBlockSizeException {
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        int length = bArr2.length;
        byte[] bArr3 = new byte[length];
        System.arraycopy(bArr2, 0, bArr3, 0, length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr3);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(1, secretKey, ivParameterSpec);
        int length2 = bArr.length;
        int outputSize = cipher.getOutputSize(length2);
        Log.d(TAG, "aesEncryptWithCbcPkcs7 in.size " + length2 + ", out.size " + outputSize);
        byte[] doFinal = cipher.doFinal(bArr);
        StringBuilder sb = new StringBuilder();
        sb.append("aesEncryptWithCbcPkcs7 final ");
        sb.append(doFinal.length);
        Log.d(TAG, sb.toString());
        return doFinal;
    }

    protected byte[] aesEncryptWithCtr(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str2 = null;
        try {
            bArr3 = aesEncrypt(str, bArr, bArr2, "CTR", "NoPadding");
        } catch (Exception e) {
            str2 = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Ctr Encrypt Fail\r\n(" + str2 + ")", -31);
    }

    protected byte[] aesEncryptWithGcm(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str2 = null;
        try {
            bArr3 = aesGcmEncrypt(bArr, getSecretKey(str), bArr2, "GCM", "NoPadding");
        } catch (Exception e) {
            str2 = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Gcm Encrypt Fail\r\n(" + str2 + ")", -31);
    }

    protected byte[] aesGcmDecrypt(SecretKey secretKey, byte[] bArr, byte[] bArr2, String str, String str2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        System.arraycopy(bArr2, 0, bArr2, 0, bArr2.length);
        AlgorithmParameterSpec paramsSpec = getParamsSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/" + str + "/" + str2);
        cipher.init(2, secretKey, paramsSpec);
        return aesCrypt(cipher, bArr);
    }

    protected byte[] aesGcmEncrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2, String str, String str2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        System.arraycopy(bArr2, 0, bArr2, 0, bArr2.length);
        AlgorithmParameterSpec paramsSpec = getParamsSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/" + str + "/" + str2);
        cipher.init(1, secretKey, paramsSpec);
        return aesCrypt(cipher, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void checkKeystore() throws XpPsoException {
        String str;
        if (keyStore != null) {
            str = null;
        } else {
            str = InitialKeystore();
        }
        if (keyStore != null) {
            return;
        }
        Context context = this.context;
        throw new XpPsoException(context, "Keystore initial failed...\r\n(" + str + ")", -99);
    }

    protected PrivateKey convertToPrivateKeyObject(String str) throws Exception {
        return convertToPrivateKeyObject(Base64Util.decode(str), O00000Oo.O000000o);
    }

    protected PublicKey convertToPublicKeyObject(String str, String str2) throws Exception {
        return convertToPublicKeyObject(Base64Util.decode(str), str2);
    }

    protected void createKeyPair(String str) {
        if (!hasAlias(str) && Build.VERSION.SDK_INT >= 23) {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(O00000Oo.O000000o, this.StoreProvider);
                keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(str, 12).setDigests("SHA-256", "SHA-512").build());
                keyPairGenerator.generateKeyPair();
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            }
        }
    }

    protected void createSecretKey(String str) {
        createSecretKey(str, this.StoreProvider);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0014  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean delSecretKey(java.lang.String r3) {
        /*
            r2 = this;
            java.security.KeyStore r0 = com.xpeng.upso.XpengKeyStore.keyStore     // Catch: java.lang.Exception -> L7 java.security.KeyStoreException -> Ld
            r0.deleteEntry(r3)     // Catch: java.lang.Exception -> L7 java.security.KeyStoreException -> Ld
            r3 = 1
            return r3
        L7:
            r3 = move-exception
            java.lang.String r3 = r3.toString()
            goto L12
        Ld:
            r3 = move-exception
            java.lang.String r3 = r3.toString()
        L12:
            if (r3 == 0) goto L2a
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "delSecretKey error:"
            r0.append(r1)
            r0.append(r3)
            java.lang.String r3 = r0.toString()
            java.lang.String r0 = "UniformedKeyStorePsoBase"
            com.xpeng.upso.utils.LogUtils.e(r0, r3)
        L2a:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStore.delSecretKey(java.lang.String):boolean");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean ecdsaSha512Verify(byte[] bArr, byte[] bArr2, PublicKey publicKey) throws XpPsoException {
        String signatureException;
        boolean z = false;
        try {
            Signature signature = Signature.getInstance("SHA512withECDSA");
            signature.initVerify(publicKey);
            signature.update(bArr);
            z = signature.verify(bArr2);
            signatureException = null;
        } catch (InvalidKeyException e) {
            signatureException = e.toString();
        } catch (NoSuchAlgorithmException e2) {
            signatureException = e2.toString();
        } catch (SignatureException e3) {
            signatureException = e3.toString();
        }
        if (z) {
            return z;
        }
        Context context = this.context;
        throw new XpPsoException(context, "bin Signed Data Verify Fail...\r\n(" + signatureException + ")", -4);
    }

    protected String ecdsaSign(String str, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        return Base64Util.encodeToString(signature.sign());
    }

    protected boolean ecdsaVerify(String str, String str2, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64Util.decode(str2));
    }

    protected boolean genAndSaveSeceretKey(String str, String str2, byte[] bArr) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore.setEntry(str, new KeyStore.SecretKeyEntry(new SecretKeySpec(bArr, str2)), new KeyProtection.Builder(4).setRandomizedEncryptionRequired(false).build());
        return true;
    }

    protected KeyPair genEcKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(AesConstants.SERVER_MAGIC_CDU);
        keyPairGenerator.initialize(256, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean genRandomAesKeyWithCbc(String str) throws XpPsoException {
        try {
            return genRandomAesKeyWithCbc(str, "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "create random Aes Cbc Key failed...\r\n(" + exc + ")", -72);
        }
    }

    protected KeyPair genRsaKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(O00000Oo.O000000o);
        keyPairGenerator.initialize(2048, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Cipher getAesCbcDecryptCipher(String str, byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, UnrecoverableKeyException, KeyStoreException {
        return getAesDecryptCipher(str, bArr, "CBC", "NoPadding");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Cipher getAesCbcEncryptCipher(String str, byte[] bArr) throws XpPsoException {
        return getAesEncryptCipher(str, bArr, "CBC", "NoPadding");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Cipher getAesDecryptCipher(String str, byte[] bArr, String str2, String str3) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnrecoverableKeyException, KeyStoreException {
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return null;
        }
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/" + str2 + "/" + str3);
        cipher.init(2, secretKey, ivParameterSpec);
        return cipher;
    }

    protected Cipher getAesEncryptCipher(String str, byte[] bArr, String str2, String str3) throws XpPsoException {
        String exc;
        try {
        } catch (InvalidAlgorithmParameterException e) {
            exc = e.toString();
        } catch (InvalidKeyException e2) {
            exc = e2.toString();
        } catch (KeyStoreException e3) {
            exc = e3.toString();
        } catch (NoSuchAlgorithmException e4) {
            exc = e4.toString();
        } catch (NoSuchPaddingException e5) {
            exc = e5.toString();
        } catch (Exception e6) {
            exc = e6.toString();
        }
        if (!keyStore.containsAlias(str)) {
            exc = null;
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
        SecretKey secretKey = getSecretKey(str);
        System.arraycopy(bArr, 0, bArr, 0, bArr.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Cipher cipher = Cipher.getInstance("AES/" + str2 + "/" + str3);
        cipher.init(1, secretKey, ivParameterSpec);
        return cipher;
    }

    protected PublicKey getAkPublicKey(String str) throws XpPsoException {
        try {
            return keyStore.getCertificate(str).getPublicKey();
        } catch (KeyStoreException e) {
            Context context = this.context;
            throw new XpPsoException(context, "No PublicKey\r\n(" + e.toString() + ")", -14);
        }
    }

    protected int getAllAliasCnt() {
        int i = 0;
        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                aliases.nextElement();
                i++;
            }
        } catch (KeyStoreException | Exception e) {
        }
        return i;
    }

    protected Certificate getCertificate(String str) throws Exception {
        return getCertificate(str.getBytes(), KeyUtil.CERT_TYPE_X509);
    }

    protected KeyPair getKeyPair(String str) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(O00000Oo.O000000o, this.StoreProvider);
            keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(str, 15).setKeySize(2048).setDigests("SHA-256").setSignaturePaddings("PSS").build());
            return keyPairGenerator.generateKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            return null;
        }
    }

    protected KeyPair getKeyPairFromKeyStore() throws Exception {
        InputStream resourceAsStream = XpengKeyStore.class.getResourceAsStream("/keystore.jks");
        KeyStore keyStore2 = KeyStore.getInstance("JCEKS");
        keyStore2.load(resourceAsStream, "s3cr3t".toCharArray());
        return new KeyPair(keyStore2.getCertificate("mykey").getPublicKey(), ((KeyStore.PrivateKeyEntry) keyStore2.getEntry("mykey", new KeyStore.PasswordProtection("s3cr3t".toCharArray()))).getPrivateKey());
    }

    protected KeyStore getKeyStore() {
        try {
            checkKeystore();
        } catch (XpPsoException e) {
        }
        return keyStore;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SecretKey getSecretKey(String str) throws XpPsoException {
        String exc;
        SecretKey secretKey = null;
        try {
            secretKey = (SecretKey) keyStore.getKey(str, null);
            exc = null;
        } catch (KeyStoreException e) {
            exc = e.toString();
        } catch (NoSuchAlgorithmException e2) {
            exc = e2.toString();
        } catch (UnrecoverableKeyException e3) {
            exc = e3.toString();
        } catch (Exception e4) {
            exc = e4.toString();
        }
        if (secretKey != null) {
            return secretKey;
        }
        throw new XpPsoException(this.context, "get SecretKey  Failed...maybe need to preset---" + exc, -15);
    }

    protected KeyPair getTargetKeyPair(String str) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(str, null);
            return new KeyPair(privateKeyEntry.getCertificate().getPublicKey(), privateKeyEntry.getPrivateKey());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | UnrecoverableEntryException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Signature getVerifyEcSha512Signature(PublicKey publicKey) throws XpPsoException {
        String noSuchAlgorithmException;
        try {
            Signature signature = Signature.getInstance("SHA512withECDSA");
            signature.initVerify(publicKey);
            return signature;
        } catch (InvalidKeyException e) {
            noSuchAlgorithmException = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "get Verify Signature Failed...\r\n(" + noSuchAlgorithmException + ")", -41);
        } catch (NoSuchAlgorithmException e2) {
            noSuchAlgorithmException = e2.toString();
            Context context2 = this.context;
            throw new XpPsoException(context2, "get Verify Signature Failed...\r\n(" + noSuchAlgorithmException + ")", -41);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Signature getVerifyRsaSha256Signature(PublicKey publicKey) throws XpPsoException {
        String noSuchAlgorithmException;
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            return signature;
        } catch (InvalidKeyException e) {
            noSuchAlgorithmException = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "get Verify Signature Failed...\r\n(" + noSuchAlgorithmException + ")", -41);
        } catch (NoSuchAlgorithmException e2) {
            noSuchAlgorithmException = e2.toString();
            Context context2 = this.context;
            throw new XpPsoException(context2, "get Verify Signature Failed...\r\n(" + noSuchAlgorithmException + ")", -41);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasAlias(String str) {
        try {
            return (keyStore != null) && keyStore.containsAlias(str);
        } catch (KeyStoreException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAesKeyWithCbcWorking(String str, String str2) throws XpPsoException {
        String exc;
        boolean z;
        try {
            z = isAesKeyWorking(str, "CBC", "NoPadding");
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
            z = false;
        }
        if (z) {
            return z;
        }
        Context context = this.context;
        throw new XpPsoException(context, "Aes Cbc Key " + str2 + "Test Failed\r\n(" + exc + ")", -123);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAesKeyWithCtrWorking(String str, String str2) throws XpPsoException {
        String exc;
        boolean z = false;
        try {
            byte[] bArr = new byte[16];
            Arrays.fill(bArr, (byte) 0);
            z = isAesKeyWorking(str, bArr, "CTR", "NoPadding");
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
        }
        if (z) {
            return z;
        }
        Context context = this.context;
        throw new XpPsoException(context, "Aes Ctr Key Test " + str2 + "Failed\r\n(" + exc + ")", -124);
    }

    protected boolean isAesKeyWorking(String str, byte[] bArr, String str2, String str3) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        if (!hasAlias(str)) {
            Log.e(TAG, str + " is invalid .... ");
        }
        SecretKey secretKey = (SecretKey) keyStore.getKey(str, null);
        if (secretKey == null) {
            Log.e(TAG, "get secretKey failed..." + str);
            return false;
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Cipher cipher = Cipher.getInstance("AES/" + str2 + "/" + str3);
        cipher.init(1, secretKey, ivParameterSpec);
        byte[] doFinal = cipher.doFinal("1234567890123456".getBytes("UTF-8"));
        cipher.init(2, secretKey, ivParameterSpec);
        return "1234567890123456".equals(new String(cipher.doFinal(doFinal)));
    }

    protected void printAliases() {
        try {
            Enumeration<String> aliases = keyStore.aliases();
            int i = 0;
            while (aliases != null) {
                if (!aliases.hasMoreElements()) {
                    return;
                }
                String nextElement = aliases.nextElement();
                StringBuilder sb = new StringBuilder();
                sb.append("aliases[");
                int i2 = i + 1;
                sb.append(i);
                sb.append("]:");
                sb.append(nextElement);
                Log.d(TAG, sb.toString());
                i = i2;
            }
        } catch (KeyStoreException e) {
        }
    }

    protected String rsaDecrypt(String str, PrivateKey privateKey) throws Exception {
        byte[] decode = Base64Util.decode(str);
        Cipher cipher = Cipher.getInstance(O00000Oo.O000000o);
        cipher.init(2, privateKey);
        return new String(cipher.doFinal(decode), StandardCharsets.UTF_8);
    }

    protected String rsaEncrypt(String str, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(O00000Oo.O000000o);
        cipher.init(1, publicKey);
        return Base64Util.encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String rsaSign(String str, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        return Base64Util.encodeToString(signature.sign());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean rsaVerify(String str, String str2, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(str.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64Util.decode(str2));
    }

    protected boolean saveAesKey(String str, byte[] bArr, String str2, String str3) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        keyStore.setEntry(str, new KeyStore.SecretKeyEntry(new SecretKeySpec(bArr, "AES")), new KeyProtection.Builder(3).setBlockModes(str2).setEncryptionPaddings(str3).setRandomizedEncryptionRequired(false).build());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean saveAesKeyWithCbc(String str, byte[] bArr) throws XpPsoException {
        try {
            return saveAesKey(str, bArr, "CBC", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Cbc Key Failed\r\n(" + exc + ")", -113);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean saveAesKeyWithCbcPkcs7(String str, byte[] bArr) throws XpPsoException {
        try {
            delSecretKey(str);
            return saveAesKey(str, bArr, "CBC", "PKCS7Padding");
        } catch (Exception e) {
            String exc = e.toString();
            e.printStackTrace();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Cbc Key Failed\r\n(" + exc + ")", -113);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean saveAesKeyWithCtr(String str, byte[] bArr, String str2) throws XpPsoException {
        try {
            return saveAesKey(str, bArr, "CTR", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Ctr Key " + str2 + " Failed\r\n(" + exc + ")", -113);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean saveAesKeyWithGcm(String str, byte[] bArr) throws XpPsoException {
        try {
            return saveAesKey(str, bArr, "GCM", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            e.printStackTrace();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Gcm Key Failed\r\n(" + exc + ")", -113);
        }
    }

    protected boolean saveAsHamcKey(String str, byte[] bArr, String str2) throws XpPsoException {
        try {
            return genAndSaveSeceretKey(str, "HmacSHA256", bArr);
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "saveAsHamcKey " + str2 + "Failed\r\n (" + exc + ")", -115);
        }
    }

    public XpengKeyStore(boolean z) {
        this.context = null;
        if (z) {
            this.StoreProvider = ANDROID_KEY_STORE;
        } else {
            this.StoreProvider = "XpengAndroidKeyStore";
        }
        InitialKeystore();
    }

    protected void createSecretKey(String str, String str2) {
        if (hasAlias(str)) {
            PrintStream printStream = System.out;
            printStream.println("#########alias" + str + "is exist#####");
            return;
        }
        PrintStream printStream2 = System.out;
        printStream2.println("#########alias" + str + "is not exist#####");
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", str2);
                try {
                    keyGenerator.init(new KeyGenParameterSpec.Builder(str, 3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").build());
                } catch (InvalidAlgorithmParameterException e) {
                }
                keyGenerator.generateKey();
                if (hasAlias(str)) {
                    System.out.println("###alias hasAlias###");
                }
            } catch (NoSuchAlgorithmException | NoSuchProviderException e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Certificate getCertificate(byte[] bArr) throws CertificateException {
        return getCertificate(bArr, KeyUtil.CERT_TYPE_X509);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PrivateKey convertToPrivateKeyObject(byte[] bArr) throws Exception {
        return convertToPrivateKeyObject(bArr, O00000Oo.O000000o);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PublicKey convertToPublicKeyObject(byte[] bArr, String str) throws XpPsoException {
        String invalidKeySpecException;
        if (bArr != null) {
            PublicKey publicKey = null;
            try {
                publicKey = KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
                invalidKeySpecException = null;
            } catch (NoSuchAlgorithmException e) {
                invalidKeySpecException = e.toString();
            } catch (InvalidKeySpecException e2) {
                invalidKeySpecException = e2.toString();
            }
            if (publicKey != null) {
                return publicKey;
            }
            throw new XpPsoException(this.context, "der to  PublicKey Failed\r\n(" + invalidKeySpecException + ")", -141);
        }
        throw new XpPsoException(this.context, "PublicKey der input is null", -14);
    }

    protected Certificate getCertificate(byte[] bArr, String str) throws CertificateException {
        return (X509Certificate) CertificateFactory.getInstance(str).generateCertificate(new ByteArrayInputStream(bArr));
    }

    protected PrivateKey convertToPrivateKeyObject(String str, String str2) throws Exception {
        return convertToPrivateKeyObject(Base64Util.decode(str), str2);
    }

    protected PrivateKey convertToPrivateKeyObject(byte[] bArr, String str) throws Exception {
        return KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
    }

    protected boolean ecdsaVerify(byte[] bArr, byte[] bArr2, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initVerify(publicKey);
        signature.update(bArr);
        return signature.verify(bArr2);
    }

    protected boolean genRandomAesKeyWithCbc(String str, String str2) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", this.StoreProvider);
        keyGenerator.init(new KeyGenParameterSpec.Builder(str, 3).setBlockModes("CBC").setEncryptionPaddings(str2).setRandomizedEncryptionRequired(false).build());
        keyGenerator.generateKey();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean saveAesKeyWithCbc(String str, byte[] bArr, String str2) throws XpPsoException {
        try {
            return saveAesKey(str, bArr, "CBC", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Cbc Key " + str2 + "Failed\r\n(" + exc + ")", -113);
        }
    }

    protected boolean saveAesKeyWithCtr(String str, byte[] bArr) throws XpPsoException {
        try {
            return saveAesKey(str, bArr, "CTR", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "Save Aes Ctr Key Failed\r\n(" + exc + ")", -114);
        }
    }

    protected byte[] aesDecrypt(SecretKey secretKey, byte[] bArr, byte[] bArr2, String str, String str2) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        System.arraycopy(bArr2, 0, bArr2, 0, bArr2.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/" + str + "/" + str2);
        cipher.init(2, secretKey, ivParameterSpec);
        return aesCrypt(cipher, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesDecryptWithCtr(SecretKey secretKey, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str = null;
        try {
            bArr3 = aesDecrypt(secretKey, bArr, bArr2, "CTR", "NoPadding");
        } catch (Exception e) {
            str = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Ctr Decrypt Failed...\r\n(" + str + ")", -3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesEncryptWithCtr(SecretKey secretKey, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str = null;
        try {
            bArr3 = aesEncrypt(bArr, secretKey, bArr2, "CTR", "NoPadding");
        } catch (Exception e) {
            str = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Ctr Encrypt Fail\r\n(" + str + ")", -31);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isAesKeyWithCbcWorking(String str, byte[] bArr, String str2) throws XpPsoException {
        String exc;
        boolean z;
        try {
            z = isAesKeyWorking(str, bArr, "CBC", "NoPadding");
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
            z = false;
        }
        if (z) {
            return z;
        }
        Context context = this.context;
        throw new XpPsoException(context, "Aes Cbc Key Test " + str2 + "Failed\r\n(" + exc + ")", -123);
    }

    protected byte[] aesDecryptWithCbc(String str, byte[] bArr, byte[] bArr2) throws BadPaddingException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, ShortBufferException, KeyStoreException, IllegalBlockSizeException {
        return aesDecrypt(str, bArr, bArr2, "CBC", "NoPadding");
    }

    protected boolean rsaVerify(byte[] bArr, byte[] bArr2, PublicKey publicKey) throws XpPsoException {
        String signatureException;
        boolean z = false;
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(bArr);
            z = signature.verify(bArr2);
            signatureException = null;
        } catch (InvalidKeyException e) {
            signatureException = e.toString();
        } catch (NoSuchAlgorithmException e2) {
            signatureException = e2.toString();
        } catch (SignatureException e3) {
            signatureException = e3.toString();
        }
        if (z) {
            return z;
        }
        Context context = this.context;
        throw new XpPsoException(context, "bin Signed Data Verify Fail...\r\n(" + signatureException + ")", -4);
    }

    protected byte[] aesEncrypt(byte[] bArr, SecretKey secretKey, byte[] bArr2, String str, String str2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException {
        System.arraycopy(bArr2, 0, bArr2, 0, bArr2.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
        Cipher cipher = Cipher.getInstance("AES/" + str + "/" + str2);
        cipher.init(1, secretKey, ivParameterSpec);
        return aesCrypt(cipher, bArr);
    }

    protected boolean isAesKeyWorking(String str, String str2, String str3) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        return isAesKeyWorking(str, new IvParameterSpec("1234567890123456".getBytes()).getIV(), str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesEncryptWithCbc(SecretKey secretKey, byte[] bArr, byte[] bArr2) throws XpPsoException {
        String exc;
        byte[] bArr3 = null;
        try {
            bArr3 = aesEncrypt(bArr, secretKey, bArr2, "CBC", "NoPadding");
            exc = null;
        } catch (InvalidAlgorithmParameterException e) {
            exc = e.toString();
        } catch (InvalidKeyException e2) {
            exc = e2.toString();
        } catch (NoSuchAlgorithmException e3) {
            exc = e3.toString();
        } catch (NoSuchPaddingException e4) {
            exc = e4.toString();
        } catch (ShortBufferException e5) {
            exc = e5.toString();
        } catch (Exception e6) {
            exc = e6.toString();
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Ctr Encrypt Fail\r\n(" + exc + ")", -31);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] aesEncryptWithCbc(String str, byte[] bArr, byte[] bArr2) throws XpPsoException {
        byte[] bArr3;
        String str2 = null;
        try {
            bArr3 = aesEncrypt(str, bArr, bArr2, "CBC", "NoPadding");
        } catch (Exception e) {
            str2 = e.toString();
            bArr3 = null;
        }
        if (bArr3 != null) {
            return bArr3;
        }
        throw new XpPsoException(this.context, "Aes Cbc Encrypt Fail\r\n(" + str2 + ")", -31);
    }
}
