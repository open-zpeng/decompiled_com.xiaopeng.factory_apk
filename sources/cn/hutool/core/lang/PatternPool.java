package cn.hutool.core.lang;

import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class PatternPool {
    public static final Pattern GENERAL = Pattern.compile(RegexPool.GENERAL);
    public static final Pattern NUMBERS = Pattern.compile(RegexPool.NUMBERS);
    public static final Pattern WORD = Pattern.compile(RegexPool.WORD);
    public static final Pattern CHINESE = Pattern.compile("[一-\u9fff]");
    public static final Pattern CHINESES = Pattern.compile("[一-\u9fff]+");
    public static final Pattern GROUP_VAR = Pattern.compile(RegexPool.GROUP_VAR);
    public static final Pattern IPV4 = Pattern.compile(RegexPool.IPV4);
    public static final Pattern IPV6 = Pattern.compile(RegexPool.IPV6);
    public static final Pattern MONEY = Pattern.compile(RegexPool.MONEY);
    public static final Pattern EMAIL = Pattern.compile(RegexPool.EMAIL, 2);
    public static final Pattern MOBILE = Pattern.compile(RegexPool.MOBILE);
    public static final Pattern MOBILE_HK = Pattern.compile(RegexPool.MOBILE_HK);
    public static final Pattern MOBILE_TW = Pattern.compile(RegexPool.MOBILE_TW);
    public static final Pattern MOBILE_MO = Pattern.compile(RegexPool.MOBILE_MO);
    public static final Pattern TEL = Pattern.compile(RegexPool.TEL);
    public static final Pattern TEL_400_800 = Pattern.compile(RegexPool.TEL_400_800);
    public static final Pattern CITIZEN_ID = Pattern.compile(RegexPool.CITIZEN_ID);
    public static final Pattern ZIP_CODE = Pattern.compile(RegexPool.ZIP_CODE);
    public static final Pattern BIRTHDAY = Pattern.compile(RegexPool.BIRTHDAY);
    public static final Pattern URL = Pattern.compile(RegexPool.URL);
    public static final Pattern URL_HTTP = Pattern.compile(RegexPool.URL_HTTP, 2);
    public static final Pattern GENERAL_WITH_CHINESE = Pattern.compile(RegexPool.GENERAL_WITH_CHINESE);
    public static final Pattern UUID = Pattern.compile(RegexPool.UUID, 2);
    public static final Pattern UUID_SIMPLE = Pattern.compile(RegexPool.UUID_SIMPLE);
    public static final Pattern MAC_ADDRESS = Pattern.compile(RegexPool.MAC_ADDRESS, 2);
    public static final Pattern HEX = Pattern.compile(RegexPool.HEX);
    public static final Pattern TIME = Pattern.compile(RegexPool.TIME);
    public static final Pattern PLATE_NUMBER = Pattern.compile(RegexPool.PLATE_NUMBER);
    public static final Pattern CREDIT_CODE = Pattern.compile(RegexPool.CREDIT_CODE);
    public static final Pattern CAR_VIN = Pattern.compile(RegexPool.CAR_VIN);
    public static final Pattern CAR_DRIVING_LICENCE = Pattern.compile(RegexPool.CAR_DRIVING_LICENCE);
    private static final SimpleCache<RegexWithFlag, Pattern> POOL = new SimpleCache<>();

    public static Pattern get(String regex) {
        return get(regex, 0);
    }

    public static Pattern get(String regex, int flags) {
        RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);
        Pattern pattern = POOL.get(regexWithFlag);
        if (pattern == null) {
            Pattern pattern2 = Pattern.compile(regex, flags);
            POOL.put(regexWithFlag, pattern2);
            return pattern2;
        }
        return pattern;
    }

    public static Pattern remove(String regex, int flags) {
        return POOL.remove(new RegexWithFlag(regex, flags));
    }

    public static void clear() {
        POOL.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class RegexWithFlag {
        private final int flag;
        private final String regex;

        public RegexWithFlag(String regex, int flag) {
            this.regex = regex;
            this.flag = flag;
        }

        public int hashCode() {
            int result = (1 * 31) + this.flag;
            int result2 = result * 31;
            String str = this.regex;
            return result2 + (str == null ? 0 : str.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            RegexWithFlag other = (RegexWithFlag) obj;
            if (this.flag != other.flag) {
                return false;
            }
            String str = this.regex;
            if (str == null) {
                if (other.regex == null) {
                    return true;
                }
                return false;
            }
            return str.equals(other.regex);
        }
    }
}
