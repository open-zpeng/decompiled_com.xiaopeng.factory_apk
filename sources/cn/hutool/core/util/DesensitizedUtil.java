package cn.hutool.core.util;
/* loaded from: classes.dex */
public class DesensitizedUtil {

    /* loaded from: classes.dex */
    public enum DesensitizedType {
        USER_ID,
        CHINESE_NAME,
        ID_CARD,
        FIXED_PHONE,
        MOBILE_PHONE,
        ADDRESS,
        EMAIL,
        PASSWORD,
        CAR_LICENSE,
        BANK_CARD
    }

    public static String desensitized(CharSequence str, DesensitizedType desensitizedType) {
        if (StrUtil.isBlank(str)) {
            return "";
        }
        String newStr = String.valueOf(str);
        switch (desensitizedType) {
            case USER_ID:
                String newStr2 = String.valueOf(userId());
                return newStr2;
            case CHINESE_NAME:
                String newStr3 = chineseName(String.valueOf(str));
                return newStr3;
            case ID_CARD:
                String newStr4 = idCardNum(String.valueOf(str), 1, 2);
                return newStr4;
            case FIXED_PHONE:
                String newStr5 = fixedPhone(String.valueOf(str));
                return newStr5;
            case MOBILE_PHONE:
                String newStr6 = mobilePhone(String.valueOf(str));
                return newStr6;
            case ADDRESS:
                String newStr7 = address(String.valueOf(str), 8);
                return newStr7;
            case EMAIL:
                String newStr8 = email(String.valueOf(str));
                return newStr8;
            case PASSWORD:
                String newStr9 = password(String.valueOf(str));
                return newStr9;
            case CAR_LICENSE:
                String newStr10 = carLicense(String.valueOf(str));
                return newStr10;
            case BANK_CARD:
                String newStr11 = bankCard(String.valueOf(str));
                return newStr11;
            default:
                return newStr;
        }
    }

    public static Long userId() {
        return 0L;
    }

    public static String chineseName(String fullName) {
        if (StrUtil.isBlank(fullName)) {
            return "";
        }
        return StrUtil.hide(fullName, 1, fullName.length());
    }

    public static String idCardNum(String idCardNum, int front, int end) {
        return (!StrUtil.isBlank(idCardNum) && front + end <= idCardNum.length() && front >= 0 && end >= 0) ? StrUtil.hide(idCardNum, front, idCardNum.length() - end) : "";
    }

    public static String fixedPhone(String num) {
        if (StrUtil.isBlank(num)) {
            return "";
        }
        return StrUtil.hide(num, 4, num.length() - 2);
    }

    public static String mobilePhone(String num) {
        if (StrUtil.isBlank(num)) {
            return "";
        }
        return StrUtil.hide(num, 3, num.length() - 4);
    }

    public static String address(String address, int sensitiveSize) {
        if (StrUtil.isBlank(address)) {
            return "";
        }
        int length = address.length();
        return StrUtil.hide(address, length - sensitiveSize, length);
    }

    public static String email(String email) {
        if (StrUtil.isBlank(email)) {
            return "";
        }
        int index = StrUtil.indexOf(email, '@');
        if (index <= 1) {
            return email;
        }
        return StrUtil.hide(email, 1, index);
    }

    public static String password(String password) {
        if (StrUtil.isBlank(password)) {
            return "";
        }
        return StrUtil.repeat('*', password.length());
    }

    public static String carLicense(String carLicense) {
        if (StrUtil.isBlank(carLicense)) {
            return "";
        }
        if (carLicense.length() == 7) {
            return StrUtil.hide(carLicense, 3, 6);
        }
        if (carLicense.length() == 8) {
            return StrUtil.hide(carLicense, 3, 7);
        }
        return carLicense;
    }

    public static String bankCard(String bankCardNo) {
        if (StrUtil.isBlank(bankCardNo)) {
            return bankCardNo;
        }
        String bankCardNo2 = StrUtil.trim(bankCardNo);
        if (bankCardNo2.length() < 9) {
            return bankCardNo2;
        }
        int length = bankCardNo2.length();
        int midLength = length - 8;
        StringBuilder buf = new StringBuilder();
        buf.append((CharSequence) bankCardNo2, 0, 4);
        for (int i = 0; i < midLength; i++) {
            if (i % 4 == 0) {
                buf.append(' ');
            }
            buf.append('*');
        }
        buf.append(' ');
        buf.append((CharSequence) bankCardNo2, length - 4, length);
        return buf.toString();
    }
}
