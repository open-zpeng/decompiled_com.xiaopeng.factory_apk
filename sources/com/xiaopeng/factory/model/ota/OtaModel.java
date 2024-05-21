package com.xiaopeng.factory.model.ota;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import com.xiaopeng.commonfunc.system.runnable.Sleep;
import com.xiaopeng.commonfunc.utils.AtUtil;
import com.xiaopeng.factory.MyApplication;
import com.xiaopeng.factory.atcommand.ResponseWriter;
import com.xiaopeng.lib.http.ICallback;
import com.xiaopeng.lib.utils.LogUtils;
import com.xiaopeng.lib.utils.ThreadUtils;
import com.xiaopeng.libconfig.ipc.IpcConfig;
import com.xiaopeng.ota.IOTAService;
/* loaded from: classes.dex */
public class OtaModel {
    private static final String TAG = "OtaModel";
    private static OtaModel sOtaModel;
    private final Context mContext;
    private boolean mHasServiceBind;
    private IOTAService mOTAService;
    private ResponseWriter mResponseWriter;
    private final ServiceConnection mServiceConn = new ServiceConnection() { // from class: com.xiaopeng.factory.model.ota.OtaModel.1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i(OtaModel.TAG, "onServiceDisconnected");
            OtaModel.this.mOTAService = null;
            OtaModel.this.mHasServiceBind = false;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i(OtaModel.TAG, "onServiceConnected");
            OtaModel.this.mOTAService = IOTAService.Stub.asInterface(service);
            OtaModel.this.mHasServiceBind = true;
        }
    };
    private int mTimes;

    private OtaModel(Context context) {
        this.mContext = context;
        bindService();
    }

    public static OtaModel getInstance() {
        if (sOtaModel == null) {
            sOtaModel = new OtaModel(MyApplication.getContext());
        }
        return sOtaModel;
    }

    public void setAtWriter(ResponseWriter responseWriter) {
        if (this.mResponseWriter != null) {
            this.mResponseWriter = responseWriter;
        }
    }

    private void bindService() {
        LogUtils.i(TAG, "bindService mHasServiceBind-->" + this.mHasServiceBind);
        Intent intent = new Intent();
        intent.setAction("com.xiaopeng.ota.system.OTAService");
        intent.setPackage(IpcConfig.App.CAR_OTA);
        this.mContext.startServiceAsUser(intent, UserHandle.CURRENT);
        if (!this.mHasServiceBind) {
            this.mContext.bindServiceAsUser(intent, this.mServiceConn, 1, UserHandle.CURRENT);
        }
    }

    private void unBind() {
        LogUtils.i(TAG, "unBind mHasServiceBind-->" + this.mHasServiceBind);
        if (this.mHasServiceBind) {
            this.mContext.unbindService(this.mServiceConn);
            this.mHasServiceBind = false;
        }
    }

    public boolean resetPsuKey(byte[] value) {
        boolean result = false;
        try {
            if (this.mHasServiceBind) {
                result = this.mOTAService.resetPsuKey(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "resetPsuKey mHasServiceBind = " + this.mHasServiceBind + ", result = " + result);
        return result;
    }

    public void asynResetPsuKey(final byte[] value) {
        this.mTimes = 5;
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.model.ota.-$$Lambda$OtaModel$3Z3Hn-GP4kSIODa7RVLFN383X0k
            @Override // java.lang.Runnable
            public final void run() {
                OtaModel.this.lambda$asynResetPsuKey$0$OtaModel(value);
            }
        });
    }

    public /* synthetic */ void lambda$asynResetPsuKey$0$OtaModel(byte[] value) {
        while (!this.mHasServiceBind && this.mTimes != 0) {
            try {
                this.mTimes--;
                Thread.sleep(1000L);
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            } catch (InterruptedException e2) {
                e2.printStackTrace();
                return;
            }
        }
        LogUtils.i(TAG, "asynResetPsuKey mHasServiceBind = " + this.mHasServiceBind);
        this.mOTAService.resetPsuKey(value);
    }

    public String getPsuVersion() {
        String psuVersion = null;
        try {
            if (this.mHasServiceBind) {
                psuVersion = this.mOTAService.getPsuVersion();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, "getPsuVersion mHasServiceBind = " + this.mHasServiceBind + "psuVersion = " + psuVersion);
        return psuVersion;
    }

    public void asynGetPsuVersion(final ICallback<String, String> callback) {
        ThreadUtils.execute(new Runnable() { // from class: com.xiaopeng.factory.model.ota.-$$Lambda$OtaModel$DtE2dNqo2AMXzQumjXLWj1sJHqE
            @Override // java.lang.Runnable
            public final void run() {
                OtaModel.this.lambda$asynGetPsuVersion$1$OtaModel(callback);
            }
        });
    }

    public /* synthetic */ void lambda$asynGetPsuVersion$1$OtaModel(ICallback callback) {
        int times = 5;
        String psuVersion = null;
        while (!this.mHasServiceBind) {
            try {
                int times2 = times - 1;
                if (times == 0) {
                    break;
                }
                try {
                    Thread.sleep(1000L);
                    times = times2;
                } catch (Exception e) {
                    e = e;
                    if (callback != null) {
                        callback.onError(null);
                    }
                    e.printStackTrace();
                    return;
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
        if (this.mOTAService != null) {
            psuVersion = this.mOTAService.getPsuVersion();
        }
        LogUtils.i(TAG, "asynGetPsuVersion mHasServiceBind = " + this.mHasServiceBind + " psuVersion = " + psuVersion);
        if (callback != null) {
            callback.onSuccess(psuVersion);
        } else if (this.mResponseWriter != null) {
            this.mResponseWriter.write(AtUtil.responseString(AtUtil.Versname.CMD_NAME, "1", psuVersion));
            onDestroy();
        }
    }

    public String getPsuId(boolean isActivated, long timeout) {
        int times = 5;
        String psuId = null;
        while (!this.mHasServiceBind) {
            try {
                int times2 = times - 1;
                if (times == 0) {
                    break;
                }
                try {
                    Sleep.sleep(1000L);
                    times = times2;
                } catch (RemoteException e) {
                    e = e;
                    e.printStackTrace();
                    return psuId;
                }
            } catch (RemoteException e2) {
                e = e2;
            }
        }
        if (this.mOTAService != null) {
            psuId = this.mOTAService.getPsuId(isActivated, timeout);
        }
        LogUtils.i(TAG, "getPsuId mHasServiceBind = " + this.mHasServiceBind + " isActivated = " + isActivated + " psuId = " + psuId);
        return psuId;
    }

    public boolean activatePsu() {
        int times = 5;
        boolean result = false;
        while (!this.mHasServiceBind) {
            try {
                int times2 = times - 1;
                if (times == 0) {
                    break;
                }
                try {
                    Sleep.sleep(1000L);
                    times = times2;
                } catch (RemoteException e) {
                    e = e;
                    e.printStackTrace();
                    return result;
                }
            } catch (RemoteException e2) {
                e = e2;
            }
        }
        if (this.mOTAService != null) {
            result = this.mOTAService.activatePsu();
        }
        LogUtils.i(TAG, "activatePsu mHasServiceBind = " + this.mHasServiceBind + " result = " + result);
        return result;
    }

    public void onDestroy() {
        unBind();
    }
}
