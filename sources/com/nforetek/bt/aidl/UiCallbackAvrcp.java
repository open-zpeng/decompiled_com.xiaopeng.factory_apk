package com.nforetek.bt.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface UiCallbackAvrcp extends IInterface {
    void onAvrcp13EventBatteryStatusChanged(byte b) throws RemoteException;

    void onAvrcp13EventPlaybackPosChanged(long j) throws RemoteException;

    void onAvrcp13EventPlaybackStatusChanged(byte b) throws RemoteException;

    void onAvrcp13EventPlayerSettingChanged(byte[] bArr, byte[] bArr2) throws RemoteException;

    void onAvrcp13EventSystemStatusChanged(byte b) throws RemoteException;

    void onAvrcp13EventTrackChanged(long j) throws RemoteException;

    void onAvrcp13EventTrackReachedEnd() throws RemoteException;

    void onAvrcp13EventTrackReachedStart() throws RemoteException;

    void onAvrcp13RegisterEventWatcherFail(byte b) throws RemoteException;

    void onAvrcp13RegisterEventWatcherSuccess(byte b) throws RemoteException;

    void onAvrcp14EventAddressedPlayerChanged(int i, int i2) throws RemoteException;

    void onAvrcp14EventAvailablePlayerChanged() throws RemoteException;

    void onAvrcp14EventNowPlayingContentChanged() throws RemoteException;

    void onAvrcp14EventUidsChanged(int i) throws RemoteException;

    void onAvrcp14EventVolumeChanged(byte b) throws RemoteException;

    void onAvrcpErrorResponse(int i, int i2, byte b) throws RemoteException;

    void onAvrcpServiceReady() throws RemoteException;

    void onAvrcpStateChanged(String str, int i, int i2) throws RemoteException;

    void retAvrcp13CapabilitiesSupportEvent(byte[] bArr) throws RemoteException;

    void retAvrcp13ElementAttributesPlaying(int[] iArr, String[] strArr) throws RemoteException;

    void retAvrcp13PlayStatus(long j, long j2, byte b) throws RemoteException;

    void retAvrcp13PlayerSettingAttributesList(byte[] bArr) throws RemoteException;

    void retAvrcp13PlayerSettingCurrentValues(byte[] bArr, byte[] bArr2) throws RemoteException;

    void retAvrcp13PlayerSettingValuesList(byte b, byte[] bArr) throws RemoteException;

    void retAvrcp13SetPlayerSettingValueSuccess() throws RemoteException;

    void retAvrcp14AddToNowPlayingSuccess() throws RemoteException;

    void retAvrcp14ChangePathSuccess(long j) throws RemoteException;

    void retAvrcp14FolderItems(int i, long j) throws RemoteException;

    void retAvrcp14ItemAttributes(int[] iArr, String[] strArr) throws RemoteException;

    void retAvrcp14MediaItems(int i, long j) throws RemoteException;

    void retAvrcp14PlaySelectedItemSuccess() throws RemoteException;

    void retAvrcp14SearchResult(int i, long j) throws RemoteException;

    void retAvrcp14SetAbsoluteVolumeSuccess(byte b) throws RemoteException;

    void retAvrcp14SetAddressedPlayerSuccess() throws RemoteException;

    void retAvrcp14SetBrowsedPlayerSuccess(String[] strArr, int i, long j) throws RemoteException;

    void retAvrcpUpdateSongStatus(String str, String str2, String str3) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements UiCallbackAvrcp {
        private static final String DESCRIPTOR = "com.nforetek.bt.aidl.UiCallbackAvrcp";
        static final int TRANSACTION_onAvrcp13EventBatteryStatusChanged = 17;
        static final int TRANSACTION_onAvrcp13EventPlaybackPosChanged = 16;
        static final int TRANSACTION_onAvrcp13EventPlaybackStatusChanged = 12;
        static final int TRANSACTION_onAvrcp13EventPlayerSettingChanged = 19;
        static final int TRANSACTION_onAvrcp13EventSystemStatusChanged = 18;
        static final int TRANSACTION_onAvrcp13EventTrackChanged = 13;
        static final int TRANSACTION_onAvrcp13EventTrackReachedEnd = 14;
        static final int TRANSACTION_onAvrcp13EventTrackReachedStart = 15;
        static final int TRANSACTION_onAvrcp13RegisterEventWatcherFail = 11;
        static final int TRANSACTION_onAvrcp13RegisterEventWatcherSuccess = 10;
        static final int TRANSACTION_onAvrcp14EventAddressedPlayerChanged = 22;
        static final int TRANSACTION_onAvrcp14EventAvailablePlayerChanged = 21;
        static final int TRANSACTION_onAvrcp14EventNowPlayingContentChanged = 20;
        static final int TRANSACTION_onAvrcp14EventUidsChanged = 23;
        static final int TRANSACTION_onAvrcp14EventVolumeChanged = 24;
        static final int TRANSACTION_onAvrcpErrorResponse = 35;
        static final int TRANSACTION_onAvrcpServiceReady = 1;
        static final int TRANSACTION_onAvrcpStateChanged = 2;
        static final int TRANSACTION_retAvrcp13CapabilitiesSupportEvent = 3;
        static final int TRANSACTION_retAvrcp13ElementAttributesPlaying = 8;
        static final int TRANSACTION_retAvrcp13PlayStatus = 9;
        static final int TRANSACTION_retAvrcp13PlayerSettingAttributesList = 4;
        static final int TRANSACTION_retAvrcp13PlayerSettingCurrentValues = 6;
        static final int TRANSACTION_retAvrcp13PlayerSettingValuesList = 5;
        static final int TRANSACTION_retAvrcp13SetPlayerSettingValueSuccess = 7;
        static final int TRANSACTION_retAvrcp14AddToNowPlayingSuccess = 33;
        static final int TRANSACTION_retAvrcp14ChangePathSuccess = 29;
        static final int TRANSACTION_retAvrcp14FolderItems = 27;
        static final int TRANSACTION_retAvrcp14ItemAttributes = 30;
        static final int TRANSACTION_retAvrcp14MediaItems = 28;
        static final int TRANSACTION_retAvrcp14PlaySelectedItemSuccess = 31;
        static final int TRANSACTION_retAvrcp14SearchResult = 32;
        static final int TRANSACTION_retAvrcp14SetAbsoluteVolumeSuccess = 34;
        static final int TRANSACTION_retAvrcp14SetAddressedPlayerSuccess = 25;
        static final int TRANSACTION_retAvrcp14SetBrowsedPlayerSuccess = 26;
        static final int TRANSACTION_retAvrcpUpdateSongStatus = 36;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static UiCallbackAvrcp asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof UiCallbackAvrcp)) {
                return (UiCallbackAvrcp) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onAvrcpServiceReady();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg2 = data.readInt();
                    onAvrcpStateChanged(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg02 = data.createByteArray();
                    retAvrcp13CapabilitiesSupportEvent(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg03 = data.createByteArray();
                    retAvrcp13PlayerSettingAttributesList(_arg03);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg04 = data.readByte();
                    byte[] _arg12 = data.createByteArray();
                    retAvrcp13PlayerSettingValuesList(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg05 = data.createByteArray();
                    byte[] _arg13 = data.createByteArray();
                    retAvrcp13PlayerSettingCurrentValues(_arg05, _arg13);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    retAvrcp13SetPlayerSettingValueSuccess();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg06 = data.createIntArray();
                    String[] _arg14 = data.createStringArray();
                    retAvrcp13ElementAttributesPlaying(_arg06, _arg14);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg07 = data.readLong();
                    long _arg15 = data.readLong();
                    byte _arg22 = data.readByte();
                    retAvrcp13PlayStatus(_arg07, _arg15, _arg22);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg08 = data.readByte();
                    onAvrcp13RegisterEventWatcherSuccess(_arg08);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg09 = data.readByte();
                    onAvrcp13RegisterEventWatcherFail(_arg09);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg010 = data.readByte();
                    onAvrcp13EventPlaybackStatusChanged(_arg010);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg011 = data.readLong();
                    onAvrcp13EventTrackChanged(_arg011);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onAvrcp13EventTrackReachedEnd();
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    onAvrcp13EventTrackReachedStart();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg012 = data.readLong();
                    onAvrcp13EventPlaybackPosChanged(_arg012);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg013 = data.readByte();
                    onAvrcp13EventBatteryStatusChanged(_arg013);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg014 = data.readByte();
                    onAvrcp13EventSystemStatusChanged(_arg014);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg015 = data.createByteArray();
                    byte[] _arg16 = data.createByteArray();
                    onAvrcp13EventPlayerSettingChanged(_arg015, _arg16);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    onAvrcp14EventNowPlayingContentChanged();
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    onAvrcp14EventAvailablePlayerChanged();
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    int _arg17 = data.readInt();
                    onAvrcp14EventAddressedPlayerChanged(_arg016, _arg17);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    onAvrcp14EventUidsChanged(_arg017);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg018 = data.readByte();
                    onAvrcp14EventVolumeChanged(_arg018);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    retAvrcp14SetAddressedPlayerSuccess();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg019 = data.createStringArray();
                    int _arg18 = data.readInt();
                    long _arg23 = data.readLong();
                    retAvrcp14SetBrowsedPlayerSuccess(_arg019, _arg18, _arg23);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    long _arg19 = data.readLong();
                    retAvrcp14FolderItems(_arg020, _arg19);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    long _arg110 = data.readLong();
                    retAvrcp14MediaItems(_arg021, _arg110);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg022 = data.readLong();
                    retAvrcp14ChangePathSuccess(_arg022);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int[] _arg023 = data.createIntArray();
                    String[] _arg111 = data.createStringArray();
                    retAvrcp14ItemAttributes(_arg023, _arg111);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    retAvrcp14PlaySelectedItemSuccess();
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    long _arg112 = data.readLong();
                    retAvrcp14SearchResult(_arg024, _arg112);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    retAvrcp14AddToNowPlayingSuccess();
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg025 = data.readByte();
                    retAvrcp14SetAbsoluteVolumeSuccess(_arg025);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    int _arg113 = data.readInt();
                    byte _arg24 = data.readByte();
                    onAvrcpErrorResponse(_arg026, _arg113, _arg24);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    String _arg114 = data.readString();
                    String _arg25 = data.readString();
                    retAvrcpUpdateSongStatus(_arg027, _arg114, _arg25);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements UiCallbackAvrcp {
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

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcpServiceReady() throws RemoteException {
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

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(prevState);
                    _data.writeInt(newState);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(eventIds);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(attributeIds);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(attributeId);
                    _data.writeByteArray(valueIds);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(attributeIds);
                    _data.writeByteArray(valueIds);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13SetPlayerSettingValueSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(metadataAtrributeIds);
                    _data.writeStringArray(texts);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(songLen);
                    _data.writeLong(songPos);
                    _data.writeByte(statusId);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(eventId);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13RegisterEventWatcherFail(byte eventId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(eventId);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventPlaybackStatusChanged(byte statusId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(statusId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventTrackChanged(long elementId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(elementId);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventTrackReachedEnd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventTrackReachedStart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventPlaybackPosChanged(long songPos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(songPos);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventBatteryStatusChanged(byte statusId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(statusId);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventSystemStatusChanged(byte statusId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(statusId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(attributeIds);
                    _data.writeByteArray(valueIds);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp14EventNowPlayingContentChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp14EventAvailablePlayerChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(playerId);
                    _data.writeInt(uidCounter);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp14EventUidsChanged(int uidCounter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidCounter);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcp14EventVolumeChanged(byte volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(volume);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14SetAddressedPlayerSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(path);
                    _data.writeInt(uidCounter);
                    _data.writeLong(itemCount);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14FolderItems(int uidCounter, long itemCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidCounter);
                    _data.writeLong(itemCount);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14MediaItems(int uidCounter, long itemCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidCounter);
                    _data.writeLong(itemCount);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14ChangePathSuccess(long itemCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(itemCount);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(metadataAtrributeIds);
                    _data.writeStringArray(texts);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14PlaySelectedItemSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14SearchResult(int uidCounter, long itemCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidCounter);
                    _data.writeLong(itemCount);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14AddToNowPlayingSuccess() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(volume);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void onAvrcpErrorResponse(int opId, int reason, byte eventId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(opId);
                    _data.writeInt(reason);
                    _data.writeByte(eventId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
            public void retAvrcpUpdateSongStatus(String artist, String album, String title) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(artist);
                    _data.writeString(album);
                    _data.writeString(title);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
