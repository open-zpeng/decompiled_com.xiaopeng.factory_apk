package cn.hutool.core.io.checksum;

import java.io.Serializable;
import java.util.zip.Checksum;
/* loaded from: classes.dex */
public class CRC8 implements Checksum, Serializable {
    private static final long serialVersionUID = 1;
    private final short[] crcTable = new short[256];
    private final short init;
    private short value;

    public CRC8(int polynomial, short init) {
        this.init = init;
        this.value = init;
        for (int dividend = 0; dividend < 256; dividend++) {
            int remainder = dividend;
            for (int bit = 0; bit < 8; bit++) {
                if ((remainder & 1) != 0) {
                    remainder = (remainder >>> 1) ^ polynomial;
                } else {
                    remainder >>>= 1;
                }
            }
            this.crcTable[dividend] = (short) remainder;
        }
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] buffer, int offset, int len) {
        for (int i = 0; i < len; i++) {
            byte b = buffer[offset + i];
            short s = this.value;
            int data = b ^ s;
            this.value = (short) ((s << 8) ^ this.crcTable[data & 255]);
        }
    }

    public void update(byte[] buffer) {
        update(buffer, 0, buffer.length);
    }

    @Override // java.util.zip.Checksum
    public void update(int b) {
        update(new byte[]{(byte) b}, 0, 1);
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        return this.value & 255;
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        this.value = this.init;
    }
}
