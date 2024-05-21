package xp.hardware.dab;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface IDabListener extends IInterface {
    void onAnnouncementEnterCompleted(int i) throws RemoteException;

    void onAnnouncementEnterRequest(int i) throws RemoteException;

    void onAnnouncementExit(int i) throws RemoteException;

    void onDabChipReady() throws RemoteException;

    void onDabInfoChanged(DabInfo dabInfo) throws RemoteException;

    void onProgramListChanged(int i, List<DabInfo> list) throws RemoteException;

    void onSlideShowChanged(int i, String str) throws RemoteException;

    void onSnrChanged(int i, int i2) throws RemoteException;

    void onStationListChanged(int i, List<DabInfo> list) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IDabListener {
        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementEnterRequest(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementEnterCompleted(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onAnnouncementExit(int type) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onDabInfoChanged(DabInfo dabInfo) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onStationListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onProgramListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onSlideShowChanged(int source, String content) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onSnrChanged(int source, int snr) throws RemoteException {
        }

        @Override // xp.hardware.dab.IDabListener
        public void onDabChipReady() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IDabListener {
        private static final String DESCRIPTOR = "xp.hardware.dab.IDabListener";
        static final int TRANSACTION_onAnnouncementEnterCompleted = 2;
        static final int TRANSACTION_onAnnouncementEnterRequest = 1;
        static final int TRANSACTION_onAnnouncementExit = 3;
        static final int TRANSACTION_onDabChipReady = 9;
        static final int TRANSACTION_onDabInfoChanged = 4;
        static final int TRANSACTION_onProgramListChanged = 6;
        static final int TRANSACTION_onSlideShowChanged = 7;
        static final int TRANSACTION_onSnrChanged = 8;
        static final int TRANSACTION_onStationListChanged = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDabListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDabListener)) {
                return (IDabListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DabInfo _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    onAnnouncementEnterRequest(_arg02);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    onAnnouncementEnterCompleted(_arg03);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    onAnnouncementExit(_arg04);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = DabInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onDabInfoChanged(_arg0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    List<DabInfo> _arg1 = data.createTypedArrayList(DabInfo.CREATOR);
                    onStationListChanged(_arg05, _arg1);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    List<DabInfo> _arg12 = data.createTypedArrayList(DabInfo.CREATOR);
                    onProgramListChanged(_arg06, _arg12);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg13 = data.readString();
                    onSlideShowChanged(_arg07, _arg13);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg14 = data.readInt();
                    onSnrChanged(_arg08, _arg14);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onDabChipReady();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IDabListener {
            public static IDabListener sDefaultImpl;
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

            @Override // xp.hardware.dab.IDabListener
            public void onAnnouncementEnterRequest(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAnnouncementEnterRequest(type);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onAnnouncementEnterCompleted(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAnnouncementEnterCompleted(type);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onAnnouncementExit(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAnnouncementExit(type);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onDabInfoChanged(DabInfo dabInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dabInfo != null) {
                        _data.writeInt(1);
                        dabInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDabInfoChanged(dabInfo);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onStationListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    _data.writeTypedList(arrayList);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStationListChanged(source, arrayList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onProgramListChanged(int source, List<DabInfo> arrayList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    _data.writeTypedList(arrayList);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onProgramListChanged(source, arrayList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onSlideShowChanged(int source, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    _data.writeString(content);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSlideShowChanged(source, content);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onSnrChanged(int source, int snr) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    _data.writeInt(snr);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSnrChanged(source, snr);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // xp.hardware.dab.IDabListener
            public void onDabChipReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onDabChipReady();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDabListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDabListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
