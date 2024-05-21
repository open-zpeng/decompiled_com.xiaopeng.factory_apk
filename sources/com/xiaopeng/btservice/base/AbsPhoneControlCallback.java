package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsPhoneControlCallback {
    public void onHfpServiceReady() {
    }

    public void onHfpStateChanged(String address, int prevState, int newState) {
    }

    public void onHfpAudioStateChanged(String address, int prevState, int newState) {
    }

    public void onHfpVoiceDial(String address, boolean isVoiceDialOn) {
    }

    public void onHfpErrorResponse(String address, int code) {
    }

    public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) {
    }

    public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) {
    }

    public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) {
    }

    public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) {
    }

    public void onHfpCallChanged(String address, int id, int state, String number, boolean multiParty, boolean outgoing) {
    }

    public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) {
    }
}
