package cn.hutool.core.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.net.NetUtil;
/* loaded from: classes.dex */
public class IdUtil {
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    public static String objectId() {
        return ObjectId.next();
    }

    @Deprecated
    public static Snowflake createSnowflake(long workerId, long datacenterId) {
        return new Snowflake(workerId, datacenterId);
    }

    public static Snowflake getSnowflake(long workerId, long datacenterId) {
        return (Snowflake) Singleton.get(Snowflake.class, Long.valueOf(workerId), Long.valueOf(datacenterId));
    }

    public static Snowflake getSnowflake(long workerId) {
        return (Snowflake) Singleton.get(Snowflake.class, Long.valueOf(workerId));
    }

    public static Snowflake getSnowflake() {
        return (Snowflake) Singleton.get(Snowflake.class, new Object[0]);
    }

    public static long getDataCenterId(long maxDatacenterId) {
        byte[] mac = NetUtil.getLocalHardwareAddress();
        if (mac == null) {
            return 1L;
        }
        long id = ((255 & mac[mac.length - 2]) | (65280 & (mac[mac.length - 1] << 8))) >> 6;
        return id % (1 + maxDatacenterId);
    }

    public static long getWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        try {
            mpid.append(RuntimeUtil.getPid());
        } catch (UtilException e) {
        }
        return (mpid.toString().hashCode() & 65535) % (1 + maxWorkerId);
    }

    public static String nanoId() {
        return NanoId.randomNanoId();
    }

    public static String nanoId(int size) {
        return NanoId.randomNanoId(size);
    }
}
