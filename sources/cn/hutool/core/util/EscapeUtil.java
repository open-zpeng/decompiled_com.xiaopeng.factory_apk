package cn.hutool.core.util;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.text.escape.Html4Escape;
import cn.hutool.core.text.escape.Html4Unescape;
import cn.hutool.core.text.escape.XmlEscape;
import cn.hutool.core.text.escape.XmlUnescape;
import com.xiaopeng.commonfunc.Constant;
/* loaded from: classes.dex */
public class EscapeUtil {
    private static final Filter<Character> JS_ESCAPE_FILTER = new Filter() { // from class: cn.hutool.core.util.-$$Lambda$EscapeUtil$Y0SLOy5wDSqxv28puqgK6kjPL-E
        @Override // cn.hutool.core.lang.Filter
        public final boolean accept(Object obj) {
            return EscapeUtil.lambda$static$0((Character) obj);
        }
    };
    private static final String NOT_ESCAPE_CHARS = "*@-_+./";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$static$0(Character c) {
        return !(Character.isDigit(c.charValue()) || Character.isLowerCase(c.charValue()) || Character.isUpperCase(c.charValue()) || StrUtil.contains(NOT_ESCAPE_CHARS, c.charValue()));
    }

    public static String escapeXml(CharSequence xml) {
        XmlEscape escape = new XmlEscape();
        return escape.replace(xml).toString();
    }

    public static String unescapeXml(CharSequence xml) {
        XmlUnescape unescape = new XmlUnescape();
        return unescape.replace(xml).toString();
    }

    public static String escapeHtml4(CharSequence html) {
        Html4Escape escape = new Html4Escape();
        return escape.replace(html).toString();
    }

    public static String unescapeHtml4(CharSequence html) {
        Html4Unescape unescape = new Html4Unescape();
        return unescape.replace(html).toString();
    }

    public static String escape(CharSequence content) {
        return escape(content, JS_ESCAPE_FILTER);
    }

    public static String escapeAll(CharSequence content) {
        return escape(content, new Filter() { // from class: cn.hutool.core.util.-$$Lambda$EscapeUtil$vzYBNxGjX75uXpCnHCpBX9sz1do
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return EscapeUtil.lambda$escapeAll$1((Character) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$escapeAll$1(Character c) {
        return true;
    }

    public static String escape(CharSequence content, Filter<Character> filter) {
        if (StrUtil.isEmpty(content)) {
            return StrUtil.str(content);
        }
        StringBuilder tmp = new StringBuilder(content.length() * 6);
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (!filter.accept(Character.valueOf(c))) {
                tmp.append(c);
            } else if (c < 256) {
                tmp.append(Constant.PERCENT_STRING);
                if (c < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            } else {
                tmp.append("%u");
                if (c <= 4095) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0;
        while (lastPos < content.length()) {
            int pos = content.indexOf(Constant.PERCENT_STRING, lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    char ch2 = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch2);
                    lastPos = pos + 6;
                } else {
                    char ch3 = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch3);
                    lastPos = pos + 3;
                }
            } else if (pos == -1) {
                tmp.append(content.substring(lastPos));
                lastPos = content.length();
            } else {
                tmp.append((CharSequence) content, lastPos, pos);
                lastPos = pos;
            }
        }
        return tmp.toString();
    }

    public static String safeUnescape(String content) {
        try {
            return unescape(content);
        } catch (Exception e) {
            return content;
        }
    }
}
