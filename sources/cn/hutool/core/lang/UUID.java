package cn.hutool.core.lang;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
/* loaded from: classes.dex */
public class UUID implements Serializable, Comparable<UUID> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = -1185015143654744140L;
    private final long leastSigBits;
    private final long mostSigBits;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Holder {
        static final SecureRandom NUMBER_GENERATOR = RandomUtil.getSecureRandom();

        private Holder() {
        }
    }

    private UUID(byte[] data) {
        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (data[i] & 255);
        }
        for (int i2 = 8; i2 < 16; i2++) {
            lsb = (lsb << 8) | (data[i2] & 255);
        }
        this.mostSigBits = msb;
        this.leastSigBits = lsb;
    }

    public UUID(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    public static UUID fastUUID() {
        return randomUUID(false);
    }

    public static UUID randomUUID() {
        return randomUUID(true);
    }

    public static UUID randomUUID(boolean isSecure) {
        Random ng = isSecure ? Holder.NUMBER_GENERATOR : RandomUtil.getRandom();
        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] = (byte) (randomBytes[6] & 15);
        randomBytes[6] = (byte) (randomBytes[6] | 64);
        randomBytes[8] = (byte) (randomBytes[8] & 63);
        randomBytes[8] = (byte) (randomBytes[8] | 128);
        return new UUID(randomBytes);
    }

    public static UUID nameUUIDFromBytes(byte[] name) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(name);
            md5Bytes[6] = (byte) (md5Bytes[6] & 15);
            md5Bytes[6] = (byte) (md5Bytes[6] | 48);
            md5Bytes[8] = (byte) (md5Bytes[8] & 63);
            md5Bytes[8] = (byte) (md5Bytes[8] | 128);
            return new UUID(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError("MD5 not supported");
        }
    }

    public static UUID fromString(String name) {
        String[] components = name.split("-");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid UUID string: " + name);
        }
        for (int i = 0; i < 5; i++) {
            components[i] = "0x" + components[i];
        }
        long mostSigBits = Long.decode(components[0]).longValue();
        long mostSigBits2 = (((mostSigBits << 16) | Long.decode(components[1]).longValue()) << 16) | Long.decode(components[2]).longValue();
        long leastSigBits = Long.decode(components[3]).longValue();
        return new UUID(mostSigBits2, (leastSigBits << 48) | Long.decode(components[4]).longValue());
    }

    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }

    public long getMostSignificantBits() {
        return this.mostSigBits;
    }

    public int version() {
        return (int) ((this.mostSigBits >> 12) & 15);
    }

    public int variant() {
        long j = this.leastSigBits;
        return (int) ((j >> 63) & (j >>> ((int) (64 - (j >>> 62)))));
    }

    public long timestamp() throws UnsupportedOperationException {
        checkTimeBase();
        long j = this.mostSigBits;
        return (j >>> 32) | ((4095 & j) << 48) | (((j >> 16) & 65535) << 32);
    }

    public int clockSequence() throws UnsupportedOperationException {
        checkTimeBase();
        return (int) ((this.leastSigBits & 4611404543450677248L) >>> 48);
    }

    public long node() throws UnsupportedOperationException {
        checkTimeBase();
        return this.leastSigBits & 281474976710655L;
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean isSimple) {
        StringBuilder builder = StrUtil.builder(isSimple ? 32 : 36);
        builder.append(digits(this.mostSigBits >> 32, 8));
        if (!isSimple) {
            builder.append(CharPool.DASHED);
        }
        builder.append(digits(this.mostSigBits >> 16, 4));
        if (!isSimple) {
            builder.append(CharPool.DASHED);
        }
        builder.append(digits(this.mostSigBits, 4));
        if (!isSimple) {
            builder.append(CharPool.DASHED);
        }
        builder.append(digits(this.leastSigBits >> 48, 4));
        if (!isSimple) {
            builder.append(CharPool.DASHED);
        }
        builder.append(digits(this.leastSigBits, 12));
        return builder.toString();
    }

    public int hashCode() {
        long hilo = this.mostSigBits ^ this.leastSigBits;
        return ((int) (hilo >> 32)) ^ ((int) hilo);
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != UUID.class) {
            return false;
        }
        UUID id = (UUID) obj;
        return this.mostSigBits == id.mostSigBits && this.leastSigBits == id.leastSigBits;
    }

    @Override // java.lang.Comparable
    public int compareTo(UUID val) {
        int compare = Long.compare(this.mostSigBits, val.mostSigBits);
        if (compare == 0) {
            return Long.compare(this.leastSigBits, val.leastSigBits);
        }
        return compare;
    }

    private static String digits(long val, int digits) {
        long hi = 1 << (digits * 4);
        return Long.toHexString(((hi - 1) & val) | hi).substring(1);
    }

    private void checkTimeBase() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
    }
}
