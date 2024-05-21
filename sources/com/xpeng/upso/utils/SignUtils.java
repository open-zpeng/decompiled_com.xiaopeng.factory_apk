package com.xpeng.upso.utils;

import android.text.TextUtils;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.lib.framework.netchannelmodule.http.xmart.bizapi.BizConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okio.Buffer;
/* loaded from: classes2.dex */
public class SignUtils {
    private static final String TAG = "SignUtils";
    private static String mLongTermInfo;

    private SignUtils() {
    }

    public static String createSignContent(String method, HttpUrl url, Headers headers, RequestBody body) {
        StringBuilder builder = new StringBuilder();
        builder.append((TextUtils.isEmpty(method) ? "GET" : method).toUpperCase());
        Map<String, String> parameters = new HashMap<>();
        parameters.putAll(getQueryParameters(url.query()));
        parameters.putAll(getBodyParameters(body));
        parameters.putAll(getHeaderParameters(headers));
        builder.append("&");
        builder.append(sortedKeyValueString(parameters));
        builder.append("&");
        builder.append(getBodyMd5(method, body));
        return builder.toString();
    }

    private static String getBodyMd5(String method, RequestBody body) {
        Buffer sink = new Buffer();
        if (body != null) {
            try {
                body.writeTo(sink);
                if ((TextUtils.equals(method, "PUT") || TextUtils.equals(method, "POST")) && sink.size() > 0) {
                    String hex = sink.md5().hex();
                    sink.close();
                    return hex;
                }
            } catch (IOException e) {
            } catch (Throwable th) {
                sink.close();
                throw th;
            }
        }
        sink.close();
        return "";
    }

    private static Map<String, String> getQueryParameters(String queryString) {
        String[] split;
        Map<String, String> result = new HashMap<>();
        if (!TextUtils.isEmpty(queryString)) {
            for (String item : queryString.split("&")) {
                String[] parts = item.split(Constant.EQUALS_STRING);
                if (parts.length > 1) {
                    result.put(parts[0], parts[1]);
                } else if (parts.length == 1) {
                    result.put(parts[0], "");
                }
            }
        }
        return result;
    }

    private static Map<String, String> getHeaderParameters(Headers headers) {
        Map<String, String> result = new HashMap<>();
        if (headers != null) {
            for (String key : headers.names()) {
                if (key.startsWith(BizConstants.HEADER_PREFIX) || key.startsWith("xp-")) {
                    result.put(key.toUpperCase(), headers.get(key));
                }
            }
        }
        return result;
    }

    private static Map<String, String> getBodyParameters(RequestBody body) {
        Map<String, String> result = new HashMap<>();
        if (body != null && (body instanceof FormBody)) {
            FormBody form = (FormBody) body;
            int len = form.size();
            for (int i = 0; i < len; i++) {
                String key = form.name(i);
                String value = form.value(i);
                result.put(key.toLowerCase(), value);
            }
        }
        return result;
    }

    private static String sortedKeyValueString(Map<String, String> parameters) {
        List<String> sortedKeys = new ArrayList<>(parameters.keySet());
        Collections.sort(sortedKeys);
        StringBuilder builder = new StringBuilder();
        int len = sortedKeys.size();
        for (int i = 0; i < len; i++) {
            String key = sortedKeys.get(i);
            builder.append(key);
            builder.append(Constant.EQUALS_STRING);
            builder.append(parameters.get(key));
            if (i + 1 < len) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    @RequiresApi(api = 8)
    public static String exSign(String content) {
        return Base64.encodeToString(content.getBytes(), 0);
    }

    public static String sign(@NonNull String secret, String content) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            byte[] key = secret.getBytes("UTF-8");
            hmacSha256.init(new SecretKeySpec(key, 0, key.length, "HmacSHA256"));
            return HexUtils.bytesToHexString(hmacSha256.doFinal(content.getBytes("UTF-8"))).toLowerCase();
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static String addKeyValueString(String original, String... keyAndValues) {
        StringBuilder builder = new StringBuilder(original);
        if (builder.length() > 0) {
            builder.append(Constant.SEMICOLON_STRING);
        }
        builder.append(toKeyValueString(keyAndValues));
        return builder.toString();
    }

    public static String toKeyValueString(String... keyAndValues) {
        StringBuilder retString = new StringBuilder();
        if (keyAndValues != null && keyAndValues.length % 2 == 0) {
            for (int i = 0; i < keyAndValues.length; i += 2) {
                String name = keyAndValues[i];
                String value = keyAndValues[i + 1];
                retString.append(name + Constant.EQUALS_STRING + value);
                if (i + 2 < keyAndValues.length) {
                    retString.append(Constant.SEMICOLON_STRING);
                }
            }
        }
        return retString.toString();
    }
}
