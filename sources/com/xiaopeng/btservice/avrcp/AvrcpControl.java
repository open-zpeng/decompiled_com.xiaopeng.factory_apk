package com.xiaopeng.btservice.avrcp;

import android.content.Context;
import com.nforetek.bt.aidl.UiCallbackAvrcp;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsAvrcpControlCallback;
import com.xiaopeng.btservice.base.AbsControl;
/* loaded from: classes.dex */
public class AvrcpControl extends AbsControl {
    private static final String TAG = "AvrcpControl";
    private AbsAvrcpControlCallback mCallback;
    private UiCallbackAvrcp mCallbackAvrcp = new UiCallbackAvrcp.Stub() { // from class: com.xiaopeng.btservice.avrcp.AvrcpControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcpServiceReady() {
            AvrcpControl.this.mCallback.onAvrcpServiceReady();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcpStateChanged(String address, int prevState, int newState) {
            AvrcpControl.this.mCallback.onAvrcpStateChanged(address, prevState, newState);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) {
            AvrcpControl.this.mCallback.retAvrcp13PlayerSettingValuesList(attributeId, valueIds);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) {
            AvrcpControl.this.mCallback.retAvrcp13PlayerSettingCurrentValues(attributeIds, valueIds);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13SetPlayerSettingValueSuccess() {
            AvrcpControl.this.mCallback.retAvrcp13SetPlayerSettingValueSuccess();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13ElementAttributesPlaying(int[] metadataAttributeIds, String[] texts) {
            AvrcpControl.this.mCallback.retAvrcp13ElementAttributesPlaying(metadataAttributeIds, texts);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) {
            AvrcpControl.this.mCallback.retAvrcp13PlayStatus(songLen, songPos, statusId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) {
            AvrcpControl.this.mCallback.onAvrcp13RegisterEventWatcherSuccess(eventId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13RegisterEventWatcherFail(byte eventId) {
            AvrcpControl.this.mCallback.onAvrcp13RegisterEventWatcherFail(eventId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventPlaybackStatusChanged(byte statusId) {
            AvrcpControl.this.mCallback.onAvrcp13EventPlaybackStatusChanged(statusId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventTrackChanged(long elementId) {
            AvrcpControl.this.mCallback.onAvrcp13EventTrackChanged(elementId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventTrackReachedEnd() {
            AvrcpControl.this.mCallback.onAvrcp13EventTrackReachedEnd();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventTrackReachedStart() {
            AvrcpControl.this.mCallback.onAvrcp13EventTrackReachedStart();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventPlaybackPosChanged(long songPos) {
            AvrcpControl.this.mCallback.onAvrcp13EventPlaybackPosChanged(songPos);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventBatteryStatusChanged(byte statusId) {
            AvrcpControl.this.mCallback.onAvrcp13EventBatteryStatusChanged(statusId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventSystemStatusChanged(byte statusId) {
            AvrcpControl.this.mCallback.onAvrcp13EventSystemStatusChanged(statusId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) {
            AvrcpControl.this.mCallback.onAvrcp13EventPlayerSettingChanged(attributeIds, valueIds);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp14EventNowPlayingContentChanged() {
            AvrcpControl.this.mCallback.onAvrcp14EventNowPlayingContentChanged();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp14EventAvailablePlayerChanged() {
            AvrcpControl.this.mCallback.onAvrcp14EventAvailablePlayerChanged();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) {
            AvrcpControl.this.mCallback.onAvrcp14EventAddressedPlayerChanged(playerId, uidCounter);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp14EventUidsChanged(int uidCounter) {
            AvrcpControl.this.mCallback.onAvrcp14EventUidsChanged(uidCounter);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcp14EventVolumeChanged(byte volume) {
            AvrcpControl.this.mCallback.onAvrcp14EventVolumeChanged(volume);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14SetAddressedPlayerSuccess() {
            AvrcpControl.this.mCallback.retAvrcp14SetAddressedPlayerSuccess();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) {
            AvrcpControl.this.mCallback.retAvrcp14SetBrowsedPlayerSuccess(path, uidCounter, itemCount);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14FolderItems(int uidCounter, long itemCount) {
            AvrcpControl.this.mCallback.retAvrcp14FolderItems(uidCounter, itemCount);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14MediaItems(int uidCounter, long itemCount) {
            AvrcpControl.this.mCallback.retAvrcp14MediaItems(uidCounter, itemCount);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14ChangePathSuccess(long itemCount) {
            AvrcpControl.this.mCallback.retAvrcp14ChangePathSuccess(itemCount);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) {
            AvrcpControl.this.mCallback.retAvrcp14ItemAttributes(metadataAtrributeIds, texts);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14PlaySelectedItemSuccess() {
            AvrcpControl.this.mCallback.retAvrcp14PlaySelectedItemSuccess();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14SearchResult(int uidCounter, long itemCount) {
            AvrcpControl.this.mCallback.retAvrcp14SearchResult(uidCounter, itemCount);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14AddToNowPlayingSuccess() {
            AvrcpControl.this.mCallback.retAvrcp14AddToNowPlayingSuccess();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) {
            AvrcpControl.this.mCallback.retAvrcp14SetAbsoluteVolumeSuccess(volume);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void onAvrcpErrorResponse(int opId, int reason, byte eventId) {
            AvrcpControl.this.mCallback.onAvrcpErrorResponse(opId, reason, eventId);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) {
            AvrcpControl.this.mCallback.retAvrcp13CapabilitiesSupportEvent(eventIds);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) {
            AvrcpControl.this.mCallback.retAvrcp13PlayerSettingAttributesList(attributeIds);
        }

        @Override // com.nforetek.bt.aidl.UiCallbackAvrcp
        public void retAvrcpUpdateSongStatus(String artist, String album, String title) {
            AvrcpControl.this.mCallback.retAvrcpUpdateSongStatus(artist, album, title);
        }
    };

    public AvrcpControl(Context mContext, AbsAvrcpControlCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            btService.registerAvrcpCallback(this.mCallbackAvrcp);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            if (this.nForeService != null) {
                this.nForeService.unregisterAvrcpCallback(this.mCallbackAvrcp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.isAvrcpServiceReady();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean isConnected() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.isAvrcpConnected();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public int getConnectionState(String address) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.getAvrcpConnectionState();
            } catch (Exception e) {
                e.printStackTrace();
                return 100;
            }
        }
        return 100;
    }

    public boolean connect(String address) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpConnect(address);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean disconnect(String address) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpDisconnect(address);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public String getConnectedAddress() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.getAvrcpConnectedAddress();
            } catch (Exception e) {
                e.printStackTrace();
                return "00:00:00:00:00:00";
            }
        }
        return "00:00:00:00:00:00";
    }

    public boolean is13Supported(String address) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.isAvrcp13Supported(address);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean is14Supported(String address) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.isAvrcp14Supported(address);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean play() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpPlay();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean stop() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpStop();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean pause() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpPause();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean forward() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpForward();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean backward() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpBackward();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean volumeUp() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpVolumeUp();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean volumeDown() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpVolumeDown();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean startFastForward() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpStartFastForward();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean stopFastForward() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpStopFastForward();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean startRewind() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpStartRewind();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean stopRewind() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpStopRewind();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getCapabilitiesSupportEvents() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetCapabilitiesSupportEvent();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getPlayerSettingAttributesList() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetPlayerSettingAttributesList();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getPlayerSettingValuesList(byte attributeId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetPlayerSettingValuesList(attributeId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getPlayerSettingCurrentValues() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetPlayerSettingCurrentValues();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean setPlayerSettingValue(byte attributeId, byte valueId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13SetPlayerSettingValue(attributeId, valueId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getElementAttributesPlaying() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetElementAttributesPlaying();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getPlayStatus() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13GetPlayStatus();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean registerEventWatcher(byte eventId, long interval) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpRegisterEventWatcher(eventId, interval);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean unregisterEventWatcher(byte eventId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcpUnregisterEventWatcher(eventId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean nextGroup() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13NextGroup();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean previousGroup() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp13PreviousGroup();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean browsingChannelEstablished() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.isAvrcp14BrowsingChannelEstablished();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean setAddressedPlayer(int playerId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14SetAddressedPlayer(playerId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean setBrowsedPlayer(int playerId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14SetBrowsedPlayer(playerId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getFolderItems(byte scopeId) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14GetFolderItems(scopeId);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean changePath(int uidCounter, long uid, byte direction) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14ChangePath(uidCounter, uid, direction);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean getItemAttributes(byte scopeId, int uidCounter, long uid) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14GetItemAttributes(scopeId, uidCounter, uid);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean requestPlaySelectedItem(byte scopeId, int uidCounter, long uid) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14PlaySelectedItem(scopeId, uidCounter, uid);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean search(String text) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14Search(text);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean addToNowPlaying(byte scopeId, int uidCounter, long uid) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14AddToNowPlaying(scopeId, uidCounter, uid);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean setAbsoluteVolume(byte volume) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqAvrcp14SetAbsoluteVolume(volume);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
