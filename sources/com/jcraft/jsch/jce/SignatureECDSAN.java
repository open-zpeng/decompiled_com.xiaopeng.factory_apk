package com.jcraft.jsch.jce;

import com.jcraft.jsch.Buffer;
import com.jcraft.jsch.SignatureECDSA;
import com.xpeng.upso.aesserver.AesConstants;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
/* loaded from: classes.dex */
public abstract class SignatureECDSAN implements SignatureECDSA {
    KeyFactory keyFactory;
    Signature signature;

    abstract String getName();

    @Override // com.jcraft.jsch.Signature
    public void init() throws Exception {
        String name = getName();
        String foo = "SHA256withECDSA";
        if (name.equals("ecdsa-sha2-nistp384")) {
            foo = "SHA384withECDSA";
        } else if (name.equals("ecdsa-sha2-nistp521")) {
            foo = "SHA512withECDSA";
        }
        this.signature = Signature.getInstance(foo);
        this.keyFactory = KeyFactory.getInstance(AesConstants.SERVER_MAGIC_CDU);
    }

    @Override // com.jcraft.jsch.SignatureECDSA
    public void setPubKey(byte[] r, byte[] s) throws Exception {
        byte[] r2 = insert0(r);
        byte[] s2 = insert0(s);
        String name = "secp256r1";
        if (r2.length >= 64) {
            name = "secp521r1";
        } else if (r2.length >= 48) {
            name = "secp384r1";
        }
        AlgorithmParameters param = AlgorithmParameters.getInstance(AesConstants.SERVER_MAGIC_CDU);
        param.init(new ECGenParameterSpec(name));
        ECParameterSpec ecparam = (ECParameterSpec) param.getParameterSpec(ECParameterSpec.class);
        ECPoint w = new ECPoint(new BigInteger(1, r2), new BigInteger(1, s2));
        PublicKey pubKey = this.keyFactory.generatePublic(new ECPublicKeySpec(w, ecparam));
        this.signature.initVerify(pubKey);
    }

    @Override // com.jcraft.jsch.SignatureECDSA
    public void setPrvKey(byte[] d) throws Exception {
        byte[] d2 = insert0(d);
        String name = "secp256r1";
        if (d2.length >= 64) {
            name = "secp521r1";
        } else if (d2.length >= 48) {
            name = "secp384r1";
        }
        AlgorithmParameters param = AlgorithmParameters.getInstance(AesConstants.SERVER_MAGIC_CDU);
        param.init(new ECGenParameterSpec(name));
        ECParameterSpec ecparam = (ECParameterSpec) param.getParameterSpec(ECParameterSpec.class);
        BigInteger _d = new BigInteger(1, d2);
        PrivateKey prvKey = this.keyFactory.generatePrivate(new ECPrivateKeySpec(_d, ecparam));
        this.signature.initSign(prvKey);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.jcraft.jsch.Signature
    public byte[] sign() throws Exception {
        byte[] sig = this.signature.sign();
        if (sig[0] == 48) {
            if (sig[1] + 2 == sig.length || ((sig[1] & 128) != 0 && (sig[2] & 255) + 3 == sig.length)) {
                int index = 3;
                if ((sig[1] & 128) != 0 && (sig[2] & 255) + 3 == sig.length) {
                    index = 4;
                }
                byte[] r = new byte[sig[index]];
                byte[] s = new byte[sig[index + 2 + sig[index]]];
                System.arraycopy(sig, index + 1, r, 0, r.length);
                System.arraycopy(sig, index + 3 + sig[index], s, 0, s.length);
                byte[] r2 = chop0(r);
                byte[] s2 = chop0(s);
                Buffer buf = new Buffer();
                buf.putMPInt(r2);
                buf.putMPInt(s2);
                byte[] sig2 = new byte[buf.getLength()];
                buf.setOffSet(0);
                buf.getByte(sig2);
                return sig2;
            }
            return sig;
        }
        return sig;
    }

    @Override // com.jcraft.jsch.Signature
    public void update(byte[] foo) throws Exception {
        this.signature.update(foo);
    }

    @Override // com.jcraft.jsch.Signature
    public boolean verify(byte[] sig) throws Exception {
        byte[] asn1;
        if (sig[0] != 48 || (sig[1] + 2 != sig.length && ((sig[1] & 128) == 0 || (sig[2] & 255) + 3 != sig.length))) {
            Buffer b = new Buffer(sig);
            b.getString();
            b.getInt();
            byte[] r = b.getMPInt();
            byte[] s = b.getMPInt();
            byte[] r2 = insert0(r);
            byte[] s2 = insert0(s);
            if (r2.length < 64) {
                asn1 = new byte[r2.length + 6 + s2.length];
                asn1[0] = 48;
                asn1[1] = (byte) (r2.length + 4 + s2.length);
                asn1[2] = 2;
                asn1[3] = (byte) r2.length;
                System.arraycopy(r2, 0, asn1, 4, r2.length);
                asn1[r2.length + 4] = 2;
                asn1[r2.length + 5] = (byte) s2.length;
                System.arraycopy(s2, 0, asn1, r2.length + 6, s2.length);
            } else {
                asn1 = new byte[r2.length + 6 + s2.length + 1];
                asn1[0] = 48;
                asn1[1] = -127;
                asn1[2] = (byte) (r2.length + 4 + s2.length);
                asn1[3] = 2;
                asn1[4] = (byte) r2.length;
                System.arraycopy(r2, 0, asn1, 5, r2.length);
                asn1[r2.length + 5] = 2;
                asn1[r2.length + 6] = (byte) s2.length;
                System.arraycopy(s2, 0, asn1, r2.length + 7, s2.length);
            }
            sig = asn1;
        }
        return this.signature.verify(sig);
    }

    private byte[] insert0(byte[] buf) {
        if ((buf[0] & 128) == 0) {
            return buf;
        }
        byte[] tmp = new byte[buf.length + 1];
        System.arraycopy(buf, 0, tmp, 1, buf.length);
        bzero(buf);
        return tmp;
    }

    private byte[] chop0(byte[] buf) {
        if (buf[0] != 0) {
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
