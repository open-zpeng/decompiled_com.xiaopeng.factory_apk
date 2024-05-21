package cn.hutool.core.text;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Map;
/* loaded from: classes.dex */
public class StrFormatter {
    public static String format(String strPattern, Object... argArray) {
        if (StrUtil.isBlank(strPattern) || ArrayUtil.isEmpty(argArray)) {
            return strPattern;
        }
        int strPatternLength = strPattern.length();
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        int handledPosition = 0;
        int argIndex = 0;
        while (argIndex < argArray.length) {
            int delimIndex = strPattern.indexOf(StrPool.EMPTY_JSON, handledPosition);
            if (delimIndex != -1) {
                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == '\\') {
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == '\\') {
                        sbuf.append((CharSequence) strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(StrUtil.utf8Str(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        argIndex--;
                        sbuf.append((CharSequence) strPattern, handledPosition, delimIndex - 1);
                        sbuf.append('{');
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    sbuf.append((CharSequence) strPattern, handledPosition, delimIndex);
                    sbuf.append(StrUtil.utf8Str(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
                argIndex++;
            } else if (handledPosition == 0) {
                return strPattern;
            } else {
                sbuf.append((CharSequence) strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }
        }
        int argIndex2 = strPattern.length();
        sbuf.append((CharSequence) strPattern, handledPosition, argIndex2);
        return sbuf.toString();
    }

    public static String format(CharSequence template, Map<?, ?> map, boolean ignoreNull) {
        if (template == null) {
            return null;
        }
        if (map == null || map.isEmpty()) {
            String template2 = template.toString();
            return template2;
        }
        String template22 = template.toString();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String value = StrUtil.utf8Str(entry.getValue());
            if (value != null || !ignoreNull) {
                template22 = StrUtil.replace(template22, StrPool.DELIM_START + entry.getKey() + StrPool.DELIM_END, value);
            }
        }
        return template22;
    }
}
