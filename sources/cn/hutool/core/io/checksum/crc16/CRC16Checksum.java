package cn.hutool.core.io.checksum.crc16;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.util.zip.Checksum;
/* loaded from: classes.dex */
public abstract class CRC16Checksum implements Checksum, Serializable {
    private static final long serialVersionUID = 1;
    protected int wCRCin;

    public CRC16Checksum() {
        reset();
    }

    @Override // java.util.zip.Checksum
    public long getValue() {
        return this.wCRCin;
    }

    public String getHexValue() {
        return getHexValue(false);
    }

    public String getHexValue(boolean isPadding) {
        String hex = HexUtil.toHex(getValue());
        if (isPadding) {
            return StrUtil.padPre((CharSequence) hex, 4, '0');
        }
        return hex;
    }

    @Override // java.util.zip.Checksum
    public void reset() {
        this.wCRCin = 0;
    }

    public void update(byte[] b) {
        update(b, 0, b.length);
    }

    @Override // java.util.zip.Checksum
    public void update(byte[] b, int off, int len) {
        for (int i = off; i < off + len; i++) {
            update(b[i]);
        }
    }
}
