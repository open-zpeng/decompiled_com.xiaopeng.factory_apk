package com.xiaopeng.xpbluetoothservice.generalcontrol;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback;
/* loaded from: classes2.dex */
public interface IBtGeneralControlService extends IInterface {
    boolean acceptConnect(String str, int i, int i2) throws RemoteException;

    boolean acceptPair(String str, String str2, String str3, boolean z) throws RemoteException;

    void actionDisconnect(String str, String str2) throws RemoteException;

    boolean closeSpp(String str) throws RemoteException;

    boolean connectA2DP(String str, String str2) throws RemoteException;

    boolean connectDevice(String str, String str2) throws RemoteException;

    int connectSpp(String str, String str2, byte[] bArr) throws RemoteException;

    boolean disconnectA2DP(String str) throws RemoteException;

    boolean disconnectDevice(String str) throws RemoteException;

    boolean disconnectSpp(String str, int i) throws RemoteException;

    int getBtPowerStatus(String str) throws RemoteException;

    boolean getConnectStatus(String str, int i, int i2) throws RemoteException;

    boolean getConnectedDeviceInfo(String str, int i, String[] strArr, String[] strArr2) throws RemoteException;

    String getDeviceName(String str) throws RemoteException;

    String getLocalAddress(String str) throws RemoteException;

    boolean getPairedList(String str, int[] iArr, String[] strArr, String[] strArr2, int[] iArr2) throws RemoteException;

    String getRemoteName(String str, String str2) throws RemoteException;

    String getWillConnect(String str) throws RemoteException;

    boolean hasConnected(String str) throws RemoteException;

    boolean initSpp(String str, byte[] bArr) throws RemoteException;

    boolean isCurrentConnecting(String str) throws RemoteException;

    boolean isCurrentPairing(String str) throws RemoteException;

    boolean isCurrentScanning(String str) throws RemoteException;

    boolean isDisConnecting(String str) throws RemoteException;

    boolean isDownloading(String str) throws RemoteException;

    boolean isFirstConnect(String str) throws RemoteException;

    boolean pairDevice(String str, String str2, String str3, int i) throws RemoteException;

    void powerOffBluetooth(String str) throws RemoteException;

    void powerOnBluetooth(String str) throws RemoteException;

    boolean readRSSI(String str, String str2) throws RemoteException;

    boolean rebootBT(String str) throws RemoteException;

    boolean refuseConnect(String str, int i, int i2) throws RemoteException;

    boolean refusePair(String str, String str2, boolean z) throws RemoteException;

    void registerCallback(IGeneralServiceCallback iGeneralServiceCallback) throws RemoteException;

    void registerSppDataCallback(String str) throws RemoteException;

    boolean sendHCICommand(byte[] bArr, int i) throws RemoteException;

    boolean setDeviceName(String str, String str2) throws RemoteException;

    boolean setDiscoveryMode(String str, int i) throws RemoteException;

    boolean setHCITestMode(boolean z) throws RemoteException;

    void setWillConnect(String str, String str2) throws RemoteException;

    void startScanDevice(String str) throws RemoteException;

    void startScanDeviceEx(String str) throws RemoteException;

    boolean stopScanDevice(String str) throws RemoteException;

    void unRegisterSppDataCallback(String str) throws RemoteException;

    boolean unpairDevice(String str, String str2) throws RemoteException;

    void unregisterCallback(IGeneralServiceCallback iGeneralServiceCallback) throws RemoteException;

    int writeSpp(String str, int i, byte[] bArr, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBtGeneralControlService {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService";
        static final int TRANSACTION_acceptConnect = 26;
        static final int TRANSACTION_acceptPair = 20;
        static final int TRANSACTION_actionDisconnect = 42;
        static final int TRANSACTION_closeSpp = 34;
        static final int TRANSACTION_connectA2DP = 31;
        static final int TRANSACTION_connectDevice = 28;
        static final int TRANSACTION_connectSpp = 35;
        static final int TRANSACTION_disconnectA2DP = 30;
        static final int TRANSACTION_disconnectDevice = 29;
        static final int TRANSACTION_disconnectSpp = 36;
        static final int TRANSACTION_getBtPowerStatus = 5;
        static final int TRANSACTION_getConnectStatus = 23;
        static final int TRANSACTION_getConnectedDeviceInfo = 25;
        static final int TRANSACTION_getDeviceName = 7;
        static final int TRANSACTION_getLocalAddress = 8;
        static final int TRANSACTION_getPairedList = 22;
        static final int TRANSACTION_getRemoteName = 9;
        static final int TRANSACTION_getWillConnect = 41;
        static final int TRANSACTION_hasConnected = 24;
        static final int TRANSACTION_initSpp = 33;
        static final int TRANSACTION_isCurrentConnecting = 13;
        static final int TRANSACTION_isCurrentPairing = 10;
        static final int TRANSACTION_isCurrentScanning = 12;
        static final int TRANSACTION_isDisConnecting = 43;
        static final int TRANSACTION_isDownloading = 14;
        static final int TRANSACTION_isFirstConnect = 45;
        static final int TRANSACTION_pairDevice = 18;
        static final int TRANSACTION_powerOffBluetooth = 4;
        static final int TRANSACTION_powerOnBluetooth = 3;
        static final int TRANSACTION_readRSSI = 32;
        static final int TRANSACTION_rebootBT = 44;
        static final int TRANSACTION_refuseConnect = 27;
        static final int TRANSACTION_refusePair = 21;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_registerSppDataCallback = 38;
        static final int TRANSACTION_sendHCICommand = 46;
        static final int TRANSACTION_setDeviceName = 6;
        static final int TRANSACTION_setDiscoveryMode = 11;
        static final int TRANSACTION_setHCITestMode = 47;
        static final int TRANSACTION_setWillConnect = 40;
        static final int TRANSACTION_startScanDevice = 15;
        static final int TRANSACTION_startScanDeviceEx = 16;
        static final int TRANSACTION_stopScanDevice = 17;
        static final int TRANSACTION_unRegisterSppDataCallback = 39;
        static final int TRANSACTION_unpairDevice = 19;
        static final int TRANSACTION_unregisterCallback = 2;
        static final int TRANSACTION_writeSpp = 37;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBtGeneralControlService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBtGeneralControlService)) {
                return (IBtGeneralControlService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            int[] _arg1;
            String[] _arg2;
            String[] _arg3;
            int[] _arg4;
            String[] _arg22;
            String[] _arg32;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    registerCallback(IGeneralServiceCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCallback(IGeneralServiceCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    powerOnBluetooth(data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    powerOffBluetooth(data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getBtPowerStatus(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg12 = data.readString();
                    boolean deviceName = setDeviceName(_arg02, _arg12);
                    reply.writeNoException();
                    reply.writeInt(deviceName ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getDeviceName(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getLocalAddress(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg13 = data.readString();
                    String _result4 = getRemoteName(_arg03, _arg13);
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCurrentPairing = isCurrentPairing(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isCurrentPairing ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    boolean discoveryMode = setDiscoveryMode(_arg04, _arg14);
                    reply.writeNoException();
                    reply.writeInt(discoveryMode ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCurrentScanning = isCurrentScanning(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isCurrentScanning ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCurrentConnecting = isCurrentConnecting(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isCurrentConnecting ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDownloading = isDownloading(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isDownloading ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    startScanDevice(data.readString());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    startScanDeviceEx(data.readString());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stopScanDevice = stopScanDevice(data.readString());
                    reply.writeNoException();
                    reply.writeInt(stopScanDevice ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg15 = data.readString();
                    String _arg23 = data.readString();
                    int _arg33 = data.readInt();
                    boolean pairDevice = pairDevice(_arg05, _arg15, _arg23, _arg33);
                    reply.writeNoException();
                    reply.writeInt(pairDevice ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg16 = data.readString();
                    boolean unpairDevice = unpairDevice(_arg06, _arg16);
                    reply.writeNoException();
                    reply.writeInt(unpairDevice ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg17 = data.readString();
                    String _arg24 = data.readString();
                    _arg0 = data.readInt() != 0;
                    boolean acceptPair = acceptPair(_arg07, _arg17, _arg24, _arg0);
                    reply.writeNoException();
                    reply.writeInt(acceptPair ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    String _arg18 = data.readString();
                    _arg0 = data.readInt() != 0;
                    boolean refusePair = refusePair(_arg08, _arg18, _arg0);
                    reply.writeNoException();
                    reply.writeInt(refusePair ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        int[] _arg19 = new int[_arg1_length];
                        _arg1 = _arg19;
                    }
                    int _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        String[] _arg25 = new String[_arg2_length];
                        _arg2 = _arg25;
                    }
                    int _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        String[] _arg34 = new String[_arg3_length];
                        _arg3 = _arg34;
                    }
                    int _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        int[] _arg42 = new int[_arg4_length];
                        _arg4 = _arg42;
                    }
                    int[] _arg43 = _arg4;
                    int[] _arg44 = _arg1;
                    boolean pairedList = getPairedList(_arg09, _arg44, _arg2, _arg3, _arg43);
                    reply.writeNoException();
                    reply.writeInt(pairedList ? 1 : 0);
                    reply.writeIntArray(_arg1);
                    reply.writeStringArray(_arg2);
                    reply.writeStringArray(_arg3);
                    reply.writeIntArray(_arg43);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg26 = data.readInt();
                    boolean connectStatus = getConnectStatus(_arg010, _arg110, _arg26);
                    reply.writeNoException();
                    reply.writeInt(connectStatus ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasConnected = hasConnected(data.readString());
                    reply.writeNoException();
                    reply.writeInt(hasConnected ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg2_length2 = data.readInt();
                    if (_arg2_length2 < 0) {
                        _arg22 = null;
                    } else {
                        _arg22 = new String[_arg2_length2];
                    }
                    int _arg3_length2 = data.readInt();
                    if (_arg3_length2 < 0) {
                        _arg32 = null;
                    } else {
                        _arg32 = new String[_arg3_length2];
                    }
                    boolean connectedDeviceInfo = getConnectedDeviceInfo(_arg011, _arg111, _arg22, _arg32);
                    reply.writeNoException();
                    reply.writeInt(connectedDeviceInfo ? 1 : 0);
                    reply.writeStringArray(_arg22);
                    reply.writeStringArray(_arg32);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    int _arg112 = data.readInt();
                    int _arg27 = data.readInt();
                    boolean acceptConnect = acceptConnect(_arg012, _arg112, _arg27);
                    reply.writeNoException();
                    reply.writeInt(acceptConnect ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _arg113 = data.readInt();
                    int _arg28 = data.readInt();
                    boolean refuseConnect = refuseConnect(_arg013, _arg113, _arg28);
                    reply.writeNoException();
                    reply.writeInt(refuseConnect ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    String _arg114 = data.readString();
                    boolean connectDevice = connectDevice(_arg014, _arg114);
                    reply.writeNoException();
                    reply.writeInt(connectDevice ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    boolean disconnectDevice = disconnectDevice(data.readString());
                    reply.writeNoException();
                    reply.writeInt(disconnectDevice ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    boolean disconnectA2DP = disconnectA2DP(data.readString());
                    reply.writeNoException();
                    reply.writeInt(disconnectA2DP ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    String _arg115 = data.readString();
                    boolean connectA2DP = connectA2DP(_arg015, _arg115);
                    reply.writeNoException();
                    reply.writeInt(connectA2DP ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    String _arg116 = data.readString();
                    boolean readRSSI = readRSSI(_arg016, _arg116);
                    reply.writeNoException();
                    reply.writeInt(readRSSI ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    byte[] _arg117 = data.createByteArray();
                    boolean initSpp = initSpp(_arg017, _arg117);
                    reply.writeNoException();
                    reply.writeInt(initSpp ? 1 : 0);
                    reply.writeByteArray(_arg117);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    boolean closeSpp = closeSpp(data.readString());
                    reply.writeNoException();
                    reply.writeInt(closeSpp ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    String _arg118 = data.readString();
                    byte[] _arg29 = data.createByteArray();
                    int _result5 = connectSpp(_arg018, _arg118, _arg29);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    reply.writeByteArray(_arg29);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int _arg119 = data.readInt();
                    boolean disconnectSpp = disconnectSpp(_arg019, _arg119);
                    reply.writeNoException();
                    reply.writeInt(disconnectSpp ? 1 : 0);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    int _arg120 = data.readInt();
                    byte[] _arg210 = data.createByteArray();
                    int _arg35 = data.readInt();
                    int _result6 = writeSpp(_arg020, _arg120, _arg210, _arg35);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    reply.writeByteArray(_arg210);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    registerSppDataCallback(data.readString());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    unRegisterSppDataCallback(data.readString());
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    String _arg121 = data.readString();
                    setWillConnect(_arg021, _arg121);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getWillConnect(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    String _arg122 = data.readString();
                    actionDisconnect(_arg022, _arg122);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDisConnecting = isDisConnecting(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isDisConnecting ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    boolean rebootBT = rebootBT(data.readString());
                    reply.writeNoException();
                    reply.writeInt(rebootBT ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isFirstConnect = isFirstConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isFirstConnect ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg023 = data.createByteArray();
                    int _arg123 = data.readInt();
                    boolean sendHCICommand = sendHCICommand(_arg023, _arg123);
                    reply.writeNoException();
                    reply.writeInt(sendHCICommand ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean hCITestMode = setHCITestMode(_arg0);
                    reply.writeNoException();
                    reply.writeInt(hCITestMode ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IBtGeneralControlService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void registerCallback(IGeneralServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void unregisterCallback(IGeneralServiceCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void powerOnBluetooth(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void powerOffBluetooth(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public int getBtPowerStatus(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean setDeviceName(String pUUid, String deviceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(deviceName);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public String getDeviceName(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public String getLocalAddress(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public String getRemoteName(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isCurrentPairing(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean setDiscoveryMode(String pUUid, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(mask);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isCurrentScanning(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isCurrentConnecting(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isDownloading(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void startScanDevice(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void startScanDeviceEx(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean stopScanDevice(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean pairDevice(String pUUid, String address, String pin, int cod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    _data.writeString(pin);
                    _data.writeInt(cod);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean unpairDevice(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean acceptPair(String pUUid, String address, String pin, boolean ssp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    _data.writeString(pin);
                    _data.writeInt(ssp ? 1 : 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean refusePair(String pUUid, String address, boolean ssp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    _data.writeInt(ssp ? 1 : 0);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean getPairedList(String pUUid, int[] count, String[] name, String[] address, int[] cod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    if (count == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(count.length);
                    }
                    if (name == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(name.length);
                    }
                    if (address == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(address.length);
                    }
                    if (cod == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(cod.length);
                    }
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.readIntArray(count);
                    _reply.readStringArray(name);
                    _reply.readStringArray(address);
                    _reply.readIntArray(cod);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean getConnectStatus(String pUUid, int profileType, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(profileType);
                    _data.writeInt(index);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean hasConnected(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean getConnectedDeviceInfo(String pUUid, int profileType, String[] deviceAddress, String[] deviceName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(profileType);
                    if (deviceAddress == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(deviceAddress.length);
                    }
                    if (deviceName == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(deviceName.length);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.readStringArray(deviceAddress);
                    _reply.readStringArray(deviceName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean acceptConnect(String pUUid, int profileType, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(profileType);
                    _data.writeInt(index);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean refuseConnect(String pUUid, int profileType, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(profileType);
                    _data.writeInt(index);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean connectDevice(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean disconnectDevice(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean disconnectA2DP(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean connectA2DP(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean readRSSI(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean initSpp(String pUUid, byte[] serviceGUID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeByteArray(serviceGUID);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.readByteArray(serviceGUID);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean closeSpp(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public int connectSpp(String pUUid, String address, byte[] GUID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    _data.writeByteArray(GUID);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(GUID);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean disconnectSpp(String pUUid, int index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(index);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public int writeSpp(String pUUid, int index, byte[] data, int dataSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(index);
                    _data.writeByteArray(data);
                    _data.writeInt(dataSize);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(data);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void registerSppDataCallback(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void unRegisterSppDataCallback(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void setWillConnect(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public String getWillConnect(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public void actionDisconnect(String pUUid, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(address);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isDisConnecting(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean rebootBT(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean isFirstConnect(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean sendHCICommand(byte[] cmd, int length) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(cmd);
                    _data.writeInt(length);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService
            public boolean setHCITestMode(boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag ? 1 : 0);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
