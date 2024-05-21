package cn.hutool.crypto.digest.otp;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.xiaopeng.lib.framework.netchannelmodule.common.TrafficStatsEntry;
/* loaded from: classes.dex */
public class HOTP {
    public static final int DEFAULT_PASSWORD_LENGTH = 6;
    private final byte[] buffer;
    private final HMac mac;
    private final int modDivisor;
    private final int passwordLength;
    private static final int[] MOD_DIVISORS = {1, 10, 100, 1000, 10000, TrafficStatsEntry.FIRST_NETWORK_UID, 1000000, 10000000, 100000000};
    public static final HmacAlgorithm HOTP_HMAC_ALGORITHM = HmacAlgorithm.HmacSHA1;

    public HOTP(byte[] key) {
        this(6, key);
    }

    public HOTP(int passwordLength, byte[] key) {
        this(passwordLength, HOTP_HMAC_ALGORITHM, key);
    }

    public HOTP(int passwordLength, HmacAlgorithm algorithm, byte[] key) {
        if (passwordLength >= MOD_DIVISORS.length) {
            throw new IllegalArgumentException("Password length must be < " + MOD_DIVISORS.length);
        }
        this.mac = new HMac(algorithm, key);
        this.modDivisor = MOD_DIVISORS[passwordLength];
        this.passwordLength = passwordLength;
        this.buffer = new byte[8];
    }

    public synchronized int generate(long counter) {
        byte[] digest;
        this.buffer[0] = (byte) (((-72057594037927936L) & counter) >>> 56);
        this.buffer[1] = (byte) ((71776119061217280L & counter) >>> 48);
        this.buffer[2] = (byte) ((280375465082880L & counter) >>> 40);
        this.buffer[3] = (byte) ((1095216660480L & counter) >>> 32);
        this.buffer[4] = (byte) ((4278190080L & counter) >>> 24);
        this.buffer[5] = (byte) ((16711680 & counter) >>> 16);
        this.buffer[6] = (byte) ((65280 & counter) >>> 8);
        this.buffer[7] = (byte) (255 & counter);
        digest = this.mac.digest(this.buffer);
        return truncate(digest);
    }

    public static String generateSecretKey(int numBytes) {
        return Base32.encode(RandomUtil.getSHA1PRNGRandom(RandomUtil.randomBytes(256)).generateSeed(numBytes));
    }

    public int getPasswordLength() {
        return this.passwordLength;
    }

    public String getAlgorithm() {
        return this.mac.getAlgorithm();
    }

    private int truncate(byte[] digest) {
        int offset = digest[digest.length - 1] & 15;
        return (((((digest[offset] & Byte.MAX_VALUE) << 24) | ((digest[offset + 1] & 255) << 16)) | ((digest[offset + 2] & 255) << 8)) | (digest[offset + 3] & 255)) % this.modDivisor;
    }
}
