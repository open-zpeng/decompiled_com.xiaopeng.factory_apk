package com.jcraft.jsch.jce;

import com.irdeto.securesdk.upgrade.O00000Oo;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
/* loaded from: classes.dex */
public class KeyPairGenRSA implements com.jcraft.jsch.KeyPairGenRSA {
    byte[] c;
    byte[] d;
    byte[] e;
    byte[] ep;
    byte[] eq;
    byte[] n;
    byte[] p;
    byte[] q;

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public void init(int key_size) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(O00000Oo.O000000o);
        keyGen.initialize(key_size, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey prvKey = pair.getPrivate();
        this.d = ((RSAPrivateKey) prvKey).getPrivateExponent().toByteArray();
        this.e = ((RSAPublicKey) pubKey).getPublicExponent().toByteArray();
        this.n = ((RSAPrivateKey) prvKey).getModulus().toByteArray();
        this.c = ((RSAPrivateCrtKey) prvKey).getCrtCoefficient().toByteArray();
        this.ep = ((RSAPrivateCrtKey) prvKey).getPrimeExponentP().toByteArray();
        this.eq = ((RSAPrivateCrtKey) prvKey).getPrimeExponentQ().toByteArray();
        this.p = ((RSAPrivateCrtKey) prvKey).getPrimeP().toByteArray();
        this.q = ((RSAPrivateCrtKey) prvKey).getPrimeQ().toByteArray();
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getD() {
        return this.d;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getE() {
        return this.e;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getN() {
        return this.n;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getC() {
        return this.c;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getEP() {
        return this.ep;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getEQ() {
        return this.eq;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getP() {
        return this.p;
    }

    @Override // com.jcraft.jsch.KeyPairGenRSA
    public byte[] getQ() {
        return this.q;
    }
}
