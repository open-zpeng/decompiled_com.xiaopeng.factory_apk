package com.xiaopeng.libbluetooth;

import android.app.Application;
/* loaded from: classes2.dex */
public abstract class LibBtApplication extends Application {
    private BluetoothBoxes mBluetoothBoxes;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        this.mBluetoothBoxes = BluetoothBoxes.getInstance();
        this.mBluetoothBoxes.setContext(this);
        this.mBluetoothBoxes.connectBluetooth();
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        this.mBluetoothBoxes.disconnectBluetooth();
    }
}
