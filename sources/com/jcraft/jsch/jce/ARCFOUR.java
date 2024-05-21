package com.jcraft.jsch.jce;

import com.jcraft.jsch.Cipher;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public class ARCFOUR implements Cipher {
    private static final int bsize = 16;
    private static final int ivsize = 8;
    private javax.crypto.Cipher cipher;

    @Override // com.jcraft.jsch.Cipher
    public int getIVSize() {
        return 8;
    }

    @Override // com.jcraft.jsch.Cipher
    public int getBlockSize() {
        return 16;
    }

    @Override // com.jcraft.jsch.Cipher
    public void init(int mode, byte[] key, byte[] iv) throws Exception {
        if (key.length > 16) {
            byte[] tmp = new byte[16];
            System.arraycopy(key, 0, tmp, 0, tmp.length);
            key = tmp;
        }
        try {
            this.cipher = javax.crypto.Cipher.getInstance("RC4");
            SecretKeySpec _key = new SecretKeySpec(key, "RC4");
            synchronized (javax.crypto.Cipher.class) {
                this.cipher.init(mode == 0 ? 1 : 2, _key);
            }
        } catch (Exception e) {
            this.cipher = null;
            throw e;
        }
    }

    @Override // com.jcraft.jsch.Cipher
    public void update(byte[] foo, int s1, int len, byte[] bar, int s2) throws Exception {
        this.cipher.update(foo, s1, len, bar, s2);
    }

    @Override // com.jcraft.jsch.Cipher
    public boolean isCBC() {
        return false;
    }
}
