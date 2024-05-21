package com.xiaopeng.xpbluetoothservice.generalcontrol;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IGeneralServiceCallback extends IInterface {
    void onMessageCallback(int i, Bundle bundle) throws RemoteException;

    void onScanCallback(String str, String str2, int i, int i2, boolean z) throws RemoteException;

    void onSppDataCallback(int i, byte[] bArr, int i2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IGeneralServiceCallback {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback";
        static final int TRANSACTION_onMessageCallback = 1;
        static final int TRANSACTION_onScanCallback = 2;
        static final int TRANSACTION_onSppDataCallback = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGeneralServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGeneralServiceCallback)) {
                return (IGeneralServiceCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg1;
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onMessageCallback(_arg0, _arg1);
                reply.writeNoException();
                if (_arg1 != null) {
                    reply.writeInt(1);
                    _arg1.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                String _arg12 = data.readString();
                int _arg2 = data.readInt();
                int _arg3 = data.readInt();
                boolean _arg4 = data.readInt() != 0;
                onScanCallback(_arg02, _arg12, _arg2, _arg3, _arg4);
                reply.writeNoException();
                return true;
            } else if (code != 3) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                byte[] _arg13 = data.createByteArray();
                int _arg22 = data.readInt();
                onSppDataCallback(_arg03, _arg13, _arg22);
                reply.writeNoException();
                reply.writeByteArray(_arg13);
                return true;
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IGeneralServiceCallback {
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

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
            public void onMessageCallback(int action, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(action);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
            public void onScanCallback(String address, String deviceName, int cod, int rssi, boolean complete) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(deviceName);
                    _data.writeInt(cod);
                    _data.writeInt(rssi);
                    _data.writeInt(complete ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.generalcontrol.IGeneralServiceCallback
            public void onSppDataCallback(int index, byte[] data, int dataLength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(index);
                    _data.writeByteArray(data);
                    _data.writeInt(dataLength);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    _reply.readByteArray(data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
