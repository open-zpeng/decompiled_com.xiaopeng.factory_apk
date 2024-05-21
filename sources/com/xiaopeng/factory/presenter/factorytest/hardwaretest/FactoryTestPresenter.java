package com.xiaopeng.factory.presenter.factorytest.hardwaretest;

import android.text.TextUtils;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.factory.atcommand.ResponseWriter;
import com.xiaopeng.factory.model.factorytest.hardwaretest.FactoryTestModel;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes2.dex */
public class FactoryTestPresenter {
    private static final String[][] COMPARE_TAG = {new String[]{"system.img.p", "vendor.img.p", "boot.img.p", "dsp.img.p", "dtbo.img.p", "rpm.img.p", "tz.img.p", "pmic.img.p", "hyp.img.p", "modem.img.p", "aboot.img.p", "bluetooth.img.p", "keymaster.img.p", "cmnlib.img.p", "cmnlib64.img.p", "vbmeta.img.p", "lksecapp.img.p", "devcfg.img.p", "xbl.img.p"}, new String[]{"system_a", "vendor_a", "boot_a", "dsp_a", "dtbo_a", "rpm_a", "tz_a", "pmic_a", "hyp_a", "modem_a", "aboot_a", "bluetooth_a", "keymaster_a", "cmnlib_a", "cmnlib64_a", "vbmeta_a", "lksecapp_a", "devcfg_a", "xbl_a"}, new String[]{"system_b", "vendor_b", "boot_b", "dsp_b", "dtbo_b", "rpm_b", "tz_b", "pmic_b", "hyp_b", "modem_b", "aboot_b", "bluetooth_b", "keymaster_b", "cmnlib_b", "cmnlib64_b", "vbmeta_b", "lksecapp_b", "devcfg_b", "xbl_b"}};
    private static final String ROM_SHA1SUM_FILE_FLASH = "/mnt/vmap/aftersales/block_sha1sum_after_flash.txt";
    private static final String ROM_SHA1SUM_FILE_FLASH_BY_BUILD = "/data/sha1sum_list.txt";
    private static final String TAG = "FactoryTestPresenter";
    private static final String TAG_CDU_VERSION = "VERSION";
    private final FactoryTestModel mFactoryTestModel;

    public FactoryTestPresenter() {
        this.mFactoryTestModel = new FactoryTestModel();
    }

    public FactoryTestPresenter(ResponseWriter responseWriter) {
        this.mFactoryTestModel = new FactoryTestModel(responseWriter);
    }

    public boolean checkRomSha1Sum() {
        int i;
        boolean res = true;
        Map<String, String> flashSh1Sum = new HashMap<>();
        Map<String, String> buildSh1Sum = new HashMap<>();
        String flashValue = FileUtil.readAll(ROM_SHA1SUM_FILE_FLASH, "\n");
        String buildValue = FileUtil.readAll(ROM_SHA1SUM_FILE_FLASH_BY_BUILD, "\n");
        String[] flashLines = flashValue.split("\n");
        int length = flashLines.length;
        char c = 0;
        int i2 = 0;
        while (true) {
            i = 1;
            if (i2 >= length) {
                break;
            }
            String line = flashLines[i2];
            String[] items = line.split(Constant.SPACE_2_STRING);
            if (items != null) {
                if (items.length > 1) {
                    flashSh1Sum.put(items[1], items[0]);
                } else if (items.length == 1) {
                    flashSh1Sum.put(TAG_CDU_VERSION, items[0]);
                }
            }
            i2++;
        }
        String[] buildLines = buildValue.split("\n");
        int length2 = buildLines.length;
        int i3 = 0;
        while (i3 < length2) {
            String line2 = buildLines[i3];
            String[] items2 = line2.split(Constant.SPACE_2_STRING);
            if (items2 != null) {
                if (items2.length > i) {
                    buildSh1Sum.put(items2[i], items2[0]);
                } else if (items2.length == 1) {
                    buildSh1Sum.put(TAG_CDU_VERSION, items2[0]);
                }
            }
            i3++;
            i = 1;
        }
        int i4 = 0;
        while (true) {
            String[][] strArr = COMPARE_TAG;
            if (i4 >= strArr[c].length) {
                break;
            }
            String build = buildSh1Sum.get(strArr[c][i4]);
            String flash_a = flashSh1Sum.get(COMPARE_TAG[1][i4]);
            String flash_b = flashSh1Sum.get(COMPARE_TAG[2][i4]);
            LogUtils.i(TAG, "i:" + i4 + ", build:" + build + ", flash_a:" + flash_a + ", flash_b:" + flash_b);
            if (TextUtils.isEmpty(build) || TextUtils.isEmpty(flash_a) || TextUtils.isEmpty(flash_b)) {
                break;
            } else if (!build.equalsIgnoreCase(flash_a)) {
                LogUtils.e(TAG, "compare fail: build sha1sum:" + COMPARE_TAG[0][i4] + " not equals to rom sha1sum:" + COMPARE_TAG[1][i4]);
                res = false;
                break;
            } else if (build.equalsIgnoreCase(flash_b)) {
                i4++;
                c = 0;
            } else {
                LogUtils.e(TAG, "compare fail: build sha1sum:" + COMPARE_TAG[0][i4] + " not equals to rom sha1sum:" + COMPARE_TAG[2][i4]);
                res = false;
                break;
            }
        }
        res = false;
        String buildVersion = buildSh1Sum.get(TAG_CDU_VERSION);
        String flashVersion = flashSh1Sum.get(TAG_CDU_VERSION);
        if (TextUtils.isEmpty(buildVersion) || TextUtils.isEmpty(flashVersion) || !buildVersion.equalsIgnoreCase(flashVersion)) {
            LogUtils.e(TAG, "compare fail: build version:" + buildVersion + " not equals to rom version:" + flashVersion);
            return false;
        }
        return res;
    }

    public boolean isNewHardwareVersion() {
        return BuildInfoUtils.getHardwareVersionCode() > 6;
    }

    public void requestMcuCheck() {
        this.mFactoryTestModel.requestMcuCheck();
    }

    public void enterMcuTestMode() {
        this.mFactoryTestModel.enterMcuTestMode();
    }

    public void exitMcuTestMode() {
        this.mFactoryTestModel.exitMcuTestMode();
    }

    public void enterTboxTestMode() {
        this.mFactoryTestModel.enterTboxTestMode();
    }

    public void exitTboxTestMode() {
        this.mFactoryTestModel.exitTboxTestMode();
    }

    public void enterAudioTestMode() {
        this.mFactoryTestModel.enterAudioTestMode();
    }

    public void exitAudioTestMode() {
        this.mFactoryTestModel.exitAudioTestMode();
    }

    public String getDtsVer() {
        return this.mFactoryTestModel.getDtsVer();
    }
}
