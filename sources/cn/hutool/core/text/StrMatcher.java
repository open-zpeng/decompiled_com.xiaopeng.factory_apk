package cn.hutool.core.text;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class StrMatcher {
    List<String> patterns;

    public StrMatcher(String pattern) {
        this.patterns = parse(pattern);
    }

    public Map<String, String> match(String text) {
        HashMap<String, String> result = MapUtil.newHashMap(true);
        int from = 0;
        String key = null;
        for (String part : this.patterns) {
            if (StrUtil.isWrap(part, "${", StrPool.DELIM_END)) {
                key = StrUtil.sub(part, 2, part.length() - 1);
            } else {
                int to = text.indexOf(part, from);
                if (to < 0) {
                    return MapUtil.empty();
                }
                if (key != null && to > from) {
                    result.put(key, text.substring(from, to));
                }
                int from2 = part.length() + to;
                key = null;
                from = from2;
            }
        }
        if (key != null && from < text.length()) {
            result.put(key, text.substring(from));
        }
        return result;
    }

    private static List<String> parse(String pattern) {
        List<String> patterns = new ArrayList<>();
        int length = pattern.length();
        char c = 0;
        boolean inVar = false;
        StrBuilder part = StrUtil.strBuilder();
        for (int i = 0; i < length; i++) {
            char pre = c;
            c = pattern.charAt(i);
            if (inVar) {
                part.append(c);
                if ('}' == c) {
                    inVar = false;
                    patterns.add(part.toString());
                    part.clear();
                }
            } else if ('{' == c && '$' == pre) {
                inVar = true;
                String preText = part.subString(0, part.length() - 1);
                if (StrUtil.isNotEmpty(preText)) {
                    patterns.add(preText);
                }
                part.reset().append(pre).append(c);
            } else {
                part.append(c);
            }
        }
        int i2 = part.length();
        if (i2 > 0) {
            patterns.add(part.toString());
        }
        return patterns;
    }
}
