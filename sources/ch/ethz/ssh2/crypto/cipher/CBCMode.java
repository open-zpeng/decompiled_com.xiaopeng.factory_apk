package ch.ethz.ssh2.crypto.cipher;
/* loaded from: classes.dex */
public class CBCMode implements BlockCipher {
    int blockSize;
    byte[] cbc_vector;
    boolean doEncrypt;
    BlockCipher tc;
    byte[] tmp_vector;

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void init(boolean forEncryption, byte[] key) {
    }

    public CBCMode(BlockCipher tc, byte[] iv, boolean doEncrypt) throws IllegalArgumentException {
        this.tc = tc;
        this.blockSize = tc.getBlockSize();
        this.doEncrypt = doEncrypt;
        int i = this.blockSize;
        if (i != iv.length) {
            StringBuffer stringBuffer = new StringBuffer("IV must be ");
            stringBuffer.append(this.blockSize);
            stringBuffer.append(" bytes long! (currently ");
            stringBuffer.append(iv.length);
            stringBuffer.append(")");
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        this.cbc_vector = new byte[i];
        this.tmp_vector = new byte[i];
        System.arraycopy(iv, 0, this.cbc_vector, 0, i);
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public int getBlockSize() {
        return this.blockSize;
    }

    private void encryptBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        for (int i = 0; i < this.blockSize; i++) {
            byte[] bArr = this.cbc_vector;
            bArr[i] = (byte) (bArr[i] ^ src[srcoff + i]);
        }
        this.tc.transformBlock(this.cbc_vector, 0, dst, dstoff);
        System.arraycopy(dst, dstoff, this.cbc_vector, 0, this.blockSize);
    }

    private void decryptBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        System.arraycopy(src, srcoff, this.tmp_vector, 0, this.blockSize);
        this.tc.transformBlock(src, srcoff, dst, dstoff);
        for (int i = 0; i < this.blockSize; i++) {
            int i2 = dstoff + i;
            dst[i2] = (byte) (dst[i2] ^ this.cbc_vector[i]);
        }
        byte[] swap = this.cbc_vector;
        this.cbc_vector = this.tmp_vector;
        this.tmp_vector = swap;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void transformBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        if (this.doEncrypt) {
            encryptBlock(src, srcoff, dst, dstoff);
        } else {
            decryptBlock(src, srcoff, dst, dstoff);
        }
    }
}
