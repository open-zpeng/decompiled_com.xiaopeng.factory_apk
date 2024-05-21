package com.xiaopeng.xpbluetoothservice.phonecontrol;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IPhoneServiceCallback extends IInterface {
    void onCallRecordCallback(int i, String str) throws RemoteException;

    void onMessageCallback(int i, Bundle bundle) throws RemoteException;

    void onPBAPCallback(int i, int i2, int i3, int i4, int i5, String str, String str2, String str3) throws RemoteException;

    void onPhoneCallback(int i, int i2, int i3, int i4, int i5, String str, String str2, String str3) throws RemoteException;

    void onSmsCallback(int i, int i2, int i3, int i4, String str, String str2, String str3) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IPhoneServiceCallback {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback";
        static final int TRANSACTION_onCallRecordCallback = 5;
        static final int TRANSACTION_onMessageCallback = 1;
        static final int TRANSACTION_onPBAPCallback = 3;
        static final int TRANSACTION_onPhoneCallback = 2;
        static final int TRANSACTION_onSmsCallback = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPhoneServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPhoneServiceCallback)) {
                return (IPhoneServiceCallback) iin;
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
                int _arg02 = data.readInt();
                int _arg12 = data.readInt();
                int _arg2 = data.readInt();
                int _arg3 = data.readInt();
                int _arg4 = data.readInt();
                String _arg5 = data.readString();
                String _arg6 = data.readString();
                String _arg7 = data.readString();
                onPhoneCallback(_arg02, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                int _arg13 = data.readInt();
                int _arg22 = data.readInt();
                int _arg32 = data.readInt();
                int _arg42 = data.readInt();
                String _arg52 = data.readString();
                String _arg62 = data.readString();
                String _arg72 = data.readString();
                onPBAPCallback(_arg03, _arg13, _arg22, _arg32, _arg42, _arg52, _arg62, _arg72);
                reply.writeNoException();
                return true;
            } else if (code != 4) {
                if (code != 5) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                }
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                String _arg14 = data.readString();
                onCallRecordCallback(_arg04, _arg14);
                reply.writeNoException();
                return true;
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg05 = data.readInt();
                int _arg15 = data.readInt();
                int _arg23 = data.readInt();
                int _arg33 = data.readInt();
                String _arg43 = data.readString();
                String _arg53 = data.readString();
                String _arg63 = data.readString();
                onSmsCallback(_arg05, _arg15, _arg23, _arg33, _arg43, _arg53, _arg63);
                reply.writeNoException();
                return true;
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IPhoneServiceCallback {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
            public void onPhoneCallback(int status, int dataType, int cur, int total, int error, String name, String number, String time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(dataType);
                    _data.writeInt(cur);
                    _data.writeInt(total);
                    _data.writeInt(error);
                    _data.writeString(name);
                    _data.writeString(number);
                    _data.writeString(time);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
            public void onPBAPCallback(int status, int dataType, int cur, int total, int error, String name, String number, String time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(dataType);
                    _data.writeInt(cur);
                    _data.writeInt(total);
                    _data.writeInt(error);
                    _data.writeString(name);
                    _data.writeString(number);
                    _data.writeString(time);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
            public void onSmsCallback(int status, int error, int cur, int total, String number, String date, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(error);
                    _data.writeInt(cur);
                    _data.writeInt(total);
                    _data.writeString(number);
                    _data.writeString(date);
                    _data.writeString(content);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback
            public void onCallRecordCallback(int callType, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callType);
                    _data.writeString(name);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
