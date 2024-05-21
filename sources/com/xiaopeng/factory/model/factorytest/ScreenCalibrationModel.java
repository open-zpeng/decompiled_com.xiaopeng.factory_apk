package com.xiaopeng.factory.model.factorytest;

import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class ScreenCalibrationModel {
    public static final int CAL_RESULT_CHECKING = 2;
    public static final int CAL_RESULT_NG = 1;
    public static final int CAL_RESULT_OK = 0;
    private static final String TAG = "ScreenCalibrationModel";
    private static final String VALUE_CALIBRATE_MUTUAL_CAP = "0,1";

    public void doCalibrate() {
        FileUtil.write(Support.Path.getFilePath(Support.Path.IVI_SCREEN_CALIBRATION), VALUE_CALIBRATE_MUTUAL_CAP);
    }

    public String getCalibrationResult() {
        return FileUtil.read(Support.Path.getFilePath(Support.Path.IVI_SCREEN_CALIBRATION));
    }
}
