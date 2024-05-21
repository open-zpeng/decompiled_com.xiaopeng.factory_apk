package com.xpeng.dolbypresetpso;

import androidx.annotation.Keep;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.LogUtils;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
@Keep
/* loaded from: classes2.dex */
public class DolbyAesCbcPkcs5Cryptor {
    private static final String TAG = "DolbyAesCbcPkcs5Cryptor";

    public static byte[] decode(byte[] bArr) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Util.decode("QlZbRCEASUVqpKg/FefNZRKBugCZ10UFUGzskJkGqbc=".getBytes()), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            try {
                cipher.init(2, secretKeySpec, new IvParameterSpec(new byte[]{115, 97, 102, 101, 116, 121, WlanPresenter.WL_RMMOD_INSMOD, 98, 121, WlanPresenter.WL_RMMOD_INSMOD, 103, 108, 97, 114, 121, 107}));
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e2) {
                e2.printStackTrace();
            }
            try {
                byte[] doFinal = cipher.doFinal(bArr);
                new String(doFinal);
                return doFinal;
            } catch (Exception e3) {
                e3.printStackTrace();
                LogUtils.e(TAG, e3.toString());
                LogUtils.e(TAG, "return null");
                return null;
            }
        } catch (NoSuchAlgorithmException e4) {
            e4.printStackTrace();
        } catch (NoSuchPaddingException e5) {
            e5.printStackTrace();
        }
    }
}
