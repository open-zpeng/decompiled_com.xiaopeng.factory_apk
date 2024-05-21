package com.nforetek.bt.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface UiCallbackMap extends IInterface {
    void onMapDownloadNotify(String str, int i, int i2, int i3) throws RemoteException;

    void onMapMemoryAvailableEvent(String str, int i, boolean z) throws RemoteException;

    void onMapMessageDeletedEvent(String str, String str2, int i, int i2) throws RemoteException;

    void onMapMessageDeliverStatusEvent(String str, String str2, boolean z) throws RemoteException;

    void onMapMessageSendingStatusEvent(String str, String str2, boolean z) throws RemoteException;

    void onMapMessageShiftedEvent(String str, String str2, int i, int i2, int i3) throws RemoteException;

    void onMapNewMessageReceivedEvent(String str, String str2, String str3, String str4) throws RemoteException;

    void onMapServiceReady() throws RemoteException;

    void onMapStateChanged(String str, int i, int i2, int i3) throws RemoteException;

    void retMapChangeReadStatusCompleted(String str, String str2, int i) throws RemoteException;

    void retMapCleanDatabaseCompleted(boolean z) throws RemoteException;

    void retMapDatabaseAvailable() throws RemoteException;

    void retMapDeleteDatabaseByAddressCompleted(String str, boolean z) throws RemoteException;

    void retMapDeleteMessageCompleted(String str, String str2, int i) throws RemoteException;

    void retMapDownloadedMessage(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, boolean z, String str7, String str8) throws RemoteException;

    void retMapSendMessageCompleted(String str, String str2, int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements UiCallbackMap {
        private static final String DESCRIPTOR = "com.nforetek.bt.aidl.UiCallbackMap";
        static final int TRANSACTION_onMapDownloadNotify = 5;
        static final int TRANSACTION_onMapMemoryAvailableEvent = 12;
        static final int TRANSACTION_onMapMessageDeletedEvent = 16;
        static final int TRANSACTION_onMapMessageDeliverStatusEvent = 14;
        static final int TRANSACTION_onMapMessageSendingStatusEvent = 13;
        static final int TRANSACTION_onMapMessageShiftedEvent = 15;
        static final int TRANSACTION_onMapNewMessageReceivedEvent = 4;
        static final int TRANSACTION_onMapServiceReady = 1;
        static final int TRANSACTION_onMapStateChanged = 2;
        static final int TRANSACTION_retMapChangeReadStatusCompleted = 11;
        static final int TRANSACTION_retMapCleanDatabaseCompleted = 8;
        static final int TRANSACTION_retMapDatabaseAvailable = 6;
        static final int TRANSACTION_retMapDeleteDatabaseByAddressCompleted = 7;
        static final int TRANSACTION_retMapDeleteMessageCompleted = 10;
        static final int TRANSACTION_retMapDownloadedMessage = 3;
        static final int TRANSACTION_retMapSendMessageCompleted = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static UiCallbackMap asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof UiCallbackMap)) {
                return (UiCallbackMap) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onMapServiceReady();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg22 = data.readInt();
                    int _arg3 = data.readInt();
                    onMapStateChanged(_arg0, _arg1, _arg22, _arg3);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg12 = data.readString();
                    String _arg23 = data.readString();
                    String _arg32 = data.readString();
                    String _arg4 = data.readString();
                    String _arg5 = data.readString();
                    int _arg6 = data.readInt();
                    int _arg7 = data.readInt();
                    boolean _arg8 = data.readInt() != 0;
                    String _arg9 = data.readString();
                    String _arg10 = data.readString();
                    retMapDownloadedMessage(_arg02, _arg12, _arg23, _arg32, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg13 = data.readString();
                    String _arg24 = data.readString();
                    String _arg33 = data.readString();
                    onMapNewMessageReceivedEvent(_arg03, _arg13, _arg24, _arg33);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg34 = data.readInt();
                    onMapDownloadNotify(_arg04, _arg14, _arg25, _arg34);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    retMapDatabaseAvailable();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    _arg2 = data.readInt() != 0;
                    retMapDeleteDatabaseByAddressCompleted(_arg05, _arg2);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    retMapCleanDatabaseCompleted(_arg2);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg15 = data.readString();
                    retMapSendMessageCompleted(_arg06, _arg15, data.readInt());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg16 = data.readString();
                    retMapDeleteMessageCompleted(_arg07, _arg16, data.readInt());
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    String _arg17 = data.readString();
                    retMapChangeReadStatusCompleted(_arg08, _arg17, data.readInt());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg18 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    onMapMemoryAvailableEvent(_arg09, _arg18, _arg2);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    String _arg19 = data.readString();
                    _arg2 = data.readInt() != 0;
                    onMapMessageSendingStatusEvent(_arg010, _arg19, _arg2);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    String _arg110 = data.readString();
                    _arg2 = data.readInt() != 0;
                    onMapMessageDeliverStatusEvent(_arg011, _arg110, _arg2);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _arg111 = data.readString();
                    int _arg26 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg42 = data.readInt();
                    onMapMessageShiftedEvent(_arg012, _arg111, _arg26, _arg35, _arg42);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    String _arg112 = data.readString();
                    int _arg27 = data.readInt();
                    int _arg36 = data.readInt();
                    onMapMessageDeletedEvent(_arg013, _arg112, _arg27, _arg36);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements UiCallbackMap {
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

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapServiceReady() throws RemoteException {
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

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapStateChanged(String address, int prevState, int newState, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prevState);
                    _data.writeInt(newState);
                    _data.writeInt(reason);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapDownloadedMessage(String address, String handle, String senderName, String senderNumber, String recipientNumber, String date, int type, int folder, boolean isReadStatus, String subject, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(address);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeString(handle);
                    try {
                        _data.writeString(senderName);
                        try {
                            _data.writeString(senderNumber);
                            try {
                                _data.writeString(recipientNumber);
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(date);
                        try {
                            _data.writeInt(type);
                            try {
                                _data.writeInt(folder);
                                _data.writeInt(isReadStatus ? 1 : 0);
                                try {
                                    _data.writeString(subject);
                                    try {
                                        _data.writeString(message);
                                        try {
                                            this.mRemote.transact(3, _data, _reply, 0);
                                            _reply.readException();
                                            _reply.recycle();
                                            _data.recycle();
                                        } catch (Throwable th6) {
                                            th = th6;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th9) {
                                th = th9;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapNewMessageReceivedEvent(String address, String handle, String sender, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeString(sender);
                    _data.writeString(message);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapDownloadNotify(String address, int folder, int totalMessages, int currentMessages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(folder);
                    _data.writeInt(totalMessages);
                    _data.writeInt(currentMessages);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapDatabaseAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapSendMessageCompleted(String address, String target, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(target);
                    _data.writeInt(state);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapDeleteMessageCompleted(String address, String handle, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(reason);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void retMapChangeReadStatusCompleted(String address, String handle, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(reason);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapMemoryAvailableEvent(String address, int structure, boolean available) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(structure);
                    _data.writeInt(available ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapMessageSendingStatusEvent(String address, String handle, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapMessageDeliverStatusEvent(String address, String handle, boolean isSuccess) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(isSuccess ? 1 : 0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapMessageShiftedEvent(String address, String handle, int type, int newFolder, int oldFolder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(type);
                    _data.writeInt(newFolder);
                    _data.writeInt(oldFolder);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackMap
            public void onMapMessageDeletedEvent(String address, String handle, int type, int folder) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(handle);
                    _data.writeInt(type);
                    _data.writeInt(folder);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
