package ch.ethz.ssh2.crypto;

import ch.ethz.ssh2.crypto.digest.HashForSSH2Types;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import java.lang.reflect.Array;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class KeyMaterial {
    public byte[] enc_key_client_to_server;
    public byte[] enc_key_server_to_client;
    public byte[] initial_iv_client_to_server;
    public byte[] initial_iv_server_to_client;
    public byte[] integrity_key_client_to_server;
    public byte[] integrity_key_server_to_client;

    private static byte[] calculateKey(HashForSSH2Types sh, BigInteger K, byte[] H, byte type, byte[] SessionID, int keyLength) {
        byte[] res = new byte[keyLength];
        int dglen = sh.getDigestLength();
        int numRounds = ((keyLength + dglen) - 1) / dglen;
        byte[][] tmp = (byte[][]) Array.newInstance(byte[].class, numRounds);
        sh.reset();
        sh.updateBigInt(K);
        sh.updateBytes(H);
        sh.updateByte(type);
        sh.updateBytes(SessionID);
        tmp[0] = sh.getDigest();
        int produced = Math.min(dglen, keyLength);
        System.arraycopy(tmp[0], 0, res, 0, produced);
        int keyLength2 = keyLength - produced;
        int off = 0 + produced;
        for (int i = 1; i < numRounds; i++) {
            sh.updateBigInt(K);
            sh.updateBytes(H);
            for (int j = 0; j < i; j++) {
                sh.updateBytes(tmp[j]);
            }
            tmp[i] = sh.getDigest();
            int produced2 = Math.min(dglen, keyLength2);
            System.arraycopy(tmp[i], 0, res, off, produced2);
            keyLength2 -= produced2;
            off += produced2;
        }
        return res;
    }

    public static KeyMaterial create(String hashType, byte[] H, BigInteger K, byte[] SessionID, int keyLengthCS, int blockSizeCS, int macLengthCS, int keyLengthSC, int blockSizeSC, int macLengthSC) throws IllegalArgumentException {
        KeyMaterial km = new KeyMaterial();
        HashForSSH2Types sh = new HashForSSH2Types(hashType);
        km.initial_iv_client_to_server = calculateKey(sh, K, H, (byte) 65, SessionID, blockSizeCS);
        km.initial_iv_server_to_client = calculateKey(sh, K, H, (byte) 66, SessionID, blockSizeSC);
        km.enc_key_client_to_server = calculateKey(sh, K, H, (byte) 67, SessionID, keyLengthCS);
        km.enc_key_server_to_client = calculateKey(sh, K, H, (byte) 68, SessionID, keyLengthSC);
        km.integrity_key_client_to_server = calculateKey(sh, K, H, TestResultItem.RESULT_ENTER, SessionID, macLengthCS);
        km.integrity_key_server_to_client = calculateKey(sh, K, H, TestResultItem.RESULT_FAIL, SessionID, macLengthSC);
        return km;
    }
}
