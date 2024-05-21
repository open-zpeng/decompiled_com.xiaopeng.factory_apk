package cn.hutool.core.io.unit;

import cn.hutool.core.util.StrUtil;
import com.xiaopeng.commonfunc.Constant;
/* loaded from: classes.dex */
public enum DataUnit {
    BYTES("B", DataSize.ofBytes(1)),
    KILOBYTES("KB", DataSize.ofKilobytes(1)),
    MEGABYTES(Constant.MEGABYTE, DataSize.ofMegabytes(1)),
    GIGABYTES("GB", DataSize.ofGigabytes(1)),
    TERABYTES("TB", DataSize.ofTerabytes(1));
    
    public static final String[] UNIT_NAMES = {"B", "KB", Constant.MEGABYTE, "GB", "TB", "PB", "EB"};
    private final DataSize size;
    private final String suffix;

    DataUnit(String suffix, DataSize size) {
        this.suffix = suffix;
        this.size = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataSize size() {
        return this.size;
    }

    public static DataUnit fromSuffix(String suffix) {
        DataUnit[] values;
        for (DataUnit candidate : values()) {
            if (StrUtil.startWithIgnoreCase(candidate.suffix, suffix)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
    }
}
