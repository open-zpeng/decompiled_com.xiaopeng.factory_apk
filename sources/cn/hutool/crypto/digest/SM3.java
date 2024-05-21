package cn.hutool.crypto.digest;
/* loaded from: classes.dex */
public class SM3 extends Digester {
    public static final String ALGORITHM_NAME = "SM3";
    private static final long serialVersionUID = 1;

    public static SM3 create() {
        return new SM3();
    }

    public SM3() {
        super(ALGORITHM_NAME);
    }

    public SM3(byte[] salt) {
        this(salt, 0, 1);
    }

    public SM3(byte[] salt, int digestCount) {
        this(salt, 0, digestCount);
    }

    public SM3(byte[] salt, int saltPosition, int digestCount) {
        this();
        this.salt = salt;
        this.saltPosition = saltPosition;
        this.digestCount = digestCount;
    }
}
