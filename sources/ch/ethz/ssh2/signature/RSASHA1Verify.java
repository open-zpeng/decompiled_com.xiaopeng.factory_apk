package ch.ethz.ssh2.signature;

import ch.ethz.ssh2.crypto.SimpleDERReader;
import ch.ethz.ssh2.crypto.digest.SHA1;
import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.TypesReader;
import ch.ethz.ssh2.packets.TypesWriter;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.factory.presenter.security.TestSecurityPresenter;
import java.io.IOException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class RSASHA1Verify {
    static /* synthetic */ Class class$0;
    private static final Logger log;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.signature.RSASHA1Verify");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public static RSAPublicKey decodeSSHRSAPublicKey(byte[] key) throws IOException {
        TypesReader tr = new TypesReader(key);
        String key_format = tr.readString();
        if (!key_format.equals("ssh-rsa")) {
            throw new IllegalArgumentException("This is not a ssh-rsa public key");
        }
        BigInteger e = tr.readMPINT();
        BigInteger n = tr.readMPINT();
        if (tr.remain() != 0) {
            throw new IOException("Padding in RSA public key!");
        }
        return new RSAPublicKey(e, n);
    }

    public static byte[] encodeSSHRSAPublicKey(RSAPublicKey pk) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-rsa");
        tw.writeMPInt(pk.getE());
        tw.writeMPInt(pk.getN());
        return tw.getBytes();
    }

    public static RSASignature decodeSSHRSASignature(byte[] sig) throws IOException {
        TypesReader tr = new TypesReader(sig);
        String sig_format = tr.readString();
        if (!sig_format.equals("ssh-rsa")) {
            throw new IOException("Peer sent wrong signature format");
        }
        byte[] s = tr.readByteString();
        if (s.length == 0) {
            throw new IOException("Error in RSA signature, S is empty.");
        }
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("Decoding ssh-rsa signature string (length: ");
            stringBuffer.append(s.length);
            stringBuffer.append(")");
            logger.log(80, stringBuffer.toString());
        }
        if (tr.remain() != 0) {
            throw new IOException("Padding in RSA signature!");
        }
        return new RSASignature(new BigInteger(1, s));
    }

    public static byte[] encodeSSHRSASignature(RSASignature sig) throws IOException {
        TypesWriter tw = new TypesWriter();
        tw.writeString("ssh-rsa");
        byte[] s = sig.getS().toByteArray();
        if (s.length > 1 && s[0] == 0) {
            tw.writeString(s, 1, s.length - 1);
        } else {
            tw.writeString(s, 0, s.length);
        }
        return tw.getBytes();
    }

    public static RSASignature generateSignature(byte[] message, RSAPrivateKey pk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        byte[] der_header = {48, WlanPresenter.WL_START_RF, 48, 9, 6, 5, 43, 14, 3, 2, TestSecurityPresenter.BRUSH_EFUSE, 5, 0, 4, 20};
        int rsa_block_len = (pk.getN().bitLength() + 7) / 8;
        int num_pad = (rsa_block_len - ((der_header.length + 2) + sha_message.length)) - 1;
        if (num_pad < 8) {
            throw new IOException("Cannot sign with RSA, message too long");
        }
        byte[] sig = new byte[der_header.length + sha_message.length + 2 + num_pad];
        sig[0] = 1;
        for (int i = 0; i < num_pad; i++) {
            sig[i + 1] = -1;
        }
        int i2 = num_pad + 1;
        sig[i2] = 0;
        System.arraycopy(der_header, 0, sig, num_pad + 2, der_header.length);
        System.arraycopy(sha_message, 0, sig, num_pad + 2 + der_header.length, sha_message.length);
        BigInteger m = new BigInteger(1, sig);
        BigInteger s = m.modPow(pk.getD(), pk.getN());
        return new RSASignature(s);
    }

    public static boolean verifySignature(byte[] message, RSASignature ds, RSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger n = dpk.getN();
        BigInteger e = dpk.getE();
        BigInteger s = ds.getS();
        int i = 20;
        boolean z = false;
        if (n.compareTo(s) <= 0) {
            log.log(20, "ssh-rsa signature: n.compareTo(s) <= 0");
            return false;
        }
        int rsa_block_len = (n.bitLength() + 7) / 8;
        int i2 = 1;
        if (rsa_block_len < 1) {
            log.log(20, "ssh-rsa signature: rsa_block_len < 1");
            return false;
        }
        byte[] v = s.modPow(e, n).toByteArray();
        int startpos = 0;
        if (v.length > 0 && v[0] == 0) {
            startpos = 0 + 1;
        }
        if (v.length - startpos == rsa_block_len - 1) {
            if (v[startpos] != 1) {
                log.log(20, "ssh-rsa signature: v[startpos] != 0x01");
                return false;
            }
            int pos = startpos + 1;
            while (pos < v.length) {
                if (v[pos] != 0) {
                    SHA1 md2 = md;
                    BigInteger n2 = n;
                    int i3 = i;
                    boolean z2 = z;
                    if (v[pos] != -1) {
                        log.log(i3, "ssh-rsa signature: v[pos] != (byte) 0xff");
                        return z2;
                    }
                    pos++;
                    z = z2;
                    i = i3;
                    md = md2;
                    n = n2;
                    i2 = 1;
                } else {
                    int num_pad = pos - (startpos + 1);
                    if (num_pad < 8) {
                        log.log(i, "ssh-rsa signature: num_pad < 8");
                        return z;
                    }
                    int pos2 = pos + i2;
                    if (pos2 >= v.length) {
                        log.log(i, "ssh-rsa signature: pos >= v.length");
                        return z;
                    }
                    SimpleDERReader dr = new SimpleDERReader(v, pos2, v.length - pos2);
                    byte[] seq = dr.readSequenceAsByteArray();
                    if (dr.available() != 0) {
                        log.log(i, "ssh-rsa signature: dr.available() != 0");
                        return false;
                    }
                    dr.resetInput(seq);
                    byte[] digestAlgorithm = dr.readSequenceAsByteArray();
                    if (digestAlgorithm.length >= 8 && digestAlgorithm.length <= 9) {
                        byte[] digestAlgorithm_sha1 = {6, 5, 43, 14, 3, 2, TestSecurityPresenter.BRUSH_EFUSE, 5};
                        int i4 = 0;
                        while (true) {
                            SHA1 md3 = md;
                            if (i4 < digestAlgorithm.length) {
                                BigInteger n3 = n;
                                if (digestAlgorithm[i4] == digestAlgorithm_sha1[i4]) {
                                    i4++;
                                    md = md3;
                                    n = n3;
                                } else {
                                    log.log(20, "ssh-rsa signature: digestAlgorithm[i] != digestAlgorithm_sha1[i]");
                                    return false;
                                }
                            } else {
                                byte[] digest = dr.readOctetString();
                                if (dr.available() != 0) {
                                    log.log(20, "ssh-rsa signature: dr.available() != 0 (II)");
                                    return false;
                                } else if (digest.length != sha_message.length) {
                                    log.log(20, "ssh-rsa signature: digest.length != sha_message.length");
                                    return false;
                                } else {
                                    for (int i5 = 0; i5 < sha_message.length; i5++) {
                                        if (sha_message[i5] != digest[i5]) {
                                            log.log(20, "ssh-rsa signature: sha_message[i] != digest[i]");
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                    log.log(20, "ssh-rsa signature: (digestAlgorithm.length < 8) || (digestAlgorithm.length > 9)");
                    return false;
                }
            }
            log.log(i, "ssh-rsa signature: pos >= v.length");
            return z;
        }
        log.log(20, "ssh-rsa signature: (v.length - startpos) != (rsa_block_len - 1)");
        return false;
    }
}
