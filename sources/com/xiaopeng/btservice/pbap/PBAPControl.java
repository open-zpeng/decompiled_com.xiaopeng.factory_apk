package com.xiaopeng.btservice.pbap;

import android.content.Context;
import android.util.Log;
import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.aidl.UiCallbackPbap;
import com.nforetek.bt.aidl.UiCommand;
import com.xiaopeng.btservice.base.AbsControl;
import com.xiaopeng.btservice.base.AbsPBAPControlCallback;
/* loaded from: classes.dex */
public class PBAPControl extends AbsControl {
    private static final String TAG = "PBAPControl";
    private AbsPBAPControlCallback mCallback;
    private UiCallbackPbap mCallbackPbap = new UiCallbackPbap.Stub() { // from class: com.xiaopeng.btservice.pbap.PBAPControl.1
        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void onPbapServiceReady() {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.onPbapServiceReady();
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.onPbapStateChanged(address, prevState, newState, reason, counts);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDownloadedContact(NfPbapContact contact) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDownloadedContact(contact.getRemoteAddress(), contact.getFirstName(), contact.getMiddleName(), contact.getLastName(), contact.getNumberArray(), contact.getPhotoType(), contact.getPhotoByteArray());
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDownloadedCallLog(address, firstName, middleName, lastName, number, type, timestamp);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.onPbapDownloadNotify(address, storage, totalContacts, downloadedContacts);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDatabaseQueryNameByPartialNumber(address, target, names, numbers, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDatabaseAvailable(String address) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDatabaseAvailable(address);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapDeleteDatabaseByAddressCompleted(address, isSuccess);
            }
        }

        @Override // com.nforetek.bt.aidl.UiCallbackPbap
        public void retPbapCleanDatabaseCompleted(boolean isSuccess) {
            if (PBAPControl.this.mCallback != null) {
                PBAPControl.this.mCallback.retPbapCleanDatabaseCompleted(isSuccess);
            }
        }
    };

    public PBAPControl(Context context, AbsPBAPControlCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void registerCallback(UiCommand btService) {
        try {
            this.nForeService = btService;
            btService.registerPbapCallback(this.mCallbackPbap);
        } catch (Exception e) {
            printError(TAG, e);
        }
    }

    @Override // com.xiaopeng.btservice.base.AbsControl
    protected void release() {
        printLog(TAG, "release");
        try {
            if (this.nForeService != null) {
                this.nForeService.unregisterPbapCallback(this.mCallbackPbap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setPbapDownloadNotify(int frequency) {
        if (this.nForeService == null) {
            Log.d(TAG, "nForeService == null");
            return false;
        }
        try {
            return this.nForeService.setPbapDownloadNotify(frequency);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getPbapConnectionState() {
        printLog(TAG, "getPbapConnectionState");
        if (this.nForeService != null) {
            try {
                return this.nForeService.getPbapConnectionState();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public boolean reqPbapDownload(String address, int storage, int property) {
        printLog(TAG, "reqPbapDownload");
        if (this.nForeService != null) {
            try {
                if (storage == 2) {
                    return this.nForeService.reqPbapDownload(address, storage, property);
                }
                return this.nForeService.reqPbapDownloadRange(address, storage, property, 0, 50);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean reqPbapDownloadInterrupt(String address) {
        printLog(TAG, "reqPbapDownloadInterrupt");
        if (this.nForeService != null) {
            try {
                return this.nForeService.reqPbapDownloadInterrupt(address);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean isPbapDownloading() {
        printLog(TAG, "isPbapDownloading");
        if (this.nForeService != null) {
            try {
                return this.nForeService.isPbapDownloading();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
