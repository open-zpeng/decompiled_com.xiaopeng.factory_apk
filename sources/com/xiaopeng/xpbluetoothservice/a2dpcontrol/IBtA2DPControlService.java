package com.xiaopeng.xpbluetoothservice.a2dpcontrol;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.xpbluetoothservice.a2dpcontrol.IA2DPServiceCallback;
/* loaded from: classes2.dex */
public interface IBtA2DPControlService extends IInterface {
    boolean addToPlayingQueue(String str, int i, String str2) throws RemoteException;

    boolean controlAVRCPEx(String str, int i, int i2) throws RemoteException;

    String getAlbumName(String str) throws RemoteException;

    String getArtistName(String str) throws RemoteException;

    boolean getBrowsingSupportFeature(String str) throws RemoteException;

    void getMediaInfoEx(String str, int i) throws RemoteException;

    boolean getPlayStatus(String str) throws RemoteException;

    boolean getPlayerSettings(String str) throws RemoteException;

    String getSongName(String str) throws RemoteException;

    int getSongTimes(String str) throws RemoteException;

    int getStreamMode(String str) throws RemoteException;

    boolean isA2DPSupportMetaData(String str) throws RemoteException;

    boolean isBrowsingBusy(String str) throws RemoteException;

    boolean isCurrentA2DPPlaying(String str) throws RemoteException;

    boolean pause(String str) throws RemoteException;

    boolean play(String str) throws RemoteException;

    boolean playBrowsingItem(String str, int i, String str2) throws RemoteException;

    boolean playNext(String str) throws RemoteException;

    boolean playPrevious(String str) throws RemoteException;

    void registerCallback(IA2DPServiceCallback iA2DPServiceCallback) throws RemoteException;

    boolean retrievePlayerSettings(String str, int i) throws RemoteException;

    boolean retrievePlayerSupported(String str, int i, int[] iArr, int i2) throws RemoteException;

    boolean setPlayerSettings(String str, int i, int i2) throws RemoteException;

    boolean setStreamMode(String str, int i) throws RemoteException;

    boolean setStreamVolume(String str, float f) throws RemoteException;

    void startBrowsingElement(String str, String str2, int i, int i2) throws RemoteException;

    void startBrowsingElementEx(String str, String str2, int i, int i2, int i3) throws RemoteException;

    void startBrowsingSearch(String str, String str2, String str3, int i, int i2) throws RemoteException;

    void startBrowsingSearchEx(String str, String str2, String str3, int i, int i2, int i3) throws RemoteException;

    void startPlayingList(String str, int i, int i2) throws RemoteException;

    void startPlayingListEx(String str, int i, int i2, int i3) throws RemoteException;

    boolean stop(String str) throws RemoteException;

    void unregisterCallback(IA2DPServiceCallback iA2DPServiceCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IBtA2DPControlService {
        private static final String DESCRIPTOR = "com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService";
        static final int TRANSACTION_addToPlayingQueue = 28;
        static final int TRANSACTION_controlAVRCPEx = 9;
        static final int TRANSACTION_getAlbumName = 13;
        static final int TRANSACTION_getArtistName = 12;
        static final int TRANSACTION_getBrowsingSupportFeature = 30;
        static final int TRANSACTION_getMediaInfoEx = 15;
        static final int TRANSACTION_getPlayStatus = 16;
        static final int TRANSACTION_getPlayerSettings = 17;
        static final int TRANSACTION_getSongName = 11;
        static final int TRANSACTION_getSongTimes = 14;
        static final int TRANSACTION_getStreamMode = 32;
        static final int TRANSACTION_isA2DPSupportMetaData = 10;
        static final int TRANSACTION_isBrowsingBusy = 29;
        static final int TRANSACTION_isCurrentA2DPPlaying = 3;
        static final int TRANSACTION_pause = 6;
        static final int TRANSACTION_play = 4;
        static final int TRANSACTION_playBrowsingItem = 27;
        static final int TRANSACTION_playNext = 7;
        static final int TRANSACTION_playPrevious = 8;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_retrievePlayerSettings = 18;
        static final int TRANSACTION_retrievePlayerSupported = 20;
        static final int TRANSACTION_setPlayerSettings = 19;
        static final int TRANSACTION_setStreamMode = 31;
        static final int TRANSACTION_setStreamVolume = 33;
        static final int TRANSACTION_startBrowsingElement = 21;
        static final int TRANSACTION_startBrowsingElementEx = 22;
        static final int TRANSACTION_startBrowsingSearch = 23;
        static final int TRANSACTION_startBrowsingSearchEx = 24;
        static final int TRANSACTION_startPlayingList = 25;
        static final int TRANSACTION_startPlayingListEx = 26;
        static final int TRANSACTION_stop = 5;
        static final int TRANSACTION_unregisterCallback = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBtA2DPControlService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IBtA2DPControlService)) {
                return (IBtA2DPControlService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int[] _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IA2DPServiceCallback _arg0 = IA2DPServiceCallback.Stub.asInterface(data.readStrongBinder());
                    registerCallback(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IA2DPServiceCallback _arg02 = IA2DPServiceCallback.Stub.asInterface(data.readStrongBinder());
                    unregisterCallback(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    boolean isCurrentA2DPPlaying = isCurrentA2DPPlaying(_arg03);
                    reply.writeNoException();
                    reply.writeInt(isCurrentA2DPPlaying ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    boolean play = play(_arg04);
                    reply.writeNoException();
                    reply.writeInt(play ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    boolean stop = stop(_arg05);
                    reply.writeNoException();
                    reply.writeInt(stop ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    boolean pause = pause(_arg06);
                    reply.writeNoException();
                    reply.writeInt(pause ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    boolean playNext = playNext(_arg07);
                    reply.writeNoException();
                    reply.writeInt(playNext ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    boolean playPrevious = playPrevious(_arg08);
                    reply.writeNoException();
                    reply.writeInt(playPrevious ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    int _arg1 = data.readInt();
                    int _arg22 = data.readInt();
                    boolean controlAVRCPEx = controlAVRCPEx(_arg09, _arg1, _arg22);
                    reply.writeNoException();
                    reply.writeInt(controlAVRCPEx ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    boolean isA2DPSupportMetaData = isA2DPSupportMetaData(_arg010);
                    reply.writeNoException();
                    reply.writeInt(isA2DPSupportMetaData ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    String _result = getSongName(_arg011);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _result2 = getArtistName(_arg012);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    String _result3 = getAlbumName(_arg013);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _result4 = getSongTimes(_arg014);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    int _arg12 = data.readInt();
                    getMediaInfoEx(_arg015, _arg12);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    boolean playStatus = getPlayStatus(_arg016);
                    reply.writeNoException();
                    reply.writeInt(playStatus ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    boolean playerSettings = getPlayerSettings(_arg017);
                    reply.writeNoException();
                    reply.writeInt(playerSettings ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg13 = data.readInt();
                    boolean retrievePlayerSettings = retrievePlayerSettings(_arg018, _arg13);
                    reply.writeNoException();
                    reply.writeInt(retrievePlayerSettings ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    int _arg14 = data.readInt();
                    int _arg23 = data.readInt();
                    boolean playerSettings2 = setPlayerSettings(_arg019, _arg14, _arg23);
                    reply.writeNoException();
                    reply.writeInt(playerSettings2 ? 1 : 0);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    int _arg15 = data.readInt();
                    int _arg2_length = data.readInt();
                    if (_arg2_length < 0) {
                        _arg2 = null;
                    } else {
                        _arg2 = new int[_arg2_length];
                    }
                    int _arg3 = data.readInt();
                    boolean retrievePlayerSupported = retrievePlayerSupported(_arg020, _arg15, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeInt(retrievePlayerSupported ? 1 : 0);
                    reply.writeIntArray(_arg2);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    String _arg16 = data.readString();
                    int _arg24 = data.readInt();
                    int _arg32 = data.readInt();
                    startBrowsingElement(_arg021, _arg16, _arg24, _arg32);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    String _arg17 = data.readString();
                    int _arg25 = data.readInt();
                    int _arg33 = data.readInt();
                    int _arg4 = data.readInt();
                    startBrowsingElementEx(_arg022, _arg17, _arg25, _arg33, _arg4);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    String _arg18 = data.readString();
                    String _arg26 = data.readString();
                    int _arg34 = data.readInt();
                    int _arg42 = data.readInt();
                    startBrowsingSearch(_arg023, _arg18, _arg26, _arg34, _arg42);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    String _arg19 = data.readString();
                    String _arg27 = data.readString();
                    int _arg35 = data.readInt();
                    int _arg43 = data.readInt();
                    int _arg5 = data.readInt();
                    startBrowsingSearchEx(_arg024, _arg19, _arg27, _arg35, _arg43, _arg5);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    int _arg110 = data.readInt();
                    int _arg28 = data.readInt();
                    startPlayingList(_arg025, _arg110, _arg28);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg36 = data.readInt();
                    startPlayingListEx(_arg026, _arg111, _arg29, _arg36);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    int _arg112 = data.readInt();
                    String _arg210 = data.readString();
                    boolean playBrowsingItem = playBrowsingItem(_arg027, _arg112, _arg210);
                    reply.writeNoException();
                    reply.writeInt(playBrowsingItem ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    int _arg113 = data.readInt();
                    String _arg211 = data.readString();
                    boolean addToPlayingQueue = addToPlayingQueue(_arg028, _arg113, _arg211);
                    reply.writeNoException();
                    reply.writeInt(addToPlayingQueue ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    boolean isBrowsingBusy = isBrowsingBusy(_arg029);
                    reply.writeNoException();
                    reply.writeInt(isBrowsingBusy ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    boolean browsingSupportFeature = getBrowsingSupportFeature(_arg030);
                    reply.writeNoException();
                    reply.writeInt(browsingSupportFeature ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    int _arg114 = data.readInt();
                    boolean streamMode = setStreamMode(_arg031, _arg114);
                    reply.writeNoException();
                    reply.writeInt(streamMode ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    int _result5 = getStreamMode(_arg032);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    float _arg115 = data.readFloat();
                    boolean streamVolume = setStreamVolume(_arg033, _arg115);
                    reply.writeNoException();
                    reply.writeInt(streamVolume ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IBtA2DPControlService {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void registerCallback(IA2DPServiceCallback callback) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void unregisterCallback(IA2DPServiceCallback callback) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean isCurrentA2DPPlaying(String pUUid) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean play(String pUUid) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean stop(String pUUid) throws RemoteException {
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

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean pause(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean playNext(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean playPrevious(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean controlAVRCPEx(String pUUid, int code, int actFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(code);
                    _data.writeInt(actFlag);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean isA2DPSupportMetaData(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public String getSongName(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public String getArtistName(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public String getAlbumName(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public int getSongTimes(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void getMediaInfoEx(String pUUid, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(mask);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean getPlayStatus(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean getPlayerSettings(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean retrievePlayerSettings(String pUUid, int attribute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(attribute);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean setPlayerSettings(String pUUid, int attribute, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(attribute);
                    _data.writeInt(value);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean retrievePlayerSupported(String pUUid, int attribute, int[] allowArray, int arraySize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(attribute);
                    if (allowArray == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(allowArray.length);
                    }
                    _data.writeInt(arraySize);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    _reply.readIntArray(allowArray);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startBrowsingElement(String pUUid, String path, int mask, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(path);
                    _data.writeInt(mask);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startBrowsingElementEx(String pUUid, String path, int mask, int startPos, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(path);
                    _data.writeInt(mask);
                    _data.writeInt(startPos);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startBrowsingSearch(String pUUid, String path, String keyword, int mask, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(path);
                    _data.writeString(keyword);
                    _data.writeInt(mask);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startBrowsingSearchEx(String pUUid, String path, String keyword, int mask, int startPos, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeString(path);
                    _data.writeString(keyword);
                    _data.writeInt(mask);
                    _data.writeInt(startPos);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startPlayingList(String pUUid, int mask, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(mask);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public void startPlayingListEx(String pUUid, int mask, int startPos, int maxGetCount) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(mask);
                    _data.writeInt(startPos);
                    _data.writeInt(maxGetCount);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean playBrowsingItem(String pUUid, int scope, String uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(scope);
                    _data.writeString(uuid);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean addToPlayingQueue(String pUUid, int scope, String uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(scope);
                    _data.writeString(uuid);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean isBrowsingBusy(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean getBrowsingSupportFeature(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean setStreamMode(String pUUid, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeInt(mode);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public int getStreamMode(String pUUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.xpbluetoothservice.a2dpcontrol.IBtA2DPControlService
            public boolean setStreamVolume(String pUUid, float volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pUUid);
                    _data.writeFloat(volume);
                    this.mRemote.transact(33, _data, _reply, 0);
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
