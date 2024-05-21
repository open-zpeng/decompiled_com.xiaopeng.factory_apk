package com.jcraft.jsch.jcraft;

import java.security.MessageDigest;
/* loaded from: classes.dex */
class HMAC {
    private static final int B = 64;
    private byte[] k_ipad = null;
    private byte[] k_opad = null;
    private MessageDigest md = null;
    private int bsize = 0;
    private final byte[] tmp = new byte[4];

    /* JADX INFO: Access modifiers changed from: protected */
    public void setH(MessageDigest md) {
        this.md = md;
        this.bsize = md.getDigestLength();
    }

    public int getBlockSize() {
        return this.bsize;
    }

    public void init(byte[] key) throws Exception {
        this.md.reset();
        int length = key.length;
        int i = this.bsize;
        if (length > i) {
            byte[] tmp = new byte[i];
            System.arraycopy(key, 0, tmp, 0, i);
            key = tmp;
        }
        if (key.length > 64) {
            this.md.update(key, 0, key.length);
            key = this.md.digest();
        }
        this.k_ipad = new byte[64];
        System.arraycopy(key, 0, this.k_ipad, 0, key.length);
        this.k_opad = new byte[64];
        System.arraycopy(key, 0, this.k_opad, 0, key.length);
        for (int i2 = 0; i2 < 64; i2++) {
            byte[] bArr = this.k_ipad;
            bArr[i2] = (byte) (bArr[i2] ^ 54);
            byte[] bArr2 = this.k_opad;
            bArr2[i2] = (byte) (bArr2[i2] ^ 92);
        }
        this.md.update(this.k_ipad, 0, 64);
    }

    public void update(int i) {
        byte[] bArr = this.tmp;
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) i;
        update(bArr, 0, 4);
    }

    public void update(byte[] foo, int s, int l) {
        this.md.update(foo, s, l);
    }

    public void doFinal(byte[] buf, int offset) {
        byte[] result = this.md.digest();
        this.md.update(this.k_opad, 0, 64);
        this.md.update(result, 0, this.bsize);
        try {
            this.md.digest(buf, offset, this.bsize);
        } catch (Exception e) {
        }
        this.md.update(this.k_ipad, 0, 64);
    }
}
