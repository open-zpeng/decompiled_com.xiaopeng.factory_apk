package cn.hutool.core.io.unit;

import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class DataSizeUtil {
    public static long parse(String text) {
        return DataSize.parse(text).toBytes();
    }

    public static String format(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = Math.min(DataUnit.UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024.0d)));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024.0d, digitGroups)) + " " + DataUnit.UNIT_NAMES[digitGroups];
    }
}
