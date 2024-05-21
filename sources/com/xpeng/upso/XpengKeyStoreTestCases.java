package com.xpeng.upso;

import android.pso.XpPsoException;
import com.xpeng.upso.utils.KeyUtils;
import com.xpeng.upso.utils.LogUtils;
import javax.crypto.SecretKey;
/* loaded from: classes2.dex */
public class XpengKeyStoreTestCases extends XpengKeyStore {
    public void CryptoTest() {
        LogUtils.e("XpengKeyStoreTestCases", "\r\nCryptoTest ================");
        LogUtils.e("XpengKeyStoreTestCases", "\r\nCryptoTest PKCS7 ================");
        try {
            String genAlias = genAlias(1, XpengCarModel.E28, XpengCarChipModel.QUALCOMM_8155p);
            saveAesKeyWithCbcPkcs7(genAlias, "1234567890654321".getBytes());
            LogUtils.d("XpengKeyStoreTestCases", "CryptoTest preset, alias = " + genAlias);
            getSecretKey(genAlias);
            try {
                byte[] aesEncryptWithCbcPkcs7 = aesEncryptWithCbcPkcs7(genAlias, "123Hello World12".getBytes(), "0987654321123456".getBytes());
                LogUtils.d("XpengKeyStoreTestCases", "encodeed " + new String(aesEncryptWithCbcPkcs7));
                LogUtils.d("XpengKeyStoreTestCases", "encodeed length " + aesEncryptWithCbcPkcs7.length);
                byte[] aesDecryptWithCbcPkcs7 = aesDecryptWithCbcPkcs7(genAlias, aesEncryptWithCbcPkcs7, "0987654321123456".getBytes());
                LogUtils.d("XpengKeyStoreTestCases", "decoded " + new String(aesDecryptWithCbcPkcs7));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (XpPsoException e2) {
            e2.printStackTrace();
        }
        LogUtils.e("XpengKeyStoreTestCases", "\r\nCryptoTest PKCS7 END ================");
        LogUtils.e("XpengKeyStoreTestCases", "\r\nCryptoTest NOPADDING ================");
        try {
            String genAlias2 = genAlias(2, XpengCarModel.E28, XpengCarChipModel.QUALCOMM_8155p);
            saveAesKeyWithCbc(genAlias2, "1234567890654321".getBytes());
            LogUtils.d("XpengKeyStoreTestCases", "CryptoTest preset, alias = " + genAlias2);
            SecretKey secretKey = getSecretKey(genAlias2);
            byte[] aesDecryptWithCbc = aesDecryptWithCbc(secretKey, aesEncryptWithCbc(secretKey, "123Hello World12".getBytes(), "0987654321123456".getBytes()), "0987654321123456".getBytes());
            LogUtils.d("XpengKeyStoreTestCases", "decoded " + new String(aesDecryptWithCbc));
        } catch (XpPsoException e3) {
            e3.printStackTrace();
        }
        LogUtils.e("XpengKeyStoreTestCases", "\r\nCryptoTest NOPADDING end ================");
    }

    public String genAlias(int i, XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel) {
        return KeyUtils.genAlias(("ALIAS_PLAIN_PREFIX" + xpengCarModel.toString() + "_" + xpengCarChipModel.toString() + "ALIAS_PLAIN_POSTFIX") + i);
    }
}
