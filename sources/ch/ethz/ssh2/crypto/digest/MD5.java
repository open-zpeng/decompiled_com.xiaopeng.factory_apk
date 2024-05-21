package ch.ethz.ssh2.crypto.digest;
/* loaded from: classes.dex */
public final class MD5 implements Digest {
    private static final byte[] padding;
    private long count;
    private int state0;
    private int state1;
    private int state2;
    private int state3;
    private final byte[] block = new byte[64];
    private final int[] x = new int[16];

    static {
        byte[] bArr = new byte[64];
        bArr[0] = Byte.MIN_VALUE;
        padding = bArr;
    }

    public MD5() {
        reset();
    }

    private static final int FF(int a, int b, int c, int d, int x, int s, int ac) {
        int a2 = a + ((b & c) | ((~b) & d)) + x + ac;
        return ((a2 << s) | (a2 >>> (32 - s))) + b;
    }

    private static final int GG(int a, int b, int c, int d, int x, int s, int ac) {
        int a2 = a + ((b & d) | ((~d) & c)) + x + ac;
        return ((a2 << s) | (a2 >>> (32 - s))) + b;
    }

    private static final int HH(int a, int b, int c, int d, int x, int s, int ac) {
        int a2 = a + ((b ^ c) ^ d) + x + ac;
        return ((a2 << s) | (a2 >>> (32 - s))) + b;
    }

    private static final int II(int a, int b, int c, int d, int x, int s, int ac) {
        int a2 = a + (((~d) | b) ^ c) + x + ac;
        return ((a2 << s) | (a2 >>> (32 - s))) + b;
    }

    private static final void encode(byte[] dst, int dstoff, int word) {
        dst[dstoff] = (byte) word;
        dst[dstoff + 1] = (byte) (word >> 8);
        dst[dstoff + 2] = (byte) (word >> 16);
        dst[dstoff + 3] = (byte) (word >> 24);
    }

    private final void transform(byte[] src, int pos) {
        int a = this.state0;
        int b = this.state1;
        int c = this.state2;
        int d = this.state3;
        int i = 0;
        int pos2 = pos;
        while (i < 16) {
            this.x[i] = (src[pos2] & 255) | ((src[pos2 + 1] & 255) << 8) | ((src[pos2 + 2] & 255) << 16) | ((src[pos2 + 3] & 255) << 24);
            i++;
            pos2 += 4;
        }
        int a2 = FF(a, b, c, d, this.x[0], 7, -680876936);
        int d2 = FF(d, a2, b, c, this.x[1], 12, -389564586);
        int c2 = FF(c, d2, a2, b, this.x[2], 17, 606105819);
        int b2 = FF(b, c2, d2, a2, this.x[3], 22, -1044525330);
        int a3 = FF(a2, b2, c2, d2, this.x[4], 7, -176418897);
        int d3 = FF(d2, a3, b2, c2, this.x[5], 12, 1200080426);
        int c3 = FF(c2, d3, a3, b2, this.x[6], 17, -1473231341);
        int b3 = FF(b2, c3, d3, a3, this.x[7], 22, -45705983);
        int a4 = FF(a3, b3, c3, d3, this.x[8], 7, 1770035416);
        int d4 = FF(d3, a4, b3, c3, this.x[9], 12, -1958414417);
        int c4 = FF(c3, d4, a4, b3, this.x[10], 17, -42063);
        int b4 = FF(b3, c4, d4, a4, this.x[11], 22, -1990404162);
        int a5 = FF(a4, b4, c4, d4, this.x[12], 7, 1804603682);
        int d5 = FF(d4, a5, b4, c4, this.x[13], 12, -40341101);
        int c5 = FF(c4, d5, a5, b4, this.x[14], 17, -1502002290);
        int b5 = FF(b4, c5, d5, a5, this.x[15], 22, 1236535329);
        int a6 = GG(a5, b5, c5, d5, this.x[1], 5, -165796510);
        int d6 = GG(d5, a6, b5, c5, this.x[6], 9, -1069501632);
        int c6 = GG(c5, d6, a6, b5, this.x[11], 14, 643717713);
        int b6 = GG(b5, c6, d6, a6, this.x[0], 20, -373897302);
        int a7 = GG(a6, b6, c6, d6, this.x[5], 5, -701558691);
        int d7 = GG(d6, a7, b6, c6, this.x[10], 9, 38016083);
        int c7 = GG(c6, d7, a7, b6, this.x[15], 14, -660478335);
        int b7 = GG(b6, c7, d7, a7, this.x[4], 20, -405537848);
        int a8 = GG(a7, b7, c7, d7, this.x[9], 5, 568446438);
        int d8 = GG(d7, a8, b7, c7, this.x[14], 9, -1019803690);
        int c8 = GG(c7, d8, a8, b7, this.x[3], 14, -187363961);
        int b8 = GG(b7, c8, d8, a8, this.x[8], 20, 1163531501);
        int a9 = GG(a8, b8, c8, d8, this.x[13], 5, -1444681467);
        int d9 = GG(d8, a9, b8, c8, this.x[2], 9, -51403784);
        int c9 = GG(c8, d9, a9, b8, this.x[7], 14, 1735328473);
        int b9 = GG(b8, c9, d9, a9, this.x[12], 20, -1926607734);
        int a10 = HH(a9, b9, c9, d9, this.x[5], 4, -378558);
        int d10 = HH(d9, a10, b9, c9, this.x[8], 11, -2022574463);
        int c10 = HH(c9, d10, a10, b9, this.x[11], 16, 1839030562);
        int b10 = HH(b9, c10, d10, a10, this.x[14], 23, -35309556);
        int a11 = HH(a10, b10, c10, d10, this.x[1], 4, -1530992060);
        int d11 = HH(d10, a11, b10, c10, this.x[4], 11, 1272893353);
        int c11 = HH(c10, d11, a11, b10, this.x[7], 16, -155497632);
        int b11 = HH(b10, c11, d11, a11, this.x[10], 23, -1094730640);
        int a12 = HH(a11, b11, c11, d11, this.x[13], 4, 681279174);
        int d12 = HH(d11, a12, b11, c11, this.x[0], 11, -358537222);
        int c12 = HH(c11, d12, a12, b11, this.x[3], 16, -722521979);
        int b12 = HH(b11, c12, d12, a12, this.x[6], 23, 76029189);
        int a13 = HH(a12, b12, c12, d12, this.x[9], 4, -640364487);
        int d13 = HH(d12, a13, b12, c12, this.x[12], 11, -421815835);
        int c13 = HH(c12, d13, a13, b12, this.x[15], 16, 530742520);
        int b13 = HH(b12, c13, d13, a13, this.x[2], 23, -995338651);
        int a14 = II(a13, b13, c13, d13, this.x[0], 6, -198630844);
        int d14 = II(d13, a14, b13, c13, this.x[7], 10, 1126891415);
        int c14 = II(c13, d14, a14, b13, this.x[14], 15, -1416354905);
        int b14 = II(b13, c14, d14, a14, this.x[5], 21, -57434055);
        int a15 = II(a14, b14, c14, d14, this.x[12], 6, 1700485571);
        int d15 = II(d14, a15, b14, c14, this.x[3], 10, -1894986606);
        int c15 = II(c14, d15, a15, b14, this.x[10], 15, -1051523);
        int b15 = II(b14, c15, d15, a15, this.x[1], 21, -2054922799);
        int a16 = II(a15, b15, c15, d15, this.x[8], 6, 1873313359);
        int d16 = II(d15, a16, b15, c15, this.x[15], 10, -30611744);
        int c16 = II(c15, d16, a16, b15, this.x[6], 15, -1560198380);
        int b16 = II(b15, c16, d16, a16, this.x[13], 21, 1309151649);
        int a17 = II(a16, b16, c16, d16, this.x[4], 6, -145523070);
        int d17 = II(d16, a17, b16, c16, this.x[11], 10, -1120210379);
        int c17 = II(c16, d17, a17, b16, this.x[2], 15, 718787259);
        int b17 = II(b16, c17, d17, a17, this.x[9], 21, -343485551);
        this.state0 += a17;
        this.state1 += b17;
        this.state2 += c17;
        this.state3 += d17;
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void reset() {
        this.count = 0L;
        this.state0 = 1732584193;
        this.state1 = -271733879;
        this.state2 = -1732584194;
        this.state3 = 271733878;
        for (int i = 0; i < 16; i++) {
            this.x[i] = 0;
        }
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte b) {
        long j = this.count;
        int space = 64 - ((int) (63 & j));
        this.count = j + 1;
        byte[] bArr = this.block;
        bArr[64 - space] = b;
        if (space == 1) {
            transform(bArr, 0);
        }
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte[] buff, int pos, int len) {
        long j = this.count;
        int space = 64 - ((int) (63 & j));
        this.count = j + len;
        while (len > 0) {
            if (len < space) {
                System.arraycopy(buff, pos, this.block, 64 - space, len);
                return;
            }
            if (space == 64) {
                transform(buff, pos);
            } else {
                System.arraycopy(buff, pos, this.block, 64 - space, space);
                transform(this.block, 0);
            }
            pos += space;
            len -= space;
            space = 64;
        }
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte[] b) {
        update(b, 0, b.length);
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void digest(byte[] dst, int pos) {
        byte[] bits = new byte[8];
        encode(bits, 0, (int) (this.count << 3));
        encode(bits, 4, (int) (this.count >> 29));
        int idx = ((int) this.count) & 63;
        int padLen = idx < 56 ? 56 - idx : 120 - idx;
        update(padding, 0, padLen);
        update(bits, 0, 8);
        encode(dst, pos, this.state0);
        encode(dst, pos + 4, this.state1);
        encode(dst, pos + 8, this.state2);
        encode(dst, pos + 12, this.state3);
        reset();
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void digest(byte[] dst) {
        digest(dst, 0);
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final int getDigestLength() {
        return 16;
    }
}
