package com.xiaopeng.libbluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.xiaopeng.xpbluetoothservice.IXPBluetoothService;
import java.util.LinkedList;
import java.util.List;
/* loaded from: classes2.dex */
public class BluetoothBoxes {
    private static final boolean DEBUG = true;
    private static final String INTENT_BLUETOOTH_SERVICE = "com.xiaopeng.xpbluetoothservice.XpBluetoothService";
    private static final String PACKAGE_BLUETOOTH_SERVICE = "com.xiaopeng.xpbluetoothservice";
    private static final String TAG = BluetoothBoxes.class.getSimpleName();
    private static volatile BluetoothBoxes mInstance;
    private static String sID;
    private IXPBluetoothService mBtService;
    private ServiceConnection mBtServiceConnection = new ServiceConnection() { // from class: com.xiaopeng.libbluetooth.BluetoothBoxes.1
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName pName) {
            BluetoothBoxes.this.notifyDisconnected();
            try {
                BluetoothBoxes.this.mBtService.unRegisterName(BluetoothBoxes.sID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            BluetoothBoxes.this.mBtService = null;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName pName, IBinder pBinder) {
            BluetoothBoxes.this.mBtService = IXPBluetoothService.Stub.asInterface(pBinder);
            Log.d(BluetoothBoxes.TAG, Thread.currentThread().getName());
            Log.d(BluetoothBoxes.TAG, "onServiceConnected");
            if (BluetoothBoxes.this.mBtService != null) {
                try {
                    String unused = BluetoothBoxes.sID = BluetoothBoxes.this.mBtService.registerName(BluetoothBoxes.this.mContext.getPackageName());
                    Log.d("tang", "onServiceConnected_sID = " + BluetoothBoxes.sID);
                    BluetoothBoxes.this.notifyConnected(BluetoothBoxes.this.mBtService);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private List<AbstractConnection> mConnectionCallbacks = new LinkedList();
    private Context mContext;

    private BluetoothBoxes() {
    }

    public static BluetoothBoxes getInstance() {
        if (mInstance == null) {
            synchronized (BluetoothBoxes.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothBoxes();
                }
            }
        }
        return mInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void connectBluetooth() {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(PACKAGE_BLUETOOTH_SERVICE, INTENT_BLUETOOTH_SERVICE);
        intent.setComponent(componentName);
        this.mContext.bindServiceAsUser(intent, this.mBtServiceConnection, 1, UserHandle.CURRENT);
    }

    public void disconnectBluetooth() {
        for (AbstractConnection connection : this.mConnectionCallbacks) {
            unregisterCallback(connection);
        }
        this.mContext.unbindService(this.mBtServiceConnection);
    }

    public void registerCallback(AbsControlBox box) {
        registerCallback(box.mConnection);
    }

    public A2DPControlBox getA2DPControlBox(AbsA2DPControlCallback callback) {
        A2DPControlBox box = new A2DPControlBox(this.mContext, callback);
        return box;
    }

    public PhoneControlBox getPhoneControlBox(AbsPhoneControlCallback callback) {
        PhoneControlBox box = new PhoneControlBox(this.mContext, callback);
        return box;
    }

    public GeneralControlBox getGeneralControlBox(AbsGeneralControlCallback callback) {
        GeneralControlBox box = new GeneralControlBox(this.mContext, callback);
        return box;
    }

    public void releaseGeneralControlBox(GeneralControlBox box) {
        unregisterCallback(box.mConnection);
    }

    public void releasePhoneControlBox(PhoneControlBox box) {
        unregisterCallback(box.mConnection);
    }

    public void releaseA2DPControlBox(A2DPControlBox box) {
        unregisterCallback(box.mConnection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyDisconnected() {
        synchronized (this.mConnectionCallbacks) {
            for (AbstractConnection callback : this.mConnectionCallbacks) {
                callback.onDisconnected();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnected(IXPBluetoothService btService) {
        synchronized (this.mConnectionCallbacks) {
            for (AbstractConnection callback : this.mConnectionCallbacks) {
                callback.onConnected(btService);
            }
        }
    }

    private void registerCallback(AbstractConnection callback) {
        IXPBluetoothService iXPBluetoothService = this.mBtService;
        if (iXPBluetoothService != null) {
            callback.connectService(iXPBluetoothService);
        }
        synchronized (this.mConnectionCallbacks) {
            this.mConnectionCallbacks.add(callback);
        }
    }

    private void unregisterCallback(AbstractConnection callback) {
        callback.onUnregister();
        synchronized (this.mConnectionCallbacks) {
            this.mConnectionCallbacks.remove(callback);
        }
    }

    public static String getID() {
        return sID;
    }
}
