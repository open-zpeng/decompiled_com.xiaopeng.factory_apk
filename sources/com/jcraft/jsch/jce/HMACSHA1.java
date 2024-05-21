package com.jcraft.jsch.jce;

import net.lingala.zip4j.util.InternalZipConstants;
/* loaded from: classes.dex */
public class HMACSHA1 extends HMAC {
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

    public HMACSHA1() {
        this.name = "hmac-sha1";
        this.bsize = 20;
        this.algorithm = InternalZipConstants.AES_MAC_ALGORITHM;
    }
}
