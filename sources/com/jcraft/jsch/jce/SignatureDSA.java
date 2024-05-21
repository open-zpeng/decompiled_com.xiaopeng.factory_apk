package com.jcraft.jsch.jce;

import com.jcraft.jsch.Buffer;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
/* loaded from: classes.dex */
public class SignatureDSA implements com.jcraft.jsch.SignatureDSA {
    KeyFactory keyFactory;
    Signature signature;

    @Override // com.jcraft.jsch.Signature
    public void init() throws Exception {
        this.signature = Signature.getInstance("SHA1withDSA");
        this.keyFactory = KeyFactory.getInstance("DSA");
    }

    @Override // com.jcraft.jsch.SignatureDSA
    public void setPubKey(byte[] y, byte[] p, byte[] q, byte[] g) throws Exception {
        DSAPublicKeySpec dsaPubKeySpec = new DSAPublicKeySpec(new BigInteger(y), new BigInteger(p), new BigInteger(q), new BigInteger(g));
        PublicKey pubKey = this.keyFactory.generatePublic(dsaPubKeySpec);
        this.signature.initVerify(pubKey);
    }

    @Override // com.jcraft.jsch.SignatureDSA
    public void setPrvKey(byte[] x, byte[] p, byte[] q, byte[] g) throws Exception {
        DSAPrivateKeySpec dsaPrivKeySpec = new DSAPrivateKeySpec(new BigInteger(x), new BigInteger(p), new BigInteger(q), new BigInteger(g));
        PrivateKey prvKey = this.keyFactory.generatePrivate(dsaPrivKeySpec);
        this.signature.initSign(prvKey);
    }

    @Override // com.jcraft.jsch.Signature
    public byte[] sign() throws Exception {
        byte[] sig = this.signature.sign();
        int index = 3 + 1;
        int len = sig[3] & 255;
        byte[] r = new byte[len];
        System.arraycopy(sig, index, r, 0, r.length);
        int index2 = index + len + 1;
        int index3 = index2 + 1;
        byte[] s = new byte[sig[index2] & 255];
        System.arraycopy(sig, index3, s, 0, s.length);
        byte[] result = new byte[40];
        System.arraycopy(r, r.length > 20 ? 1 : 0, result, r.length > 20 ? 0 : 20 - r.length, r.length > 20 ? 20 : r.length);
        System.arraycopy(s, s.length > 20 ? 1 : 0, result, s.length > 20 ? 20 : 40 - s.length, s.length <= 20 ? s.length : 20);
        return result;
    }

    @Override // com.jcraft.jsch.Signature
    public void update(byte[] foo) throws Exception {
        this.signature.update(foo);
    }

    @Override // com.jcraft.jsch.Signature
    public boolean verify(byte[] sig) throws Exception {
        Buffer buf = new Buffer(sig);
        if (new String(buf.getString()).equals("ssh-dss")) {
            int j = buf.getInt();
            int i = buf.getOffSet();
            byte[] tmp = new byte[j];
            System.arraycopy(sig, i, tmp, 0, j);
            sig = tmp;
        }
        byte[] _frst = new byte[20];
        System.arraycopy(sig, 0, _frst, 0, 20);
        byte[] _frst2 = normalize(_frst);
        byte[] _scnd = new byte[20];
        System.arraycopy(sig, 20, _scnd, 0, 20);
        byte[] _scnd2 = normalize(_scnd);
        int frst = (_frst2[0] & 128) != 0 ? 1 : 0;
        int scnd = (_scnd2[0] & 128) != 0 ? 1 : 0;
        int length = _frst2.length + _scnd2.length + 6 + frst + scnd;
        byte[] tmp2 = new byte[length];
        tmp2[0] = 48;
        tmp2[1] = (byte) (_frst2.length + _scnd2.length + 4);
        tmp2[1] = (byte) (tmp2[1] + frst);
        tmp2[1] = (byte) (tmp2[1] + scnd);
        tmp2[2] = 2;
        tmp2[3] = (byte) _frst2.length;
        tmp2[3] = (byte) (tmp2[3] + frst);
        System.arraycopy(_frst2, 0, tmp2, frst + 4, _frst2.length);
        tmp2[tmp2[3] + 4] = 2;
        tmp2[tmp2[3] + 5] = (byte) _scnd2.length;
        int i2 = tmp2[3] + 5;
        tmp2[i2] = (byte) (tmp2[i2] + scnd);
        System.arraycopy(_scnd2, 0, tmp2, tmp2[3] + 6 + scnd, _scnd2.length);
        return this.signature.verify(tmp2);
    }

    protected byte[] normalize(byte[] secret) {
        if (secret.length > 1 && secret[0] == 0 && (secret[1] & 128) == 0) {
            byte[] tmp = new byte[secret.length - 1];
            System.arraycopy(secret, 1, tmp, 0, tmp.length);
            return normalize(tmp);
        }
        return secret;
    }
}
