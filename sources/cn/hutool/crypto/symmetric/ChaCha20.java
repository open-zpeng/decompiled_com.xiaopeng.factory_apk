package cn.hutool.crypto.symmetric;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import javax.crypto.spec.IvParameterSpec;
/* loaded from: classes.dex */
public class ChaCha20 extends SymmetricCrypto {
    public static final String ALGORITHM_NAME = "ChaCha20";
    private static final long serialVersionUID = 1;

    public ChaCha20(byte[] key, byte[] iv) {
        super(ALGORITHM_NAME, KeyUtil.generateKey(ALGORITHM_NAME, key), generateIvParam(iv));
    }

    private static IvParameterSpec generateIvParam(byte[] iv) {
        if (iv == null) {
            iv = RandomUtil.randomBytes(12);
        }
        return new IvParameterSpec(iv);
    }
}
