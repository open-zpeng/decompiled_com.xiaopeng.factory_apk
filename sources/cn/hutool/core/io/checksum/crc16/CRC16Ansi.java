package cn.hutool.core.io.checksum.crc16;
/* loaded from: classes.dex */
public class CRC16Ansi extends CRC16Checksum {
    private static final int WC_POLY = 40961;
    private static final long serialVersionUID = 1;

    @Override // cn.hutool.core.io.checksum.crc16.CRC16Checksum, java.util.zip.Checksum
    public void reset() {
        this.wCRCin = 65535;
    }

    @Override // java.util.zip.Checksum
    public void update(int b) {
        int hi = this.wCRCin >> 8;
        this.wCRCin = hi ^ b;
        for (int i = 0; i < 8; i++) {
            int flag = this.wCRCin & 1;
            this.wCRCin >>= 1;
            if (flag == 1) {
                this.wCRCin ^= WC_POLY;
            }
        }
    }
}
