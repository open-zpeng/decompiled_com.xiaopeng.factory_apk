package cn.hutool.core.io;
/* loaded from: classes.dex */
public class FastByteBuffer {
    private byte[][] buffers;
    private int buffersCount;
    private byte[] currentBuffer;
    private int currentBufferIndex;
    private final int minChunkLen;
    private int offset;
    private int size;

    public FastByteBuffer() {
        this(1024);
    }

    public FastByteBuffer(int size) {
        this.buffers = new byte[16];
        this.currentBufferIndex = -1;
        this.minChunkLen = Math.abs(size <= 0 ? 1024 : size);
    }

    private void needNewBuffer(int newSize) {
        int delta = newSize - this.size;
        int newBufferSize = Math.max(this.minChunkLen, delta);
        this.currentBufferIndex++;
        this.currentBuffer = new byte[newBufferSize];
        this.offset = 0;
        int i = this.currentBufferIndex;
        byte[][] bArr = this.buffers;
        if (i >= bArr.length) {
            int newLen = bArr.length << 1;
            byte[][] newBuffers = new byte[newLen];
            System.arraycopy(bArr, 0, newBuffers, 0, bArr.length);
            this.buffers = newBuffers;
        }
        this.buffers[this.currentBufferIndex] = this.currentBuffer;
        this.buffersCount++;
    }

    public FastByteBuffer append(byte[] array, int off, int len) {
        int end = off + len;
        if (off < 0 || len < 0 || end > array.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return this;
        }
        int newSize = this.size + len;
        int remaining = len;
        byte[] bArr = this.currentBuffer;
        if (bArr != null) {
            int part = Math.min(remaining, bArr.length - this.offset);
            System.arraycopy(array, end - remaining, this.currentBuffer, this.offset, part);
            remaining -= part;
            this.offset += part;
            this.size += part;
        }
        if (remaining > 0) {
            needNewBuffer(newSize);
            int part2 = Math.min(remaining, this.currentBuffer.length - this.offset);
            System.arraycopy(array, end - remaining, this.currentBuffer, this.offset, part2);
            this.offset += part2;
            this.size += part2;
        }
        return this;
    }

    public FastByteBuffer append(byte[] array) {
        return append(array, 0, array.length);
    }

    public FastByteBuffer append(byte element) {
        byte[] bArr = this.currentBuffer;
        if (bArr == null || this.offset == bArr.length) {
            needNewBuffer(this.size + 1);
        }
        byte[] bArr2 = this.currentBuffer;
        int i = this.offset;
        bArr2[i] = element;
        this.offset = i + 1;
        this.size++;
        return this;
    }

    public FastByteBuffer append(FastByteBuffer buff) {
        if (buff.size == 0) {
            return this;
        }
        for (int i = 0; i < buff.currentBufferIndex; i++) {
            append(buff.buffers[i]);
        }
        append(buff.currentBuffer, 0, buff.offset);
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int index() {
        return this.currentBufferIndex;
    }

    public int offset() {
        return this.offset;
    }

    public byte[] array(int index) {
        return this.buffers[index];
    }

    public void reset() {
        this.size = 0;
        this.offset = 0;
        this.currentBufferIndex = -1;
        this.currentBuffer = null;
        this.buffersCount = 0;
    }

    public byte[] toArray() {
        int pos = 0;
        byte[] array = new byte[this.size];
        if (this.currentBufferIndex == -1) {
            return array;
        }
        int i = 0;
        while (true) {
            int i2 = this.currentBufferIndex;
            if (i < i2) {
                byte[][] bArr = this.buffers;
                int len = bArr[i].length;
                System.arraycopy(bArr[i], 0, array, pos, len);
                pos += len;
                i++;
            } else {
                System.arraycopy(this.buffers[i2], 0, array, pos, this.offset);
                return array;
            }
        }
    }

    public byte[] toArray(int start, int len) {
        int remaining = len;
        int pos = 0;
        byte[] array = new byte[len];
        if (len == 0) {
            return array;
        }
        int i = 0;
        while (true) {
            byte[][] bArr = this.buffers;
            if (start < bArr[i].length) {
                break;
            }
            start -= bArr[i].length;
            i++;
        }
        while (i < this.buffersCount) {
            byte[] buf = this.buffers[i];
            int c = Math.min(buf.length - start, remaining);
            System.arraycopy(buf, start, array, pos, c);
            pos += c;
            remaining -= c;
            if (remaining == 0) {
                break;
            }
            start = 0;
            i++;
        }
        return array;
    }

    public byte get(int index) {
        if (index >= this.size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        int ndx = 0;
        while (true) {
            byte[] b = this.buffers[ndx];
            if (index < b.length) {
                return b[index];
            }
            ndx++;
            index -= b.length;
        }
    }
}
