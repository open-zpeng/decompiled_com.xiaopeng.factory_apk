package com.xiaopeng.btservice.map;

import android.content.Context;
import android.os.RemoteException;
import com.nforetek.bt.aidl.UiCallbackMap;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsControl;
import com.xiaopeng.btservice.base.AbsMAPControlCallback;
/* loaded from: classes.dex */
public class MAPControl extends AbsControl {
    private static final String TAG = "PBAPControl";
    private AbsMAPControlCallback mCallback;
    private UiCallbackMap mCallbackMap = new UiCallbackMap.Stub() { // from class: com.xiaopeng.btservice.map.MAPControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapServiceReady() throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapServiceReady();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapStateChanged(String address, int prevState, int newState, int reason) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapStateChanged(address, prevState, newState, reason);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapDownloadedMessage(String address, String handle, String senderName, String senderNumber, String recipientNumber, String date, int type, int folder, boolean isReadStatus, String subject, String message) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapDownloadedMessage(address, handle, senderName, senderNumber, recipientNumber, date, type, folder, isReadStatus, subject, message);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapNewMessageReceivedEvent(String address, String handle, String sender, String message) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapNewMessageReceivedEvent(address, handle, sender, message);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapDownloadNotify(String address, int folder, int totalMessages, int currentMessages) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapDownloadNotify(address, folder, totalMessages, currentMessages);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapDatabaseAvailable() throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapDatabaseAvailable();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapDeleteDatabaseByAddressCompleted(address, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapCleanDatabaseCompleted(isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapSendMessageCompleted(String address, String target, int state) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapSendMessageCompleted(address, target, state);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapDeleteMessageCompleted(String address, String handle, int reason) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapDeleteMessageCompleted(address, handle, reason);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void retMapChangeReadStatusCompleted(String address, String handle, int reason) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.retMapChangeReadStatusCompleted(address, handle, reason);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapMemoryAvailableEvent(String address, int structure, boolean available) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapMemoryAvailableEvent(address, structure, available);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapMessageSendingStatusEvent(String address, String handle, boolean isSuccess) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapMessageSendingStatusEvent(address, handle, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapMessageDeliverStatusEvent(String address, String handle, boolean isSuccess) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapMessageDeliverStatusEvent(address, handle, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapMessageShiftedEvent(String address, String handle, int type, int newFolder, int oldFolder) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapMessageShiftedEvent(address, handle, type, newFolder, oldFolder);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackMap
        public void onMapMessageDeletedEvent(String address, String handle, int type, int folder) throws RemoteException {
            if (MAPControl.this.mCallback != null) {
                MAPControl.this.mCallback.onMapMessageDeletedEvent(address, handle, type, folder);
            }
        }
    };

    public MAPControl(Context context, AbsMAPControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            btService.registerMapCallback(this.mCallbackMap);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        try {
            if (this.nForeService != null) {
                this.nForeService.unregisterMapCallback(this.mCallbackMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
