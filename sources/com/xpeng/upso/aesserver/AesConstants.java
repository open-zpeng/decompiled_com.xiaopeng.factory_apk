package com.xpeng.upso.aesserver;

import com.xpeng.upso.XpengPsoClientType;
import com.xpeng.upso.http.UpsoConstants;
/* loaded from: classes2.dex */
public class AesConstants extends UpsoConstants {
    public static final int AES_GCM_IV_SIZE = 12;
    public static final String APPID = "xp_xmart_car";
    public static final String APPID_WEB = "web.xmart.com";
    public static final String CLIENT = "web.xmart.com/test";
    public static final String DN1_PREFIX = "CN=xmart.";
    public static final int KEY_INDEX_COUNT_DEFAULT = 32;
    public static final int KEY_INDEX_COUNT_DEFAULT_CDU = 10;
    public static final int KEY_INDEX_MAX = 31;
    public static final int KEY_INDEX_MIN = 0;
    public static final String LOGAN_TEST = "test";
    public static final String REQUEST_HEADER_KEY_APPID = "XP-Appid";
    public static final String REQUEST_HEADER_KEY_CLIENT = "Client";
    public static final String REQUEST_HEADER_KEY_DN1 = "XP-DN";
    public static final String REQUEST_HEADER_KEY_LOGAN = "logan";
    public static final String REQUEST_HEADER_KEY_SIGN = "XP-PRESET-SIGN";
    public static final String REQUEST_HEADER_KEY_TS = "XP-TIMESTAMP";
    public static final String REQUEST_PARAM_DEVICE_ID = "deviceId";
    public static final String REQUEST_PARAM_DEVICE_TYPE = "deviceType";
    public static final String REQUEST_PARAM_ECU_PLATFORM = "platform";
    public static final String REQUEST_PARAM_TEE_DEVICE_TYPE = "teeDeviceType";
    public static final String REQUEST_PARAM_TO_UPDATE_SECRET = "toUpdateSecret";
    public static final String REQUEST_PARAM_VERSION = "version";
    public static final int SERVER_CONNECT_TIMEOUT = 30000;
    public static final String SERVER_MAGIC_CDU = "EC";
    public static final String SERVER_MAGIC_TBOX = "ET";
    public static final String SERVER_MAGIC_XDU = "EX";
    public static final int SERVER_NONCE_LENGTH = 32;
    public static final String SERVER_PATH_CERT_PRESET = "/v1/certificate?appId=web.xmart.com";
    public static final String SERVER_PATH_DECRYPT = "/aes/decrypt";
    public static final String SERVER_PATH_DECRYPT_MULTI = "/aes/decryptMulti";
    public static final String SERVER_PATH_ENCRYPT = "/aes/encrypt";
    public static final String SERVER_PATH_ENCRYPT_MULTI = "/aes/encryptMulti";
    public static final String SERVER_PATH_PRESET = "/aes/preset";
    public static final String SERVER_PATH_VER = "/v1";
    public static final String SERVER_SIGN_SAIT = "2x3ihrvyk1r037anmheooob57gaxn0ew";
    public static final String SERVER_SIGN_SECRET = "ghepbrwmgcispjja";
    public static final int SERVER_VERSION = 2;
    public static final int SERVER_WR_TIMEOUT = 30000;
    public static final String UNKNOWN_DEVICE_ID = "UNKNOWN_DEVICE_ID";

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.xpeng.upso.aesserver.AesConstants$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$xpeng$upso$XpengPsoClientType = new int[XpengPsoClientType.values().length];

        static {
            try {
                $SwitchMap$com$xpeng$upso$XpengPsoClientType[XpengPsoClientType.CDU.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    public static int getKeyIndexCountDefault(XpengPsoClientType clientType) {
        if (AnonymousClass1.$SwitchMap$com$xpeng$upso$XpengPsoClientType[clientType.ordinal()] == 1) {
            return 10;
        }
        return 32;
    }

    public static int getKeyIndexCountDefault(String clientType) {
        XpengPsoClientType type = XpengPsoClientType.valueOfName(clientType);
        return getKeyIndexCountDefault(type);
    }
}
