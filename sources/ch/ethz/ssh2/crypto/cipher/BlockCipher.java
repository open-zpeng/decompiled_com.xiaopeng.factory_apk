package ch.ethz.ssh2.crypto.cipher;
/* loaded from: classes.dex */
public interface BlockCipher {
    int getBlockSize();

    void init(boolean z, byte[] bArr);

    void transformBlock(byte[] bArr, int i, byte[] bArr2, int i2);
}
