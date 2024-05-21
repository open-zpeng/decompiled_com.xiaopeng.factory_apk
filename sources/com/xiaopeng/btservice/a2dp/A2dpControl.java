package com.xiaopeng.btservice.a2dp;

import android.content.Context;
import com.nforetek.bt.aidl.UiCallbackA2dp;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsA2dpControlCallback;
import com.xiaopeng.btservice.base.AbsControl;
/* loaded from: classes.dex */
public class A2dpControl extends AbsControl {
    private static final String TAG = "A2dpControl";
    private AbsA2dpControlCallback mCallback;
    private UiCallbackA2dp mCallbackA2dp = new UiCallbackA2dp.Stub() { // from class: com.xiaopeng.btservice.a2dp.A2dpControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackA2dp
        public void onA2dpServiceReady() {
            A2dpControl.this.mCallback.onA2dpServiceReady();
        }

        @Override // com.nforetek.bt.aidl.UiCallbackA2dp
        public void onA2dpStateChanged(String address, int prevState, int newState) {
            A2dpControl.this.mCallback.onA2dpStateChanged(address, prevState, newState);
        }
    };

    public A2dpControl(Context mContext, AbsA2dpControlCallback mCallback) {
        this.mContext = mContext;
        this.mCallback = mCallback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            btService.registerA2dpCallback(this.mCallbackA2dp);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            if (this.nForeService != null) {
                this.nForeService.unregisterA2dpCallback(this.mCallbackA2dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        if (this.nForeService != null) {
            try {
                this.nForeService.isA2dpServiceReady();
                return false;
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
                return this.nForeService.isA2dpConnected();
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
                return this.nForeService.getA2dpConnectionState();
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
                return this.nForeService.reqA2dpConnect(address);
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
                this.nForeService.reqA2dpDisconnect(address);
                return false;
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
                return this.nForeService.getA2dpConnectedAddress();
            } catch (Exception e) {
                e.printStackTrace();
                return "00:00:00:00:00:00";
            }
        }
        return "00:00:00:00:00:00";
    }

    public boolean setLocalVolume(float volume) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.setA2dpLocalVolume(volume);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean setStreamType(int type) {
        if (this.nForeService != null) {
            try {
                return this.nForeService.setA2dpStreamType(type);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public int getStreamType() {
        if (this.nForeService != null) {
            try {
                return this.nForeService.getA2dpStreamType();
            } catch (Exception e) {
                e.printStackTrace();
                return 3;
            }
        }
        return 3;
    }

    public boolean startRender() {
        if (this.nForeService != null) {
            try {
                this.nForeService.startA2dpRender();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean pauseRender() {
        if (this.nForeService != null) {
            try {
                this.nForeService.pauseA2dpRender();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
