package cn.hutool.crypto.digest.mac;

import cn.hutool.crypto.CryptoException;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public interface MacEngine {
    byte[] doFinal();

    String getAlgorithm();

    int getMacLength();

    void reset();

    void update(byte[] bArr, int i, int i2);

    default void update(byte[] in) {
        update(in, 0, in.length);
    }

    default byte[] digest(InputStream data, int bufferLength) {
        if (bufferLength < 1) {
            bufferLength = 8192;
        }
        byte[] buffer = new byte[bufferLength];
        try {
            try {
                int read = data.read(buffer, 0, bufferLength);
                while (read > -1) {
                    update(buffer, 0, read);
                    read = data.read(buffer, 0, bufferLength);
                }
                byte[] result = doFinal();
                return result;
            } catch (IOException e) {
                throw new CryptoException(e);
            }
        } finally {
            reset();
        }
    }
}
