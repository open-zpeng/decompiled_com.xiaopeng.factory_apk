package cn.hutool.core.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharPool;
import com.xiaopeng.commonfunc.utils.AtUtil;
import com.xiaopeng.lib.framework.netchannelmodule.remotestorage.aws.UploadInfo;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.BooleanUtils;
/* loaded from: classes.dex */
public class IdcardUtil {
    private static final int CHINA_ID_MAX_LENGTH = 18;
    private static final int CHINA_ID_MIN_LENGTH = 15;
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final Map<String, String> CITY_CODES = new HashMap();
    private static final Map<Character, Integer> TW_FIRST_CODE = new HashMap();

    static {
        CITY_CODES.put("11", "北京");
        CITY_CODES.put("12", "天津");
        CITY_CODES.put("13", "河北");
        CITY_CODES.put("14", "山西");
        CITY_CODES.put("15", "内蒙古");
        CITY_CODES.put("21", "辽宁");
        CITY_CODES.put("22", "吉林");
        CITY_CODES.put("23", "黑龙江");
        CITY_CODES.put("31", "上海");
        CITY_CODES.put(AtUtil.SpkTest.DSP_VOLUME_MAX, "江苏");
        CITY_CODES.put("33", "浙江");
        CITY_CODES.put("34", "安徽");
        CITY_CODES.put("35", "福建");
        CITY_CODES.put("36", "江西");
        CITY_CODES.put("37", "山东");
        CITY_CODES.put("41", "河南");
        CITY_CODES.put("42", "湖北");
        CITY_CODES.put("43", "湖南");
        CITY_CODES.put("44", "广东");
        CITY_CODES.put("45", "广西");
        CITY_CODES.put("46", "海南");
        CITY_CODES.put("50", "重庆");
        CITY_CODES.put("51", "四川");
        CITY_CODES.put("52", "贵州");
        CITY_CODES.put("53", "云南");
        CITY_CODES.put("54", "西藏");
        CITY_CODES.put("61", "陕西");
        CITY_CODES.put("62", "甘肃");
        CITY_CODES.put("63", "青海");
        CITY_CODES.put("64", "宁夏");
        CITY_CODES.put("65", "新疆");
        CITY_CODES.put("71", "台湾");
        CITY_CODES.put(UploadInfo.UPLOAD_TYPE, "香港");
        CITY_CODES.put("82", "澳门");
        CITY_CODES.put("83", "台湾");
        CITY_CODES.put("91", "国外");
        TW_FIRST_CODE.put('A', 10);
        TW_FIRST_CODE.put('B', 11);
        TW_FIRST_CODE.put('C', 12);
        TW_FIRST_CODE.put('D', 13);
        TW_FIRST_CODE.put('E', 14);
        TW_FIRST_CODE.put('F', 15);
        TW_FIRST_CODE.put('G', 16);
        TW_FIRST_CODE.put('H', 17);
        TW_FIRST_CODE.put('J', 18);
        TW_FIRST_CODE.put('K', 19);
        TW_FIRST_CODE.put('L', 20);
        TW_FIRST_CODE.put('M', 21);
        TW_FIRST_CODE.put('N', 22);
        TW_FIRST_CODE.put('P', 23);
        TW_FIRST_CODE.put('Q', 24);
        TW_FIRST_CODE.put('R', 25);
        TW_FIRST_CODE.put('S', 26);
        TW_FIRST_CODE.put('T', 27);
        TW_FIRST_CODE.put('U', 28);
        TW_FIRST_CODE.put('V', 29);
        TW_FIRST_CODE.put('X', 30);
        TW_FIRST_CODE.put('Y', 31);
        TW_FIRST_CODE.put('W', 32);
        TW_FIRST_CODE.put('Z', 33);
        TW_FIRST_CODE.put('I', 34);
        TW_FIRST_CODE.put('O', 35);
    }

    public static String convert15To18(String idCard) {
        if (idCard.length() == 15 && ReUtil.isMatch(PatternPool.NUMBERS, idCard)) {
            String birthday = idCard.substring(6, 12);
            Date birthDate = DateUtil.parse(birthday, "yyMMdd");
            int sYear = DateUtil.year(birthDate);
            if (sYear > 2000) {
                sYear -= 100;
            }
            StringBuilder builder = StrUtil.builder();
            builder.append((CharSequence) idCard, 0, 6);
            builder.append(sYear);
            StringBuilder idCard18 = builder.append(idCard.substring(8));
            char sVal = getCheckCode18(idCard18.toString());
            idCard18.append(sVal);
            return idCard18.toString();
        }
        return null;
    }

    public static boolean isValidCard(String idCard) {
        if (StrUtil.isBlank(idCard)) {
            return false;
        }
        String idCard2 = idCard.trim();
        int length = idCard2.length();
        if (length == 10) {
            String[] cardVal = isValidCard10(idCard2);
            return cardVal != null && "true".equals(cardVal[2]);
        } else if (length != 15) {
            if (length != 18) {
                return false;
            }
            return isValidCard18(idCard2);
        } else {
            return isValidCard15(idCard2);
        }
    }

    public static boolean isValidCard18(String idcard) {
        return isValidCard18(idcard, true);
    }

    public static boolean isValidCard18(String idcard, boolean ignoreCase) {
        if (18 != idcard.length()) {
            return false;
        }
        String proCode = idcard.substring(0, 2);
        if (CITY_CODES.get(proCode) != null && Validator.isBirthday(idcard.substring(6, 14))) {
            String code17 = idcard.substring(0, 17);
            if (ReUtil.isMatch(PatternPool.NUMBERS, code17)) {
                char val = getCheckCode18(code17);
                return CharUtil.equals(val, idcard.charAt(17), ignoreCase);
            }
            return false;
        }
        return false;
    }

    public static boolean isValidCard15(String idcard) {
        if (15 == idcard.length() && ReUtil.isMatch(PatternPool.NUMBERS, idcard)) {
            String proCode = idcard.substring(0, 2);
            if (CITY_CODES.get(proCode) == null) {
                return false;
            }
            return Validator.isBirthday("19" + idcard.substring(6, 12));
        }
        return false;
    }

    public static String[] isValidCard10(String idcard) {
        if (StrUtil.isBlank(idcard)) {
            return null;
        }
        String[] info = new String[3];
        String card = idcard.replaceAll("[()]", "");
        if (card.length() == 8 || card.length() == 9 || idcard.length() == 10) {
            boolean matches = idcard.matches("^[a-zA-Z][0-9]{9}$");
            String str = BooleanUtils.FALSE;
            if (matches) {
                info[0] = "台湾";
                char char2 = idcard.charAt(1);
                if ('1' == char2) {
                    info[1] = "M";
                } else if ('2' == char2) {
                    info[1] = "F";
                } else {
                    info[1] = "N";
                    info[2] = BooleanUtils.FALSE;
                    return info;
                }
                if (isValidTWCard(idcard)) {
                    str = "true";
                }
                info[2] = str;
            } else if (idcard.matches("^[157][0-9]{6}\\(?[0-9A-Z]\\)?$")) {
                info[0] = "澳门";
                info[1] = "N";
                info[2] = "true";
            } else if (!idcard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) {
                return null;
            } else {
                info[0] = "香港";
                info[1] = "N";
                if (isValidHKCard(idcard)) {
                    str = "true";
                }
                info[2] = str;
            }
            return info;
        }
        return null;
    }

    public static boolean isValidTWCard(String idcard) {
        Integer iStart;
        if (idcard == null || idcard.length() != 10 || (iStart = TW_FIRST_CODE.get(Character.valueOf(idcard.charAt(0)))) == null) {
            return false;
        }
        int sum = (iStart.intValue() / 10) + ((iStart.intValue() % 10) * 9);
        String mid = idcard.substring(1, 9);
        char[] chars = mid.toCharArray();
        int iflag = 8;
        int iflag2 = sum;
        for (char c : chars) {
            iflag2 += Integer.parseInt(String.valueOf(c)) * iflag;
            iflag--;
        }
        String end = idcard.substring(9, 10);
        if ((iflag2 % 10 == 0 ? 0 : 10 - (iflag2 % 10)) != Integer.parseInt(end)) {
            return false;
        }
        return true;
    }

    public static boolean isValidHKCard(String idcard) {
        int sum;
        int sum2;
        String card = idcard.replaceAll("[()]", "");
        if (card.length() == 9) {
            sum = ((Character.toUpperCase(card.charAt(0)) - '7') * 9) + ((Character.toUpperCase(card.charAt(1)) - '7') * 8);
            card = card.substring(1, 9);
        } else {
            sum = ((Character.toUpperCase(card.charAt(0)) - '7') * 8) + 522;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int iflag = 7;
        int iflag2 = sum;
        for (char c : chars) {
            iflag2 += Integer.parseInt(String.valueOf(c)) * iflag;
            iflag--;
        }
        if ("A".equalsIgnoreCase(end)) {
            sum2 = iflag2 + 10;
        } else {
            sum2 = iflag2 + Integer.parseInt(end);
        }
        return sum2 % 11 == 0;
    }

    public static String getBirthByIdCard(String idcard) {
        return getBirth(idcard);
    }

    public static String getBirth(String idCard) {
        Assert.notBlank(idCard, "id card must be not blank!", new Object[0]);
        int len = idCard.length();
        if (len < 15) {
            return null;
        }
        if (len == 15) {
            idCard = convert15To18(idCard);
        }
        return ((String) Objects.requireNonNull(idCard)).substring(6, 14);
    }

    public static DateTime getBirthDate(String idCard) {
        String birthByIdCard = getBirthByIdCard(idCard);
        if (birthByIdCard == null) {
            return null;
        }
        return DateUtil.parse(birthByIdCard, DatePattern.PURE_DATE_FORMAT);
    }

    public static int getAgeByIdCard(String idcard) {
        return getAgeByIdCard(idcard, DateUtil.date());
    }

    public static int getAgeByIdCard(String idcard, Date dateToCompare) {
        String birth = getBirthByIdCard(idcard);
        return DateUtil.age(DateUtil.parse(birth, DatePattern.PURE_DATE_PATTERN), dateToCompare);
    }

    public static Short getYearByIdCard(String idcard) {
        int len = idcard.length();
        if (len < 15) {
            return null;
        }
        if (len == 15) {
            idcard = convert15To18(idcard);
        }
        return Short.valueOf(((String) Objects.requireNonNull(idcard)).substring(6, 10));
    }

    public static Short getMonthByIdCard(String idcard) {
        int len = idcard.length();
        if (len < 15) {
            return null;
        }
        if (len == 15) {
            idcard = convert15To18(idcard);
        }
        return Short.valueOf(((String) Objects.requireNonNull(idcard)).substring(10, 12));
    }

    public static Short getDayByIdCard(String idcard) {
        int len = idcard.length();
        if (len < 15) {
            return null;
        }
        if (len == 15) {
            idcard = convert15To18(idcard);
        }
        return Short.valueOf(((String) Objects.requireNonNull(idcard)).substring(12, 14));
    }

    public static int getGenderByIdCard(String idcard) {
        Assert.notBlank(idcard);
        int len = idcard.length();
        if (len < 15) {
            throw new IllegalArgumentException("ID Card length must be 15 or 18");
        }
        if (len == 15) {
            idcard = convert15To18(idcard);
        }
        char sCardChar = ((String) Objects.requireNonNull(idcard)).charAt(16);
        return sCardChar % 2 != 0 ? 1 : 0;
    }

    public static String getProvinceCodeByIdCard(String idcard) {
        int len = idcard.length();
        if (len == 15 || len == 18) {
            return idcard.substring(0, 2);
        }
        return null;
    }

    public static String getProvinceByIdCard(String idcard) {
        String code = getProvinceCodeByIdCard(idcard);
        if (StrUtil.isNotBlank(code)) {
            return CITY_CODES.get(code);
        }
        return null;
    }

    public static String getCityCodeByIdCard(String idcard) {
        int len = idcard.length();
        if (len == 15 || len == 18) {
            return idcard.substring(0, 5);
        }
        return null;
    }

    public static String hide(String idcard, int startInclude, int endExclude) {
        return StrUtil.hide(idcard, startInclude, endExclude);
    }

    public static Idcard getIdcardInfo(String idcard) {
        return new Idcard(idcard);
    }

    private static char getCheckCode18(String code17) {
        int sum = getPowerSum(code17.toCharArray());
        return getCheckCode18(sum);
    }

    private static char getCheckCode18(int iSum) {
        switch (iSum % 11) {
            case 0:
                return '1';
            case 1:
                return '0';
            case 2:
                return 'X';
            case 3:
                return '9';
            case 4:
                return '8';
            case 5:
                return '7';
            case 6:
                return '6';
            case 7:
                return '5';
            case 8:
                return '4';
            case 9:
                return '3';
            case 10:
                return '2';
            default:
                return ' ';
        }
    }

    private static int getPowerSum(char[] iArr) {
        int iSum = 0;
        if (POWER.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                iSum += Integer.parseInt(String.valueOf(iArr[i])) * POWER[i];
            }
        }
        return iSum;
    }

    /* loaded from: classes.dex */
    public static class Idcard implements Serializable {
        private static final long serialVersionUID = 1;
        private final int age;
        private final DateTime birthDate;
        private final String cityCode;
        private final Integer gender;
        private final String provinceCode;

        public Idcard(String idcard) {
            this.provinceCode = IdcardUtil.getProvinceCodeByIdCard(idcard);
            this.cityCode = IdcardUtil.getCityCodeByIdCard(idcard);
            this.birthDate = IdcardUtil.getBirthDate(idcard);
            this.gender = Integer.valueOf(IdcardUtil.getGenderByIdCard(idcard));
            this.age = IdcardUtil.getAgeByIdCard(idcard);
        }

        public String getProvinceCode() {
            return this.provinceCode;
        }

        public String getProvince() {
            return (String) IdcardUtil.CITY_CODES.get(this.provinceCode);
        }

        public String getCityCode() {
            return this.cityCode;
        }

        public DateTime getBirthDate() {
            return this.birthDate;
        }

        public Integer getGender() {
            return this.gender;
        }

        public int getAge() {
            return this.age;
        }

        public String toString() {
            return "Idcard{provinceCode='" + this.provinceCode + CharPool.SINGLE_QUOTE + ", cityCode='" + this.cityCode + CharPool.SINGLE_QUOTE + ", birthDate=" + this.birthDate + ", gender=" + this.gender + ", age=" + this.age + '}';
        }
    }
}
