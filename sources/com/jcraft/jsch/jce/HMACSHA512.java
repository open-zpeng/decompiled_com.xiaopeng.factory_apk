package com.jcraft.jsch.jce;
/* loaded from: classes.dex */
public class HMACSHA512 extends HMAC {
    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void doFinal(byte[] x0, int x1) {
        super.doFinal(x0, x1);
    }

    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ int getBlockSize() {
        return super.getBlockSize();
    }

    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ String getName() {
        return super.getName();
    }

    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void init(byte[] x0) throws Exception {
        super.init(x0);
    }

    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void update(int x0) {
        super.update(x0);
    }

    @Override // com.jcraft.jsch.jce.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void update(byte[] x0, int x1, int x2) {
        super.update(x0, x1, x2);
    }

    public HMACSHA512() {
        this.name = "hmac-sha2-512";
        this.bsize = 64;
        this.algorithm = "HmacSHA512";
    }
}
