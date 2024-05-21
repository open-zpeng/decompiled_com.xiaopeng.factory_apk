package com.xiaopeng.factory.model.factorytest.hardwaretest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.lib.framework.module.Module;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IHttp;
import com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.IResponse;
import com.xiaopeng.lib.framework.netchannelmodule.NetworkChannelsEntry;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
/* loaded from: classes.dex */
public class MobileNetworkModel {
    private static final int MESSAGE_HTTP_GET = 1;
    private static final String MODULE_4G = "/dev/ttyUSB0";
    public static final String NETWORK_BAIDU = "http://www.baidu.com";
    private static final String TAG = "MobileNetworkModel";
    private int lteSignalStrength;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private WeakReference<Handler> mHandlerWeakReference;
    private TelephonyManager mTelephonyManager;
    private final WifiManager mWifiManager;
    private NetworkLooperThread networkLooperThread;
    private MyPhoneStateListener phoneStateListener;
    private boolean wifiStatus;

    public MobileNetworkModel(Handler handler) {
        this.lteSignalStrength = 0;
        this.mHandlerWeakReference = new WeakReference<>(handler);
        this.mContext = MyApplication.getContext();
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
    }

    public MobileNetworkModel() {
        this.lteSignalStrength = 0;
        this.mContext = MyApplication.getContext();
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        this.mTelephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
    }

    public void onInit() {
        this.wifiStatus = this.mWifiManager.isWifiEnabled();
        this.mWifiManager.setWifiEnabled(false);
        if (this.mHandlerWeakReference == null && this.networkLooperThread == null) {
            this.networkLooperThread = new NetworkLooperThread();
            this.networkLooperThread.start();
        }
    }

    public void onDestory() {
        this.mWifiManager.setWifiEnabled(this.wifiStatus);
        NetworkLooperThread networkLooperThread = this.networkLooperThread;
        if (networkLooperThread != null) {
            networkLooperThread.kill();
        }
    }

    public void setMobileDataEnable(boolean enable) {
        Class mConnectivityManagerClass = this.mConnectivityManager.getClass();
        try {
            Method method = mConnectivityManagerClass.getMethod("setMobileDataEnabled", Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(this.mConnectivityManager, Boolean.valueOf(enable));
            if (!enable) {
                this.lteSignalStrength = 0;
            }
        } catch (Exception e) {
            LogUtils.i(TAG, "e = " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean getMobileDataEnabled() {
        boolean isMobileDataEnabled = false;
        Class mConnectivityManagerClass = this.mConnectivityManager.getClass();
        try {
            Method method = mConnectivityManagerClass.getMethod("getMobileDataEnabled", new Class[0]);
            isMobileDataEnabled = ((Boolean) method.invoke(this.mConnectivityManager, new Object[0])).booleanValue();
        } catch (Exception e) {
            LogUtils.i(TAG, "e = " + e.toString());
            e.printStackTrace();
        }
        LogUtils.i(TAG, "getMobileDataEnabled isMobileDataEnabled = " + isMobileDataEnabled);
        return isMobileDataEnabled;
    }

    public int getNetworkType() {
        int type = this.mTelephonyManager.getNetworkType();
        LogUtils.i(TAG, "getNetworkType type = " + type);
        return type;
    }

    public int getLteSignalStrength() {
        LogUtils.i(TAG, "getLteSignalStrength lteSignalStrength = " + this.lteSignalStrength);
        return this.lteSignalStrength;
    }

    public void getConnectInfo() {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.-$$Lambda$MobileNetworkModel$qQdqk1Q5XhHS5ibpujfXjPv2u5g
            @Override // java.lang.Runnable
            public final void run() {
                MobileNetworkModel.this.lambda$getConnectInfo$0$MobileNetworkModel();
            }
        });
    }

    public /* synthetic */ void lambda$getConnectInfo$0$MobileNetworkModel() {
        LogUtils.i(TAG, "getConnectInfo");
        IHttp http = (IHttp) Module.get(NetworkChannelsEntry.class).get(IHttp.class);
        http.get(NETWORK_BAIDU).execute(new Callback() { // from class: com.xiaopeng.factory.model.factorytest.hardwaretest.MobileNetworkModel.1
            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onSuccess(IResponse iResponse) {
                if (iResponse.code() == 200) {
                    MobileNetworkModel.this.sendMessage(iResponse.body());
                } else {
                    MobileNetworkModel.this.sendMessage("");
                }
            }

            @Override // com.xiaopeng.lib.framework.moduleinterface.netchannelmodule.http.Callback
            public void onFailure(IResponse iResponse) {
                MobileNetworkModel.this.sendMessage("");
            }
        });
    }

    public boolean isModuleNormal() {
        File file = new File(MODULE_4G);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(String string) {
        if (this.mHandlerWeakReference.get() != null) {
            Message message = this.mHandlerWeakReference.get().obtainMessage();
            message.what = 1;
            message.obj = string;
            this.mHandlerWeakReference.get().sendMessage(message);
        }
    }

    /* loaded from: classes.dex */
    private final class NetworkLooperThread extends Thread {
        private NetworkLooperThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Looper.prepare();
            MobileNetworkModel mobileNetworkModel = MobileNetworkModel.this;
            mobileNetworkModel.phoneStateListener = new MyPhoneStateListener();
            MobileNetworkModel.this.mTelephonyManager.listen(MobileNetworkModel.this.phoneStateListener, 256);
            Looper.loop();
        }

        public void kill() {
            MobileNetworkModel.this.networkLooperThread.interrupt();
            if (MobileNetworkModel.this.phoneStateListener != null && MobileNetworkModel.this.mTelephonyManager != null) {
                MobileNetworkModel.this.mTelephonyManager.listen(MobileNetworkModel.this.phoneStateListener, 0);
                MobileNetworkModel.this.phoneStateListener = null;
            }
            MobileNetworkModel.this.networkLooperThread = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MyPhoneStateListener extends PhoneStateListener {
        private MyPhoneStateListener() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            String signalInfo = signalStrength.toString();
            String[] params = signalInfo.split(" ");
            MobileNetworkModel.this.lteSignalStrength = Integer.parseInt(params[9]);
            LogUtils.i(MobileNetworkModel.TAG, "onSignalStrengthsChanged lteSignalStrength = " + MobileNetworkModel.this.lteSignalStrength);
        }
    }
}
