package com.xiaopeng.btservice.base;
/* loaded from: classes.dex */
public abstract class AbsMAPControlCallback {
    public void onMapDownloadNotify(String address, int folder, int totalMessages, int currentMessages) {
    }

    public void onMapMemoryAvailableEvent(String address, int structure, boolean available) {
    }

    public void onMapMessageDeletedEvent(String address, String handle, int type, int folder) {
    }

    public void onMapMessageDeliverStatusEvent(String address, String handle, boolean isSuccess) {
    }

    public void onMapMessageSendingStatusEvent(String address, String handle, boolean isSuccess) {
    }

    public void onMapMessageShiftedEvent(String address, String handle, int type, int newFolder, int oldFolder) {
    }

    public void onMapNewMessageReceivedEvent(String address, String handle, String sender, String message) {
    }

    public void onMapServiceReady() {
    }

    public void onMapStateChanged(String address, int prevState, int newState, int reason) {
    }

    public void retMapChangeReadStatusCompleted(String address, String handle, int reason) {
    }

    public void retMapCleanDatabaseCompleted(boolean isSuccess) {
    }

    public void retMapDatabaseAvailable() {
    }

    public void retMapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) {
    }

    public void retMapDeleteMessageCompleted(String address, String handle, int reason) {
    }

    public void retMapDownloadedMessage(String address, String handle, String senderName, String senderNumber, String recipientNumber, String date, int type, int folder, boolean isReadStatus, String subject, String message) {
    }

    public void retMapSendMessageCompleted(String address, String target, int state) {
    }
}
