package com.xiaopeng.factory.model.factorytest.hardwaretest.psu;

import com.xiaopeng.commonfunc.bean.car.AndroidTest;
import com.xiaopeng.commonfunc.model.car.McuModel;
import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class PsuTestModel {
    private static final String TAG = "PsuTestModel";

    public void sendCanTestMessage() {
        AndroidTest msg = new AndroidTest();
        msg.canTest = 1;
        LogUtils.i(TAG, "sendFactoryTestMsgToMcu data = " + msg.toString());
        new McuModel(TAG).sendFactoryTestMsgToMcu(msg.packToIntArray());
    }

    public boolean isPsuExit(int vid, int pid) {
        return FileUtil.isUsbDeviceAttached(MyApplication.getContext(), vid, pid);
    }
}
