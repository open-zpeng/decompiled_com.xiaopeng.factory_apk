package com.jcraft.jsch.jce;

import com.jcraft.jsch.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public class BlowfishCBC implements Cipher {
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
        if (iv.length > 8) {
            byte[] tmp = new byte[8];
            System.arraycopy(iv, 0, tmp, 0, tmp.length);
            iv = tmp;
        }
        if (key.length > 16) {
            byte[] tmp2 = new byte[16];
            System.arraycopy(key, 0, tmp2, 0, tmp2.length);
            key = tmp2;
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, "Blowfish");
            this.cipher = javax.crypto.Cipher.getInstance("Blowfish/CBC/NoPadding");
            synchronized (javax.crypto.Cipher.class) {
                this.cipher.init(mode == 0 ? 1 : 2, skeySpec, new IvParameterSpec(iv));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override // com.jcraft.jsch.Cipher
    public void update(byte[] foo, int s1, int len, byte[] bar, int s2) throws Exception {
        this.cipher.update(foo, s1, len, bar, s2);
    }

    @Override // com.jcraft.jsch.Cipher
    public boolean isCBC() {
        return true;
    }
}
