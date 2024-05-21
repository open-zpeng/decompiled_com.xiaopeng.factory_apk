package com.xpeng.upso.cduc;

import android.pso.XpPsoException;
import com.xiaopeng.lib.security.xmartv1.KeyStoreHelper;
import com.xiaopeng.lib.security.xmartv1.XmartV1Constants;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengKeyStore;
import com.xpeng.upso.proxy.ProxyService;
import com.xpeng.upso.proxy.TeeEncrpytedContent;
import com.xpeng.upso.utils.LogUtils;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
/* loaded from: classes2.dex */
public class CduClientPreset extends XpengKeyStore {
    private static final String TAG = "Upso-CduPreset";

    public CduClientPreset() {
        super(ProxyService.isUsingAndroidKeystore());
    }

    public boolean isPreseted(XpengCarModel carModel, XpengCarChipModel chipModel) {
        int i = 0;
        while (true) {
            if (i >= 10) {
                return true;
            }
            String alias = genAlias(i);
            try {
                boolean hasKey = getSecretKey(alias) != null;
                if (hasKey) {
                    i++;
                } else {
                    return false;
                }
            } catch (XpPsoException e) {
                LogUtils.d(TAG, "isPreseted false");
                return false;
            }
        }
    }

    public String genAlias(long index) {
        return XmartV1Constants.LOCAL_KEYS_PREFIX + index;
    }

    public boolean delAesKey(String alias) {
        if (alias == null) {
            LogUtils.d(TAG, "error,delAesKey, alias = " + alias);
            return false;
        } else if (hasAlias(alias)) {
            if (UpsoConfig.isLogEnabled()) {
                LogUtils.d(TAG, "delAesKey, alias = " + alias);
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
            LogUtils.d(TAG, "presetAesKeyByData, alias = " + alias);
        }
        boolean result = saveAesKeyWithGcm(alias, dataBytes);
        return result;
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

    public String decryptAesGcm(String alais, TeeEncrpytedContent content) {
        LogUtils.d(TAG, "decryptAesGcm, alais =" + alais);
        try {
            KeyStoreHelper.init();
            byte[] result = KeyStoreHelper.decryptString(content.getContent(), alais, content.getIv());
            int rawDataBytesLength = result.length - content.getNonceLength().intValue();
            byte[] rawDataBytes = new byte[rawDataBytesLength];
            System.arraycopy(result, content.getNonceLength().intValue(), rawDataBytes, 0, rawDataBytesLength);
            return new String(rawDataBytes, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] encryptAesGcm(String alais, String content, String nounce, byte[] iv) {
        String data = nounce + content;
        try {
            KeyStoreHelper.init();
            byte[] result = KeyStoreHelper.encryptString(data.getBytes(StandardCharsets.UTF_8), alais, iv);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
