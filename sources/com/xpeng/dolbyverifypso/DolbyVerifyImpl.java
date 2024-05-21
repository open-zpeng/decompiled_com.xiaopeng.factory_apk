package com.xpeng.dolbyverifypso;

import android.content.Context;
import android.pso.XpPsoException;
import androidx.annotation.Keep;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.XpengCarChipModel;
import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.XpengKeyStore;
import com.xpeng.upso.sentry.SentryReporter;
import com.xpeng.upso.utils.Base64Util;
import com.xpeng.upso.utils.LogUtils;
import com.xpeng.upso.utils.SysPropUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
@Keep
/* loaded from: classes2.dex */
public class DolbyVerifyImpl extends XpengKeyStore implements IDolbyVerify {
    private static final String TAG = "Upso-DolbyVerifyImpl";
    private String alias;
    private Context mContext;
    private long taskStartTime;
    private XpengCarChipModel xpengCarChipModel;
    private XpengCarModel xpengCarModel;

    /* loaded from: classes2.dex */
    public class a implements Runnable {
        public final /* synthetic */ boolean a;

        public a(boolean z) {
            this.a = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SentryReporter option = SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setUiCarType(DolbyVerifyImpl.this.xpengCarModel.toString()).setOption(getClass().getSimpleName());
                option.setErrorDisc("checkPreset=" + this.a).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibDolbyVerifyVer("2.3.7").setLocalLog(LogUtils.getLog()).setDuration(System.currentTimeMillis() - DolbyVerifyImpl.this.taskStartTime).report(new DolbyVerifyException());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(DolbyVerifyImpl.TAG, e.toString());
            }
        }
    }

    /* loaded from: classes2.dex */
    public class b implements Runnable {
        public b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                SentryReporter.ReportBuilder().setCDUID(SysPropUtils.get(SysPropUtils.SYS_PROP_CDUID, "")).setUiCarType(DolbyVerifyImpl.this.xpengCarModel.toString()).setOption(getClass().getSimpleName()).setEventTime(System.currentTimeMillis()).setLibUpsoVer("2.3.7").setLibDolbyVerifyVer("2.3.7").setDuration(System.currentTimeMillis() - DolbyVerifyImpl.this.taskStartTime).report(new DolbyVerifySucceed(), false);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(DolbyVerifyImpl.TAG, e.toString());
            }
        }
    }

    public DolbyVerifyImpl(boolean z, Context context) {
        super(z);
        this.mContext = context;
        this.taskStartTime = System.currentTimeMillis();
        SentryReporter.init(this.mContext);
        LogUtils.i(TAG, "new DolbyVerifyImpl,ver=2.3.7");
    }

    private String genAlias(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel) {
        String str = this.alias;
        if (str != null) {
            return str;
        }
        String str2 = xpengCarModel.toString() + "_" + xpengCarChipModel.toString() + "_doooolby";
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "HmacMD5");
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
            String encodeToString = Base64Util.encodeToString(mac.doFinal(str2.getBytes()));
            this.alias = encodeToString;
            return encodeToString;
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    private void onDolbyVerifyFailedReport(boolean z) {
        LogUtils.e(TAG, "onDolbyVerifyFailedReport");
        new Thread(new a(z)).start();
    }

    private void onDolbyVerifySucceed() {
        LogUtils.e(TAG, "onDolbyVerifySucceed");
        new Thread(new b()).start();
    }

    public boolean DolbyVerify(String str) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
        if (hasAlias(str)) {
            char[] cArr = {154, 'C', 242, 22, 'n', 7, 26, '<', 187, 194, 188, '!', 145, '0', 213, 6};
            byte[] bArr = new byte[16];
            for (int i = 0; i < 16; i++) {
                bArr[i] = (byte) cArr[i];
            }
            char[] cArr2 = {'J', 177, 132, 151, '(', 'o', 229, 240, 174, 227, 237, 'j', 7, '%', ':', 17, ';', '^', '7', 233, 197, 245, 243, 162, '0', 29, 22, 153, 'q', 239, ';', 'a'};
            byte[] bArr2 = new byte[32];
            for (int i2 = 0; i2 < 32; i2++) {
                bArr2[i2] = (byte) cArr2[i2];
            }
            byte[] DolbyAesDecrypt = DolbyAesDecrypt(str, bArr, bArr2);
            if (DolbyAesDecrypt == null) {
                LogUtils.e(TAG, "DolbyAesDecrypt failed...");
                return false;
            }
            String str2 = null;
            try {
                str2 = new String(DolbyAesDecrypt, "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                LogUtils.e(TAG, e2.toString());
            }
            LogUtils.i(TAG, "Dolby Decrypt decData = " + str2);
            if (str2 != null && str2.equals("test for xpdolby secure key!!")) {
                LogUtils.i(TAG, "Dolby Decrypt Verify OK!!");
                onDolbyVerifySucceed();
                return true;
            }
            LogUtils.i(TAG, "Dolby Decrypt Verify failed...");
            onDolbyVerifyFailedReport(false);
            return false;
        }
        return false;
    }

    @Override // com.xpeng.dolbyverifypso.IDolbyVerify
    public boolean checkCrypo() throws XpPsoException {
        XpengCarModel xpengCarModel;
        XpengCarChipModel xpengCarChipModel = this.xpengCarChipModel;
        if (xpengCarChipModel != null && (xpengCarModel = this.xpengCarModel) != null) {
            String genAlias = genAlias(xpengCarModel, xpengCarChipModel);
            if (genAlias != null) {
                if (UpsoConfig.isLogEnabled()) {
                    LogUtils.d(TAG, "checkCrypo, alias = " + genAlias);
                }
                boolean hasAlias = hasAlias(genAlias);
                LogUtils.d(TAG, "checkCrypo, return = " + hasAlias);
                return hasAlias;
            }
            throw new XpPsoException("SecretKey env error occurred");
        }
        throw new XpPsoException("MUST set param first");
    }

    @Override // com.xpeng.dolbyverifypso.IDolbyVerify
    public boolean checkPreset() throws XpPsoException {
        XpengCarModel xpengCarModel;
        XpengCarChipModel xpengCarChipModel = this.xpengCarChipModel;
        if (xpengCarChipModel != null && (xpengCarModel = this.xpengCarModel) != null) {
            String genAlias = genAlias(xpengCarModel, xpengCarChipModel);
            if (genAlias != null) {
                if (UpsoConfig.isLogEnabled()) {
                    LogUtils.d(TAG, "checkPreset, alias = " + genAlias);
                }
                return DolbyVerify(genAlias);
            }
            throw new XpPsoException("SecretKey env error occurred");
        }
        throw new XpPsoException("MUST set param first");
    }

    @Override // com.xpeng.dolbyverifypso.IDolbyVerify
    public void enableSensitiveLog(boolean z) {
        UpsoConfig.setLogEnabled(z);
    }

    @Override // com.xpeng.dolbyverifypso.IDolbyVerify
    public void setParameters(XpengCarModel xpengCarModel, XpengCarChipModel xpengCarChipModel) {
        this.xpengCarModel = xpengCarModel;
        this.xpengCarChipModel = xpengCarChipModel;
    }
}
