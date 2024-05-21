package ch.ethz.ssh2.crypto.digest;
/* loaded from: classes.dex */
public final class MAC {
    Digest mac;
    int size;

    public static final String[] getMacList() {
        return new String[]{"hmac-sha1-96", "hmac-sha1", "hmac-md5-96", "hmac-md5"};
    }

    public static final void checkMacList(String[] macs) {
        for (String str : macs) {
            getKeyLen(str);
        }
    }

    public static final int getKeyLen(String type) {
        if (type.equals("hmac-sha1") || type.equals("hmac-sha1-96")) {
            return 20;
        }
        if (type.equals("hmac-md5") || type.equals("hmac-md5-96")) {
            return 16;
        }
        StringBuffer stringBuffer = new StringBuffer("Unkown algorithm ");
        stringBuffer.append(type);
        throw new IllegalArgumentException(stringBuffer.toString());
    }

    public MAC(String type, byte[] key) {
        if (type.equals("hmac-sha1")) {
            this.mac = new HMAC(new SHA1(), key, 20);
        } else if (type.equals("hmac-sha1-96")) {
            this.mac = new HMAC(new SHA1(), key, 12);
        } else if (type.equals("hmac-md5")) {
            this.mac = new HMAC(new MD5(), key, 16);
        } else if (type.equals("hmac-md5-96")) {
            this.mac = new HMAC(new MD5(), key, 12);
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unkown algorithm ");
            stringBuffer.append(type);
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        this.size = this.mac.getDigestLength();
    }

    public final void initMac(int seq) {
        this.mac.reset();
        this.mac.update((byte) (seq >> 24));
        this.mac.update((byte) (seq >> 16));
        this.mac.update((byte) (seq >> 8));
        this.mac.update((byte) seq);
    }

    public final void update(byte[] packetdata, int off, int len) {
        this.mac.update(packetdata, off, len);
    }

    public final void getMac(byte[] out, int off) {
        this.mac.digest(out, off);
    }

    public final int size() {
        return this.size;
    }
}
