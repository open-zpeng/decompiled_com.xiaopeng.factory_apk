package com.xiaopeng.xpbluetoothservice.phonecontrol;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.xpbluetoothservice.phonecontrol.IPhoneServiceCallback;
/* loaded from: classes2.dex */
public interface IBtPhoneControlService extends IInterface {
    boolean answerCall(String str) throws RemoteException;

    void changeDownLoadFlag(String str, boolean z) throws RemoteException;

    boolean deletePhote(String str, String str2) throws RemoteException;

    boolean dialDTMF(String str, String str2) throws RemoteException;

    boolean dialNumber(String str, String str2) throws RemoteException;

    void getContacts(String str, boolean z, int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void getContactsByPBAP(String str, int i, int i2, int i3, int i4, int i5, int i6) throws RemoteException;

    void getMessage(String str, boolean z, int i) throws RemoteException;

    int getMicMaxVolume(String str) throws RemoteException;

    int getMicVolume(String str) throws RemoteException;

    int getMissCallCount(String str) throws RemoteException;

    int getSpeakerMaxVolume(String str) throws RemoteException;

    int getSpeakerVolume(String str) throws RemoteException;

    boolean handleThreeWayCall(String str, int i) throws RemoteException;

    boolean hangUpCall(String str) throws RemoteException;

    boolean isCurrentDownloading(String str) throws RemoteException;

    boolean isForDownLoad(String str) throws RemoteException;

    boolean isSupportTransferAudio(String str) throws RemoteException;

    boolean queryCall(String str, String[] strArr, String[] strArr2, int[] iArr, String[] strArr3, String[] strArr4, int[] iArr2) throws RemoteException;

    boolean reDial(String str) throws RemoteException;

    Bitmap readBitmap(String str, String str2, int i, int i2) throws RemoteException;

    void registerCallback(IPhoneServiceCallback iPhoneServiceCallback) throws RemoteException;

    boolean sendMessage(String str, String str2, String str3) throws RemoteException;

    boolean setDownloadPBAttr(String str, int i) throws RemoteException;

    void setForDownLoad(String str) throws RemoteException;

    boolean setMicVolume(String str, int i) throws RemoteException;

    boolean setSpeakerVolume(String str, int i) throws RemoteException;

    boolean transferAudio(String str, boolean z) throws RemoteException;

    void unRegisterContactsByPBAP(String str) throws RemoteException;

    void unRegisterContactsCallback(String str) throws RemoteException;

    void unRegisterSMSCallback(String str) throws RemoteException;

    void unregisterCallback(IPhoneServiceCallback iPhoneServiceCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBtPhoneControlService {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService";
        static final int TRANSACTION_answerCall = 4;
        static final int TRANSACTION_changeDownLoadFlag = 30;
        static final int TRANSACTION_deletePhote = 18;
        static final int TRANSACTION_dialDTMF = 7;
        static final int TRANSACTION_dialNumber = 6;
        static final int TRANSACTION_getContacts = 14;
        static final int TRANSACTION_getContactsByPBAP = 19;
        static final int TRANSACTION_getMessage = 21;
        static final int TRANSACTION_getMicMaxVolume = 28;
        static final int TRANSACTION_getMicVolume = 25;
        static final int TRANSACTION_getMissCallCount = 13;
        static final int TRANSACTION_getSpeakerMaxVolume = 29;
        static final int TRANSACTION_getSpeakerVolume = 27;
        static final int TRANSACTION_handleThreeWayCall = 8;
        static final int TRANSACTION_hangUpCall = 5;
        static final int TRANSACTION_isCurrentDownloading = 3;
        static final int TRANSACTION_isForDownLoad = 32;
        static final int TRANSACTION_isSupportTransferAudio = 12;
        static final int TRANSACTION_queryCall = 10;
        static final int TRANSACTION_reDial = 9;
        static final int TRANSACTION_readBitmap = 17;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_sendMessage = 23;
        static final int TRANSACTION_setDownloadPBAttr = 16;
        static final int TRANSACTION_setForDownLoad = 31;
        static final int TRANSACTION_setMicVolume = 24;
        static final int TRANSACTION_setSpeakerVolume = 26;
        static final int TRANSACTION_transferAudio = 11;
        static final int TRANSACTION_unRegisterContactsByPBAP = 20;
        static final int TRANSACTION_unRegisterContactsCallback = 15;
        static final int TRANSACTION_unRegisterSMSCallback = 22;
        static final int TRANSACTION_unregisterCallback = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBtPhoneControlService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBtPhoneControlService)) {
                return (IBtPhoneControlService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String[] _arg1;
            String[] _arg2;
            int[] _arg3;
            String[] _arg4;
            String[] _arg5;
            String[] _arg52;
            int[] _arg6;
            boolean _arg12;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IPhoneServiceCallback _arg0 = IPhoneServiceCallback.Stub.asInterface(data.readStrongBinder());
                    registerCallback(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IPhoneServiceCallback _arg02 = IPhoneServiceCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    boolean isCurrentDownloading = isCurrentDownloading(_arg03);
                    reply.writeNoException();
                    reply.writeInt(isCurrentDownloading ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    boolean answerCall = answerCall(_arg04);
                    reply.writeNoException();
                    reply.writeInt(answerCall ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    boolean hangUpCall = hangUpCall(_arg05);
                    reply.writeNoException();
                    reply.writeInt(hangUpCall ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    boolean dialNumber = dialNumber(_arg06, data.readString());
                    reply.writeNoException();
                    reply.writeInt(dialNumber ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    boolean dialDTMF = dialDTMF(_arg07, data.readString());
                    reply.writeNoException();
                    reply.writeInt(dialDTMF ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    boolean handleThreeWayCall = handleThreeWayCall(_arg08, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(handleThreeWayCall ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    boolean reDial = reDial(_arg09);
                    reply.writeNoException();
                    reply.writeInt(reDial ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg1_length = data.readInt();
                    if (_arg1_length < 0) {
                        _arg1 = null;
                    } else {
                        _arg1 = new String[_arg1_length];
                    }
                    int _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        String[] _arg22 = new String[_arg2_length];
                        _arg2 = _arg22;
                    }
                    int _arg3_length = data.readInt();
                    if (_arg3_length < 0) {
                        _arg3 = null;
                    } else {
                        int[] _arg32 = new int[_arg3_length];
                        _arg3 = _arg32;
                    }
                    int _arg4_length = data.readInt();
                    if (_arg4_length < 0) {
                        _arg4 = null;
                    } else {
                        String[] _arg42 = new String[_arg4_length];
                        _arg4 = _arg42;
                    }
                    int _arg5_length = data.readInt();
                    if (_arg5_length < 0) {
                        _arg5 = null;
                    } else {
                        _arg5 = new String[_arg5_length];
                    }
                    int _arg6_length = data.readInt();
                    if (_arg6_length < 0) {
                        _arg52 = _arg5;
                        _arg6 = null;
                    } else {
                        _arg52 = _arg5;
                        _arg6 = new int[_arg6_length];
                    }
                    String[] _arg53 = _arg52;
                    int[] _arg62 = _arg6;
                    String[] _arg43 = _arg4;
                    String[] _arg44 = _arg1;
                    boolean queryCall = queryCall(_arg010, _arg44, _arg2, _arg3, _arg43, _arg53, _arg62);
                    reply.writeNoException();
                    reply.writeInt(queryCall ? 1 : 0);
                    reply.writeStringArray(_arg1);
                    reply.writeStringArray(_arg2);
                    reply.writeIntArray(_arg3);
                    reply.writeStringArray(_arg43);
                    reply.writeStringArray(_arg53);
                    reply.writeIntArray(_arg62);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    _arg12 = data.readInt() != 0;
                    boolean transferAudio = transferAudio(_arg011, _arg12);
                    reply.writeNoException();
                    reply.writeInt(transferAudio ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    boolean isSupportTransferAudio = isSupportTransferAudio(_arg012);
                    reply.writeNoException();
                    reply.writeInt(isSupportTransferAudio ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _result = getMissCallCount(_arg013);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    boolean _arg13 = data.readInt() != 0;
                    int _arg23 = data.readInt();
                    int _arg33 = data.readInt();
                    int _arg45 = data.readInt();
                    int _arg54 = data.readInt();
                    int _arg63 = data.readInt();
                    int _arg7 = data.readInt();
                    getContacts(_arg014, _arg13, _arg23, _arg33, _arg45, _arg54, _arg63, _arg7);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    unRegisterContactsCallback(_arg015);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    boolean downloadPBAttr = setDownloadPBAttr(_arg016, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(downloadPBAttr ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    String _arg14 = data.readString();
                    int _arg24 = data.readInt();
                    int _arg34 = data.readInt();
                    Bitmap _result2 = readBitmap(_arg017, _arg14, _arg24, _arg34);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    boolean deletePhote = deletePhote(_arg018, data.readString());
                    reply.writeNoException();
                    reply.writeInt(deletePhote ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg46 = data.readInt();
                    int _arg55 = data.readInt();
                    int _arg64 = data.readInt();
                    getContactsByPBAP(_arg019, _arg15, _arg25, _arg35, _arg46, _arg55, _arg64);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    unRegisterContactsByPBAP(_arg020);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    _arg12 = data.readInt() != 0;
                    int _arg26 = data.readInt();
                    getMessage(_arg021, _arg12, _arg26);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    unRegisterSMSCallback(_arg022);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    String _arg16 = data.readString();
                    String _arg27 = data.readString();
                    boolean sendMessage = sendMessage(_arg023, _arg16, _arg27);
                    reply.writeNoException();
                    reply.writeInt(sendMessage ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    boolean micVolume = setMicVolume(_arg024, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(micVolume ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    int _result3 = getMicVolume(_arg025);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    boolean speakerVolume = setSpeakerVolume(_arg026, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(speakerVolume ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    int _result4 = getSpeakerVolume(_arg027);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    int _result5 = getMicMaxVolume(_arg028);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    int _result6 = getSpeakerMaxVolume(_arg029);
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    _arg12 = data.readInt() != 0;
                    changeDownLoadFlag(_arg030, _arg12);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    setForDownLoad(_arg031);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    boolean isForDownLoad = isForDownLoad(_arg032);
                    reply.writeNoException();
                    reply.writeInt(isForDownLoad ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IBtPhoneControlService {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void registerCallback(IPhoneServiceCallback callback) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void unregisterCallback(IPhoneServiceCallback callback) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean isCurrentDownloading(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean answerCall(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean hangUpCall(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean dialNumber(String pUUid, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(number);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean dialDTMF(String pUUid, String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(number);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean handleThreeWayCall(String pUUid, int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(type);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean reDial(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean queryCall(String pUUid, String[] number1, String[] name1, int[] callStatus1, String[] number2, String[] name2, int[] callStatus2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    if (number1 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(number1.length);
                    }
                    if (name1 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(name1.length);
                    }
                    if (callStatus1 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(callStatus1.length);
                    }
                    if (number2 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(number2.length);
                    }
                    if (name2 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(name2.length);
                    }
                    if (callStatus2 == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(callStatus2.length);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.readStringArray(number1);
                    _reply.readStringArray(name1);
                    _reply.readIntArray(callStatus1);
                    _reply.readStringArray(number2);
                    _reply.readStringArray(name2);
                    _reply.readIntArray(callStatus2);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean transferAudio(String pUUid, boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(flag ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean isSupportTransferAudio(String pUUid) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public int getMissCallCount(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void getContacts(String pUUid, boolean refresh, int memtype, int nMax_ME, int nMax_SM, int nMax_RC, int nMax_MC, int nMax_DC) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeInt(memtype);
                    _data.writeInt(nMax_ME);
                    _data.writeInt(nMax_SM);
                    _data.writeInt(nMax_RC);
                    _data.writeInt(nMax_MC);
                    _data.writeInt(nMax_DC);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void unRegisterContactsCallback(String pUUid) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean setDownloadPBAttr(String pUUid, int attribute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(attribute);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public Bitmap readBitmap(String pUUid, String imgName, int width, int height) throws RemoteException {
                Bitmap _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(imgName);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean deletePhote(String pUUid, String imgName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(imgName);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void getContactsByPBAP(String pUUid, int memtype, int nMax_ME, int nMax_SM, int nMax_RC, int nMax_MC, int nMax_DC) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(memtype);
                    _data.writeInt(nMax_ME);
                    _data.writeInt(nMax_SM);
                    _data.writeInt(nMax_RC);
                    _data.writeInt(nMax_MC);
                    _data.writeInt(nMax_DC);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void unRegisterContactsByPBAP(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void getMessage(String pUUid, boolean refresh, int nMaxGet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeInt(nMaxGet);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void unRegisterSMSCallback(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean sendMessage(String pUUid, String number, String text) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(number);
                    _data.writeString(text);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean setMicVolume(String pUUid, int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(volume);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public int getMicVolume(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean setSpeakerVolume(String pUUid, int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(volume);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public int getSpeakerVolume(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public int getMicMaxVolume(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public int getSpeakerMaxVolume(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void changeDownLoadFlag(String pUUid, boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(flag ? 1 : 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public void setForDownLoad(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.phonecontrol.IBtPhoneControlService
            public boolean isForDownLoad(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(32, _data, _reply, 0);
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
