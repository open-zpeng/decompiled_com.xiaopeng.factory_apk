package com.xpeng.upso;

import android.pso.XpPsoException;
import androidx.annotation.Keep;
@Keep
/* loaded from: classes2.dex */
public interface IUniformedPsoCrypto {
    public static final int OTA_DEC_MOD1 = 1;
    public static final int OTA_DEC_MOD2 = 2;

    byte[] blePackageDecryptVerifyMemoryToMemory(byte[] bArr, XpengCarModel xpengCarModel) throws XpPsoException;

    boolean certKeyPreset(String str) throws XpPsoException;

    boolean checkIfPreseted() throws XpPsoException;

    XpengCarModel getCarType();

    long getDecFileToFileProcessedLen();

    long getDecFileToMemoryProcessedLen();

    long getDecMemoryToMemoryProcessedLen();

    String getPSUID(String str) throws XpPsoException;

    boolean isCertKeyWorking() throws XpPsoException;

    boolean isCertKeyWorking(String str) throws XpPsoException;

    boolean mcuKeyPreset(String str) throws XpPsoException;

    void otaDecStop();

    byte[] otaGenMcuKey() throws XpPsoException;

    byte[] otaMcuDecrypt(byte[] bArr) throws XpPsoException;

    byte[] otaMcuEncrypt(byte[] bArr) throws XpPsoException;

    boolean otaPackageDecryptVerifyFileToFile(String str, String str2) throws XpPsoException;

    byte[] otaPackageDecryptVerifyFileToFileE28(String str, String str2) throws XpPsoException;

    byte[] otaPackageDecryptVerifyFileToMemory(String str) throws XpPsoException;

    byte[] otaPackageDecryptVerifyMemoryToMemory(byte[] bArr) throws XpPsoException;

    int otaSecurityAccessKey(int i, int i2, int i3) throws XpPsoException;

    void setCarType(XpengCarModel xpengCarModel);
}
