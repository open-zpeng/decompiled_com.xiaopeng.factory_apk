package com.xiaopeng.ota;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IOTAListener extends IInterface {
    void onCampaign(String str) throws RemoteException;

    void onECUVersion(String str, String str2, String str3) throws RemoteException;

    void onUpgradeChanged(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IOTAListener {
        @Override // com.xiaopeng.ota.IOTAListener
        public void onECUVersion(String typeId, String typeName, String version) throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAListener
        public void onCampaign(String campaignJson) throws RemoteException {
        }

        @Override // com.xiaopeng.ota.IOTAListener
        public void onUpgradeChanged(String timeSting) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IOTAListener {
        private static final String DESCRIPTOR = "com.xiaopeng.ota.IOTAListener";
        static final int TRANSACTION_onCampaign = 2;
        static final int TRANSACTION_onECUVersion = 1;
        static final int TRANSACTION_onUpgradeChanged = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOTAListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOTAListener)) {
                return (IOTAListener) iin;
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
                String _arg1 = data.readString();
                String _arg2 = data.readString();
                onECUVersion(_arg0, _arg1, _arg2);
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                String _arg02 = data.readString();
                onCampaign(_arg02);
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
                String _arg03 = data.readString();
                onUpgradeChanged(_arg03);
                reply.writeNoException();
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IOTAListener {
            public static IOTAListener sDefaultImpl;
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

            @Override // com.xiaopeng.ota.IOTAListener
            public void onECUVersion(String typeId, String typeName, String version) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(typeId);
                    _data.writeString(typeName);
                    _data.writeString(version);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onECUVersion(typeId, typeName, version);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAListener
            public void onCampaign(String campaignJson) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(campaignJson);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onCampaign(campaignJson);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.ota.IOTAListener
            public void onUpgradeChanged(String timeSting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(timeSting);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUpgradeChanged(timeSting);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOTAListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IOTAListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
