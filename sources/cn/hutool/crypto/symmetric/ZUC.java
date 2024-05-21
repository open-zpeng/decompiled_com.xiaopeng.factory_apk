package cn.hutool.crypto.symmetric;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import javax.crypto.spec.IvParameterSpec;
/* loaded from: classes.dex */
public class ZUC extends SymmetricCrypto {
    private static final long serialVersionUID = 1;

    public static byte[] generateKey(ZUCAlgorithm algorithm) {
        return KeyUtil.generateKey(algorithm.value).getEncoded();
    }

    public ZUC(ZUCAlgorithm algorithm, byte[] key, byte[] iv) {
        super(algorithm.value, KeyUtil.generateKey(algorithm.value, key), generateIvParam(algorithm, iv));
    }

    /* loaded from: classes.dex */
    public enum ZUCAlgorithm {
        ZUC_128("ZUC-128"),
        ZUC_256("ZUC-256");
        
        private final String value;

        ZUCAlgorithm(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: cn.hutool.crypto.symmetric.ZUC$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$crypto$symmetric$ZUC$ZUCAlgorithm = new int[ZUCAlgorithm.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$crypto$symmetric$ZUC$ZUCAlgorithm[ZUCAlgorithm.ZUC_128.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$crypto$symmetric$ZUC$ZUCAlgorithm[ZUCAlgorithm.ZUC_256.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static IvParameterSpec generateIvParam(ZUCAlgorithm algorithm, byte[] iv) {
        if (iv == null) {
            int i = AnonymousClass1.$SwitchMap$cn$hutool$crypto$symmetric$ZUC$ZUCAlgorithm[algorithm.ordinal()];
            if (i == 1) {
                iv = RandomUtil.randomBytes(16);
            } else if (i == 2) {
                iv = RandomUtil.randomBytes(25);
            }
        }
        return new IvParameterSpec(iv);
    }
}
