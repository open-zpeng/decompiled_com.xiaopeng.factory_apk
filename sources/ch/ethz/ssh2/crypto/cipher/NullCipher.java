package ch.ethz.ssh2.crypto.cipher;
/* loaded from: classes.dex */
public class NullCipher implements BlockCipher {
    private int blockSize;

    public NullCipher() {
        this.blockSize = 8;
    }

    public NullCipher(int blockSize) {
        this.blockSize = 8;
        this.blockSize = blockSize;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void init(boolean forEncryption, byte[] key) {
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public int getBlockSize() {
        return this.blockSize;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void transformBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        System.arraycopy(src, srcoff, dst, dstoff, this.blockSize);
    }
}
