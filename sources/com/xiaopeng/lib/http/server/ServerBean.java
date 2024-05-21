package com.xiaopeng.lib.http.server;

import androidx.core.app.NotificationCompat;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes2.dex */
public class ServerBean {
    @SerializedName("code")
    private int mCode;
    @SerializedName("data")
    private String mData;
    @SerializedName(NotificationCompat.CATEGORY_MESSAGE)
    private String mMsg;

    public int getCode() {
        return this.mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public String getData() {
        return this.mData;
    }

    public void setData(String data) {
        this.mData = data;
    }

    public String getMsg() {
        return this.mMsg;
    }

    public void setMsg(String msg) {
        this.mMsg = msg;
    }
}
