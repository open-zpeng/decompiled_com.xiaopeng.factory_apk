package com.xiaopeng.btservice.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsBluetoothControlCallback;
import com.xiaopeng.btservice.base.AbsControl;
import com.xiaopeng.btservice.base.AbsMAPControlCallback;
import com.xiaopeng.btservice.base.AbsPBAPControlCallback;
import com.xiaopeng.btservice.base.AbsPhoneControlCallback;
import com.xiaopeng.btservice.base.AbsUsbBluetoothCallback;
import com.xiaopeng.btservice.base.AbstractConnection;
import com.xiaopeng.btservice.bluetooth.BluetoothControl;
import com.xiaopeng.btservice.map.MAPControl;
import com.xiaopeng.btservice.pbap.PBAPControl;
import com.xiaopeng.btservice.phone.PhoneControl;
import com.xiaopeng.btservice.usb.UsbBluetoothControl;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes.dex */
public class BtBoxesUtil {
    private static final boolean DEBUG = true;
    private static final String INTENT_BLUETOOTH_SERVICE = "com.xiaopeng.bt.ui.service.XpBtService";
    private static final String PACKAGE_BLUETOOTH_SERVICE = "com.xiaopeng.bt.ui.service";
    private static final String TAG = BtBoxesUtil.class.getSimpleName();
    private static volatile BtBoxesUtil mInstance;
    private Context mContext;
    private UiCommand nForeService;
    private ServiceConnection mBtServiceConnection = new ServiceConnection() { // from class: com.xiaopeng.btservice.util.BtBoxesUtil.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName pName, IBinder service) {
            BtBoxesUtil.this.nForeService = UiCommand.Stub.asInterface(service);
            String str = BtBoxesUtil.TAG;
            Log.i(str, "ready  onServiceConnected from : " + BtBoxesUtil.this.getPackageName());
            if (BtBoxesUtil.this.nForeService != null) {
                try {
                    BtBoxesUtil.this.nForeService.asBinder().linkToDeath(BtBoxesUtil.this.mDeathRecipient, 0);
                    BtBoxesUtil.this.notifyConnected(BtBoxesUtil.this.nForeService);
                    return;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
            }
            String str2 = BtBoxesUtil.TAG;
            Log.i(str2, "mBtService is null!! from : " + BtBoxesUtil.this.getPackageName());
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName pName) {
            String str = BtBoxesUtil.TAG;
            Log.i(str, " onServiceDisconnected from : " + BtBoxesUtil.this.getPackageName());
            BtBoxesUtil.this.notifyDisconnected();
            BtBoxesUtil.this.nForeService = null;
        }
    };
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.xiaopeng.btservice.util.BtBoxesUtil.2
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            String str = BtBoxesUtil.TAG;
            Log.i(str, "binderDied.nForeService = " + BtBoxesUtil.this.nForeService + " from : " + BtBoxesUtil.this.getPackageName());
            if (BtBoxesUtil.this.nForeService != null) {
                BtBoxesUtil.this.nForeService.asBinder().unlinkToDeath(this, 0);
                BtBoxesUtil.this.nForeService = null;
                BtBoxesUtil.this.connectBluetooth();
            }
        }
    };
    private List<AbstractConnection> mConnectionCallbacks = new LinkedList();

    public BtBoxesUtil(Context context) {
        this.mContext = context;
    }

    public static BtBoxesUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BtBoxesUtil.class) {
                if (mInstance == null) {
                    mInstance = new BtBoxesUtil(context);
                }
            }
        }
        return mInstance;
    }

    public MAPControl getMAPControl(AbsMAPControlCallback callback) {
        MAPControl control = new MAPControl(this.mContext, callback);
        return control;
    }

    public PhoneControl getPhoneControl(AbsPhoneControlCallback callback) {
        PhoneControl control = new PhoneControl(this.mContext, callback);
        return control;
    }

    public PBAPControl getPBAPControl(AbsPBAPControlCallback callback) {
        PBAPControl control = new PBAPControl(this.mContext, callback);
        return control;
    }

    public BluetoothControl getBluetoothControl(AbsBluetoothControlCallback callback) {
        BluetoothControl control = new BluetoothControl(this.mContext, callback);
        return control;
    }

    public UsbBluetoothControl getUsbBluetoothControl(AbsUsbBluetoothCallback callback) {
        UsbBluetoothControl control = new UsbBluetoothControl(this.mContext, callback);
        return control;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getPackageName() {
        Context context = this.mContext;
        return context == null ? "" : context.getPackageName();
    }

    public static BtBoxesUtil getInstance() {
        return mInstance;
    }

    public void connectBluetooth() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_BLUETOOTH_SERVICE, INTENT_BLUETOOTH_SERVICE);
        intent.setComponent(componentName);
        try {
            String str = TAG;
            Log.d(str, "connectBluetooth " + SystemClock.uptimeMillis() + " from : " + getPackageName());
            this.mContext.bindService(intent, this.mBtServiceConnection, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectBluetooth() {
        try {
            this.mContext.unbindService(this.mBtServiceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnected(UiCommand nForeService) {
        String str = TAG;
        Log.d(str, "notifyConnected  from : " + getPackageName() + " callback.size = " + this.mConnectionCallbacks.size());
        synchronized (this.mConnectionCallbacks) {
            for (AbstractConnection callback : this.mConnectionCallbacks) {
                callback.onConnected(nForeService);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDisconnected() {
        synchronized (this.mConnectionCallbacks) {
            String str = TAG;
            Log.d(str, "notifyDisconnected from : " + getPackageName());
            for (AbstractConnection callback : this.mConnectionCallbacks) {
                callback.unRegister();
            }
        }
    }

    public void registerCallback(AbsControl box) {
        registerCallback(box.mConnection);
    }

    private void registerCallback(AbstractConnection callback) {
        synchronized (this.mConnectionCallbacks) {
            this.mConnectionCallbacks.add(callback);
            Log.d(TAG, "mConnectionCallbacks.add");
        }
        String str = TAG;
        Log.d(str, "registerCallback " + this.nForeService + " from : " + getPackageName());
        UiCommand uiCommand = this.nForeService;
        if (uiCommand != null) {
            callback.register(uiCommand);
        }
    }

    public void unRegisterCallback(AbsControl box) {
        unRegisterCallback(box.mConnection);
    }

    private void unRegisterCallback(AbstractConnection callback) {
        callback.unRegister();
        String str = TAG;
        Log.d(str, "unRegisterCallback from : " + getPackageName());
        synchronized (this.mConnectionCallbacks) {
            this.mConnectionCallbacks.remove(callback);
        }
    }
}
