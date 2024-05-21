package com.xiaopeng.libconfig.ipc.event;

import cn.hutool.core.text.CharPool;
/* loaded from: classes2.dex */
public class Account2MqttEvent {
    private int msgType;
    private String value;

    public Account2MqttEvent(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "Account2MqttEvent{msgType=" + this.msgType + ", value='" + this.value + CharPool.SINGLE_QUOTE + '}';
    }
}
