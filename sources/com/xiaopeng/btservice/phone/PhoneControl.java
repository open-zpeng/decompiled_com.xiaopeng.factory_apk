package com.xiaopeng.btservice.phone;

import android.content.Context;
import android.util.Log;
import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.UiCallbackHfp;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsControl;
import com.xiaopeng.btservice.base.AbsPhoneControlCallback;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class PhoneControl extends AbsControl {
    private static final String TAG = "PhoneControl";
    private AbsPhoneControlCallback mCallback;
    private UiCallbackHfp mCallbackHfp = new UiCallbackHfp.Stub() { // from class: com.xiaopeng.btservice.phone.PhoneControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpServiceReady() {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpServiceReady();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpStateChanged(String address, int prevState, int newState) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpAudioStateChanged(String address, int prevState, int newState) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpAudioStateChanged(address, prevState, newState);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpVoiceDial(String address, boolean isVoiceDialOn) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpVoiceDial(address, isVoiceDialOn);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpErrorResponse(String address, int code) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpErrorResponse(address, code);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpRemoteTelecomService(address, isTelecomServiceOn);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpRemoteRoamingStatus(address, isRoamingOn);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpRemoteBatteryIndicator(address, currentValue, maxValue, minValue);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpRemoteSignalStrength(address, currentStrength, maxStrength, minStrength);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void onHfpCallChanged(String address, NfHfpClientCall call) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.onHfpCallChanged(address, call.getId(), call.getState(), call.getNumber(), call.isMultiParty(), call.isOutgoing());
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackHfp
        public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) {
            if (PhoneControl.this.mCallback != null) {
                PhoneControl.this.mCallback.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
            }
        }
    };

    public PhoneControl(Context context, AbsPhoneControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            btService.registerHfpCallback(this.mCallbackHfp);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            if (this.nForeService != null) {
                this.nForeService.unregisterHfpCallback(this.mCallbackHfp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getHfpConnectionState() {
        printLog(TAG, "getHfpConnectionState");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int state = this.nForeService.getHfpConnectionState();
            return state;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public boolean isHfpConnected() {
        printLog(TAG, "isHfpConnected");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isConnected = this.nForeService.isHfpConnected();
            return isConnected;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean dialNumber(String number) {
        printLog(TAG, "dialNumber");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isRemoteTelecomServiceOn = this.nForeService.isHfpRemoteTelecomServiceOn();
            Log.v("yfl", "isRemoteTelecomServiceOn = " + isRemoteTelecomServiceOn);
            boolean isSuccess = this.nForeService.reqHfpDialCall(number);
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpReDial() {
        printLog(TAG, "reqHfpReDial");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpReDial();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpRejectIncomingCall() {
        printLog(TAG, "reqHfpRejectIncomingCall");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpRejectIncomingCall();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpTerminateCurrentCall() {
        printLog(TAG, "reqHfpTerminateCurrentCall");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpTerminateCurrentCall();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpAnswerCall(int flag) {
        printLog(TAG, "reqHfpAnswerCall");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpAnswerCall(flag);
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpAudioTransferToCarkit() {
        printLog(TAG, "reqHfpAudioTransferToCarkit");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpAudioTransferToCarkit();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpAudioTransferToPhone() {
        printLog(TAG, "reqHfpAudioTransferToPhone");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpAudioTransferToPhone();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpVoiceDial(boolean enable) {
        printLog(TAG, "reqHfpVoiceDial");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpVoiceDial(enable);
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public boolean reqHfpSendDtmf(String number) {
        printLog(TAG, "reqHfpSendDtmf");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.reqHfpSendDtmf(number);
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public String getHfpRemoteSubscriberNumber() {
        printLog(TAG, "getHfpRemoteSubscriberNumber");
        if (this.nForeService == null) {
            return null;
        }
        try {
            String localNumber = this.nForeService.getHfpRemoteSubscriberNumber();
            return localNumber;
        } catch (Exception e) {
            printError(TAG, e);
            return null;
        }
    }

    public String getHfpRemoteNetworkOperator() {
        printLog(TAG, "getHfpRemoteNetworkOperator");
        if (this.nForeService == null) {
            return null;
        }
        try {
            String operator = this.nForeService.getHfpRemoteNetworkOperator();
            return operator;
        } catch (Exception e) {
            printError(TAG, e);
            return null;
        }
    }

    public boolean isCurrentInCall(OnCurrentInCallListener callListener) {
        printLog(TAG, "isCurrentInCall");
        if (this.nForeService == null) {
            return false;
        }
        try {
            List<NfHfpClientCall> callList = this.nForeService.getHfpCallList();
            if (callList != null && callList.size() > 0) {
                if (callListener != null) {
                    NfHfpClientCall call = callList.get(0);
                    String address = getHfpConnectedAddress(TAG);
                    callListener.onHfpCallChanged(address, call.getId(), call.getState(), call.getNumber(), call.isMultiParty(), call.isOutgoing());
                    return true;
                }
                return true;
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return false;
    }

    public boolean isHas2ndCall() {
        printLog(TAG, "isHas2ndCall");
        if (this.nForeService == null) {
            return false;
        }
        try {
            List<NfHfpClientCall> callList = this.nForeService.getHfpCallList();
            if (callList != null) {
                if (callList.size() > 1) {
                    return true;
                }
            }
        } catch (Exception e) {
            printError(TAG, e);
        }
        return false;
    }

    public boolean isHfpInBandRingtoneSupport() {
        printLog(TAG, "isHfpInBandRingtoneSupport");
        if (this.nForeService == null) {
            return false;
        }
        try {
            boolean isSuccess = this.nForeService.isHfpInBandRingtoneSupport();
            return isSuccess;
        } catch (Exception e) {
            printError(TAG, e);
            return false;
        }
    }

    public int getHfpRemoteBatteryIndicator() {
        printLog(TAG, "getHfpRemoteBatteryIndicator");
        if (this.nForeService == null) {
            return -1;
        }
        try {
            int batteryLevel = this.nForeService.getHfpRemoteBatteryIndicator();
            return batteryLevel;
        } catch (Exception e) {
            printError(TAG, e);
            return -1;
        }
    }

    public List<NfHfpClientCall> getCallList() {
        printLog(TAG, "getCallList");
        if (this.nForeService != null) {
            try {
                return this.nForeService.getHfpCallList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    public int getAudioConnectionState() {
        printLog(TAG, "getAudioConnectionState");
        if (this.nForeService != null) {
            try {
                return this.nForeService.getHfpAudioConnectionState();
            } catch (Exception e) {
                e.printStackTrace();
                return 100;
            }
        }
        return 100;
    }
}
