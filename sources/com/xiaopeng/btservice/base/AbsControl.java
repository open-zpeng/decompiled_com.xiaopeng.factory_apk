package com.xiaopeng.btservice.base;

import android.content.Context;
import android.util.Log;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbstractConnection;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public abstract class AbsControl {
    List<AbstractConnection.ConnecteCompletedCallback> mConnectCompletedCallbacks = new ArrayList();
    public AbstractConnection mConnection = new AbstractConnection() { // from class: com.xiaopeng.btservice.base.AbsControl.1
        @Override // com.xiaopeng.btservice.base.AbstractConnection
        public void registerConnecteCompletedCallback(AbstractConnection.ConnecteCompletedCallback connecteCompleted) {
            synchronized (AbsControl.this.mConnectCompletedCallbacks) {
                AbsControl.this.mConnectCompletedCallbacks.add(connecteCompleted);
            }
        }

        @Override // com.xiaopeng.btservice.base.AbstractConnection
        public void unregisterConnecteCompletedCallback(AbstractConnection.ConnecteCompletedCallback connecteCompleted) {
            synchronized (AbsControl.this.mConnectCompletedCallbacks) {
                AbsControl.this.mConnectCompletedCallbacks.remove(connecteCompleted);
            }
        }

        private void notifyConnecteCompletedCallback() {
            synchronized (AbsControl.this.mConnectCompletedCallbacks) {
                for (AbstractConnection.ConnecteCompletedCallback callback : AbsControl.this.mConnectCompletedCallbacks) {
                    callback.connectServiceCompleted();
                }
            }
        }

        @Override // com.xiaopeng.btservice.base.AbstractConnection
        public void register(UiCommand nForeService) {
            AbsControl.this.registerCallback(nForeService);
            notifyConnecteCompletedCallback();
        }

        @Override // com.xiaopeng.btservice.base.AbstractConnection
        public void unRegister() {
            AbsControl.this.release();
        }

        @Override // com.xiaopeng.btservice.base.AbstractConnection
        public void onDisconnected() {
            AbsControl.this.release();
        }
    };
    public Context mContext;
    public UiCommand nForeService;

    protected abstract void registerCallback(UiCommand uiCommand);

    protected abstract void release();

    public String getHfpConnectedAddress(String tag) {
        printLog(tag, "getHfpConnectedAddress");
        UiCommand uiCommand = this.nForeService;
        if (uiCommand == null) {
            return "";
        }
        try {
            String address1 = uiCommand.getHfpConnectedAddress();
            return address1;
        } catch (Exception e) {
            printError(tag, e);
            return "";
        }
    }

    public void printError(String tag, Exception e) {
        Log.e(tag, "" + e);
    }

    public void printLog(String tag, String method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method);
        sb.append(" pkg:");
        Context context = this.mContext;
        sb.append(context != null ? context.getPackageName() : "");
        Log.d(tag, sb.toString());
        if (this.nForeService == null) {
            Log.d(tag, "nForeService == null");
        }
    }
}
