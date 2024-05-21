package xp.hardware.dab;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import xp.hardware.dab.IDabListener;
/* loaded from: classes2.dex */
public interface IRadioServiceBase extends IInterface {
    void allowAnnouncement(int i) throws RemoteException;

    void cancelAnnouncement(int i) throws RemoteException;

    boolean componentSeekDown() throws RemoteException;

    boolean componentSeekUp() throws RemoteException;

    boolean enterApp() throws RemoteException;

    boolean exitApp() throws RemoteException;

    DabInfo getCurrentDabInfo() throws RemoteException;

    int getLastSource() throws RemoteException;

    int getLinkSource() throws RemoteException;

    void getSnrLevel() throws RemoteException;

    void initDabChip() throws RemoteException;

    boolean isDabDabLinkOn() throws RemoteException;

    boolean isDabFmLinkOn() throws RemoteException;

    boolean isTpOn() throws RemoteException;

    void registerDabLisenter(IDabListener iDabListener) throws RemoteException;

    void rejectAnnouncement(int i) throws RemoteException;

    void reset() throws RemoteException;

    boolean scan() throws RemoteException;

    boolean seekDown() throws RemoteException;

    boolean seekUp() throws RemoteException;

    void setDabDabLinkOn(boolean z) throws RemoteException;

    void setDabFmLinkOn(boolean z) throws RemoteException;

    void setTpOn(boolean z) throws RemoteException;

    boolean stepDown() throws RemoteException;

    boolean stepUp() throws RemoteException;

    boolean stopComponentSeek() throws RemoteException;

    boolean switchSourceTo(int i) throws RemoteException;

    boolean tuneToAmFm(int i) throws RemoteException;

    boolean tuneto(int i, int i2, int i3) throws RemoteException;

    void unregisterDabLisenter(IDabListener iDabListener) throws RemoteException;

    boolean updateStationList() throws RemoteException;

    boolean updateStationListByType(int i) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IRadioServiceBase {
        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean switchSourceTo(int source) throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean stepUp() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean stepDown() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean seekUp() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean seekDown() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean scan() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean updateStationList() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean updateStationListByType(int type) throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean tuneto(int freqId, int serviceId, int componentId) throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean tuneToAmFm(int freqId) throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean componentSeekUp() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean componentSeekDown() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean stopComponentSeek() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean isDabDabLinkOn() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean isDabFmLinkOn() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean isTpOn() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean enterApp() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public boolean exitApp() throws RemoteException {
            return false;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public int getLastSource() throws RemoteException {
            return 0;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public int getLinkSource() throws RemoteException {
            return 0;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public DabInfo getCurrentDabInfo() throws RemoteException {
            return null;
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void rejectAnnouncement(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void allowAnnouncement(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void cancelAnnouncement(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void registerDabLisenter(IDabListener iDabListener) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void unregisterDabLisenter(IDabListener iDabListener) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void setDabFmLinkOn(boolean isDabFmLinkOn) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void setDabDabLinkOn(boolean isOn) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void setTpOn(boolean isOn) throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void reset() throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void getSnrLevel() throws RemoteException {
        }

        @Override // xp.hardware.dab.IRadioServiceBase
        public void initDabChip() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IRadioServiceBase {
        private static final String DESCRIPTOR = "xp.hardware.dab.IRadioServiceBase";
        static final int TRANSACTION_allowAnnouncement = 23;
        static final int TRANSACTION_cancelAnnouncement = 24;
        static final int TRANSACTION_componentSeekDown = 12;
        static final int TRANSACTION_componentSeekUp = 11;
        static final int TRANSACTION_enterApp = 17;
        static final int TRANSACTION_exitApp = 18;
        static final int TRANSACTION_getCurrentDabInfo = 21;
        static final int TRANSACTION_getLastSource = 19;
        static final int TRANSACTION_getLinkSource = 20;
        static final int TRANSACTION_getSnrLevel = 31;
        static final int TRANSACTION_initDabChip = 32;
        static final int TRANSACTION_isDabDabLinkOn = 14;
        static final int TRANSACTION_isDabFmLinkOn = 15;
        static final int TRANSACTION_isTpOn = 16;
        static final int TRANSACTION_registerDabLisenter = 25;
        static final int TRANSACTION_rejectAnnouncement = 22;
        static final int TRANSACTION_reset = 30;
        static final int TRANSACTION_scan = 6;
        static final int TRANSACTION_seekDown = 5;
        static final int TRANSACTION_seekUp = 4;
        static final int TRANSACTION_setDabDabLinkOn = 28;
        static final int TRANSACTION_setDabFmLinkOn = 27;
        static final int TRANSACTION_setTpOn = 29;
        static final int TRANSACTION_stepDown = 3;
        static final int TRANSACTION_stepUp = 2;
        static final int TRANSACTION_stopComponentSeek = 13;
        static final int TRANSACTION_switchSourceTo = 1;
        static final int TRANSACTION_tuneToAmFm = 10;
        static final int TRANSACTION_tuneto = 9;
        static final int TRANSACTION_unregisterDabLisenter = 26;
        static final int TRANSACTION_updateStationList = 7;
        static final int TRANSACTION_updateStationListByType = 8;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRadioServiceBase asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IRadioServiceBase)) {
                return (IRadioServiceBase) iin;
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
                    boolean switchSourceTo = switchSourceTo(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(switchSourceTo ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stepUp = stepUp();
                    reply.writeNoException();
                    reply.writeInt(stepUp ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stepDown = stepDown();
                    reply.writeNoException();
                    reply.writeInt(stepDown ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean seekUp = seekUp();
                    reply.writeNoException();
                    reply.writeInt(seekUp ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean seekDown = seekDown();
                    reply.writeNoException();
                    reply.writeInt(seekDown ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    boolean scan = scan();
                    reply.writeNoException();
                    reply.writeInt(scan ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean updateStationList = updateStationList();
                    reply.writeNoException();
                    reply.writeInt(updateStationList ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean updateStationListByType = updateStationListByType(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(updateStationListByType ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    boolean tuneto = tuneto(_arg02, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(tuneto ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean tuneToAmFm = tuneToAmFm(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(tuneToAmFm ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean componentSeekUp = componentSeekUp();
                    reply.writeNoException();
                    reply.writeInt(componentSeekUp ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean componentSeekDown = componentSeekDown();
                    reply.writeNoException();
                    reply.writeInt(componentSeekDown ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stopComponentSeek = stopComponentSeek();
                    reply.writeNoException();
                    reply.writeInt(stopComponentSeek ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDabDabLinkOn = isDabDabLinkOn();
                    reply.writeNoException();
                    reply.writeInt(isDabDabLinkOn ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isDabFmLinkOn = isDabFmLinkOn();
                    reply.writeNoException();
                    reply.writeInt(isDabFmLinkOn ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isTpOn = isTpOn();
                    reply.writeNoException();
                    reply.writeInt(isTpOn ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean enterApp = enterApp();
                    reply.writeNoException();
                    reply.writeInt(enterApp ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    boolean exitApp = exitApp();
                    reply.writeNoException();
                    reply.writeInt(exitApp ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getLastSource();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getLinkSource();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    DabInfo _result3 = getCurrentDabInfo();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    rejectAnnouncement(data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    allowAnnouncement(data.readInt());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    cancelAnnouncement(data.readInt());
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    registerDabLisenter(IDabListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterDabLisenter(IDabListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setDabFmLinkOn(_arg0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setDabDabLinkOn(_arg0);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setTpOn(_arg0);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    reset();
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    getSnrLevel();
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    initDabChip();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IRadioServiceBase {
            public static IRadioServiceBase sDefaultImpl;
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

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean switchSourceTo(int source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().switchSourceTo(source);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean stepUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stepUp();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean stepDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stepDown();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean seekUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().seekUp();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean seekDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().seekDown();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean scan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().scan();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean updateStationList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateStationList();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean updateStationListByType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateStationListByType(type);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean tuneto(int freqId, int serviceId, int componentId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(freqId);
                    _data.writeInt(serviceId);
                    _data.writeInt(componentId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().tuneto(freqId, serviceId, componentId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean tuneToAmFm(int freqId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(freqId);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().tuneToAmFm(freqId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean componentSeekUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().componentSeekUp();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean componentSeekDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().componentSeekDown();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean stopComponentSeek() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopComponentSeek();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean isDabDabLinkOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDabDabLinkOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean isDabFmLinkOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isDabFmLinkOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean isTpOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTpOn();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean enterApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().enterApp();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public boolean exitApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().exitApp();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public int getLastSource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLastSource();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public int getLinkSource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLinkSource();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public DabInfo getCurrentDabInfo() throws RemoteException {
                DabInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCurrentDabInfo();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = DabInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void rejectAnnouncement(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().rejectAnnouncement(type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void allowAnnouncement(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().allowAnnouncement(type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void cancelAnnouncement(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAnnouncement(type);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void registerDabLisenter(IDabListener iDabListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iDabListener != null ? iDabListener.asBinder() : null);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerDabLisenter(iDabListener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void unregisterDabLisenter(IDabListener iDabListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(iDabListener != null ? iDabListener.asBinder() : null);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterDabLisenter(iDabListener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void setDabFmLinkOn(boolean isDabFmLinkOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isDabFmLinkOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDabFmLinkOn(isDabFmLinkOn);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void setDabDabLinkOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setDabDabLinkOn(isOn);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void setTpOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setTpOn(isOn);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reset();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void getSnrLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getSnrLevel();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IRadioServiceBase
            public void initDabChip() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initDabChip();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IRadioServiceBase impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IRadioServiceBase getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
