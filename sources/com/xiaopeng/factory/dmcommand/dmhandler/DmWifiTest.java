package com.xiaopeng.factory.dmcommand.dmhandler;

import android.content.Context;
import android.text.format.Formatter;
import com.xiaopeng.commonfunc.system.runnable.Sleep;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.List;
/* loaded from: classes.dex */
public class DmWifiTest extends DmCommandHandler {
    private final IWlanPresenter mWlanPresenter;

    public DmWifiTest(Context context, DmResponseWriter dmResponseWriter) {
        super(context);
        this.CMD_NAME = DmUtil.WifiTest.CMD_NAME;
        this.CLASS_NAME = "DmWifiTest";
        this.mWlanPresenter = new WlanPresenter(dmResponseWriter);
    }

    @Override // com.xiaopeng.factory.dmcommand.dmhandler.DmCommandHandler
    public synchronized byte[] handleCommand(byte[] argu) {
        byte[] resData;
        resData = null;
        if (checkArgu(argu, new byte[]{0, 0})) {
            LogUtils.i(this.CLASS_NAME, "openWifi");
            if (3 != this.mWlanPresenter.checkState()) {
                this.mWlanPresenter.registerReceiver();
                this.mWlanPresenter.openWifi();
            } else {
                resData = responseOK(argu);
            }
        } else if (checkArgu(argu, new byte[]{0, 1})) {
            LogUtils.i(this.CLASS_NAME, "closeWifi");
            if (1 != this.mWlanPresenter.checkState()) {
                this.mWlanPresenter.registerReceiver();
                this.mWlanPresenter.closeWifi();
            } else {
                resData = responseOK(argu);
            }
        } else if (checkArgu(argu, new byte[]{1, 0})) {
            int state = this.mWlanPresenter.checkState();
            String str = this.CLASS_NAME;
            LogUtils.i(str, "checkState  state = " + state);
            if (state == 3) {
                resData = responseWithValue(argu, DmUtil.WifiTest.WLAN_STATUS_ON);
            } else {
                resData = responseWithValue(argu, DmUtil.WifiTest.WLAN_STATUS_OFF);
            }
        } else if (checkArgu(argu, new byte[]{1, 1})) {
            String mac = this.mWlanPresenter.getMacAddress();
            String str2 = this.CLASS_NAME;
            LogUtils.i(str2, "getMacAddress  mac = " + mac);
            resData = responseWithValue(argu, mac.getBytes());
        } else if (checkArgu(argu, new byte[]{1, 2})) {
            String ssid = this.mWlanPresenter.getSSID();
            String str3 = this.CLASS_NAME;
            LogUtils.i(str3, "getSSID  ssid = " + ssid);
            resData = responseWithValue(argu, ssid.getBytes());
        } else if (checkArgu(argu, new byte[]{1, 3})) {
            int ip = this.mWlanPresenter.getIPAddress();
            if (ip != 0) {
                resData = responseWithValue(argu, DataHelp.strToByteArray(Formatter.formatIpAddress(ip)));
            } else {
                resData = responseNG(argu);
            }
        } else if (checkArgu(argu, new byte[]{2, 0})) {
            List<String> params = parseArgu(argu);
            if (params.size() > 0) {
                String ssid2 = params.get(0);
                if (params.size() > 1) {
                    String pass = params.get(1);
                    this.mWlanPresenter.addNetwork(this.mWlanPresenter.createWifiInfo(ssid2, pass, 3));
                } else {
                    this.mWlanPresenter.addNetwork(this.mWlanPresenter.createWifiInfo(ssid2));
                }
            } else {
                resData = responseNA(argu);
            }
        } else if (checkArgu(argu, new byte[]{2, 1})) {
            this.mWlanPresenter.forgetAllWifi();
            Sleep.sleep(1000L);
            resData = responseOK(argu);
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
