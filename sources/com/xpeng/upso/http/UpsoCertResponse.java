package com.xpeng.upso.http;

import androidx.annotation.Keep;
import com.google.gson.annotations.SerializedName;
import java.util.List;
@Keep
/* loaded from: classes2.dex */
public class UpsoCertResponse {
    private static List<String> results;
    @SerializedName("code")
    public String code;
    @SerializedName("data")
    public String data;

    public static List<String> GetResult() {
        return results;
    }

    public static void parse(List<String> list) {
        results = list;
    }
}
