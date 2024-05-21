package com.jcraft.jsch.jcraft;

import com.jcraft.jsch.MAC;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public class HMACSHA1 extends HMAC implements MAC {
    private static final String name = "hmac-sha1";

    @Override // com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void doFinal(byte[] x0, int x1) {
        super.doFinal(x0, x1);
    }

    @Override // com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ int getBlockSize() {
        return super.getBlockSize();
    }

    @Override // com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void init(byte[] x0) throws Exception {
        super.init(x0);
    }

    @Override // com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void update(int x0) {
        super.update(x0);
    }

    @Override // com.jcraft.jsch.jcraft.HMAC, com.jcraft.jsch.MAC
    public /* bridge */ /* synthetic */ void update(byte[] x0, int x1, int x2) {
        super.update(x0, x1, x2);
    }

    public HMACSHA1() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
            System.err.println(e);
        }
        setH(md);
    }

    @Override // com.jcraft.jsch.MAC
    public String getName() {
        return name;
    }
}
