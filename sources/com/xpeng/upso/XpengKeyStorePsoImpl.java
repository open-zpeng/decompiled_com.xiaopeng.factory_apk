package com.xpeng.upso;

import android.content.Context;
import android.pso.XpPsoException;
import android.util.Log;
import androidx.annotation.Keep;
import com.xpeng.upso.NativeMethod;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.CertFileUtils;
import com.xpeng.upso.utils.HexUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import net.lingala.zip4j.util.InternalZipConstants;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
@Keep
/* loaded from: classes2.dex */
public class XpengKeyStorePsoImpl extends XpengKeyStore implements IUniformedPsoCrypto {
    private static final String AK = "AK";
    private static final String AKalias = "AKa";
    private static final String AKe = "AKe";
    private static final String AKm = "AKm";
    private static final int AKmRandom = 0;
    private static final int AKmRoot = 1;
    private static final int AKmSecond = 2;
    private static final String AKs = "AKspec";
    private static final String CK = "CK";
    private static final String CKe = "CKe";
    private static final String CPK = "CPK";
    private static final String CPKe = "CPKe";
    private static final boolean ENABLE_DIAGNOSE = false;
    private static final String TAG = "UniformedKeyStorePsoImpl";
    private static final int d21keylistReduct = 2;
    private static final int keybinList_ExtAES_size = 16;
    private static final int keybinList_ExtCert_size = 8;
    private static final int keybinList_ExtCustomkey_size = 32;
    private static final int keybinList_IntAES_size = 128;
    private static final int keybinList_IntCert_size = 64;
    private static final int keybinList_IntCustomkey_size = 128;
    private static final int specKeyList_size = 4;
    private float certKeyVerifyProgress;
    private long[] decMToMProcessedLen;
    private long[] decToFProcessedLen;
    private long[] decToMProcessedLen;
    private float getCertKeyPresetProgress;
    SecretKey mcuSecretkey;
    private boolean otaDecStop;
    private static XpengCarModel carType = XpengCarModel.E28;
    public static boolean stop = false;

    static {
        System.loadLibrary("XpPso");
    }

    public XpengKeyStorePsoImpl(Context context) {
        this.getCertKeyPresetProgress = 0.0f;
        this.certKeyVerifyProgress = 0.0f;
        this.decMToMProcessedLen = new long[]{0};
        this.decToMProcessedLen = new long[]{0};
        this.decToFProcessedLen = new long[]{0};
        this.otaDecStop = false;
        this.mcuSecretkey = null;
        this.context = context.getApplicationContext();
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0172  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean certKeyPresetD21(java.lang.String r9) throws android.pso.XpPsoException {
        /*
            Method dump skipped, instructions count: 412
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStorePsoImpl.certKeyPresetD21(java.lang.String):boolean");
    }

    private boolean certKeyPresetE28(String str) throws XpPsoException {
        if (new File(str).exists()) {
            NativeMethod.E28decKeybin processKeyBin = NativeMethod.processKeyBin(str);
            ArrayList<NativeMethod.E28decKeybin.KeyInfo> arrayList = processKeyBin.CommKey;
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (saveAesKeyWithCtr(convertToKeyAlias(arrayList.get(i).ID), arrayList.get(i).KeyValue, "CommKey " + String.valueOf(i))) {
                        Log.i(TAG, "S:CommKey " + String.valueOf(i) + " succeed!");
                    }
                    if (stop) {
                        return false;
                    }
                }
            }
            return processKeyBin.result == 0;
        }
        throw new XpPsoException(this.context, "keybinFile not exists", -6);
    }

    private boolean certKeyVerifyD21() throws XpPsoException {
        for (int i = 0; i < 64; i++) {
            if (isAesKeyWithCbcWorking(convertToKeyAliasV2(AK, i), "AK " + String.valueOf(i))) {
                Log.i(TAG, "T:AK " + String.valueOf(i) + " succeed!");
            }
            if (stop) {
                return false;
            }
        }
        for (int i2 = 0; i2 < 8; i2++) {
            if (isAesKeyWithCbcWorking(convertToKeyAliasV2(AKe, i2), "AKe " + String.valueOf(i2))) {
                Log.i(TAG, "T:AKe " + String.valueOf(i2) + " succeed!");
            }
            if (stop) {
                return false;
            }
        }
        for (int i3 = 0; i3 < 4; i3++) {
            if (i3 != 1) {
                byte[] encAesSpecKey = NativeMethod.getEncAesSpecKey(i3);
                if (isAesKeyWithCbcWorking(convertToKeyAliasV2(AKs, i3), NativeMethod.getAesSpecKeyVector(i3), "AKs " + String.valueOf(i3)) && encAesSpecKey != null) {
                    Log.i(TAG, "T:AKs " + String.valueOf(i3) + " succeed!");
                }
                if (stop) {
                    return false;
                }
            }
        }
        return !stop;
    }

    private boolean certKeyVerifyE28(String str) throws XpPsoException {
        if (new File(str).exists()) {
            NativeMethod.E28decKeybin processKeyBin = NativeMethod.processKeyBin(str);
            ArrayList<NativeMethod.E28decKeybin.CertInfo> arrayList = processKeyBin.CommSignCert;
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (rsaCertKeyTest(arrayList.get(i).PriPem, arrayList.get(i).ID, "CommSignCert " + String.valueOf(i))) {
                        Log.i(TAG, "T:CommSignCert " + String.valueOf(i) + " succeed!");
                    }
                    if (stop) {
                        return false;
                    }
                }
            }
            ArrayList<NativeMethod.E28decKeybin.KeyInfo> arrayList2 = processKeyBin.CommKey;
            if (arrayList2 != null) {
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    byte[] e28enccKey = NativeMethod.getE28enccKey(arrayList2.get(i2).ID);
                    if (isAesKeyWithCtrWorking(convertToKeyAliasV2(arrayList2.get(i2).ID), "CommKey " + String.valueOf(i2)) && e28enccKey != null) {
                        Log.i(TAG, "T:CommKey " + String.valueOf(i2) + " succeed!");
                    }
                    if (stop) {
                        return false;
                    }
                }
            }
            return processKeyBin.result == 0;
        }
        throw new XpPsoException(this.context, "keybinFile not exists", -6);
    }

    private boolean checkIfPresetedD21() throws XpPsoException {
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                byte[] encAesSpecKey = NativeMethod.getEncAesSpecKey(i);
                if (!hasAlias(convertToKeyAliasV2(AKs, i)) || encAesSpecKey == null) {
                    Log.e(TAG, "T:AKs " + String.valueOf(i) + " not Preseted!");
                    return false;
                }
                Log.i(TAG, "T:AKs " + String.valueOf(i) + " Preseted!");
                if (stop) {
                    return false;
                }
            }
        }
        return !stop;
    }

    private boolean checkIfPresetedE28() throws XpPsoException {
        String str = null;
        int i = 127;
        while (true) {
            if (i >= 128) {
                break;
            }
            try {
            } catch (Exception e) {
                str = e.toString();
            }
            if (!hasAlias(convertToOtaKeyAlisa(NativeMethod.getE28enccKeyId(i)))) {
                str = "E28checkIfPreseted get key failed";
                break;
            }
            Log.i(TAG, "T:CommKey " + String.valueOf(i) + " preseted!");
            i++;
        }
        if (str == null) {
            return true;
        }
        throw new XpPsoException(this.context, "E28checkIfPreseted  failed", -14);
    }

    private void checkRootKey() throws XpPsoException {
        String exc;
        boolean z = false;
        try {
            z = hasAlias(convertToRootKeyAlisa(AKalias, 0));
            exc = null;
        } catch (InvalidKeyException e) {
            exc = e.toString();
        } catch (NoSuchAlgorithmException e2) {
            exc = e2.toString();
        } catch (Exception e3) {
            exc = e3.toString();
        }
        if (z) {
            return;
        }
        Context context = this.context;
        throw new XpPsoException(context, "No Root Key...\r\n" + exc, -98);
    }

    private String convertToKeyAlias(String str, int i) throws XpPsoException {
        return convertToKeyAlias(str + i + "nnnnaks");
    }

    private String convertToKeyAliasV2(String str, int i) throws XpPsoException {
        return convertToKeyAliasV2(str + String.valueOf(i) + "nnnnaks");
    }

    private String convertToOtaKeyAlisa(String str, int i) throws NoSuchAlgorithmException, InvalidKeyException, XpPsoException, UnrecoverableKeyException, InvalidAlgorithmParameterException, KeyStoreException, ShortBufferException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        return convertToOtaKeyAlisa(str + i + "nnnnaks");
    }

    private String convertToRootKeyAlisa(String str, int i) throws NoSuchAlgorithmException, InvalidKeyException {
        String str2 = str + i + "rrrkkkksssss";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKeySpec);
        return Base64Util.encodeToString(mac.doFinal(str2.getBytes()));
    }

    private boolean genAliasRootKey() throws Exception {
        return genRandomAesKeyWithCbc(convertToRootKeyAlisa(AKalias, 0));
    }

    private float getCertKeyPresetProgress() {
        return this.getCertKeyPresetProgress;
    }

    private float getCertKeyVerifyProgress() {
        return this.certKeyVerifyProgress;
    }

    private static String getLuaScript(String str) {
        try {
            File file = new File(str);
            if (file.exists()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[(int) file.length()];
                fileInputStream.read(bArr, 0, (int) file.length());
                return new String(bArr);
            }
            return null;
        } catch (FileNotFoundException | IOException e) {
            return "";
        }
    }

    private String getMiscString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "" : "hao" : "ni" : "ppp" : "xxx";
    }

    private PublicKey getOtaEcuPublicKeyD21() throws XpPsoException {
        String exc;
        byte[] d21OtaEcuCert = NativeMethod.getD21OtaEcuCert();
        if (d21OtaEcuCert != null) {
            try {
                return getCertificate(d21OtaEcuCert).getPublicKey();
            } catch (CertificateException e) {
                exc = e.toString();
                Context context = this.context;
                throw new XpPsoException(context, "No Cert...\r\n(" + exc + ")", -2);
            } catch (Exception e2) {
                exc = e2.toString();
                Context context2 = this.context;
                throw new XpPsoException(context2, "No Cert...\r\n(" + exc + ")", -2);
            }
        }
        throw new XpPsoException(this.context, "No Cert...\r\n(get D21 Ota Ecu Cert)", -2);
    }

    private byte[] hmacMd5(String str) throws NoSuchAlgorithmException, InvalidKeyException {
        String str2 = str + "XpIccid0Xdafedb";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKeySpec);
        return mac.doFinal(str2.getBytes());
    }

    private int int2LittleEndian(int i) {
        return (((-16777216) & i) >> 24) + ((16711680 & i) >> 8) + ((65280 & i) << 8) + ((i & 255) << 24);
    }

    private byte[] otaPackageDecryptCerifyToMD21(String str, int i) throws XpPsoException {
        Cipher defaultDecryptCipherD21;
        File file = new File(str);
        if (file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[(int) file.length()];
                fileInputStream.read(bArr);
                long otaPackageSeek = otaPackageSeek(bArr);
                NativeMethod.OtaEncBinInfo otaEncBinInfoFormMem = NativeMethod.getOtaEncBinInfoFormMem(bArr, otaPackageSeek);
                if (otaEncBinInfoFormMem.result == 0) {
                    int i2 = (int) otaEncBinInfoFormMem.dstBinLen;
                    byte[] bArr2 = new byte[i2];
                    System.arraycopy(bArr, (int) (otaEncBinInfoFormMem.dstBinOffset + otaPackageSeek), bArr2, 0, i2);
                    if (otaEncBinInfoFormMem.dstBinLen > 0) {
                        this.decToMProcessedLen[0] = otaPackageSeek;
                        int i3 = otaEncBinInfoFormMem.BINType;
                        if (i3 > 2) {
                            i3 = 2;
                        }
                        if (i == 2) {
                            defaultDecryptCipherD21 = getAesDecryptCipherD21(i3, "CBC", "NoPadding");
                        } else {
                            defaultDecryptCipherD21 = getDefaultDecryptCipherD21(i3);
                        }
                        PublicKey otaEcuPublicKeyD21 = getOtaEcuPublicKeyD21();
                        File file2 = new File(str);
                        if (file2.exists() && file2.length() > 0) {
                            this.decToMProcessedLen[0] = otaPackageSeek + otaEncBinInfoFormMem.dstBinOffset;
                            Signature verifyEcSha512Signature = getVerifyEcSha512Signature(otaEcuPublicKeyD21);
                            long[] jArr = this.decToMProcessedLen;
                            byte[] decryptVerifyM2MD21 = decryptVerifyM2MD21(otaEncBinInfoFormMem, (int) otaEncBinInfoFormMem.dstBinLen, (int) jArr[0], bArr2, defaultDecryptCipherD21, verifyEcSha512Signature, jArr);
                            try {
                                fileInputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            otaVerifyDecData(decryptVerifyM2MD21, (int) otaEncBinInfoFormMem.dstBinLen, getVerifyEcSha512Signature(otaEcuPublicKeyD21), otaEncBinInfoFormMem.SignData);
                            this.decToMProcessedLen[0] = file2.length();
                            if (decryptVerifyM2MD21 != null) {
                                return decryptVerifyM2MD21;
                            }
                            throw new XpPsoException(this.context, "ota package decryptverify to mem failed...", -42);
                        }
                        throw new XpPsoException(this.context, "ota File is not exists...", -6);
                    }
                    throw new XpPsoException(this.context, "Invalid File...\r\n(parse head failed...)", -1);
                }
                throw new XpPsoException(this.context, "Invalid File...", -1);
            } catch (IOException e2) {
                throw new XpPsoException(this.context, "Invalid File...\r\n(" + e2.toString() + ")", -1);
            }
        }
        throw new XpPsoException(this.context, "ota File is not exists...", -6);
    }

    private boolean otaPackageDecryptVerifyToFD21(String str, String str2, int i) throws XpPsoException {
        Cipher defaultDecryptCipherD21;
        Throwable th;
        FileInputStream fileInputStream;
        Throwable th2;
        FileInputStream fileInputStream2;
        FileOutputStream fileOutputStream;
        if (new File(str).exists()) {
            long otaPackageSeek = otaPackageSeek(str);
            NativeMethod.OtaEncBinInfo otaEncBinInfo = NativeMethod.getOtaEncBinInfo(str, otaPackageSeek > 0 ? otaPackageSeek : 0L);
            if (otaEncBinInfo != null && otaEncBinInfo.dstBinLen > 0) {
                this.decToFProcessedLen[0] = otaPackageSeek;
                int i2 = otaEncBinInfo.BINType;
                if (i2 > 2) {
                    i2 = 2;
                }
                if (i == 2) {
                    defaultDecryptCipherD21 = getAesDecryptCipherD21(i2, "CBC", "NoPadding");
                } else {
                    defaultDecryptCipherD21 = getDefaultDecryptCipherD21(i2);
                }
                PublicKey otaEcuPublicKeyD21 = getOtaEcuPublicKeyD21();
                if (otaEcuPublicKeyD21 != null) {
                    File file = new File(str);
                    File file2 = new File(str2);
                    if (file.exists() && file.length() > 0) {
                        if (file2.exists()) {
                            file2.delete();
                        }
                        try {
                            file2.createNewFile();
                            FileOutputStream fileOutputStream2 = null;
                            try {
                                fileInputStream2 = new FileInputStream(file);
                            } catch (IOException e) {
                                e = e;
                                fileInputStream = null;
                            } catch (Throwable th3) {
                                th = th3;
                                fileInputStream = null;
                            }
                            try {
                                FileOutputStream fileOutputStream3 = new FileOutputStream(file2);
                                try {
                                    fileInputStream2.skip(otaEncBinInfo.dstBinOffset);
                                    this.decToFProcessedLen[0] = otaEncBinInfo.dstBinOffset + otaPackageSeek;
                                    fileOutputStream = fileOutputStream3;
                                    fileInputStream = fileInputStream2;
                                    try {
                                        decryptVerifyF2FD21(otaEncBinInfo, otaEncBinInfo.dstBinLen, (int) (otaPackageSeek + otaEncBinInfo.dstBinOffset), fileInputStream2, fileOutputStream, defaultDecryptCipherD21, getVerifyEcSha512Signature(otaEcuPublicKeyD21), this.decToFProcessedLen);
                                        try {
                                            fileInputStream.close();
                                            fileOutputStream.close();
                                        } catch (IOException e2) {
                                            e2.printStackTrace();
                                        }
                                        otaVerifyDecFile(str2, otaEncBinInfo.dstBinLen, getVerifyEcSha512Signature(otaEcuPublicKeyD21), otaEncBinInfo.SignData);
                                        this.decToFProcessedLen[0] = file.length();
                                        return true;
                                    } catch (IOException e3) {
                                        e = e3;
                                        fileOutputStream2 = fileOutputStream;
                                        try {
                                            throw new XpPsoException(this.context, "Invalid File...\r\n(" + e.toString() + ")", -1);
                                        } catch (Throwable th4) {
                                            th2 = th4;
                                            th = th2;
                                            try {
                                                fileInputStream.close();
                                                fileOutputStream2.close();
                                            } catch (IOException e4) {
                                                e4.printStackTrace();
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th5) {
                                        th2 = th5;
                                        fileOutputStream2 = fileOutputStream;
                                        th = th2;
                                        fileInputStream.close();
                                        fileOutputStream2.close();
                                        throw th;
                                    }
                                } catch (IOException e5) {
                                    e = e5;
                                    fileOutputStream = fileOutputStream3;
                                    fileInputStream = fileInputStream2;
                                } catch (Throwable th6) {
                                    th2 = th6;
                                    fileOutputStream = fileOutputStream3;
                                    fileInputStream = fileInputStream2;
                                }
                            } catch (IOException e6) {
                                e = e6;
                                fileInputStream = fileInputStream2;
                            } catch (Throwable th7) {
                                fileInputStream = fileInputStream2;
                                th = th7;
                                fileInputStream.close();
                                fileOutputStream2.close();
                                throw th;
                            }
                        } catch (IOException e7) {
                            throw new XpPsoException(this.context, str2 + "create NewFile Fail...\r\n(" + e7.toString() + ")", -43);
                        }
                    } else {
                        throw new XpPsoException(this.context, "ota File is not exists...", -6);
                    }
                } else {
                    throw new XpPsoException(this.context, "No PublicKey", -14);
                }
            } else {
                throw new XpPsoException(this.context, "Invalid File...\r\n(parse head failed...)", -1);
            }
        } else {
            throw new XpPsoException(this.context, "ota File is not exists...", -6);
        }
    }

    private boolean otaPackageDecryptVerifyToFE28(String str, String str2, int i) throws XpPsoException {
        File file = new File(str);
        File file2 = new File(str2);
        int e28OtaPackheadLen = NativeMethod.getE28OtaPackheadLen();
        if (file.exists() && file.length() > 0) {
            if (file2.exists()) {
                file2.delete();
            }
            try {
                file2.createNewFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[e28OtaPackheadLen];
                    fileInputStream.read(bArr);
                    NativeMethod.E28OtaPackhead e28OtaPackhead = NativeMethod.getE28OtaPackhead(bArr, file.length());
                    if (e28OtaPackhead != null) {
                        this.decToFProcessedLen[0] = e28OtaPackheadLen;
                        Cipher aesDecryptCipherE28 = i == 2 ? getAesDecryptCipherE28(e28OtaPackhead.key_id, "CTR", "NoPadding") : getDefaultAesDecCipherE28(e28OtaPackhead.key_id);
                        PublicKey publicKeyE28 = getPublicKeyE28(e28OtaPackhead.cert_id);
                        Signature verifyRsaSha256Signature = getVerifyRsaSha256Signature(publicKeyE28);
                        decryptVerifyF2FE28(e28OtaPackhead.datalen, e28OtaPackheadLen, fileInputStream, fileOutputStream, aesDecryptCipherE28, verifyRsaSha256Signature, e28OtaPackhead.datasha256, this.decToFProcessedLen);
                        try {
                            if (verifyRsaSha256Signature.verify(e28OtaPackhead.sign)) {
                                try {
                                    fileInputStream.close();
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                boolean otaVerifyDecFile = otaVerifyDecFile(str2, e28OtaPackhead.datalen, getVerifyRsaSha256Signature(publicKeyE28), e28OtaPackhead.sign);
                                this.decToFProcessedLen[0] = file.length();
                                if (otaVerifyDecFile) {
                                    return true;
                                }
                                throw new XpPsoException(this.context, "ota package decryptverify to file failed...", -42);
                            }
                            throw new XpPsoException(this.context, "bin Signed Data Verify  Fail...", -4);
                        } catch (SignatureException e2) {
                            Context context = this.context;
                            throw new XpPsoException(context, "bin Signed Data Verify  Fail...\r\n(" + e2.toString() + ")", -4);
                        }
                    }
                    throw new XpPsoException(this.context, "Invalid File...\r\n(Packhead null)", -1);
                } catch (IOException e3) {
                    Context context2 = this.context;
                    throw new XpPsoException(context2, "Invalid File...\r\n(" + e3.toString() + ")", -1);
                }
            } catch (IOException e4) {
                Context context3 = this.context;
                throw new XpPsoException(context3, str2 + "create NewFile Fail...\r\n(" + e4.toString() + ")", -43);
            }
        }
        throw new XpPsoException(this.context, "ota File is not exists...", -6);
    }

    private byte[] otaPackageDecryptVerifyToFWithMd5E28(String str, String str2, int i) throws XpPsoException {
        File file = new File(str);
        File file2 = new File(str2);
        int e28OtaPackheadLen = NativeMethod.getE28OtaPackheadLen();
        if (file.exists() && file.length() > 0) {
            if (file2.exists()) {
                file2.delete();
            }
            try {
                file2.createNewFile();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    byte[] bArr = new byte[e28OtaPackheadLen];
                    fileInputStream.read(bArr);
                    NativeMethod.E28OtaPackhead e28OtaPackhead = NativeMethod.getE28OtaPackhead(bArr, file.length());
                    if (e28OtaPackhead != null) {
                        this.decToFProcessedLen[0] = e28OtaPackheadLen;
                        Cipher aesDecryptCipherE28 = i == 2 ? getAesDecryptCipherE28(e28OtaPackhead.key_id, "CTR", "NoPadding") : getDefaultAesDecCipherE28(e28OtaPackhead.key_id);
                        PublicKey publicKeyE28 = getPublicKeyE28(e28OtaPackhead.cert_id);
                        Signature verifyRsaSha256Signature = getVerifyRsaSha256Signature(publicKeyE28);
                        byte[] decryptVerifyF2FWithMd5E28 = decryptVerifyF2FWithMd5E28(e28OtaPackhead.datalen, e28OtaPackheadLen, fileInputStream, fileOutputStream, aesDecryptCipherE28, verifyRsaSha256Signature, e28OtaPackhead.datasha256, this.decToFProcessedLen);
                        try {
                            if (verifyRsaSha256Signature.verify(e28OtaPackhead.sign)) {
                                try {
                                    fileInputStream.close();
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                boolean otaVerifyDecFile = otaVerifyDecFile(str2, e28OtaPackhead.datalen, getVerifyRsaSha256Signature(publicKeyE28), e28OtaPackhead.sign);
                                this.decToFProcessedLen[0] = file.length();
                                if (otaVerifyDecFile) {
                                    return decryptVerifyF2FWithMd5E28;
                                }
                                throw new XpPsoException(this.context, "ota package decryptverify to file failed...", -42);
                            }
                            throw new XpPsoException(this.context, "bin Signed Data Verify  Fail...", -4);
                        } catch (SignatureException e2) {
                            Context context = this.context;
                            throw new XpPsoException(context, "bin Signed Data Verify  Fail...\r\n(" + e2.toString() + ")", -4);
                        }
                    }
                    throw new XpPsoException(this.context, "Invalid File...\r\n(Packhead null)", -1);
                } catch (IOException e3) {
                    Context context2 = this.context;
                    throw new XpPsoException(context2, "Invalid File...\r\n(" + e3.toString() + ")", -1);
                }
            } catch (IOException e4) {
                Context context3 = this.context;
                throw new XpPsoException(context3, str2 + "create NewFile Fail...\r\n(" + e4.toString() + ")", -43);
            }
        }
        throw new XpPsoException(this.context, "ota File is not exists...", -6);
    }

    private byte[] otaPackageDecryptVerifyToME28(String str, int i) throws XpPsoException {
        File file = new File(str);
        int e28OtaPackheadLen = NativeMethod.getE28OtaPackheadLen();
        if (file.exists() && file.length() > 0) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[e28OtaPackheadLen];
                fileInputStream.read(bArr);
                NativeMethod.E28OtaPackhead e28OtaPackhead = NativeMethod.getE28OtaPackhead(bArr, file.length());
                if (e28OtaPackhead != null) {
                    this.decToMProcessedLen[0] = e28OtaPackheadLen;
                    int i2 = (int) e28OtaPackhead.datalen;
                    byte[] bArr2 = new byte[i2];
                    byte[] bArr3 = new byte[i2];
                    try {
                        fileInputStream.read(bArr2);
                        Cipher aesDecryptCipherE28 = i == 2 ? getAesDecryptCipherE28(e28OtaPackhead.key_id, "CTR", "NoPadding") : getDefaultAesDecCipherE28(e28OtaPackhead.key_id);
                        PublicKey publicKeyE28 = getPublicKeyE28(e28OtaPackhead.cert_id);
                        Signature verifyRsaSha256Signature = getVerifyRsaSha256Signature(publicKeyE28);
                        decryptVerifyM2ME28((int) e28OtaPackhead.datalen, e28OtaPackheadLen, bArr2, bArr3, aesDecryptCipherE28, verifyRsaSha256Signature, e28OtaPackhead.datasha256, this.decToMProcessedLen);
                        try {
                            verifyRsaSha256Signature.verify(e28OtaPackhead.sign);
                            if (otaVerifyDecData(bArr3, e28OtaPackhead.datalen, getVerifyRsaSha256Signature(publicKeyE28), e28OtaPackhead.sign)) {
                                this.decToMProcessedLen[0] = file.length();
                                try {
                                    fileInputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return bArr3;
                            }
                            throw new XpPsoException(this.context, "ota package verify failed...", -4);
                        } catch (SignatureException e2) {
                            Context context = this.context;
                            throw new XpPsoException(context, "bin Signed Data Verify  Fail...\r\n(" + e2.toString() + ")", -4);
                        }
                    } catch (IOException e3) {
                        Context context2 = this.context;
                        throw new XpPsoException(context2, "Read File Failed\r\n(" + e3.toString() + ")", -22);
                    }
                }
                throw new XpPsoException(this.context, "Invalid File...\r\n(Packhead==null)", -1);
            } catch (IOException e4) {
                Context context3 = this.context;
                throw new XpPsoException(context3, "Invalid File...\r\n(" + e4.toString() + ")", -1);
            }
        }
        throw new XpPsoException(this.context, "ota File is not exists...", -6);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x003c, code lost:
        if (r3 == null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0040, code lost:
        if (r3 == null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0042, code lost:
        r3.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private long otaPackageSeek(java.lang.String r6) {
        /*
            r5 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r6)
            boolean r6 = r0.exists()
            r1 = 0
            if (r6 != 0) goto Le
            return r1
        Le:
            long r3 = r0.length()
            int r6 = (int) r3
            r3 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b java.io.FileNotFoundException -> L3f
            r4.<init>(r0)     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b java.io.FileNotFoundException -> L3f
            byte[] r0 = new byte[r6]     // Catch: java.lang.Throwable -> L29 java.io.IOException -> L2c java.io.FileNotFoundException -> L2f
            r3 = 0
            r4.read(r0, r3, r6)     // Catch: java.lang.Throwable -> L29 java.io.IOException -> L2c java.io.FileNotFoundException -> L2f
            long r0 = r5.otaPackageSeek(r0)     // Catch: java.lang.Throwable -> L29 java.io.IOException -> L2c java.io.FileNotFoundException -> L2f
            r4.close()     // Catch: java.io.IOException -> L27
            goto L28
        L27:
            r6 = move-exception
        L28:
            return r0
        L29:
            r6 = move-exception
            r3 = r4
            goto L33
        L2c:
            r6 = move-exception
            r3 = r4
            goto L3c
        L2f:
            r6 = move-exception
            r3 = r4
            goto L40
        L32:
            r6 = move-exception
        L33:
            if (r3 == 0) goto L3a
            r3.close()     // Catch: java.io.IOException -> L39
            goto L3a
        L39:
            r0 = move-exception
        L3a:
            throw r6
        L3b:
            r6 = move-exception
        L3c:
            if (r3 == 0) goto L47
            goto L42
        L3f:
            r6 = move-exception
        L40:
            if (r3 == 0) goto L47
        L42:
            r3.close()     // Catch: java.io.IOException -> L46
            goto L47
        L46:
            r6 = move-exception
        L47:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStorePsoImpl.otaPackageSeek(java.lang.String):long");
    }

    private static void runLuaScript(String str) {
        if (str != null) {
            Globals standardGlobals = JsePlatform.standardGlobals();
            standardGlobals.load(str).call(LuaValue.valueOf("73f,11223344,01"));
            System.out.println(String.format("java re_key=%d\r\n", Long.valueOf(Long.valueOf(standardGlobals.get("re_key").toString()).longValue())));
        }
    }

    protected byte[] blePackageDecryptVerifyMToMD22(byte[] bArr) throws XpPsoException {
        int e28OtaPackheadLen = NativeMethod.getE28OtaPackheadLen();
        byte[] bArr2 = new byte[e28OtaPackheadLen];
        System.arraycopy(bArr, 0, bArr2, 0, e28OtaPackheadLen);
        NativeMethod.E28OtaPackhead e28OtaPackhead = NativeMethod.getE28OtaPackhead(bArr2, bArr.length);
        if (e28OtaPackhead != null) {
            this.decMToMProcessedLen[0] = e28OtaPackheadLen;
            int i = (int) e28OtaPackhead.datalen;
            byte[] bArr3 = new byte[i];
            byte[] bArr4 = new byte[i];
            System.arraycopy(bArr, e28OtaPackheadLen, bArr3, 0, i);
            Cipher bleAesDecryptCipherD22 = getBleAesDecryptCipherD22(e28OtaPackhead.key_id, "CTR", "NoPadding");
            PublicKey publicKeyE28 = getPublicKeyE28(e28OtaPackhead.cert_id);
            Signature verifyRsaSha256Signature = getVerifyRsaSha256Signature(publicKeyE28);
            decryptVerifyM2ME28((int) e28OtaPackhead.datalen, e28OtaPackheadLen, bArr3, bArr4, bleAesDecryptCipherD22, verifyRsaSha256Signature, e28OtaPackhead.datasha256, this.decMToMProcessedLen);
            try {
                verifyRsaSha256Signature.verify(e28OtaPackhead.sign);
                if (otaVerifyDecData(bArr4, e28OtaPackhead.datalen, getVerifyRsaSha256Signature(publicKeyE28), e28OtaPackhead.sign)) {
                    this.decMToMProcessedLen[0] = bArr.length;
                    return bArr4;
                }
                throw new XpPsoException(this.context, "package verify failed...", -4);
            } catch (SignatureException e) {
                Context context = this.context;
                throw new XpPsoException(context, "Data Signed Verify  Fail...\r\n(" + e.toString() + ")", -4);
            }
        }
        throw new XpPsoException(this.context, "Invalid Input...\r\n(Packhead==null)", -1);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] blePackageDecryptVerifyMemoryToMemory(byte[] bArr, XpengCarModel xpengCarModel) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        if (xpengCarModel == XpengCarModel.E28) {
            return otaPackageDecryptVerifyMToME28(bArr, 2);
        }
        if (xpengCarModel == XpengCarModel.D22) {
            return blePackageDecryptVerifyMToMD22(bArr);
        }
        return otaPackageDecryptVerifyMToMD21(bArr, 2);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean certKeyPreset(String str) throws XpPsoException {
        checkKeystore();
        if (carType == XpengCarModel.E28) {
            return certKeyPresetE28(str) & mcuKeyPreset();
        }
        return certKeyPresetD21(str) & mcuKeyPreset();
    }

    boolean checkIfMcuKeyPresetd() throws XpPsoException {
        if (otaGenMcuKey() != null) {
            Log.d(TAG, "Mcu Key preseted!");
            return true;
        }
        throw new XpPsoException(this.context, "get Mcu Key Failed...!", -123);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean checkIfPreseted() throws XpPsoException {
        checkKeystore();
        checkRootKey();
        return carType == XpengCarModel.D21 ? checkIfPresetedD21() && checkIfMcuKeyPresetd() : checkIfPresetedE28() && checkIfMcuKeyPresetd();
    }

    protected boolean decryptVerifyF2FD21(NativeMethod.OtaEncBinInfo otaEncBinInfo, long j, int i, InputStream inputStream, OutputStream outputStream, Cipher cipher, Signature signature, long[] jArr) throws XpPsoException {
        XpengKeyStorePsoImpl xpengKeyStorePsoImpl;
        String str;
        String str2;
        String str3;
        String str4;
        int i2;
        long j2;
        int i3;
        int i4 = 10240 > j ? (int) j : 10240;
        long j3 = i4;
        long j4 = (j / j3) + (j % j3 == 0 ? 0 : 1);
        byte[] bArr = new byte[i4];
        if (j4 > 1) {
            int i5 = 0;
            i2 = 0;
            j2 = 0;
            while (i5 < j4) {
                long j5 = j2 + j3;
                if (j5 > j) {
                    break;
                }
                long j6 = j3;
                try {
                    if (inputStream.read(bArr) == i4) {
                        try {
                            byte[] update = cipher.update(NativeMethod.doXor(7, bArr));
                            if (update != null) {
                                i2 += NativeMethod.getCheckSum(update);
                                try {
                                    signature.update(update);
                                    try {
                                        outputStream.write(update);
                                        outputStream.flush();
                                        this.decToFProcessedLen[0] = i + j5;
                                        i5++;
                                        j2 = j5;
                                        j3 = j6;
                                        i4 = i4;
                                        j4 = j4;
                                    } catch (IOException e) {
                                        throw new XpPsoException(this.context, "write File Failed...\r\n(" + e.toString() + ")", -44);
                                    }
                                } catch (SignatureException e2) {
                                    throw new XpPsoException(this.context, "bin Signed Data Verify update Fail...\r\n(" + e2.toString() + ")", -4);
                                }
                            } else {
                                throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(DecData=null)", -3);
                            }
                        } catch (Exception e3) {
                            throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(" + e3.toString() + ")", -3);
                        }
                    } else {
                        throw new XpPsoException(this.context, "Read File Failed\r\n(readLen error)", -22);
                    }
                } catch (IOException e4) {
                    throw new XpPsoException(this.context, "Read File Failed\r\n(" + e4.toString() + ")", -22);
                }
            }
            xpengKeyStorePsoImpl = this;
            str = "write File Failed...\r\n(";
            str2 = "bin Signed Data Verify update Fail...\r\n(";
            str3 = "Decrypt update Fail...\r\n(";
            str4 = "Read File Failed\r\n(";
        } else {
            xpengKeyStorePsoImpl = this;
            str = "write File Failed...\r\n(";
            str2 = "bin Signed Data Verify update Fail...\r\n(";
            str3 = "Decrypt update Fail...\r\n(";
            str4 = "Read File Failed\r\n(";
            i2 = 0;
            j2 = 0;
        }
        int i6 = (int) (j - j2);
        if (i6 <= 0) {
            i3 = i2;
        } else {
            byte[] bArr2 = new byte[i6];
            byte[] bArr3 = new byte[i6];
            try {
                if (inputStream.read(bArr3) == i6) {
                    try {
                        byte[] doFinal = cipher.doFinal(NativeMethod.doXor(7, bArr3));
                        if (doFinal != null) {
                            int checkSum = i2 + NativeMethod.getCheckSum(doFinal);
                            try {
                                signature.update(doFinal);
                                try {
                                    outputStream.write(doFinal);
                                    outputStream.flush();
                                    i3 = checkSum;
                                } catch (IOException e5) {
                                    throw new XpPsoException(xpengKeyStorePsoImpl.context, str + e5.toString() + ")", -44);
                                }
                            } catch (SignatureException e6) {
                                throw new XpPsoException(xpengKeyStorePsoImpl.context, str2 + e6.toString() + ")", -4);
                            }
                        } else {
                            throw new XpPsoException(xpengKeyStorePsoImpl.context, "Decrypt update Fail...\r\n(DecData==null)", -3);
                        }
                    } catch (Exception e7) {
                        throw new XpPsoException(xpengKeyStorePsoImpl.context, str3 + e7.toString() + ")", -3);
                    }
                } else {
                    throw new XpPsoException(xpengKeyStorePsoImpl.context, "Read File Failed\r\n(len!=lastLen)", -22);
                }
            } catch (IOException e8) {
                throw new XpPsoException(xpengKeyStorePsoImpl.context, str4 + e8.toString() + ")", -22);
            }
        }
        try {
            if (signature.verify(otaEncBinInfo.SignData)) {
                if (i3 == otaEncBinInfo.CheckSum) {
                    return true;
                }
                throw new XpPsoException(xpengKeyStorePsoImpl.context, "ota package CheckSum Error...", -5);
            }
            throw new XpPsoException(xpengKeyStorePsoImpl.context, "bin Signed Data Verify  Fail...", -4);
        } catch (SignatureException e9) {
            throw new XpPsoException(xpengKeyStorePsoImpl.context, "bin Signed Data Verify  Fail...\r\n(" + e9.toString() + ")", -4);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x0190, code lost:
        r5 = r23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected boolean decryptVerifyF2FE28(long r29, int r31, java.io.InputStream r32, java.io.OutputStream r33, javax.crypto.Cipher r34, java.security.Signature r35, byte[] r36, long[] r37) throws android.pso.XpPsoException {
        /*
            Method dump skipped, instructions count: 711
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStorePsoImpl.decryptVerifyF2FE28(long, int, java.io.InputStream, java.io.OutputStream, javax.crypto.Cipher, java.security.Signature, byte[], long[]):boolean");
    }

    protected byte[] decryptVerifyF2FWithMd5E28(long j, int i, InputStream inputStream, OutputStream outputStream, Cipher cipher, Signature signature, byte[] bArr, long[] jArr) throws XpPsoException {
        MessageDigest messageDigest;
        String str;
        MessageDigest messageDigest2;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        int i2;
        int i3;
        long j2;
        int read;
        byte[] bArr2;
        byte[] bArr3;
        boolean z;
        byte[] bArr4;
        String str7;
        String str8;
        String str9;
        String str10;
        int i4;
        MessageDigest messageDigest3;
        long j3;
        String str11;
        int update;
        long j4 = j;
        InputStream inputStream2 = inputStream;
        int i5 = 10240 > j4 ? (int) j4 : 10240;
        long j5 = i5;
        long j6 = (j4 / j5) + (j4 % j5 == 0 ? 0 : 1);
        MessageDigest messageDigest4 = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            try {
                messageDigest4 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        } catch (NoSuchAlgorithmException e2) {
            messageDigest = null;
        }
        MessageDigest messageDigest5 = messageDigest4;
        MessageDigest messageDigest6 = messageDigest;
        byte[] bArr5 = new byte[i5];
        byte[] bArr6 = new byte[i5];
        byte[] bArr7 = new byte[i5 * 3];
        MessageDigest messageDigest7 = messageDigest6;
        String str12 = "write File Failed...\r\n(";
        String str13 = "bin Signed Data Verify update Fail...\r\n(";
        String str14 = "Decrypt update Fail...\r\n(";
        String str15 = "Read File Failed\r\n(";
        if (j6 > 1) {
            String str16 = ")";
            int i6 = 0;
            j2 = 0;
            boolean z2 = true;
            while (true) {
                if (i6 >= j6) {
                    str = str16;
                    messageDigest2 = messageDigest7;
                    str2 = str12;
                    str3 = str13;
                    str4 = str14;
                    str5 = "Read File Failed\r\n(read len error)";
                    str6 = str15;
                    i2 = -3;
                    i3 = -22;
                    break;
                } else if (!this.otaDecStop) {
                    long j7 = j2 + j5;
                    if (j7 > j4) {
                        str = str16;
                        messageDigest2 = messageDigest7;
                        str2 = str12;
                        str3 = str13;
                        str4 = str14;
                        str5 = "Read File Failed\r\n(read len error)";
                        str6 = str15;
                        i2 = -3;
                        i3 = -22;
                        break;
                    }
                    boolean z3 = !z2;
                    if (z3) {
                        try {
                            read = inputStream2.read(bArr5);
                        } catch (IOException e3) {
                            throw new XpPsoException(this.context, str15 + e3.toString() + str16, -22);
                        }
                    } else {
                        read = inputStream2.read(bArr6);
                    }
                    if (read == i5) {
                        if (z3) {
                            bArr2 = bArr7;
                            bArr3 = bArr6;
                            z = z3;
                            bArr4 = bArr5;
                            str7 = str16;
                            str8 = str13;
                            str9 = str14;
                            str10 = str15;
                            i4 = i6;
                            long j8 = j5;
                            messageDigest3 = messageDigest7;
                            j3 = j8;
                            str11 = str12;
                            try {
                                update = cipher.update(bArr5, 0, i5, bArr2, 0);
                            } catch (Exception e4) {
                                throw new XpPsoException(this.context, str9 + e4.toString() + str7, -3);
                            }
                        } else {
                            bArr2 = bArr7;
                            bArr3 = bArr6;
                            z = z3;
                            bArr4 = bArr5;
                            str7 = str16;
                            str8 = str13;
                            str9 = str14;
                            str10 = str15;
                            i4 = i6;
                            long j9 = j5;
                            messageDigest3 = messageDigest7;
                            j3 = j9;
                            str11 = str12;
                            update = cipher.update(bArr3, 0, i5, bArr2, 0);
                        }
                        if (update > 0) {
                            byte[] bArr8 = bArr2;
                            try {
                                messageDigest3.update(bArr8, 0, update);
                                messageDigest5.update(bArr8, 0, update);
                                signature.update(bArr8, 0, update);
                                try {
                                    outputStream.write(bArr8, 0, update);
                                    outputStream.flush();
                                    jArr[0] = i + j7;
                                    i6 = i4 + 1;
                                    str14 = str9;
                                    str16 = str7;
                                    str13 = str8;
                                    bArr7 = bArr8;
                                    str12 = str11;
                                    z2 = z;
                                    bArr6 = bArr3;
                                    j2 = j7;
                                    bArr5 = bArr4;
                                    str15 = str10;
                                    j4 = j;
                                    inputStream2 = inputStream;
                                    long j10 = j3;
                                    messageDigest7 = messageDigest3;
                                    j5 = j10;
                                } catch (IOException e5) {
                                    throw new XpPsoException(this.context, str11 + e5.toString() + str7, -44);
                                }
                            } catch (Exception e6) {
                                throw new XpPsoException(this.context, str8 + e6.toString() + str7, -4);
                            }
                        } else {
                            throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(out = null)", -3);
                        }
                    } else {
                        throw new XpPsoException(this.context, "Read File Failed\r\n(read len error)", -22);
                    }
                } else {
                    throw new XpPsoException(this.context, "Decrypt Exit...", -33);
                }
            }
        } else {
            str = ")";
            messageDigest2 = messageDigest7;
            str2 = str12;
            str3 = str13;
            str4 = str14;
            str5 = "Read File Failed\r\n(read len error)";
            str6 = str15;
            i2 = -3;
            i3 = -22;
            j2 = 0;
        }
        int i7 = (int) (j - j2);
        if (i7 > 0) {
            byte[] bArr9 = new byte[i7];
            String str17 = str3;
            try {
                if (inputStream.read(bArr9) == i7) {
                    try {
                        byte[] doFinal = cipher.doFinal(bArr9);
                        if (doFinal != null) {
                            try {
                                messageDigest2.update(doFinal);
                                messageDigest5.update(doFinal);
                                signature.update(doFinal);
                                try {
                                    outputStream.write(doFinal);
                                    outputStream.flush();
                                } catch (Exception e7) {
                                    throw new XpPsoException(this.context, str2 + e7.toString() + str, -44);
                                }
                            } catch (Exception e8) {
                                throw new XpPsoException(this.context, str17 + e8.toString() + str, -4);
                            }
                        } else {
                            throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(out null)", i2);
                        }
                    } catch (Exception e9) {
                        throw new XpPsoException(this.context, str4 + e9.toString() + str, i2);
                    }
                } else {
                    throw new XpPsoException(this.context, str5, i3);
                }
            } catch (IOException e10) {
                throw new XpPsoException(this.context, str6 + e10.toString() + str, i3);
            }
        }
        try {
            byte[] digest = messageDigest2.digest();
            byte[] digest2 = messageDigest5.digest();
            String bytesToHexString = HexUtils.bytesToHexString(digest);
            String bytesToHexString2 = HexUtils.bytesToHexString(bArr);
            Log.i(TAG, "sha256sum correct?=" + bytesToHexString2.contains(bytesToHexString) + ",sha256result=" + bytesToHexString);
            return digest2;
        } catch (Exception e11) {
            throw new XpPsoException(this.context, "sha256sum check Failed...\r\n(" + e11.toString() + str, -4);
        }
    }

    protected byte[] decryptVerifyM2MD21(NativeMethod.OtaEncBinInfo otaEncBinInfo, int i, int i2, byte[] bArr, Cipher cipher, Signature signature, long[] jArr) throws XpPsoException {
        String str;
        int i3;
        int i4;
        int i5;
        Signature signature2;
        int i6;
        Signature signature3 = signature;
        String str2 = "Decrypt doFinal Fail...\r\n(";
        byte[] bArr2 = new byte[i];
        int i7 = 10240 > i ? i : 10240;
        int i8 = (i / i7) + (i % i7 == 0 ? 0 : 1);
        byte[] bArr3 = new byte[i7];
        if (i8 > 1) {
            int i9 = 0;
            i4 = 0;
            i5 = 0;
            int i10 = 0;
            while (i9 < i8) {
                int i11 = i4 + i7;
                if (i11 > i) {
                    break;
                }
                int i12 = i8;
                System.arraycopy(bArr, i4, bArr3, 0, i7);
                try {
                    byte[] update = cipher.update(NativeMethod.doXor(7, bArr3));
                    if (update != null) {
                        int i13 = i7;
                        int i14 = i10;
                        byte[] bArr4 = bArr3;
                        System.arraycopy(update, 0, bArr2, i14, update.length);
                        int length = update.length;
                        byte[] bArr5 = new byte[length];
                        System.arraycopy(update, 0, bArr5, 0, length);
                        i5 += NativeMethod.getCheckSum(bArr5);
                        try {
                            signature3.update(update);
                            jArr[0] = i2 + i11;
                            int length2 = update.length + i14;
                            i9++;
                            signature3 = signature;
                            str2 = str2;
                            i4 = i11;
                            bArr3 = bArr4;
                            i8 = i12;
                            i7 = i13;
                            i10 = length2;
                        } catch (SignatureException e) {
                            Context context = this.context;
                            throw new XpPsoException(context, "bin Signed Data Verify update Fail...\r\n(" + e.toString() + ")", -4);
                        }
                    } else {
                        throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(out null)", -3);
                    }
                } catch (Exception e2) {
                    Context context2 = this.context;
                    throw new XpPsoException(context2, "Decrypt update Fail...\r\n(" + e2.toString() + ")", -3);
                }
            }
            str = str2;
            i3 = i10;
        } else {
            str = "Decrypt doFinal Fail...\r\n(";
            i3 = 0;
            i4 = 0;
            i5 = 0;
        }
        int i15 = i - i4;
        if (i15 <= 0) {
            signature2 = signature;
            i6 = i5;
        } else {
            byte[] bArr6 = new byte[i15];
            System.arraycopy(bArr, i4, bArr6, 0, i15);
            try {
                byte[] doFinal = cipher.doFinal(NativeMethod.doXor(7, bArr6));
                if (doFinal != null) {
                    System.arraycopy(doFinal, 0, bArr2, i3, doFinal.length);
                    int length3 = doFinal.length;
                    byte[] bArr7 = new byte[length3];
                    System.arraycopy(doFinal, 0, bArr7, 0, length3);
                    int checkSum = i5 + NativeMethod.getCheckSum(bArr7);
                    signature2 = signature;
                    try {
                        signature2.update(doFinal);
                        i6 = checkSum;
                    } catch (SignatureException e3) {
                        Context context3 = this.context;
                        throw new XpPsoException(context3, "bin Signed Data Verify update Fail...\r\n(" + e3.toString() + ")", -4);
                    }
                } else {
                    throw new XpPsoException(this.context, "Decrypt doFinal Fail...\r\n(out null)", -3);
                }
            } catch (BadPaddingException e4) {
                Context context4 = this.context;
                throw new XpPsoException(context4, str + e4.toString() + ")", -3);
            } catch (IllegalBlockSizeException e5) {
                Context context5 = this.context;
                throw new XpPsoException(context5, str + e5.toString() + ")", -3);
            } catch (Exception e6) {
                Context context6 = this.context;
                throw new XpPsoException(context6, str + e6.toString() + ")", -3);
            }
        }
        try {
            if (signature2.verify(otaEncBinInfo.SignData)) {
                if (i6 == otaEncBinInfo.CheckSum) {
                    return bArr2;
                }
                throw new XpPsoException(this.context, "ota package CheckSum Error...", -5);
            }
            throw new XpPsoException(this.context, "bin Signed Data Verify  Fail...", -4);
        } catch (SignatureException e7) {
            Context context7 = this.context;
            throw new XpPsoException(context7, "bin Signed Data Verify  Fail...\r\n(" + e7.toString() + ")", -4);
        }
    }

    protected byte[] decryptVerifyM2ME28(int i, int i2, byte[] bArr, byte[] bArr2, Cipher cipher, Signature signature, byte[] bArr3, long[] jArr) throws XpPsoException {
        String str;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 10240 > i ? i : 10240;
        int i8 = (i / i7) + (i % i7 == 0 ? 0 : 1);
        int i9 = -4;
        String str2 = ")";
        if (i8 <= 1) {
            str = ")";
            i3 = -4;
            i4 = 0;
            i5 = 0;
        } else {
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            while (true) {
                if (i11 >= i8) {
                    i5 = i10;
                    str = str2;
                    i3 = i9;
                    break;
                } else if (!this.otaDecStop) {
                    int i13 = i12 + i7;
                    if (i13 > i) {
                        i5 = i10;
                        str = str2;
                        i3 = i9;
                        break;
                    }
                    int i14 = i10;
                    int i15 = i11;
                    String str3 = str2;
                    int i16 = i9;
                    try {
                        int update = cipher.update(bArr, i12, i7, bArr2, i14);
                        if (update <= 0) {
                            i6 = i14;
                        } else {
                            i6 = i14;
                            try {
                                signature.update(bArr2, i6, update);
                            } catch (SignatureException e) {
                                throw new XpPsoException(this.context, "bin Signed Data Verify update Fail...\r\n(" + e.toString() + str3, i16);
                            }
                        }
                        if (update > 0) {
                            i10 = i6 + update;
                            jArr[0] = i2 + i13;
                            i12 = i13;
                        } else {
                            i10 = i6;
                        }
                        i11 = i15 + 1;
                        i9 = i16;
                        str2 = str3;
                    } catch (ShortBufferException e2) {
                        throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(" + e2.toString() + str3, -3);
                    }
                } else {
                    throw new XpPsoException(this.context, "Decrypt Exit...", -33);
                }
            }
            i4 = i12;
        }
        int i17 = i - i4;
        if (i17 > 0) {
            try {
                byte[] doFinal = cipher.doFinal(bArr, i4, i17);
                if (doFinal != null) {
                    System.arraycopy(doFinal, 0, bArr2, i5, doFinal.length);
                }
                if (doFinal != null) {
                    try {
                        signature.update(doFinal);
                    } catch (SignatureException e3) {
                        throw new XpPsoException(this.context, "bin Signed Data Verify update Fail...\r\n(" + e3.toString() + str, i3);
                    }
                }
            } catch (BadPaddingException e4) {
                throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(" + e4.toString() + str, -3);
            } catch (IllegalBlockSizeException e5) {
                throw new XpPsoException(this.context, "Decrypt update Fail...\r\n(" + e5.toString() + str, -3);
            }
        }
        try {
            String bytesToHexString = HexUtils.bytesToHexString(getSHA256Bytes(bArr2));
            String bytesToHexString2 = HexUtils.bytesToHexString(bArr3);
            Log.i(TAG, "sha256sum correct?=" + bytesToHexString2.contains(bytesToHexString) + ",sha256result=" + bytesToHexString);
            return bArr2;
        } catch (Exception e6) {
            throw new XpPsoException(this.context, "sha256sum check Failed...\r\n(" + e6.toString() + str, i3);
        }
    }

    protected Cipher getAesDecryptCipherD21(int i, String str, String str2) throws XpPsoException {
        checkRootKey();
        Cipher cipher = null;
        try {
            String convertToOtaKeyAlisa = convertToOtaKeyAlisa(AKs, i);
            byte[] aesSpecKeyVector = NativeMethod.getAesSpecKeyVector(i);
            byte[] doFinal = getAesCbcDecryptCipher(convertToOtaKeyAlisa, aesSpecKeyVector).doFinal(NativeMethod.getEncAesSpecKey(i));
            SecretKeySpec secretKeySpec = new SecretKeySpec(doFinal, "AES");
            Arrays.fill(doFinal, (byte) 0);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(aesSpecKeyVector);
            cipher = Cipher.getInstance("AES/" + str + "/" + str2);
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            String exc = e.toString();
            if (cipher != null) {
                return cipher;
            }
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc + "", -15);
        }
    }

    protected Cipher getAesDecryptCipherE28(byte[] bArr, String str, String str2) throws XpPsoException {
        checkRootKey();
        Cipher cipher = null;
        try {
            byte[] bArr2 = new byte[16];
            System.arraycopy(bArr, 0, bArr2, 0, 16);
            byte[] doFinal = getAesDecryptCipher(convertToOtaKeyAlisa(bArr), bArr2, "CTR", "NoPadding").doFinal(NativeMethod.getE28enccKey(bArr));
            byte[] bArr3 = new byte[16];
            Arrays.fill(bArr3, (byte) 0);
            SecretKeySpec secretKeySpec = new SecretKeySpec(doFinal, "AES");
            Arrays.fill(doFinal, (byte) 0);
            System.arraycopy(bArr3, 0, bArr3, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr3);
            cipher = Cipher.getInstance("AES/" + str + "/" + str2);
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            String exc = e.toString();
            if (cipher != null) {
                return cipher;
            }
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
    }

    protected Cipher getBleAesDecryptCipherD22(byte[] bArr, String str, String str2) throws XpPsoException {
        checkRootKey();
        Cipher cipher = null;
        try {
            byte[] doFinal = getDefaultDecryptCipherD21(2).doFinal(NativeMethod.getD22encKey(bArr));
            byte[] bArr2 = new byte[16];
            Arrays.fill(bArr2, (byte) 0);
            SecretKeySpec secretKeySpec = new SecretKeySpec(doFinal, "AES");
            Arrays.fill(doFinal, (byte) 0);
            System.arraycopy(bArr2, 0, bArr2, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr2);
            cipher = Cipher.getInstance("AES/" + str + "/" + str2);
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            String exc = e.toString();
            if (cipher != null) {
                return cipher;
            }
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public XpengCarModel getCarType() {
        return carType;
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public long getDecFileToFileProcessedLen() {
        return this.decToFProcessedLen[0];
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public long getDecFileToMemoryProcessedLen() {
        return this.decToMProcessedLen[0];
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public long getDecMemoryToMemoryProcessedLen() {
        return this.decMToMProcessedLen[0];
    }

    protected Cipher getDefaultAesDecCipherE28(byte[] bArr) throws XpPsoException {
        checkRootKey();
        byte[] bArr2 = new byte[16];
        try {
            Arrays.fill(bArr2, (byte) 0);
            return getAesDecryptCipher(convertToOtaKeyAlisa(bArr), bArr2, "CTR", "NoPadding");
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
    }

    protected Cipher getDefaultDecryptCipherD21(int i) throws XpPsoException {
        checkRootKey();
        try {
            return getAesCbcDecryptCipher(convertToOtaKeyAlisa(AKs, i), NativeMethod.getAesSpecKeyVector(i));
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
    }

    protected Cipher getDefaultEncryptCipherD21(int i) throws XpPsoException {
        checkRootKey();
        try {
            return getAesCbcEncryptCipher(convertToOtaKeyAlisa(AKs, i), NativeMethod.getAesSpecKeyVector(i));
        } catch (Exception e) {
            String exc = e.toString();
            Context context = this.context;
            throw new XpPsoException(context, "get Aes Key Cipher Failed...maybe need to preset\r\n" + exc, -15);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0073 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0074  */
    @Override // com.xpeng.upso.IUniformedPsoCrypto
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getPSUID(java.lang.String r9) throws android.pso.XpPsoException {
        /*
            r8 = this;
            java.lang.String r0 = "HmacMD5"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r9)
            java.lang.String r2 = "A234567890123456"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = 0
            javax.crypto.spec.SecretKeySpec r3 = new javax.crypto.spec.SecretKeySpec     // Catch: java.lang.Exception -> L6b
            byte[] r1 = r1.getBytes()     // Catch: java.lang.Exception -> L6b
            r3.<init>(r1, r0)     // Catch: java.lang.Exception -> L6b
            javax.crypto.Mac r0 = javax.crypto.Mac.getInstance(r0)     // Catch: java.lang.Exception -> L6b
            r0.init(r3)     // Catch: java.lang.Exception -> L6b
            byte[] r9 = r9.getBytes()     // Catch: java.lang.Exception -> L6b
            byte[] r9 = r0.doFinal(r9)     // Catch: java.lang.Exception -> L6b
            r0 = 0
            java.lang.String r1 = ""
            r3 = r1
            r1 = r0
        L31:
            int r4 = r9.length     // Catch: java.lang.Exception -> L69
            if (r1 >= r4) goto L57
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L69
            r4.<init>()     // Catch: java.lang.Exception -> L69
            r4.append(r3)     // Catch: java.lang.Exception -> L69
            java.lang.String r5 = "%02X"
            r6 = 1
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch: java.lang.Exception -> L69
            r7 = r9[r1]     // Catch: java.lang.Exception -> L69
            java.lang.Byte r7 = java.lang.Byte.valueOf(r7)     // Catch: java.lang.Exception -> L69
            r6[r0] = r7     // Catch: java.lang.Exception -> L69
            java.lang.String r5 = java.lang.String.format(r5, r6)     // Catch: java.lang.Exception -> L69
            r4.append(r5)     // Catch: java.lang.Exception -> L69
            java.lang.String r3 = r4.toString()     // Catch: java.lang.Exception -> L69
            int r1 = r1 + 1
            goto L31
        L57:
            int r9 = r3.length()     // Catch: java.lang.Exception -> L69
            r0 = 40
            if (r9 <= r0) goto L71
            int r9 = r3.length()     // Catch: java.lang.Exception -> L69
            int r9 = r9 % r0
            java.lang.String r3 = r3.substring(r9)     // Catch: java.lang.Exception -> L69
            goto L71
        L69:
            r9 = move-exception
            goto L6d
        L6b:
            r9 = move-exception
            r3 = r2
        L6d:
            java.lang.String r2 = r9.toString()
        L71:
            if (r3 == 0) goto L74
            return r3
        L74:
            android.pso.XpPsoException r9 = new android.pso.XpPsoException
            android.content.Context r0 = r8.context
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "Get PSUID Failed...\r\n("
            r1.append(r3)
            r1.append(r2)
            java.lang.String r2 = ")"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r2 = -71
            r9.<init>(r0, r1, r2)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStorePsoImpl.getPSUID(java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected java.security.PublicKey getPublicKeyE28(byte[] r7) throws android.pso.XpPsoException {
        /*
            r6 = this;
            java.lang.String r0 = new java.lang.String
            r0.<init>(r7)
            byte[] r7 = com.xpeng.upso.NativeMethod.getE28Cert(r0)
            r0 = -2
            if (r7 == 0) goto L69
            r1 = 0
            byte[] r7 = com.xpeng.upso.utils.CertFileUtils.pemStringFormat2Der(r7)     // Catch: java.lang.Exception -> L1a java.io.IOException -> L20 java.security.cert.CertificateException -> L26
            java.security.cert.Certificate r1 = r6.getCertificate(r7)     // Catch: java.lang.Exception -> L1a java.io.IOException -> L20 java.security.cert.CertificateException -> L26
            java.security.PublicKey r7 = r1.getPublicKey()     // Catch: java.lang.Exception -> L1a java.io.IOException -> L20 java.security.cert.CertificateException -> L26
            return r7
        L1a:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            goto L2b
        L20:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            goto L2b
        L26:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
        L2b:
            java.lang.String r2 = ")"
            if (r1 != 0) goto L4b
            android.pso.XpPsoException r1 = new android.pso.XpPsoException
            android.content.Context r3 = r6.context
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "No Cert\r\n("
            r4.append(r5)
            r4.append(r7)
            r4.append(r2)
            java.lang.String r7 = r4.toString()
            r1.<init>(r3, r7, r0)
            throw r1
        L4b:
            android.pso.XpPsoException r0 = new android.pso.XpPsoException
            android.content.Context r1 = r6.context
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "No PublicKey\r\n("
            r3.append(r4)
            r3.append(r7)
            r3.append(r2)
            java.lang.String r7 = r3.toString()
            r2 = -14
            r0.<init>(r1, r7, r2)
            throw r0
        L69:
            android.pso.XpPsoException r7 = new android.pso.XpPsoException
            android.content.Context r1 = r6.context
            java.lang.String r2 = "No Cert(get E28 Cert pem)"
            r7.<init>(r1, r2, r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xpeng.upso.XpengKeyStorePsoImpl.getPublicKeyE28(byte[]):java.security.PublicKey");
    }

    protected byte[] getSHA256Bytes(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean isCertKeyWorking() throws XpPsoException {
        checkKeystore();
        checkRootKey();
        if (carType == XpengCarModel.D21) {
            return certKeyVerifyD21() & mcuKeyVerity();
        }
        throw new XpPsoException(this.context, "Can not Verify Cert Key ,Please user the input filename Api ...", -128);
    }

    protected byte[] luaDataDec(byte[] bArr) throws XpPsoException {
        checkRootKey();
        Cipher defaultDecryptCipherD21 = getDefaultDecryptCipherD21(3);
        byte[] bArr2 = new byte[bArr.length + (defaultDecryptCipherD21.getBlockSize() - (bArr.length % defaultDecryptCipherD21.getBlockSize()))];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        byte[] bArr3 = new byte[bArr.length];
        try {
            byte[] aesCrypt = aesCrypt(defaultDecryptCipherD21, bArr2);
            if (aesCrypt != null) {
                System.arraycopy(aesCrypt, 0, bArr3, 0, bArr.length);
                return bArr3;
            }
        } catch (Exception e) {
            e.toString();
        }
        return bArr3;
    }

    void luaDataEncAndSave() throws XpPsoException {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        String exc;
        File file = new File("/sdcard/Download/uds_security_access.lua");
        File file2 = new File("/sdcard/Download/uds_security_access.lua.enc");
        if (file.exists()) {
            byte[] bArr = null;
            try {
                if (file2.exists()) {
                    file2.delete();
                }
                file2.createNewFile();
                int length = (int) file.length();
                byte[] bArr2 = new byte[length];
                fileInputStream = new FileInputStream(file);
                fileOutputStream = new FileOutputStream(file2);
                fileInputStream.read(bArr2);
                try {
                    Cipher defaultEncryptCipherD21 = getDefaultEncryptCipherD21(3);
                    byte[] bArr3 = new byte[(defaultEncryptCipherD21.getBlockSize() - (length % defaultEncryptCipherD21.getBlockSize())) + length];
                    Arrays.fill(bArr3, (byte) 0);
                    System.arraycopy(bArr2, 0, bArr3, 0, length);
                    bArr = aesCrypt(defaultEncryptCipherD21, bArr3);
                    exc = null;
                } catch (Exception e) {
                    exc = e.toString();
                }
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            if (bArr == null) {
                throw new XpPsoException(this.context, "get lua Enc Data failed...\r\n(" + exc + ")", -73);
            }
            fileOutputStream.write(bArr);
            fileOutputStream.flush();
            fileInputStream.close();
            fileOutputStream.close();
            try {
                int otaGetSecurityAccessKeyFromLua = otaGetSecurityAccessKeyFromLua(bArr, 1829, 1111111, 1);
                Log.d(TAG, "ota_security_access_key_sampl result:" + String.format("%x(%d)", Integer.valueOf(otaGetSecurityAccessKeyFromLua), Long.valueOf(otaGetSecurityAccessKeyFromLua & InternalZipConstants.ZIP_64_SIZE_LIMIT)));
                int otaGetSecurityAccessKeyFromLua2 = otaGetSecurityAccessKeyFromLua(bArr, 1829, 1111111, 17);
                Log.d(TAG, "ota_security_access_key_sampl result:" + String.format("%x(%d)", Integer.valueOf(otaGetSecurityAccessKeyFromLua2), Long.valueOf(otaGetSecurityAccessKeyFromLua2 & InternalZipConstants.ZIP_64_SIZE_LIMIT)));
                int otaGetSecurityAccessKeyFromLua3 = otaGetSecurityAccessKeyFromLua(bArr, 1865, 1111111, 1);
                Log.d(TAG, "ota_security_access_key_sampl result:" + String.format("%x(%d)", Integer.valueOf(otaGetSecurityAccessKeyFromLua3), Long.valueOf(otaGetSecurityAccessKeyFromLua3 & InternalZipConstants.ZIP_64_SIZE_LIMIT)));
                int otaGetSecurityAccessKeyFromLua4 = otaGetSecurityAccessKeyFromLua(bArr, 1865, 1111111, 17);
                Log.d(TAG, "ota_security_access_key_sampl result:" + String.format("%x(%d)", Integer.valueOf(otaGetSecurityAccessKeyFromLua4), Long.valueOf(otaGetSecurityAccessKeyFromLua4 & InternalZipConstants.ZIP_64_SIZE_LIMIT)));
            } catch (XpPsoException e4) {
                e4.printStackTrace();
                int value = e4.getValue();
                Log.e(TAG, "get ota security access key...errorCode =" + String.valueOf(value) + ",Descrition:" + e4.toString());
            }
        }
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean mcuKeyPreset(String str) throws XpPsoException {
        checkKeystore();
        checkRootKey();
        return saveAesKeyWithCbc(convertToKeyAliasV2(AKm, 1), NativeMethod.getMcuRtKey());
    }

    boolean mcuKeyVerity() throws XpPsoException {
        if (otaGenMcuKey() != null) {
            if ("12345679".equals(new String(otaMcuDecrypt(otaMcuEncrypt("12345679".getBytes()))))) {
                Log.d(TAG, "Mcu Key Test succeed!");
                return true;
            }
            throw new XpPsoException(this.context, "Mcu Key Test Failed...!", -123);
        }
        throw new XpPsoException(this.context, "get Mcu Key Failed...!", -123);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public void otaDecStop() {
        if (carType == XpengCarModel.E28) {
            this.otaDecStop = true;
        }
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaGenMcuKey() throws XpPsoException {
        String exc;
        byte[] aesEncryptWithCbc;
        checkKeystore();
        checkRootKey();
        byte[] bArr = null;
        try {
            String convertToOtaKeyAlisa = convertToOtaKeyAlisa(AKm, 1);
            String str = null;
            for (int i = 0; i < 4; i++) {
                str = str + getMiscString(i);
            }
            aesEncryptWithCbc = aesEncryptWithCbc(convertToOtaKeyAlisa, hmacMd5(str), NativeMethod.getAesSpecKeyVector(0));
        } catch (Exception e) {
            exc = e.toString();
        }
        if (aesEncryptWithCbc != null) {
            return aesEncryptWithCbc;
        }
        exc = null;
        bArr = aesEncryptWithCbc;
        if (bArr != null) {
            return bArr;
        }
        throw new XpPsoException(this.context, "get MCU Key failed...maybe need to preset\r\n(" + exc + ")", -15);
    }

    protected int otaGetSecurityAccessKeyFromLua(byte[] bArr, int i, int i2, int i3) throws XpPsoException {
        long j;
        checkKeystore();
        String str = new String(luaDataDec(bArr));
        try {
            String str2 = String.format("%x", Integer.valueOf(i)) + "," + String.format("%x", Integer.valueOf(i2)) + "," + String.format("%x", Integer.valueOf(i3));
            Globals standardGlobals = JsePlatform.standardGlobals();
            standardGlobals.load(str).call(LuaValue.valueOf(str2));
            String luaValue = standardGlobals.get("re_key").toString();
            if (luaValue == null) {
                j = 0;
            } else {
                j = Long.valueOf(luaValue).longValue();
            }
            if (j != 0) {
                return (int) j;
            }
            throw new XpPsoException(this.context, "get ota security access key failed...", -73);
        } catch (Exception e) {
            throw new XpPsoException(this.context, "get ota security access key failed...\r\n(" + e.toString() + ")", -73);
        }
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaMcuDecrypt(byte[] bArr) throws XpPsoException {
        if (this.mcuSecretkey == null) {
            String str = null;
            try {
                this.mcuSecretkey = new SecretKeySpec(otaGenMcuKey(), "AES");
            } catch (XpPsoException e) {
                str = e.toString();
            }
            if (this.mcuSecretkey == null) {
                Context context = this.context;
                throw new XpPsoException(context, "get MCU Key failed ...maybe need to preset\r\n(" + str + ")", -15);
            }
        }
        return aesDecryptWithCtr(this.mcuSecretkey, bArr, NativeMethod.getAesSpecKeyVector(0));
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaMcuEncrypt(byte[] bArr) throws XpPsoException {
        String str;
        if (this.mcuSecretkey == null) {
            try {
                this.mcuSecretkey = new SecretKeySpec(otaGenMcuKey(), "AES");
            } catch (Exception e) {
                str = e.toString();
            }
        }
        str = null;
        SecretKey secretKey = this.mcuSecretkey;
        if (secretKey != null) {
            return aesEncryptWithCtr(secretKey, bArr, NativeMethod.getAesSpecKeyVector(0));
        }
        Context context = this.context;
        throw new XpPsoException(context, "get MCU Key failed ...maybe need to preset\r\n(" + str + ")", -15);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean otaPackageDecryptVerifyFileToFile(String str, String str2) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        if (carType == XpengCarModel.E28) {
            return otaPackageDecryptVerifyToFE28(str, str2, 2);
        }
        return otaPackageDecryptVerifyToFD21(str, str2, 2);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaPackageDecryptVerifyFileToFileE28(String str, String str2) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        return otaPackageDecryptVerifyToFWithMd5E28(str, str2, 2);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaPackageDecryptVerifyFileToMemory(String str) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        if (carType == XpengCarModel.E28) {
            return otaPackageDecryptVerifyToME28(str, 2);
        }
        return otaPackageDecryptCerifyToMD21(str, 2);
    }

    protected byte[] otaPackageDecryptVerifyMToMD21(byte[] bArr, int i) throws XpPsoException {
        Cipher defaultDecryptCipherD21;
        long otaPackageSeek = otaPackageSeek(bArr);
        NativeMethod.OtaEncBinInfo otaEncBinInfoFormMem = NativeMethod.getOtaEncBinInfoFormMem(bArr, otaPackageSeek);
        if (otaEncBinInfoFormMem.result == 0) {
            int i2 = (int) otaEncBinInfoFormMem.dstBinLen;
            byte[] bArr2 = new byte[i2];
            System.arraycopy(bArr, (int) (otaEncBinInfoFormMem.dstBinOffset + otaPackageSeek), bArr2, 0, i2);
            this.decMToMProcessedLen[0] = otaPackageSeek;
            int i3 = otaEncBinInfoFormMem.BINType;
            if (i3 > 2) {
                i3 = 2;
            }
            if (i == 2) {
                defaultDecryptCipherD21 = getAesDecryptCipherD21(i3, "CBC", "NoPadding");
            } else {
                defaultDecryptCipherD21 = getDefaultDecryptCipherD21(i3);
            }
            PublicKey otaEcuPublicKeyD21 = getOtaEcuPublicKeyD21();
            this.decMToMProcessedLen[0] = otaPackageSeek + otaEncBinInfoFormMem.dstBinOffset;
            Signature verifyEcSha512Signature = getVerifyEcSha512Signature(otaEcuPublicKeyD21);
            long[] jArr = this.decMToMProcessedLen;
            byte[] decryptVerifyM2MD21 = decryptVerifyM2MD21(otaEncBinInfoFormMem, (int) otaEncBinInfoFormMem.dstBinLen, (int) jArr[0], bArr2, defaultDecryptCipherD21, verifyEcSha512Signature, jArr);
            this.decMToMProcessedLen[0] = bArr.length;
            otaVerifyDecData(decryptVerifyM2MD21, (int) otaEncBinInfoFormMem.dstBinLen, getVerifyEcSha512Signature(otaEcuPublicKeyD21), otaEncBinInfoFormMem.SignData);
            if (decryptVerifyM2MD21 != null) {
                return decryptVerifyM2MD21;
            }
            throw new XpPsoException(this.context, "ota package decryptverify to mem failed...", -42);
        }
        throw new XpPsoException(this.context, "Invalid File...", -1);
    }

    protected byte[] otaPackageDecryptVerifyMToME28(byte[] bArr, int i) throws XpPsoException {
        int e28OtaPackheadLen = NativeMethod.getE28OtaPackheadLen();
        byte[] bArr2 = new byte[e28OtaPackheadLen];
        System.arraycopy(bArr, 0, bArr2, 0, e28OtaPackheadLen);
        NativeMethod.E28OtaPackhead e28OtaPackhead = NativeMethod.getE28OtaPackhead(bArr2, bArr.length);
        if (e28OtaPackhead != null) {
            this.decMToMProcessedLen[0] = e28OtaPackheadLen;
            int i2 = (int) e28OtaPackhead.datalen;
            byte[] bArr3 = new byte[i2];
            byte[] bArr4 = new byte[i2];
            System.arraycopy(bArr, e28OtaPackheadLen, bArr3, 0, i2);
            Cipher aesDecryptCipherE28 = i == 2 ? getAesDecryptCipherE28(e28OtaPackhead.key_id, "CTR", "NoPadding") : getDefaultAesDecCipherE28(e28OtaPackhead.key_id);
            PublicKey publicKeyE28 = getPublicKeyE28(e28OtaPackhead.cert_id);
            Signature verifyRsaSha256Signature = getVerifyRsaSha256Signature(publicKeyE28);
            decryptVerifyM2ME28((int) e28OtaPackhead.datalen, e28OtaPackheadLen, bArr3, bArr4, aesDecryptCipherE28, verifyRsaSha256Signature, e28OtaPackhead.datasha256, this.decMToMProcessedLen);
            try {
                verifyRsaSha256Signature.verify(e28OtaPackhead.sign);
                if (otaVerifyDecData(bArr4, e28OtaPackhead.datalen, getVerifyRsaSha256Signature(publicKeyE28), e28OtaPackhead.sign)) {
                    this.decMToMProcessedLen[0] = bArr.length;
                    return bArr4;
                }
                throw new XpPsoException(this.context, "ota package verify failed...", -4);
            } catch (SignatureException e) {
                Context context = this.context;
                throw new XpPsoException(context, "bin Signed Data Verify  Fail...\r\n(" + e.toString() + ")", -4);
            }
        }
        throw new XpPsoException(this.context, "Invalid File...\r\n(Packhead==null)", -1);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public byte[] otaPackageDecryptVerifyMemoryToMemory(byte[] bArr) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        if (carType == XpengCarModel.E28) {
            return otaPackageDecryptVerifyMToME28(bArr, 2);
        }
        return otaPackageDecryptVerifyMToMD21(bArr, 2);
    }

    boolean otaPackageDecryptVerifyToF(String str, String str2, int i) throws XpPsoException {
        checkKeystore();
        this.otaDecStop = false;
        if (carType == XpengCarModel.E28) {
            return otaPackageDecryptVerifyToFE28(str, str2, i);
        }
        return otaPackageDecryptVerifyToFD21(str, str2, i);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public int otaSecurityAccessKey(int i, int i2, int i3) throws XpPsoException {
        long j;
        checkKeystore();
        String str = new String(luaDataDec(NativeMethod.getUdsSecurityLuaEncData()));
        try {
            String str2 = String.format("%x", Integer.valueOf(i)) + "," + String.format("%x", Integer.valueOf(i2)) + "," + String.format("%x", Integer.valueOf(i3));
            Globals standardGlobals = JsePlatform.standardGlobals();
            standardGlobals.load(str).call(LuaValue.valueOf(str2));
            String luaValue = standardGlobals.get("re_key").toString();
            if (luaValue == null) {
                j = 0;
            } else {
                j = Long.valueOf(luaValue).longValue();
            }
            if (j != 0) {
                return (int) j;
            }
            throw new XpPsoException(this.context, "get ota security access key failed...", -73);
        } catch (Exception e) {
            throw new XpPsoException(this.context, "get ota security access key failed...\r\n(" + e.toString() + ")", -73);
        }
    }

    protected boolean otaVerifyDecData(byte[] bArr, long j, Signature signature, byte[] bArr2) throws XpPsoException {
        if (bArr.length == j) {
            try {
                signature.update(bArr);
                try {
                    if (signature.verify(bArr2)) {
                        return true;
                    }
                    throw new XpPsoException(this.context, "ota verify Dec Data failed...", -42);
                } catch (SignatureException e) {
                    Context context = this.context;
                    throw new XpPsoException(context, "ota verify Dec Data failed...\r\n(" + e.toString() + ")", -4);
                }
            } catch (SignatureException e2) {
                Context context2 = this.context;
                throw new XpPsoException(context2, "ota verify Dec Data signature update Fail...\r\n(" + e2.toString() + ")", -4);
            }
        }
        throw new XpPsoException(this.context, "Dec Data length is not correct", -1);
    }

    protected boolean otaVerifyDecFile(String str, long j, Signature signature, byte[] bArr) throws XpPsoException {
        String str2;
        long j2;
        File file = new File(str);
        if (file.exists()) {
            if (file.length() == j) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    int i = 10240 > j ? (int) j : 10240;
                    long j3 = i;
                    long j4 = (j / j3) + (j % j3 == 0 ? 0 : 1);
                    byte[] bArr2 = new byte[i];
                    String str3 = "Read File Failed\r\n(";
                    if (j4 > 1) {
                        int i2 = 0;
                        j2 = 0;
                        while (true) {
                            String str4 = str3;
                            if (i2 >= j4) {
                                str2 = str4;
                                break;
                            } else if (!this.otaDecStop) {
                                long j5 = j2 + j3;
                                if (j5 > j) {
                                    str2 = str4;
                                    break;
                                }
                                try {
                                    fileInputStream.read(bArr2);
                                    try {
                                        signature.update(bArr2);
                                        i2++;
                                        j2 = j5;
                                        str3 = str4;
                                    } catch (SignatureException e) {
                                        throw new XpPsoException(this.context, "ota verify dec file data signature update Fail...\r\n(" + e.toString() + ")", -4);
                                    }
                                } catch (IOException e2) {
                                    throw new XpPsoException(this.context, str4 + e2.toString() + ")", -22);
                                }
                            } else {
                                throw new XpPsoException(this.context, "Decrypt Exit...", -33);
                            }
                        }
                    } else {
                        str2 = "Read File Failed\r\n(";
                        j2 = 0;
                    }
                    int i3 = (int) (j - j2);
                    if (i3 > 0) {
                        byte[] bArr3 = new byte[i3];
                        try {
                            fileInputStream.read(bArr3);
                            try {
                                signature.update(bArr3);
                            } catch (SignatureException e3) {
                                throw new XpPsoException(this.context, "ota verify dec file data signature update Fail...\r\n(" + e3.toString() + ")", -4);
                            }
                        } catch (IOException e4) {
                            throw new XpPsoException(this.context, str2 + e4.toString() + ")", -22);
                        }
                    }
                    try {
                        if (signature.verify(bArr)) {
                            return true;
                        }
                        throw new XpPsoException(this.context, "ota verify dec file failed...", -42);
                    } catch (SignatureException e5) {
                        throw new XpPsoException(this.context, "ota verify dec file failed...\r\n(" + e5.toString() + ")", -4);
                    }
                } catch (IOException e6) {
                    throw new XpPsoException(this.context, "create FileInputStream failed...\r\n(" + e6.toString() + ")", -1);
                }
            }
            throw new XpPsoException(this.context, "file length is not correct", -1);
        }
        throw new XpPsoException(this.context, "ota dec file Not Exists...", -6);
    }

    protected boolean rsaCertKeyTest(byte[] bArr, byte[] bArr2, String str) throws XpPsoException {
        String exc;
        PrivateKey privateKey;
        boolean z;
        String str2 = new String(bArr);
        PublicKey publicKey = null;
        try {
            privateKey = convertToPrivateKeyObject(CertFileUtils.pemStringFormat2Der(str2));
            exc = null;
        } catch (Exception e) {
            exc = e.toString();
            privateKey = null;
        }
        if (privateKey != null) {
            byte[] e28Cert = NativeMethod.getE28Cert(new String(bArr2));
            if (e28Cert != null) {
                try {
                    publicKey = getCertificate(CertFileUtils.pemStringFormat2Der(e28Cert)).getPublicKey();
                } catch (IOException e2) {
                    exc = e2.toString();
                } catch (CertificateException e3) {
                    exc = e3.toString();
                }
                if (publicKey != null) {
                    try {
                        z = rsaVerify("tttttttPlanText", rsaSign("tttttttPlanText", privateKey), publicKey);
                    } catch (Exception e4) {
                        e4.printStackTrace();
                        z = false;
                    }
                    if (z && exc == null) {
                        return z;
                    }
                    throw new XpPsoException(this.context, "RSA cert Key Test " + str + ":" + new String(bArr2) + " Failed\r\n(" + exc + ")", -122);
                }
                throw new XpPsoException(this.context, "No Public Key", -14);
            }
            throw new XpPsoException(this.context, "No Cert Bytes", -2);
        }
        throw new XpPsoException(this.context, "No PrivateKey", -23);
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public void setCarType(XpengCarModel xpengCarModel) {
        carType = xpengCarModel;
    }

    protected String convertToKeyAlias(String str) throws XpPsoException {
        return convertToKeyAlias(str.getBytes());
    }

    protected String convertToKeyAliasV2(String str) throws XpPsoException {
        return convertToKeyAliasV2(str.getBytes());
    }

    protected String convertToOtaKeyAlisa(String str) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnrecoverableKeyException, KeyStoreException, ShortBufferException, XpPsoException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        return convertToOtaKeyAlisa(str.getBytes());
    }

    protected String convertToKeyAlias(byte[] bArr) throws XpPsoException {
        String str = new String(bArr) + "nnnnaks";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "HmacMD5");
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
            byte[] doFinal = mac.doFinal(str.getBytes());
            String convertToRootKeyAlisa = convertToRootKeyAlisa(AKalias, 0);
            for (int i = 0; i < 100; i++) {
                if (!hasAlias(convertToRootKeyAlisa) && i >= 99) {
                    genAliasRootKey();
                } else if (hasAlias(convertToRootKeyAlisa)) {
                    break;
                }
                Thread.sleep(5L);
            }
            return Base64Util.encodeToString(aesEncryptWithCbc(convertToRootKeyAlisa, doFinal, NativeMethod.getAesSpecKeyVector(0)));
        } catch (Exception e) {
            String exc = e.toString();
            throw new XpPsoException(this.context, "get Alias failed ...\r\n(" + exc + ")", -7);
        }
    }

    protected String convertToKeyAliasV2(byte[] bArr) throws XpPsoException {
        String exc;
        String str = new String(bArr) + "nnnnaks";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "HmacMD5");
        String str2 = null;
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
            byte[] doFinal = mac.doFinal(str.getBytes());
            str2 = convertToRootKeyAlisa(AKalias, 0);
            return Base64Util.encodeToString(aesEncryptWithCbc(str2, doFinal, NativeMethod.getAesSpecKeyVector(0)));
        } catch (InvalidKeyException e) {
            exc = e.toString();
            throw new XpPsoException(this.context, "get Alias failed ...(" + str2 + ")\r\n(" + exc + ")", -7);
        } catch (NoSuchAlgorithmException e2) {
            exc = e2.toString();
            throw new XpPsoException(this.context, "get Alias failed ...(" + str2 + ")\r\n(" + exc + ")", -7);
        } catch (Exception e3) {
            exc = e3.toString();
            throw new XpPsoException(this.context, "get Alias failed ...(" + str2 + ")\r\n(" + exc + ")", -7);
        }
    }

    protected String convertToOtaKeyAlisa(byte[] bArr) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnrecoverableKeyException, KeyStoreException, ShortBufferException, XpPsoException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        String str = new String(bArr) + "nnnnaks";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(secretKeySpec);
        return Base64Util.encodeToString(aesEncryptWithCbc(convertToRootKeyAlisa(AKalias, 0), mac.doFinal(str.getBytes()), NativeMethod.getAesSpecKeyVector(0)));
    }

    boolean mcuKeyPreset() throws XpPsoException {
        checkKeystore();
        checkRootKey();
        for (int i = 0; i < 4; i++) {
            if (saveAesKeyWithCbc(convertToKeyAlias(AKs, i), NativeMethod.getD21AesSpecKey(i), "AKs " + String.valueOf(i))) {
                Log.i(TAG, "S:AKs " + String.valueOf(i) + " succeed!");
            }
            if (stop) {
                return false;
            }
        }
        return saveAesKeyWithCbc(convertToKeyAliasV2(AKm, 1), NativeMethod.getMcuRtKey());
    }

    @Override // com.xpeng.upso.IUniformedPsoCrypto
    public boolean isCertKeyWorking(String str) throws XpPsoException {
        checkKeystore();
        checkRootKey();
        if (carType == XpengCarModel.D21) {
            return certKeyVerifyD21() & mcuKeyVerity();
        }
        return certKeyVerifyE28(str) & mcuKeyVerity();
    }

    public XpengKeyStorePsoImpl(Context context, boolean z) {
        super(z);
        this.getCertKeyPresetProgress = 0.0f;
        this.certKeyVerifyProgress = 0.0f;
        this.decMToMProcessedLen = new long[]{0};
        this.decToMProcessedLen = new long[]{0};
        this.decToFProcessedLen = new long[]{0};
        this.otaDecStop = false;
        this.mcuSecretkey = null;
        this.context = context.getApplicationContext();
    }

    private long otaPackageSeek(byte[] bArr) {
        long j;
        byte[] bArr2 = new byte[100];
        System.arraycopy(bArr, 0, bArr2, 0, 100);
        String str = new String(bArr2);
        if (!str.contains("\r\n\r\n")) {
            j = 0;
        } else {
            j = str.lastIndexOf("\r\n\r\n") + 4;
        }
        if (j < 0) {
            return 0L;
        }
        return j;
    }
}
