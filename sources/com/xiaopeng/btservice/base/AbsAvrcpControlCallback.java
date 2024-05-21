package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsAvrcpControlCallback {
    public void onAvrcpServiceReady() {
    }

    public void onAvrcpStateChanged(String address, int prevState, int newState) {
    }

    public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) {
    }

    public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) {
    }

    public void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) {
    }

    public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) {
    }

    public void retAvrcp13SetPlayerSettingValueSuccess() {
    }

    public void retAvrcp13ElementAttributesPlaying(int[] metadataAttributeIds, String[] texts) {
    }

    public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) {
    }

    public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) {
    }

    public void onAvrcp13RegisterEventWatcherFail(byte eventId) {
    }

    public void onAvrcp13EventPlaybackStatusChanged(byte statusId) {
    }

    public void onAvrcp13EventTrackChanged(long elementId) {
    }

    public void onAvrcp13EventTrackReachedEnd() {
    }

    public void onAvrcp13EventTrackReachedStart() {
    }

    public void onAvrcp13EventPlaybackPosChanged(long songPos) {
    }

    public void onAvrcp13EventBatteryStatusChanged(byte statusId) {
    }

    public void onAvrcp13EventSystemStatusChanged(byte statusId) {
    }

    public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) {
    }

    public void onAvrcp14EventNowPlayingContentChanged() {
    }

    public void onAvrcp14EventAvailablePlayerChanged() {
    }

    public void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) {
    }

    public void onAvrcp14EventUidsChanged(int uidCounter) {
    }

    public void onAvrcp14EventVolumeChanged(byte volume) {
    }

    public void retAvrcp14SetAddressedPlayerSuccess() {
    }

    public void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) {
    }

    public void retAvrcp14FolderItems(int uidCounter, long itemCount) {
    }

    public void retAvrcp14MediaItems(int uidCounter, long itemCount) {
    }

    public void retAvrcp14ChangePathSuccess(long itemCount) {
    }

    public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) {
    }

    public void retAvrcp14PlaySelectedItemSuccess() {
    }

    public void retAvrcp14SearchResult(int uidCounter, long itemCount) {
    }

    public void retAvrcp14AddToNowPlayingSuccess() {
    }

    public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) {
    }

    public void onAvrcpErrorResponse(int opId, int reason, byte eventId) {
    }

    public void retAvrcpUpdateSongStatus(String artist, String album, String title) {
    }
}
