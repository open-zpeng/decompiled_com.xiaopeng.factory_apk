package cn.hutool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.apache.commons.lang3.CharEncoding;
/* loaded from: classes.dex */
public class BOMInputStream extends InputStream {
    private static final int BOM_SIZE = 4;
    private String charset;
    private final String defaultCharset;
    private final PushbackInputStream in;
    private boolean isInited;

    public BOMInputStream(InputStream in) {
        this(in, "UTF-8");
    }

    public BOMInputStream(InputStream in, String defaultCharset) {
        this.isInited = false;
        this.in = new PushbackInputStream(in, 4);
        this.defaultCharset = defaultCharset;
    }

    public String getDefaultCharset() {
        return this.defaultCharset;
    }

    public String getCharset() {
        if (!this.isInited) {
            try {
                init();
            } catch (IOException ex) {
                throw new IORuntimeException(ex);
            }
        }
        return this.charset;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.isInited = true;
        this.in.close();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        this.isInited = true;
        return this.in.read();
    }

    protected void init() throws IOException {
        int unread;
        if (this.isInited) {
            return;
        }
        byte[] bom = new byte[4];
        int n = this.in.read(bom, 0, bom.length);
        if (bom[0] == 0 && bom[1] == 0 && bom[2] == -2 && bom[3] == -1) {
            this.charset = "UTF-32BE";
            unread = n - 4;
        } else {
            int unread2 = bom[0];
            if (unread2 == -1 && bom[1] == -2 && bom[2] == 0 && bom[3] == 0) {
                this.charset = "UTF-32LE";
                unread = n - 4;
            } else {
                int unread3 = bom[0];
                if (unread3 == -17 && bom[1] == -69 && bom[2] == -65) {
                    this.charset = "UTF-8";
                    unread = n - 3;
                } else {
                    int unread4 = bom[0];
                    if (unread4 == -2 && bom[1] == -1) {
                        this.charset = CharEncoding.UTF_16BE;
                        unread = n - 2;
                    } else {
                        int unread5 = bom[0];
                        if (unread5 == -1 && bom[1] == -2) {
                            this.charset = CharEncoding.UTF_16LE;
                            unread = n - 2;
                        } else {
                            this.charset = this.defaultCharset;
                            unread = n;
                        }
                    }
                }
            }
        }
        if (unread > 0) {
            this.in.unread(bom, n - unread, unread);
        }
        this.isInited = true;
    }
}
