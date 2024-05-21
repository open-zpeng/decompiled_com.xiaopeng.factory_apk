package com.jcraft.jsch.jce;

import com.jcraft.jsch.JSchException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
/* loaded from: classes.dex */
public class DH implements com.jcraft.jsch.DH {
    BigInteger K;
    byte[] K_array;
    BigInteger e;
    byte[] e_array;
    BigInteger f;
    BigInteger g;
    private KeyAgreement myKeyAgree;
    private KeyPairGenerator myKpairGen;
    BigInteger p;

    @Override // com.jcraft.jsch.DH
    public void init() throws Exception {
        this.myKpairGen = KeyPairGenerator.getInstance("DH");
        this.myKeyAgree = KeyAgreement.getInstance("DH");
    }

    @Override // com.jcraft.jsch.DH
    public byte[] getE() throws Exception {
        if (this.e == null) {
            DHParameterSpec dhSkipParamSpec = new DHParameterSpec(this.p, this.g);
            this.myKpairGen.initialize(dhSkipParamSpec);
            KeyPair myKpair = this.myKpairGen.generateKeyPair();
            this.myKeyAgree.init(myKpair.getPrivate());
            this.e = ((DHPublicKey) myKpair.getPublic()).getY();
            this.e_array = this.e.toByteArray();
        }
        return this.e_array;
    }

    @Override // com.jcraft.jsch.DH
    public byte[] getK() throws Exception {
        if (this.K == null) {
            KeyFactory myKeyFac = KeyFactory.getInstance("DH");
            DHPublicKeySpec keySpec = new DHPublicKeySpec(this.f, this.p, this.g);
            PublicKey yourPubKey = myKeyFac.generatePublic(keySpec);
            this.myKeyAgree.doPhase(yourPubKey, true);
            byte[] mySharedSecret = this.myKeyAgree.generateSecret();
            this.K = new BigInteger(1, mySharedSecret);
            this.K_array = this.K.toByteArray();
            this.K_array = mySharedSecret;
        }
        return this.K_array;
    }

    @Override // com.jcraft.jsch.DH
    public void setP(byte[] p) {
        setP(new BigInteger(1, p));
    }

    @Override // com.jcraft.jsch.DH
    public void setG(byte[] g) {
        setG(new BigInteger(1, g));
    }

    @Override // com.jcraft.jsch.DH
    public void setF(byte[] f) {
        setF(new BigInteger(1, f));
    }

    void setP(BigInteger p) {
        this.p = p;
    }

    void setG(BigInteger g) {
        this.g = g;
    }

    void setF(BigInteger f) {
        this.f = f;
    }

    @Override // com.jcraft.jsch.DH
    public void checkRange() throws Exception {
    }

    private void checkRange(BigInteger tmp) throws Exception {
        BigInteger one = BigInteger.ONE;
        BigInteger p_1 = this.p.subtract(one);
        if (one.compareTo(tmp) >= 0 || tmp.compareTo(p_1) >= 0) {
            throw new JSchException("invalid DH value");
        }
    }
}
