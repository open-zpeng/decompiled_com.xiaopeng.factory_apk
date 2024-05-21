package com.xiaopeng.libbluetooth;
/* loaded from: classes2.dex */
public abstract class AbsGeneralControlCallback {
    /* JADX INFO: Access modifiers changed from: protected */
    public void onBtPower(boolean isOn) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBtPairStatus(int status, String address) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBtConnectStatus(String address, boolean connectStatus, int linkLost, int profileType, int index) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onScanCallback(String address, String deviceName, int cod, int rssi, boolean complete) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBindSuccess() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBtRssiCallback(String address, int rssiValue) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSppDataCallback(int index, byte[] data, int dataLength) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPairRequest(String address, String name, int pinCode, boolean bssp) {
    }
}
