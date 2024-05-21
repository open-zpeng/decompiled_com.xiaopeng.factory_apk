package com.jcraft.jsch.jce;

import com.irdeto.securesdk.upgrade.O00000Oo;
import com.jcraft.jsch.Buffer;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
/* loaded from: classes.dex */
public class SignatureRSA implements com.jcraft.jsch.SignatureRSA {
    KeyFactory keyFactory;
    Signature signature;

    @Override // com.jcraft.jsch.Signature
    public void init() throws Exception {
        this.signature = Signature.getInstance(O00000Oo.O00000Oo);
        this.keyFactory = KeyFactory.getInstance(O00000Oo.O000000o);
    }

    @Override // com.jcraft.jsch.SignatureRSA
    public void setPubKey(byte[] e, byte[] n) throws Exception {
        RSAPublicKeySpec rsaPubKeySpec = new RSAPublicKeySpec(new BigInteger(n), new BigInteger(e));
        PublicKey pubKey = this.keyFactory.generatePublic(rsaPubKeySpec);
        this.signature.initVerify(pubKey);
    }

    @Override // com.jcraft.jsch.SignatureRSA
    public void setPrvKey(byte[] d, byte[] n) throws Exception {
        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(new BigInteger(n), new BigInteger(d));
        PrivateKey prvKey = this.keyFactory.generatePrivate(rsaPrivKeySpec);
        this.signature.initSign(prvKey);
    }

    @Override // com.jcraft.jsch.Signature
    public byte[] sign() throws Exception {
        byte[] sig = this.signature.sign();
        return sig;
    }

    @Override // com.jcraft.jsch.Signature
    public void update(byte[] foo) throws Exception {
        this.signature.update(foo);
    }

    @Override // com.jcraft.jsch.Signature
    public boolean verify(byte[] sig) throws Exception {
        Buffer buf = new Buffer(sig);
        if (new String(buf.getString()).equals("ssh-rsa")) {
            int j = buf.getInt();
            int i = buf.getOffSet();
            byte[] tmp = new byte[j];
            System.arraycopy(sig, i, tmp, 0, j);
            sig = tmp;
        }
        return this.signature.verify(sig);
    }
}
