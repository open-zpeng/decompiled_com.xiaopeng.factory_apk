package com.xiaopeng.factory.model.factorytest.hardwaretest;

import com.xiaopeng.commonfunc.bean.car.AndroidTest;
import com.xiaopeng.commonfunc.model.car.McuModel;
/* loaded from: classes.dex */
public class SensorTestModel {
    private static final String TAG = "SensorTestModel";

    public void sendGsensorTestMessage() {
        AndroidTest mcuMsg = new AndroidTest();
        mcuMsg.gnssComDet = 1;
        new McuModel(TAG).sendFactoryTestMsgToMcu(mcuMsg.packToIntArray());
    }
}
