package com.jcraft.jsch;
/* loaded from: classes.dex */
public class Packet {
    private static Random random = null;
    byte[] ba4 = new byte[4];
    Buffer buffer;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setRandom(Random foo) {
        random = foo;
    }

    public Packet(Buffer buffer) {
        this.buffer = buffer;
    }

    public void reset() {
        this.buffer.index = 5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void padding(int bsize) {
        int len = this.buffer.index;
        int pad = (-len) & (bsize - 1);
        if (pad < bsize) {
            pad += bsize;
        }
        int len2 = (len + pad) - 4;
        byte[] bArr = this.ba4;
        bArr[0] = (byte) (len2 >>> 24);
        bArr[1] = (byte) (len2 >>> 16);
        bArr[2] = (byte) (len2 >>> 8);
        bArr[3] = (byte) len2;
        System.arraycopy(bArr, 0, this.buffer.buffer, 0, 4);
        this.buffer.buffer[4] = (byte) pad;
        synchronized (random) {
            random.fill(this.buffer.buffer, this.buffer.index, pad);
        }
        this.buffer.skip(pad);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int shift(int len, int bsize, int mac) {
        int s = len + 5 + 9;
        int pad = (-s) & (bsize - 1);
        if (pad < bsize) {
            pad += bsize;
        }
        int s2 = s + pad + mac + 32;
        if (this.buffer.buffer.length < (((this.buffer.index + s2) - 5) - 9) - len) {
            byte[] foo = new byte[(((this.buffer.index + s2) - 5) - 9) - len];
            System.arraycopy(this.buffer.buffer, 0, foo, 0, this.buffer.buffer.length);
            this.buffer.buffer = foo;
        }
        System.arraycopy(this.buffer.buffer, len + 5 + 9, this.buffer.buffer, s2, ((this.buffer.index - 5) - 9) - len);
        Buffer buffer = this.buffer;
        buffer.index = 10;
        buffer.putInt(len);
        this.buffer.index = len + 5 + 9;
        return s2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unshift(byte command, int recipient, int s, int len) {
        System.arraycopy(this.buffer.buffer, s, this.buffer.buffer, 14, len);
        this.buffer.buffer[5] = command;
        Buffer buffer = this.buffer;
        buffer.index = 6;
        buffer.putInt(recipient);
        this.buffer.putInt(len);
        this.buffer.index = len + 5 + 9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Buffer getBuffer() {
        return this.buffer;
    }
}
