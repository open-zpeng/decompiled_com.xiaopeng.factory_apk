package cn.hutool.core.lang;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.util.Date;
/* loaded from: classes.dex */
public class Snowflake implements Serializable {
    private static final long DATA_CENTER_ID_BITS = 5;
    private static final long DATA_CENTER_ID_SHIFT = 17;
    private static final long MAX_DATA_CENTER_ID = 31;
    private static final long MAX_WORKER_ID = 31;
    private static final long SEQUENCE_BITS = 12;
    private static final long SEQUENCE_MASK = 4095;
    private static final long TIMESTAMP_LEFT_SHIFT = 22;
    private static final long WORKER_ID_BITS = 5;
    private static final long WORKER_ID_SHIFT = 12;
    private static final long serialVersionUID = 1;
    private final long dataCenterId;
    private long lastTimestamp;
    private long sequence;
    private final long timeOffset;
    private final long twepoch;
    private final boolean useSystemClock;
    private final long workerId;
    public static long DEFAULT_TWEPOCH = 1288834974657L;
    public static long DEFAULT_TIME_OFFSET = 2000;

    public Snowflake() {
        this(IdUtil.getWorkerId(IdUtil.getDataCenterId(31L), 31L));
    }

    public Snowflake(long workerId) {
        this(workerId, IdUtil.getDataCenterId(31L));
    }

    public Snowflake(long workerId, long dataCenterId) {
        this(workerId, dataCenterId, false);
    }

    public Snowflake(long workerId, long dataCenterId, boolean isUseSystemClock) {
        this(null, workerId, dataCenterId, isUseSystemClock);
    }

    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, DEFAULT_TIME_OFFSET);
    }

    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock, long timeOffset) {
        this.sequence = 0L;
        this.lastTimestamp = -1L;
        if (epochDate != null) {
            this.twepoch = epochDate.getTime();
        } else {
            this.twepoch = DEFAULT_TWEPOCH;
        }
        if (workerId > 31 || workerId < 0) {
            throw new IllegalArgumentException(StrUtil.format("worker Id can't be greater than {} or less than 0", 31L));
        }
        if (dataCenterId > 31 || dataCenterId < 0) {
            throw new IllegalArgumentException(StrUtil.format("datacenter Id can't be greater than {} or less than 0", 31L));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.useSystemClock = isUseSystemClock;
        this.timeOffset = timeOffset;
    }

    public long getWorkerId(long id) {
        return (id >> 12) & 31;
    }

    public long getDataCenterId(long id) {
        return (id >> DATA_CENTER_ID_SHIFT) & 31;
    }

    public long getGenerateDateTime(long id) {
        return ((id >> TIMESTAMP_LEFT_SHIFT) & 2199023255551L) + this.twepoch;
    }

    public synchronized long nextId() {
        long timestamp;
        timestamp = genTime();
        if (timestamp < this.lastTimestamp) {
            if (this.lastTimestamp - timestamp < this.timeOffset) {
                timestamp = this.lastTimestamp;
            } else {
                throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", Long.valueOf(this.lastTimestamp - timestamp)));
            }
        }
        if (timestamp == this.lastTimestamp) {
            long sequence = (this.sequence + serialVersionUID) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(this.lastTimestamp);
            }
            this.sequence = sequence;
        } else {
            this.sequence = 0L;
        }
        this.lastTimestamp = timestamp;
        return ((timestamp - this.twepoch) << TIMESTAMP_LEFT_SHIFT) | (this.dataCenterId << DATA_CENTER_ID_SHIFT) | (this.workerId << 12) | this.sequence;
    }

    public String nextIdStr() {
        return Long.toString(nextId());
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        while (timestamp == lastTimestamp) {
            timestamp = genTime();
        }
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", Long.valueOf(lastTimestamp - timestamp)));
        }
        return timestamp;
    }

    private long genTime() {
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }
}
