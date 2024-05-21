package cn.hutool.core.text;

import cn.hutool.core.util.StrUtil;
/* loaded from: classes.dex */
public class NamingCase {
    public static String toUnderlineCase(CharSequence str) {
        return toSymbolCase(str, '_');
    }

    public static String toKebabCase(CharSequence str) {
        return toSymbolCase(str, CharPool.DASHED);
    }

    public static String toSymbolCase(CharSequence str, char symbol) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        StrBuilder sb = new StrBuilder();
        int i = 0;
        while (i < length) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                Character preChar = i > 0 ? Character.valueOf(str.charAt(i - 1)) : null;
                Character nextChar = i < str.length() + (-1) ? Character.valueOf(str.charAt(i + 1)) : null;
                if (preChar != null) {
                    if (symbol == preChar.charValue()) {
                        if (nextChar == null || Character.isLowerCase(nextChar.charValue())) {
                            c = Character.toLowerCase(c);
                        }
                    } else if (Character.isLowerCase(preChar.charValue())) {
                        sb.append(symbol);
                        if (nextChar == null || Character.isLowerCase(nextChar.charValue())) {
                            c = Character.toLowerCase(c);
                        }
                    } else if (nextChar == null || Character.isLowerCase(nextChar.charValue())) {
                        sb.append(symbol);
                        c = Character.toLowerCase(c);
                    }
                } else if (nextChar == null || Character.isLowerCase(nextChar.charValue())) {
                    c = Character.toLowerCase(c);
                }
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static String toPascalCase(CharSequence name) {
        return StrUtil.upperFirst(toCamelCase(name));
    }

    public static String toCamelCase(CharSequence name) {
        if (name == null) {
            return null;
        }
        String name2 = name.toString();
        if (StrUtil.contains((CharSequence) name2, '_')) {
            int length = name2.length();
            StringBuilder sb = new StringBuilder(length);
            boolean upperCase = false;
            for (int i = 0; i < length; i++) {
                char c = name2.charAt(i);
                if (c == '_') {
                    upperCase = true;
                } else if (upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        }
        return name2;
    }
}
