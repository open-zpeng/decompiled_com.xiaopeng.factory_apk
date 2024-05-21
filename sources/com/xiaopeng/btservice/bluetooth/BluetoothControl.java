package com.xiaopeng.btservice.bluetooth;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsBluetoothControlCallback;
import com.xiaopeng.btservice.base.AbsControl;
/* loaded from: classes.dex */
public class BluetoothControl extends AbsControl {
    private static final String TAG = "BluetoothControl";
    private AbsBluetoothControlCallback mCallback;
    private UiCallbackBluetooth mCallbackBluetooth = new UiCallbackBluetooth.Stub() { // from class: com.xiaopeng.btservice.bluetooth.BluetoothControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onBluetoothServiceReady() {
            Log.d(BluetoothControl.TAG, "onBluetoothServiceReady");
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onBluetoothServiceReady();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onAdapterStateChanged(int prevState, int newState) {
            Log.d(BluetoothControl.TAG, "onAdapterStateChanged newState:" + newState);
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onAdapterStateChanged(prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onAdapterDiscoverableModeChanged(int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onAdapterDiscoverableModeChanged(prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onAdapterDiscoveryStarted() {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onAdapterDiscoveryStarted();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onAdapterDiscoveryFinished() {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onAdapterDiscoveryFinished();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.retPairedDevices(elements, address, name, supportProfile, category);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onDeviceFound(String address, String name, byte category) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onDeviceFound(address, name, category);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onDeviceBondStateChanged(address, name, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onDeviceUuidsUpdated(String address, String name, int supportProfile) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onDeviceUuidsUpdated(address, name, supportProfile);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onLocalAdapterNameChanged(String name) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onLocalAdapterNameChanged(name);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onDeviceOutOfRange(String address) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onDeviceOutOfRange(address);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onDeviceAclDisconnected(String address) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onDeviceAclDisconnected(address);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onBtRoleModeChanged(int mode) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onBtRoleModeChanged(mode);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onBtAutoConnectStateChanged(String address, int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onBtAutoConnectStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onHfpStateChanged(String address, int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onHfpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onA2dpStateChanged(String address, int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onA2dpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackBluetooth
        public void onAvrcpStateChanged(String address, int prevState, int newState) {
            if (BluetoothControl.this.mCallback != null) {
                BluetoothControl.this.mCallback.onAvrcpStateChanged(address, prevState, newState);
            }
        }
    };

    public BluetoothControl(Context context, AbsBluetoothControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            Log.d(TAG, "bluetooth registerCallback " + this.nForeService);
            btService.registerBtCallback(this.mCallbackBluetooth);
            Log.d(TAG, "bluetooth nf btService registerCallback ");
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            Log.d(TAG, "release");
            if (this.nForeService != null) {
                this.nForeService.unregisterBtCallback(this.mCallbackBluetooth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UiCommand getBtService() {
        return this.nForeService;
    }

    public int getBtState() {
        printLog(TAG, "getBtState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int state = this.nForeService.getBtState();
            return state;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean reqBtPairedDevices() {
        printLog(TAG, "reqBtPairedDevices");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqBtPairedDevices();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean startBtDiscovery() {
        if (this.nForeService == null) {
            Log.d(TAG, "nForeService == null");
        }
        boolean success = false;
        if (this.nForeService == null) {
            return false;
        }
        try {
        } catch (Exception e) {
            printError(TAG, e);
        }
        if (this.nForeService.isBtDiscovering()) {
            Log.d(TAG, "startBtDiscovery isBtDiscovering");
            return true;
        }
        success = this.nForeService.startBtDiscovery();
        StringBuilder sb = new StringBuilder();
        sb.append("app startBtDiscovery pkg:");
        sb.append(this.mContext != null ? this.mContext.getPackageName() : "");
        sb.append(" success:");
        sb.append(success);
        Log.d(TAG, sb.toString());
        return success;
    }

    public boolean stopBtDiscovery() {
        printLog(TAG, "stopBtDiscovery");
        if (this.nForeService == null) {
            return false;
        }
        try {
            if (!this.nForeService.isBtDiscovering()) {
                return false;
            }
            boolean success = this.nForeService.cancelBtDiscovery();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean cancelBtDiscovery() {
        printLog(TAG, "cancelBtDiscovery");
        boolean success = false;
        if (this.nForeService == null) {
            return false;
        }
        try {
            if (this.nForeService.isBtDiscovering()) {
                success = this.nForeService.cancelBtDiscovery();
                StringBuilder sb = new StringBuilder();
                sb.append("app cancelBtDiscovery pkg:");
                sb.append(this.mContext != null ? this.mContext.getPackageName() : "");
                sb.append(" success:");
                sb.append(success);
                Log.d(TAG, sb.toString());
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return success;
    }

    public boolean reqBtPair(String address) {
        printLog(TAG, "reqBtPair");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqBtPair(address);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int reqBtConnectHfpA2dp(String address) {
        printLog(TAG, "reqBtConnectHfpA2dp");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int state = this.nForeService.reqBtConnectHfpA2dp(address);
            return state;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public int getHfpConnectionState() {
        printLog(TAG, "getHfpConnectionState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int state = this.nForeService.getHfpConnectionState();
            return state;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public int getA2dpConnectionState() {
        printLog(TAG, "getA2dpConnectionState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int state = this.nForeService.getA2dpConnectionState();
            return state;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean setBtEnabled(boolean enable) {
        printLog(TAG, "setBtEnabled");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.setBtEnable(enable);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isBtEnabled() {
        printLog(TAG, "isBtEnabled");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.isBtEnabled();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isBtDiscovering() {
        printLog(TAG, "isBtDiscovering");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.isBtDiscovering();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean setBtLocalName(String name) {
        printLog(TAG, "setBtLocalName");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.setBtLocalName(name);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public String getBtLocalName() {
        printLog(TAG, "getBtLocalName");
        if (this.nForeService == null) {
            return "";
        }
        try {
            String name = this.nForeService.getBtLocalName();
            return name;
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public String getBtRemoteDeviceName() {
        printLog(TAG, "getBtRemoteDeviceName");
        if (this.nForeService == null) {
            return "";
        }
        try {
            String address = getHfpConnectedAddress();
            if (TextUtils.isEmpty(address)) {
                return "";
            }
            String remoteDeviceName = this.nForeService.getBtRemoteDeviceName(address);
            return remoteDeviceName;
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public boolean reqBtUnpair(String address) {
        printLog(TAG, "reqBtUnpair");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqBtUnpair(address);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int reqBtDisconnectAll() {
        printLog(TAG, "reqBtDisconnectAll");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int profile = this.nForeService.reqBtDisconnectAll();
            return profile;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean isBtDiscoverable() {
        printLog(TAG, "isBtDiscoverable");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.isBtDiscoverable();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean setBtDiscoverableTimeout(int timeout) {
        printLog(TAG, "setBtDiscoverableTimeout");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.setBtDiscoverableTimeout(timeout);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int getBtRemoteUuids(String address) {
        printLog(TAG, "getBtRemoteUuids");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int profile = this.nForeService.getBtRemoteUuids(address);
            return profile;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean isBluetoothServiceReady() {
        printLog(TAG, "isBluetoothServiceReady");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.isBluetoothServiceReady();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isHfpConnected() {
        printLog(TAG, "isHfpConnected");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isHfpConnected();
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int getHFPConnectState(String address) {
        printLog(TAG, "getHFPConnectState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            boolean isConnected = this.nForeService.isHfpConnected();
            if (isConnected) {
                String address1 = this.nForeService.getHfpConnectedAddress();
                if (address.equals(address1)) {
                    return this.nForeService.getHfpConnectionState();
                }
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return -1;
    }

    public String getHfpConnectedAddress() {
        return super.getHfpConnectedAddress(TAG);
    }

    public String getA2dpConnectedAddress() {
        printLog(TAG, "getA2dpConnectedAddress");
        if (this.nForeService == null) {
            return "";
        }
        try {
            String address1 = this.nForeService.getA2dpConnectedAddress();
            return address1;
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public int getA2DPConnectState(String address) {
        printLog(TAG, "getA2DPConnectState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            boolean isConnected = this.nForeService.isA2dpConnected();
            if (isConnected) {
                String address1 = this.nForeService.getA2dpConnectedAddress();
                if (address.equals(address1)) {
                    return this.nForeService.getA2dpConnectionState();
                }
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return -1;
    }

    public boolean isA2dpConnected() {
        printLog(TAG, "isA2dpConnected");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isA2dpConnected();
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int getAVRCPConnectState(String address) {
        printLog(TAG, "getAVRCPConnectState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            boolean isConnected = this.nForeService.isAvrcpConnected();
            if (isConnected) {
                String address1 = this.nForeService.getAvrcpConnectedAddress();
                if (address.equals(address1)) {
                    return this.nForeService.getAvrcpConnectionState();
                }
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return -1;
    }

    public boolean isAvrcpConnected() {
        printLog(TAG, "isAvrcpConnected");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isAvrcpConnected();
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public void setBtAutoConnect(int condition, int period) {
        printLog(TAG, "setBtAutoConnect");
        if (this.nForeService == null) {
            return;
        }
        try {
            this.nForeService.setBtAutoConnect(condition, period);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    public boolean reqHfpConnect(String address) {
        printLog(TAG, "reqHfpConnect");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqHfpConnect(address);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reqA2dpConnect(String address) {
        printLog(TAG, "reqA2dpConnect");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqA2dpConnect(address);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reqHfpDisconnect(String address) {
        printLog(TAG, "reqHfpDisconnect");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqHfpDisconnect(address);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reqA2dpDisconnect(String address) {
        printLog(TAG, "reqA2dpDisConnect");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean success = this.nForeService.reqA2dpDisconnect(address);
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
