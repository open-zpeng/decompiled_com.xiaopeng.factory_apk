package com.jcraft.jsch;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.irdeto.securesdk.upgrade.O00000Oo;
import java.io.PrintStream;
/* loaded from: classes.dex */
public abstract class KeyExchange {
    static final int PROPOSAL_COMP_ALGS_CTOS = 6;
    static final int PROPOSAL_COMP_ALGS_STOC = 7;
    static final int PROPOSAL_ENC_ALGS_CTOS = 2;
    static final int PROPOSAL_ENC_ALGS_STOC = 3;
    static final int PROPOSAL_KEX_ALGS = 0;
    static final int PROPOSAL_LANG_CTOS = 8;
    static final int PROPOSAL_LANG_STOC = 9;
    static final int PROPOSAL_MAC_ALGS_CTOS = 4;
    static final int PROPOSAL_MAC_ALGS_STOC = 5;
    static final int PROPOSAL_MAX = 10;
    static final int PROPOSAL_SERVER_HOST_KEY_ALGS = 1;
    public static final int STATE_END = 0;
    static String kex = "diffie-hellman-group1-sha1";
    static String server_host_key = "ssh-rsa,ssh-dss";
    static String enc_c2s = "blowfish-cbc";
    static String enc_s2c = "blowfish-cbc";
    static String mac_c2s = "hmac-md5";
    static String mac_s2c = "hmac-md5";
    static String lang_c2s = "";
    static String lang_s2c = "";
    protected Session session = null;
    protected HASH sha = null;
    protected byte[] K = null;
    protected byte[] H = null;
    protected byte[] K_S = null;
    protected final int RSA = 0;
    protected final int DSS = 1;
    protected final int ECDSA = 2;
    private int type = 0;
    private String key_alg_name = "";

    public abstract int getState();

    public abstract void init(Session session, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) throws Exception;

    public abstract boolean next(Buffer buffer) throws Exception;

    public String getKeyType() {
        int i = this.type;
        return i == 1 ? "DSA" : i == 0 ? O00000Oo.O000000o : "ECDSA";
    }

    public String getKeyAlgorithName() {
        return this.key_alg_name;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String[] guess(byte[] I_S, byte[] I_C) {
        String[] guess = new String[10];
        Buffer sb = new Buffer(I_S);
        sb.setOffSet(17);
        Buffer cb = new Buffer(I_C);
        cb.setOffSet(17);
        if (JSch.getLogger().isEnabled(1)) {
            for (int i = 0; i < 10; i++) {
                JSch.getLogger().log(1, "kex: server: " + Util.byte2str(sb.getString()));
            }
            for (int i2 = 0; i2 < 10; i2++) {
                JSch.getLogger().log(1, "kex: client: " + Util.byte2str(cb.getString()));
            }
            sb.setOffSet(17);
            cb.setOffSet(17);
        }
        int i3 = 0;
        for (int i4 = 10; i3 < i4; i4 = 10) {
            byte[] sp = sb.getString();
            byte[] cp = cb.getString();
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= cp.length) {
                    break;
                }
                while (j < cp.length && cp[j] != 44) {
                    j++;
                }
                if (k == j) {
                    return null;
                }
                String algorithm = Util.byte2str(cp, k, j - k);
                int l = 0;
                int m = 0;
                while (l < sp.length) {
                    while (l < sp.length && sp[l] != 44) {
                        l++;
                    }
                    if (m == l) {
                        return null;
                    }
                    if (algorithm.equals(Util.byte2str(sp, m, l - m))) {
                        guess[i3] = algorithm;
                        break;
                    }
                    l++;
                    m = l;
                }
                j++;
                k = j;
            }
            if (j == 0) {
                guess[i3] = "";
            } else if (guess[i3] == null) {
                return null;
            }
            i3++;
        }
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "kex: server->client " + guess[3] + " " + guess[5] + " " + guess[7]);
            JSch.getLogger().log(1, "kex: client->server " + guess[2] + " " + guess[4] + " " + guess[6]);
        }
        return guess;
    }

    public String getFingerPrint() {
        HASH hash = null;
        try {
            Class c = Class.forName(this.session.getConfig("md5"));
            hash = (HASH) c.newInstance();
        } catch (Exception e) {
            PrintStream printStream = System.err;
            printStream.println("getFingerPrint: " + e);
        }
        return Util.getFingerPrint(hash, getHostKey());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getK() {
        return this.K;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getH() {
        return this.H;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HASH getHash() {
        return this.sha;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getHostKey() {
        return this.K_S;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[] normalize(byte[] secret) {
        if (secret.length > 1 && secret[0] == 0 && (secret[1] & 128) == 0) {
            byte[] tmp = new byte[secret.length - 1];
            System.arraycopy(secret, 1, tmp, 0, tmp.length);
            return normalize(tmp);
        }
        return secret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean verify(String alg, byte[] K_S, int index, byte[] sig_of_H) throws Exception {
        if (alg.equals("ssh-rsa")) {
            this.type = 0;
            this.key_alg_name = alg;
            int i = index + 1;
            int i2 = i + 1;
            int i3 = ((K_S[index] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((K_S[i] << 16) & 16711680);
            int i4 = i2 + 1;
            int i5 = i3 | ((K_S[i2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            int i6 = i4 + 1;
            int j = i5 | (K_S[i4] & 255);
            byte[] tmp = new byte[j];
            System.arraycopy(K_S, i6, tmp, 0, j);
            int i7 = i6 + j;
            int i8 = i7 + 1;
            int i9 = (-16777216) & (K_S[i7] << 24);
            int i10 = i8 + 1;
            int i11 = (16711680 & (K_S[i8] << 16)) | i9;
            int i12 = i10 + 1;
            int i13 = (65280 & (K_S[i10] << 8)) | i11;
            int i14 = i12 + 1;
            int j2 = i13 | (K_S[i12] & 255);
            byte[] tmp2 = new byte[j2];
            System.arraycopy(K_S, i14, tmp2, 0, j2);
            int i15 = i14 + j2;
            SignatureRSA sig = null;
            try {
                Class c = Class.forName(this.session.getConfig("signature.rsa"));
                sig = (SignatureRSA) c.newInstance();
                sig.init();
            } catch (Exception e) {
                System.err.println(e);
            }
            sig.setPubKey(tmp, tmp2);
            sig.update(this.H);
            boolean result = sig.verify(sig_of_H);
            if (JSch.getLogger().isEnabled(1)) {
                Logger logger = JSch.getLogger();
                logger.log(1, "ssh_rsa_verify: signature " + result);
                return result;
            }
            return result;
        } else if (alg.equals("ssh-dss")) {
            this.type = 1;
            this.key_alg_name = alg;
            int i16 = index + 1;
            int i17 = i16 + 1;
            int i18 = ((K_S[index] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((K_S[i16] << 16) & 16711680);
            int i19 = i17 + 1;
            int i20 = i18 | ((K_S[i17] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            int i21 = i19 + 1;
            int j3 = i20 | (K_S[i19] & 255);
            byte[] tmp3 = new byte[j3];
            System.arraycopy(K_S, i21, tmp3, 0, j3);
            int i22 = i21 + j3;
            int i23 = i22 + 1;
            int i24 = i23 + 1;
            int i25 = ((K_S[i22] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((K_S[i23] << 16) & 16711680);
            int i26 = i24 + 1;
            int i27 = i26 + 1;
            int j4 = i25 | ((K_S[i24] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (K_S[i26] & 255);
            byte[] tmp4 = new byte[j4];
            System.arraycopy(K_S, i27, tmp4, 0, j4);
            int i28 = i27 + j4;
            int i29 = i28 + 1;
            int i30 = i29 + 1;
            int i31 = ((K_S[i28] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((K_S[i29] << 16) & 16711680);
            int i32 = i30 + 1;
            int i33 = i31 | ((K_S[i30] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            int i34 = i32 + 1;
            int j5 = i33 | (K_S[i32] & 255);
            byte[] tmp5 = new byte[j5];
            System.arraycopy(K_S, i34, tmp5, 0, j5);
            int i35 = i34 + j5;
            int i36 = i35 + 1;
            int i37 = (-16777216) & (K_S[i35] << 24);
            int i38 = i36 + 1;
            int i39 = (16711680 & (K_S[i36] << 16)) | i37;
            int i40 = i38 + 1;
            int i41 = (65280 & (K_S[i38] << 8)) | i39;
            int i42 = i40 + 1;
            int j6 = i41 | (K_S[i40] & 255);
            byte[] tmp6 = new byte[j6];
            System.arraycopy(K_S, i42, tmp6, 0, j6);
            int i43 = i42 + j6;
            SignatureDSA sig2 = null;
            try {
                Class c2 = Class.forName(this.session.getConfig("signature.dss"));
                sig2 = (SignatureDSA) c2.newInstance();
                sig2.init();
            } catch (Exception e2) {
                System.err.println(e2);
            }
            sig2.setPubKey(tmp6, tmp3, tmp4, tmp5);
            sig2.update(this.H);
            boolean result2 = sig2.verify(sig_of_H);
            if (JSch.getLogger().isEnabled(1)) {
                Logger logger2 = JSch.getLogger();
                logger2.log(1, "ssh_dss_verify: signature " + result2);
                return result2;
            }
            return result2;
        } else if (alg.equals("ecdsa-sha2-nistp256") || alg.equals("ecdsa-sha2-nistp384") || alg.equals("ecdsa-sha2-nistp521")) {
            this.type = 2;
            this.key_alg_name = alg;
            int i44 = index + 1;
            int i45 = i44 + 1;
            int i46 = ((K_S[index] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((K_S[i44] << 16) & 16711680);
            int i47 = i45 + 1;
            int i48 = i46 | ((K_S[i45] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
            int i49 = i47 + 1;
            int j7 = i48 | (K_S[i47] & 255);
            System.arraycopy(K_S, i49, new byte[j7], 0, j7);
            int i50 = i49 + j7;
            int i51 = i50 + 1;
            int i52 = (-16777216) & (K_S[i50] << 24);
            int i53 = i51 + 1;
            int i54 = (16711680 & (K_S[i51] << 16)) | i52;
            int i55 = i53 + 1;
            int i56 = (65280 & (K_S[i53] << 8)) | i54;
            int j8 = i56 | (K_S[i55] & 255);
            int i57 = i55 + 1 + 1;
            byte[] tmp7 = new byte[(j8 - 1) / 2];
            System.arraycopy(K_S, i57, tmp7, 0, tmp7.length);
            int i58 = i57 + ((j8 - 1) / 2);
            byte[] tmp8 = new byte[(j8 - 1) / 2];
            System.arraycopy(K_S, i58, tmp8, 0, tmp8.length);
            int i59 = i58 + ((j8 - 1) / 2);
            SignatureECDSA sig3 = null;
            try {
                Class c3 = Class.forName(this.session.getConfig(alg));
                sig3 = (SignatureECDSA) c3.newInstance();
                sig3.init();
            } catch (Exception e3) {
                System.err.println(e3);
            }
            sig3.setPubKey(tmp7, tmp8);
            sig3.update(this.H);
            return sig3.verify(sig_of_H);
        } else {
            System.err.println("unknown alg");
            return false;
        }
    }
}
