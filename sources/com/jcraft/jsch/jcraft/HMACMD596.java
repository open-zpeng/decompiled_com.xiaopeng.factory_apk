package com.jcraft.jsch.jcraft;
/* loaded from: classes.dex */
public class HMACMD596 extends HMACMD5 {
    private static final int BSIZE = 12;
    private static final String name = "hmac-md5-96";
    private final byte[] _buf16 = new byte[16];

    @Override // com.jcraft.jsch.jcraft.HMACMD5, com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public int getBlockSize() {
        return 12;
    }

    @Override // com.jcraft.jsch.jcraft.HMACMD5, com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public void doFinal(byte[] buf, int offset) {
        super.doFinal(this._buf16, 0);
        System.arraycopy(this._buf16, 0, buf, offset, 12);
    }

    @Override // com.jcraft.jsch.jcraft.HMACMD5, com.jcraft.jsch.MAC
    public String getName() {
        return name;
    }
}
