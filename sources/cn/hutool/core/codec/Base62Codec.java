package cn.hutool.core.codec;

import cn.hutool.core.util.ArrayUtil;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
/* loaded from: classes.dex */
public class Base62Codec implements Serializable {
    private static final byte[] GMP = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, TestResultItem.RESULT_ENTER, TestResultItem.RESULT_FAIL, 71, 72, 73, 74, 75, 76, 77, TestResultItem.RESULT_NOTEST, 79, TestResultItem.RESULT_PASS, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static final byte[] INVERTED = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, TestResultItem.RESULT_ENTER, TestResultItem.RESULT_FAIL, 71, 72, 73, 74, 75, 76, 77, TestResultItem.RESULT_NOTEST, 79, TestResultItem.RESULT_PASS, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90};
    private static final int STANDARD_BASE = 256;
    private static final int TARGET_BASE = 62;
    private static final long serialVersionUID = 1;
    private final byte[] alphabet;
    private final byte[] lookup = new byte[256];

    public static Base62Codec createGmp() {
        return new Base62Codec(GMP);
    }

    public static Base62Codec createInverted() {
        return new Base62Codec(INVERTED);
    }

    public Base62Codec(byte[] alphabet) {
        this.alphabet = alphabet;
        for (int i = 0; i < alphabet.length; i++) {
            this.lookup[alphabet[i]] = (byte) (i & 255);
        }
    }

    public byte[] encode(byte[] message) {
        byte[] indices = convert(message, 256, TARGET_BASE);
        return translate(indices, this.alphabet);
    }

    public byte[] decode(byte[] encoded) {
        byte[] prepared = translate(encoded, this.lookup);
        return convert(prepared, TARGET_BASE, 256);
    }

    private byte[] translate(byte[] indices, byte[] dictionary) {
        byte[] translation = new byte[indices.length];
        for (int i = 0; i < indices.length; i++) {
            translation[i] = dictionary[indices[i]];
        }
        return translation;
    }

    private byte[] convert(byte[] message, int sourceBase, int targetBase) {
        int estimatedLength = estimateOutputLength(message.length, sourceBase, targetBase);
        ByteArrayOutputStream out = new ByteArrayOutputStream(estimatedLength);
        byte[] source = message;
        while (true) {
            if (source.length <= 0) {
                break;
            }
            ByteArrayOutputStream quotient = new ByteArrayOutputStream(source.length);
            int remainder = 0;
            for (byte b : source) {
                int accumulator = (b & 255) + (remainder * sourceBase);
                int digit = (accumulator - (accumulator % targetBase)) / targetBase;
                remainder = accumulator % targetBase;
                if (quotient.size() > 0 || digit > 0) {
                    quotient.write(digit);
                }
            }
            out.write(remainder);
            source = quotient.toByteArray();
        }
        for (int i = 0; i < message.length - 1 && message[i] == 0; i++) {
            out.write(0);
        }
        return ArrayUtil.reverse(out.toByteArray());
    }

    private int estimateOutputLength(int inputLength, int sourceBase, int targetBase) {
        return (int) Math.ceil((Math.log(sourceBase) / Math.log(targetBase)) * inputLength);
    }
}
