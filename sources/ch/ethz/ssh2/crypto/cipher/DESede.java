package ch.ethz.ssh2.crypto.cipher;
/* loaded from: classes.dex */
public class DESede extends DES {
    private boolean encrypt;
    private int[] key1 = null;
    private int[] key2 = null;
    private int[] key3 = null;

    @Override // ch.ethz.ssh2.crypto.cipher.DES, ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void init(boolean encrypting, byte[] key) {
        this.key1 = generateWorkingKey(encrypting, key, 0);
        this.key2 = generateWorkingKey(!encrypting, key, 8);
        this.key3 = generateWorkingKey(encrypting, key, 16);
        this.encrypt = encrypting;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.DES
    public String getAlgorithmName() {
        return "DESede";
    }

    @Override // ch.ethz.ssh2.crypto.cipher.DES, ch.ethz.ssh2.crypto.cipher.BlockCipher
    public int getBlockSize() {
        return 8;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.DES, ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void transformBlock(byte[] in, int inOff, byte[] out, int outOff) {
        int[] iArr = this.key1;
        if (iArr == null) {
            throw new IllegalStateException("DESede engine not initialised!");
        }
        if (this.encrypt) {
            desFunc(iArr, in, inOff, out, outOff);
            desFunc(this.key2, out, outOff, out, outOff);
            desFunc(this.key3, out, outOff, out, outOff);
            return;
        }
        desFunc(this.key3, in, inOff, out, outOff);
        desFunc(this.key2, out, outOff, out, outOff);
        desFunc(this.key1, out, outOff, out, outOff);
    }

    @Override // ch.ethz.ssh2.crypto.cipher.DES
    public void reset() {
    }
}
