package com.xpeng.upso;

import androidx.annotation.Keep;
import java.util.ArrayList;
@Keep
/* loaded from: classes2.dex */
public class NativeMethod {

    @Keep
    /* loaded from: classes2.dex */
    public class E28OtaPackhead {
        public byte[] cert_id;
        public long datalen;
        public byte[] datasha256;
        public byte[] headsha256;
        public byte[] key_id;
        public byte[] sign;
        public long timestamp;
        public byte[] vin;

        public E28OtaPackhead() {
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public class E28decKeybin {
        public ArrayList<KeyInfo> CommKey;
        public ArrayList<CertInfo> CommSignCert;
        public ArrayList<KeyInfo> OwnKey;
        public ArrayList<CertInfo> OwnSignCert;
        public int result;

        @Keep
        /* loaded from: classes2.dex */
        public class CertInfo {
            public byte[] CertPem;
            public byte[] ID;
            public byte[] PriPem;

            public CertInfo() {
            }
        }

        @Keep
        /* loaded from: classes2.dex */
        public class KeyInfo {
            public byte[] ID;
            public byte[] KeyValue;

            public KeyInfo() {
            }
        }

        public E28decKeybin() {
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public class OtaEncBinInfo {
        public int BINHeaderDubExVer;
        public int BINType;
        public int CheckSum;
        public byte[] SignData;
        public long dstBinLen;
        public long dstBinOffset;
        public int result;

        public OtaEncBinInfo() {
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public class PSOdecKeybin {
        public ArrayList<byte[]> ExtAES;
        public ArrayList<byte[]> ExtCert;
        public ArrayList<Integer> ExtCertLen;
        public ArrayList<byte[]> ExtCustomkey;
        public ArrayList<byte[]> ExtPrivateKey;
        public ArrayList<Integer> ExtPrivateKeyLen;
        public ArrayList<byte[]> IntAES;
        public ArrayList<byte[]> IntCert;
        public ArrayList<Integer> IntCertLen;
        public ArrayList<byte[]> IntCustomkey;
        public ArrayList<byte[]> IntPrivateKey;
        public ArrayList<Integer> IntPrivateKeyLen;
        public int Version;
        public int result;

        public PSOdecKeybin() {
        }
    }

    @Keep
    /* loaded from: classes2.dex */
    public class PSOencKeybinData {
        public int BINHeaderDubExVer;
        public int BINType;
        public byte[] EncData;
        public byte[] SignData;
        public int Version;
        public int result;

        public PSOencKeybinData() {
        }
    }

    public static native byte[] doXor(int i, byte[] bArr);

    public static native byte[] getAesSpecKeyVector(int i);

    public static native int getCheckSum(byte[] bArr);

    public static native byte[] getD21AesSpecKey(int i);

    public static native byte[] getD21OtaEcuCert();

    public static native byte[] getD22encKey(byte[] bArr);

    public static native byte[] getE28Cert(String str);

    public static native E28OtaPackhead getE28OtaPackhead(byte[] bArr, long j);

    public static native int getE28OtaPackheadLen();

    public static native byte[] getE28enccKey(byte[] bArr);

    public static native byte[] getE28enccKeyId(int i);

    public static native byte[] getEncAesSpecKey(int i);

    public static native PSOencKeybinData getKeybinEncData(byte[] bArr);

    public static native PSOdecKeybin getKeybinList(byte[] bArr, int i);

    public static native byte[] getKeybinVerifyKey(int i);

    public static native byte[] getMcuRtKey();

    public static native OtaEncBinInfo getOtaEncBinInfo(String str, long j);

    public static native OtaEncBinInfo getOtaEncBinInfoFormMem(byte[] bArr, long j);

    public static native byte[] getUdsSecurityLuaEncData();

    public static native E28decKeybin processKeyBin(String str);
}
