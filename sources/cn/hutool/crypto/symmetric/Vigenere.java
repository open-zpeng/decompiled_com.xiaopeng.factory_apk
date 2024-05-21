package cn.hutool.crypto.symmetric;
/* loaded from: classes.dex */
public class Vigenere {
    public static String encrypt(CharSequence data, CharSequence cipherKey) {
        int dataLen = data.length();
        int cipherKeyLen = cipherKey.length();
        char[] cipherArray = new char[dataLen];
        for (int i = 0; i < (dataLen / cipherKeyLen) + 1; i++) {
            for (int t = 0; t < cipherKeyLen; t++) {
                if ((i * cipherKeyLen) + t < dataLen) {
                    char dataChar = data.charAt((i * cipherKeyLen) + t);
                    char cipherKeyChar = cipherKey.charAt(t);
                    cipherArray[(i * cipherKeyLen) + t] = (char) ((((dataChar + cipherKeyChar) - 64) % 95) + 32);
                }
            }
        }
        return String.valueOf(cipherArray);
    }

    public static String decrypt(CharSequence data, CharSequence cipherKey) {
        int dataLen = data.length();
        int cipherKeyLen = cipherKey.length();
        char[] clearArray = new char[dataLen];
        for (int i = 0; i < dataLen; i++) {
            for (int t = 0; t < cipherKeyLen; t++) {
                if ((i * cipherKeyLen) + t < dataLen) {
                    char dataChar = data.charAt((i * cipherKeyLen) + t);
                    char cipherKeyChar = cipherKey.charAt(t);
                    if (dataChar - cipherKeyChar >= 0) {
                        clearArray[(i * cipherKeyLen) + t] = (char) (((dataChar - cipherKeyChar) % 95) + 32);
                    } else {
                        clearArray[(i * cipherKeyLen) + t] = (char) ((((dataChar - cipherKeyChar) + 95) % 95) + 32);
                    }
                }
            }
        }
        return String.valueOf(clearArray);
    }
}
