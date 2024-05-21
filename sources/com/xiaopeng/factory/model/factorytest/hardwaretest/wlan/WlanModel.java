package com.xiaopeng.factory.model.factorytest.hardwaretest.wlan;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.xiaopeng.commonfunc.model.test.WifiTest;
import com.xiaopeng.factory.MyApplication;
/* loaded from: classes.dex */
public class WlanModel implements IWlanModel {
    private static final String TAG = "WlanModel";
    private final Context mContext;
    private final WifiTest mWifiTest;

    public WlanModel() {
        this.mContext = MyApplication.getContext();
        this.mWifiTest = new WifiTest(this.mContext);
    }

    public WlanModel(WifiManager.ActionListener connectListener, WifiManager.ActionListener forgetListener) {
        this.mContext = MyApplication.getContext();
        this.mWifiTest = new WifiTest(this.mContext, connectListener, forgetListener);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void onInit() {
        this.mWifiTest.onInit();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void openWifi() {
        this.mWifiTest.openWifi();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void closeWifi() {
        this.mWifiTest.closeWifi();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public int checkState() {
        return this.mWifiTest.checkState();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public String getMacAddress() {
        return this.mWifiTest.getMacAddress();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public String getSSID() {
        return this.mWifiTest.getSSID();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public int getIPAddress() {
        return this.mWifiTest.getIPAddress();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void addNetwork(WifiConfiguration wcg) {
        this.mWifiTest.addNetwork(wcg);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public WifiConfiguration createWifiInfo(String SSID) {
        return this.mWifiTest.createWifiInfo(SSID);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public WifiConfiguration createWifiInfo(String ssid, String pass, int type) {
        return this.mWifiTest.createWifiInfo(ssid, pass, type);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void forgetWIfi(String SSID) {
        this.mWifiTest.forgetWifi(SSID);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public void forgetAllWifi() {
        this.mWifiTest.forgetAllWifi();
    }

    private WifiConfiguration isExist(String SSID) {
        return this.mWifiTest.isExist(SSID);
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public String getWlanLocalAddr() {
        return this.mWifiTest.getWlanLocalAddr();
    }

    @Override // com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel
    public WifiInfo getWifiInfo() {
        return this.mWifiTest.getWifiInfo();
    }
}
