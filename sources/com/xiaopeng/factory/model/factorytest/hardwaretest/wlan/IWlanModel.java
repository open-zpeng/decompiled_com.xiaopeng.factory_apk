package com.xiaopeng.factory.model.factorytest.hardwaretest.wlan;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
/* loaded from: classes.dex */
public interface IWlanModel {
    void addNetwork(WifiConfiguration wifiConfiguration);

    int checkState();

    void closeWifi();

    WifiConfiguration createWifiInfo(String str);

    WifiConfiguration createWifiInfo(String str, String str2, int i);

    void forgetAllWifi();

    void forgetWIfi(String str);

    int getIPAddress();

    String getMacAddress();

    String getSSID();

    WifiInfo getWifiInfo();

    String getWlanLocalAddr();

    void onInit();

    void openWifi();
}
