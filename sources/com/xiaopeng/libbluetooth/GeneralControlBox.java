package com.xiaopeng.libbluetooth;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import cn.hutool.core.text.CharSequenceUtil;
import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
import com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService;
import com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback;
/* loaded from: classes2.dex */
public class GeneralControlBox extends AbsControlBox {
    private static final String TAG = "GeneralControlBox";
    private AbsGeneralControlCallback mCallback;
    private Context mContext;
    private OnInquiryStopListener mOnInquiryStopListener;
    private IBtGeneralControlService mService;
    private IGeneralServiceCallback mServiceCallback = new IGeneralServiceCallback.Stub() { // from class: com.xiaopeng.libbluetooth.GeneralControlBox.1
        @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
        public void onMessageCallback(int action, Bundle bundle) {
            boolean connectStatus;
            if (bundle == null) {
                return;
            }
            try {
                if (GeneralControlBox.this.mCallback == null) {
                    return;
                }
                if (action == 5) {
                    boolean isOn = bundle.getBoolean("Value");
                    GeneralControlBox.this.mCallback.onBtPower(isOn);
                } else if (action == 6) {
                    int status = bundle.getInt("Status");
                    String address = bundle.getString("Address");
                    GeneralControlBox.this.mCallback.onBtPairStatus(status, address);
                } else if (action == 7) {
                    String pairAddress = bundle.getString("Address");
                    String pairName = bundle.getString("DeviceName");
                    int pairCode = bundle.getInt("PINCode", -1);
                    boolean pairBssp = bundle.getBoolean("SSP");
                    GeneralControlBox.this.mCallback.onPairRequest(pairAddress, pairName, pairCode, pairBssp);
                } else if (action == 9) {
                    int value = bundle.getInt("Value");
                    if (value == 1) {
                        connectStatus = true;
                    } else {
                        connectStatus = false;
                    }
                    String deviceAddress = bundle.getString("Address");
                    int linkLost = bundle.getInt("Linklost");
                    int profileType = bundle.getInt("Profile");
                    int index = bundle.getInt("Index");
                    GeneralControlBox.this.mCallback.onBtConnectStatus(deviceAddress, connectStatus, linkLost, profileType, index);
                } else if (action == 20) {
                    String address2 = bundle.getString("Address", "");
                    int rssiValue = bundle.getInt("Value", -1);
                    GeneralControlBox.this.mCallback.onBtRssiCallback(address2, rssiValue);
                }
            } catch (Exception e) {
                GeneralControlBox.this.printError(e);
            }
        }

        @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
        public void onScanCallback(String address, String deviceName, int cod, int rssi, boolean complete) {
            if (complete) {
                try {
                    if (GeneralControlBox.this.mOnInquiryStopListener != null) {
                        GeneralControlBox.this.mOnInquiryStopListener.onInquiryStopSuccess();
                        GeneralControlBox.this.mOnInquiryStopListener = null;
                    }
                } catch (Exception e) {
                    GeneralControlBox.this.printError(e);
                    return;
                }
            }
            if (GeneralControlBox.this.mCallback == null) {
                return;
            }
            GeneralControlBox.this.mCallback.onScanCallback(address, deviceName, cod, rssi, complete);
        }

        @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
        public void onSppDataCallback(int index, byte[] data, int dataLength) throws RemoteException {
            if (GeneralControlBox.this.mCallback == null) {
                return;
            }
            try {
                GeneralControlBox.this.mCallback.onSppDataCallback(index, data, dataLength);
            } catch (Exception e) {
                GeneralControlBox.this.printError(e);
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface OnInquiryStopListener {
        void onInquiryStopSuccess();
    }

    public GeneralControlBox(Context context, AbsGeneralControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void initService(IXPBluetoothService btService) {
        try {
            this.mService = btService.getBtGeneralControlService();
            this.mService.registerCallback(this.mServiceCallback);
            if (this.mCallback != null) {
                this.mCallback.onBindSuccess();
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void clearService() {
        this.mService = null;
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void onWorkDone() {
    }

    @Override // com.xiaopeng.libbluetooth.AbsControlBox
    protected void release() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService != null) {
            try {
                iBtGeneralControlService.unregisterCallback(this.mServiceCallback);
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    public boolean powerOnBluetooth() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            iBtGeneralControlService.powerOnBluetooth(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean powerOffBluetooth() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            iBtGeneralControlService.powerOffBluetooth(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int getBtPowerStatus() {
        try {
            if (this.mService == null) {
                return 0;
            }
            int status = this.mService.getBtPowerStatus(BluetoothBoxes.getID());
            return status;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public boolean setDeviceName(String deviceName) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.setDeviceName(BluetoothBoxes.getID(), deviceName);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public String getDeviceName() {
        try {
            if (this.mService == null) {
                return null;
            }
            String name = this.mService.getDeviceName(BluetoothBoxes.getID());
            return name;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public String getLocalAddress() {
        try {
            if (this.mService == null) {
                return null;
            }
            String address = this.mService.getLocalAddress(BluetoothBoxes.getID());
            return address;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public String getRemoteName(String address) {
        try {
            if (this.mService == null) {
                return null;
            }
            String name = this.mService.getRemoteName(BluetoothBoxes.getID(), address);
            return name;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public boolean isCurrentPairing() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.isCurrentPairing(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isCurrentConnecting() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.isCurrentConnecting(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isDownloading() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.isDownloading(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setDiscoveryMode(int mask) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.setDiscoveryMode(BluetoothBoxes.getID(), mask);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isCurrentScanning() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.isCurrentScanning(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startScanDevice() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            iBtGeneralControlService.startScanDevice(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean startScanDeviceEx() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            iBtGeneralControlService.startScanDeviceEx(BluetoothBoxes.getID());
            return true;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean stopScanDevice(OnInquiryStopListener listener) {
        this.mOnInquiryStopListener = listener;
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.stopScanDevice(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean pairDevice(String address, String pin, int cod) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.pairDevice(BluetoothBoxes.getID(), address, pin, cod);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean unpairDevice(String address) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.unpairDevice(BluetoothBoxes.getID(), address);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean acceptPair(String address, String pin, boolean ssp) {
        try {
            if (this.mService != null) {
                this.mService.acceptPair(BluetoothBoxes.getID(), address, pin, ssp);
            }
        } catch (Exception e) {
            printError(e);
        }
        return false;
    }

    public boolean refusePair(String address, boolean ssp) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSucess = this.mService.refusePair(BluetoothBoxes.getID(), address, ssp);
            return isSucess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getPairedList(int[] count, String[] name, String[] address, int[] cod) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.getPairedList(BluetoothBoxes.getID(), count, name, address, cod);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getConnectStatus(int profileType, int index) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.getConnectStatus(BluetoothBoxes.getID(), profileType, index);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean hasConnected() {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.hasConnected(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean getConnectedDeviceInfo(int profileType, String[] deviceAddress, String[] deviceName) {
        try {
            if (this.mService == null) {
                return false;
            }
            boolean isSuccess = this.mService.getConnectedDeviceInfo(BluetoothBoxes.getID(), profileType, deviceAddress, deviceName);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean acceptConnect(int profileType, int index) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.acceptConnect(BluetoothBoxes.getID(), profileType, index);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean refuseConnect(int profileType, int index) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.refuseConnect(BluetoothBoxes.getID(), profileType, index);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean connectDevice(String address) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.connectDevice(BluetoothBoxes.getID(), address);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean connectA2DP(String address) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.connectA2DP(BluetoothBoxes.getID(), address);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean disconnectDevice() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.disconnectDevice(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean disconnectA2DP() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.disconnectA2DP(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean readRSSI(String address) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.readRSSI(BluetoothBoxes.getID(), address);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean initSpp(byte[] serviceGUID) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.initSpp(BluetoothBoxes.getID(), serviceGUID);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean closeSpp() {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.closeSpp(BluetoothBoxes.getID());
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int connectSpp(String address, byte[] GUID) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return -1;
        }
        try {
            int indexId = iBtGeneralControlService.connectSpp(BluetoothBoxes.getID(), address, GUID);
            return indexId;
        } catch (Exception e) {
            printError(e);
            return -1;
        }
    }

    public boolean disconnectSpp(int index) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return false;
        }
        try {
            boolean isSuccess = iBtGeneralControlService.disconnectSpp(BluetoothBoxes.getID(), index);
            return isSuccess;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public int writeSpp(int index, byte[] data, int dataSize) {
        IBtGeneralControlService iBtGeneralControlService = this.mService;
        if (iBtGeneralControlService == null) {
            return 0;
        }
        try {
            int dataLength = iBtGeneralControlService.writeSpp(BluetoothBoxes.getID(), index, data, dataSize);
            return dataLength;
        } catch (Exception e) {
            printError(e);
            return 0;
        }
    }

    public void registerSppDataCallback() {
        try {
            this.mService.registerSppDataCallback(BluetoothBoxes.getID());
        } catch (Exception e) {
            printError(e);
        }
    }

    public void unRegisterSppDataCallback() {
        try {
            this.mService.unRegisterSppDataCallback(BluetoothBoxes.getID());
        } catch (Exception e) {
            printError(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void printError(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(e);
        Log.e(TAG, sb.toString() != null ? e.getMessage() : CharSequenceUtil.NULL);
    }

    public void setWillConnect(String address) {
        try {
            if (this.mService != null) {
                this.mService.setWillConnect(BluetoothBoxes.getID(), address);
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    public String getWillConnect() {
        try {
            if (this.mService != null) {
                return this.mService.getWillConnect(BluetoothBoxes.getID());
            }
            return null;
        } catch (Exception e) {
            printError(e);
            return null;
        }
    }

    public void actionDisconnect(String address) {
        try {
            if (this.mService != null) {
                this.mService.actionDisconnect(BluetoothBoxes.getID(), address);
            }
        } catch (Exception e) {
            printError(e);
        }
    }

    public boolean isDisConnecting() {
        try {
            if (this.mService != null) {
                return this.mService.isDisConnecting(BluetoothBoxes.getID());
            }
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean rebootBT() {
        try {
            if (this.mService != null) {
                return this.mService.rebootBT(BluetoothBoxes.getID());
            }
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean isFirstConnect() {
        try {
            if (this.mService != null) {
                return this.mService.isFirstConnect(BluetoothBoxes.getID());
            }
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean sendHCICommand(byte[] cmd, int length) {
        try {
            if (this.mService != null) {
                return this.mService.sendHCICommand(cmd, length);
            }
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }

    public boolean setHCITestMode(boolean flag) {
        try {
            if (this.mService != null) {
                return this.mService.setHCITestMode(flag);
            }
            return false;
        } catch (Exception e) {
            printError(e);
            return false;
        }
    }
}
