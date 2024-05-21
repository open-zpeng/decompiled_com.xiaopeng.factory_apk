package com.xiaopeng.factory.model.factorytest.hardwaretest.phy;

import com.xiaopeng.commonfunc.utils.FileUtil;
import com.xiaopeng.xmlconfig.Support;
/* loaded from: classes.dex */
public class PhyModel {
    public static final int PHY_LINK_DOWN = 0;
    public static final String PHY_LINK_DOWN_STRING = "DOWN";
    public static final int PHY_LINK_UP = 1;
    public static final String PHY_LINK_UP_STRING = "UP";
    public static final int PHY_MASTER_MODE = 1;
    public static final String PHY_MASTER_MODE_STRING = "master";
    public static final int PHY_SLAVE_MODE = 0;
    public static final String PHY_SLAVE_MODE_STRING = "slave";

    public int getPhyModeStatus() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.PHY_MODE));
    }

    public int getPhyLinkStatus() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.PHY_LINK));
    }

    public int getPhySqiStatus() {
        return FileUtil.readVal(Support.Path.getFilePath(Support.Path.PHY_SQI));
    }
}
