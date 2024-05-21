package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsPBAPControlCallback {
    public void onPbapServiceReady() {
    }

    public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) {
    }

    public void retPbapDownloadedContact(String macAddress, String firstName, String middleName, String lastName, String[] numbers, int photoType, byte[] photoBytes) {
    }

    public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) {
    }

    public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) {
    }

    public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) {
    }

    public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) {
    }

    public void retPbapDatabaseAvailable(String address) {
    }

    public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) {
    }

    public void retPbapCleanDatabaseCompleted(boolean isSuccess) {
    }
}
