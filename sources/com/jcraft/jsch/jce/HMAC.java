package com.jcraft.jsch.jce;

import com.jcraft.jsch.MAC;
import javax.crypto.Mac;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
abstract class HMAC implements MAC {
    protected String algorithm;
    protected int bsize;
    private Mac mac;
    protected String name;
    private final byte[] tmp = new byte[4];

    @Override // com.jcraft.jsch.MAC
    public int getBlockSize() {
        return this.bsize;
    }

    @Override // com.jcraft.jsch.MAC
    public void init(byte[] key) throws Exception {
        int length = key.length;
        int i = this.bsize;
        if (length > i) {
            byte[] tmp = new byte[i];
            System.arraycopy(key, 0, tmp, 0, i);
            key = tmp;
        }
        SecretKeySpec skey = new SecretKeySpec(key, this.algorithm);
        this.mac = Mac.getInstance(this.algorithm);
        this.mac.init(skey);
    }

    @Override // com.jcraft.jsch.MAC
    public void update(int i) {
        byte[] bArr = this.tmp;
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) i;
        update(bArr, 0, 4);
    }

    @Override // com.jcraft.jsch.MAC
    public void update(byte[] foo, int s, int l) {
        this.mac.update(foo, s, l);
    }

    @Override // com.jcraft.jsch.MAC
    public void doFinal(byte[] buf, int offset) {
        try {
            this.mac.doFinal(buf, offset);
        } catch (ShortBufferException e) {
            System.err.println(e);
        }
    }

    @Override // com.jcraft.jsch.MAC
    public String getName() {
        return this.name;
    }
}
