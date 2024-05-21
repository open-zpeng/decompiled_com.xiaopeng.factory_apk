package ch.ethz.ssh2.crypto;

import java.io.IOException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class SimpleDERReader {
    byte[] buffer;
    int count;
    int pos;

    public SimpleDERReader(byte[] b) {
        resetInput(b);
    }

    public SimpleDERReader(byte[] b, int off, int len) {
        resetInput(b, off, len);
    }

    public void resetInput(byte[] b) {
        resetInput(b, 0, b.length);
    }

    public void resetInput(byte[] b, int off, int len) {
        this.buffer = b;
        this.pos = off;
        this.count = len;
    }

    private byte readByte() throws IOException {
        int i = this.count;
        if (i <= 0) {
            throw new IOException("DER byte array: out of data");
        }
        this.count = i - 1;
        byte[] bArr = this.buffer;
        int i2 = this.pos;
        this.pos = i2 + 1;
        return bArr[i2];
    }

    private byte[] readBytes(int len) throws IOException {
        if (len > this.count) {
            throw new IOException("DER byte array: out of data");
        }
        byte[] b = new byte[len];
        System.arraycopy(this.buffer, this.pos, b, 0, len);
        this.pos += len;
        this.count -= len;
        return b;
    }

    public int available() {
        return this.count;
    }

    private int readLength() throws IOException {
        int len = readByte() & 255;
        if ((len & 128) == 0) {
            return len;
        }
        int remain = len & 127;
        if (remain == 0) {
            return -1;
        }
        int len2 = 0;
        while (remain > 0) {
            len2 = (len2 << 8) | (readByte() & 255);
            remain--;
        }
        return len2;
    }

    public int ignoreNextObject() throws IOException {
        int type = readByte() & 255;
        int len = readLength();
        if (len < 0 || len > available()) {
            StringBuffer stringBuffer = new StringBuffer("Illegal len in DER object (");
            stringBuffer.append(len);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        readBytes(len);
        return type;
    }

    public BigInteger readInt() throws IOException {
        int type = readByte() & 255;
        if (type != 2) {
            StringBuffer stringBuffer = new StringBuffer("Expected DER Integer, but found type ");
            stringBuffer.append(type);
            throw new IOException(stringBuffer.toString());
        }
        int len = readLength();
        if (len < 0 || len > available()) {
            StringBuffer stringBuffer2 = new StringBuffer("Illegal len in DER object (");
            stringBuffer2.append(len);
            stringBuffer2.append(")");
            throw new IOException(stringBuffer2.toString());
        }
        byte[] b = readBytes(len);
        BigInteger bi = new BigInteger(b);
        return bi;
    }

    public byte[] readSequenceAsByteArray() throws IOException {
        int type = readByte() & 255;
        if (type != 48) {
            StringBuffer stringBuffer = new StringBuffer("Expected DER Sequence, but found type ");
            stringBuffer.append(type);
            throw new IOException(stringBuffer.toString());
        }
        int len = readLength();
        if (len < 0 || len > available()) {
            StringBuffer stringBuffer2 = new StringBuffer("Illegal len in DER object (");
            stringBuffer2.append(len);
            stringBuffer2.append(")");
            throw new IOException(stringBuffer2.toString());
        }
        byte[] b = readBytes(len);
        return b;
    }

    public byte[] readOctetString() throws IOException {
        int type = readByte() & 255;
        if (type != 4) {
            StringBuffer stringBuffer = new StringBuffer("Expected DER Octetstring, but found type ");
            stringBuffer.append(type);
            throw new IOException(stringBuffer.toString());
        }
        int len = readLength();
        if (len < 0 || len > available()) {
            StringBuffer stringBuffer2 = new StringBuffer("Illegal len in DER object (");
            stringBuffer2.append(len);
            stringBuffer2.append(")");
            throw new IOException(stringBuffer2.toString());
        }
        byte[] b = readBytes(len);
        return b;
    }
}
