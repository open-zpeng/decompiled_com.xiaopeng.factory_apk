package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsUsbBluetoothCallback {
    public void onUsbPairedDevices(int state, String address, String name) {
    }

    public void onUsbDeviceFound(String address, String name, byte category) {
    }

    public void onUsbAdapterStateChanged(int prevState, int newState) {
    }

    public void onUsbAdapterDiscoveryStarted() {
    }

    public void onUsbAdapterDiscoveryFinished() {
    }

    public void onUsbDeviceBondStateChanged(String address, String name, int prevState, int newState) {
    }

    public void onUsbHfpStateChanged(String address, int prevState, int newState) {
    }

    public void onUsbA2dpStateChanged(String address, int prevState, int newState) {
    }

    public void onUsbAvrcpStateChanged(String address, int prevState, int newState) {
    }

    public void onUsbDeviceState(int newState) {
    }

    public void onUsbConnectionStateChanged(String address, int prevState, int newState) {
    }
}
