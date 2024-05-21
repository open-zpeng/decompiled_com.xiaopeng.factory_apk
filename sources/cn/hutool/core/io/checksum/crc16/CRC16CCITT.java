package cn.hutool.core.io.checksum.crc16;
/* loaded from: classes.dex */
public class CRC16CCITT extends CRC16Checksum {
    private static final int WC_POLY = 33800;
    private static final long serialVersionUID = 1;

    @Override // java.util.zip.Checksum
    public void update(int b) {
        this.wCRCin ^= b & 255;
        for (int j = 0; j < 8; j++) {
            if ((this.wCRCin & 1) != 0) {
                this.wCRCin >>= 1;
                this.wCRCin ^= WC_POLY;
            } else {
                this.wCRCin >>= 1;
            }
        }
    }
}
