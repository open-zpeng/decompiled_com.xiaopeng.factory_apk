package com.xiaopeng.btservice.usb;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.nforetek.bt.aidl.UiCallbackUsbBluetooth;
import com.nforetek.bt.aidl.UiCommand;
import com.nforetek.bt.aidl.UsbBluetoothDevice;
import com.xiaopeng.btservice.base.AbsControl;
import com.xiaopeng.btservice.base.AbsUsbBluetoothCallback;
import java.util.List;
/* loaded from: classes.dex */
public class UsbBluetoothControl extends AbsControl {
    private static final String TAG = "UsbBluetoothControl";
    private AbsUsbBluetoothCallback mCallback;
    private UiCallbackUsbBluetooth mCallbackUsbBluetooth = new UiCallbackUsbBluetooth.Stub() { // from class: com.xiaopeng.btservice.usb.UsbBluetoothControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbPairedDevices(int state, String address, String name) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbPairedDevices(state, address, name);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbDeviceFound(String address, String name, byte category) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbDeviceFound(address, name, category);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbAdapterStateChanged(int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbAdapterStateChanged(prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbAdapterDiscoveryStarted() throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbAdapterDiscoveryStarted();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbAdapterDiscoveryFinished() throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbAdapterDiscoveryFinished();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbDeviceBondStateChanged(String address, String name, int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbDeviceBondStateChanged(address, name, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbHfpStateChanged(String address, int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbHfpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbA2dpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbAvrcpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbDeviceState(int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbDeviceState(newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackUsbBluetooth
        public void onUsbConnectionStateChanged(String address, int prevState, int newState) throws RemoteException {
            if (UsbBluetoothControl.this.mCallback != null) {
                UsbBluetoothControl.this.mCallback.onUsbConnectionStateChanged(address, prevState, newState);
            }
        }
    };

    public UsbBluetoothControl(Context mContext, AbsUsbBluetoothCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            Log.d(TAG, "bluetooth registerCallback " + this.nForeService);
            boolean register = btService.registerUsbCallback(this.mCallbackUsbBluetooth);
            Log.d(TAG, "bluetooth nf btService registerCallback " + register);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            Log.d(TAG, "release");
            if (this.nForeService != null) {
                this.nForeService.unregisterUsbCallback(this.mCallbackUsbBluetooth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean usbSearch() {
        printLog(TAG, "usbSearch");
        boolean success = false;
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
        } catch (Exception e) {
            printError(TAG, e);
        }
        if (this.nForeService.isUsbDiscovering()) {
            Log.d(TAG, "startBtDiscovery isUsbDiscovering");
            return true;
        }
        success = this.nForeService.usbSearch();
        StringBuilder sb = new StringBuilder();
        sb.append("app usbSearch pkg:");
        sb.append(this.mContext != null ? this.mContext.getPackageName() : "");
        sb.append(" success:");
        sb.append(success);
        Log.d(TAG, sb.toString());
        return success;
    }

    public boolean usbStopSearch() {
        printLog(TAG, "usbStopSearch");
        boolean success = false;
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            success = this.nForeService.usbStopSearch();
            StringBuilder sb = new StringBuilder();
            sb.append("app usbStopSearch pkg:");
            sb.append(this.mContext != null ? this.mContext.getPackageName() : "");
            sb.append(" success:");
            sb.append(success);
            Log.d(TAG, sb.toString());
        } catch (Exception e) {
            printError(TAG, e);
        }
        return success;
    }

    public boolean isUsbDiscovering() {
        printLog(TAG, "isUsbDiscovering");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean success = this.nForeService.isUsbDiscovering();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean usbConnect(String address) {
        printLog(TAG, "usbConnect");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            return this.nForeService.usbConnect(address);
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean usbDisConnect() {
        printLog(TAG, "usbDisConnect");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            return this.nForeService.usbDisConnect();
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean usbDisConnectDevice(String address) {
        printLog(TAG, "usbDisConnectDevice " + address);
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            return this.nForeService.usbDisConnectDevice(address);
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean usbReqUnpair(String address) {
        printLog(TAG, "usbReqUnpair");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean success = this.nForeService.usbReqUnpair(address);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean setUsbEnabled(boolean enable) {
        printLog(TAG, "setUsbEnabled");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean success = this.nForeService.setUsbEnabled(enable);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isUsbEnabled() {
        printLog(TAG, "isUsbEnabled");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean success = this.nForeService.isUsbEnabled();
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isUsbHfpConnected(String address) {
        printLog(TAG, "isUsbHfpConnected");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isUsbHfpConnected(address);
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean isUsbA2dpConnected(String address) {
        printLog(TAG, "isUsbA2dpConnected");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isUsbA2dpConnected(address);
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int getUsbConnectionState(String address) {
        printLog(TAG, "getUsbConnectionState");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return -1;
        }
        try {
            int status = this.nForeService.getUsbConnectionState(address);
            return status;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean isUsbAvrcpConnected(String address) {
        printLog(TAG, "isUsbAvrcpConnected");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isUsbAvrcpConnected(address);
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public List<UsbBluetoothDevice> reqUsbBtPairedDevices() {
        printLog(TAG, "reqUsbBtPairedDevices");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return null;
        }
        try {
            List<UsbBluetoothDevice> list = this.nForeService.reqUsbBtPairedDevices();
            return list;
        } catch (Exception e) {
            printError(TAG, e);
            return null;
        }
    }

    public String getUsbBtLocalName() {
        printLog(TAG, "getUsbBtLocalName");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return "";
        }
        try {
            return this.nForeService.getUsbBtLocalName();
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public boolean setUsbBtLocalName(String name) {
        printLog(TAG, "setUsbBtLocalName");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return false;
        }
        try {
            boolean success = this.nForeService.setUsbBtLocalName(name);
            return success;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public String getUsbAddress() {
        printLog(TAG, "getUsbAddress");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return "";
        }
        try {
            return this.nForeService.getUsbAddress();
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public String getUsbConnectedDevice() {
        printLog(TAG, "getUsbConnectedDevice");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return "";
        }
        try {
            return this.nForeService.getUsbConnectedDevice();
        } catch (Exception e) {
            printError(TAG, e);
            return "";
        }
    }

    public int getLocalUsbConnectionState() {
        printLog(TAG, "getLocalUsbConnectionState");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return -1;
        }
        try {
            return this.nForeService.getLocalUsbConnectionState();
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public int getProfileConnectionState(int profile) {
        printLog(TAG, "getProfileConnectionState");
        if (this.nForeService == null) {
            printLog(TAG, "nForeService == null");
            return -1;
        }
        try {
            return this.nForeService.getProfileConnectionState(profile);
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }
}
