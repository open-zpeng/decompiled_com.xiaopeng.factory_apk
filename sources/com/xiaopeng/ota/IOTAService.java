package com.xiaopeng.ota;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.ota.IOTAListener;
/* loaded from: classes2.dex */
public interface IOTAService extends IInterface {
    boolean activatePsu() throws RemoteException;

    void checkCampaign() throws RemoteException;

    String getCduVersion() throws RemoteException;

    void getECUSVersion() throws RemoteException;

    double getInstallProgress() throws RemoteException;

    int getInstallState() throws RemoteException;

    String getPsuId(boolean z, long j) throws RemoteException;

    String getPsuVersion() throws RemoteException;

    String getText(String str) throws RemoteException;

    String getUpgradeTime() throws RemoteException;

    boolean hasCduFileFromUsb() throws RemoteException;

    boolean hasMcuFileFromUsb() throws RemoteException;

    boolean hasMeterFileFromUsb() throws RemoteException;

    boolean hasPsuFileFromUsb() throws RemoteException;

    boolean hasScuFileFromUsb() throws RemoteException;

    boolean isCarTypeD10() throws RemoteException;

    void onScreenHide() throws RemoteException;

    void onScreenShow() throws RemoteException;

    void registerListener(IOTAListener iOTAListener) throws RemoteException;

    boolean resetPsuKey(byte[] bArr) throws RemoteException;

    void setUpgradeTime() throws RemoteException;

    void unregisterListener(IOTAListener iOTAListener) throws RemoteException;

    void updateCduFromUsb() throws RemoteException;

    void updateEcuComplete(String str, boolean z) throws RemoteException;

    void updateMcuFromUsb() throws RemoteException;

    void updateMeterFromUsb() throws RemoteException;

    void updatePsuFromUsb() throws RemoteException;

    void updateScuFromUsb() throws RemoteException;

    void upgradeNow() throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IOTAService {
        @Override // com.xiaopeng.ota.IOTAService
        public String getCduVersion() throws RemoteException {
            return null;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void registerListener(IOTAListener listener) throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void unregisterListener(IOTAListener listener) throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void getECUSVersion() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void checkCampaign() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void upgradeNow() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public String getText(String textId) throws RemoteException {
            return null;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public int getInstallState() throws RemoteException {
            return 0;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public double getInstallProgress() throws RemoteException {
            return 0.0d;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean hasMcuFileFromUsb() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updateMcuFromUsb() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean hasMeterFileFromUsb() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updateMeterFromUsb() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean hasCduFileFromUsb() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updateCduFromUsb() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean hasScuFileFromUsb() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updateScuFromUsb() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void onScreenShow() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void onScreenHide() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updatePsuFromUsb() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean hasPsuFileFromUsb() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean resetPsuKey(byte[] binBytes) throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public String getPsuVersion() throws RemoteException {
            return null;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void setUpgradeTime() throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public String getUpgradeTime() throws RemoteException {
            return null;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean isCarTypeD10() throws RemoteException {
            return false;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public void updateEcuComplete(String ecuName, boolean isSuccess) throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAService
        public String getPsuId(boolean isActivated, long timeout) throws RemoteException {
            return null;
        }

        @Override // com.xiaopeng.ota.IOTAService
        public boolean activatePsu() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IOTAService {
        private static final String DESCRIPTOR = "com.xiaopeng.ota.IOTAService";
        static final int TRANSACTION_activatePsu = 29;
        static final int TRANSACTION_checkCampaign = 5;
        static final int TRANSACTION_getCduVersion = 1;
        static final int TRANSACTION_getECUSVersion = 4;
        static final int TRANSACTION_getInstallProgress = 9;
        static final int TRANSACTION_getInstallState = 8;
        static final int TRANSACTION_getPsuId = 28;
        static final int TRANSACTION_getPsuVersion = 23;
        static final int TRANSACTION_getText = 7;
        static final int TRANSACTION_getUpgradeTime = 25;
        static final int TRANSACTION_hasCduFileFromUsb = 14;
        static final int TRANSACTION_hasMcuFileFromUsb = 10;
        static final int TRANSACTION_hasMeterFileFromUsb = 12;
        static final int TRANSACTION_hasPsuFileFromUsb = 21;
        static final int TRANSACTION_hasScuFileFromUsb = 16;
        static final int TRANSACTION_isCarTypeD10 = 26;
        static final int TRANSACTION_onScreenHide = 19;
        static final int TRANSACTION_onScreenShow = 18;
        static final int TRANSACTION_registerListener = 2;
        static final int TRANSACTION_resetPsuKey = 22;
        static final int TRANSACTION_setUpgradeTime = 24;
        static final int TRANSACTION_unregisterListener = 3;
        static final int TRANSACTION_updateCduFromUsb = 15;
        static final int TRANSACTION_updateEcuComplete = 27;
        static final int TRANSACTION_updateMcuFromUsb = 11;
        static final int TRANSACTION_updateMeterFromUsb = 13;
        static final int TRANSACTION_updatePsuFromUsb = 20;
        static final int TRANSACTION_updateScuFromUsb = 17;
        static final int TRANSACTION_upgradeNow = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOTAService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOTAService)) {
                return (IOTAService) iin;
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
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getCduVersion();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    registerListener(IOTAListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterListener(IOTAListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    getECUSVersion();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    checkCampaign();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    upgradeNow();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getText(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _result3 = getInstallState();
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    double _result4 = getInstallProgress();
                    reply.writeNoException();
                    reply.writeDouble(_result4);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasMcuFileFromUsb = hasMcuFileFromUsb();
                    reply.writeNoException();
                    reply.writeInt(hasMcuFileFromUsb ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    updateMcuFromUsb();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasMeterFileFromUsb = hasMeterFileFromUsb();
                    reply.writeNoException();
                    reply.writeInt(hasMeterFileFromUsb ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    updateMeterFromUsb();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasCduFileFromUsb = hasCduFileFromUsb();
                    reply.writeNoException();
                    reply.writeInt(hasCduFileFromUsb ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    updateCduFromUsb();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasScuFileFromUsb = hasScuFileFromUsb();
                    reply.writeNoException();
                    reply.writeInt(hasScuFileFromUsb ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    updateScuFromUsb();
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenShow();
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenHide();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    updatePsuFromUsb();
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    boolean hasPsuFileFromUsb = hasPsuFileFromUsb();
                    reply.writeNoException();
                    reply.writeInt(hasPsuFileFromUsb ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean resetPsuKey = resetPsuKey(data.createByteArray());
                    reply.writeNoException();
                    reply.writeInt(resetPsuKey ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = getPsuVersion();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    setUpgradeTime();
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _result6 = getUpgradeTime();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isCarTypeD10 = isCarTypeD10();
                    reply.writeNoException();
                    reply.writeInt(isCarTypeD10 ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    _arg0 = data.readInt() != 0;
                    updateEcuComplete(_arg02, _arg0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    long _arg1 = data.readLong();
                    String _result7 = getPsuId(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    boolean activatePsu = activatePsu();
                    reply.writeNoException();
                    reply.writeInt(activatePsu ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IOTAService {
            public static IOTAService sDefaultImpl;
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

            @Override // com.xiaopeng.ota.IOTAService
            public String getCduVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCduVersion();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void registerListener(IOTAListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void unregisterListener(IOTAListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void getECUSVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getECUSVersion();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void checkCampaign() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkCampaign();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void upgradeNow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().upgradeNow();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public String getText(String textId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(textId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getText(textId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public int getInstallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstallState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public double getInstallProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstallProgress();
                    }
                    _reply.readException();
                    double _result = _reply.readDouble();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean hasMcuFileFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasMcuFileFromUsb();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updateMcuFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateMcuFromUsb();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean hasMeterFileFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasMeterFileFromUsb();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updateMeterFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateMeterFromUsb();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean hasCduFileFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasCduFileFromUsb();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updateCduFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateCduFromUsb();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean hasScuFileFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasScuFileFromUsb();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updateScuFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateScuFromUsb();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void onScreenShow() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onScreenShow();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void onScreenHide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onScreenHide();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updatePsuFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePsuFromUsb();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean hasPsuFileFromUsb() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasPsuFileFromUsb();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean resetPsuKey(byte[] binBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(binBytes);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resetPsuKey(binBytes);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public String getPsuVersion() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPsuVersion();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void setUpgradeTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setUpgradeTime();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public String getUpgradeTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUpgradeTime();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean isCarTypeD10() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCarTypeD10();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public void updateEcuComplete(String ecuName, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ecuName);
                    _data.writeInt(isSuccess ? 1 : 0);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateEcuComplete(ecuName, isSuccess);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public String getPsuId(boolean isActivated, long timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isActivated ? 1 : 0);
                    _data.writeLong(timeout);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPsuId(isActivated, timeout);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAService
            public boolean activatePsu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().activatePsu();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOTAService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IOTAService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
