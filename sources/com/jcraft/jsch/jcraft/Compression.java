package com.jcraft.jsch.jcraft;

import com.jcraft.jzlib.ZStream;
import java.io.PrintStream;
/* loaded from: classes.dex */
public class Compression implements com.jcraft.jsch.Compression {
    private static final int BUF_SIZE = 4096;
    private byte[] inflated_buf;
    private int type;
    private final int buffer_margin = 52;
    private byte[] tmpbuf = new byte[4096];
    private ZStream stream = new ZStream();

    @Override // com.jcraft.jsch.Compression
    public void init(int type, int level) {
        if (type == 1) {
            this.stream.deflateInit(level);
            this.type = 1;
        } else if (type == 0) {
            this.stream.inflateInit();
            this.inflated_buf = new byte[4096];
            this.type = 0;
        }
    }

    @Override // com.jcraft.jsch.Compression
    public byte[] compress(byte[] buf, int start, int[] len) {
        ZStream zStream = this.stream;
        zStream.next_in = buf;
        zStream.next_in_index = start;
        zStream.avail_in = len[0] - start;
        int outputlen = start;
        byte[] outputbuf = buf;
        do {
            ZStream zStream2 = this.stream;
            zStream2.next_out = this.tmpbuf;
            zStream2.next_out_index = 0;
            zStream2.avail_out = 4096;
            int status = zStream2.deflate(1);
            if (status == 0) {
                int tmp = 4096 - this.stream.avail_out;
                if (outputbuf.length < outputlen + tmp + 52) {
                    byte[] foo = new byte[(outputlen + tmp + 52) * 2];
                    System.arraycopy(outputbuf, 0, foo, 0, outputbuf.length);
                    outputbuf = foo;
                }
                System.arraycopy(this.tmpbuf, 0, outputbuf, outputlen, tmp);
                outputlen += tmp;
            } else {
                PrintStream printStream = System.err;
                printStream.println("compress: deflate returnd " + status);
            }
        } while (this.stream.avail_out == 0);
        len[0] = outputlen;
        return outputbuf;
    }

    @Override // com.jcraft.jsch.Compression
    public byte[] uncompress(byte[] buffer, int start, int[] length) {
        int inflated_end = 0;
        ZStream zStream = this.stream;
        zStream.next_in = buffer;
        zStream.next_in_index = start;
        zStream.avail_in = length[0];
        while (true) {
            ZStream zStream2 = this.stream;
            zStream2.next_out = this.tmpbuf;
            zStream2.next_out_index = 0;
            zStream2.avail_out = 4096;
            int status = zStream2.inflate(1);
            if (status == -5) {
                if (inflated_end <= buffer.length - start) {
                    System.arraycopy(this.inflated_buf, 0, buffer, start, inflated_end);
                } else {
                    byte[] foo = new byte[inflated_end + start];
                    System.arraycopy(buffer, 0, foo, 0, start);
                    System.arraycopy(this.inflated_buf, 0, foo, start, inflated_end);
                    buffer = foo;
                }
                length[0] = inflated_end;
                return buffer;
            } else if (status == 0) {
                if (this.inflated_buf.length < (inflated_end + 4096) - this.stream.avail_out) {
                    int len = this.inflated_buf.length * 2;
                    if (len < (inflated_end + 4096) - this.stream.avail_out) {
                        len = (inflated_end + 4096) - this.stream.avail_out;
                    }
                    byte[] foo2 = new byte[len];
                    System.arraycopy(this.inflated_buf, 0, foo2, 0, inflated_end);
                    this.inflated_buf = foo2;
                }
                System.arraycopy(this.tmpbuf, 0, this.inflated_buf, inflated_end, 4096 - this.stream.avail_out);
                inflated_end += 4096 - this.stream.avail_out;
                length[0] = inflated_end;
            } else {
                PrintStream printStream = System.err;
                printStream.println("uncompress: inflate returnd " + status);
                return null;
            }
        }
    }
}
