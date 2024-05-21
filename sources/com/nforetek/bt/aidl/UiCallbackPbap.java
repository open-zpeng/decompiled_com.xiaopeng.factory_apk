package com.nforetek.bt.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface UiCallbackPbap extends IInterface {
    void onPbapDownloadNotify(String str, int i, int i2, int i3) throws RemoteException;

    void onPbapServiceReady() throws RemoteException;

    void onPbapStateChanged(String str, int i, int i2, int i3, int i4) throws RemoteException;

    void retPbapCleanDatabaseCompleted(boolean z) throws RemoteException;

    void retPbapDatabaseAvailable(String str) throws RemoteException;

    void retPbapDatabaseQueryNameByNumber(String str, String str2, String str3, boolean z) throws RemoteException;

    void retPbapDatabaseQueryNameByPartialNumber(String str, String str2, String[] strArr, String[] strArr2, boolean z) throws RemoteException;

    void retPbapDeleteDatabaseByAddressCompleted(String str, boolean z) throws RemoteException;

    void retPbapDownloadedCallLog(String str, String str2, String str3, String str4, String str5, int i, String str6) throws RemoteException;

    void retPbapDownloadedContact(NfPbapContact nfPbapContact) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements UiCallbackPbap {
        private static final String DESCRIPTOR = "com.nforetek.bt.aidl.UiCallbackPbap";
        static final int TRANSACTION_onPbapDownloadNotify = 5;
        static final int TRANSACTION_onPbapServiceReady = 1;
        static final int TRANSACTION_onPbapStateChanged = 2;
        static final int TRANSACTION_retPbapCleanDatabaseCompleted = 10;
        static final int TRANSACTION_retPbapDatabaseAvailable = 8;
        static final int TRANSACTION_retPbapDatabaseQueryNameByNumber = 6;
        static final int TRANSACTION_retPbapDatabaseQueryNameByPartialNumber = 7;
        static final int TRANSACTION_retPbapDeleteDatabaseByAddressCompleted = 9;
        static final int TRANSACTION_retPbapDownloadedCallLog = 4;
        static final int TRANSACTION_retPbapDownloadedContact = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static UiCallbackPbap asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof UiCallbackPbap)) {
                return (UiCallbackPbap) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            NfPbapContact _arg0;
            boolean _arg02;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onPbapServiceReady();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    int _arg4 = data.readInt();
                    onPbapStateChanged(_arg03, _arg1, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (NfPbapContact) NfPbapContact.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    retPbapDownloadedContact(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg12 = data.readString();
                    String _arg22 = data.readString();
                    String _arg32 = data.readString();
                    String _arg42 = data.readString();
                    int _arg5 = data.readInt();
                    String _arg6 = data.readString();
                    retPbapDownloadedCallLog(_arg04, _arg12, _arg22, _arg32, _arg42, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    int _arg13 = data.readInt();
                    int _arg23 = data.readInt();
                    int _arg33 = data.readInt();
                    onPbapDownloadNotify(_arg05, _arg13, _arg23, _arg33);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg14 = data.readString();
                    String _arg24 = data.readString();
                    _arg02 = data.readInt() != 0;
                    retPbapDatabaseQueryNameByNumber(_arg06, _arg14, _arg24, _arg02);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg15 = data.readString();
                    String[] _arg25 = data.createStringArray();
                    String[] _arg34 = data.createStringArray();
                    boolean _arg43 = data.readInt() != 0;
                    retPbapDatabaseQueryNameByPartialNumber(_arg07, _arg15, _arg25, _arg34, _arg43);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    retPbapDatabaseAvailable(data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    _arg02 = data.readInt() != 0;
                    retPbapDeleteDatabaseByAddressCompleted(_arg08, _arg02);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg02 = data.readInt() != 0;
                    retPbapCleanDatabaseCompleted(_arg02);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements UiCallbackPbap {
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

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void onPbapServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prevState);
                    _data.writeInt(newState);
                    _data.writeInt(reason);
                    _data.writeInt(counts);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDownloadedContact(NfPbapContact contact) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (contact != null) {
                        _data.writeInt(1);
                        contact.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(firstName);
                    _data.writeString(middleName);
                    _data.writeString(lastName);
                    _data.writeString(number);
                    _data.writeInt(type);
                    _data.writeString(timestamp);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(totalContacts);
                    _data.writeInt(downloadedContacts);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(target);
                    _data.writeString(name);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(target);
                    _data.writeStringArray(names);
                    _data.writeStringArray(numbers);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDatabaseAvailable(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackPbap
            public void retPbapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
