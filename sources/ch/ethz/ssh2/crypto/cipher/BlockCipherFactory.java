package ch.ethz.ssh2.crypto.cipher;

import java.util.Vector;
/* loaded from: classes.dex */
public class BlockCipherFactory {
    static Vector ciphers = new Vector();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CipherEntry {
        int blocksize;
        String cipherClass;
        int keysize;
        String type;

        public CipherEntry(String type, int blockSize, int keySize, String cipherClass) {
            this.type = type;
            this.blocksize = blockSize;
            this.keysize = keySize;
            this.cipherClass = cipherClass;
        }
    }

    static {
        ciphers.addElement(new CipherEntry("aes256-ctr", 16, 32, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("aes192-ctr", 16, 24, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("aes128-ctr", 16, 16, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("blowfish-ctr", 8, 16, "ch.ethz.ssh2.crypto.cipher.BlowFish"));
        ciphers.addElement(new CipherEntry("aes256-cbc", 16, 32, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("aes192-cbc", 16, 24, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("aes128-cbc", 16, 16, "ch.ethz.ssh2.crypto.cipher.AES"));
        ciphers.addElement(new CipherEntry("blowfish-cbc", 8, 16, "ch.ethz.ssh2.crypto.cipher.BlowFish"));
        ciphers.addElement(new CipherEntry("3des-ctr", 8, 24, "ch.ethz.ssh2.crypto.cipher.DESede"));
        ciphers.addElement(new CipherEntry("3des-cbc", 8, 24, "ch.ethz.ssh2.crypto.cipher.DESede"));
    }

    public static String[] getDefaultCipherList() {
        String[] list = new String[ciphers.size()];
        for (int i = 0; i < ciphers.size(); i++) {
            CipherEntry ce = (CipherEntry) ciphers.elementAt(i);
            list[i] = new String(ce.type);
        }
        return list;
    }

    public static void checkCipherList(String[] cipherCandidates) {
        for (String str : cipherCandidates) {
            getEntry(str);
        }
    }

    public static BlockCipher createCipher(String type, boolean encrypt, byte[] key, byte[] iv) {
        try {
            CipherEntry ce = getEntry(type);
            Class cc = Class.forName(ce.cipherClass);
            BlockCipher bc = (BlockCipher) cc.newInstance();
            if (type.endsWith("-cbc")) {
                bc.init(encrypt, key);
                return new CBCMode(bc, iv, encrypt);
            } else if (!type.endsWith("-ctr")) {
                StringBuffer stringBuffer = new StringBuffer("Cannot instantiate ");
                stringBuffer.append(type);
                throw new IllegalArgumentException(stringBuffer.toString());
            } else {
                bc.init(true, key);
                return new CTRMode(bc, iv, encrypt);
            }
        } catch (Exception e) {
            StringBuffer stringBuffer2 = new StringBuffer("Cannot instantiate ");
            stringBuffer2.append(type);
            throw new IllegalArgumentException(stringBuffer2.toString());
        }
    }

    private static CipherEntry getEntry(String type) {
        for (int i = 0; i < ciphers.size(); i++) {
            CipherEntry ce = (CipherEntry) ciphers.elementAt(i);
            if (ce.type.equals(type)) {
                return ce;
            }
        }
        StringBuffer stringBuffer = new StringBuffer("Unkown algorithm ");
        stringBuffer.append(type);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    public static int getBlockSize(String type) {
        CipherEntry ce = getEntry(type);
        return ce.blocksize;
    }

    public static int getKeySize(String type) {
        CipherEntry ce = getEntry(type);
        return ce.keysize;
    }
}
