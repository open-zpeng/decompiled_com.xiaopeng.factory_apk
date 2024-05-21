package com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.xiaopeng.commonfunc.system.runnable.Sleep;
import com.xiaopeng.commonfunc.utils.AtUtil;
import com.xiaopeng.commonfunc.utils.DataHelp;
import com.xiaopeng.commonfunc.utils.DmUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.atcommand.ResponseWriter;
import com.xiaopeng.factory.dmcommand.DmResponseWriter;
import com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.IWlanModel;
import com.xiaopeng.factory.model.factorytest.hardwaretest.wlan.WlanModel;
import com.xiaopeng.factory.system.socket.FactoryClientConnector;
import com.xiaopeng.lib.utils.LogUtils;
/* loaded from: classes2.dex */
public class WlanPresenter implements IWlanPresenter {
    public static final int CLOSING = 2;
    private static final String CMD_24G_FORMAT = "myftm -J -I 1 -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 ";
    private static final String CMD_5G_FORMAT = "myftm -J -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 ";
    private static final String CMD_FORMAT_NF3205 = "myftm -a%d -M%d -r%d -f%d ";
    private static final String INSMOD_NF3219 = "insmod vendor/lib/modules/qca_cld3_qca6390.ko con_mode=5";
    private static final String NF3205_MODEL = "qca6174";
    private static final String NF3219_MODEL = "qca6390";
    public static final int NON_SIGNAL_24B = 6;
    public static final int NON_SIGNAL_24B_RX = 10;
    public static final int NON_SIGNAL_24G = 7;
    public static final int NON_SIGNAL_24G_RX = 11;
    public static final int NON_SIGNAL_24N = 8;
    public static final int NON_SIGNAL_24N_RX = 12;
    public static final int NON_SIGNAL_INSMOD = 4;
    public static final int NON_SIGNAL_RMMOD = 3;
    public static final int NON_SIGNAL_RMMOD_INSMOD = 14;
    public static final int NON_SIGNAL_STOP = 5;
    public static final int NON_SIGNAL_STOP_RX = 9;
    public static final int NON_SIGNAL_WLAN_CNTS = 13;
    public static final int NON_SIGNAL_WLAN_RX_NF3219 = 17;
    public static final int NON_SIGNAL_WLAN_START_RF_CMD = 15;
    public static final int NON_SIGNAL_WLAN_TX_NF3219 = 16;
    public static final int OPENING = 1;
    public static final int PENDING = 0;
    private static final String PROP_WIFI_CHIP = "ro.vendor.wlan.chip";
    private static final String RMMOD_NF3219 = "rmmod wlan";
    private static final String RX_CMD_24G_FORMAT = "myftm -J -I 1 -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 -x %d";
    private static final String RX_CMD_5G_FORMAT = "myftm -J -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 -x %d";
    private static final String RX_CMD_FORMAT_NF3205 = "myftm -a%d -M%d -r%d -f%d -x%d";
    private static final String START_NF3205 = "echo 5 > /sys/module/wlan/parameters/con_mode";
    private static final String TAG = "WlanPresenter";
    private static final String TX_CMD_24G_FORMAT = "myftm -J -I 1 -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 -t %d";
    private static final String TX_CMD_5G_FORMAT = "myftm -J -M %d -r %d -f %d -c 0 -p %d -a 2 -X 00:03:7F:44:55:98 -Y 00:03:7F:44:55:97 -N 00:03:7F:44:55:99 -t %d";
    private static final String TX_CMD_FORMAT_NF3205 = "myftm -a%d -M%d -r%d -f%d -p%d -c0 -t%d";
    public static final byte WL_CNTS = 6;
    public static final byte WL_EXECUTE = 1;
    public static final byte WL_INSMOD = 3;
    public static final byte WL_RMMOD = 2;
    public static final byte WL_RMMOD_INSMOD = 32;
    public static final byte WL_RX_NF3219 = 35;
    public static final byte WL_START_RF = 33;
    public static final byte WL_STOP = 4;
    public static final byte WL_STOP_RX = 5;
    public static final byte WL_TX_NF3219 = 34;
    private final String WIFI_BT_MODEL;
    private final WifiManager.ActionListener mConnectListener;
    private DmResponseWriter mDmResponseWriter;
    private final FactoryClientConnector.ReceiverListener mFacSockReceiver;
    private final WifiManager.ActionListener mForgetListener;
    private final BroadcastReceiver mReceiver;
    private ResponseWriter mResponseWriter;
    private int mWifiTestStep;
    private final IWlanModel mWlanModel;

    public WlanPresenter() {
        this.WIFI_BT_MODEL = SystemProperties.get(PROP_WIFI_CHIP, "unknown");
        this.mWifiTestStep = 0;
        this.mResponseWriter = null;
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                int state = WlanPresenter.this.checkState();
                LogUtils.i(WlanPresenter.TAG, "onReceive " + action + " state = " + state);
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                    if (WlanPresenter.this.mWifiTestStep != 1 || state != 3) {
                        if (WlanPresenter.this.mWifiTestStep == 2 && state == 1) {
                            if (WlanPresenter.this.mResponseWriter != null) {
                                WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                            }
                            if (WlanPresenter.this.mDmResponseWriter != null) {
                                WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_01));
                            }
                            WlanPresenter.this.unregisterReceiver();
                            WlanPresenter.this.mWifiTestStep = 0;
                            return;
                        }
                        return;
                    }
                    if (WlanPresenter.this.mResponseWriter != null) {
                        WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                    }
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_00));
                    }
                    WlanPresenter.this.unregisterReceiver();
                    WlanPresenter.this.mWifiTestStep = 0;
                }
            }
        };
        this.mFacSockReceiver = new FactoryClientConnector.ReceiverListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.2
            @Override // com.xiaopeng.factory.system.socket.FactoryClientConnector.ReceiverListener
            public void onReceiveData(byte[] data) {
                byte[] responseNG;
                LogUtils.i(WlanPresenter.TAG, "FacSock Receive data:" + DataHelp.byteArrayToHexStr(data, " "));
                byte[] cmd = null;
                int value = 0;
                byte b = data[0];
                switch (b) {
                    case 1:
                        switch (WlanPresenter.this.mWifiTestStep) {
                            case 6:
                                cmd = DmUtil.ARGU_04_02;
                                break;
                            case 7:
                                cmd = DmUtil.ARGU_04_03;
                                break;
                            case 8:
                                cmd = DmUtil.ARGU_04_04;
                                break;
                            case 9:
                            default:
                                LogUtils.e(WlanPresenter.TAG, "wrong test step mWifiTestStep : " + WlanPresenter.this.mWifiTestStep);
                                break;
                            case 10:
                                cmd = DmUtil.ARGU_04_06;
                                break;
                            case 11:
                                cmd = DmUtil.ARGU_04_07;
                                break;
                            case 12:
                                cmd = DmUtil.ARGU_04_08;
                                break;
                        }
                    case 2:
                        if (WlanPresenter.this.mWifiTestStep == 3) {
                            cmd = DmUtil.ARGU_04_00;
                            break;
                        }
                        break;
                    case 3:
                        if (WlanPresenter.this.mWifiTestStep == 4) {
                            cmd = DmUtil.ARGU_04_01;
                            break;
                        }
                        break;
                    case 4:
                        if (WlanPresenter.this.mWifiTestStep == 5) {
                            cmd = DmUtil.ARGU_04_05;
                            break;
                        }
                        break;
                    case 5:
                        if (WlanPresenter.this.mWifiTestStep == 9) {
                            cmd = DmUtil.ARGU_04_0A;
                            break;
                        }
                        break;
                    case 6:
                        if (WlanPresenter.this.mWifiTestStep == 13) {
                            cmd = DmUtil.ARGU_04_09;
                            try {
                                value = Integer.valueOf(DataHelp.byteArrayToStr(DataHelp.byteSub(data, 3, DataHelp.byteToInt(data[1]) - 1))).intValue();
                                break;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        break;
                    default:
                        switch (b) {
                            case 32:
                                if (WlanPresenter.this.mWifiTestStep == 14) {
                                    cmd = new byte[]{20, 0};
                                    break;
                                }
                                break;
                            case 33:
                                if (WlanPresenter.this.mWifiTestStep == 15) {
                                    cmd = new byte[]{20, 1};
                                    break;
                                }
                                break;
                            case 34:
                                if (WlanPresenter.this.mWifiTestStep == 16) {
                                    cmd = new byte[]{20, 16};
                                    break;
                                }
                                break;
                            case 35:
                                if (WlanPresenter.this.mWifiTestStep == 17) {
                                    cmd = new byte[]{20, 17};
                                    break;
                                }
                                break;
                        }
                }
                if (cmd != null) {
                    WlanPresenter.this.mWifiTestStep = 0;
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        DmResponseWriter dmResponseWriter = WlanPresenter.this.mDmResponseWriter;
                        if (data[2] == 0) {
                            responseNG = data[0] != 6 ? DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, cmd) : DmUtil.responseWithValue(DmUtil.WifiTest.CMD_NAME, cmd, DataHelp.intToBytes2(value));
                        } else {
                            responseNG = DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, cmd);
                        }
                        dmResponseWriter.write(responseNG);
                    }
                }
            }
        };
        this.mConnectListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.3
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onSuccess");
                Sleep.sleep(3000L);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }
        };
        this.mForgetListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.4
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onSuccess");
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "0"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_01));
                }
            }
        };
        this.mWlanModel = new WlanModel();
    }

    public WlanPresenter(ResponseWriter responseWriter) {
        this.WIFI_BT_MODEL = SystemProperties.get(PROP_WIFI_CHIP, "unknown");
        this.mWifiTestStep = 0;
        this.mResponseWriter = null;
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                int state = WlanPresenter.this.checkState();
                LogUtils.i(WlanPresenter.TAG, "onReceive " + action + " state = " + state);
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                    if (WlanPresenter.this.mWifiTestStep != 1 || state != 3) {
                        if (WlanPresenter.this.mWifiTestStep == 2 && state == 1) {
                            if (WlanPresenter.this.mResponseWriter != null) {
                                WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                            }
                            if (WlanPresenter.this.mDmResponseWriter != null) {
                                WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_01));
                            }
                            WlanPresenter.this.unregisterReceiver();
                            WlanPresenter.this.mWifiTestStep = 0;
                            return;
                        }
                        return;
                    }
                    if (WlanPresenter.this.mResponseWriter != null) {
                        WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                    }
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_00));
                    }
                    WlanPresenter.this.unregisterReceiver();
                    WlanPresenter.this.mWifiTestStep = 0;
                }
            }
        };
        this.mFacSockReceiver = new FactoryClientConnector.ReceiverListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.2
            @Override // com.xiaopeng.factory.system.socket.FactoryClientConnector.ReceiverListener
            public void onReceiveData(byte[] data) {
                byte[] responseNG;
                LogUtils.i(WlanPresenter.TAG, "FacSock Receive data:" + DataHelp.byteArrayToHexStr(data, " "));
                byte[] cmd = null;
                int value = 0;
                byte b = data[0];
                switch (b) {
                    case 1:
                        switch (WlanPresenter.this.mWifiTestStep) {
                            case 6:
                                cmd = DmUtil.ARGU_04_02;
                                break;
                            case 7:
                                cmd = DmUtil.ARGU_04_03;
                                break;
                            case 8:
                                cmd = DmUtil.ARGU_04_04;
                                break;
                            case 9:
                            default:
                                LogUtils.e(WlanPresenter.TAG, "wrong test step mWifiTestStep : " + WlanPresenter.this.mWifiTestStep);
                                break;
                            case 10:
                                cmd = DmUtil.ARGU_04_06;
                                break;
                            case 11:
                                cmd = DmUtil.ARGU_04_07;
                                break;
                            case 12:
                                cmd = DmUtil.ARGU_04_08;
                                break;
                        }
                    case 2:
                        if (WlanPresenter.this.mWifiTestStep == 3) {
                            cmd = DmUtil.ARGU_04_00;
                            break;
                        }
                        break;
                    case 3:
                        if (WlanPresenter.this.mWifiTestStep == 4) {
                            cmd = DmUtil.ARGU_04_01;
                            break;
                        }
                        break;
                    case 4:
                        if (WlanPresenter.this.mWifiTestStep == 5) {
                            cmd = DmUtil.ARGU_04_05;
                            break;
                        }
                        break;
                    case 5:
                        if (WlanPresenter.this.mWifiTestStep == 9) {
                            cmd = DmUtil.ARGU_04_0A;
                            break;
                        }
                        break;
                    case 6:
                        if (WlanPresenter.this.mWifiTestStep == 13) {
                            cmd = DmUtil.ARGU_04_09;
                            try {
                                value = Integer.valueOf(DataHelp.byteArrayToStr(DataHelp.byteSub(data, 3, DataHelp.byteToInt(data[1]) - 1))).intValue();
                                break;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        break;
                    default:
                        switch (b) {
                            case 32:
                                if (WlanPresenter.this.mWifiTestStep == 14) {
                                    cmd = new byte[]{20, 0};
                                    break;
                                }
                                break;
                            case 33:
                                if (WlanPresenter.this.mWifiTestStep == 15) {
                                    cmd = new byte[]{20, 1};
                                    break;
                                }
                                break;
                            case 34:
                                if (WlanPresenter.this.mWifiTestStep == 16) {
                                    cmd = new byte[]{20, 16};
                                    break;
                                }
                                break;
                            case 35:
                                if (WlanPresenter.this.mWifiTestStep == 17) {
                                    cmd = new byte[]{20, 17};
                                    break;
                                }
                                break;
                        }
                }
                if (cmd != null) {
                    WlanPresenter.this.mWifiTestStep = 0;
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        DmResponseWriter dmResponseWriter = WlanPresenter.this.mDmResponseWriter;
                        if (data[2] == 0) {
                            responseNG = data[0] != 6 ? DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, cmd) : DmUtil.responseWithValue(DmUtil.WifiTest.CMD_NAME, cmd, DataHelp.intToBytes2(value));
                        } else {
                            responseNG = DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, cmd);
                        }
                        dmResponseWriter.write(responseNG);
                    }
                }
            }
        };
        this.mConnectListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.3
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onSuccess");
                Sleep.sleep(3000L);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }
        };
        this.mForgetListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.4
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onSuccess");
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "0"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_01));
                }
            }
        };
        this.mWlanModel = new WlanModel(this.mConnectListener, this.mForgetListener);
        this.mResponseWriter = responseWriter;
    }

    public WlanPresenter(DmResponseWriter dmResponseWriter) {
        this.WIFI_BT_MODEL = SystemProperties.get(PROP_WIFI_CHIP, "unknown");
        this.mWifiTestStep = 0;
        this.mResponseWriter = null;
        this.mReceiver = new BroadcastReceiver() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                int state = WlanPresenter.this.checkState();
                LogUtils.i(WlanPresenter.TAG, "onReceive " + action + " state = " + state);
                if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                    if (WlanPresenter.this.mWifiTestStep != 1 || state != 3) {
                        if (WlanPresenter.this.mWifiTestStep == 2 && state == 1) {
                            if (WlanPresenter.this.mResponseWriter != null) {
                                WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                            }
                            if (WlanPresenter.this.mDmResponseWriter != null) {
                                WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_01));
                            }
                            WlanPresenter.this.unregisterReceiver();
                            WlanPresenter.this.mWifiTestStep = 0;
                            return;
                        }
                        return;
                    }
                    if (WlanPresenter.this.mResponseWriter != null) {
                        WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "0"));
                    }
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_00_00));
                    }
                    WlanPresenter.this.unregisterReceiver();
                    WlanPresenter.this.mWifiTestStep = 0;
                }
            }
        };
        this.mFacSockReceiver = new FactoryClientConnector.ReceiverListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.2
            @Override // com.xiaopeng.factory.system.socket.FactoryClientConnector.ReceiverListener
            public void onReceiveData(byte[] data) {
                byte[] responseNG;
                LogUtils.i(WlanPresenter.TAG, "FacSock Receive data:" + DataHelp.byteArrayToHexStr(data, " "));
                byte[] cmd = null;
                int value = 0;
                byte b = data[0];
                switch (b) {
                    case 1:
                        switch (WlanPresenter.this.mWifiTestStep) {
                            case 6:
                                cmd = DmUtil.ARGU_04_02;
                                break;
                            case 7:
                                cmd = DmUtil.ARGU_04_03;
                                break;
                            case 8:
                                cmd = DmUtil.ARGU_04_04;
                                break;
                            case 9:
                            default:
                                LogUtils.e(WlanPresenter.TAG, "wrong test step mWifiTestStep : " + WlanPresenter.this.mWifiTestStep);
                                break;
                            case 10:
                                cmd = DmUtil.ARGU_04_06;
                                break;
                            case 11:
                                cmd = DmUtil.ARGU_04_07;
                                break;
                            case 12:
                                cmd = DmUtil.ARGU_04_08;
                                break;
                        }
                    case 2:
                        if (WlanPresenter.this.mWifiTestStep == 3) {
                            cmd = DmUtil.ARGU_04_00;
                            break;
                        }
                        break;
                    case 3:
                        if (WlanPresenter.this.mWifiTestStep == 4) {
                            cmd = DmUtil.ARGU_04_01;
                            break;
                        }
                        break;
                    case 4:
                        if (WlanPresenter.this.mWifiTestStep == 5) {
                            cmd = DmUtil.ARGU_04_05;
                            break;
                        }
                        break;
                    case 5:
                        if (WlanPresenter.this.mWifiTestStep == 9) {
                            cmd = DmUtil.ARGU_04_0A;
                            break;
                        }
                        break;
                    case 6:
                        if (WlanPresenter.this.mWifiTestStep == 13) {
                            cmd = DmUtil.ARGU_04_09;
                            try {
                                value = Integer.valueOf(DataHelp.byteArrayToStr(DataHelp.byteSub(data, 3, DataHelp.byteToInt(data[1]) - 1))).intValue();
                                break;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        break;
                    default:
                        switch (b) {
                            case 32:
                                if (WlanPresenter.this.mWifiTestStep == 14) {
                                    cmd = new byte[]{20, 0};
                                    break;
                                }
                                break;
                            case 33:
                                if (WlanPresenter.this.mWifiTestStep == 15) {
                                    cmd = new byte[]{20, 1};
                                    break;
                                }
                                break;
                            case 34:
                                if (WlanPresenter.this.mWifiTestStep == 16) {
                                    cmd = new byte[]{20, 16};
                                    break;
                                }
                                break;
                            case 35:
                                if (WlanPresenter.this.mWifiTestStep == 17) {
                                    cmd = new byte[]{20, 17};
                                    break;
                                }
                                break;
                        }
                }
                if (cmd != null) {
                    WlanPresenter.this.mWifiTestStep = 0;
                    if (WlanPresenter.this.mDmResponseWriter != null) {
                        DmResponseWriter dmResponseWriter2 = WlanPresenter.this.mDmResponseWriter;
                        if (data[2] == 0) {
                            responseNG = data[0] != 6 ? DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, cmd) : DmUtil.responseWithValue(DmUtil.WifiTest.CMD_NAME, cmd, DataHelp.intToBytes2(value));
                        } else {
                            responseNG = DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, cmd);
                        }
                        dmResponseWriter2.write(responseNG);
                    }
                }
            }
        };
        this.mConnectListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.3
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onSuccess");
                Sleep.sleep(3000L);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseOK(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseOK(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mConnectListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "2"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_00));
                }
            }
        };
        this.mForgetListener = new WifiManager.ActionListener() { // from class: com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter.4
            public void onSuccess() {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onSuccess");
            }

            public void onFailure(int reason) {
                LogUtils.i(WlanPresenter.TAG, "mForgetListener onFailure reason = " + reason);
                if (WlanPresenter.this.mResponseWriter != null) {
                    WlanPresenter.this.mResponseWriter.write(AtUtil.responseNG(AtUtil.WifiTest.CMD_NAME, "0"));
                }
                if (WlanPresenter.this.mDmResponseWriter != null) {
                    WlanPresenter.this.mDmResponseWriter.write(DmUtil.responseNG(DmUtil.WifiTest.CMD_NAME, DmUtil.ARGU_02_01));
                }
            }
        };
        this.mWlanModel = new WlanModel(this.mConnectListener, this.mForgetListener);
        this.mDmResponseWriter = dmResponseWriter;
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void onInit() {
        this.mWlanModel.onInit();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        MyApplication.getContext().registerReceiver(this.mReceiver, filter);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void unregisterReceiver() {
        MyApplication.getContext().unregisterReceiver(this.mReceiver);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void openWifi() {
        this.mWifiTestStep = 1;
        this.mWlanModel.openWifi();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void closeWifi() {
        this.mWifiTestStep = 2;
        this.mWlanModel.closeWifi();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public int checkState() {
        return this.mWlanModel.checkState();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public String getMacAddress() {
        return this.mWlanModel.getMacAddress();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public String getSSID() {
        return this.mWlanModel.getSSID();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public int getIPAddress() {
        return this.mWlanModel.getIPAddress();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void addNetwork(WifiConfiguration wcg) {
        this.mWlanModel.addNetwork(wcg);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public WifiConfiguration createWifiInfo(String SSID) {
        return this.mWlanModel.createWifiInfo(SSID);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public WifiConfiguration createWifiInfo(String ssid, String pass, int type) {
        return this.mWlanModel.createWifiInfo(ssid, pass, type);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void forgetWIfi(String SSID) {
        this.mWlanModel.forgetWIfi(SSID);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void forgetAllWifi() {
        this.mWlanModel.forgetAllWifi();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public String getWlanLocalAddr() {
        return this.mWlanModel.getWlanLocalAddr();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public WifiInfo getWifiInfo() {
        return this.mWlanModel.getWifiInfo();
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void rmmod_driver() {
        int4Dm();
        this.mWifiTestStep = 3;
        FactoryClientConnector.getInstance().send(new byte[]{2, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void insmode_test_driver() {
        int4Dm();
        this.mWifiTestStep = 4;
        FactoryClientConnector.getInstance().send(new byte[]{3, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24bTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 6;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 5}, DataHelp.byteSub(arg, 1, 5)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24gTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 7;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 5}, DataHelp.byteSub(arg, 1, 5)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24nTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 8;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 6}, DataHelp.byteSub(arg, 1, 6)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void stopNsTest() {
        int4Dm();
        this.mWifiTestStep = 5;
        FactoryClientConnector.getInstance().send(new byte[]{4, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24bRxTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 10;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 3}, DataHelp.byteSub(arg, 1, 3)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24gRxTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 11;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 3}, DataHelp.byteSub(arg, 1, 3)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void run24nRxTest(byte[] arg) {
        int4Dm();
        this.mWifiTestStep = 12;
        FactoryClientConnector.getInstance().send(DataHelp.byteMerger(new byte[]{1, 4}, DataHelp.byteSub(arg, 1, 4)));
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void stopNsRxTest() {
        int4Dm();
        this.mWifiTestStep = 9;
        FactoryClientConnector.getInstance().send(new byte[]{5, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void readWlanCounts() {
        int4Dm();
        this.mWifiTestStep = 13;
        FactoryClientConnector.getInstance().send(new byte[]{6, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void wlanRmmodAndInsmod(byte[] arg) {
        char c;
        int4Dm();
        this.mWifiTestStep = 14;
        byte[] send = null;
        String str = this.WIFI_BT_MODEL;
        int hashCode = str.hashCode();
        if (hashCode != 134225895) {
            if (hashCode == 134227875 && str.equals(NF3219_MODEL)) {
                c = 0;
            }
            c = 65535;
        } else {
            if (str.equals(NF3205_MODEL)) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            send = DataHelp.byteMerger(new byte[]{WL_RMMOD_INSMOD}, new byte[]{2}, new byte[]{(byte) RMMOD_NF3219.length()}, DataHelp.strToByteArray(RMMOD_NF3219), new byte[]{(byte) INSMOD_NF3219.length()}, DataHelp.strToByteArray(INSMOD_NF3219));
        } else if (c == 1) {
            send = DataHelp.byteMerger(new byte[]{WL_RMMOD_INSMOD}, new byte[]{1}, new byte[]{(byte) START_NF3205.length()}, DataHelp.strToByteArray(START_NF3205));
        }
        if (send != null) {
            LogUtils.d(TAG, "wlanRmmodAndInsmod, send size: " + send.length);
            FactoryClientConnector.getInstance().send(send);
            return;
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseNA(DmUtil.WifiTest.CMD_NAME, arg));
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void wlanStartRfCmd() {
        int4Dm();
        this.mWifiTestStep = 15;
        FactoryClientConnector.getInstance().send(new byte[]{WL_START_RF, 0});
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void runTxTest(byte[] arg) {
        char c;
        int4Dm();
        this.mWifiTestStep = 16;
        String cmd = null;
        String str = this.WIFI_BT_MODEL;
        int hashCode = str.hashCode();
        if (hashCode != 134225895) {
            if (hashCode == 134227875 && str.equals(NF3219_MODEL)) {
                c = 0;
            }
            c = 65535;
        } else {
            if (str.equals(NF3205_MODEL)) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            int antenna = arg[2];
            int wlanMode = antenna & 255;
            int dataRate = arg[3] & 255;
            int channel = ((arg[4] & 255) * 256) + (arg[5] & 255);
            int power = arg[6] & 255;
            int onoff = arg[7] & 255;
            cmd = channel > 4000 ? String.format(TX_CMD_5G_FORMAT, Integer.valueOf(wlanMode), Integer.valueOf(dataRate), Integer.valueOf(channel), Integer.valueOf(power), Integer.valueOf(onoff)) : String.format(TX_CMD_24G_FORMAT, Integer.valueOf(wlanMode), Integer.valueOf(dataRate), Integer.valueOf(channel), Integer.valueOf(power), Integer.valueOf(onoff));
        } else if (c == 1) {
            int antenna2 = arg[2] & 255;
            cmd = String.format(TX_CMD_FORMAT_NF3205, Integer.valueOf(antenna2), Integer.valueOf(arg[3] & 255), Integer.valueOf(arg[4] & 255), Integer.valueOf(((arg[5] & 255) * 256) + (arg[6] & 255)), Integer.valueOf(arg[7] & 255), Integer.valueOf(arg[8] & 255));
        }
        LogUtils.d(TAG, "runTxTest cmd: " + cmd);
        if (!TextUtils.isEmpty(cmd)) {
            byte[] send = DataHelp.byteMerger(new byte[]{WL_TX_NF3219}, DataHelp.strToByteArray(cmd));
            FactoryClientConnector.getInstance().send(send);
            return;
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseNA(DmUtil.WifiTest.CMD_NAME, arg));
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void runRxTest(byte[] arg) {
        char c;
        int4Dm();
        this.mWifiTestStep = 17;
        String cmd = null;
        String str = this.WIFI_BT_MODEL;
        int hashCode = str.hashCode();
        if (hashCode != 134225895) {
            if (hashCode == 134227875 && str.equals(NF3219_MODEL)) {
                c = 0;
            }
            c = 65535;
        } else {
            if (str.equals(NF3205_MODEL)) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            int antenna = arg[2];
            int wlanMode = antenna & 255;
            int dataRate = arg[3] & 255;
            int channel = ((arg[4] & 255) * 256) + (arg[5] & 255);
            int power = arg[6] & 255;
            int onoff = arg[7] & 255;
            cmd = channel > 4000 ? String.format(RX_CMD_5G_FORMAT, Integer.valueOf(wlanMode), Integer.valueOf(dataRate), Integer.valueOf(channel), Integer.valueOf(power), Integer.valueOf(onoff)) : String.format(RX_CMD_24G_FORMAT, Integer.valueOf(wlanMode), Integer.valueOf(dataRate), Integer.valueOf(channel), Integer.valueOf(power), Integer.valueOf(onoff));
        } else if (c == 1) {
            int antenna2 = arg[2] & 255;
            cmd = String.format(RX_CMD_FORMAT_NF3205, Integer.valueOf(antenna2), Integer.valueOf(arg[3] & 255), Integer.valueOf(arg[4] & 255), Integer.valueOf(((arg[5] & 255) * 256) + (arg[6] & 255)), Integer.valueOf(arg[7] & 255));
        }
        LogUtils.d(TAG, "runRxTest cmd: " + cmd);
        if (!TextUtils.isEmpty(cmd)) {
            byte[] send = DataHelp.byteMerger(new byte[]{WL_RX_NF3219}, DataHelp.strToByteArray(cmd));
            FactoryClientConnector.getInstance().send(send);
            return;
        }
        DmResponseWriter dmResponseWriter = this.mDmResponseWriter;
        if (dmResponseWriter != null) {
            dmResponseWriter.write(DmUtil.responseNA(DmUtil.WifiTest.CMD_NAME, arg));
        }
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void int4Dm() {
        FactoryClientConnector.getInstance().setReceiverListener(this.mFacSockReceiver);
    }

    @Override // com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.IWlanPresenter
    public void deinit4Dm() {
        FactoryClientConnector.getInstance().setReceiverListener(null);
    }
}
