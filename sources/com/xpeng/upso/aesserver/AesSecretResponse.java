package com.xpeng.upso.aesserver;

import androidx.annotation.Keep;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.http.UpsoResponse;
import com.xpeng.upso.util.SoftAesCryptor;
import com.xpeng.upso.utils.KeyUtils;
import com.xpeng.upso.utils.LogUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
@Keep
/* loaded from: classes2.dex */
public class AesSecretResponse extends UpsoResponse {
    private static final String TAG = "Upso-SecretRes";
    @SerializedName("secreteKey")
    public String secreteKey;
    @SerializedName("tboxKey")
    public String tboxKey;
    @SerializedName("xpuKey")
    public String xpuKey;

    private byte[] decode(String deviceId, String chiperedText) {
        if (chiperedText == null) {
            LogUtils.e(TAG, "getKeys chiperedText is null");
            return null;
        }
        LogUtils.d(TAG, "getKeys length = " + chiperedText.length());
        LogUtils.d(TAG, "getKeys deviceID = " + deviceId);
        try {
            byte[] key = KeyUtils.md5(deviceId);
            byte[] decoded = SoftAesCryptor.decryptWithEcbPkscs5(key, chiperedText);
            return decoded;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    private List<String> unwrapNumberedKeys(byte[] decoded) throws IllegalStateException {
        List<String> secrets = new ArrayList<>();
        String jsonBody = new String(decoded, StandardCharsets.UTF_8);
        LogUtils.d(TAG, "unwrapKeys: " + UpsoConfig.logLimited(jsonBody));
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonBody);
        Iterator<Map.Entry<String, JsonElement>> it = jsonObject.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, JsonElement> stringJsonElementEntry = it.next();
            JsonObject temp = stringJsonElementEntry.getValue().getAsJsonObject();
            if (temp == null) {
                LogUtils.e(TAG, "unwrapKeys, json string err");
                break;
            } else if (temp.has("index")) {
                secrets.add(temp.get("index").getAsInt(), temp.get("key").toString());
            }
        }
        return secrets;
    }

    private Map<String, String> unwrapNamedKeys(byte[] decoded) {
        Map<String, String> results = new HashMap<>();
        String jsonBody = new String(decoded, StandardCharsets.UTF_8);
        LogUtils.d(TAG, "unwrapNamedKeys: " + UpsoConfig.logLimited(jsonBody));
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonBody);
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            LogUtils.d(TAG, "unwrapNamedKeys, key = " + entry.getKey() + ", v = " + entry.getValue().getAsString());
            results.put(entry.getKey(), entry.getValue().getAsString());
        }
        return results;
    }

    public List<String> getKeys(String deviceId) {
        byte[] decoded = decode(deviceId, this.secreteKey);
        if (decoded == null) {
            LogUtils.e(TAG, "getKeys, decode key failed");
            return null;
        }
        List<String> secrets = unwrapNumberedKeys(decoded);
        return secrets;
    }

    public Map<String, String> getTboxKeys(String deviceId) {
        byte[] decoded = decode(deviceId, this.tboxKey);
        if (decoded == null) {
            LogUtils.e(TAG, "getTboxKeys, decode key failed");
            return null;
        }
        return unwrapNamedKeys(decoded);
    }

    public Map<String, String> getXpuKeys(String deviceId) {
        byte[] decoded = decode(deviceId, this.xpuKey);
        if (decoded == null) {
            LogUtils.e(TAG, "getXpuKeys, decode key failed");
            return null;
        }
        return unwrapNamedKeys(decoded);
    }
}
