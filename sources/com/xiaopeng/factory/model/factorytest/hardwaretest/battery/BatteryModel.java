package com.xiaopeng.factory.model.factorytest.hardwaretest.battery;

import com.xiaopeng.commonfunc.model.car.McuModel;
/* loaded from: classes.dex */
public class BatteryModel {
    public static final int CMD_BMS_CHARGE = 1;
    public static final int CMD_BMS_DISCHARGE = 2;
    private static final String TAG = "BatteryModel";
    private final McuModel mMcuModel = new McuModel(TAG);

    public void stopDischarge() {
        this.mMcuModel.sendFactoryMcuBmsMsgToMcu(1);
    }

    public void enableDischarge() {
        this.mMcuModel.sendFactoryMcuBmsMsgToMcu(2);
    }
}
