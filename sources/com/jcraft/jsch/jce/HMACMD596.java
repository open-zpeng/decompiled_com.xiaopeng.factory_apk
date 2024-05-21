package com.jcraft.jsch.jce;
/* loaded from: classes.dex */
public class HMACMD596 extends HMACMD5 {
    private final byte[] _buf16 = new byte[16];

    public HMACMD596() {
        this.name = "hmac-md5-96";
    }

    @Override // com.jcraft.jsch.jce.HMACMD5, com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public int getBlockSize() {
        return 12;
    }

    @Override // com.jcraft.jsch.jce.HMACMD5, com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public void doFinal(byte[] buf, int offset) {
        super.doFinal(this._buf16, 0);
        System.arraycopy(this._buf16, 0, buf, offset, 12);
    }
}
