package com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
/* loaded from: classes2.dex */
public interface IWlanPresenter {
    void addNetwork(WifiConfiguration wifiConfiguration);

    int checkState();

    void closeWifi();

    WifiConfiguration createWifiInfo(String str);

    WifiConfiguration createWifiInfo(String str, String str2, int i);

    void deinit4Dm();

    void forgetAllWifi();

    void forgetWIfi(String str);

    int getIPAddress();

    String getMacAddress();

    String getSSID();

    WifiInfo getWifiInfo();

    String getWlanLocalAddr();

    void insmode_test_driver();

    void int4Dm();

    void onInit();

    void openWifi();

    void readWlanCounts();

    void registerReceiver();

    void rmmod_driver();

    void run24bRxTest(byte[] bArr);

    void run24bTest(byte[] bArr);

    void run24gRxTest(byte[] bArr);

    void run24gTest(byte[] bArr);

    void run24nRxTest(byte[] bArr);

    void run24nTest(byte[] bArr);

    void runRxTest(byte[] bArr);

    void runTxTest(byte[] bArr);

    void stopNsRxTest();

    void stopNsTest();

    void unregisterReceiver();

    void wlanRmmodAndInsmod(byte[] bArr);

    void wlanStartRfCmd();
}
