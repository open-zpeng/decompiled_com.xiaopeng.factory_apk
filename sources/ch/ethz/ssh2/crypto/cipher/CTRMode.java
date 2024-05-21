package ch.ethz.ssh2.crypto.cipher;
/* loaded from: classes.dex */
public class CTRMode implements BlockCipher {
    byte[] X;
    byte[] Xenc;
    BlockCipher bc;
    int blockSize;
    int count = 0;
    boolean doEncrypt;

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public void init(boolean forEncryption, byte[] key) {
    }

    public CTRMode(BlockCipher tc, byte[] iv, boolean doEnc) throws IllegalArgumentException {
        this.bc = tc;
        this.blockSize = this.bc.getBlockSize();
        this.doEncrypt = doEnc;
        int i = this.blockSize;
        if (i != iv.length) {
            StringBuffer stringBuffer = new StringBuffer("IV must be ");
            stringBuffer.append(this.blockSize);
            stringBuffer.append(" bytes long! (currently ");
            stringBuffer.append(iv.length);
            stringBuffer.append(")");
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        this.X = new byte[i];
        this.Xenc = new byte[i];
        System.arraycopy(iv, 0, this.X, 0, i);
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public final int getBlockSize() {
        return this.blockSize;
    }

    @Override // ch.ethz.ssh2.crypto.cipher.BlockCipher
    public final void transformBlock(byte[] src, int srcoff, byte[] dst, int dstoff) {
        int i;
        this.bc.transformBlock(this.X, 0, this.Xenc, 0);
        int i2 = 0;
        while (true) {
            i = this.blockSize;
            if (i2 >= i) {
                break;
            }
            dst[dstoff + i2] = (byte) (src[srcoff + i2] ^ this.Xenc[i2]);
            i2++;
        }
        for (int i3 = i - 1; i3 >= 0; i3--) {
            byte[] bArr = this.X;
            bArr[i3] = (byte) (bArr[i3] + 1);
            if (bArr[i3] != 0) {
                return;
            }
        }
    }
}
