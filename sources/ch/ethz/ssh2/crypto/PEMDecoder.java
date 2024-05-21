package ch.ethz.ssh2.crypto;

import ch.ethz.ssh2.crypto.cipher.AES;
import ch.ethz.ssh2.crypto.cipher.BlockCipher;
import ch.ethz.ssh2.crypto.cipher.CBCMode;
import ch.ethz.ssh2.crypto.cipher.DES;
import ch.ethz.ssh2.crypto.cipher.DESede;
import ch.ethz.ssh2.crypto.digest.MD5;
import ch.ethz.ssh2.signature.DSAPrivateKey;
import ch.ethz.ssh2.signature.RSAPrivateKey;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class PEMDecoder {
    private static final int PEM_DSA_PRIVATE_KEY = 2;
    private static final int PEM_RSA_PRIVATE_KEY = 1;

    private static final int hexToInt(char c) {
        if (c >= 'a' && c <= 'f') {
            return (c - 'a') + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 'A') + 10;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        throw new IllegalArgumentException("Need hex char");
    }

    private static byte[] hexToByteArray(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Uneven string length in hex encoding.");
        }
        byte[] decoded = new byte[hex.length() / 2];
        for (int i = 0; i < decoded.length; i++) {
            int hi = hexToInt(hex.charAt(i * 2));
            int lo = hexToInt(hex.charAt((i * 2) + 1));
            decoded[i] = (byte) ((hi * 16) + lo);
        }
        return decoded;
    }

    private static byte[] generateKeyFromPasswordSaltWithMD5(byte[] password, byte[] salt, int keyLen) throws IOException {
        if (salt.length < 8) {
            throw new IllegalArgumentException("Salt needs to be at least 8 bytes for key generation.");
        }
        MD5 md5 = new MD5();
        byte[] key = new byte[keyLen];
        byte[] tmp = new byte[md5.getDigestLength()];
        while (true) {
            md5.update(password, 0, password.length);
            md5.update(salt, 0, 8);
            int copy = keyLen < tmp.length ? keyLen : tmp.length;
            md5.digest(tmp, 0);
            System.arraycopy(tmp, 0, key, key.length - keyLen, copy);
            keyLen -= copy;
            if (keyLen == 0) {
                return key;
            }
            md5.update(tmp, 0, tmp.length);
        }
    }

    private static byte[] removePadding(byte[] buff, int blockSize) throws IOException {
        int rfc_1423_padding = buff[buff.length - 1] & 255;
        if (rfc_1423_padding < 1 || rfc_1423_padding > blockSize) {
            throw new IOException("Decrypted PEM has wrong padding, did you specify the correct password?");
        }
        for (int i = 2; i <= rfc_1423_padding; i++) {
            if (buff[buff.length - i] != rfc_1423_padding) {
                throw new IOException("Decrypted PEM has wrong padding, did you specify the correct password?");
            }
        }
        int i2 = buff.length;
        byte[] tmp = new byte[i2 - rfc_1423_padding];
        System.arraycopy(buff, 0, tmp, 0, buff.length - rfc_1423_padding);
        return tmp;
    }

    private static final PEMStructure parsePEM(char[] pem) throws IOException {
        String endLine;
        PEMStructure ps = new PEMStructure();
        BufferedReader br = new BufferedReader(new CharArrayReader(pem));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                throw new IOException("Invalid PEM structure, '-----BEGIN...' missing");
            }
            String line2 = line.trim();
            if (line2.startsWith("-----BEGIN DSA PRIVATE KEY-----")) {
                endLine = "-----END DSA PRIVATE KEY-----";
                ps.pemType = 2;
                break;
            } else if (line2.startsWith("-----BEGIN RSA PRIVATE KEY-----")) {
                endLine = "-----END RSA PRIVATE KEY-----";
                ps.pemType = 1;
                break;
            }
        }
        while (true) {
            String line3 = br.readLine();
            if (line3 == null) {
                StringBuffer stringBuffer = new StringBuffer("Invalid PEM structure, ");
                stringBuffer.append(endLine);
                stringBuffer.append(" missing");
                throw new IOException(stringBuffer.toString());
            }
            String line4 = line3.trim();
            int sem_idx = line4.indexOf(58);
            if (sem_idx != -1) {
                String name = line4.substring(0, sem_idx + 1);
                String value = line4.substring(sem_idx + 1);
                String[] values = value.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                if ("Proc-Type:".equals(name)) {
                    ps.procType = values;
                } else if ("DEK-Info:".equals(name)) {
                    ps.dekInfo = values;
                }
            } else {
                StringBuffer keyData = new StringBuffer();
                while (line4 != null) {
                    String line5 = line4.trim();
                    if (!line5.startsWith(endLine)) {
                        keyData.append(line5);
                        line4 = br.readLine();
                    } else {
                        char[] pem_chars = new char[keyData.length()];
                        keyData.getChars(0, pem_chars.length, pem_chars, 0);
                        ps.data = Base64.decode(pem_chars);
                        if (ps.data.length == 0) {
                            throw new IOException("Invalid PEM structure, no data available");
                        }
                        return ps;
                    }
                }
                StringBuffer stringBuffer2 = new StringBuffer("Invalid PEM structure, ");
                stringBuffer2.append(endLine);
                stringBuffer2.append(" missing");
                throw new IOException(stringBuffer2.toString());
            }
        }
    }

    private static final void decryptPEM(PEMStructure ps, byte[] pw) throws IOException {
        BlockCipher bc;
        if (ps.dekInfo == null) {
            throw new IOException("Broken PEM, no mode and salt given, but encryption enabled");
        }
        if (ps.dekInfo.length != 2) {
            throw new IOException("Broken PEM, DEK-Info is incomplete!");
        }
        String algo = ps.dekInfo[0];
        byte[] salt = hexToByteArray(ps.dekInfo[1]);
        if (algo.equals("DES-EDE3-CBC")) {
            DESede des3 = new DESede();
            des3.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 24));
            bc = new CBCMode(des3, salt, false);
        } else if (algo.equals("DES-CBC")) {
            DES des = new DES();
            des.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 8));
            bc = new CBCMode(des, salt, false);
        } else if (algo.equals("AES-128-CBC")) {
            AES aes = new AES();
            aes.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 16));
            bc = new CBCMode(aes, salt, false);
        } else if (algo.equals("AES-192-CBC")) {
            AES aes2 = new AES();
            aes2.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 24));
            bc = new CBCMode(aes2, salt, false);
        } else if (algo.equals("AES-256-CBC")) {
            AES aes3 = new AES();
            aes3.init(false, generateKeyFromPasswordSaltWithMD5(pw, salt, 32));
            bc = new CBCMode(aes3, salt, false);
        } else {
            StringBuffer stringBuffer = new StringBuffer("Cannot decrypt PEM structure, unknown cipher ");
            stringBuffer.append(algo);
            throw new IOException(stringBuffer.toString());
        }
        if (ps.data.length % bc.getBlockSize() != 0) {
            StringBuffer stringBuffer2 = new StringBuffer("Invalid PEM structure, size of encrypted block is not a multiple of ");
            stringBuffer2.append(bc.getBlockSize());
            throw new IOException(stringBuffer2.toString());
        }
        byte[] dz = new byte[ps.data.length];
        for (int i = 0; i < ps.data.length / bc.getBlockSize(); i++) {
            bc.transformBlock(ps.data, bc.getBlockSize() * i, dz, bc.getBlockSize() * i);
        }
        int i2 = bc.getBlockSize();
        ps.data = removePadding(dz, i2);
        ps.dekInfo = null;
        ps.procType = null;
    }

    public static final boolean isPEMEncrypted(PEMStructure ps) throws IOException {
        if (ps.procType == null) {
            return false;
        }
        if (ps.procType.length != 2) {
            throw new IOException("Unknown Proc-Type field.");
        }
        if (BuildInfoUtils.BID_LAN.equals(ps.procType[0])) {
            return "ENCRYPTED".equals(ps.procType[1]);
        }
        StringBuffer stringBuffer = new StringBuffer("Unknown Proc-Type field (");
        stringBuffer.append(ps.procType[0]);
        stringBuffer.append(")");
        throw new IOException(stringBuffer.toString());
    }

    public static Object decode(char[] pem, String password) throws IOException {
        PEMStructure ps = parsePEM(pem);
        if (isPEMEncrypted(ps)) {
            if (password == null) {
                throw new IOException("PEM is encrypted, but no password was specified");
            }
            decryptPEM(ps, password.getBytes());
        }
        if (ps.pemType == 2) {
            SimpleDERReader dr = new SimpleDERReader(ps.data);
            byte[] seq = dr.readSequenceAsByteArray();
            if (dr.available() != 0) {
                throw new IOException("Padding in DSA PRIVATE KEY DER stream.");
            }
            dr.resetInput(seq);
            BigInteger version = dr.readInt();
            if (version.compareTo(BigInteger.ZERO) != 0) {
                StringBuffer stringBuffer = new StringBuffer("Wrong version (");
                stringBuffer.append(version);
                stringBuffer.append(") in DSA PRIVATE KEY DER stream.");
                throw new IOException(stringBuffer.toString());
            }
            BigInteger p = dr.readInt();
            BigInteger q = dr.readInt();
            BigInteger g = dr.readInt();
            BigInteger y = dr.readInt();
            BigInteger x = dr.readInt();
            if (dr.available() != 0) {
                throw new IOException("Padding in DSA PRIVATE KEY DER stream.");
            }
            return new DSAPrivateKey(p, q, g, y, x);
        } else if (ps.pemType == 1) {
            SimpleDERReader dr2 = new SimpleDERReader(ps.data);
            byte[] seq2 = dr2.readSequenceAsByteArray();
            if (dr2.available() != 0) {
                throw new IOException("Padding in RSA PRIVATE KEY DER stream.");
            }
            dr2.resetInput(seq2);
            BigInteger version2 = dr2.readInt();
            if (version2.compareTo(BigInteger.ZERO) != 0 && version2.compareTo(BigInteger.ONE) != 0) {
                StringBuffer stringBuffer2 = new StringBuffer("Wrong version (");
                stringBuffer2.append(version2);
                stringBuffer2.append(") in RSA PRIVATE KEY DER stream.");
                throw new IOException(stringBuffer2.toString());
            }
            BigInteger n = dr2.readInt();
            BigInteger e = dr2.readInt();
            BigInteger d = dr2.readInt();
            return new RSAPrivateKey(d, e, n);
        } else {
            throw new IOException("PEM problem: it is of unknown type");
        }
    }
}
