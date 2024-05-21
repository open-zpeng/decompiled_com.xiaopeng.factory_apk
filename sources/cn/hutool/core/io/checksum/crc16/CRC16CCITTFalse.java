package cn.hutool.core.io.checksum.crc16;
/* loaded from: classes.dex */
public class CRC16CCITTFalse extends CRC16Checksum {
    private static final int WC_POLY = 4129;
    private static final long serialVersionUID = 1;

    @Override // cn.hutool.core.io.checksum.crc16.CRC16Checksum, java.util.zip.Checksum
    public void reset() {
        this.wCRCin = 65535;
    }

    @Override // cn.hutool.core.io.checksum.crc16.CRC16Checksum, java.util.zip.Checksum
    public void update(byte[] b, int off, int len) {
        super.update(b, off, len);
        this.wCRCin &= 65535;
    }

    @Override // java.util.zip.Checksum
    public void update(int b) {
        for (int i = 0; i < 8; i++) {
            boolean bit = ((b >> (7 - i)) & 1) == 1;
            boolean c15 = ((this.wCRCin >> 15) & 1) == 1;
            this.wCRCin <<= 1;
            if (c15 ^ bit) {
                this.wCRCin ^= WC_POLY;
            }
        }
    }
}
