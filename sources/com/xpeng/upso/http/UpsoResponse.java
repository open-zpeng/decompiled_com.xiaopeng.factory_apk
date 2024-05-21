package com.xpeng.upso.http;

import androidx.annotation.Keep;
import androidx.core.app.NotificationCompat;
import com.google.gson.annotations.SerializedName;
@Keep
/* loaded from: classes2.dex */
public class UpsoResponse {
    @SerializedName("code")
    public String code;
    @SerializedName(NotificationCompat.CATEGORY_MESSAGE)
    public String msg;
}
