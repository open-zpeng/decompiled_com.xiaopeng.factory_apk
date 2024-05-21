package com.jcraft.jsch.jce;

import com.jcraft.jsch.JSchException;
import com.xpeng.upso.aesserver.AesConstants;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
/* loaded from: classes.dex */
public class KeyPairGenECDSA implements com.jcraft.jsch.KeyPairGenECDSA {
    byte[] d;
    ECParameterSpec params;
    ECPrivateKey prvKey;
    ECPublicKey pubKey;
    byte[] r;
    byte[] s;

    @Override // com.jcraft.jsch.KeyPairGenECDSA
    public void init(int key_size) throws Exception {
        String name;
        if (key_size == 256) {
            name = "secp256r1";
        } else if (key_size == 384) {
            name = "secp384r1";
        } else if (key_size != 521) {
            throw new JSchException("unsupported key size: " + key_size);
        } else {
            name = "secp521r1";
        }
        for (int i = 0; i < 1000; i++) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(AesConstants.SERVER_MAGIC_CDU);
            ECGenParameterSpec ecsp = new ECGenParameterSpec(name);
            kpg.initialize(ecsp);
            KeyPair kp = kpg.genKeyPair();
            this.prvKey = (ECPrivateKey) kp.getPrivate();
            this.pubKey = (ECPublicKey) kp.getPublic();
            this.params = this.pubKey.getParams();
            this.d = this.prvKey.getS().toByteArray();
            ECPoint w = this.pubKey.getW();
            this.r = w.getAffineX().toByteArray();
            this.s = w.getAffineY().toByteArray();
            byte[] bArr = this.r;
            if (bArr.length == this.s.length && ((key_size == 256 && bArr.length == 32) || ((key_size == 384 && this.r.length == 48) || (key_size == 521 && this.r.length == 66)))) {
                break;
            }
        }
        byte[] bArr2 = this.d;
        if (bArr2.length < this.r.length) {
            this.d = insert0(bArr2);
        }
    }

    @Override // com.jcraft.jsch.KeyPairGenECDSA
    public byte[] getD() {
        return this.d;
    }

    @Override // com.jcraft.jsch.KeyPairGenECDSA
    public byte[] getR() {
        return this.r;
    }

    @Override // com.jcraft.jsch.KeyPairGenECDSA
    public byte[] getS() {
        return this.s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ECPublicKey getPublicKey() {
        return this.pubKey;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ECPrivateKey getPrivateKey() {
        return this.prvKey;
    }

    private byte[] insert0(byte[] buf) {
        byte[] tmp = new byte[buf.length + 1];
        System.arraycopy(buf, 0, tmp, 1, buf.length);
        bzero(buf);
        return tmp;
    }

    private byte[] chop0(byte[] buf) {
        if (buf[0] != 0 || (buf[1] & 128) == 0) {
            return buf;
        }
        byte[] tmp = new byte[buf.length - 1];
        System.arraycopy(buf, 1, tmp, 0, tmp.length);
        bzero(buf);
        return tmp;
    }

    private void bzero(byte[] buf) {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = 0;
        }
    }
}
