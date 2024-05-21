package ch.ethz.ssh2.packets;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class TypesWriter {
    byte[] arr = new byte[256];
    int pos = 0;

    private void resize(int len) {
        byte[] new_arr = new byte[len];
        byte[] bArr = this.arr;
        System.arraycopy(bArr, 0, new_arr, 0, bArr.length);
        this.arr = new_arr;
    }

    public int length() {
        return this.pos;
    }

    public byte[] getBytes() {
        int i = this.pos;
        byte[] dst = new byte[i];
        System.arraycopy(this.arr, 0, dst, 0, i);
        return dst;
    }

    public void getBytes(byte[] dst) {
        System.arraycopy(this.arr, 0, dst, 0, this.pos);
    }

    public void writeUINT32(int val, int off) {
        if (off + 4 > this.arr.length) {
            resize(off + 32);
        }
        byte[] bArr = this.arr;
        int off2 = off + 1;
        bArr[off] = (byte) (val >> 24);
        int off3 = off2 + 1;
        bArr[off2] = (byte) (val >> 16);
        int off4 = off3 + 1;
        bArr[off3] = (byte) (val >> 8);
        int i = off4 + 1;
        bArr[off4] = (byte) val;
    }

    public void writeUINT32(int val) {
        writeUINT32(val, this.pos);
        this.pos += 4;
    }

    public void writeUINT64(long val) {
        int i = this.pos + 8;
        byte[] bArr = this.arr;
        if (i > bArr.length) {
            resize(bArr.length + 32);
        }
        byte[] bArr2 = this.arr;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr2[i2] = (byte) (val >> 56);
        int i3 = this.pos;
        this.pos = i3 + 1;
        bArr2[i3] = (byte) (val >> 48);
        int i4 = this.pos;
        this.pos = i4 + 1;
        bArr2[i4] = (byte) (val >> 40);
        int i5 = this.pos;
        this.pos = i5 + 1;
        bArr2[i5] = (byte) (val >> 32);
        int i6 = this.pos;
        this.pos = i6 + 1;
        bArr2[i6] = (byte) (val >> 24);
        int i7 = this.pos;
        this.pos = i7 + 1;
        bArr2[i7] = (byte) (val >> 16);
        int i8 = this.pos;
        this.pos = i8 + 1;
        bArr2[i8] = (byte) (val >> 8);
        int i9 = this.pos;
        this.pos = i9 + 1;
        bArr2[i9] = (byte) val;
    }

    public void writeBoolean(boolean v) {
        int i = this.pos + 1;
        byte[] bArr = this.arr;
        if (i > bArr.length) {
            resize(bArr.length + 32);
        }
        byte[] bArr2 = this.arr;
        int i2 = this.pos;
        this.pos = i2 + 1;
        bArr2[i2] = v ? (byte) 1 : (byte) 0;
    }

    public void writeByte(int v, int off) {
        if (off + 1 > this.arr.length) {
            resize(off + 32);
        }
        this.arr[off] = (byte) v;
    }

    public void writeByte(int v) {
        writeByte(v, this.pos);
        this.pos++;
    }

    public void writeMPInt(BigInteger b) {
        byte[] raw = b.toByteArray();
        if (raw.length == 1 && raw[0] == 0) {
            writeUINT32(0);
        } else {
            writeString(raw, 0, raw.length);
        }
    }

    public void writeBytes(byte[] buff) {
        writeBytes(buff, 0, buff.length);
    }

    public void writeBytes(byte[] buff, int off, int len) {
        int i = this.pos + len;
        byte[] bArr = this.arr;
        if (i > bArr.length) {
            resize(bArr.length + len + 32);
        }
        System.arraycopy(buff, off, this.arr, this.pos, len);
        this.pos += len;
    }

    public void writeString(byte[] buff, int off, int len) {
        writeUINT32(len);
        writeBytes(buff, off, len);
    }

    public void writeString(String v) {
        byte[] b = v.getBytes();
        writeUINT32(b.length);
        writeBytes(b, 0, b.length);
    }

    public void writeString(String v, String charsetName) throws UnsupportedEncodingException {
        byte[] b = charsetName == null ? v.getBytes() : v.getBytes(charsetName);
        writeUINT32(b.length);
        writeBytes(b, 0, b.length);
    }

    public void writeNameList(String[] v) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < v.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(v[i]);
        }
        writeString(sb.toString());
    }
}
