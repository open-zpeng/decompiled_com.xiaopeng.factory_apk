package com.xiaopeng.xpbluetoothservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService;
import com.xiaopeng.xpbluetoothservice.generalcontrol.IBtGeneralControlService;
import com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService;
/* loaded from: classes2.dex */
public interface IXPBluetoothService extends IInterface {
    IBtA2DPControlService getBtA2DPControlService() throws RemoteException;

    IBtGeneralControlService getBtGeneralControlService() throws RemoteException;

    IBtPhoneControlService getBtPhoneControlService() throws RemoteException;

    String registerName(String str) throws RemoteException;

    void unRegisterName(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IXPBluetoothService {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.IXPBluetoothService";
        static final int TRANSACTION_getBtA2DPControlService = 4;
        static final int TRANSACTION_getBtGeneralControlService = 3;
        static final int TRANSACTION_getBtPhoneControlService = 5;
        static final int TRANSACTION_registerName = 1;
        static final int TRANSACTION_unRegisterName = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IXPBluetoothService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IXPBluetoothService)) {
                return (IXPBluetoothService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                String _result = registerName(_arg0);
                reply.writeNoException();
                reply.writeString(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                unRegisterName(_arg02);
                reply.writeNoException();
                return true;
            } else {
                if (code == 3) {
                    data.enforceInterface(DESCRIPTOR);
                    IBtGeneralControlService _result2 = getBtGeneralControlService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                    return true;
                } else if (code == 4) {
                    data.enforceInterface(DESCRIPTOR);
                    IBtA2DPControlService _result3 = getBtA2DPControlService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3 != null ? _result3.asBinder() : null);
                    return true;
                } else if (code != 5) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                } else {
                    data.enforceInterface(DESCRIPTOR);
                    IBtPhoneControlService _result4 = getBtPhoneControlService();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result4 != null ? _result4.asBinder() : null);
                    return true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IXPBluetoothService {
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

            @Override // com.xiaopeng.xpbluetoothservice.IXPBluetoothService
            public String registerName(String pName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.IXPBluetoothService
            public void unRegisterName(String pUUID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUID);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.IXPBluetoothService
            public IBtGeneralControlService getBtGeneralControlService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    IBtGeneralControlService _result = IBtGeneralControlService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.IXPBluetoothService
            public IBtA2DPControlService getBtA2DPControlService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    IBtA2DPControlService _result = IBtA2DPControlService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.IXPBluetoothService
            public IBtPhoneControlService getBtPhoneControlService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    IBtPhoneControlService _result = IBtPhoneControlService.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
