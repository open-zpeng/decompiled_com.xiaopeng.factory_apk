package com.xpeng.upso.aesserver;

import androidx.annotation.Keep;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.commonfunc.Constant;
import com.xpeng.upso.http.UpsoResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Keep
/* loaded from: classes2.dex */
public class AesDecryptMultiResponse extends UpsoResponse {
    private static final String TAG = "Upso-DecMultiRes";
    private List<String> results;

    public void parse(String response) throws JsonSyntaxException, IllegalStateException, NumberFormatException {
        this.results = new ArrayList();
        TreeMap<Integer, String> encodedMap = new TreeMap<>();
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(response);
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            JsonObject temp = stringJsonElementEntry.getValue().getAsJsonObject();
            if (temp != null) {
                int index = Integer.parseInt(stringJsonElementEntry.getKey());
                String text = temp.get(Constant.KEY_RAW_DATA).getAsString();
                encodedMap.put(Integer.valueOf(index), text);
            }
        }
        for (String code : encodedMap.values()) {
            this.results.add(code);
        }
    }

    public List<String> GetResult() {
        return this.results;
    }
}
