package com.xiaopeng.factory.model.factorytest.hardwaretest.temperature;

import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class TemperatureModel {
    private static final String TAG = "TemperatureModel";

    public double readCpuInternalTemp() {
        return FileUtil.readDoubleVal(Support.Path.getFilePath(Support.Path.CPU_INTERNAL_THERM)) / 1000.0d;
    }

    public double readSocTemp() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.SOC_THERM)) / 1000.0d;
    }

    public double readUfsTemp() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.UFS_THERM)) / 1000.0d;
    }

    public double readDdrTemp() {
        return FileUtil.readDoubleVal(Support.Path.getFilePath(Support.Path.DDR_THERM)) / 1000.0d;
    }

    public double readAmpTemp() {
        return FileUtil.readDoubleVal(Support.Path.getFilePath(Support.Path.AMP_THERM)) / 1000.0d;
    }

    public double readPmicTemp() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.PMIC_THERM)) / 1000.0d;
    }

    public double readPmic2Temp() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.PMIC_2_THERM)) / 1000.0d;
    }

    public double readGpuTemp() {
        return FileUtil.readDoubleVal(Support.Path.getFilePath(Support.Path.GPU_THERM)) / 1000.0d;
    }
}
