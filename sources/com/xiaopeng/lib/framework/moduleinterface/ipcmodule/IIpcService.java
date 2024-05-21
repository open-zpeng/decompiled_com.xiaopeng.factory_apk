package com.xiaopeng.lib.framework.moduleinterface.ipcmodule;

import android.os.Bundle;
import cn.hutool.core.text.CharPool;
/* loaded from: classes2.dex */
public interface IIpcService {
    void init();

    void sendData(int msgId, Bundle payloadData, String... appIds);

    /* loaded from: classes2.dex */
    public static class IpcMessageEvent {
        int mMsgID;
        Bundle mPayloadData;
        String mSenderPackageName;

        public String getSenderPackageName() {
            return this.mSenderPackageName;
        }

        public void setSenderPackageName(String senderPackageName) {
            this.mSenderPackageName = senderPackageName;
        }

        public int getMsgID() {
            return this.mMsgID;
        }

        public void setMsgID(int msgID) {
            this.mMsgID = msgID;
        }

        public Bundle getPayloadData() {
            return this.mPayloadData;
        }

        public void setPayloadData(Bundle payloadData) {
            this.mPayloadData = payloadData;
        }

        public String toString() {
            return "IpcMessageEvent{mSenderPackageName='" + this.mSenderPackageName + CharPool.SINGLE_QUOTE + ", mMsgID=" + this.mMsgID + ", mPayloadData=" + this.mPayloadData + '}';
        }
    }
}
