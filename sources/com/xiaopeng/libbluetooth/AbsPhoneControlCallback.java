package com.xiaopeng.libbluetooth;
/* loaded from: classes2.dex */
public abstract class AbsPhoneControlCallback {
    /* JADX INFO: Access modifiers changed from: protected */
    public void onCallStatus(int value, String number, String name) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onMissCall(int missedCallCount) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onIncomingSms(String number, String date, String content) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onHFPIndicator(int keyType, int typeValue) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPhoneBook(int status, int dataType, int cur, int total, int error, String name, String number, String time) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onPBAPCallback(int status, int dataType, int cur, int total, int error, String name, String number, String time) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSmsCallback(int status, int error, int cur, int total, String number, String date, String content) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRecordCallback(int type, String name) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBindSuccess() {
    }
}
