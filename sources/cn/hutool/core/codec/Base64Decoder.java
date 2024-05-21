package cn.hutool.core.codec;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.factory.presenter.security.TestSecurityPresenter;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class Base64Decoder {
    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
    private static final byte PADDING = -2;
    private static final byte[] DECODE_TABLE = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, PADDING, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, TestSecurityPresenter.BRUSH_EFUSE, 27, 28, 29, 30, 31, WlanPresenter.WL_RMMOD_INSMOD, WlanPresenter.WL_START_RF, WlanPresenter.WL_TX_NF3219, WlanPresenter.WL_RX_NF3219, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    public static String decodeStr(CharSequence source) {
        return decodeStr(source, DEFAULT_CHARSET);
    }

    public static String decodeStr(CharSequence source, Charset charset) {
        return StrUtil.str(decode(source), charset);
    }

    public static byte[] decode(CharSequence source) {
        return decode(StrUtil.bytes(source, DEFAULT_CHARSET));
    }

    public static byte[] decode(byte[] in) {
        if (ArrayUtil.isEmpty(in)) {
            return in;
        }
        return decode(in, 0, in.length);
    }

    public static byte[] decode(byte[] in, int pos, int length) {
        if (ArrayUtil.isEmpty(in)) {
            return in;
        }
        IntWrapper offset = new IntWrapper(pos);
        int maxPos = (pos + length) - 1;
        int octetId = 0;
        byte[] octet = new byte[(length * 3) / 4];
        while (offset.value <= maxPos) {
            byte sestet0 = getNextValidDecodeByte(in, offset, maxPos);
            byte sestet1 = getNextValidDecodeByte(in, offset, maxPos);
            byte sestet2 = getNextValidDecodeByte(in, offset, maxPos);
            byte sestet3 = getNextValidDecodeByte(in, offset, maxPos);
            if (-2 != sestet1) {
                octet[octetId] = (byte) ((sestet0 << 2) | (sestet1 >>> 4));
                octetId++;
            }
            if (-2 != sestet2) {
                octet[octetId] = (byte) (((sestet1 & 15) << 4) | (sestet2 >>> 2));
                octetId++;
            }
            if (-2 != sestet3) {
                octet[octetId] = (byte) (((sestet2 & 3) << 6) | sestet3);
                octetId++;
            }
        }
        if (octetId == octet.length) {
            return octet;
        }
        return (byte[]) ArrayUtil.copy(octet, new byte[octetId], octetId);
    }

    public static boolean isBase64Code(byte octet) {
        if (octet != 61) {
            if (octet >= 0) {
                byte[] bArr = DECODE_TABLE;
                if (octet >= bArr.length || bArr[octet] == -1) {
                }
            }
            return false;
        }
        return true;
    }

    private static byte getNextValidDecodeByte(byte[] in, IntWrapper pos, int maxPos) {
        byte decodeByte;
        while (pos.value <= maxPos) {
            int i = pos.value;
            pos.value = i + 1;
            byte base64Byte = in[i];
            if (base64Byte > -1 && (decodeByte = DECODE_TABLE[base64Byte]) > -1) {
                return decodeByte;
            }
        }
        return PADDING;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class IntWrapper {
        int value;

        IntWrapper(int value) {
            this.value = value;
        }
    }
}
