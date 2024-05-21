package cn.hutool.core.io.unit;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final class DataSize implements Comparable<DataSize> {
    private static final long BYTES_PER_GB = 1073741824;
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = 1048576;
    private static final long BYTES_PER_TB = 1099511627776L;
    private static final Pattern PATTERN = Pattern.compile("^([+-]?\\d+(\\.\\d+)?)([a-zA-Z]{0,2})$");
    private final long bytes;

    private DataSize(long bytes) {
        this.bytes = bytes;
    }

    public static DataSize ofBytes(long bytes) {
        return new DataSize(bytes);
    }

    public static DataSize ofKilobytes(long kilobytes) {
        return new DataSize(Math.multiplyExact(kilobytes, 1024L));
    }

    public static DataSize ofMegabytes(long megabytes) {
        return new DataSize(Math.multiplyExact(megabytes, 1048576L));
    }

    public static DataSize ofGigabytes(long gigabytes) {
        return new DataSize(Math.multiplyExact(gigabytes, 1073741824L));
    }

    public static DataSize ofTerabytes(long terabytes) {
        return new DataSize(Math.multiplyExact(terabytes, (long) BYTES_PER_TB));
    }

    public static DataSize of(long amount, DataUnit unit) {
        if (unit == null) {
            unit = DataUnit.BYTES;
        }
        return new DataSize(Math.multiplyExact(amount, unit.size().toBytes()));
    }

    public static DataSize of(BigDecimal amount, DataUnit unit) {
        if (unit == null) {
            unit = DataUnit.BYTES;
        }
        return new DataSize(amount.multiply(new BigDecimal(unit.size().toBytes())).longValue());
    }

    public static DataSize parse(CharSequence text) {
        return parse(text, null);
    }

    public static DataSize parse(CharSequence text, DataUnit defaultUnit) {
        Assert.notNull(text, "Text must not be null", new Object[0]);
        try {
            Matcher matcher = PATTERN.matcher(text);
            Assert.state(matcher.matches(), "Does not match data size pattern", new Object[0]);
            DataUnit unit = determineDataUnit(matcher.group(3), defaultUnit);
            return of(new BigDecimal(matcher.group(1)), unit);
        } catch (Exception ex) {
            throw new IllegalArgumentException("'" + ((Object) text) + "' is not a valid data size", ex);
        }
    }

    private static DataUnit determineDataUnit(String suffix, DataUnit defaultUnit) {
        DataUnit defaultUnitToUse = defaultUnit != null ? defaultUnit : DataUnit.BYTES;
        return StrUtil.isNotEmpty(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse;
    }

    public boolean isNegative() {
        return this.bytes < 0;
    }

    public long toBytes() {
        return this.bytes;
    }

    public long toKilobytes() {
        return this.bytes / 1024;
    }

    public long toMegabytes() {
        return this.bytes / 1048576;
    }

    public long toGigabytes() {
        return this.bytes / 1073741824;
    }

    public long toTerabytes() {
        return this.bytes / BYTES_PER_TB;
    }

    @Override // java.lang.Comparable
    public int compareTo(DataSize other) {
        return Long.compare(this.bytes, other.bytes);
    }

    public String toString() {
        return String.format("%dB", Long.valueOf(this.bytes));
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        DataSize otherSize = (DataSize) other;
        if (this.bytes == otherSize.bytes) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Long.hashCode(this.bytes);
    }
}
