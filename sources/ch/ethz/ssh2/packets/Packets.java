package ch.ethz.ssh2.packets;
/* loaded from: classes.dex */
public class Packets {
    public static final int SSH_DISCONNECT_AUTH_CANCELLED_BY_USER = 13;
    public static final int SSH_DISCONNECT_BY_APPLICATION = 11;
    public static final int SSH_DISCONNECT_COMPRESSION_ERROR = 6;
    public static final int SSH_DISCONNECT_CONNECTION_LOST = 10;
    public static final int SSH_DISCONNECT_HOST_KEY_NOT_VERIFIABLE = 9;
    public static final int SSH_DISCONNECT_HOST_NOT_ALLOWED_TO_CONNECT = 1;
    public static final int SSH_DISCONNECT_ILLEGAL_USER_NAME = 15;
    public static final int SSH_DISCONNECT_KEY_EXCHANGE_FAILED = 3;
    public static final int SSH_DISCONNECT_MAC_ERROR = 5;
    public static final int SSH_DISCONNECT_NO_MORE_AUTH_METHODS_AVAILABLE = 14;
    public static final int SSH_DISCONNECT_PROTOCOL_ERROR = 2;
    public static final int SSH_DISCONNECT_PROTOCOL_VERSION_NOT_SUPPORTED = 8;
    public static final int SSH_DISCONNECT_RESERVED = 4;
    public static final int SSH_DISCONNECT_SERVICE_NOT_AVAILABLE = 7;
    public static final int SSH_DISCONNECT_TOO_MANY_CONNECTIONS = 12;
    public static final int SSH_EXTENDED_DATA_STDERR = 1;
    public static final int SSH_MSG_CHANNEL_CLOSE = 97;
    public static final int SSH_MSG_CHANNEL_DATA = 94;
    public static final int SSH_MSG_CHANNEL_EOF = 96;
    public static final int SSH_MSG_CHANNEL_EXTENDED_DATA = 95;
    public static final int SSH_MSG_CHANNEL_FAILURE = 100;
    public static final int SSH_MSG_CHANNEL_OPEN = 90;
    public static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION = 91;
    public static final int SSH_MSG_CHANNEL_OPEN_FAILURE = 92;
    public static final int SSH_MSG_CHANNEL_REQUEST = 98;
    public static final int SSH_MSG_CHANNEL_SUCCESS = 99;
    public static final int SSH_MSG_CHANNEL_WINDOW_ADJUST = 93;
    public static final int SSH_MSG_DEBUG = 4;
    public static final int SSH_MSG_DISCONNECT = 1;
    public static final int SSH_MSG_GLOBAL_REQUEST = 80;
    public static final int SSH_MSG_IGNORE = 2;
    public static final int SSH_MSG_KEXDH_INIT = 30;
    public static final int SSH_MSG_KEXDH_REPLY = 31;
    public static final int SSH_MSG_KEXINIT = 20;
    public static final int SSH_MSG_KEX_DH_GEX_GROUP = 31;
    public static final int SSH_MSG_KEX_DH_GEX_INIT = 32;
    public static final int SSH_MSG_KEX_DH_GEX_REPLY = 33;
    public static final int SSH_MSG_KEX_DH_GEX_REQUEST = 34;
    public static final int SSH_MSG_KEX_DH_GEX_REQUEST_OLD = 30;
    public static final int SSH_MSG_NEWKEYS = 21;
    public static final int SSH_MSG_REQUEST_FAILURE = 82;
    public static final int SSH_MSG_REQUEST_SUCCESS = 81;
    public static final int SSH_MSG_SERVICE_ACCEPT = 6;
    public static final int SSH_MSG_SERVICE_REQUEST = 5;
    public static final int SSH_MSG_UNIMPLEMENTED = 3;
    public static final int SSH_MSG_USERAUTH_BANNER = 53;
    public static final int SSH_MSG_USERAUTH_FAILURE = 51;
    public static final int SSH_MSG_USERAUTH_INFO_REQUEST = 60;
    public static final int SSH_MSG_USERAUTH_INFO_RESPONSE = 61;
    public static final int SSH_MSG_USERAUTH_REQUEST = 50;
    public static final int SSH_MSG_USERAUTH_SUCCESS = 52;
    public static final int SSH_OPEN_ADMINISTRATIVELY_PROHIBITED = 1;
    public static final int SSH_OPEN_CONNECT_FAILED = 2;
    public static final int SSH_OPEN_RESOURCE_SHORTAGE = 4;
    public static final int SSH_OPEN_UNKNOWN_CHANNEL_TYPE = 3;
    private static final String[] reverseNames = new String[101];

    static {
        String[] strArr = reverseNames;
        strArr[1] = "SSH_MSG_DISCONNECT";
        strArr[2] = "SSH_MSG_IGNORE";
        strArr[3] = "SSH_MSG_UNIMPLEMENTED";
        strArr[4] = "SSH_MSG_DEBUG";
        strArr[5] = "SSH_MSG_SERVICE_REQUEST";
        strArr[6] = "SSH_MSG_SERVICE_ACCEPT";
        strArr[20] = "SSH_MSG_KEXINIT";
        strArr[21] = "SSH_MSG_NEWKEYS";
        strArr[30] = "SSH_MSG_KEXDH_INIT";
        strArr[31] = "SSH_MSG_KEXDH_REPLY/SSH_MSG_KEX_DH_GEX_GROUP";
        strArr[32] = "SSH_MSG_KEX_DH_GEX_INIT";
        strArr[33] = "SSH_MSG_KEX_DH_GEX_REPLY";
        strArr[34] = "SSH_MSG_KEX_DH_GEX_REQUEST";
        strArr[50] = "SSH_MSG_USERAUTH_REQUEST";
        strArr[51] = "SSH_MSG_USERAUTH_FAILURE";
        strArr[52] = "SSH_MSG_USERAUTH_SUCCESS";
        strArr[53] = "SSH_MSG_USERAUTH_BANNER";
        strArr[60] = "SSH_MSG_USERAUTH_INFO_REQUEST";
        strArr[61] = "SSH_MSG_USERAUTH_INFO_RESPONSE";
        strArr[80] = "SSH_MSG_GLOBAL_REQUEST";
        strArr[81] = "SSH_MSG_REQUEST_SUCCESS";
        strArr[82] = "SSH_MSG_REQUEST_FAILURE";
        strArr[90] = "SSH_MSG_CHANNEL_OPEN";
        strArr[91] = "SSH_MSG_CHANNEL_OPEN_CONFIRMATION";
        strArr[92] = "SSH_MSG_CHANNEL_OPEN_FAILURE";
        strArr[93] = "SSH_MSG_CHANNEL_WINDOW_ADJUST";
        strArr[94] = "SSH_MSG_CHANNEL_DATA";
        strArr[95] = "SSH_MSG_CHANNEL_EXTENDED_DATA";
        strArr[96] = "SSH_MSG_CHANNEL_EOF";
        strArr[97] = "SSH_MSG_CHANNEL_CLOSE";
        strArr[98] = "SSH_MSG_CHANNEL_REQUEST";
        strArr[99] = "SSH_MSG_CHANNEL_SUCCESS";
        strArr[100] = "SSH_MSG_CHANNEL_FAILURE";
    }

    public static final String getMessageName(int type) {
        String res = null;
        if (type >= 0) {
            String[] strArr = reverseNames;
            if (type < strArr.length) {
                res = strArr[type];
            }
        }
        if (res == null) {
            StringBuffer stringBuffer = new StringBuffer("UNKNOWN MSG ");
            stringBuffer.append(type);
            return stringBuffer.toString();
        }
        return res;
    }
}
