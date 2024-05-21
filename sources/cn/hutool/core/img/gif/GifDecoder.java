package cn.hutool.core.img.gif;

import androidx.fragment.app.FragmentTransaction;
import cn.hutool.core.io.IoUtil;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GifDecoder {
    protected static final int MAX_STACK_SIZE = 4096;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    protected int[] act;
    protected int bgColor;
    protected int bgIndex;
    protected int frameCount;
    protected ArrayList<GifFrame> frames;
    protected int[] gct;
    protected boolean gctFlag;
    protected int gctSize;
    protected int height;
    protected int ih;
    protected BufferedImage image;
    protected BufferedInputStream in;
    protected boolean interlace;
    protected int iw;
    protected int ix;
    protected int iy;
    protected int lastBgColor;
    protected BufferedImage lastImage;
    protected Rectangle lastRect;
    protected int[] lct;
    protected boolean lctFlag;
    protected int lctSize;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected short[] prefix;
    protected int status;
    protected byte[] suffix;
    protected int transIndex;
    protected int width;
    protected int loopCount = 1;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;
    protected int dispose = 0;
    protected int lastDispose = 0;
    protected boolean transparency = false;
    protected int delay = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class GifFrame {
        public int delay;
        public BufferedImage image;

        public GifFrame(BufferedImage im, int del) {
            this.image = im;
            this.delay = del;
        }
    }

    public int getDelay(int n) {
        this.delay = -1;
        if (n >= 0 && n < this.frameCount) {
            this.delay = this.frames.get(n).delay;
        }
        return this.delay;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public BufferedImage getImage() {
        return getFrame(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    protected void setPixels() {
        Color c;
        int[] dest = this.image.getRaster().getDataBuffer().getData();
        int i = this.lastDispose;
        if (i > 0) {
            if (i == 3) {
                int n = this.frameCount - 2;
                if (n > 0) {
                    this.lastImage = getFrame(n - 1);
                } else {
                    this.lastImage = null;
                }
            }
            BufferedImage bufferedImage = this.lastImage;
            if (bufferedImage != null) {
                int[] prev = bufferedImage.getRaster().getDataBuffer().getData();
                System.arraycopy(prev, 0, dest, 0, this.width * this.height);
                if (this.lastDispose == 2) {
                    Graphics2D g = this.image.createGraphics();
                    if (this.transparency) {
                        c = new Color(0, 0, 0, 0);
                    } else {
                        c = new Color(this.lastBgColor);
                    }
                    g.setColor(c);
                    g.setComposite(AlphaComposite.Src);
                    g.fill(this.lastRect);
                    g.dispose();
                }
            }
        }
        int pass = 1;
        int inc = 8;
        int iline = 0;
        int i2 = 0;
        while (true) {
            int i3 = this.ih;
            if (i2 < i3) {
                int line = i2;
                if (this.interlace) {
                    if (iline >= i3) {
                        pass++;
                        if (pass == 2) {
                            iline = 4;
                        } else if (pass == 3) {
                            iline = 2;
                            inc = 4;
                        } else if (pass == 4) {
                            iline = 1;
                            inc = 2;
                        }
                    }
                    line = iline;
                    iline += inc;
                }
                int line2 = line + this.iy;
                if (line2 < this.height) {
                    int i4 = this.width;
                    int k = line2 * i4;
                    int dx = this.ix + k;
                    int dlim = this.iw + dx;
                    if (k + i4 < dlim) {
                        dlim = k + i4;
                    }
                    int sx = this.iw * i2;
                    while (dx < dlim) {
                        int sx2 = sx + 1;
                        int index = this.pixels[sx] & 255;
                        int c2 = this.act[index];
                        if (c2 != 0) {
                            dest[dx] = c2;
                        }
                        dx++;
                        sx = sx2;
                    }
                }
                i2++;
            } else {
                return;
            }
        }
    }

    public BufferedImage getFrame(int n) {
        if (n < 0 || n >= this.frameCount) {
            return null;
        }
        BufferedImage im = this.frames.get(n).image;
        return im;
    }

    public Dimension getFrameSize() {
        return new Dimension(this.width, this.height);
    }

    public int read(BufferedInputStream is) {
        init();
        if (is != null) {
            this.in = is;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        IoUtil.close((Closeable) is);
        return this.status;
    }

    public int read(InputStream is) {
        init();
        if (is != null) {
            if (!(is instanceof BufferedInputStream)) {
                is = new BufferedInputStream(is);
            }
            this.in = (BufferedInputStream) is;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        IoUtil.close((Closeable) is);
        return this.status;
    }

    public int read(String name) {
        String name2;
        this.status = 0;
        try {
            name2 = name.trim().toLowerCase();
        } catch (IOException e) {
            this.status = 2;
        }
        if (!name2.contains("file:") && name2.indexOf(":/") <= 0) {
            this.in = new BufferedInputStream(new FileInputStream(name2));
            this.status = read(this.in);
            return this.status;
        }
        URL url = new URL(name2);
        this.in = new BufferedInputStream(url.openStream());
        this.status = read(this.in);
        return this.status;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void decodeImageData() {
        int first;
        int NullCode;
        int data_size;
        int clear;
        int in_code = -1;
        int npix = this.iw * this.ih;
        byte[] bArr = this.pixels;
        if (bArr == null || bArr.length < npix) {
            this.pixels = new byte[npix];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
        }
        int data_size2 = read();
        int clear2 = 1 << data_size2;
        int end_of_information = clear2 + 1;
        int available = clear2 + 2;
        int code_size = data_size2 + 1;
        int code_mask = (1 << code_size) - 1;
        int code = 0;
        while (true) {
            first = 0;
            if (code >= clear2) {
                break;
            }
            this.prefix[code] = 0;
            this.suffix[code] = (byte) code;
            code++;
        }
        int top = 0;
        int count = 0;
        int datum = 0;
        int old_code = -1;
        int bits = 0;
        int bits2 = 0;
        int bi = 0;
        int available2 = available;
        int available3 = 0;
        while (available3 < npix) {
            if (top != 0) {
                NullCode = in_code;
                data_size = data_size2;
                clear = clear2;
            } else if (bits < code_size) {
                if (count == 0) {
                    count = readBlock();
                    if (count <= 0) {
                        break;
                    }
                    bi = 0;
                }
                datum += (this.block[bi] & 255) << bits;
                bits += 8;
                bi++;
                count--;
            } else {
                short code2 = datum & code_mask;
                datum >>= code_size;
                bits -= code_size;
                if (code2 <= available2 && code2 != end_of_information) {
                    if (code2 == clear2) {
                        code_size = data_size2 + 1;
                        code_mask = (1 << code_size) - 1;
                        available2 = clear2 + 2;
                        old_code = in_code;
                    } else if (old_code == in_code) {
                        int NullCode2 = in_code;
                        this.pixelStack[top] = this.suffix[code2];
                        old_code = code2;
                        first = code2;
                        top++;
                        in_code = NullCode2;
                    } else {
                        NullCode = in_code;
                        if (code2 != available2) {
                            data_size = data_size2;
                        } else {
                            data_size = data_size2;
                            this.pixelStack[top] = (byte) first;
                            code2 = old_code;
                            top++;
                        }
                        while (code2 > clear2) {
                            this.pixelStack[top] = this.suffix[code2];
                            code2 = this.prefix[code2];
                            top++;
                        }
                        byte[] bArr2 = this.suffix;
                        first = bArr2[code2] & 255;
                        if (available2 >= 4096) {
                            this.pixelStack[top] = (byte) first;
                            top++;
                            in_code = NullCode;
                            data_size2 = data_size;
                        } else {
                            int top2 = top + 1;
                            clear = clear2;
                            this.pixelStack[top] = (byte) first;
                            this.prefix[available2] = (short) old_code;
                            bArr2[available2] = (byte) first;
                            available2++;
                            if ((available2 & code_mask) == 0 && available2 < 4096) {
                                code_size++;
                                code_mask += available2;
                            }
                            old_code = code2;
                            top = top2;
                        }
                    }
                }
            }
            top--;
            int pi = bits2 + 1;
            this.pixels[bits2] = this.pixelStack[top];
            available3++;
            bits2 = pi;
            in_code = NullCode;
            data_size2 = data_size;
            clear2 = clear;
        }
        for (int i = bits2; i < npix; i++) {
            this.pixels[i] = 0;
        }
    }

    protected boolean err() {
        return this.status != 0;
    }

    protected void init() {
        this.status = 0;
        this.frameCount = 0;
        this.frames = new ArrayList<>();
        this.gct = null;
        this.lct = null;
    }

    protected int read() {
        try {
            int curByte = this.in.read();
            return curByte;
        } catch (IOException e) {
            this.status = 1;
            return 0;
        }
    }

    protected int readBlock() {
        this.blockSize = read();
        int n = 0;
        if (this.blockSize > 0) {
            while (n < this.blockSize) {
                try {
                    int count = this.in.read(this.block, n, this.blockSize - n);
                    if (count == -1) {
                        break;
                    }
                    n += count;
                } catch (IOException e) {
                }
            }
            if (n < this.blockSize) {
                this.status = 1;
            }
        }
        return n;
    }

    protected int[] readColorTable(int ncolors) {
        int nbytes = ncolors * 3;
        int[] tab = null;
        byte[] c = new byte[nbytes];
        int n = 0;
        try {
            n = this.in.read(c);
        } catch (IOException e) {
        }
        if (n < nbytes) {
            this.status = 1;
        } else {
            tab = new int[256];
            int r = 0;
            for (int i = 0; i < ncolors; i++) {
                int j = r + 1;
                int r2 = c[r] & 255;
                int j2 = j + 1;
                int g = c[j] & 255;
                int j3 = j2 + 1;
                int b = c[j2] & 255;
                tab[i] = (-16777216) | (r2 << 16) | (g << 8) | b;
                r = j3;
            }
        }
        return tab;
    }

    protected void readContents() {
        boolean done = false;
        while (!done && !err()) {
            int code = read();
            if (code != 0) {
                if (code == 33) {
                    int code2 = read();
                    if (code2 == 249) {
                        readGraphicControlExt();
                    } else if (code2 == 255) {
                        readBlock();
                        StringBuilder app = new StringBuilder();
                        for (int i = 0; i < 11; i++) {
                            app.append((char) this.block[i]);
                        }
                        if ("NETSCAPE2.0".equals(app.toString())) {
                            readNetscapeExt();
                        } else {
                            skip();
                        }
                    } else {
                        skip();
                    }
                } else if (code == 44) {
                    readImage();
                } else if (code == 59) {
                    done = true;
                } else {
                    this.status = 1;
                }
            }
        }
    }

    protected void readGraphicControlExt() {
        read();
        int packed = read();
        this.dispose = (packed & 28) >> 2;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        this.transparency = (packed & 1) != 0;
        this.delay = readShort() * 10;
        this.transIndex = read();
        read();
    }

    protected void readHeader() {
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            id.append((char) read());
        }
        if (!id.toString().startsWith("GIF")) {
            this.status = 1;
            return;
        }
        readLSD();
        if (this.gctFlag && !err()) {
            this.gct = readColorTable(this.gctSize);
            this.bgColor = this.gct[this.bgIndex];
        }
    }

    protected void readImage() {
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int packed = read();
        this.lctFlag = (packed & 128) != 0;
        this.interlace = (packed & 64) != 0;
        this.lctSize = 2 << (packed & 7);
        if (this.lctFlag) {
            this.lct = readColorTable(this.lctSize);
            this.act = this.lct;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        int save = 0;
        if (this.transparency) {
            int[] iArr = this.act;
            int i = this.transIndex;
            save = iArr[i];
            iArr[i] = 0;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (err()) {
            return;
        }
        decodeImageData();
        skip();
        if (err()) {
            return;
        }
        this.frameCount++;
        this.image = new BufferedImage(this.width, this.height, 3);
        setPixels();
        this.frames.add(new GifFrame(this.image, this.delay));
        if (this.transparency) {
            this.act[this.transIndex] = save;
        }
        resetFrame();
    }

    protected void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int packed = read();
        this.gctFlag = (packed & 128) != 0;
        this.gctSize = 2 << (packed & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    protected void readNetscapeExt() {
        do {
            readBlock();
            byte[] bArr = this.block;
            if (bArr[0] == 1) {
                int b1 = bArr[1] & 255;
                int b2 = bArr[2] & 255;
                this.loopCount = (b2 << 8) | b1;
            }
            int b22 = this.blockSize;
            if (b22 <= 0) {
                return;
            }
        } while (!err());
    }

    protected int readShort() {
        return read() | (read() << 8);
    }

    protected void resetFrame() {
        this.lastDispose = this.dispose;
        this.lastRect = new Rectangle(this.ix, this.iy, this.iw, this.ih);
        this.lastImage = this.image;
        this.lastBgColor = this.bgColor;
        this.lct = null;
    }

    protected void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
