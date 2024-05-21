package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsBluetoothControlCallback {
    public void onBluetoothServiceReady() {
    }

    public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) {
    }

    public void onDeviceFound(String address, String name, byte category) {
    }

    public void onAdapterStateChanged(int prevState, int newState) {
    }

    public void onAdapterDiscoverableModeChanged(int prevState, int newState) {
    }

    public void onAdapterDiscoveryStarted() {
    }

    public void onAdapterDiscoveryFinished() {
    }

    public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) {
    }

    public void onDeviceUuidsUpdated(String address, String name, int supportProfile) {
    }

    public void onLocalAdapterNameChanged(String name) {
    }

    public void onDeviceOutOfRange(String address) {
    }

    public void onDeviceAclDisconnected(String address) {
    }

    public void onBtRoleModeChanged(int mode) {
    }

    public void onBtAutoConnectStateChanged(String address, int prevState, int newState) {
    }

    public void onHfpStateChanged(String address, int prevState, int newState) {
    }

    public void onA2dpStateChanged(String address, int prevState, int newState) {
    }

    public void onAvrcpStateChanged(String address, int prevState, int newState) {
    }
}
