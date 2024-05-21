package ch.ethz.ssh2.crypto.digest;

import java.math.BigInteger;
/* loaded from: classes.dex */
public class HashForSSH2Types {
    Digest md;

    public HashForSSH2Types(Digest md) {
        this.md = md;
    }

    public HashForSSH2Types(String type) {
        if (type.equals("SHA1")) {
            this.md = new SHA1();
        } else if (type.equals("MD5")) {
            this.md = new MD5();
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unknown algorithm ");
            stringBuffer.append(type);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
    }

    public void updateByte(byte b) {
        byte[] tmp = {b};
        this.md.update(tmp);
    }

    public void updateBytes(byte[] b) {
        this.md.update(b);
    }

    public void updateUINT32(int v) {
        this.md.update((byte) (v >> 24));
        this.md.update((byte) (v >> 16));
        this.md.update((byte) (v >> 8));
        this.md.update((byte) v);
    }

    public void updateByteString(byte[] b) {
        updateUINT32(b.length);
        updateBytes(b);
    }

    public void updateBigInt(BigInteger b) {
        updateByteString(b.toByteArray());
    }

    public void reset() {
        this.md.reset();
    }

    public int getDigestLength() {
        return this.md.getDigestLength();
    }

    public byte[] getDigest() {
        byte[] tmp = new byte[this.md.getDigestLength()];
        getDigest(tmp);
        return tmp;
    }

    public void getDigest(byte[] out) {
        getDigest(out, 0);
    }

    public void getDigest(byte[] out, int off) {
        this.md.digest(out, off);
    }
}
