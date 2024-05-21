package com.xpeng.upso.selftest.tboxClientTest;

import android.pso.XpPsoException;
import android.util.Log;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengKeyStore;
import com.xpeng.upso.proxy.ProxyConstants;
import com.xpeng.upso.proxy.ProxyService;
import com.xpeng.upso.proxy.TeeEncrpytedContent;
import com.xpeng.upso.utils.KeyUtils;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
/* loaded from: classes2.dex */
public class TboxClientPreset extends XpengKeyStore {
    private static final String TAG = "Upso-TBOX_Preset";

    public TboxClientPreset() {
        super(ProxyService.isUsingAndroidKeystore());
    }

    public boolean isPreseted(XpengCarModel carModel, XpengCarChipModel chipModel) {
        int i = 0;
        while (true) {
            if (i >= 32) {
                return true;
            }
            String alias = genAlias(i, carModel, chipModel);
            try {
                boolean hasKey = getSecretKey(alias) != null;
                if (hasKey) {
                    i++;
                } else {
                    return false;
                }
            } catch (XpPsoException e) {
                Log.d(TAG, "isPreseted false");
                return false;
            }
        }
    }

    public String genAlias(long index, XpengCarModel carModel, XpengCarChipModel chipModel) {
        String keyIndexS = "tboxxpeng_alias_" + carModel.toString() + "_" + chipModel.toString() + ProxyConstants.ALIAS_PLAIN_POSTFIX + index;
        return KeyUtils.genAlias(keyIndexS);
    }

    public boolean delAesKey(String alias) {
        if (alias == null) {
            Log.d(TAG, "error,delAesKey, alias = " + alias);
            return false;
        } else if (hasAlias(alias)) {
            if (UpsoConfig.isLogEnabled()) {
                Log.d(TAG, "delAesKey, alias = " + alias);
            }
            delSecretKey(alias);
            return true;
        } else {
            return true;
        }
    }

    public boolean presetAesKeyByData(String alias, byte[] dataBytes) throws XpPsoException {
        if (alias == null) {
            throw new XpPsoException("SecretKey env error occurred");
        }
        if (UpsoConfig.isLogEnabled()) {
            Log.d(TAG, "presetAesKeyByData, alias = " + alias);
        }
        saveAesKeyWithCbcPkcs7(alias, dataBytes);
        return true;
    }

    public SecretKey getAesSecretKey(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            SecretKey secretKey = getSecretKey(alias);
            return secretKey;
        } catch (XpPsoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decryptAesCbcPkcs7(String alais, TeeEncrpytedContent content) {
        try {
            byte[] result = aesDecryptWithCbcPkcs7(alais, content.getContent(), content.getIv());
            int rawDataBytesLength = result.length - content.getNonceLength().intValue();
            byte[] rawDataBytes = new byte[rawDataBytesLength];
            System.arraycopy(result, content.getNonceLength().intValue(), rawDataBytes, 0, rawDataBytesLength);
            return new String(rawDataBytes, StandardCharsets.UTF_8).trim();
        } catch (XpPsoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] encryptAesCbcPkcs7(String alais, String content, String nounce, byte[] iv) {
        String data = nounce + content;
        try {
            byte[] result = aesEncryptWithCbcPkcs7(alais, data.getBytes(StandardCharsets.UTF_8), iv);
            return result;
        } catch (XpPsoException e) {
            e.printStackTrace();
            return null;
        }
    }
}
