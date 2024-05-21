package com.xpeng.upso.aesserver;

import com.google.gson.JsonObject;
/* loaded from: classes2.dex */
public class PresetParam {
    public static final int REQUEST_TYPE_AES_KEY = 1;
    public static final int REQUEST_TYPE_AES_KEY_AFTER_CERT = 10;
    public static final int REQUEST_TYPE_CERT_LIST = 11;
    public static final int REQUEST_TYPE_DECRYPT = 3;
    public static final int REQUEST_TYPE_DECRYPT_MULTI = 5;
    public static final int REQUEST_TYPE_ENCRYPT = 2;
    public static final int REQUEST_TYPE_ENCRYPT_MULTI = 4;
    private String deviceId;
    private String deviceType;
    private JsonObject requestParma;
    private int requestType;
    private String urlPath;

    public PresetParam(int requestType, String urlPath, JsonObject requestParma) {
        this.requestType = 0;
        this.requestType = requestType;
        this.urlPath = urlPath;
        this.requestParma = requestParma;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getUrlPath() {
        return this.urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public JsonObject getRequestParma() {
        return this.requestParma;
    }

    public void setRequestParma(JsonObject requestParma) {
        this.requestParma = requestParma;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return this.deviceType;
    }
}
