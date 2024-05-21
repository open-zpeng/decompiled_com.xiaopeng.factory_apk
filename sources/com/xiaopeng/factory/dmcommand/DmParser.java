package com.xiaopeng.factory.dmcommand;

import android.content.Context;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler;
import com.xiaopeng.factory.dmcommand.dmhandler.DmFactoryMode;
import com.xiaopeng.factory.dmcommand.dmhandler.DmHwParam;
import com.xiaopeng.factory.dmcommand.dmhandler.DmIndivTest;
import com.xiaopeng.factory.dmcommand.dmhandler.DmNavi;
import com.xiaopeng.factory.dmcommand.dmhandler.DmSecurityTest;
import com.xiaopeng.factory.dmcommand.dmhandler.DmTestResult;
import com.xiaopeng.factory.dmcommand.dmhandler.DmVersName;
import com.xiaopeng.factory.dmcommand.dmhandler.DmWifiTest;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.HashMap;
/* loaded from: classes.dex */
public class DmParser {
    public static final String TAG = "DmParser";
    private final HashMap<String, DmCommandHandler> mDmHandlers = new HashMap<>();

    public void registerDmComandHandler(Context context, DmResponseWriter writer) {
        LogUtils.i(TAG, "Register DM command handler for user binary");
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.FactoryMode.CMD_NAME, " "), new DmFactoryMode(context));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.VersName.CMD_NAME, " "), new DmVersName(context, writer));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.TestResult.CMD_NAME, " "), new DmTestResult(context));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.HwParam.CMD_NAME, " "), new DmHwParam(context, writer));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.SecurityTest.CMD_NAME, " "), new DmSecurityTest(context, writer));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.NaviTest.CMD_NAME, " "), new DmNavi(context, writer));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.IndivTest.CMD_NAME, " "), new DmIndivTest(context, writer));
        this.mDmHandlers.put(DataHelp.byteArrayToHexStr(DmUtil.WifiTest.CMD_NAME, " "), new DmWifiTest(context, writer));
    }

    public void unregisterDmComandHandler() {
        LogUtils.e(TAG, "unregister DM command handler");
        for (DmCommandHandler handler : this.mDmHandlers.values()) {
            handler.destroy();
        }
        this.mDmHandlers.clear();
    }

    public boolean process(byte[] cmd, DmResponseWriter writer) {
        if (runCmd(cmd, writer)) {
            LogUtils.i(TAG, "process done successfully. cmd = " + DataHelp.byteArrayToHexStr(cmd));
        }
        return true;
    }

    private boolean runCmd(byte[] cmd, DmResponseWriter writer) {
        LogUtils.i(TAG, "runCmd:" + DataHelp.byteArrayToHexStr(cmd));
        byte[] resData = null;
        if (cmd != null) {
            try {
                if (cmd.length >= 6) {
                    byte[] commandName = {cmd[2], cmd[3]};
                    byte[] argu = DataHelp.byteSub(cmd, 4, cmd.length - 4);
                    String key = DataHelp.byteArrayToHexStr(commandName);
                    LogUtils.i(TAG, "runCmd commandName : " + key);
                    DmCommandHandler handler = this.mDmHandlers.get(key);
                    if (handler != null) {
                        resData = handler.handleCommand(argu);
                        LogUtils.i(TAG, "CMD : " + DataHelp.byteArrayToHexStr(cmd) + ", result : " + DataHelp.byteArrayToHexStr(resData));
                    } else {
                        writer.write(DmUtil.responseNA(commandName, argu));
                    }
                    if (resData == null) {
                        return false;
                    }
                    writer.write(resData);
                    return true;
                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }
        LogUtils.i(TAG, "runCmd CMD format not correct");
        return false;
    }
}
