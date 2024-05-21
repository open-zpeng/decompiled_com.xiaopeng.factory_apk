package com.xiaopeng.factory.model.security;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.commonfunc.system.runnable.Sleep;
import com.xiaopeng.lib.utils.LogUtils;
import java.util.List;
/* loaded from: classes2.dex */
public class WifiAdmin {
    private static final String TAG = "WifiAdmin";
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    WifiManager.WifiLock mWifiLock;
    private final WifiManager mWifiManager;

    public WifiAdmin(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }
    }

    public int checkState() {
        int state = this.mWifiManager.getWifiState();
        LogUtils.i(TAG, "checkState : " + state);
        return state;
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }
    }

    public void creatWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    public void connectConfiguration(int index) {
        if (index > this.mWifiConfiguration.size()) {
            return;
        }
        this.mWifiManager.enableNetwork(this.mWifiConfiguration.get(index).networkId, true);
    }

    public void startScan() {
        this.mWifiManager.startScan();
        this.mWifiList = this.mWifiManager.getScanResults();
        this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.mWifiList.size(); i++) {
            stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
            stringBuilder.append(this.mWifiList.get(i).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getMacAddress();
    }

    public String getBSSID() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.getBSSID();
    }

    public String getSSID() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        String ssid = wifiInfo == null ? "NULL" : wifiInfo.getSSID();
        LogUtils.i(TAG, "getSSID : " + ssid);
        return ssid;
    }

    public int getIPAddress() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        int ip = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        LogUtils.i(TAG, "getIPAddress : " + ip);
        return ip;
    }

    public int getNetworkId() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return 0;
        }
        return wifiInfo.getNetworkId();
    }

    public String getWifiInfo() {
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        WifiInfo wifiInfo = this.mWifiInfo;
        return wifiInfo == null ? "NULL" : wifiInfo.toString();
    }

    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = this.mWifiManager.addNetwork(wcg);
        this.mWifiManager.saveConfiguration();
        this.mWifiManager.enableNetwork(wcgID, true);
    }

    public void disconnectWifi(int netId) {
        this.mWifiManager.disableNetwork(netId);
        this.mWifiManager.disconnect();
    }

    public void removeWIfiInfo(String ssid) {
        LogUtils.i(TAG, "removeWIfiInfo ssid:" + ssid);
        WifiConfiguration tempConfig = isExist(ssid);
        if (tempConfig != null) {
            this.mWifiManager.disableNetwork(tempConfig.networkId);
            this.mWifiManager.removeNetwork(tempConfig.networkId);
            this.mWifiManager.saveConfiguration();
        }
    }

    public WifiConfiguration createWifiInfo(String ssid, String passward, int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = Constant.DOUBLE_QUOTA + ssid + Constant.DOUBLE_QUOTA;
        WifiConfiguration tempConfig = isExist(ssid);
        if (tempConfig != null) {
            this.mWifiManager.removeNetwork(tempConfig.networkId);
        }
        if (type == 1) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (type == 2) {
            config.hiddenSSID = true;
            String[] strArr = config.wepKeys;
            strArr[0] = Constant.DOUBLE_QUOTA + passward + Constant.DOUBLE_QUOTA;
            config.allowedAuthAlgorithms.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }
        if (type == 3) {
            config.preSharedKey = Constant.DOUBLE_QUOTA + passward + Constant.DOUBLE_QUOTA;
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }
        return config;
    }

    private WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> existingConfigs = this.mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                String str = existingConfig.SSID;
                if (str.equals(Constant.DOUBLE_QUOTA + ssid + Constant.DOUBLE_QUOTA)) {
                    return existingConfig;
                }
            }
            return null;
        }
        return null;
    }

    public boolean waitConnectToServerWifi(String ssid, String passwd) {
        openWifi();
        int time = 10;
        while (true) {
            int time2 = time - 1;
            if (time <= 0 || checkState() == 3) {
                break;
            }
            Sleep.sleep(1000L);
            time = time2;
        }
        if (checkState() != 3) {
            LogUtils.i(TAG, "wifi not enabled");
            return false;
        }
        addNetwork(createWifiInfo(ssid, passwd, 3));
        startScan();
        int time3 = 45;
        while (true) {
            int time4 = time3 - 1;
            if (time3 <= 0) {
                break;
            }
            if (isWifiConnected(Constant.DOUBLE_QUOTA + ssid + Constant.DOUBLE_QUOTA)) {
                break;
            }
            if (time4 % 5 == 0) {
                startScan();
            }
            Sleep.sleep(1000L);
            time3 = time4;
        }
        if (!isWifiConnected(Constant.DOUBLE_QUOTA + ssid + Constant.DOUBLE_QUOTA)) {
            LogUtils.i(TAG, "wifi not connected to secure AP");
            return false;
        }
        return true;
    }

    public boolean isWifiConnected(String ssid_spec) {
        return ssid_spec.equals(getSSID()) && getIPAddress() != 0;
    }

    public void closeServerWifi(String ssid) {
        try {
            LogUtils.i(TAG, "closeServerWifi");
            removeWIfiInfo(ssid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
