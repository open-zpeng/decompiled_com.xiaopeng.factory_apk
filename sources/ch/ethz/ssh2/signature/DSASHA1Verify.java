package ch.ethz.ssh2.signature;

import ch.ethz.ssh2.crypto.digest.SHA1;
import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.TypesReader;
import ch.ethz.ssh2.packets.TypesWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
/* loaded from: classes.dex */
public class DSASHA1Verify {
    static /* synthetic */ Class class$0;
    private static final Logger log;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.signature.DSASHA1Verify");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public static DSAPublicKey decodeSSHDSAPublicKey(byte[] key) throws IOException {
        TypesReader tr = new TypesReader(key);
        String key_format = tr.readString();
        if (!key_format.equals("ssh-dss")) {
            throw new IllegalArgumentException("This is not a ssh-dss public key!");
        }
        BigInteger p = tr.readMPINT();
        BigInteger q = tr.readMPINT();
        BigInteger g = tr.readMPINT();
        BigInteger y = tr.readMPINT();
        if (tr.remain() != 0) {
            throw new IOException("Padding in DSA public key!");
        }
        return new DSAPublicKey(p, q, g, y);
    }

    public static byte[] encodeSSHDSAPublicKey(DSAPublicKey pk) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-dss");
        tw.writeMPInt(pk.getP());
        tw.writeMPInt(pk.getQ());
        tw.writeMPInt(pk.getG());
        tw.writeMPInt(pk.getY());
        return tw.getBytes();
    }

    public static byte[] encodeSSHDSASignature(DSASignature ds) {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-dss");
        byte[] r = ds.getR().toByteArray();
        byte[] s = ds.getS().toByteArray();
        byte[] a40 = new byte[40];
        int r_copylen = r.length < 20 ? r.length : 20;
        int s_copylen = s.length < 20 ? s.length : 20;
        System.arraycopy(r, r.length - r_copylen, a40, 20 - r_copylen, r_copylen);
        System.arraycopy(s, s.length - s_copylen, a40, 40 - s_copylen, s_copylen);
        tw.writeString(a40, 0, 40);
        return tw.getBytes();
    }

    public static DSASignature decodeSSHDSASignature(byte[] sig) throws IOException {
        TypesReader tr = new TypesReader(sig);
        String sig_format = tr.readString();
        if (!sig_format.equals("ssh-dss")) {
            throw new IOException("Peer sent wrong signature format");
        }
        byte[] rsArray = tr.readByteString();
        if (rsArray.length != 40) {
            throw new IOException("Peer sent corrupt signature");
        }
        if (tr.remain() != 0) {
            throw new IOException("Padding in DSA signature!");
        }
        byte[] tmp = new byte[20];
        System.arraycopy(rsArray, 0, tmp, 0, 20);
        BigInteger r = new BigInteger(1, tmp);
        System.arraycopy(rsArray, 20, tmp, 0, 20);
        BigInteger s = new BigInteger(1, tmp);
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("decoded ssh-dss signature: first bytes r(");
            stringBuffer.append(rsArray[0] & 255);
            stringBuffer.append("), s(");
            stringBuffer.append(rsArray[20] & 255);
            stringBuffer.append(")");
            logger.log(30, stringBuffer.toString());
        }
        return new DSASignature(r, s);
    }

    public static boolean verifySignature(byte[] message, DSASignature ds, DSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        BigInteger r = ds.getR();
        BigInteger s = ds.getS();
        BigInteger g = dpk.getG();
        BigInteger p = dpk.getP();
        BigInteger q = dpk.getQ();
        BigInteger y = dpk.getY();
        BigInteger zero = BigInteger.ZERO;
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("ssh-dss signature: m: ");
            stringBuffer.append(m.toString(16));
            logger.log(60, stringBuffer.toString());
            Logger logger2 = log;
            StringBuffer stringBuffer2 = new StringBuffer("ssh-dss signature: r: ");
            stringBuffer2.append(r.toString(16));
            logger2.log(60, stringBuffer2.toString());
            Logger logger3 = log;
            StringBuffer stringBuffer3 = new StringBuffer("ssh-dss signature: s: ");
            stringBuffer3.append(s.toString(16));
            logger3.log(60, stringBuffer3.toString());
            Logger logger4 = log;
            StringBuffer stringBuffer4 = new StringBuffer("ssh-dss signature: g: ");
            stringBuffer4.append(g.toString(16));
            logger4.log(60, stringBuffer4.toString());
            Logger logger5 = log;
            StringBuffer stringBuffer5 = new StringBuffer("ssh-dss signature: p: ");
            stringBuffer5.append(p.toString(16));
            logger5.log(60, stringBuffer5.toString());
            Logger logger6 = log;
            StringBuffer stringBuffer6 = new StringBuffer("ssh-dss signature: q: ");
            stringBuffer6.append(q.toString(16));
            logger6.log(60, stringBuffer6.toString());
            Logger logger7 = log;
            StringBuffer stringBuffer7 = new StringBuffer("ssh-dss signature: y: ");
            stringBuffer7.append(y.toString(16));
            logger7.log(60, stringBuffer7.toString());
        }
        if (zero.compareTo(r) >= 0 || q.compareTo(r) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(r) >= 0 || q.compareTo(r) <= 0");
            return false;
        } else if (zero.compareTo(s) >= 0 || q.compareTo(s) <= 0) {
            log.log(20, "ssh-dss signature: zero.compareTo(s) >= 0 || q.compareTo(s) <= 0");
            return false;
        } else {
            BigInteger w = s.modInverse(q);
            BigInteger u1 = m.multiply(w).mod(q);
            BigInteger u2 = r.multiply(w).mod(q);
            BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
            return v.equals(r);
        }
    }

    public static DSASignature generateSignature(byte[] message, DSAPrivateKey pk, SecureRandom rnd) {
        BigInteger k;
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        int qBitLength = pk.getQ().bitLength();
        do {
            k = new BigInteger(qBitLength, rnd);
        } while (k.compareTo(pk.getQ()) >= 0);
        BigInteger r = pk.getG().modPow(k, pk.getP()).mod(pk.getQ());
        BigInteger s = k.modInverse(pk.getQ()).multiply(m.add(pk.getX().multiply(r))).mod(pk.getQ());
        return new DSASignature(r, s);
    }
}
