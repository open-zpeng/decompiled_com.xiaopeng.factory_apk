package cn.hutool.crypto.symmetric.fpe;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AlphabetMapper;
import org.bouncycastle.jcajce.spec.FPEParameterSpec;
/* loaded from: classes.dex */
public class FPE implements Serializable {
    private static final long serialVersionUID = 1;
    private final AES aes;
    private final AlphabetMapper mapper;

    public FPE(FPEMode mode, byte[] key, AlphabetMapper mapper) {
        this(mode, key, mapper, null);
    }

    public FPE(FPEMode mode, byte[] key, AlphabetMapper mapper, byte[] tweak) {
        mode = mode == null ? FPEMode.FF1 : mode;
        if (tweak == null) {
            int i = AnonymousClass1.$SwitchMap$cn$hutool$crypto$symmetric$fpe$FPE$FPEMode[mode.ordinal()];
            if (i == 1) {
                tweak = new byte[0];
            } else if (i == 2) {
                tweak = new byte[7];
            }
        }
        this.aes = new AES(mode.value, Padding.NoPadding.name(), KeyUtil.generateKey(mode.value, key), (AlgorithmParameterSpec) new FPEParameterSpec(mapper.getRadix(), tweak));
        this.mapper = mapper;
    }

    /* renamed from: cn.hutool.crypto.symmetric.fpe.FPE$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$cn$hutool$crypto$symmetric$fpe$FPE$FPEMode = new int[FPEMode.values().length];

        static {
            try {
                $SwitchMap$cn$hutool$crypto$symmetric$fpe$FPE$FPEMode[FPEMode.FF1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cn$hutool$crypto$symmetric$fpe$FPE$FPEMode[FPEMode.FF3_1.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public String encrypt(String data) {
        if (data == null) {
            return null;
        }
        return new String(encrypt(data.toCharArray()));
    }

    public char[] encrypt(char[] data) {
        if (data == null) {
            return null;
        }
        AlphabetMapper alphabetMapper = this.mapper;
        return alphabetMapper.convertToChars(this.aes.encrypt(alphabetMapper.convertToIndexes(data)));
    }

    public String decrypt(String data) {
        if (data == null) {
            return null;
        }
        return new String(decrypt(data.toCharArray()));
    }

    public char[] decrypt(char[] data) {
        if (data == null) {
            return null;
        }
        AlphabetMapper alphabetMapper = this.mapper;
        return alphabetMapper.convertToChars(this.aes.decrypt(alphabetMapper.convertToIndexes(data)));
    }

    /* loaded from: classes.dex */
    public enum FPEMode {
        FF1("FF1"),
        FF3_1("FF3-1");
        
        private final String value;

        FPEMode(String name) {
            this.value = name;
        }

        public String getValue() {
            return this.value;
        }
    }
}
