package ch.ethz.ssh2.packets;

import ch.ethz.ssh2.util.Tokenizer;
import java.io.IOException;
import java.math.BigInteger;
import net.lingala.zip4j.util.InternalZipConstants;
/* loaded from: classes.dex */
public class TypesReader {
    byte[] arr;
    int max;
    int pos;

    public TypesReader(byte[] arr) {
        this.pos = 0;
        this.max = 0;
        this.arr = arr;
        this.pos = 0;
        this.max = arr.length;
    }

    public TypesReader(byte[] arr, int off) {
        this.pos = 0;
        this.max = 0;
        this.arr = arr;
        this.pos = off;
        this.max = arr.length;
        int i = this.pos;
        if (i < 0 || i > arr.length) {
            throw new IllegalArgumentException("Illegal offset.");
        }
    }

    public TypesReader(byte[] arr, int off, int len) {
        this.pos = 0;
        this.max = 0;
        this.arr = arr;
        this.pos = off;
        this.max = off + len;
        int i = this.pos;
        if (i < 0 || i > arr.length) {
            throw new IllegalArgumentException("Illegal offset.");
        }
        int i2 = this.max;
        if (i2 < 0 || i2 > arr.length) {
            throw new IllegalArgumentException("Illegal length.");
        }
    }

    public int readByte() throws IOException {
        int i = this.pos;
        if (i >= this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    public byte[] readBytes(int len) throws IOException {
        int i = this.pos;
        if (i + len > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] res = new byte[len];
        System.arraycopy(this.arr, i, res, 0, len);
        this.pos += len;
        return res;
    }

    public void readBytes(byte[] dst, int off, int len) throws IOException {
        int i = this.pos;
        if (i + len > this.max) {
            throw new IOException("Packet too short.");
        }
        System.arraycopy(this.arr, i, dst, off, len);
        this.pos += len;
    }

    public boolean readBoolean() throws IOException {
        int i = this.pos;
        if (i >= this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        this.pos = i + 1;
        return bArr[i] != 0;
    }

    public int readUINT32() throws IOException {
        int i = this.pos;
        if (i + 4 > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        this.pos = i + 1;
        int i2 = this.pos;
        this.pos = i2 + 1;
        int i3 = ((bArr[i] & 255) << 24) | ((bArr[i2] & 255) << 16);
        int i4 = this.pos;
        this.pos = i4 + 1;
        int i5 = i3 | ((bArr[i4] & 255) << 8);
        int i6 = this.pos;
        this.pos = i6 + 1;
        return i5 | (bArr[i6] & 255);
    }

    public long readUINT64() throws IOException {
        int i = this.pos;
        if (i + 8 > this.max) {
            throw new IOException("Packet too short.");
        }
        byte[] bArr = this.arr;
        this.pos = i + 1;
        int i2 = this.pos;
        this.pos = i2 + 1;
        int i3 = ((bArr[i] & 255) << 24) | ((bArr[i2] & 255) << 16);
        int i4 = this.pos;
        this.pos = i4 + 1;
        int i5 = i3 | ((bArr[i4] & 255) << 8);
        int i6 = this.pos;
        this.pos = i6 + 1;
        long high = i5 | (bArr[i6] & 255);
        int i7 = this.pos;
        this.pos = i7 + 1;
        int i8 = this.pos;
        this.pos = i8 + 1;
        int i9 = ((bArr[i7] & 255) << 24) | ((bArr[i8] & 255) << 16);
        int i10 = this.pos;
        this.pos = i10 + 1;
        int i11 = i9 | ((bArr[i10] & 255) << 8);
        int i12 = this.pos;
        this.pos = i12 + 1;
        long low = i11 | (bArr[i12] & 255);
        return (high << 32) | (InternalZipConstants.ZIP_64_SIZE_LIMIT & low);
    }

    public BigInteger readMPINT() throws IOException {
        byte[] raw = readByteString();
        if (raw.length == 0) {
            BigInteger b = BigInteger.ZERO;
            return b;
        }
        BigInteger b2 = new BigInteger(raw);
        return b2;
    }

    public byte[] readByteString() throws IOException {
        int len = readUINT32();
        int i = this.pos;
        if (len + i > this.max) {
            throw new IOException("Malformed SSH byte string.");
        }
        byte[] res = new byte[len];
        System.arraycopy(this.arr, i, res, 0, len);
        this.pos += len;
        return res;
    }

    /* JADX WARN: Type inference failed for: r2v5, types: [int, java.lang.String] */
    public String readString(String charsetName) throws IOException {
        int len = readUINT32();
        int i = this.pos;
        if (len + i > this.max) {
            throw new IOException("Malformed SSH string.");
        }
        byte[] bArr = this.arr;
        if (charsetName == null) {
            new String(bArr, i, len);
        } else {
            new String(bArr, i, len, charsetName);
        }
        ?? r2 = this.pos + len;
        this.pos = r2;
        return r2;
    }

    public String readString() throws IOException {
        int len = readUINT32();
        int i = this.pos;
        if (len + i > this.max) {
            throw new IOException("Malformed SSH string.");
        }
        String res = new String(this.arr, i, len);
        this.pos += len;
        return res;
    }

    public String[] readNameList() throws IOException {
        return Tokenizer.parseTokens(readString(), ',');
    }

    public int remain() {
        return this.max - this.pos;
    }
}
