package com.jcraft.jsch.jce;

import com.jcraft.jsch.HASH;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public class SHA512 implements HASH {
    MessageDigest md;

    @Override // com.jcraft.jsch.HASH
    public int getBlockSize() {
        return 64;
    }

    @Override // com.jcraft.jsch.HASH
    public void init() throws Exception {
        try {
            this.md = MessageDigest.getInstance("SHA-512");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override // com.jcraft.jsch.HASH
    public void update(byte[] foo, int start, int len) throws Exception {
        this.md.update(foo, start, len);
    }

    @Override // com.jcraft.jsch.HASH
    public byte[] digest() throws Exception {
        return this.md.digest();
    }
}
