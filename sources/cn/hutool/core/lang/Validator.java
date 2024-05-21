package cn.hutool.core.lang;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.CreditCodeUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class Validator {
    public static final Pattern GENERAL = PatternPool.GENERAL;
    public static final Pattern NUMBERS = PatternPool.NUMBERS;
    public static final Pattern GROUP_VAR = PatternPool.GROUP_VAR;
    public static final Pattern IPV4 = PatternPool.IPV4;
    public static final Pattern IPV6 = PatternPool.IPV6;
    public static final Pattern MONEY = PatternPool.MONEY;
    public static final Pattern EMAIL = PatternPool.EMAIL;
    public static final Pattern MOBILE = PatternPool.MOBILE;
    public static final Pattern CITIZEN_ID = PatternPool.CITIZEN_ID;
    public static final Pattern ZIP_CODE = PatternPool.ZIP_CODE;
    public static final Pattern BIRTHDAY = PatternPool.BIRTHDAY;
    public static final Pattern URL = PatternPool.URL;
    public static final Pattern URL_HTTP = PatternPool.URL_HTTP;
    public static final Pattern GENERAL_WITH_CHINESE = PatternPool.GENERAL_WITH_CHINESE;
    public static final Pattern UUID = PatternPool.UUID;
    public static final Pattern UUID_SIMPLE = PatternPool.UUID_SIMPLE;
    public static final Pattern PLATE_NUMBER = PatternPool.PLATE_NUMBER;
    public static final Pattern CAR_VIN = PatternPool.CAR_VIN;
    public static final Pattern CAR_DRIVING_LICENCE = PatternPool.CAR_DRIVING_LICENCE;

    public static boolean isTrue(boolean value) {
        return value;
    }

    public static boolean isFalse(boolean value) {
        return !value;
    }

    public static boolean validateTrue(boolean value, String errorMsgTemplate, Object... params) throws ValidateException {
        if (isFalse(value)) {
            throw new ValidateException(errorMsgTemplate, params);
        }
        return true;
    }

    public static boolean validateFalse(boolean value, String errorMsgTemplate, Object... params) throws ValidateException {
        if (isTrue(value)) {
            throw new ValidateException(errorMsgTemplate, params);
        }
        return false;
    }

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isNotNull(Object value) {
        return value != null;
    }

    public static <T> T validateNull(T value, String errorMsgTemplate, Object... params) throws ValidateException {
        if (isNotNull(value)) {
            throw new ValidateException(errorMsgTemplate, params);
        }
        return null;
    }

    public static <T> T validateNotNull(T value, String errorMsgTemplate, Object... params) throws ValidateException {
        if (isNull(value)) {
            throw new ValidateException(errorMsgTemplate, params);
        }
        return value;
    }

    public static boolean isEmpty(Object value) {
        return value == null || ((value instanceof String) && StrUtil.isEmpty((String) value));
    }

    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }

    public static <T> T validateEmpty(T value, String errorMsg) throws ValidateException {
        if (isNotEmpty(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static <T> T validateNotEmpty(T value, String errorMsg) throws ValidateException {
        if (isEmpty(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean equal(Object t1, Object t2) {
        return ObjectUtil.equal(t1, t2);
    }

    public static Object validateEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
        if (!equal(t1, t2)) {
            throw new ValidateException(errorMsg);
        }
        return t1;
    }

    public static void validateNotEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
        if (equal(t1, t2)) {
            throw new ValidateException(errorMsg);
        }
    }

    public static void validateNotEmptyAndEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
        validateNotEmpty(t1, errorMsg);
        validateEqual(t1, t2, errorMsg);
    }

    public static void validateNotEmptyAndNotEqual(Object t1, Object t2, String errorMsg) throws ValidateException {
        validateNotEmpty(t1, errorMsg);
        validateNotEqual(t1, t2, errorMsg);
    }

    public static <T extends CharSequence> T validateMatchRegex(String regex, T value, String errorMsg) throws ValidateException {
        if (!isMatchRegex(regex, value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isMatchRegex(Pattern pattern, CharSequence value) {
        return ReUtil.isMatch(pattern, value);
    }

    public static boolean isMatchRegex(String regex, CharSequence value) {
        return ReUtil.isMatch(regex, value);
    }

    public static boolean isGeneral(CharSequence value) {
        return isMatchRegex(GENERAL, value);
    }

    public static <T extends CharSequence> T validateGeneral(T value, String errorMsg) throws ValidateException {
        if (!isGeneral(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isGeneral(CharSequence value, int min, int max) {
        if (min < 0) {
            min = 0;
        }
        String reg = "^\\w{" + min + "," + max + "}$";
        if (max <= 0) {
            reg = "^\\w{" + min + ",}$";
        }
        return isMatchRegex(reg, value);
    }

    public static <T extends CharSequence> T validateGeneral(T value, int min, int max, String errorMsg) throws ValidateException {
        if (!isGeneral(value, min, max)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isGeneral(CharSequence value, int min) {
        return isGeneral(value, min, 0);
    }

    public static <T extends CharSequence> T validateGeneral(T value, int min, String errorMsg) throws ValidateException {
        return (T) validateGeneral(value, min, 0, errorMsg);
    }

    public static boolean isLetter(CharSequence value) {
        return StrUtil.isAllCharMatch(value, new Matcher() { // from class: cn.hutool.core.lang.-$$Lambda$SV9JJ1QSsSKen-FcGACmnDL9nu0
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Character.isLetter(((Character) obj).charValue());
            }
        });
    }

    public static <T extends CharSequence> T validateLetter(T value, String errorMsg) throws ValidateException {
        if (!isLetter(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isUpperCase(CharSequence value) {
        return StrUtil.isAllCharMatch(value, new Matcher() { // from class: cn.hutool.core.lang.-$$Lambda$Qqpj4kglvti_mzNuEyuHtN7wmaM
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Character.isUpperCase(((Character) obj).charValue());
            }
        });
    }

    public static <T extends CharSequence> T validateUpperCase(T value, String errorMsg) throws ValidateException {
        if (!isUpperCase(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isLowerCase(CharSequence value) {
        return StrUtil.isAllCharMatch(value, new Matcher() { // from class: cn.hutool.core.lang.-$$Lambda$1GOCWFW4WvPRjg19cXlyAwntozE
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Character.isLowerCase(((Character) obj).charValue());
            }
        });
    }

    public static <T extends CharSequence> T validateLowerCase(T value, String errorMsg) throws ValidateException {
        if (!isLowerCase(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isNumber(CharSequence value) {
        return NumberUtil.isNumber(value);
    }

    public static boolean hasNumber(CharSequence value) {
        return ReUtil.contains(PatternPool.NUMBERS, value);
    }

    public static String validateNumber(String value, String errorMsg) throws ValidateException {
        if (!isNumber(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isWord(CharSequence value) {
        return isMatchRegex(PatternPool.WORD, value);
    }

    public static <T extends CharSequence> T validateWord(T value, String errorMsg) throws ValidateException {
        if (!isWord(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isMoney(CharSequence value) {
        return isMatchRegex(MONEY, value);
    }

    public static <T extends CharSequence> T validateMoney(T value, String errorMsg) throws ValidateException {
        if (!isMoney(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isZipCode(CharSequence value) {
        return isMatchRegex(ZIP_CODE, value);
    }

    public static <T extends CharSequence> T validateZipCode(T value, String errorMsg) throws ValidateException {
        if (!isZipCode(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isEmail(CharSequence value) {
        return isMatchRegex(EMAIL, value);
    }

    public static <T extends CharSequence> T validateEmail(T value, String errorMsg) throws ValidateException {
        if (!isEmail(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isMobile(CharSequence value) {
        return isMatchRegex(MOBILE, value);
    }

    public static <T extends CharSequence> T validateMobile(T value, String errorMsg) throws ValidateException {
        if (!isMobile(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isCitizenId(CharSequence value) {
        return IdcardUtil.isValidCard(String.valueOf(value));
    }

    public static <T extends CharSequence> T validateCitizenIdNumber(T value, String errorMsg) throws ValidateException {
        if (!isCitizenId(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isBirthday(int year, int month, int day) {
        int thisYear = DateUtil.thisYear();
        if (year < 1900 || year > thisYear || month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (month != 2) {
            return true;
        }
        if (day >= 29 && (day != 29 || !DateUtil.isLeapYear(year))) {
            return false;
        }
        return true;
    }

    public static boolean isBirthday(CharSequence value) {
        java.util.regex.Matcher matcher = BIRTHDAY.matcher(value);
        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(3));
            int day = Integer.parseInt(matcher.group(5));
            return isBirthday(year, month, day);
        }
        return false;
    }

    public static <T extends CharSequence> T validateBirthday(T value, String errorMsg) throws ValidateException {
        if (!isBirthday(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isIpv4(CharSequence value) {
        return isMatchRegex(IPV4, value);
    }

    public static <T extends CharSequence> T validateIpv4(T value, String errorMsg) throws ValidateException {
        if (!isIpv4(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isIpv6(CharSequence value) {
        return isMatchRegex(IPV6, value);
    }

    public static <T extends CharSequence> T validateIpv6(T value, String errorMsg) throws ValidateException {
        if (!isIpv6(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isMac(CharSequence value) {
        return isMatchRegex(PatternPool.MAC_ADDRESS, value);
    }

    public static <T extends CharSequence> T validateMac(T value, String errorMsg) throws ValidateException {
        if (!isMac(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isPlateNumber(CharSequence value) {
        return isMatchRegex(PLATE_NUMBER, value);
    }

    public static <T extends CharSequence> T validatePlateNumber(T value, String errorMsg) throws ValidateException {
        if (!isPlateNumber(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isUrl(CharSequence value) {
        if (StrUtil.isBlank(value)) {
            return false;
        }
        try {
            new URL(StrUtil.str(value));
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static <T extends CharSequence> T validateUrl(T value, String errorMsg) throws ValidateException {
        if (!isUrl(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isChinese(CharSequence value) {
        return isMatchRegex(PatternPool.CHINESES, value);
    }

    public static boolean hasChinese(CharSequence value) {
        return ReUtil.contains("[一-\u9fff]+", value);
    }

    public static <T extends CharSequence> T validateChinese(T value, String errorMsg) throws ValidateException {
        if (!isChinese(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isGeneralWithChinese(CharSequence value) {
        return isMatchRegex(GENERAL_WITH_CHINESE, value);
    }

    public static <T extends CharSequence> T validateGeneralWithChinese(T value, String errorMsg) throws ValidateException {
        if (!isGeneralWithChinese(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isUUID(CharSequence value) {
        return isMatchRegex(UUID, value) || isMatchRegex(UUID_SIMPLE, value);
    }

    public static <T extends CharSequence> T validateUUID(T value, String errorMsg) throws ValidateException {
        if (!isUUID(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isHex(CharSequence value) {
        return isMatchRegex(PatternPool.HEX, value);
    }

    public static <T extends CharSequence> T validateHex(T value, String errorMsg) throws ValidateException {
        if (!isHex(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isBetween(Number value, Number min, Number max) {
        Assert.notNull(value);
        Assert.notNull(min);
        Assert.notNull(max);
        double doubleValue = value.doubleValue();
        return doubleValue >= min.doubleValue() && doubleValue <= max.doubleValue();
    }

    public static void validateBetween(Number value, Number min, Number max, String errorMsg) throws ValidateException {
        if (!isBetween(value, min, max)) {
            throw new ValidateException(errorMsg);
        }
    }

    public static boolean isCreditCode(CharSequence creditCode) {
        return CreditCodeUtil.isCreditCode(creditCode);
    }

    public static boolean isCarVin(CharSequence value) {
        return isMatchRegex(CAR_VIN, value);
    }

    public static <T extends CharSequence> T validateCarVin(T value, String errorMsg) throws ValidateException {
        if (!isCarVin(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }

    public static boolean isCarDrivingLicence(CharSequence value) {
        return isMatchRegex(CAR_DRIVING_LICENCE, value);
    }

    public static <T extends CharSequence> T validateCarDrivingLicence(T value, String errorMsg) throws ValidateException {
        if (!isCarDrivingLicence(value)) {
            throw new ValidateException(errorMsg);
        }
        return value;
    }
}
