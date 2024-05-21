package cn.hutool.crypto;
/* loaded from: classes.dex */
public enum CipherMode {
    encrypt(1),
    decrypt(2),
    wrap(3),
    unwrap(4);
    
    private final int value;

    CipherMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
