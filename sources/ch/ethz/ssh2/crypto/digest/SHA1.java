package ch.ethz.ssh2.crypto.digest;

import net.lingala.zip4j.crypto.PBKDF2.BinTools;
/* loaded from: classes.dex */
public final class SHA1 implements Digest {
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private long currentLen;
    private int currentPos;
    private final byte[] msg = new byte[64];
    private final int[] w = new int[80];

    public SHA1() {
        reset();
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final int getDigestLength() {
        return 20;
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void reset() {
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
        this.H4 = -1009589776;
        this.currentPos = 0;
        this.currentLen = 0L;
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(b[i]);
        }
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte[] b) {
        for (byte b2 : b) {
            update(b2);
        }
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void update(byte b) {
        byte[] bArr = this.msg;
        int i = this.currentPos;
        this.currentPos = i + 1;
        bArr[i] = b;
        this.currentLen += 8;
        if (this.currentPos == 64) {
            perform();
            this.currentPos = 0;
        }
    }

    private static final String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(BinTools.hex.charAt((b[i] >> 4) & 15));
            sb.append(BinTools.hex.charAt(b[i] & 15));
        }
        return sb.toString();
    }

    private final void putInt(byte[] b, int pos, int val) {
        b[pos] = (byte) (val >> 24);
        b[pos + 1] = (byte) (val >> 16);
        b[pos + 2] = (byte) (val >> 8);
        b[pos + 3] = (byte) val;
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void digest(byte[] out) {
        digest(out, 0);
    }

    @Override // ch.ethz.ssh2.crypto.digest.Digest
    public final void digest(byte[] out, int off) {
        long l = this.currentLen;
        update(Byte.MIN_VALUE);
        while (this.currentPos != 56) {
            update((byte) 0);
        }
        update((byte) (l >> 56));
        update((byte) (l >> 48));
        update((byte) (l >> 40));
        update((byte) (l >> 32));
        update((byte) (l >> 24));
        update((byte) (l >> 16));
        update((byte) (l >> 8));
        update((byte) l);
        putInt(out, off, this.H0);
        putInt(out, off + 4, this.H1);
        putInt(out, off + 8, this.H2);
        putInt(out, off + 12, this.H3);
        putInt(out, off + 16, this.H4);
        reset();
    }

    private final void perform() {
        for (int i = 0; i < 16; i++) {
            int[] iArr = this.w;
            byte[] bArr = this.msg;
            iArr[i] = ((bArr[(i * 4) + 1] & 255) << 16) | ((bArr[i * 4] & 255) << 24) | ((bArr[(i * 4) + 2] & 255) << 8) | (bArr[(i * 4) + 3] & 255);
        }
        for (int t = 16; t < 80; t++) {
            int[] iArr2 = this.w;
            int x = ((iArr2[t - 3] ^ iArr2[t - 8]) ^ iArr2[t - 14]) ^ iArr2[t - 16];
            iArr2[t] = (x << 1) | (x >>> 31);
        }
        int A = this.H0;
        int B = this.H1;
        int C = this.H2;
        int D = this.H3;
        int E = this.H4;
        for (int t2 = 0; t2 <= 19; t2++) {
            int T = A << 5;
            int T2 = (T | (A >>> 27)) + ((B & C) | ((~B) & D)) + E + this.w[t2] + 1518500249;
            E = D;
            D = C;
            C = (B << 30) | (B >>> 2);
            B = A;
            A = T2;
        }
        for (int t3 = 20; t3 <= 39; t3++) {
            int T3 = A << 5;
            int T4 = (T3 | (A >>> 27)) + ((B ^ C) ^ D) + E + this.w[t3] + 1859775393;
            E = D;
            D = C;
            C = (B << 30) | (B >>> 2);
            B = A;
            A = T4;
        }
        for (int t4 = 40; t4 <= 59; t4++) {
            int T5 = A << 5;
            int T6 = ((((T5 | (A >>> 27)) + (((B & C) | (B & D)) | (C & D))) + E) + this.w[t4]) - 1894007588;
            E = D;
            D = C;
            C = (B << 30) | (B >>> 2);
            B = A;
            A = T6;
        }
        for (int t5 = 60; t5 <= 79; t5++) {
            int T7 = (((((A << 5) | (A >>> 27)) + ((B ^ C) ^ D)) + E) + this.w[t5]) - 899497514;
            E = D;
            D = C;
            C = (B << 30) | (B >>> 2);
            B = A;
            A = T7;
        }
        int t6 = this.H0;
        this.H0 = t6 + A;
        this.H1 += B;
        this.H2 += C;
        this.H3 += D;
        this.H4 += E;
    }

    public static void main(String[] args) {
        SHA1 sha = new SHA1();
        byte[] dig1 = new byte[20];
        byte[] dig2 = new byte[20];
        byte[] dig3 = new byte[20];
        sha.update("abc".getBytes());
        sha.digest(dig1);
        sha.update("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".getBytes());
        sha.digest(dig2);
        for (int i = 0; i < 1000000; i++) {
            sha.update((byte) 97);
        }
        sha.digest(dig3);
        String dig1_res = toHexString(dig1);
        String dig2_res = toHexString(dig2);
        String dig3_res = toHexString(dig3);
        if (dig1_res.equals("A9993E364706816ABA3E25717850C26C9CD0D89D")) {
            System.out.println("SHA-1 Test 1 OK.");
        } else {
            System.out.println("SHA-1 Test 1 FAILED.");
        }
        if (dig2_res.equals("84983E441C3BD26EBAAE4AA1F95129E5E54670F1")) {
            System.out.println("SHA-1 Test 2 OK.");
        } else {
            System.out.println("SHA-1 Test 2 FAILED.");
        }
        if (dig3_res.equals("34AA973CD4C4DAA4F61EEB2BDBAD27316534016F")) {
            System.out.println("SHA-1 Test 3 OK.");
        } else {
            System.out.println("SHA-1 Test 3 FAILED.");
        }
    }
}
