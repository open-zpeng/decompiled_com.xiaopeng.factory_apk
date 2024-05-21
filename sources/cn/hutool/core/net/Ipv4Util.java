package cn.hutool.core.net;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;
/* loaded from: classes.dex */
public class Ipv4Util {
    public static final int IP_MASK_MAX = 32;
    public static final String IP_MASK_SPLIT_MARK = "/";
    public static final String IP_SPLIT_MARK = "-";

    public static String formatIpBlock(String ip, String mask) {
        return ip + "/" + getMaskBitByMask(mask);
    }

    public static List<String> list(String ipRange, boolean isAll) {
        if (ipRange.contains("-")) {
            String[] range = StrUtil.splitToArray(ipRange, "-");
            return list(range[0], range[1]);
        } else if (ipRange.contains("/")) {
            String[] param = StrUtil.splitToArray(ipRange, "/");
            return list(param[0], Integer.parseInt(param[1]), isAll);
        } else {
            return ListUtil.toList(ipRange);
        }
    }

    public static List<String> list(String ip, int maskBit, boolean isAll) {
        if (maskBit == 32) {
            List<String> list = new ArrayList<>();
            if (isAll) {
                list.add(ip);
            }
            return list;
        }
        String startIp = getBeginIpStr(ip, maskBit);
        String endIp = getEndIpStr(ip, maskBit);
        if (isAll) {
            return list(startIp, endIp);
        }
        int lastDotIndex = startIp.lastIndexOf(46) + 1;
        String startIp2 = StrUtil.subPre(startIp, lastDotIndex) + (Integer.parseInt((String) Objects.requireNonNull(StrUtil.subSuf(startIp, lastDotIndex))) + 1);
        int lastDotIndex2 = endIp.lastIndexOf(46) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append(StrUtil.subPre(endIp, lastDotIndex2));
        sb.append(Integer.parseInt((String) Objects.requireNonNull(StrUtil.subSuf(endIp, lastDotIndex2))) - 1);
        return list(startIp2, sb.toString());
    }

    public static List<String> list(String ipFrom, String ipTo) {
        int[] ipf = (int[]) Convert.convert((Class<Object>) int[].class, (Object) StrUtil.splitToArray((CharSequence) ipFrom, '.'));
        int[] ipt = (int[]) Convert.convert((Class<Object>) int[].class, (Object) StrUtil.splitToArray((CharSequence) ipTo, '.'));
        List<String> ips = new ArrayList<>();
        int a = ipf[0];
        while (a <= ipt[0]) {
            int b = a == ipf[0] ? ipf[1] : 0;
            while (true) {
                if (b <= (a == ipt[0] ? ipt[1] : 255)) {
                    int c = b == ipf[1] ? ipf[2] : 0;
                    while (true) {
                        if (c <= (b == ipt[1] ? ipt[2] : 255)) {
                            int d = c == ipf[2] ? ipf[3] : 0;
                            while (true) {
                                if (d <= (c == ipt[2] ? ipt[3] : 255)) {
                                    ips.add(a + "." + b + "." + c + "." + d);
                                    d++;
                                }
                            }
                            c++;
                        }
                    }
                    b++;
                }
            }
            a++;
        }
        return ips;
    }

    public static String longToIpv4(long longIP) {
        StringBuilder sb = StrUtil.builder();
        sb.append((longIP >> 24) & 255);
        sb.append('.');
        sb.append((longIP >> 16) & 255);
        sb.append('.');
        sb.append((longIP >> 8) & 255);
        sb.append('.');
        sb.append(longIP & 255);
        return sb.toString();
    }

    public static long ipv4ToLong(String strIP) {
        Validator.validateIpv4(strIP, "Invalid IPv4 address!");
        long[] ip = (long[]) Convert.convert((Class<Object>) long[].class, (Object) StrUtil.split((CharSequence) strIP, '.'));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String getBeginIpStr(String ip, int maskBit) {
        return longToIpv4(getBeginIpLong(ip, maskBit).longValue());
    }

    private static Long getBeginIpLong(String ip, int maskBit) {
        return Long.valueOf(ipv4ToLong(ip) & ipv4ToLong(getMaskByMaskBit(maskBit)));
    }

    public static String getEndIpStr(String ip, int maskBit) {
        return longToIpv4(getEndIpLong(ip, maskBit).longValue());
    }

    public static int getMaskBitByMask(String mask) {
        Integer maskBit = MaskBit.getMaskBit(mask);
        if (maskBit == null) {
            throw new IllegalArgumentException("Invalid netmask " + mask);
        }
        return maskBit.intValue();
    }

    public static int countByMaskBit(int maskBit, boolean isAll) {
        if (!isAll && (maskBit <= 0 || maskBit >= 32)) {
            return 0;
        }
        int count = (int) Math.pow(2.0d, 32 - maskBit);
        return isAll ? count : count - 2;
    }

    public static String getMaskByMaskBit(int maskBit) {
        return MaskBit.get(maskBit);
    }

    public static String getMaskByIpRange(String fromIp, String toIp) {
        long toIpLong = ipv4ToLong(toIp);
        long fromIpLong = ipv4ToLong(fromIp);
        Assert.isTrue(fromIpLong < toIpLong, "to IP must be greater than from IP!", new Object[0]);
        String[] fromIpSplit = StrUtil.splitToArray((CharSequence) fromIp, '.');
        String[] toIpSplit = StrUtil.splitToArray((CharSequence) toIp, '.');
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < toIpSplit.length; i++) {
            mask.append((255 - Integer.parseInt(toIpSplit[i])) + Integer.parseInt(fromIpSplit[i]));
            mask.append('.');
        }
        return mask.substring(0, mask.length() - 1);
    }

    public static int countByIpRange(String fromIp, String toIp) {
        long toIpLong = ipv4ToLong(toIp);
        long fromIpLong = ipv4ToLong(fromIp);
        if (fromIpLong <= toIpLong) {
            int count = 1;
            int[] fromIpSplit = StrUtil.split((CharSequence) fromIp, '.').stream().mapToInt(new ToIntFunction() { // from class: cn.hutool.core.net.-$$Lambda$wddj3-hVVrg0MkscpMtYt3BzY8Y
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return Integer.parseInt((String) obj);
                }
            }).toArray();
            int[] toIpSplit = StrUtil.split((CharSequence) toIp, '.').stream().mapToInt(new ToIntFunction() { // from class: cn.hutool.core.net.-$$Lambda$wddj3-hVVrg0MkscpMtYt3BzY8Y
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return Integer.parseInt((String) obj);
                }
            }).toArray();
            int i = fromIpSplit.length - 1;
            while (i >= 0) {
                count = (int) (count + ((toIpSplit[i] - fromIpSplit[i]) * Math.pow(256.0d, (fromIpSplit.length - i) - 1)));
                i--;
                toIpLong = toIpLong;
            }
            return count;
        }
        throw new IllegalArgumentException("to IP must be greater than from IP!");
    }

    public static boolean isMaskValid(String mask) {
        return MaskBit.getMaskBit(mask) != null;
    }

    public static boolean isMaskBitValid(int maskBit) {
        return MaskBit.get(maskBit) != null;
    }

    private static Long getEndIpLong(String ip, int maskBit) {
        return Long.valueOf(getBeginIpLong(ip, maskBit).longValue() + (~ipv4ToLong(getMaskByMaskBit(maskBit))));
    }
}
