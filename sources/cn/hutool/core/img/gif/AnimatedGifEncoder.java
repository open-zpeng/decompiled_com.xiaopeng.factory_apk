package cn.hutool.core.img.gif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.net.telnet.TelnetCommand;
/* loaded from: classes.dex */
public class AnimatedGifEncoder {
    protected int colorDepth;
    protected byte[] colorTab;
    protected int height;
    protected BufferedImage image;
    protected byte[] indexedPixels;
    protected OutputStream out;
    protected byte[] pixels;
    protected int transIndex;
    protected int width;
    protected Color transparent = null;
    protected boolean transparentExactMatch = false;
    protected Color background = null;
    protected int repeat = -1;
    protected int delay = 0;
    protected boolean started = false;
    protected boolean[] usedEntry = new boolean[256];
    protected int palSize = 7;
    protected int dispose = -1;
    protected boolean closeStream = false;
    protected boolean firstFrame = true;
    protected boolean sizeSet = false;
    protected int sample = 10;

    public void setDelay(int ms) {
        this.delay = Math.round(ms / 10.0f);
    }

    public void setDispose(int code) {
        if (code >= 0) {
            this.dispose = code;
        }
    }

    public void setRepeat(int iter) {
        if (iter >= 0) {
            this.repeat = iter;
        }
    }

    public void setTransparent(Color c) {
        setTransparent(c, false);
    }

    public void setTransparent(Color c, boolean exactMatch) {
        this.transparent = c;
        this.transparentExactMatch = exactMatch;
    }

    public void setBackground(Color c) {
        this.background = c;
    }

    public boolean addFrame(BufferedImage im) {
        if (im == null || !this.started) {
            return false;
        }
        try {
            if (!this.sizeSet) {
                setSize(im.getWidth(), im.getHeight());
            }
            this.image = im;
            getImagePixels();
            analyzePixels();
            if (this.firstFrame) {
                writeLSD();
                writePalette();
                if (this.repeat >= 0) {
                    writeNetscapeExt();
                }
            }
            writeGraphicCtrlExt();
            writeImageDesc();
            if (!this.firstFrame) {
                writePalette();
            }
            writePixels();
            this.firstFrame = false;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean finish() {
        if (this.started) {
            boolean ok = true;
            this.started = false;
            try {
                this.out.write(59);
                this.out.flush();
                if (this.closeStream) {
                    this.out.close();
                }
            } catch (IOException e) {
                ok = false;
            }
            this.transIndex = 0;
            this.out = null;
            this.image = null;
            this.pixels = null;
            this.indexedPixels = null;
            this.colorTab = null;
            this.closeStream = false;
            this.firstFrame = true;
            return ok;
        }
        return false;
    }

    public void setFrameRate(float fps) {
        if (fps != 0.0f) {
            this.delay = Math.round(100.0f / fps);
        }
    }

    public void setQuality(int quality) {
        if (quality < 1) {
            quality = 1;
        }
        this.sample = quality;
    }

    public void setSize(int w, int h) {
        if (!this.started || this.firstFrame) {
            this.width = w;
            this.height = h;
            if (this.width < 1) {
                this.width = 320;
            }
            if (this.height < 1) {
                this.height = 240;
            }
            this.sizeSet = true;
        }
    }

    public boolean start(OutputStream os) {
        if (os == null) {
            return false;
        }
        boolean ok = true;
        this.closeStream = false;
        this.out = os;
        try {
            writeString("GIF89a");
        } catch (IOException e) {
            ok = false;
        }
        this.started = ok;
        return ok;
    }

    public boolean start(String file) {
        boolean ok;
        try {
            this.out = new BufferedOutputStream(new FileOutputStream(file));
            ok = start(this.out);
            this.closeStream = true;
        } catch (IOException e) {
            ok = false;
        }
        this.started = ok;
        return ok;
    }

    public boolean isStarted() {
        return this.started;
    }

    protected void analyzePixels() {
        byte[] bArr = this.pixels;
        int len = bArr.length;
        int nPix = len / 3;
        this.indexedPixels = new byte[nPix];
        NeuQuant nq = new NeuQuant(bArr, len, this.sample);
        this.colorTab = nq.process();
        int i = 0;
        while (true) {
            byte[] bArr2 = this.colorTab;
            if (i >= bArr2.length) {
                break;
            }
            byte temp = bArr2[i];
            bArr2[i] = bArr2[i + 2];
            bArr2[i + 2] = temp;
            this.usedEntry[i / 3] = false;
            i += 3;
        }
        int k = 0;
        int i2 = 0;
        while (i2 < nPix) {
            byte[] bArr3 = this.pixels;
            int k2 = k + 1;
            int k3 = k2 + 1;
            int index = nq.map(bArr3[k] & 255, bArr3[k2] & 255, bArr3[k3] & 255);
            this.usedEntry[index] = true;
            this.indexedPixels[i2] = (byte) index;
            i2++;
            k = k3 + 1;
        }
        this.pixels = null;
        this.colorDepth = 8;
        this.palSize = 7;
        Color color = this.transparent;
        if (color != null) {
            this.transIndex = this.transparentExactMatch ? findExact(color) : findClosest(color);
        }
    }

    protected int findClosest(Color c) {
        if (this.colorTab == null) {
            return -1;
        }
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int minpos = 0;
        int dmin = 16777216;
        int len = this.colorTab.length;
        int dr = 0;
        while (dr < len) {
            byte[] bArr = this.colorTab;
            int i = dr + 1;
            int dr2 = r - (bArr[dr] & 255);
            int i2 = i + 1;
            int dg = g - (bArr[i] & 255);
            int db = b - (bArr[i2] & 255);
            int d = (dr2 * dr2) + (dg * dg) + (db * db);
            int index = i2 / 3;
            if (this.usedEntry[index] && d < dmin) {
                dmin = d;
                minpos = index;
            }
            dr = i2 + 1;
        }
        return minpos;
    }

    boolean isColorUsed(Color c) {
        return findExact(c) != -1;
    }

    protected int findExact(Color c) {
        if (this.colorTab == null) {
            return -1;
        }
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int len = this.colorTab.length / 3;
        for (int index = 0; index < len; index++) {
            int i = index * 3;
            if (this.usedEntry[index]) {
                byte[] bArr = this.colorTab;
                if (r == (bArr[i] & 255) && g == (bArr[i + 1] & 255) && b == (bArr[i + 2] & 255)) {
                    return index;
                }
            }
        }
        return -1;
    }

    protected void getImagePixels() {
        int w = this.image.getWidth();
        int h = this.image.getHeight();
        int type = this.image.getType();
        if (w != this.width || h != this.height || type != 5) {
            BufferedImage temp = new BufferedImage(this.width, this.height, 5);
            Graphics2D g = temp.createGraphics();
            g.setColor(this.background);
            g.fillRect(0, 0, this.width, this.height);
            g.drawImage(this.image, 0, 0, (ImageObserver) null);
            this.image = temp;
        }
        this.pixels = this.image.getRaster().getDataBuffer().getData();
    }

    protected void writeGraphicCtrlExt() throws IOException {
        int transp;
        int disp;
        this.out.write(33);
        this.out.write(TelnetCommand.GA);
        this.out.write(4);
        if (this.transparent == null) {
            transp = 0;
            disp = 0;
        } else {
            transp = 1;
            disp = 2;
        }
        int i = this.dispose;
        if (i >= 0) {
            disp = i & 7;
        }
        this.out.write((disp << 2) | 0 | 0 | transp);
        writeShort(this.delay);
        this.out.write(this.transIndex);
        this.out.write(0);
    }

    protected void writeImageDesc() throws IOException {
        this.out.write(44);
        writeShort(0);
        writeShort(0);
        writeShort(this.width);
        writeShort(this.height);
        if (this.firstFrame) {
            this.out.write(0);
        } else {
            this.out.write(this.palSize | 128);
        }
    }

    protected void writeLSD() throws IOException {
        writeShort(this.width);
        writeShort(this.height);
        this.out.write(this.palSize | 240);
        this.out.write(0);
        this.out.write(0);
    }

    protected void writeNetscapeExt() throws IOException {
        this.out.write(33);
        this.out.write(255);
        this.out.write(11);
        writeString("NETSCAPE2.0");
        this.out.write(3);
        this.out.write(1);
        writeShort(this.repeat);
        this.out.write(0);
    }

    protected void writePalette() throws IOException {
        OutputStream outputStream = this.out;
        byte[] bArr = this.colorTab;
        outputStream.write(bArr, 0, bArr.length);
        int n = 768 - this.colorTab.length;
        for (int i = 0; i < n; i++) {
            this.out.write(0);
        }
    }

    protected void writePixels() throws IOException {
        LZWEncoder encoder = new LZWEncoder(this.width, this.height, this.indexedPixels, this.colorDepth);
        encoder.encode(this.out);
    }

    protected void writeShort(int value) throws IOException {
        this.out.write(value & 255);
        this.out.write((value >> 8) & 255);
    }

    protected void writeString(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            this.out.write((byte) s.charAt(i));
        }
    }
}
