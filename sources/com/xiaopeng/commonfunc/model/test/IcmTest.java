package com.xiaopeng.commonfunc.model.test;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.icm.CarIcmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
/* loaded from: classes.dex */
public class IcmTest {
    private Car mCar;
    private CarIcmManager mIcmManager;
    public String TAG = "IcmTest";
    private ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.xiaopeng.commonfunc.model.test.IcmTest.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            String str = IcmTest.this.TAG;
            Log.i(str, "onServiceConnected, name: " + name + ", service: " + service);
            try {
                IcmTest.this.mIcmManager = (CarIcmManager) IcmTest.this.mCar.getCarManager("xp_icm");
            } catch (CarNotConnectedException e) {
                String str2 = IcmTest.this.TAG;
                Log.e(str2, "get IcmManager failed!" + e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            String str = IcmTest.this.TAG;
            Log.i(str, "onServiceDisconnected, name: " + name);
            IcmTest.this.mIcmManager = null;
        }
    };

    public IcmTest(Context context) {
        this.mCar = Car.createCar(context, this.mServiceConnection);
        try {
            this.mCar.connect();
        } catch (IllegalStateException e) {
            String str = this.TAG;
            Log.e(str, "connect to carservice failed :" + e);
        }
    }

    public int getIcmAlarmVolume() {
        CarIcmManager carIcmManager = this.mIcmManager;
        if (carIcmManager == null) {
            Log.e(this.TAG, "not connected to carservice");
            return -1;
        }
        try {
            int ret = carIcmManager.getIcmAlarmVolume();
            return ret;
        } catch (Exception e) {
            String str = this.TAG;
            Log.e(str, "getIcmAlarmVolume from icm failed!" + e);
            return -1;
        }
    }
}