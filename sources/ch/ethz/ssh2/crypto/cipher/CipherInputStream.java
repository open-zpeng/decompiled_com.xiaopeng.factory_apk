package ch.ethz.ssh2.crypto.cipher;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class CipherInputStream {
    InputStream bi;
    int blockSize;
    byte[] buffer;
    BlockCipher currentCipher;
    byte[] enc;
    int pos;
    final int BUFF_SIZE = 2048;
    byte[] input_buffer = new byte[2048];
    int input_buffer_pos = 0;
    int input_buffer_size = 0;

    public CipherInputStream(BlockCipher tc, InputStream bi) {
        this.bi = bi;
        changeCipher(tc);
    }

    private int fill_buffer() throws IOException {
        this.input_buffer_pos = 0;
        this.input_buffer_size = this.bi.read(this.input_buffer, 0, 2048);
        return this.input_buffer_size;
    }

    private int internal_read(byte[] b, int off, int len) throws IOException {
        int i = this.input_buffer_size;
        if (i < 0) {
            return -1;
        }
        if (this.input_buffer_pos < i || fill_buffer() > 0) {
            int avail = this.input_buffer_size - this.input_buffer_pos;
            int thiscopy = len > avail ? avail : len;
            System.arraycopy(this.input_buffer, this.input_buffer_pos, b, off, thiscopy);
            this.input_buffer_pos += thiscopy;
            return thiscopy;
        }
        return -1;
    }

    public void changeCipher(BlockCipher bc) {
        this.currentCipher = bc;
        this.blockSize = bc.getBlockSize();
        int i = this.blockSize;
        this.buffer = new byte[i];
        this.enc = new byte[i];
        this.pos = i;
    }

    private void getBlock() throws IOException {
        int n = 0;
        while (true) {
            int i = this.blockSize;
            if (n < i) {
                int len = internal_read(this.enc, n, i - n);
                if (len < 0) {
                    throw new IOException("Cannot read full block, EOF reached.");
                }
                n += len;
            } else {
                try {
                    this.currentCipher.transformBlock(this.enc, 0, this.buffer, 0);
                    this.pos = 0;
                    return;
                } catch (Exception e) {
                    throw new IOException("Error while decrypting block.");
                }
            }
        }
    }

    public int read(byte[] dst) throws IOException {
        return read(dst, 0, dst.length);
    }

    public int read(byte[] dst, int off, int len) throws IOException {
        int count = 0;
        while (len > 0) {
            if (this.pos >= this.blockSize) {
                getBlock();
            }
            int avail = this.blockSize - this.pos;
            int copy = Math.min(avail, len);
            System.arraycopy(this.buffer, this.pos, dst, off, copy);
            this.pos += copy;
            off += copy;
            len -= copy;
            count += copy;
        }
        return count;
    }

    public int read() throws IOException {
        if (this.pos >= this.blockSize) {
            getBlock();
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public int readPlain(byte[] b, int off, int len) throws IOException {
        if (this.pos != this.blockSize) {
            throw new IOException("Cannot read plain since crypto buffer is not aligned.");
        }
        int n = 0;
        while (n < len) {
            int cnt = internal_read(b, off + n, len - n);
            if (cnt < 0) {
                throw new IOException("Cannot fill buffer, EOF reached.");
            }
            n += cnt;
        }
        return n;
    }
}
