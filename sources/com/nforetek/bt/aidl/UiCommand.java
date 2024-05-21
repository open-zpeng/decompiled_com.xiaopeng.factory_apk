package com.nforetek.bt.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import com.nforetek.bt.aidl.UiCallbackA2dp;
import com.nforetek.bt.aidl.UiCallbackAvrcp;
import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.aidl.UiCallbackGattServer;
import com.nforetek.bt.aidl.UiCallbackHfp;
import com.nforetek.bt.aidl.UiCallbackHid;
import com.nforetek.bt.aidl.UiCallbackMap;
import com.nforetek.bt.aidl.UiCallbackOpp;
import com.nforetek.bt.aidl.UiCallbackPbap;
import com.nforetek.bt.aidl.UiCallbackSpp;
import com.nforetek.bt.aidl.UiCallbackUsbBluetooth;
import java.util.List;
/* loaded from: classes.dex */
public interface UiCommand extends IInterface {
    boolean cancelBtDiscovery() throws RemoteException;

    String getA2dpConnectedAddress() throws RemoteException;

    int getA2dpConnectionState() throws RemoteException;

    int getA2dpStreamType() throws RemoteException;

    String getAvrcpConnectedAddress() throws RemoteException;

    int getAvrcpConnectionState() throws RemoteException;

    int getBtAutoConnectCondition() throws RemoteException;

    int getBtAutoConnectPeriod() throws RemoteException;

    int getBtAutoConnectState() throws RemoteException;

    String getBtAutoConnectingAddress() throws RemoteException;

    String getBtLocalAddress() throws RemoteException;

    String getBtLocalName() throws RemoteException;

    String getBtRemoteDeviceName(String str) throws RemoteException;

    int getBtRemoteUuids(String str) throws RemoteException;

    int getBtRoleMode() throws RemoteException;

    int getBtState() throws RemoteException;

    List<ParcelUuid> getGattAddedGattCharacteristicUuidList(ParcelUuid parcelUuid) throws RemoteException;

    List<ParcelUuid> getGattAddedGattDescriptorUuidList(ParcelUuid parcelUuid, ParcelUuid parcelUuid2) throws RemoteException;

    List<ParcelUuid> getGattAddedGattServiceUuidList() throws RemoteException;

    int getGattServerConnectionState() throws RemoteException;

    int getHfpAudioConnectionState() throws RemoteException;

    List<NfHfpClientCall> getHfpCallList() throws RemoteException;

    String getHfpConnectedAddress() throws RemoteException;

    int getHfpConnectionState() throws RemoteException;

    int getHfpRemoteBatteryIndicator() throws RemoteException;

    String getHfpRemoteNetworkOperator() throws RemoteException;

    int getHfpRemoteSignalStrength() throws RemoteException;

    String getHfpRemoteSubscriberNumber() throws RemoteException;

    String getHidConnectedAddress() throws RemoteException;

    int getHidConnectionState() throws RemoteException;

    int getLocalUsbConnectionState() throws RemoteException;

    int getMapCurrentState(String str) throws RemoteException;

    int getMapRegisterState(String str) throws RemoteException;

    String getNfServiceVersionName() throws RemoteException;

    String getOppFilePath() throws RemoteException;

    int getPbapConnectionState() throws RemoteException;

    String getPbapDownloadingAddress() throws RemoteException;

    int getProfileConnectionState(int i) throws RemoteException;

    String getTargetAddress() throws RemoteException;

    String getUiServiceVersionName() throws RemoteException;

    String getUsbAddress() throws RemoteException;

    String getUsbBtLocalName() throws RemoteException;

    String getUsbConnectedDevice() throws RemoteException;

    int getUsbConnectionState(String str) throws RemoteException;

    boolean isA2dpConnected() throws RemoteException;

    boolean isA2dpServiceReady() throws RemoteException;

    boolean isAvrcp13Supported(String str) throws RemoteException;

    boolean isAvrcp14BrowsingChannelEstablished() throws RemoteException;

    boolean isAvrcp14Supported(String str) throws RemoteException;

    boolean isAvrcpConnected() throws RemoteException;

    boolean isAvrcpServiceReady() throws RemoteException;

    boolean isBluetoothServiceReady() throws RemoteException;

    boolean isBtAutoConnectEnable() throws RemoteException;

    boolean isBtDiscoverable() throws RemoteException;

    boolean isBtDiscovering() throws RemoteException;

    boolean isBtEnabled() throws RemoteException;

    boolean isGattServiceReady() throws RemoteException;

    boolean isHfpConnected() throws RemoteException;

    boolean isHfpInBandRingtoneSupport() throws RemoteException;

    boolean isHfpMicMute() throws RemoteException;

    boolean isHfpRemoteOnRoaming() throws RemoteException;

    boolean isHfpRemoteTelecomServiceOn() throws RemoteException;

    boolean isHfpRemoteVoiceDialOn() throws RemoteException;

    boolean isHfpServiceReady() throws RemoteException;

    boolean isHidConnected() throws RemoteException;

    boolean isHidServiceReady() throws RemoteException;

    boolean isMapNotificationRegistered(String str) throws RemoteException;

    boolean isMapServiceReady() throws RemoteException;

    boolean isOppServiceReady() throws RemoteException;

    boolean isPbapDownloading() throws RemoteException;

    boolean isPbapServiceReady() throws RemoteException;

    boolean isSppConnected(String str) throws RemoteException;

    boolean isSppServiceReady() throws RemoteException;

    boolean isUsbA2dpConnected(String str) throws RemoteException;

    boolean isUsbAvrcpConnected(String str) throws RemoteException;

    boolean isUsbDiscovering() throws RemoteException;

    boolean isUsbEnabled() throws RemoteException;

    boolean isUsbHfpConnected(String str) throws RemoteException;

    void muteHfpMic(boolean z) throws RemoteException;

    void pauseA2dpRender() throws RemoteException;

    void pauseHfpRender() throws RemoteException;

    boolean registerA2dpCallback(UiCallbackA2dp uiCallbackA2dp) throws RemoteException;

    boolean registerAvrcpCallback(UiCallbackAvrcp uiCallbackAvrcp) throws RemoteException;

    boolean registerBtCallback(UiCallbackBluetooth uiCallbackBluetooth) throws RemoteException;

    boolean registerGattServerCallback(UiCallbackGattServer uiCallbackGattServer) throws RemoteException;

    boolean registerHfpCallback(UiCallbackHfp uiCallbackHfp) throws RemoteException;

    boolean registerHidCallback(UiCallbackHid uiCallbackHid) throws RemoteException;

    boolean registerMapCallback(UiCallbackMap uiCallbackMap) throws RemoteException;

    boolean registerOppCallback(UiCallbackOpp uiCallbackOpp) throws RemoteException;

    boolean registerPbapCallback(UiCallbackPbap uiCallbackPbap) throws RemoteException;

    boolean registerSppCallback(UiCallbackSpp uiCallbackSpp) throws RemoteException;

    boolean registerUsbCallback(UiCallbackUsbBluetooth uiCallbackUsbBluetooth) throws RemoteException;

    boolean reqA2dpConnect(String str) throws RemoteException;

    boolean reqA2dpDisconnect(String str) throws RemoteException;

    boolean reqAvrcp13GetCapabilitiesSupportEvent() throws RemoteException;

    boolean reqAvrcp13GetElementAttributesPlaying() throws RemoteException;

    boolean reqAvrcp13GetPlayStatus() throws RemoteException;

    boolean reqAvrcp13GetPlayerSettingAttributesList() throws RemoteException;

    boolean reqAvrcp13GetPlayerSettingCurrentValues() throws RemoteException;

    boolean reqAvrcp13GetPlayerSettingValuesList(byte b) throws RemoteException;

    boolean reqAvrcp13NextGroup() throws RemoteException;

    boolean reqAvrcp13PreviousGroup() throws RemoteException;

    boolean reqAvrcp13SetPlayerSettingValue(byte b, byte b2) throws RemoteException;

    boolean reqAvrcp14AddToNowPlaying(byte b, int i, long j) throws RemoteException;

    boolean reqAvrcp14ChangePath(int i, long j, byte b) throws RemoteException;

    boolean reqAvrcp14GetFolderItems(byte b) throws RemoteException;

    boolean reqAvrcp14GetItemAttributes(byte b, int i, long j) throws RemoteException;

    boolean reqAvrcp14PlaySelectedItem(byte b, int i, long j) throws RemoteException;

    boolean reqAvrcp14Search(String str) throws RemoteException;

    boolean reqAvrcp14SetAbsoluteVolume(byte b) throws RemoteException;

    boolean reqAvrcp14SetAddressedPlayer(int i) throws RemoteException;

    boolean reqAvrcp14SetBrowsedPlayer(int i) throws RemoteException;

    boolean reqAvrcpBackward() throws RemoteException;

    boolean reqAvrcpConnect(String str) throws RemoteException;

    boolean reqAvrcpDisconnect(String str) throws RemoteException;

    boolean reqAvrcpForward() throws RemoteException;

    boolean reqAvrcpPause() throws RemoteException;

    boolean reqAvrcpPlay() throws RemoteException;

    boolean reqAvrcpRegisterEventWatcher(byte b, long j) throws RemoteException;

    boolean reqAvrcpStartFastForward() throws RemoteException;

    boolean reqAvrcpStartRewind() throws RemoteException;

    boolean reqAvrcpStop() throws RemoteException;

    boolean reqAvrcpStopFastForward() throws RemoteException;

    boolean reqAvrcpStopRewind() throws RemoteException;

    boolean reqAvrcpUnregisterEventWatcher(byte b) throws RemoteException;

    void reqAvrcpUpdateSongStatus() throws RemoteException;

    boolean reqAvrcpVolumeDown() throws RemoteException;

    boolean reqAvrcpVolumeUp() throws RemoteException;

    int reqBtConnectHfpA2dp(String str) throws RemoteException;

    int reqBtDisconnectAll() throws RemoteException;

    boolean reqBtPair(String str) throws RemoteException;

    boolean reqBtPairedDevices() throws RemoteException;

    boolean reqBtUnpair(String str) throws RemoteException;

    boolean reqGattServerAddCharacteristic(ParcelUuid parcelUuid, int i, int i2) throws RemoteException;

    boolean reqGattServerAddDescriptor(ParcelUuid parcelUuid, int i) throws RemoteException;

    boolean reqGattServerBeginServiceDeclaration(int i, ParcelUuid parcelUuid) throws RemoteException;

    boolean reqGattServerClearServices() throws RemoteException;

    boolean reqGattServerDisconnect(String str) throws RemoteException;

    boolean reqGattServerEndServiceDeclaration() throws RemoteException;

    boolean reqGattServerListen(boolean z) throws RemoteException;

    boolean reqGattServerRemoveService(int i, ParcelUuid parcelUuid) throws RemoteException;

    boolean reqGattServerSendNotification(String str, int i, ParcelUuid parcelUuid, ParcelUuid parcelUuid2, boolean z, byte[] bArr) throws RemoteException;

    boolean reqGattServerSendResponse(String str, int i, int i2, int i3, byte[] bArr) throws RemoteException;

    boolean reqHfpAnswerCall(int i) throws RemoteException;

    boolean reqHfpAudioTransferToCarkit() throws RemoteException;

    boolean reqHfpAudioTransferToPhone() throws RemoteException;

    boolean reqHfpConnect(String str) throws RemoteException;

    boolean reqHfpDialCall(String str) throws RemoteException;

    boolean reqHfpDisconnect(String str) throws RemoteException;

    boolean reqHfpMemoryDial(String str) throws RemoteException;

    boolean reqHfpReDial() throws RemoteException;

    boolean reqHfpRejectIncomingCall() throws RemoteException;

    boolean reqHfpSendDtmf(String str) throws RemoteException;

    boolean reqHfpTerminateCurrentCall() throws RemoteException;

    boolean reqHfpVoiceDial(boolean z) throws RemoteException;

    boolean reqHidConnect(String str) throws RemoteException;

    boolean reqHidDisconnect(String str) throws RemoteException;

    boolean reqMapChangeReadStatus(String str, int i, String str2, boolean z) throws RemoteException;

    void reqMapCleanDatabase() throws RemoteException;

    void reqMapDatabaseAvailable() throws RemoteException;

    void reqMapDeleteDatabaseByAddress(String str) throws RemoteException;

    boolean reqMapDeleteMessage(String str, int i, String str2) throws RemoteException;

    boolean reqMapDownloadInterrupt(String str) throws RemoteException;

    boolean reqMapDownloadMessage(String str, int i, boolean z, int i2, int i3, int i4, String str2, String str3, String str4, String str5, int i5, int i6) throws RemoteException;

    boolean reqMapDownloadSingleMessage(String str, int i, String str2, int i2) throws RemoteException;

    boolean reqMapRegisterNotification(String str, boolean z) throws RemoteException;

    boolean reqMapSendMessage(String str, String str2, String str3) throws RemoteException;

    void reqMapUnregisterNotification(String str) throws RemoteException;

    boolean reqOppAcceptReceiveFile(boolean z) throws RemoteException;

    boolean reqOppInterruptReceiveFile() throws RemoteException;

    void reqPbapCleanDatabase() throws RemoteException;

    void reqPbapDatabaseAvailable(String str) throws RemoteException;

    void reqPbapDatabaseQueryNameByNumber(String str, String str2) throws RemoteException;

    void reqPbapDatabaseQueryNameByPartialNumber(String str, String str2, int i) throws RemoteException;

    void reqPbapDeleteDatabaseByAddress(String str) throws RemoteException;

    boolean reqPbapDownload(String str, int i, int i2) throws RemoteException;

    boolean reqPbapDownloadInterrupt(String str) throws RemoteException;

    boolean reqPbapDownloadRange(String str, int i, int i2, int i3, int i4) throws RemoteException;

    boolean reqPbapDownloadRangeToContactsProvider(String str, int i, int i2, int i3, int i4) throws RemoteException;

    boolean reqPbapDownloadRangeToDatabase(String str, int i, int i2, int i3, int i4) throws RemoteException;

    boolean reqPbapDownloadToContactsProvider(String str, int i, int i2) throws RemoteException;

    boolean reqPbapDownloadToDatabase(String str, int i, int i2) throws RemoteException;

    boolean reqSendHidMouseCommand(int i, int i2, int i3, int i4) throws RemoteException;

    boolean reqSendHidVirtualKeyCommand(int i, int i2) throws RemoteException;

    boolean reqSppConnect(String str) throws RemoteException;

    void reqSppConnectedDeviceAddressList() throws RemoteException;

    boolean reqSppDisconnect(String str) throws RemoteException;

    void reqSppSendData(String str, byte[] bArr) throws RemoteException;

    List<UsbBluetoothDevice> reqUsbBtPairedDevices() throws RemoteException;

    boolean setA2dpLocalVolume(float f) throws RemoteException;

    boolean setA2dpStreamType(int i) throws RemoteException;

    void setBtAutoConnect(int i, int i2) throws RemoteException;

    boolean setBtDiscoverableTimeout(int i) throws RemoteException;

    boolean setBtEnable(boolean z) throws RemoteException;

    boolean setBtLocalName(String str) throws RemoteException;

    boolean setMapDownloadNotify(int i) throws RemoteException;

    boolean setOppFilePath(String str) throws RemoteException;

    boolean setPbapDownloadNotify(int i) throws RemoteException;

    void setTargetAddress(String str) throws RemoteException;

    boolean setUsbBtLocalName(String str) throws RemoteException;

    boolean setUsbEnabled(boolean z) throws RemoteException;

    void startA2dpRender() throws RemoteException;

    boolean startBtDiscovery() throws RemoteException;

    void startHfpRender() throws RemoteException;

    boolean switchBtRoleMode(int i) throws RemoteException;

    boolean unregisterA2dpCallback(UiCallbackA2dp uiCallbackA2dp) throws RemoteException;

    boolean unregisterAvrcpCallback(UiCallbackAvrcp uiCallbackAvrcp) throws RemoteException;

    boolean unregisterBtCallback(UiCallbackBluetooth uiCallbackBluetooth) throws RemoteException;

    boolean unregisterGattServerCallback(UiCallbackGattServer uiCallbackGattServer) throws RemoteException;

    boolean unregisterHfpCallback(UiCallbackHfp uiCallbackHfp) throws RemoteException;

    boolean unregisterHidCallback(UiCallbackHid uiCallbackHid) throws RemoteException;

    boolean unregisterMapCallback(UiCallbackMap uiCallbackMap) throws RemoteException;

    boolean unregisterOppCallback(UiCallbackOpp uiCallbackOpp) throws RemoteException;

    boolean unregisterPbapCallback(UiCallbackPbap uiCallbackPbap) throws RemoteException;

    boolean unregisterSppCallback(UiCallbackSpp uiCallbackSpp) throws RemoteException;

    boolean unregisterUsbCallback(UiCallbackUsbBluetooth uiCallbackUsbBluetooth) throws RemoteException;

    boolean usbConnect(String str) throws RemoteException;

    boolean usbDisConnect() throws RemoteException;

    boolean usbDisConnectDevice(String str) throws RemoteException;

    boolean usbReqUnpair(String str) throws RemoteException;

    boolean usbSearch() throws RemoteException;

    boolean usbStopSearch() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements UiCommand {
        private static final String DESCRIPTOR = "com.nforetek.bt.aidl.UiCommand";
        static final int TRANSACTION_cancelBtDiscovery = 70;
        static final int TRANSACTION_getA2dpConnectedAddress = 15;
        static final int TRANSACTION_getA2dpConnectionState = 13;
        static final int TRANSACTION_getA2dpStreamType = 22;
        static final int TRANSACTION_getAvrcpConnectedAddress = 27;
        static final int TRANSACTION_getAvrcpConnectionState = 25;
        static final int TRANSACTION_getBtAutoConnectCondition = 89;
        static final int TRANSACTION_getBtAutoConnectPeriod = 90;
        static final int TRANSACTION_getBtAutoConnectState = 91;
        static final int TRANSACTION_getBtAutoConnectingAddress = 92;
        static final int TRANSACTION_getBtLocalAddress = 76;
        static final int TRANSACTION_getBtLocalName = 74;
        static final int TRANSACTION_getBtRemoteDeviceName = 75;
        static final int TRANSACTION_getBtRemoteUuids = 85;
        static final int TRANSACTION_getBtRoleMode = 87;
        static final int TRANSACTION_getBtState = 79;
        static final int TRANSACTION_getGattAddedGattCharacteristicUuidList = 199;
        static final int TRANSACTION_getGattAddedGattDescriptorUuidList = 200;
        static final int TRANSACTION_getGattAddedGattServiceUuidList = 198;
        static final int TRANSACTION_getGattServerConnectionState = 187;
        static final int TRANSACTION_getHfpAudioConnectionState = 98;
        static final int TRANSACTION_getHfpCallList = 102;
        static final int TRANSACTION_getHfpConnectedAddress = 97;
        static final int TRANSACTION_getHfpConnectionState = 95;
        static final int TRANSACTION_getHfpRemoteBatteryIndicator = 104;
        static final int TRANSACTION_getHfpRemoteNetworkOperator = 116;
        static final int TRANSACTION_getHfpRemoteSignalStrength = 101;
        static final int TRANSACTION_getHfpRemoteSubscriberNumber = 117;
        static final int TRANSACTION_getHidConnectedAddress = 153;
        static final int TRANSACTION_getHidConnectionState = 151;
        static final int TRANSACTION_getLocalUsbConnectionState = 220;
        static final int TRANSACTION_getMapCurrentState = 169;
        static final int TRANSACTION_getMapRegisterState = 170;
        static final int TRANSACTION_getNfServiceVersionName = 66;
        static final int TRANSACTION_getOppFilePath = 178;
        static final int TRANSACTION_getPbapConnectionState = 126;
        static final int TRANSACTION_getPbapDownloadingAddress = 128;
        static final int TRANSACTION_getProfileConnectionState = 221;
        static final int TRANSACTION_getTargetAddress = 182;
        static final int TRANSACTION_getUiServiceVersionName = 1;
        static final int TRANSACTION_getUsbAddress = 218;
        static final int TRANSACTION_getUsbBtLocalName = 216;
        static final int TRANSACTION_getUsbConnectedDevice = 219;
        static final int TRANSACTION_getUsbConnectionState = 213;
        static final int TRANSACTION_isA2dpConnected = 14;
        static final int TRANSACTION_isA2dpServiceReady = 3;
        static final int TRANSACTION_isAvrcp13Supported = 30;
        static final int TRANSACTION_isAvrcp14BrowsingChannelEstablished = 54;
        static final int TRANSACTION_isAvrcp14Supported = 31;
        static final int TRANSACTION_isAvrcpConnected = 26;
        static final int TRANSACTION_isAvrcpServiceReady = 2;
        static final int TRANSACTION_isBluetoothServiceReady = 5;
        static final int TRANSACTION_isBtAutoConnectEnable = 82;
        static final int TRANSACTION_isBtDiscoverable = 81;
        static final int TRANSACTION_isBtDiscovering = 80;
        static final int TRANSACTION_isBtEnabled = 78;
        static final int TRANSACTION_isGattServiceReady = 184;
        static final int TRANSACTION_isHfpConnected = 96;
        static final int TRANSACTION_isHfpInBandRingtoneSupport = 123;
        static final int TRANSACTION_isHfpMicMute = 121;
        static final int TRANSACTION_isHfpRemoteOnRoaming = 103;
        static final int TRANSACTION_isHfpRemoteTelecomServiceOn = 105;
        static final int TRANSACTION_isHfpRemoteVoiceDialOn = 106;
        static final int TRANSACTION_isHfpServiceReady = 6;
        static final int TRANSACTION_isHidConnected = 152;
        static final int TRANSACTION_isHidServiceReady = 7;
        static final int TRANSACTION_isMapNotificationRegistered = 164;
        static final int TRANSACTION_isMapServiceReady = 10;
        static final int TRANSACTION_isOppServiceReady = 9;
        static final int TRANSACTION_isPbapDownloading = 127;
        static final int TRANSACTION_isPbapServiceReady = 8;
        static final int TRANSACTION_isSppConnected = 147;
        static final int TRANSACTION_isSppServiceReady = 4;
        static final int TRANSACTION_isUsbA2dpConnected = 212;
        static final int TRANSACTION_isUsbAvrcpConnected = 214;
        static final int TRANSACTION_isUsbDiscovering = 210;
        static final int TRANSACTION_isUsbEnabled = 209;
        static final int TRANSACTION_isUsbHfpConnected = 211;
        static final int TRANSACTION_muteHfpMic = 122;
        static final int TRANSACTION_pauseA2dpRender = 18;
        static final int TRANSACTION_pauseHfpRender = 119;
        static final int TRANSACTION_registerA2dpCallback = 11;
        static final int TRANSACTION_registerAvrcpCallback = 23;
        static final int TRANSACTION_registerBtCallback = 64;
        static final int TRANSACTION_registerGattServerCallback = 185;
        static final int TRANSACTION_registerHfpCallback = 93;
        static final int TRANSACTION_registerHidCallback = 149;
        static final int TRANSACTION_registerMapCallback = 158;
        static final int TRANSACTION_registerOppCallback = 175;
        static final int TRANSACTION_registerPbapCallback = 124;
        static final int TRANSACTION_registerSppCallback = 142;
        static final int TRANSACTION_registerUsbCallback = 201;
        static final int TRANSACTION_reqA2dpConnect = 16;
        static final int TRANSACTION_reqA2dpDisconnect = 17;
        static final int TRANSACTION_reqAvrcp13GetCapabilitiesSupportEvent = 43;
        static final int TRANSACTION_reqAvrcp13GetElementAttributesPlaying = 48;
        static final int TRANSACTION_reqAvrcp13GetPlayStatus = 49;
        static final int TRANSACTION_reqAvrcp13GetPlayerSettingAttributesList = 44;
        static final int TRANSACTION_reqAvrcp13GetPlayerSettingCurrentValues = 46;
        static final int TRANSACTION_reqAvrcp13GetPlayerSettingValuesList = 45;
        static final int TRANSACTION_reqAvrcp13NextGroup = 52;
        static final int TRANSACTION_reqAvrcp13PreviousGroup = 53;
        static final int TRANSACTION_reqAvrcp13SetPlayerSettingValue = 47;
        static final int TRANSACTION_reqAvrcp14AddToNowPlaying = 62;
        static final int TRANSACTION_reqAvrcp14ChangePath = 58;
        static final int TRANSACTION_reqAvrcp14GetFolderItems = 57;
        static final int TRANSACTION_reqAvrcp14GetItemAttributes = 59;
        static final int TRANSACTION_reqAvrcp14PlaySelectedItem = 60;
        static final int TRANSACTION_reqAvrcp14Search = 61;
        static final int TRANSACTION_reqAvrcp14SetAbsoluteVolume = 63;
        static final int TRANSACTION_reqAvrcp14SetAddressedPlayer = 55;
        static final int TRANSACTION_reqAvrcp14SetBrowsedPlayer = 56;
        static final int TRANSACTION_reqAvrcpBackward = 36;
        static final int TRANSACTION_reqAvrcpConnect = 28;
        static final int TRANSACTION_reqAvrcpDisconnect = 29;
        static final int TRANSACTION_reqAvrcpForward = 35;
        static final int TRANSACTION_reqAvrcpPause = 34;
        static final int TRANSACTION_reqAvrcpPlay = 32;
        static final int TRANSACTION_reqAvrcpRegisterEventWatcher = 50;
        static final int TRANSACTION_reqAvrcpStartFastForward = 39;
        static final int TRANSACTION_reqAvrcpStartRewind = 41;
        static final int TRANSACTION_reqAvrcpStop = 33;
        static final int TRANSACTION_reqAvrcpStopFastForward = 40;
        static final int TRANSACTION_reqAvrcpStopRewind = 42;
        static final int TRANSACTION_reqAvrcpUnregisterEventWatcher = 51;
        static final int TRANSACTION_reqAvrcpUpdateSongStatus = 183;
        static final int TRANSACTION_reqAvrcpVolumeDown = 38;
        static final int TRANSACTION_reqAvrcpVolumeUp = 37;
        static final int TRANSACTION_reqBtConnectHfpA2dp = 83;
        static final int TRANSACTION_reqBtDisconnectAll = 84;
        static final int TRANSACTION_reqBtPair = 71;
        static final int TRANSACTION_reqBtPairedDevices = 73;
        static final int TRANSACTION_reqBtUnpair = 72;
        static final int TRANSACTION_reqGattServerAddCharacteristic = 190;
        static final int TRANSACTION_reqGattServerAddDescriptor = 191;
        static final int TRANSACTION_reqGattServerBeginServiceDeclaration = 189;
        static final int TRANSACTION_reqGattServerClearServices = 194;
        static final int TRANSACTION_reqGattServerDisconnect = 188;
        static final int TRANSACTION_reqGattServerEndServiceDeclaration = 192;
        static final int TRANSACTION_reqGattServerListen = 195;
        static final int TRANSACTION_reqGattServerRemoveService = 193;
        static final int TRANSACTION_reqGattServerSendNotification = 197;
        static final int TRANSACTION_reqGattServerSendResponse = 196;
        static final int TRANSACTION_reqHfpAnswerCall = 110;
        static final int TRANSACTION_reqHfpAudioTransferToCarkit = 114;
        static final int TRANSACTION_reqHfpAudioTransferToPhone = 115;
        static final int TRANSACTION_reqHfpConnect = 99;
        static final int TRANSACTION_reqHfpDialCall = 107;
        static final int TRANSACTION_reqHfpDisconnect = 100;
        static final int TRANSACTION_reqHfpMemoryDial = 109;
        static final int TRANSACTION_reqHfpReDial = 108;
        static final int TRANSACTION_reqHfpRejectIncomingCall = 111;
        static final int TRANSACTION_reqHfpSendDtmf = 113;
        static final int TRANSACTION_reqHfpTerminateCurrentCall = 112;
        static final int TRANSACTION_reqHfpVoiceDial = 118;
        static final int TRANSACTION_reqHidConnect = 154;
        static final int TRANSACTION_reqHidDisconnect = 155;
        static final int TRANSACTION_reqMapChangeReadStatus = 173;
        static final int TRANSACTION_reqMapCleanDatabase = 168;
        static final int TRANSACTION_reqMapDatabaseAvailable = 166;
        static final int TRANSACTION_reqMapDeleteDatabaseByAddress = 167;
        static final int TRANSACTION_reqMapDeleteMessage = 172;
        static final int TRANSACTION_reqMapDownloadInterrupt = 165;
        static final int TRANSACTION_reqMapDownloadMessage = 161;
        static final int TRANSACTION_reqMapDownloadSingleMessage = 160;
        static final int TRANSACTION_reqMapRegisterNotification = 162;
        static final int TRANSACTION_reqMapSendMessage = 171;
        static final int TRANSACTION_reqMapUnregisterNotification = 163;
        static final int TRANSACTION_reqOppAcceptReceiveFile = 179;
        static final int TRANSACTION_reqOppInterruptReceiveFile = 180;
        static final int TRANSACTION_reqPbapCleanDatabase = 139;
        static final int TRANSACTION_reqPbapDatabaseAvailable = 137;
        static final int TRANSACTION_reqPbapDatabaseQueryNameByNumber = 135;
        static final int TRANSACTION_reqPbapDatabaseQueryNameByPartialNumber = 136;
        static final int TRANSACTION_reqPbapDeleteDatabaseByAddress = 138;
        static final int TRANSACTION_reqPbapDownload = 129;
        static final int TRANSACTION_reqPbapDownloadInterrupt = 140;
        static final int TRANSACTION_reqPbapDownloadRange = 130;
        static final int TRANSACTION_reqPbapDownloadRangeToContactsProvider = 134;
        static final int TRANSACTION_reqPbapDownloadRangeToDatabase = 132;
        static final int TRANSACTION_reqPbapDownloadToContactsProvider = 133;
        static final int TRANSACTION_reqPbapDownloadToDatabase = 131;
        static final int TRANSACTION_reqSendHidMouseCommand = 156;
        static final int TRANSACTION_reqSendHidVirtualKeyCommand = 157;
        static final int TRANSACTION_reqSppConnect = 144;
        static final int TRANSACTION_reqSppConnectedDeviceAddressList = 146;
        static final int TRANSACTION_reqSppDisconnect = 145;
        static final int TRANSACTION_reqSppSendData = 148;
        static final int TRANSACTION_reqUsbBtPairedDevices = 215;
        static final int TRANSACTION_setA2dpLocalVolume = 20;
        static final int TRANSACTION_setA2dpStreamType = 21;
        static final int TRANSACTION_setBtAutoConnect = 88;
        static final int TRANSACTION_setBtDiscoverableTimeout = 68;
        static final int TRANSACTION_setBtEnable = 67;
        static final int TRANSACTION_setBtLocalName = 77;
        static final int TRANSACTION_setMapDownloadNotify = 174;
        static final int TRANSACTION_setOppFilePath = 177;
        static final int TRANSACTION_setPbapDownloadNotify = 141;
        static final int TRANSACTION_setTargetAddress = 181;
        static final int TRANSACTION_setUsbBtLocalName = 217;
        static final int TRANSACTION_setUsbEnabled = 208;
        static final int TRANSACTION_startA2dpRender = 19;
        static final int TRANSACTION_startBtDiscovery = 69;
        static final int TRANSACTION_startHfpRender = 120;
        static final int TRANSACTION_switchBtRoleMode = 86;
        static final int TRANSACTION_unregisterA2dpCallback = 12;
        static final int TRANSACTION_unregisterAvrcpCallback = 24;
        static final int TRANSACTION_unregisterBtCallback = 65;
        static final int TRANSACTION_unregisterGattServerCallback = 186;
        static final int TRANSACTION_unregisterHfpCallback = 94;
        static final int TRANSACTION_unregisterHidCallback = 150;
        static final int TRANSACTION_unregisterMapCallback = 159;
        static final int TRANSACTION_unregisterOppCallback = 176;
        static final int TRANSACTION_unregisterPbapCallback = 125;
        static final int TRANSACTION_unregisterSppCallback = 143;
        static final int TRANSACTION_unregisterUsbCallback = 202;
        static final int TRANSACTION_usbConnect = 205;
        static final int TRANSACTION_usbDisConnect = 206;
        static final int TRANSACTION_usbDisConnectDevice = 222;
        static final int TRANSACTION_usbReqUnpair = 207;
        static final int TRANSACTION_usbSearch = 203;
        static final int TRANSACTION_usbStopSearch = 204;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static UiCommand asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof UiCommand)) {
                return (UiCommand) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            ParcelUuid _arg1;
            ParcelUuid _arg02;
            ParcelUuid _arg03;
            ParcelUuid _arg12;
            ParcelUuid _arg2;
            ParcelUuid _arg3;
            ParcelUuid _arg04;
            ParcelUuid _arg05;
            ParcelUuid _arg13;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getUiServiceVersionName();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvrcpServiceReady = isAvrcpServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isAvrcpServiceReady ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isA2dpServiceReady = isA2dpServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isA2dpServiceReady ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSppServiceReady = isSppServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isSppServiceReady ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBluetoothServiceReady = isBluetoothServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isBluetoothServiceReady ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpServiceReady = isHfpServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isHfpServiceReady ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHidServiceReady = isHidServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isHidServiceReady ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isPbapServiceReady = isPbapServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isPbapServiceReady ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isOppServiceReady = isOppServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isOppServiceReady ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMapServiceReady = isMapServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isMapServiceReady ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerA2dpCallback = registerA2dpCallback(UiCallbackA2dp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerA2dpCallback ? 1 : 0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterA2dpCallback = unregisterA2dpCallback(UiCallbackA2dp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterA2dpCallback ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getA2dpConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isA2dpConnected = isA2dpConnected();
                    reply.writeNoException();
                    reply.writeInt(isA2dpConnected ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getA2dpConnectedAddress();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqA2dpConnect = reqA2dpConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqA2dpConnect ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqA2dpDisconnect = reqA2dpDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqA2dpDisconnect ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    pauseA2dpRender();
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    startA2dpRender();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    boolean a2dpLocalVolume = setA2dpLocalVolume(data.readFloat());
                    reply.writeNoException();
                    reply.writeInt(a2dpLocalVolume ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    boolean a2dpStreamType = setA2dpStreamType(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(a2dpStreamType ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = getA2dpStreamType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerAvrcpCallback = registerAvrcpCallback(UiCallbackAvrcp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerAvrcpCallback ? 1 : 0);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterAvrcpCallback = unregisterAvrcpCallback(UiCallbackAvrcp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterAvrcpCallback ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _result5 = getAvrcpConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvrcpConnected = isAvrcpConnected();
                    reply.writeNoException();
                    reply.writeInt(isAvrcpConnected ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _result6 = getAvrcpConnectedAddress();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpConnect = reqAvrcpConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpConnect ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpDisconnect = reqAvrcpDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpDisconnect ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvrcp13Supported = isAvrcp13Supported(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isAvrcp13Supported ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvrcp14Supported = isAvrcp14Supported(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isAvrcp14Supported ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpPlay = reqAvrcpPlay();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpPlay ? 1 : 0);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpStop = reqAvrcpStop();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpStop ? 1 : 0);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpPause = reqAvrcpPause();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpPause ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpForward = reqAvrcpForward();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpForward ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpBackward = reqAvrcpBackward();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpBackward ? 1 : 0);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpVolumeUp = reqAvrcpVolumeUp();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpVolumeUp ? 1 : 0);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpVolumeDown = reqAvrcpVolumeDown();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpVolumeDown ? 1 : 0);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpStartFastForward = reqAvrcpStartFastForward();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpStartFastForward ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpStopFastForward = reqAvrcpStopFastForward();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpStopFastForward ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpStartRewind = reqAvrcpStartRewind();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpStartRewind ? 1 : 0);
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpStopRewind = reqAvrcpStopRewind();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpStopRewind ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetCapabilitiesSupportEvent = reqAvrcp13GetCapabilitiesSupportEvent();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetCapabilitiesSupportEvent ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetPlayerSettingAttributesList = reqAvrcp13GetPlayerSettingAttributesList();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetPlayerSettingAttributesList ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetPlayerSettingValuesList = reqAvrcp13GetPlayerSettingValuesList(data.readByte());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetPlayerSettingValuesList ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetPlayerSettingCurrentValues = reqAvrcp13GetPlayerSettingCurrentValues();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetPlayerSettingCurrentValues ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg06 = data.readByte();
                    byte _arg14 = data.readByte();
                    boolean reqAvrcp13SetPlayerSettingValue = reqAvrcp13SetPlayerSettingValue(_arg06, _arg14);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13SetPlayerSettingValue ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetElementAttributesPlaying = reqAvrcp13GetElementAttributesPlaying();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetElementAttributesPlaying ? 1 : 0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13GetPlayStatus = reqAvrcp13GetPlayStatus();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13GetPlayStatus ? 1 : 0);
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg07 = data.readByte();
                    long _arg15 = data.readLong();
                    boolean reqAvrcpRegisterEventWatcher = reqAvrcpRegisterEventWatcher(_arg07, _arg15);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpRegisterEventWatcher ? 1 : 0);
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcpUnregisterEventWatcher = reqAvrcpUnregisterEventWatcher(data.readByte());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcpUnregisterEventWatcher ? 1 : 0);
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13NextGroup = reqAvrcp13NextGroup();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13NextGroup ? 1 : 0);
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp13PreviousGroup = reqAvrcp13PreviousGroup();
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp13PreviousGroup ? 1 : 0);
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isAvrcp14BrowsingChannelEstablished = isAvrcp14BrowsingChannelEstablished();
                    reply.writeNoException();
                    reply.writeInt(isAvrcp14BrowsingChannelEstablished ? 1 : 0);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp14SetAddressedPlayer = reqAvrcp14SetAddressedPlayer(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14SetAddressedPlayer ? 1 : 0);
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp14SetBrowsedPlayer = reqAvrcp14SetBrowsedPlayer(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14SetBrowsedPlayer ? 1 : 0);
                    return true;
                case TRANSACTION_reqAvrcp14GetFolderItems /* 57 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp14GetFolderItems = reqAvrcp14GetFolderItems(data.readByte());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14GetFolderItems ? 1 : 0);
                    return true;
                case TRANSACTION_reqAvrcp14ChangePath /* 58 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    long _arg16 = data.readLong();
                    byte _arg22 = data.readByte();
                    boolean reqAvrcp14ChangePath = reqAvrcp14ChangePath(_arg08, _arg16, _arg22);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14ChangePath ? 1 : 0);
                    return true;
                case TRANSACTION_reqAvrcp14GetItemAttributes /* 59 */:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg09 = data.readByte();
                    int _arg17 = data.readInt();
                    long _arg23 = data.readLong();
                    boolean reqAvrcp14GetItemAttributes = reqAvrcp14GetItemAttributes(_arg09, _arg17, _arg23);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14GetItemAttributes ? 1 : 0);
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg010 = data.readByte();
                    int _arg18 = data.readInt();
                    long _arg24 = data.readLong();
                    boolean reqAvrcp14PlaySelectedItem = reqAvrcp14PlaySelectedItem(_arg010, _arg18, _arg24);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14PlaySelectedItem ? 1 : 0);
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp14Search = reqAvrcp14Search(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14Search ? 1 : 0);
                    return true;
                case TRANSACTION_reqAvrcp14AddToNowPlaying /* 62 */:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg011 = data.readByte();
                    int _arg19 = data.readInt();
                    long _arg25 = data.readLong();
                    boolean reqAvrcp14AddToNowPlaying = reqAvrcp14AddToNowPlaying(_arg011, _arg19, _arg25);
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14AddToNowPlaying ? 1 : 0);
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqAvrcp14SetAbsoluteVolume = reqAvrcp14SetAbsoluteVolume(data.readByte());
                    reply.writeNoException();
                    reply.writeInt(reqAvrcp14SetAbsoluteVolume ? 1 : 0);
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerBtCallback = registerBtCallback(UiCallbackBluetooth.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerBtCallback ? 1 : 0);
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterBtCallback = unregisterBtCallback(UiCallbackBluetooth.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterBtCallback ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    String _result7 = getNfServiceVersionName();
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean btEnable = setBtEnable(_arg0);
                    reply.writeNoException();
                    reply.writeInt(btEnable ? 1 : 0);
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    boolean btDiscoverableTimeout = setBtDiscoverableTimeout(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(btDiscoverableTimeout ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    boolean startBtDiscovery = startBtDiscovery();
                    reply.writeNoException();
                    reply.writeInt(startBtDiscovery ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    boolean cancelBtDiscovery = cancelBtDiscovery();
                    reply.writeNoException();
                    reply.writeInt(cancelBtDiscovery ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqBtPair = reqBtPair(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqBtPair ? 1 : 0);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqBtUnpair = reqBtUnpair(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqBtUnpair ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqBtPairedDevices = reqBtPairedDevices();
                    reply.writeNoException();
                    reply.writeInt(reqBtPairedDevices ? 1 : 0);
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    String _result8 = getBtLocalName();
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    String _result9 = getBtRemoteDeviceName(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    String _result10 = getBtLocalAddress();
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    boolean btLocalName = setBtLocalName(data.readString());
                    reply.writeNoException();
                    reply.writeInt(btLocalName ? 1 : 0);
                    return true;
                case TRANSACTION_isBtEnabled /* 78 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtEnabled = isBtEnabled();
                    reply.writeNoException();
                    reply.writeInt(isBtEnabled ? 1 : 0);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int _result11 = getBtState();
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtDiscovering = isBtDiscovering();
                    reply.writeNoException();
                    reply.writeInt(isBtDiscovering ? 1 : 0);
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtDiscoverable = isBtDiscoverable();
                    reply.writeNoException();
                    reply.writeInt(isBtDiscoverable ? 1 : 0);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isBtAutoConnectEnable = isBtAutoConnectEnable();
                    reply.writeNoException();
                    reply.writeInt(isBtAutoConnectEnable ? 1 : 0);
                    return true;
                case TRANSACTION_reqBtConnectHfpA2dp /* 83 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result12 = reqBtConnectHfpA2dp(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result12);
                    return true;
                case TRANSACTION_reqBtDisconnectAll /* 84 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result13 = reqBtDisconnectAll();
                    reply.writeNoException();
                    reply.writeInt(_result13);
                    return true;
                case TRANSACTION_getBtRemoteUuids /* 85 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result14 = getBtRemoteUuids(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result14);
                    return true;
                case TRANSACTION_switchBtRoleMode /* 86 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean switchBtRoleMode = switchBtRoleMode(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(switchBtRoleMode ? 1 : 0);
                    return true;
                case TRANSACTION_getBtRoleMode /* 87 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result15 = getBtRoleMode();
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case TRANSACTION_setBtAutoConnect /* 88 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg110 = data.readInt();
                    setBtAutoConnect(_arg012, _arg110);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getBtAutoConnectCondition /* 89 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result16 = getBtAutoConnectCondition();
                    reply.writeNoException();
                    reply.writeInt(_result16);
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    int _result17 = getBtAutoConnectPeriod();
                    reply.writeNoException();
                    reply.writeInt(_result17);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    int _result18 = getBtAutoConnectState();
                    reply.writeNoException();
                    reply.writeInt(_result18);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    String _result19 = getBtAutoConnectingAddress();
                    reply.writeNoException();
                    reply.writeString(_result19);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerHfpCallback = registerHfpCallback(UiCallbackHfp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerHfpCallback ? 1 : 0);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterHfpCallback = unregisterHfpCallback(UiCallbackHfp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterHfpCallback ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    int _result20 = getHfpConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpConnected = isHfpConnected();
                    reply.writeNoException();
                    reply.writeInt(isHfpConnected ? 1 : 0);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    String _result21 = getHfpConnectedAddress();
                    reply.writeNoException();
                    reply.writeString(_result21);
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    int _result22 = getHfpAudioConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result22);
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpConnect = reqHfpConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHfpConnect ? 1 : 0);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpDisconnect = reqHfpDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHfpDisconnect ? 1 : 0);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    int _result23 = getHfpRemoteSignalStrength();
                    reply.writeNoException();
                    reply.writeInt(_result23);
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    List<NfHfpClientCall> _result24 = getHfpCallList();
                    reply.writeNoException();
                    reply.writeTypedList(_result24);
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpRemoteOnRoaming = isHfpRemoteOnRoaming();
                    reply.writeNoException();
                    reply.writeInt(isHfpRemoteOnRoaming ? 1 : 0);
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    int _result25 = getHfpRemoteBatteryIndicator();
                    reply.writeNoException();
                    reply.writeInt(_result25);
                    return true;
                case 105:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpRemoteTelecomServiceOn = isHfpRemoteTelecomServiceOn();
                    reply.writeNoException();
                    reply.writeInt(isHfpRemoteTelecomServiceOn ? 1 : 0);
                    return true;
                case 106:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpRemoteVoiceDialOn = isHfpRemoteVoiceDialOn();
                    reply.writeNoException();
                    reply.writeInt(isHfpRemoteVoiceDialOn ? 1 : 0);
                    return true;
                case 107:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpDialCall = reqHfpDialCall(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHfpDialCall ? 1 : 0);
                    return true;
                case 108:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpReDial = reqHfpReDial();
                    reply.writeNoException();
                    reply.writeInt(reqHfpReDial ? 1 : 0);
                    return true;
                case 109:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpMemoryDial = reqHfpMemoryDial(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHfpMemoryDial ? 1 : 0);
                    return true;
                case 110:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpAnswerCall = reqHfpAnswerCall(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(reqHfpAnswerCall ? 1 : 0);
                    return true;
                case TRANSACTION_reqHfpRejectIncomingCall /* 111 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpRejectIncomingCall = reqHfpRejectIncomingCall();
                    reply.writeNoException();
                    reply.writeInt(reqHfpRejectIncomingCall ? 1 : 0);
                    return true;
                case TRANSACTION_reqHfpTerminateCurrentCall /* 112 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpTerminateCurrentCall = reqHfpTerminateCurrentCall();
                    reply.writeNoException();
                    reply.writeInt(reqHfpTerminateCurrentCall ? 1 : 0);
                    return true;
                case 113:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpSendDtmf = reqHfpSendDtmf(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHfpSendDtmf ? 1 : 0);
                    return true;
                case 114:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpAudioTransferToCarkit = reqHfpAudioTransferToCarkit();
                    reply.writeNoException();
                    reply.writeInt(reqHfpAudioTransferToCarkit ? 1 : 0);
                    return true;
                case 115:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHfpAudioTransferToPhone = reqHfpAudioTransferToPhone();
                    reply.writeNoException();
                    reply.writeInt(reqHfpAudioTransferToPhone ? 1 : 0);
                    return true;
                case 116:
                    data.enforceInterface(DESCRIPTOR);
                    String _result26 = getHfpRemoteNetworkOperator();
                    reply.writeNoException();
                    reply.writeString(_result26);
                    return true;
                case 117:
                    data.enforceInterface(DESCRIPTOR);
                    String _result27 = getHfpRemoteSubscriberNumber();
                    reply.writeNoException();
                    reply.writeString(_result27);
                    return true;
                case 118:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean reqHfpVoiceDial = reqHfpVoiceDial(_arg0);
                    reply.writeNoException();
                    reply.writeInt(reqHfpVoiceDial ? 1 : 0);
                    return true;
                case 119:
                    data.enforceInterface(DESCRIPTOR);
                    pauseHfpRender();
                    reply.writeNoException();
                    return true;
                case 120:
                    data.enforceInterface(DESCRIPTOR);
                    startHfpRender();
                    reply.writeNoException();
                    return true;
                case 121:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpMicMute = isHfpMicMute();
                    reply.writeNoException();
                    reply.writeInt(isHfpMicMute ? 1 : 0);
                    return true;
                case TRANSACTION_muteHfpMic /* 122 */:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    muteHfpMic(_arg0);
                    reply.writeNoException();
                    return true;
                case 123:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHfpInBandRingtoneSupport = isHfpInBandRingtoneSupport();
                    reply.writeNoException();
                    reply.writeInt(isHfpInBandRingtoneSupport ? 1 : 0);
                    return true;
                case TRANSACTION_registerPbapCallback /* 124 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerPbapCallback = registerPbapCallback(UiCallbackPbap.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerPbapCallback ? 1 : 0);
                    return true;
                case 125:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterPbapCallback = unregisterPbapCallback(UiCallbackPbap.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterPbapCallback ? 1 : 0);
                    return true;
                case 126:
                    data.enforceInterface(DESCRIPTOR);
                    int _result28 = getPbapConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result28);
                    return true;
                case TRANSACTION_isPbapDownloading /* 127 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isPbapDownloading = isPbapDownloading();
                    reply.writeNoException();
                    reply.writeInt(isPbapDownloading ? 1 : 0);
                    return true;
                case 128:
                    data.enforceInterface(DESCRIPTOR);
                    String _result29 = getPbapDownloadingAddress();
                    reply.writeNoException();
                    reply.writeString(_result29);
                    return true;
                case TRANSACTION_reqPbapDownload /* 129 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    int _arg111 = data.readInt();
                    int _arg26 = data.readInt();
                    boolean reqPbapDownload = reqPbapDownload(_arg013, _arg111, _arg26);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownload ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDownloadRange /* 130 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _arg112 = data.readInt();
                    int _arg27 = data.readInt();
                    int _arg32 = data.readInt();
                    int _arg4 = data.readInt();
                    boolean reqPbapDownloadRange = reqPbapDownloadRange(_arg014, _arg112, _arg27, _arg32, _arg4);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadRange ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDownloadToDatabase /* 131 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    int _arg113 = data.readInt();
                    int _arg28 = data.readInt();
                    boolean reqPbapDownloadToDatabase = reqPbapDownloadToDatabase(_arg015, _arg113, _arg28);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadToDatabase ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDownloadRangeToDatabase /* 132 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int _arg114 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg33 = data.readInt();
                    int _arg42 = data.readInt();
                    boolean reqPbapDownloadRangeToDatabase = reqPbapDownloadRangeToDatabase(_arg016, _arg114, _arg29, _arg33, _arg42);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadRangeToDatabase ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDownloadToContactsProvider /* 133 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    int _arg115 = data.readInt();
                    int _arg210 = data.readInt();
                    boolean reqPbapDownloadToContactsProvider = reqPbapDownloadToContactsProvider(_arg017, _arg115, _arg210);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadToContactsProvider ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDownloadRangeToContactsProvider /* 134 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg116 = data.readInt();
                    int _arg211 = data.readInt();
                    int _arg34 = data.readInt();
                    int _arg43 = data.readInt();
                    boolean reqPbapDownloadRangeToContactsProvider = reqPbapDownloadRangeToContactsProvider(_arg018, _arg116, _arg211, _arg34, _arg43);
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadRangeToContactsProvider ? 1 : 0);
                    return true;
                case TRANSACTION_reqPbapDatabaseQueryNameByNumber /* 135 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    String _arg117 = data.readString();
                    reqPbapDatabaseQueryNameByNumber(_arg019, _arg117);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqPbapDatabaseQueryNameByPartialNumber /* 136 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    String _arg118 = data.readString();
                    int _arg212 = data.readInt();
                    reqPbapDatabaseQueryNameByPartialNumber(_arg020, _arg118, _arg212);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqPbapDatabaseAvailable /* 137 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqPbapDatabaseAvailable(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqPbapDeleteDatabaseByAddress /* 138 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqPbapDeleteDatabaseByAddress(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqPbapCleanDatabase /* 139 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqPbapCleanDatabase();
                    reply.writeNoException();
                    return true;
                case 140:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqPbapDownloadInterrupt = reqPbapDownloadInterrupt(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqPbapDownloadInterrupt ? 1 : 0);
                    return true;
                case TRANSACTION_setPbapDownloadNotify /* 141 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean pbapDownloadNotify = setPbapDownloadNotify(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(pbapDownloadNotify ? 1 : 0);
                    return true;
                case TRANSACTION_registerSppCallback /* 142 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerSppCallback = registerSppCallback(UiCallbackSpp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerSppCallback ? 1 : 0);
                    return true;
                case 143:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterSppCallback = unregisterSppCallback(UiCallbackSpp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterSppCallback ? 1 : 0);
                    return true;
                case TRANSACTION_reqSppConnect /* 144 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqSppConnect = reqSppConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqSppConnect ? 1 : 0);
                    return true;
                case TRANSACTION_reqSppDisconnect /* 145 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqSppDisconnect = reqSppDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqSppDisconnect ? 1 : 0);
                    return true;
                case TRANSACTION_reqSppConnectedDeviceAddressList /* 146 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqSppConnectedDeviceAddressList();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isSppConnected /* 147 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSppConnected = isSppConnected(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isSppConnected ? 1 : 0);
                    return true;
                case TRANSACTION_reqSppSendData /* 148 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    byte[] _arg119 = data.createByteArray();
                    reqSppSendData(_arg021, _arg119);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_registerHidCallback /* 149 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerHidCallback = registerHidCallback(UiCallbackHid.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerHidCallback ? 1 : 0);
                    return true;
                case 150:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterHidCallback = unregisterHidCallback(UiCallbackHid.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterHidCallback ? 1 : 0);
                    return true;
                case TRANSACTION_getHidConnectionState /* 151 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result30 = getHidConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result30);
                    return true;
                case 152:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isHidConnected = isHidConnected();
                    reply.writeNoException();
                    reply.writeInt(isHidConnected ? 1 : 0);
                    return true;
                case TRANSACTION_getHidConnectedAddress /* 153 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result31 = getHidConnectedAddress();
                    reply.writeNoException();
                    reply.writeString(_result31);
                    return true;
                case TRANSACTION_reqHidConnect /* 154 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHidConnect = reqHidConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHidConnect ? 1 : 0);
                    return true;
                case TRANSACTION_reqHidDisconnect /* 155 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqHidDisconnect = reqHidDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqHidDisconnect ? 1 : 0);
                    return true;
                case TRANSACTION_reqSendHidMouseCommand /* 156 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    int _arg120 = data.readInt();
                    int _arg213 = data.readInt();
                    int _arg35 = data.readInt();
                    boolean reqSendHidMouseCommand = reqSendHidMouseCommand(_arg022, _arg120, _arg213, _arg35);
                    reply.writeNoException();
                    reply.writeInt(reqSendHidMouseCommand ? 1 : 0);
                    return true;
                case TRANSACTION_reqSendHidVirtualKeyCommand /* 157 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    int _arg121 = data.readInt();
                    boolean reqSendHidVirtualKeyCommand = reqSendHidVirtualKeyCommand(_arg023, _arg121);
                    reply.writeNoException();
                    reply.writeInt(reqSendHidVirtualKeyCommand ? 1 : 0);
                    return true;
                case TRANSACTION_registerMapCallback /* 158 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerMapCallback = registerMapCallback(UiCallbackMap.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerMapCallback ? 1 : 0);
                    return true;
                case TRANSACTION_unregisterMapCallback /* 159 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterMapCallback = unregisterMapCallback(UiCallbackMap.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterMapCallback ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapDownloadSingleMessage /* 160 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    int _arg122 = data.readInt();
                    String _arg214 = data.readString();
                    int _arg36 = data.readInt();
                    boolean reqMapDownloadSingleMessage = reqMapDownloadSingleMessage(_arg024, _arg122, _arg214, _arg36);
                    reply.writeNoException();
                    reply.writeInt(reqMapDownloadSingleMessage ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapDownloadMessage /* 161 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    int _arg123 = data.readInt();
                    boolean _arg215 = data.readInt() != 0;
                    int _arg37 = data.readInt();
                    int _arg44 = data.readInt();
                    int _arg5 = data.readInt();
                    String _arg6 = data.readString();
                    String _arg7 = data.readString();
                    String _arg8 = data.readString();
                    String _arg9 = data.readString();
                    int _arg10 = data.readInt();
                    int _arg11 = data.readInt();
                    boolean reqMapDownloadMessage = reqMapDownloadMessage(_arg025, _arg123, _arg215, _arg37, _arg44, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11);
                    reply.writeNoException();
                    reply.writeInt(reqMapDownloadMessage ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapRegisterNotification /* 162 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    _arg0 = data.readInt() != 0;
                    boolean reqMapRegisterNotification = reqMapRegisterNotification(_arg026, _arg0);
                    reply.writeNoException();
                    reply.writeInt(reqMapRegisterNotification ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapUnregisterNotification /* 163 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqMapUnregisterNotification(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isMapNotificationRegistered /* 164 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMapNotificationRegistered = isMapNotificationRegistered(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isMapNotificationRegistered ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapDownloadInterrupt /* 165 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqMapDownloadInterrupt = reqMapDownloadInterrupt(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqMapDownloadInterrupt ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapDatabaseAvailable /* 166 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqMapDatabaseAvailable();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqMapDeleteDatabaseByAddress /* 167 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqMapDeleteDatabaseByAddress(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_reqMapCleanDatabase /* 168 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqMapCleanDatabase();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getMapCurrentState /* 169 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result32 = getMapCurrentState(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result32);
                    return true;
                case TRANSACTION_getMapRegisterState /* 170 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result33 = getMapRegisterState(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case TRANSACTION_reqMapSendMessage /* 171 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    String _arg124 = data.readString();
                    String _arg216 = data.readString();
                    boolean reqMapSendMessage = reqMapSendMessage(_arg027, _arg124, _arg216);
                    reply.writeNoException();
                    reply.writeInt(reqMapSendMessage ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapDeleteMessage /* 172 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    int _arg125 = data.readInt();
                    String _arg217 = data.readString();
                    boolean reqMapDeleteMessage = reqMapDeleteMessage(_arg028, _arg125, _arg217);
                    reply.writeNoException();
                    reply.writeInt(reqMapDeleteMessage ? 1 : 0);
                    return true;
                case TRANSACTION_reqMapChangeReadStatus /* 173 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    int _arg126 = data.readInt();
                    String _arg218 = data.readString();
                    _arg0 = data.readInt() != 0;
                    boolean reqMapChangeReadStatus = reqMapChangeReadStatus(_arg029, _arg126, _arg218, _arg0);
                    reply.writeNoException();
                    reply.writeInt(reqMapChangeReadStatus ? 1 : 0);
                    return true;
                case TRANSACTION_setMapDownloadNotify /* 174 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mapDownloadNotify = setMapDownloadNotify(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(mapDownloadNotify ? 1 : 0);
                    return true;
                case TRANSACTION_registerOppCallback /* 175 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerOppCallback = registerOppCallback(UiCallbackOpp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerOppCallback ? 1 : 0);
                    return true;
                case TRANSACTION_unregisterOppCallback /* 176 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterOppCallback = unregisterOppCallback(UiCallbackOpp.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterOppCallback ? 1 : 0);
                    return true;
                case TRANSACTION_setOppFilePath /* 177 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean oppFilePath = setOppFilePath(data.readString());
                    reply.writeNoException();
                    reply.writeInt(oppFilePath ? 1 : 0);
                    return true;
                case TRANSACTION_getOppFilePath /* 178 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result34 = getOppFilePath();
                    reply.writeNoException();
                    reply.writeString(_result34);
                    return true;
                case TRANSACTION_reqOppAcceptReceiveFile /* 179 */:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean reqOppAcceptReceiveFile = reqOppAcceptReceiveFile(_arg0);
                    reply.writeNoException();
                    reply.writeInt(reqOppAcceptReceiveFile ? 1 : 0);
                    return true;
                case TRANSACTION_reqOppInterruptReceiveFile /* 180 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqOppInterruptReceiveFile = reqOppInterruptReceiveFile();
                    reply.writeNoException();
                    reply.writeInt(reqOppInterruptReceiveFile ? 1 : 0);
                    return true;
                case TRANSACTION_setTargetAddress /* 181 */:
                    data.enforceInterface(DESCRIPTOR);
                    setTargetAddress(data.readString());
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getTargetAddress /* 182 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result35 = getTargetAddress();
                    reply.writeNoException();
                    reply.writeString(_result35);
                    return true;
                case TRANSACTION_reqAvrcpUpdateSongStatus /* 183 */:
                    data.enforceInterface(DESCRIPTOR);
                    reqAvrcpUpdateSongStatus();
                    reply.writeNoException();
                    return true;
                case TRANSACTION_isGattServiceReady /* 184 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isGattServiceReady = isGattServiceReady();
                    reply.writeNoException();
                    reply.writeInt(isGattServiceReady ? 1 : 0);
                    return true;
                case TRANSACTION_registerGattServerCallback /* 185 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerGattServerCallback = registerGattServerCallback(UiCallbackGattServer.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerGattServerCallback ? 1 : 0);
                    return true;
                case TRANSACTION_unregisterGattServerCallback /* 186 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterGattServerCallback = unregisterGattServerCallback(UiCallbackGattServer.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterGattServerCallback ? 1 : 0);
                    return true;
                case TRANSACTION_getGattServerConnectionState /* 187 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _result36 = getGattServerConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case TRANSACTION_reqGattServerDisconnect /* 188 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqGattServerDisconnect = reqGattServerDisconnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(reqGattServerDisconnect ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerBeginServiceDeclaration /* 189 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    boolean reqGattServerBeginServiceDeclaration = reqGattServerBeginServiceDeclaration(_arg030, _arg1);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerBeginServiceDeclaration ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerAddCharacteristic /* 190 */:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    int _arg127 = data.readInt();
                    int _arg219 = data.readInt();
                    boolean reqGattServerAddCharacteristic = reqGattServerAddCharacteristic(_arg02, _arg127, _arg219);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerAddCharacteristic ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerAddDescriptor /* 191 */:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg128 = data.readInt();
                    boolean reqGattServerAddDescriptor = reqGattServerAddDescriptor(_arg03, _arg128);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerAddDescriptor ? 1 : 0);
                    return true;
                case 192:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqGattServerEndServiceDeclaration = reqGattServerEndServiceDeclaration();
                    reply.writeNoException();
                    reply.writeInt(reqGattServerEndServiceDeclaration ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerRemoveService /* 193 */:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    boolean reqGattServerRemoveService = reqGattServerRemoveService(_arg031, _arg12);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerRemoveService ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerClearServices /* 194 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean reqGattServerClearServices = reqGattServerClearServices();
                    reply.writeNoException();
                    reply.writeInt(reqGattServerClearServices ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerListen /* 195 */:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean reqGattServerListen = reqGattServerListen(_arg0);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerListen ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerSendResponse /* 196 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    int _arg129 = data.readInt();
                    int _arg220 = data.readInt();
                    int _arg38 = data.readInt();
                    byte[] _arg45 = data.createByteArray();
                    boolean reqGattServerSendResponse = reqGattServerSendResponse(_arg032, _arg129, _arg220, _arg38, _arg45);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerSendResponse ? 1 : 0);
                    return true;
                case TRANSACTION_reqGattServerSendNotification /* 197 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    int _arg130 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg2 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    boolean _arg46 = data.readInt() != 0;
                    byte[] _arg52 = data.createByteArray();
                    boolean reqGattServerSendNotification = reqGattServerSendNotification(_arg033, _arg130, _arg2, _arg3, _arg46, _arg52);
                    reply.writeNoException();
                    reply.writeInt(reqGattServerSendNotification ? 1 : 0);
                    return true;
                case TRANSACTION_getGattAddedGattServiceUuidList /* 198 */:
                    data.enforceInterface(DESCRIPTOR);
                    List<ParcelUuid> _result37 = getGattAddedGattServiceUuidList();
                    reply.writeNoException();
                    reply.writeTypedList(_result37);
                    return true;
                case 199:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    List<ParcelUuid> _result38 = getGattAddedGattCharacteristicUuidList(_arg04);
                    reply.writeNoException();
                    reply.writeTypedList(_result38);
                    return true;
                case 200:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    List<ParcelUuid> _result39 = getGattAddedGattDescriptorUuidList(_arg05, _arg13);
                    reply.writeNoException();
                    reply.writeTypedList(_result39);
                    return true;
                case 201:
                    data.enforceInterface(DESCRIPTOR);
                    boolean registerUsbCallback = registerUsbCallback(UiCallbackUsbBluetooth.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(registerUsbCallback ? 1 : 0);
                    return true;
                case 202:
                    data.enforceInterface(DESCRIPTOR);
                    boolean unregisterUsbCallback = unregisterUsbCallback(UiCallbackUsbBluetooth.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeInt(unregisterUsbCallback ? 1 : 0);
                    return true;
                case 203:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbSearch = usbSearch();
                    reply.writeNoException();
                    reply.writeInt(usbSearch ? 1 : 0);
                    return true;
                case 204:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbStopSearch = usbStopSearch();
                    reply.writeNoException();
                    reply.writeInt(usbStopSearch ? 1 : 0);
                    return true;
                case 205:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbConnect = usbConnect(data.readString());
                    reply.writeNoException();
                    reply.writeInt(usbConnect ? 1 : 0);
                    return true;
                case 206:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbDisConnect = usbDisConnect();
                    reply.writeNoException();
                    reply.writeInt(usbDisConnect ? 1 : 0);
                    return true;
                case 207:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbReqUnpair = usbReqUnpair(data.readString());
                    reply.writeNoException();
                    reply.writeInt(usbReqUnpair ? 1 : 0);
                    return true;
                case 208:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    boolean usbEnabled = setUsbEnabled(_arg0);
                    reply.writeNoException();
                    reply.writeInt(usbEnabled ? 1 : 0);
                    return true;
                case TRANSACTION_isUsbEnabled /* 209 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUsbEnabled = isUsbEnabled();
                    reply.writeNoException();
                    reply.writeInt(isUsbEnabled ? 1 : 0);
                    return true;
                case TRANSACTION_isUsbDiscovering /* 210 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUsbDiscovering = isUsbDiscovering();
                    reply.writeNoException();
                    reply.writeInt(isUsbDiscovering ? 1 : 0);
                    return true;
                case 211:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUsbHfpConnected = isUsbHfpConnected(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isUsbHfpConnected ? 1 : 0);
                    return true;
                case 212:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUsbA2dpConnected = isUsbA2dpConnected(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isUsbA2dpConnected ? 1 : 0);
                    return true;
                case 213:
                    data.enforceInterface(DESCRIPTOR);
                    int _result40 = getUsbConnectionState(data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result40);
                    return true;
                case 214:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isUsbAvrcpConnected = isUsbAvrcpConnected(data.readString());
                    reply.writeNoException();
                    reply.writeInt(isUsbAvrcpConnected ? 1 : 0);
                    return true;
                case 215:
                    data.enforceInterface(DESCRIPTOR);
                    List<UsbBluetoothDevice> _result41 = reqUsbBtPairedDevices();
                    reply.writeNoException();
                    reply.writeTypedList(_result41);
                    return true;
                case TRANSACTION_getUsbBtLocalName /* 216 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result42 = getUsbBtLocalName();
                    reply.writeNoException();
                    reply.writeString(_result42);
                    return true;
                case TRANSACTION_setUsbBtLocalName /* 217 */:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbBtLocalName = setUsbBtLocalName(data.readString());
                    reply.writeNoException();
                    reply.writeInt(usbBtLocalName ? 1 : 0);
                    return true;
                case TRANSACTION_getUsbAddress /* 218 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result43 = getUsbAddress();
                    reply.writeNoException();
                    reply.writeString(_result43);
                    return true;
                case TRANSACTION_getUsbConnectedDevice /* 219 */:
                    data.enforceInterface(DESCRIPTOR);
                    String _result44 = getUsbConnectedDevice();
                    reply.writeNoException();
                    reply.writeString(_result44);
                    return true;
                case 220:
                    data.enforceInterface(DESCRIPTOR);
                    int _result45 = getLocalUsbConnectionState();
                    reply.writeNoException();
                    reply.writeInt(_result45);
                    return true;
                case 221:
                    data.enforceInterface(DESCRIPTOR);
                    int _result46 = getProfileConnectionState(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result46);
                    return true;
                case 222:
                    data.enforceInterface(DESCRIPTOR);
                    boolean usbDisConnectDevice = usbDisConnectDevice(data.readString());
                    reply.writeNoException();
                    reply.writeInt(usbDisConnectDevice ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements UiCommand {
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

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getUiServiceVersionName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isAvrcpServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isA2dpServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isSppServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isBluetoothServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHidServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isPbapServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isOppServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isMapServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getA2dpConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isA2dpConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getA2dpConnectedAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqA2dpConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqA2dpDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void pauseA2dpRender() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void startA2dpRender() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setA2dpLocalVolume(float vol) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(vol);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setA2dpStreamType(int type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getA2dpStreamType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getAvrcpConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isAvrcpConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getAvrcpConnectedAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isAvrcp13Supported(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isAvrcp14Supported(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpPlay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpStop() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpPause() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpForward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpBackward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpVolumeUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpVolumeDown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpStartFastForward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpStopFastForward() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpStartRewind() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpStopRewind() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetCapabilitiesSupportEvent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetPlayerSettingAttributesList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(attributeId);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetPlayerSettingCurrentValues() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13SetPlayerSettingValue(byte attributeId, byte valueId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(attributeId);
                    _data.writeByte(valueId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetElementAttributesPlaying() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13GetPlayStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(eventId);
                    _data.writeLong(interval);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcpUnregisterEventWatcher(byte eventId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(eventId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13NextGroup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp13PreviousGroup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isAvrcp14BrowsingChannelEstablished() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14SetAddressedPlayer(int playerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(playerId);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14SetBrowsedPlayer(int playerId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(playerId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14GetFolderItems(byte scopeId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(scopeId);
                    this.mRemote.transact(Stub.TRANSACTION_reqAvrcp14GetFolderItems, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uidCounter);
                    _data.writeLong(uid);
                    _data.writeByte(direction);
                    this.mRemote.transact(Stub.TRANSACTION_reqAvrcp14ChangePath, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter, long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(scopeId);
                    _data.writeInt(uidCounter);
                    _data.writeLong(uid);
                    this.mRemote.transact(Stub.TRANSACTION_reqAvrcp14GetItemAttributes, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter, long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(scopeId);
                    _data.writeInt(uidCounter);
                    _data.writeLong(uid);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14Search(String text) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(text);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter, long uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(scopeId);
                    _data.writeInt(uidCounter);
                    _data.writeLong(uid);
                    this.mRemote.transact(Stub.TRANSACTION_reqAvrcp14AddToNowPlaying, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqAvrcp14SetAbsoluteVolume(byte volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(volume);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerBtCallback(UiCallbackBluetooth cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterBtCallback(UiCallbackBluetooth cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getNfServiceVersionName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setBtEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setBtDiscoverableTimeout(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean startBtDiscovery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean cancelBtDiscovery() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqBtPair(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqBtUnpair(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqBtPairedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getBtLocalName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getBtRemoteDeviceName(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getBtLocalAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setBtLocalName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isBtEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isBtEnabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isBtDiscovering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isBtDiscoverable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isBtAutoConnectEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(82, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int reqBtConnectHfpA2dp(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqBtConnectHfpA2dp, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int reqBtDisconnectAll() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqBtDisconnectAll, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtRemoteUuids(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_getBtRemoteUuids, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean switchBtRoleMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(Stub.TRANSACTION_switchBtRoleMode, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtRoleMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getBtRoleMode, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void setBtAutoConnect(int condition, int period) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(condition);
                    _data.writeInt(period);
                    this.mRemote.transact(Stub.TRANSACTION_setBtAutoConnect, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtAutoConnectCondition() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getBtAutoConnectCondition, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtAutoConnectPeriod() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getBtAutoConnectState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getBtAutoConnectingAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerHfpCallback(UiCallbackHfp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterHfpCallback(UiCallbackHfp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getHfpConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getHfpConnectedAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getHfpAudioConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getHfpRemoteSignalStrength() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public List<NfHfpClientCall> getHfpCallList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                    List<NfHfpClientCall> _result = _reply.createTypedArrayList(NfHfpClientCall.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpRemoteOnRoaming() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getHfpRemoteBatteryIndicator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpRemoteTelecomServiceOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(105, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpRemoteVoiceDialOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(106, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpDialCall(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(107, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpReDial() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(108, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpMemoryDial(String index) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(index);
                    this.mRemote.transact(109, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpAnswerCall(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(110, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpRejectIncomingCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqHfpRejectIncomingCall, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpTerminateCurrentCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqHfpTerminateCurrentCall, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpSendDtmf(String number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(number);
                    this.mRemote.transact(113, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpAudioTransferToCarkit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(114, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpAudioTransferToPhone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(115, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getHfpRemoteNetworkOperator() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(116, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getHfpRemoteSubscriberNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(117, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHfpVoiceDial(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(118, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void pauseHfpRender() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(119, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void startHfpRender() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(120, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpMicMute() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(121, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void muteHfpMic(boolean mute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mute ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_muteHfpMic, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHfpInBandRingtoneSupport() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(123, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerPbapCallback(UiCallbackPbap cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerPbapCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterPbapCallback(UiCallbackPbap cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(125, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getPbapConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(126, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isPbapDownloading() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isPbapDownloading, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getPbapDownloadingAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(128, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownload(String address, int storage, int property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownload, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    _data.writeInt(startPos);
                    _data.writeInt(offset);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownloadRange, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadToDatabase(String address, int storage, int property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownloadToDatabase, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadRangeToDatabase(String address, int storage, int property, int startPos, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    _data.writeInt(startPos);
                    _data.writeInt(offset);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownloadRangeToDatabase, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadToContactsProvider(String address, int storage, int property) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownloadToContactsProvider, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(storage);
                    _data.writeInt(property);
                    _data.writeInt(startPos);
                    _data.writeInt(offset);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDownloadRangeToContactsProvider, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqPbapDatabaseQueryNameByNumber(String address, String target) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(target);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDatabaseQueryNameByNumber, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqPbapDatabaseQueryNameByPartialNumber(String address, String target, int findPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(target);
                    _data.writeInt(findPosition);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDatabaseQueryNameByPartialNumber, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqPbapDatabaseAvailable(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDatabaseAvailable, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqPbapDeleteDatabaseByAddress(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapDeleteDatabaseByAddress, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqPbapCleanDatabase() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqPbapCleanDatabase, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqPbapDownloadInterrupt(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(140, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setPbapDownloadNotify(int frequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(frequency);
                    this.mRemote.transact(Stub.TRANSACTION_setPbapDownloadNotify, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerSppCallback(UiCallbackSpp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerSppCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterSppCallback(UiCallbackSpp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(143, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqSppConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqSppConnect, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqSppDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqSppDisconnect, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqSppConnectedDeviceAddressList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqSppConnectedDeviceAddressList, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isSppConnected(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_isSppConnected, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqSppSendData(String address, byte[] sppData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeByteArray(sppData);
                    this.mRemote.transact(Stub.TRANSACTION_reqSppSendData, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerHidCallback(UiCallbackHid cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerHidCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterHidCallback(UiCallbackHid cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(150, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getHidConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getHidConnectionState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isHidConnected() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(152, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getHidConnectedAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getHidConnectedAddress, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHidConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqHidConnect, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqHidDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqHidDisconnect, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqSendHidMouseCommand(int button, int offset_x, int offset_y, int wheel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(button);
                    _data.writeInt(offset_x);
                    _data.writeInt(offset_y);
                    _data.writeInt(wheel);
                    this.mRemote.transact(Stub.TRANSACTION_reqSendHidMouseCommand, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqSendHidVirtualKeyCommand(int key_1, int key_2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(key_1);
                    _data.writeInt(key_2);
                    this.mRemote.transact(Stub.TRANSACTION_reqSendHidVirtualKeyCommand, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerMapCallback(UiCallbackMap cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerMapCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterMapCallback(UiCallbackMap cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unregisterMapCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapDownloadSingleMessage(String address, int folder, String handle, int storage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(folder);
                    _data.writeString(handle);
                    _data.writeInt(storage);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapDownloadSingleMessage, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapDownloadMessage(String address, int folder, boolean isContentDownload, int count, int startPos, int storage, String periodBegin, String periodEnd, String sender, String recipient, int readStatus, int typeFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(folder);
                    _data.writeInt(isContentDownload ? 1 : 0);
                    try {
                        _data.writeInt(count);
                        try {
                            _data.writeInt(startPos);
                            try {
                                _data.writeInt(storage);
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
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
                    try {
                        _data.writeString(periodBegin);
                        try {
                            _data.writeString(periodEnd);
                            try {
                                _data.writeString(sender);
                                try {
                                    _data.writeString(recipient);
                                } catch (Throwable th5) {
                                    th = th5;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(readStatus);
                            try {
                                _data.writeInt(typeFilter);
                                try {
                                    this.mRemote.transact(Stub.TRANSACTION_reqMapDownloadMessage, _data, _reply, 0);
                                    _reply.readException();
                                    boolean _result = _reply.readInt() != 0;
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th8) {
                                    th = th8;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th9) {
                                th = th9;
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

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapRegisterNotification(String address, boolean downloadNewMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(downloadNewMessage ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapRegisterNotification, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqMapUnregisterNotification(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapUnregisterNotification, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isMapNotificationRegistered(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_isMapNotificationRegistered, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapDownloadInterrupt(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapDownloadInterrupt, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqMapDatabaseAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapDatabaseAvailable, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqMapDeleteDatabaseByAddress(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapDeleteDatabaseByAddress, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqMapCleanDatabase() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapCleanDatabase, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getMapCurrentState(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_getMapCurrentState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getMapRegisterState(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_getMapRegisterState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapSendMessage(String address, String message, String target) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(message);
                    _data.writeString(target);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapSendMessage, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapDeleteMessage(String address, int folder, String handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(folder);
                    _data.writeString(handle);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapDeleteMessage, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(folder);
                    _data.writeString(handle);
                    _data.writeInt(isReadStatus ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_reqMapChangeReadStatus, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setMapDownloadNotify(int frequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(frequency);
                    this.mRemote.transact(Stub.TRANSACTION_setMapDownloadNotify, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerOppCallback(UiCallbackOpp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerOppCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterOppCallback(UiCallbackOpp cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unregisterOppCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setOppFilePath(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(Stub.TRANSACTION_setOppFilePath, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getOppFilePath() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getOppFilePath, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqOppAcceptReceiveFile(boolean accept) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(accept ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_reqOppAcceptReceiveFile, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqOppInterruptReceiveFile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqOppInterruptReceiveFile, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void setTargetAddress(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_setTargetAddress, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getTargetAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getTargetAddress, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public void reqAvrcpUpdateSongStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqAvrcpUpdateSongStatus, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isGattServiceReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isGattServiceReady, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registerGattServerCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unregisterGattServerCallback, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getGattServerConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getGattServerConnectionState, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerDisconnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerDisconnect, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerBeginServiceDeclaration(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srvcType);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerBeginServiceDeclaration, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerAddCharacteristic(ParcelUuid charUuid, int properties, int permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(properties);
                    _data.writeInt(permissions);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerAddCharacteristic, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerAddDescriptor(ParcelUuid descUuid, int permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (descUuid != null) {
                        _data.writeInt(1);
                        descUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(permissions);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerAddDescriptor, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerEndServiceDeclaration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(192, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerRemoveService(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srvcType);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerRemoveService, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerClearServices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerClearServices, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerListen(boolean listen) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(listen ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerListen, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerSendResponse(String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(requestId);
                    _data.writeInt(status);
                    _data.writeInt(offset);
                    _data.writeByteArray(value);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerSendResponse, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean reqGattServerSendNotification(String address, int srvcType, ParcelUuid srvcUuid, ParcelUuid charUuid, boolean confirm, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(srvcType);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(confirm ? 1 : 0);
                    _data.writeByteArray(value);
                    this.mRemote.transact(Stub.TRANSACTION_reqGattServerSendNotification, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public List<ParcelUuid> getGattAddedGattServiceUuidList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getGattAddedGattServiceUuidList, _data, _reply, 0);
                    _reply.readException();
                    List<ParcelUuid> _result = _reply.createTypedArrayList(ParcelUuid.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public List<ParcelUuid> getGattAddedGattCharacteristicUuidList(ParcelUuid srvcUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(199, _data, _reply, 0);
                    _reply.readException();
                    List<ParcelUuid> _result = _reply.createTypedArrayList(ParcelUuid.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public List<ParcelUuid> getGattAddedGattDescriptorUuidList(ParcelUuid srvcUuid, ParcelUuid charUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srvcUuid != null) {
                        _data.writeInt(1);
                        srvcUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (charUuid != null) {
                        _data.writeInt(1);
                        charUuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(200, _data, _reply, 0);
                    _reply.readException();
                    List<ParcelUuid> _result = _reply.createTypedArrayList(ParcelUuid.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean registerUsbCallback(UiCallbackUsbBluetooth cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(201, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean unregisterUsbCallback(UiCallbackUsbBluetooth cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    this.mRemote.transact(202, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbSearch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(203, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbStopSearch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(204, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbConnect(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(205, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbDisConnect() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(206, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbReqUnpair(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(207, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setUsbEnabled(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    this.mRemote.transact(208, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isUsbEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isUsbEnabled, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isUsbDiscovering() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isUsbDiscovering, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isUsbHfpConnected(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(211, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isUsbA2dpConnected(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(212, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getUsbConnectionState(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(213, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean isUsbAvrcpConnected(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(214, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public List<UsbBluetoothDevice> reqUsbBtPairedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(215, _data, _reply, 0);
                    _reply.readException();
                    List<UsbBluetoothDevice> _result = _reply.createTypedArrayList(UsbBluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getUsbBtLocalName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getUsbBtLocalName, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean setUsbBtLocalName(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    this.mRemote.transact(Stub.TRANSACTION_setUsbBtLocalName, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getUsbAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getUsbAddress, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public String getUsbConnectedDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getUsbConnectedDevice, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getLocalUsbConnectionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(220, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public int getProfileConnectionState(int profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    this.mRemote.transact(221, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nforetek.bt.aidl.UiCommand
            public boolean usbDisConnectDevice(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(222, _data, _reply, 0);
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
