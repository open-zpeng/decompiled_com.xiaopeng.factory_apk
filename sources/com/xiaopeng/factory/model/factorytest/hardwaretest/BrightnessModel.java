package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.content.Context;
import android.os.IPowerManager;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.provider.Settings;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes.dex */
public class BrightnessModel {
    private static final String TAG = "BrightnessModel";
    private final Context mContext;
    private final IPowerManager mPower = IPowerManager.Stub.asInterface(ServiceManager.getService("power"));
    private final PowerManager mPowerManager;

    public BrightnessModel(Context context) {
        this.mContext = context;
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
    }

    public int getMaxBrightness() {
        int max = this.mPowerManager.getMaximumScreenBrightnessSetting();
        LogUtils.i(TAG, "getMaxBrightness = " + max);
        return max;
    }

    public int getMinBrightness() {
        int min = this.mPowerManager.getMinimumScreenBrightnessSetting();
        LogUtils.i(TAG, "getMinBrightness = " + min);
        return min;
    }

    public int getCurrentBrightness() {
        int current = -1;
        try {
            current = Settings.System.getInt(this.mContext.getContentResolver(), "screen_brightness");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "getCurrentBrightness = " + current);
        return current;
    }

    public void setBrightness(int brightness) {
        LogUtils.i(TAG, "setBrightness : " + brightness);
        Settings.System.putInt(this.mContext.getContentResolver(), "screen_brightness", brightness);
    }
}
