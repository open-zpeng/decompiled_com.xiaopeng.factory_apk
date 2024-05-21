package com.xiaopeng.factory.dmcommand;

import com.xiaopeng.commonfunc.model.car.XpDiagnosticModel;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class DmResponseWriter {
    private static final String TAG = "DmResponseWriter";
    private XpDiagnosticModel mXpDiagnosticModel;

    public DmResponseWriter(XpDiagnosticModel xpDiagnosticModel) {
        LogUtils.i(TAG, "Create DmResponseWriter");
        updateDmResponseWriter(xpDiagnosticModel);
    }

    public void updateDmResponseWriter(XpDiagnosticModel xpDiagnosticModel) {
        LogUtils.i(TAG, "update DmResponseWriter");
        this.mXpDiagnosticModel = xpDiagnosticModel;
    }

    public synchronized boolean write(byte[] msg) {
        boolean result;
        result = false;
        if (msg != null) {
            try {
                result = this.mXpDiagnosticModel.sendMcuDiagCmdAck(DataHelp.convert64Byte(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LogUtils.i(TAG, "write response :" + DataHelp.byteArrayToHexStr(msg) + ", result:" + result);
        return result;
    }
}
