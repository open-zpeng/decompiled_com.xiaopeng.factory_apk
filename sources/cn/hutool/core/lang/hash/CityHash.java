package cn.hutool.core.lang.hash;

import java.util.Arrays;
import net.lingala.zip4j.util.InternalZipConstants;
/* loaded from: classes.dex */
public class CityHash {
    private static final int c1 = -862048943;
    private static final int c2 = 461845907;
    private static final long k0 = -4348849565147123417L;
    private static final long k1 = -5435081209227447693L;
    private static final long k2 = -7286425919675154353L;
    private static final long kMul = -7070675565921424023L;

    public static int hash32(byte[] data) {
        int len = data.length;
        if (len > 24) {
            int g = len * c1;
            int a0 = rotate32(fetch32(data, len - 4) * c1, 17) * c2;
            int a1 = rotate32(fetch32(data, len - 8) * c1, 17) * c2;
            int a2 = rotate32(fetch32(data, len - 16) * c1, 17) * c2;
            int a3 = rotate32(fetch32(data, len - 12) * c1, 17) * c2;
            int a4 = rotate32(fetch32(data, len - 20) * c1, 17) * c2;
            int h = len ^ a0;
            int h2 = (rotate32(((rotate32(h, 19) * 5) - 430675100) ^ a2, 19) * 5) - 430675100;
            int h3 = g ^ a1;
            int h4 = (rotate32(((rotate32(h3, 19) * 5) - 430675100) ^ a3, 19) * 5) - 430675100;
            int f = g + a4;
            int f2 = (rotate32(f, 19) * 5) - 430675100;
            int f3 = len - 1;
            int iters = f3 / 20;
            int iters2 = iters;
            int iters3 = 0;
            while (true) {
                int a02 = rotate32(fetch32(data, iters3) * c1, 17) * c2;
                int a12 = fetch32(data, iters3 + 4);
                int a22 = rotate32(fetch32(data, iters3 + 8) * c1, 17) * c2;
                int a32 = rotate32(fetch32(data, iters3 + 12) * c1, 17) * c2;
                int a42 = fetch32(data, iters3 + 16);
                int h5 = (rotate32(h2 ^ a02, 18) * 5) - 430675100;
                int f4 = rotate32(f2 + a12, 19) * c1;
                int h6 = ((rotate32(h4 + a22, 18) * 5) - 430675100) ^ a42;
                int h7 = Integer.reverseBytes(((rotate32(h5 ^ (a32 + a12), 19) * 5) - 430675100) + (a42 * 5));
                int f5 = f4 + a02;
                f2 = Integer.reverseBytes(h6) * 5;
                iters3 += 20;
                iters2--;
                if (iters2 != 0) {
                    h2 = f5;
                    h4 = h7;
                } else {
                    int g2 = rotate32(rotate32(h7, 11) * c1, 17) * c1;
                    int f6 = rotate32(rotate32(f2, 11) * c1, 17) * c1;
                    int h8 = rotate32(f5 + g2, 19);
                    return rotate32((rotate32((rotate32((h8 * 5) - 430675100, 17) * c1) + f6, 19) * 5) - 430675100, 17) * c1;
                }
            }
        } else if (len <= 12) {
            return len <= 4 ? hash32Len0to4(data) : hash32Len5to12(data);
        } else {
            return hash32Len13to24(data);
        }
    }

    public static long hash64(byte[] data) {
        int len = data.length;
        if (len <= 32) {
            if (len <= 16) {
                return hashLen0to16(data);
            }
            return hashLen17to32(data);
        } else if (len > 64) {
            long x = fetch64(data, len - 40);
            long y = fetch64(data, len - 16) + fetch64(data, len - 56);
            long z = hashLen16(fetch64(data, len - 48) + len, fetch64(data, len - 24));
            Number128 v = weakHashLen32WithSeeds(data, len - 64, len, z);
            Number128 w = weakHashLen32WithSeeds(data, len - 32, y + k1, x);
            long x2 = (x * k1) + fetch64(data, 0);
            Number128 w2 = w;
            int len2 = (len - 1) & (-64);
            int pos = 0;
            while (true) {
                long x3 = rotate(x2 + y + v.getLowValue() + fetch64(data, pos + 8), 37) * k1;
                long y2 = rotate(v.getHighValue() + y + fetch64(data, pos + 48), 42) * k1;
                long x4 = w2.getHighValue() ^ x3;
                long y3 = y2 + v.getLowValue() + fetch64(data, pos + 40);
                long z2 = rotate(w2.getLowValue() + z, 33) * k1;
                v = weakHashLen32WithSeeds(data, pos, v.getHighValue() * k1, x4 + w2.getLowValue());
                w2 = weakHashLen32WithSeeds(data, pos + 32, w2.getHighValue() + z2, y3 + fetch64(data, pos + 16));
                z = x4;
                pos += 64;
                len2 -= 64;
                if (len2 != 0) {
                    x2 = z2;
                    y = y3;
                } else {
                    return hashLen16(hashLen16(v.getLowValue(), w2.getLowValue()) + (shiftMix(y3) * k1) + z, hashLen16(v.getHighValue(), w2.getHighValue()) + z2);
                }
            }
        } else {
            return hashLen33to64(data);
        }
    }

    public static long hash64(byte[] data, long seed0, long seed1) {
        return hashLen16(hash64(data) - seed0, seed1);
    }

    public static long hash64(byte[] data, long seed) {
        return hash64(data, k2, seed);
    }

    public static Number128 hash128(byte[] data) {
        int len = data.length;
        if (len >= 16) {
            return hash128(data, 16, new Number128(fetch64(data, 0), fetch64(data, 8) + k0));
        }
        return hash128(data, 0, new Number128(k0, k1));
    }

    public static Number128 hash128(byte[] data, Number128 seed) {
        return hash128(data, 0, seed);
    }

    private static Number128 hash128(byte[] byteArray, int start, Number128 seed) {
        long x;
        long y;
        byte[] bArr = byteArray;
        int len = bArr.length - start;
        if (len < 128) {
            return cityMurmur(Arrays.copyOfRange(bArr, start, bArr.length), seed);
        }
        Number128 v = new Number128(0L, 0L);
        Number128 w = new Number128(0L, 0L);
        long x2 = seed.getLowValue();
        long z = seed.getHighValue();
        long z2 = len * k1;
        v.setLowValue((rotate(z ^ k1, 49) * k1) + fetch64(byteArray, start));
        v.setHighValue((rotate(v.getLowValue(), 42) * k1) + fetch64(bArr, start + 8));
        w.setLowValue((rotate(z + z2, 35) * k1) + x2);
        w.setHighValue(rotate(fetch64(bArr, start + 88) + x2, 53) * k1);
        Number128 v2 = v;
        int pos = start;
        int len2 = len;
        Number128 w2 = w;
        long z3 = z2;
        while (true) {
            long x3 = rotate(x2 + z + v2.getLowValue() + fetch64(bArr, pos + 8), 37) * k1;
            long y2 = rotate(v2.getHighValue() + z + fetch64(bArr, pos + 48), 42) * k1;
            long y3 = w2.getHighValue();
            long x4 = y3 ^ x3;
            long y4 = v2.getLowValue() + fetch64(bArr, pos + 40) + y2;
            long y5 = w2.getLowValue();
            long z4 = rotate(y5 + z3, 33) * k1;
            Number128 v3 = weakHashLen32WithSeeds(byteArray, pos, v2.getHighValue() * k1, x4 + w2.getLowValue());
            Number128 w3 = weakHashLen32WithSeeds(byteArray, pos + 32, z4 + w2.getHighValue(), fetch64(bArr, pos + 16) + y4);
            int pos2 = pos + 64;
            long x5 = rotate(z4 + y4 + v3.getLowValue() + fetch64(bArr, pos2 + 8), 37) * k1;
            long y6 = rotate(v3.getHighValue() + y4 + fetch64(bArr, pos2 + 48), 42) * k1;
            x = w3.getHighValue() ^ x5;
            y = y6 + v3.getLowValue() + fetch64(bArr, pos2 + 40);
            long y7 = w3.getLowValue();
            long z5 = rotate(y7 + x4, 33) * k1;
            v2 = weakHashLen32WithSeeds(byteArray, pos2, v3.getHighValue() * k1, x + w3.getLowValue());
            w2 = weakHashLen32WithSeeds(byteArray, pos2 + 32, z5 + w3.getHighValue(), fetch64(bArr, pos2 + 16) + y);
            x2 = z5;
            pos = pos2 + 64;
            len2 -= 128;
            if (len2 < 128) {
                break;
            }
            bArr = byteArray;
            z3 = x;
            z = y;
        }
        long x6 = x2 + (rotate(v2.getLowValue() + x, 49) * k0);
        long y8 = (y * k0) + rotate(w2.getHighValue(), 37);
        w2.setLowValue(w2.getLowValue() * 9);
        v2.setLowValue(v2.getLowValue() * k0);
        int tail_done = 0;
        long z6 = (x * k0) + rotate(w2.getLowValue(), 27);
        Number128 v4 = v2;
        while (tail_done < len2) {
            int tail_done2 = tail_done + 32;
            long y9 = (rotate(x6 + y8, 42) * k0) + v4.getHighValue();
            long y10 = w2.getLowValue();
            w2.setLowValue(y10 + fetch64(bArr, ((pos + len2) - tail_done2) + 16));
            long x7 = (x6 * k0) + w2.getLowValue();
            z6 += w2.getHighValue() + fetch64(bArr, (pos + len2) - tail_done2);
            w2.setHighValue(w2.getHighValue() + v4.getLowValue());
            v4 = weakHashLen32WithSeeds(byteArray, (pos + len2) - tail_done2, v4.getLowValue() + z6, v4.getHighValue());
            v4.setLowValue(v4.getLowValue() * k0);
            tail_done = tail_done2;
            y8 = y9;
            x6 = x7;
        }
        long y11 = v4.getLowValue();
        long x8 = hashLen16(x6, y11);
        long y12 = hashLen16(y8 + z6, w2.getLowValue());
        return new Number128(hashLen16(v4.getHighValue() + x8, w2.getHighValue()) + y12, hashLen16(w2.getHighValue() + x8, y12 + v4.getHighValue()));
    }

    private static int hash32Len0to4(byte[] byteArray) {
        int b = 0;
        int c = 9;
        int len = byteArray.length;
        for (int v : byteArray) {
            b = (c1 * b) + v;
            c ^= b;
        }
        return fmix(mur(b, mur(len, c)));
    }

    private static int hash32Len5to12(byte[] byteArray) {
        int len = byteArray.length;
        int b = len * 5;
        int a = len + fetch32(byteArray, 0);
        int b2 = b + fetch32(byteArray, len - 4);
        int c = 9 + fetch32(byteArray, (len >>> 1) & 4);
        return fmix(mur(c, mur(b2, mur(a, b))));
    }

    private static int hash32Len13to24(byte[] byteArray) {
        int len = byteArray.length;
        int a = fetch32(byteArray, (len >>> 1) - 4);
        int b = fetch32(byteArray, 4);
        int c = fetch32(byteArray, len - 8);
        int d = fetch32(byteArray, len >>> 1);
        int e = fetch32(byteArray, 0);
        int f = fetch32(byteArray, len - 4);
        return fmix(mur(f, mur(e, mur(d, mur(c, mur(b, mur(a, len)))))));
    }

    private static long hashLen0to16(byte[] byteArray) {
        int len = byteArray.length;
        if (len >= 8) {
            long mul = (len * 2) + k2;
            long a = fetch64(byteArray, 0) + k2;
            long b = fetch64(byteArray, len - 8);
            long c = (rotate(b, 37) * mul) + a;
            long d = (rotate(a, 25) + b) * mul;
            return hashLen16(c, d, mul);
        } else if (len >= 4) {
            long mul2 = (len * 2) + k2;
            long a2 = fetch32(byteArray, 0) & InternalZipConstants.ZIP_64_SIZE_LIMIT;
            return hashLen16(len + (a2 << 3), fetch32(byteArray, len - 4) & InternalZipConstants.ZIP_64_SIZE_LIMIT, mul2);
        } else if (len > 0) {
            int a3 = byteArray[0] & 255;
            int c3 = byteArray[len - 1] & 255;
            int y = ((byteArray[len >>> 1] & 255) << 8) + a3;
            int z = (c3 << 2) + len;
            return shiftMix((y * k2) ^ (z * k0)) * k2;
        } else {
            return k2;
        }
    }

    private static long hashLen17to32(byte[] byteArray) {
        int len = byteArray.length;
        long mul = (len * 2) + k2;
        long a = fetch64(byteArray, 0) * k1;
        long b = fetch64(byteArray, 8);
        long c = fetch64(byteArray, len - 8) * mul;
        long d = fetch64(byteArray, len - 16) * k2;
        return hashLen16(rotate(a + b, 43) + rotate(c, 30) + d, rotate(k2 + b, 18) + a + c, mul);
    }

    private static long hashLen33to64(byte[] byteArray) {
        int len = byteArray.length;
        long mul = (len * 2) + k2;
        long a = fetch64(byteArray, 0) * k2;
        long b = fetch64(byteArray, 8);
        long c = fetch64(byteArray, len - 24);
        long d = fetch64(byteArray, len - 32);
        long e = fetch64(byteArray, 16) * k2;
        long f = fetch64(byteArray, 24) * 9;
        long g = fetch64(byteArray, len - 8);
        long h = fetch64(byteArray, len - 16) * mul;
        long u = rotate(a + g, 43) + ((rotate(b, 30) + c) * 9);
        long v = ((a + g) ^ d) + f + 1;
        long w = Long.reverseBytes((u + v) * mul) + h;
        long u2 = e + f;
        long x = rotate(u2, 42) + c;
        long y = (Long.reverseBytes((v + w) * mul) + g) * mul;
        long z = e + f + c;
        return (shiftMix(((z + Long.reverseBytes(((x + z) * mul) + y) + b) * mul) + d + h) * mul) + x;
    }

    private static long loadUnaligned64(byte[] byteArray, int start) {
        long result = 0;
        OrderIter orderIter = new OrderIter(8);
        while (orderIter.hasNext()) {
            int next = orderIter.next();
            long value = (byteArray[next + start] & 255) << (next * 8);
            result |= value;
        }
        return result;
    }

    private static int loadUnaligned32(byte[] byteArray, int start) {
        int result = 0;
        OrderIter orderIter = new OrderIter(4);
        while (orderIter.hasNext()) {
            int next = orderIter.next();
            int value = (byteArray[next + start] & 255) << (next * 8);
            result |= value;
        }
        return result;
    }

    private static long fetch64(byte[] byteArray, int start) {
        return loadUnaligned64(byteArray, start);
    }

    private static int fetch32(byte[] byteArray, int start) {
        return loadUnaligned32(byteArray, start);
    }

    private static long rotate(long val, int shift) {
        return shift == 0 ? val : (val >>> shift) | (val << (64 - shift));
    }

    private static int rotate32(int val, int shift) {
        return shift == 0 ? val : (val >>> shift) | (val << (32 - shift));
    }

    private static long hashLen16(long u, long v, long mul) {
        long a = (u ^ v) * mul;
        long b = (v ^ (a ^ (a >>> 47))) * mul;
        return (b ^ (b >>> 47)) * mul;
    }

    private static long hashLen16(long u, long v) {
        return hash128to64(new Number128(u, v));
    }

    private static long hash128to64(Number128 number128) {
        long a = (number128.getLowValue() ^ number128.getHighValue()) * kMul;
        long b = (number128.getHighValue() ^ (a ^ (a >>> 47))) * kMul;
        return (b ^ (b >>> 47)) * kMul;
    }

    private static long shiftMix(long val) {
        return (val >>> 47) ^ val;
    }

    private static int fmix(int h) {
        int h2 = (h ^ (h >>> 16)) * (-2048144789);
        int h3 = (h2 ^ (h2 >>> 13)) * (-1028477387);
        return h3 ^ (h3 >>> 16);
    }

    private static int mur(int a, int h) {
        return (rotate32(h ^ (rotate32(a * c1, 17) * c2), 19) * 5) - 430675100;
    }

    private static Number128 weakHashLen32WithSeeds(long w, long x, long y, long z, long a, long b) {
        long a2 = a + w;
        long b2 = rotate(b + a2 + z, 21);
        long a3 = a2 + x + y;
        return new Number128(a3 + z, b2 + rotate(a3, 44) + a2);
    }

    private static Number128 weakHashLen32WithSeeds(byte[] byteArray, int start, long a, long b) {
        return weakHashLen32WithSeeds(fetch64(byteArray, start), fetch64(byteArray, start + 8), fetch64(byteArray, start + 16), fetch64(byteArray, start + 24), a, b);
    }

    private static Number128 cityMurmur(byte[] byteArray, Number128 seed) {
        long d;
        long a;
        long c;
        byte[] bArr = byteArray;
        int len = bArr.length;
        long a2 = seed.getLowValue();
        long b = seed.getHighValue();
        int l = len - 16;
        if (l <= 0) {
            long a3 = shiftMix(a2 * k1) * k1;
            c = (k1 * b) + hashLen0to16(byteArray);
            d = shiftMix((len >= 8 ? fetch64(bArr, 0) : c) + a3);
            a = a3;
        } else {
            long c3 = hashLen16(fetch64(bArr, len - 8) + k1, a2);
            d = hashLen16(len + b, fetch64(bArr, len - 16) + c3);
            a = a2 + d;
            int pos = 0;
            while (true) {
                a = (a ^ (shiftMix(fetch64(bArr, pos) * k1) * k1)) * k1;
                b ^= a;
                c3 = (c3 ^ (shiftMix(fetch64(bArr, pos + 8) * k1) * k1)) * k1;
                d ^= c3;
                pos += 16;
                l -= 16;
                if (l <= 0) {
                    break;
                }
                bArr = byteArray;
            }
            c = c3;
        }
        long a4 = hashLen16(a, c);
        long b2 = hashLen16(d, b);
        return new Number128(a4 ^ b2, hashLen16(b2, a4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class OrderIter {
        private static final boolean IS_LITTLE_ENDIAN = "little".equals(System.getProperty("sun.cpu.endian"));
        private int index;
        private final int size;

        OrderIter(int size) {
            this.size = size;
        }

        boolean hasNext() {
            return this.index < this.size;
        }

        int next() {
            if (IS_LITTLE_ENDIAN) {
                int i = this.index;
                this.index = i + 1;
                return i;
            }
            int i2 = this.index;
            this.index = i2 + 1;
            return (this.size - 1) - i2;
        }
    }
}
