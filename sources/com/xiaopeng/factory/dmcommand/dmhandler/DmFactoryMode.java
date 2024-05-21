package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.SystemPropertyUtil;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class DmFactoryMode extends DmCommandHandler {
    public DmFactoryMode(Context context) {
        super(context);
        this.CMD_NAME = DmUtil.FactoryMode.CMD_NAME;
        this.CLASS_NAME = "DmFactoryMode";
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        if (checkArgu(argu, new byte[]{1, 0})) {
            LogUtils.i(this.CLASS_NAME, "enable factory mode");
            SystemPropertyUtil.setFactoryMode(true);
            resData = responseOK(argu);
        } else if (checkArgu(argu, new byte[]{1, 1})) {
            LogUtils.i(this.CLASS_NAME, "disable factory mode");
            SystemPropertyUtil.setFactoryMode(false);
            resData = responseOK(argu);
        } else if (checkArgu(argu, new byte[]{2, 0})) {
            boolean isFactoryMode = SystemPropertyUtil.isFactoryMode();
            String str = this.CLASS_NAME;
            LogUtils.i(str, "isFactoryMode : " + isFactoryMode);
            if (isFactoryMode) {
                resData = responseWithValue(argu, DmUtil.FactoryMode.STATUS_ON);
            } else {
                byte[] resData2 = DmUtil.FactoryMode.STATUS_OFF;
                resData = responseWithValue(argu, resData2);
            }
        } else {
            resData = responseNA(argu);
        }
        return resData;
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public void destroy() {
        super.destroy();
    }
}
