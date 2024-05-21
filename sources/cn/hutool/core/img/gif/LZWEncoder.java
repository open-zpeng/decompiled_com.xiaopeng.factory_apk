package cn.hutool.core.img.gif;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.net.bsd.RCommandClient;
/* loaded from: classes.dex */
class LZWEncoder {
    static final int BITS = 12;
    private static final int EOF = -1;
    static final int HSIZE = 5003;
    int ClearCode;
    int EOFCode;
    int a_count;
    private int curPixel;
    int g_init_bits;
    private final int imgH;
    private final int imgW;
    private final int initCodeSize;
    int maxcode;
    int n_bits;
    private final byte[] pixAry;
    private int remaining;
    int maxbits = 12;
    int maxmaxcode = 4096;
    int[] htab = new int[HSIZE];
    int[] codetab = new int[HSIZE];
    int hsize = HSIZE;
    int free_ent = 0;
    boolean clear_flg = false;
    int cur_accum = 0;
    int cur_bits = 0;
    final int[] masks = {0, 1, 3, 7, 15, 31, 63, 127, 255, 511, RCommandClient.MAX_CLIENT_PORT, 2047, 4095, 8191, 16383, 32767, 65535};
    byte[] accum = new byte[256];

    /* JADX INFO: Access modifiers changed from: package-private */
    public LZWEncoder(int width, int height, byte[] pixels, int color_depth) {
        this.imgW = width;
        this.imgH = height;
        this.pixAry = pixels;
        this.initCodeSize = Math.max(2, color_depth);
    }

    void char_out(byte c, OutputStream outs) throws IOException {
        byte[] bArr = this.accum;
        int i = this.a_count;
        this.a_count = i + 1;
        bArr[i] = c;
        if (this.a_count >= 254) {
            flush_char(outs);
        }
    }

    void cl_block(OutputStream outs) throws IOException {
        cl_hash(this.hsize);
        int i = this.ClearCode;
        this.free_ent = i + 2;
        this.clear_flg = true;
        output(i, outs);
    }

    void cl_hash(int hsize) {
        for (int i = 0; i < hsize; i++) {
            this.htab[i] = -1;
        }
    }

    void compress(int init_bits, OutputStream outs) throws IOException {
        int[] iArr;
        this.g_init_bits = init_bits;
        this.clear_flg = false;
        this.n_bits = this.g_init_bits;
        this.maxcode = MAXCODE(this.n_bits);
        this.ClearCode = 1 << (init_bits - 1);
        int i = this.ClearCode;
        this.EOFCode = i + 1;
        this.free_ent = i + 2;
        this.a_count = 0;
        int ent = nextPixel();
        int hshift = 0;
        for (int fcode = this.hsize; fcode < 65536; fcode *= 2) {
            hshift++;
        }
        int hshift2 = 8 - hshift;
        int hsize_reg = this.hsize;
        cl_hash(hsize_reg);
        output(this.ClearCode, outs);
        while (true) {
            int c = nextPixel();
            if (c != -1) {
                int fcode2 = (c << this.maxbits) + ent;
                int i2 = (c << hshift2) ^ ent;
                int[] iArr2 = this.htab;
                if (iArr2[i2] == fcode2) {
                    ent = this.codetab[i2];
                } else {
                    if (iArr2[i2] >= 0) {
                        int disp = hsize_reg - i2;
                        if (i2 == 0) {
                            disp = 1;
                        }
                        do {
                            int i3 = i2 - disp;
                            i2 = i3;
                            if (i3 < 0) {
                                i2 += hsize_reg;
                            }
                            iArr = this.htab;
                            if (iArr[i2] == fcode2) {
                                ent = this.codetab[i2];
                                break;
                            }
                        } while (iArr[i2] >= 0);
                    }
                    output(ent, outs);
                    ent = c;
                    int i4 = this.free_ent;
                    if (i4 < this.maxmaxcode) {
                        int[] iArr3 = this.codetab;
                        this.free_ent = i4 + 1;
                        iArr3[i2] = i4;
                        this.htab[i2] = fcode2;
                    } else {
                        cl_block(outs);
                    }
                }
            } else {
                output(ent, outs);
                output(this.EOFCode, outs);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void encode(OutputStream os) throws IOException {
        os.write(this.initCodeSize);
        this.remaining = this.imgW * this.imgH;
        this.curPixel = 0;
        compress(this.initCodeSize + 1, os);
        os.write(0);
    }

    void flush_char(OutputStream outs) throws IOException {
        int i = this.a_count;
        if (i > 0) {
            outs.write(i);
            outs.write(this.accum, 0, this.a_count);
            this.a_count = 0;
        }
    }

    final int MAXCODE(int n_bits) {
        return (1 << n_bits) - 1;
    }

    private int nextPixel() {
        int i = this.remaining;
        if (i == 0) {
            return -1;
        }
        this.remaining = i - 1;
        byte[] bArr = this.pixAry;
        int i2 = this.curPixel;
        this.curPixel = i2 + 1;
        byte pix = bArr[i2];
        return pix & 255;
    }

    void output(int code, OutputStream outs) throws IOException {
        int i = this.cur_accum;
        int[] iArr = this.masks;
        int i2 = this.cur_bits;
        this.cur_accum = i & iArr[i2];
        if (i2 > 0) {
            this.cur_accum |= code << i2;
        } else {
            this.cur_accum = code;
        }
        this.cur_bits += this.n_bits;
        while (this.cur_bits >= 8) {
            char_out((byte) (this.cur_accum & 255), outs);
            this.cur_accum >>= 8;
            this.cur_bits -= 8;
        }
        if (this.free_ent > this.maxcode || this.clear_flg) {
            if (this.clear_flg) {
                int i3 = this.g_init_bits;
                this.n_bits = i3;
                this.maxcode = MAXCODE(i3);
                this.clear_flg = false;
            } else {
                this.n_bits++;
                int i4 = this.n_bits;
                if (i4 == this.maxbits) {
                    this.maxcode = this.maxmaxcode;
                } else {
                    this.maxcode = MAXCODE(i4);
                }
            }
        }
        if (code == this.EOFCode) {
            while (this.cur_bits > 0) {
                char_out((byte) (this.cur_accum & 255), outs);
                this.cur_accum >>= 8;
                this.cur_bits -= 8;
            }
            flush_char(outs);
        }
    }
}
