package cn.hutool.crypto.asymmetric;
/* loaded from: classes.dex */
public enum KeyType {
    PublicKey(1),
    PrivateKey(2),
    SecretKey(3);
    
    private final int value;

    KeyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
