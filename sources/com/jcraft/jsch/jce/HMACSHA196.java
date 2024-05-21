package com.jcraft.jsch.jce;
/* loaded from: classes.dex */
public class HMACSHA196 extends HMACSHA1 {
    private final byte[] _buf20 = new byte[20];

    public HMACSHA196() {
        this.name = "hmac-sha1-96";
    }

    @Override // com.jcraft.jsch.jce.HMACSHA1, com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public int getBlockSize() {
        return 12;
    }

    @Override // com.jcraft.jsch.jce.HMACSHA1, com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public void doFinal(byte[] buf, int offset) {
        super.doFinal(this._buf20, 0);
        System.arraycopy(this._buf20, 0, buf, offset, 12);
    }
}
