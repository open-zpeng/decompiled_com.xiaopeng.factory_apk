package com.xpeng.upso.aesserver;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.xiaopeng.commonfunc.Constant;
import com.xpeng.upso.UpsoConfig;
import com.xpeng.upso.http.UpsoCertResponse;
import com.xpeng.upso.http.UpsoConstants;
import com.xpeng.upso.utils.HexUtils;
import com.xpeng.upso.utils.LogUtils;
import com.xpeng.upso.utils.cert.CertDecryptUtil;
import com.xpeng.upso.utils.cert.CertResp;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
/* loaded from: classes2.dex */
public class PresetTask<T> extends AsyncTask<PresetParam, Integer, T> {
    private static final int HW_ID_SIZE_MIN = 20;
    private static final String TAG = "Upso-PresetTask";
    private IPsuServerResponseCallback psuResponseCallback;
    private int requestType = 0;

    /* loaded from: classes2.dex */
    public interface IPsuServerResponseCallback<T> {
        void onPsuServerResponse(int requestType, T response);

        void onPsuServerResponseIsNull(int requestType);
    }

    public void setTboxPresetSecretCallback(IPsuServerResponseCallback callback) {
        this.psuResponseCallback = callback;
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(T response) {
        super.onPostExecute(response);
        LogUtils.i(TAG, "onPostExecute response: " + response);
        IPsuServerResponseCallback iPsuServerResponseCallback = this.psuResponseCallback;
        if (iPsuServerResponseCallback != null) {
            if (response != null) {
                iPsuServerResponseCallback.onPsuServerResponse(this.requestType, response);
                return;
            } else {
                iPsuServerResponseCallback.onPsuServerResponseIsNull(this.requestType);
                return;
            }
        }
        LogUtils.i(TAG, "onPostExecute psuResponseCallback = null ");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public T doInBackground(PresetParam... presetParams) {
        LogUtils.i(TAG, "doInBackground");
        if (presetParams.length <= 0) {
            return null;
        }
        try {
            PresetParam requestParma = presetParams[0];
            this.requestType = requestParma.getRequestType();
            if (this.requestType == 11) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                String hardware_id = requestParma.getDeviceId();
                String sign = sign(requestParma.getDeviceType(), hardware_id, timestamp);
                JsonObject jsonObject = buildRequestParma(requestParma.getDeviceType(), hardware_id, timestamp, sign);
                Request req = httpRequestFactoryForCert(requestParma, jsonObject, timestamp);
                String res = requestCall(req);
                return processResponse(res, sign);
            }
            Request req2 = httpRequestFactoryForAES(requestParma);
            String res2 = requestCall(req2);
            return processResponse(res2);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    private Request httpRequestFactoryForAES(PresetParam requestParma) {
        RequestBody requestBody = createRequestBody(requestParma.getRequestParma());
        if (requestBody == null || requestParma.getUrlPath() == null) {
            LogUtils.e(TAG, "doInBackground: request body or url path is null do nothing !");
            return null;
        }
        String DN1 = AesConstants.DN1_PREFIX + requestParma.getDeviceId();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String bodyStr = requestParma.getRequestParma().toString();
        String generateSign = genSign(timestamp, bodyStr);
        Headers headers = new Headers.Builder().add(AesConstants.REQUEST_HEADER_KEY_DN1, DN1).add("XP-Appid", "xp_xmart_car").add(AesConstants.REQUEST_HEADER_KEY_SIGN, generateSign).add(AesConstants.REQUEST_HEADER_KEY_TS, timestamp).build();
        Request request = new Request.Builder().url(requestParma.getUrlPath()).headers(headers).post(requestBody).build();
        LogUtils.i(TAG, "doInBackground: post headers: " + UpsoConfig.logLimited(headers.toString()));
        LogUtils.i(TAG, "doInBackground: post body: " + UpsoConfig.logLimited(bodyStr));
        LogUtils.i(TAG, "doInBackground: request: " + request);
        return request;
    }

    private Request httpRequestFactoryForCert(PresetParam requestParma, JsonObject requestParmaJson, String timestamp) {
        String bodyStr = requestParmaJson.toString();
        Headers headers = new Headers.Builder().add("Client", AesConstants.CLIENT).add("Content-Type: application/json").build();
        requestParma.setRequestParma(requestParmaJson);
        RequestBody requestBody = createRequestBody(requestParmaJson);
        if (requestBody == null || requestParma.getUrlPath() == null) {
            LogUtils.e(TAG, "doInBackground: request body or url path is null do nothing !");
            return null;
        }
        Request request = new Request.Builder().url(requestParma.getUrlPath()).headers(headers).post(requestBody).build();
        LogUtils.i(TAG, "doInBackground: post headers: " + UpsoConfig.logLimited(headers.toString()));
        LogUtils.i(TAG, "doInBackground: post body: " + UpsoConfig.logLimited(bodyStr));
        LogUtils.i(TAG, "doInBackground: request: " + request);
        return request;
    }

    private String sign(String deviceType, String hardware_id, String timestamp) {
        Map<String, String> requestMap = new HashMap<>();
        LogUtils.e(TAG, "hardware_id.length()" + hardware_id.length());
        requestMap.put(Constant.HARDWAREID, hardware_id);
        requestMap.put("timestamp", timestamp);
        requestMap.put(Constant.ACTION, deviceType);
        String sign1 = CertDecryptUtil.sign(requestMap);
        LogUtils.i(TAG, "requestMap:" + requestMap.toString());
        LogUtils.i(TAG, "sign" + sign1);
        return sign1;
    }

    private JsonObject buildRequestParma(String deviceType, String hardware_id, String timestamp, String sign1) {
        JsonObject requestParmaJson = new JsonObject();
        requestParmaJson.addProperty(Constant.HARDWAREID, hardware_id);
        requestParmaJson.addProperty("timestamp", timestamp);
        requestParmaJson.addProperty(Constant.ACTION, deviceType);
        requestParmaJson.addProperty("sign", sign1);
        requestParmaJson.addProperty("app_id", AesConstants.APPID_WEB);
        return requestParmaJson;
    }

    private String requestCall(Request request) {
        OkHttpClient httpClient = AesHttpHelper.getTrustedOkHttpClient();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response == null) {
                LogUtils.e(TAG, "resp is null");
                return null;
            }
            ResponseBody body = response.body();
            if (body == null) {
                LogUtils.e(TAG, "response body is null");
                return null;
            }
            String res = body.string();
            LogUtils.i(TAG, "response = " + UpsoConfig.logLimited(res));
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }

    private RequestBody createRequestBody(JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        MediaType type = MediaType.parse(UpsoConstants.REQUEST_MEDIA_TYPE);
        return RequestBody.create(type, jsonObject.toString());
    }

    /* JADX WARN: Type inference failed for: r0v17, types: [T, com.xpeng.upso.aesserver.AesDecryptMultiResponse] */
    /* JADX WARN: Type inference failed for: r0v19, types: [T, com.xpeng.upso.aesserver.AesEncryptMultiResponse] */
    private T processResponse(String body) {
        int i = this.requestType;
        if (i == 1 || i == 10) {
            try {
                return (T) ((AesSecretResponse) new Gson().fromJson(body, (Class<Object>) AesSecretResponse.class));
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                LogUtils.e(TAG, e.toString());
                return null;
            }
        } else if (i == 3) {
            try {
                return (T) ((AesEncryptResponse) new Gson().fromJson(body, (Class<Object>) AesEncryptResponse.class));
            } catch (JsonSyntaxException e2) {
                e2.printStackTrace();
                LogUtils.e(TAG, e2.toString());
                return null;
            }
        } else if (i == 2) {
            try {
                return (T) ((AesEncryptResponse) new Gson().fromJson(body, (Class<Object>) AesEncryptResponse.class));
            } catch (JsonSyntaxException e3) {
                e3.printStackTrace();
                LogUtils.e(TAG, e3.toString());
                return null;
            }
        } else if (i == 4) {
            ?? r0 = (T) new AesEncryptMultiResponse();
            try {
                r0.parse(body);
                return r0;
            } catch (Exception e4) {
                e4.printStackTrace();
                LogUtils.e(TAG, e4.toString());
                return null;
            }
        } else if (i == 5) {
            ?? r02 = (T) new AesDecryptMultiResponse();
            try {
                r02.parse(body);
                return r02;
            } catch (Exception e5) {
                e5.printStackTrace();
                LogUtils.e(TAG, e5.toString());
                return null;
            }
        } else {
            return null;
        }
    }

    private T processResponse(String body, String sign) {
        if (this.requestType == 11) {
            try {
                List<CertResp> certInfoList = CertDecryptUtil.parseE38Response(body, sign);
                if (certInfoList == null) {
                    return null;
                }
                final List<String> certInfoListResult = new ArrayList<>();
                certInfoList.forEach(new Consumer() { // from class: com.xpeng.upso.aesserver.-$$Lambda$PresetTask$dxt44z6juoFb0Hf3uoD6gOM37ZU
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        certInfoListResult.add(((CertResp) obj).getPkcs12());
                    }
                });
                LogUtils.e(TAG, "certInfoList:" + UpsoConfig.logLimited(certInfoListResult.toString()));
                T t = (T) new UpsoCertResponse();
                UpsoCertResponse.parse(certInfoListResult);
                return t;
            } catch (Exception e) {
                LogUtils.e(TAG, "REQUEST_TYPE_CERT,processResponse,error:" + e.toString());
            }
        }
        return null;
    }

    private String genSign(String timestamp, String body) {
        String signStr = timestamp + body + timestamp + AesConstants.SERVER_SIGN_SAIT;
        String generateSign = getHmacSHA256LowerString(signStr, AesConstants.SERVER_SIGN_SECRET);
        return generateSign;
    }

    public static String getHmacSHA256LowerString(String content, String secret) {
        return getHmacSHA256String(content, secret).toLowerCase();
    }

    private static String getHmacSHA256String(String content, String secret) {
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            byte[] secretBytes = secret.getBytes("UTF-8");
            hmacSHA256.init(new SecretKeySpec(secretBytes, 0, secretBytes.length, "HmacSHA256"));
            return HexUtils.bytesToHexString(hmacSHA256.doFinal(content.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
            return "";
        } catch (InvalidKeyException var6) {
            var6.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
            return "";
        }
    }
}
