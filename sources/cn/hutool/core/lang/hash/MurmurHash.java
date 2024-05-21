package cn.hutool.core.lang.hash;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.nio.charset.Charset;
/* loaded from: classes.dex */
public class MurmurHash implements Serializable {
    private static final long C1 = -8663945395140668459L;
    private static final int C1_32 = -862048943;
    private static final long C2 = 5545529020109919103L;
    private static final int C2_32 = 461845907;
    private static final Charset DEFAULT_CHARSET = CharsetUtil.CHARSET_UTF_8;
    private static final int DEFAULT_SEED = 0;
    private static final int M = 5;
    private static final int M_32 = 5;
    private static final int N1 = 1390208809;
    private static final int N2 = 944331445;
    private static final int N_32 = -430675100;
    private static final int R1 = 31;
    private static final int R1_32 = 15;
    private static final int R2 = 27;
    private static final int R2_32 = 13;
    private static final int R3 = 33;
    private static final long serialVersionUID = 1;

    public static int hash32(CharSequence data) {
        return hash32(StrUtil.bytes(data, DEFAULT_CHARSET));
    }

    public static int hash32(byte[] data) {
        return hash32(data, data.length, 0);
    }

    public static int hash32(byte[] data, int length, int seed) {
        int hash = seed;
        int nblocks = length >> 2;
        for (int i = 0; i < nblocks; i++) {
            int i_4 = i << 2;
            int k = (data[i_4] & 255) | ((data[i_4 + 1] & 255) << 8) | ((data[i_4 + 2] & 255) << 16) | ((data[i_4 + 3] & 255) << 24);
            hash = (Integer.rotateLeft(hash ^ (Integer.rotateLeft(k * C1_32, 15) * C2_32), 13) * 5) + N_32;
        }
        int i2 = nblocks << 2;
        int i3 = length - i2;
        if (i3 != 1) {
            if (i3 != 2) {
                k1 = i3 == 3 ? 0 ^ (data[i2 + 2] << 16) : 0;
                int hash2 = hash ^ length;
                int hash3 = (hash2 ^ (hash2 >>> 16)) * (-2048144789);
                int hash4 = (hash3 ^ (hash3 >>> 13)) * (-1028477387);
                return hash4 ^ (hash4 >>> 16);
            }
            k1 ^= data[i2 + 1] << 8;
        }
        hash ^= Integer.rotateLeft((k1 ^ data[i2]) * C1_32, 15) * C2_32;
        int hash22 = hash ^ length;
        int hash32 = (hash22 ^ (hash22 >>> 16)) * (-2048144789);
        int hash42 = (hash32 ^ (hash32 >>> 13)) * (-1028477387);
        return hash42 ^ (hash42 >>> 16);
    }

    public static long hash64(CharSequence data) {
        return hash64(StrUtil.bytes(data, DEFAULT_CHARSET));
    }

    public static long hash64(byte[] data) {
        return hash64(data, data.length, 0);
    }

    public static long hash64(byte[] data, int length, int seed) {
        long hash = seed;
        int nblocks = length >> 3;
        for (int i = 0; i < nblocks; i++) {
            int i8 = i << 3;
            long k = (data[i8] & 255) | ((data[i8 + 1] & 255) << 8) | ((data[i8 + 2] & 255) << 16) | ((data[i8 + 3] & 255) << 24) | ((data[i8 + 4] & 255) << 32) | ((data[i8 + 5] & 255) << 40) | ((data[i8 + 6] & 255) << 48) | ((data[i8 + 7] & 255) << 56);
            hash = (Long.rotateLeft(hash ^ (Long.rotateLeft(k * C1, 31) * C2), 27) * 5) + 1390208809;
        }
        long k1 = 0;
        int tailStart = nblocks << 3;
        switch (length - tailStart) {
            case 7:
                k1 = 0 ^ ((data[tailStart + 6] & 255) << 48);
            case 6:
                k1 ^= (data[tailStart + 5] & 255) << 40;
            case 5:
                k1 ^= (data[tailStart + 4] & 255) << 32;
            case 4:
                k1 ^= (data[tailStart + 3] & 255) << 24;
            case 3:
                k1 ^= (data[tailStart + 2] & 255) << 16;
            case 2:
                k1 ^= (data[tailStart + 1] & 255) << 8;
            case 1:
                hash ^= Long.rotateLeft((k1 ^ (data[tailStart] & 255)) * C1, 31) * C2;
                break;
        }
        return fmix64(hash ^ length);
    }

    public static long[] hash128(CharSequence data) {
        return hash128(StrUtil.bytes(data, DEFAULT_CHARSET));
    }

    public static long[] hash128(byte[] data) {
        return hash128(data, data.length, 0);
    }

    public static long[] hash128(byte[] data, int length, int seed) {
        long h1 = seed;
        long h2 = seed;
        int nblocks = length >> 4;
        for (int i = 0; i < nblocks; i++) {
            int i16 = i << 4;
            h1 = (5 * (Long.rotateLeft(h1 ^ (Long.rotateLeft(((((((((data[i16] & 255) | ((data[i16 + 1] & 255) << 8)) | ((data[i16 + 2] & 255) << 16)) | ((data[i16 + 3] & 255) << 24)) | ((data[i16 + 4] & 255) << 32)) | ((data[i16 + 5] & 255) << 40)) | ((data[i16 + 6] & 255) << 48)) | ((data[i16 + 7] & 255) << 56)) * C1, 31) * C2), 27) + h2)) + 1390208809;
            h2 = (5 * (Long.rotateLeft(h2 ^ (Long.rotateLeft((((((((((data[i16 + 9] & 255) << 8) | (data[i16 + 8] & 255)) | ((data[i16 + 10] & 255) << 16)) | ((data[i16 + 11] & 255) << 24)) | ((data[i16 + 12] & 255) << 32)) | ((data[i16 + 13] & 255) << 40)) | ((data[i16 + 14] & 255) << 48)) | ((data[i16 + 15] & 255) << 56)) * C2, 33) * C1), 31) + h1)) + 944331445;
        }
        long k1 = 0;
        long k2 = 0;
        int tailStart = nblocks << 4;
        switch (length - tailStart) {
            case 15:
                k2 = 0 ^ ((data[tailStart + 14] & 255) << 48);
            case 14:
                k2 ^= (data[tailStart + 13] & 255) << 40;
            case 13:
                k2 ^= (data[tailStart + 12] & 255) << 32;
            case 12:
                k2 ^= (data[tailStart + 11] & 255) << 24;
            case 11:
                k2 ^= (data[tailStart + 10] & 255) << 16;
            case 10:
                k2 ^= (data[tailStart + 9] & 255) << 8;
            case 9:
                h2 ^= Long.rotateLeft((k2 ^ (data[tailStart + 8] & 255)) * C2, 33) * C1;
            case 8:
                k1 = 0 ^ ((data[tailStart + 7] & 255) << 56);
            case 7:
                k1 ^= (data[tailStart + 6] & 255) << 48;
            case 6:
                k1 ^= (data[tailStart + 5] & 255) << 40;
            case 5:
                k1 ^= (data[tailStart + 4] & 255) << 32;
            case 4:
                k1 ^= (data[tailStart + 3] & 255) << 24;
            case 3:
                k1 ^= (data[tailStart + 2] & 255) << 16;
            case 2:
                k1 ^= (data[tailStart + 1] & 255) << 8;
            case 1:
                h1 ^= Long.rotateLeft((k1 ^ (data[tailStart] & 255)) * C1, 31) * C2;
                break;
        }
        long h22 = h2 ^ length;
        long h12 = (h1 ^ length) + h22;
        long h23 = h22 + h12;
        long h13 = fmix64(h12);
        long h24 = fmix64(h23);
        long h14 = h13 + h24;
        return new long[]{h14, h24 + h14};
    }

    private static long fmix64(long h) {
        long h2 = (h ^ (h >>> 33)) * (-49064778989728563L);
        long h3 = (h2 ^ (h2 >>> 33)) * (-4265267296055464877L);
        return h3 ^ (h3 >>> 33);
    }
}
