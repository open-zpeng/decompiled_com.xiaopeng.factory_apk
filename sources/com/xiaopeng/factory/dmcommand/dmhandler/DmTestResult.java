package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import com.xiaopeng.commonfunc.bean.factorytest.AgingTestResult;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.commonfunc.utils.TestResultUtil;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class DmTestResult extends DmCommandHandler {
    public DmTestResult(Context context) {
        super(context);
        this.CMD_NAME = DmUtil.TestResult.CMD_NAME;
        this.CLASS_NAME = "DmTestResult";
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        if (checkArgu(argu, new byte[]{0, 0})) {
            int id = ((argu[2] & 255) * 256) + (argu[3] & 255);
            byte value = argu[4];
            String str = this.CLASS_NAME;
            LogUtils.i(str, "write id:" + id + ", value:" + ((char) value));
            if (TestResultUtil.writeTestResult(id, value)) {
                resData = responseOK(argu);
            } else {
                resData = responseNG(argu);
            }
        } else if (checkArgu(argu, new byte[]{1, 0})) {
            int id2 = ((argu[2] & 255) * 256) + (argu[3] & 255);
            byte value2 = TestResultUtil.readTestResult(id2);
            String str2 = this.CLASS_NAME;
            LogUtils.i(str2, "read id:" + id2 + ", value:" + ((char) value2));
            resData = responseWithValue(argu, new byte[]{value2});
        } else if (checkArgu(argu, new byte[]{2, 0})) {
            byte value3 = argu[2];
            String str3 = this.CLASS_NAME;
            LogUtils.i(str3, "full write value:" + ((char) value3));
            if (TestResultUtil.createTestResultFile(value3)) {
                resData = responseOK(argu);
            } else {
                resData = responseNG(argu);
            }
        } else if (checkArgu(argu, new byte[]{3, 0})) {
            if (TestResultUtil.createAgingTestResultFile()) {
                resData = responseOK(argu);
            } else {
                resData = responseNG(argu);
            }
        } else if (checkArgu(argu, new byte[]{3, 1})) {
            String result = TestResultUtil.readAgingTestRecord();
            AgingTestResult mAgingTestResult = new AgingTestResult(result);
            resData = responseWithValue(argu, mAgingTestResult.toByteArrayForDm());
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
