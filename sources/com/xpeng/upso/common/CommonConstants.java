package com.xpeng.upso.common;

import com.xpeng.upso.XpengCarModel;
import com.xpeng.upso.aesserver.AesConstants;
/* loaded from: classes2.dex */
public class CommonConstants {
    public static final String AES_ENV_PREPROD_NO_TLS_SERVER_BASE_URL = "https://xmart.deploy-test.xiaopeng.com/secure-gateway/web";
    public static final String AES_ENV_PREPROD_TLS_SERVER_BASE_URL = "https://xmart-e38.deploy-test.xiaopeng.com/secure-gateway/web";
    public static final String AES_ENV_PRODUCTION_SERVER_BASE_URL = "https://xmart-secure.xiaopeng.com/secure-gateway/web";
    public static final String AES_ENV_TEST_SERVER_BASE_URL = "http://logan-gateway.test.logan.xiaopeng.local/xp-secure-gateway-boot/web";
    public static final String CERT_ENV_PREPROD_SERVER_BASE_URL = "http://100.64.254.1:8081/Secure_Proxy/Certificate_For_Proxy_The_Name_Is_Very_Long_?appId=web.xmart.com";
    public static final String CERT_ENV_PRODUCTION_SERVER_BASE_URL = "http://100.64.254.1:8080/Secure_Proxy/Certificate_For_Proxy_The_Name_Is_Very_Long_?appId=web.xmart.com";
    public static final String CERT_ENV_TEST_SERVER_BASE_URL = "http://10.0.13.28:9527/v1/certificate?appId=web.xmart.com";
    public static final String MAIN_TAG = "Upso-";
    public static final int OP_IDLE = -1;
    public static final int OP_START_PRESET = 1;
    public static final int OP_START_VERIFY = 2;
    public static final int PB_DEFAULT_ECU_IS_USER_VER = 1;
    public static final int PB_DEFAULT_INDEX = Integer.MAX_VALUE;
    public static final int PB_DEFAULT_MASK = 0;
    public static final int PB_DEFAULT_VERSION = 3;
    public static final int PB_ROLE_CLIENT = 0;
    public static final int PB_ROLE_PROXY = 1;

    public static String getAesBaseUrl(XpengCarModel carModel) {
        String carType = "";
        if (carModel != null) {
            carType = carModel.toString().toLowerCase();
        }
        return getAesBaseUrlExceptVer() + AesConstants.SERVER_PATH_VER + "/" + carType;
    }

    public static String getAesBaseUrlExceptVer() {
        return AES_ENV_PRODUCTION_SERVER_BASE_URL;
    }

    public static String getCertUrl() {
        return CERT_ENV_PRODUCTION_SERVER_BASE_URL;
    }
}
