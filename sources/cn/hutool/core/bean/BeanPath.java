package cn.hutool.core.bean;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class BeanPath implements Serializable {
    private static final char[] EXP_CHARS = {'.', '[', ']'};
    private static final long serialVersionUID = 1;
    private boolean isStartWith = false;
    protected List<String> patternParts;

    public static BeanPath create(String expression) {
        return new BeanPath(expression);
    }

    public BeanPath(String expression) {
        init(expression);
    }

    public Object get(Object bean) {
        return get(this.patternParts, bean, false);
    }

    public void set(Object bean, Object value) {
        set(bean, this.patternParts, value);
    }

    private void set(Object bean, List<String> patternParts, Object value) {
        Object subBean = get(patternParts, bean, true);
        if (subBean == null) {
            set(bean, patternParts.subList(0, patternParts.size() - 1), new HashMap());
            subBean = get(patternParts, bean, true);
        }
        BeanUtil.setFieldValue(subBean, patternParts.get(patternParts.size() - 1), value);
    }

    private Object get(List<String> patternParts, Object bean, boolean ignoreLast) {
        int length = patternParts.size();
        if (ignoreLast) {
            length--;
        }
        Object subBean = bean;
        boolean isFirst = true;
        for (int i = 0; i < length; i++) {
            String patternPart = patternParts.get(i);
            subBean = getFieldValue(subBean, patternPart);
            if (subBean == null) {
                if (isFirst && !this.isStartWith && BeanUtil.isMatchName(bean, patternPart, true)) {
                    subBean = bean;
                    isFirst = false;
                } else {
                    return null;
                }
            }
        }
        return subBean;
    }

    private static Object getFieldValue(Object bean, String expression) {
        if (StrUtil.isBlank(expression)) {
            return null;
        }
        if (StrUtil.contains((CharSequence) expression, ':')) {
            List<String> parts = StrUtil.splitTrim((CharSequence) expression, ':');
            int start = Integer.parseInt(parts.get(0));
            int end = Integer.parseInt(parts.get(1));
            int step = 1;
            if (3 == parts.size()) {
                step = Integer.parseInt(parts.get(2));
            }
            if (bean instanceof Collection) {
                return CollUtil.sub((Collection) bean, start, end, step);
            }
            if (ArrayUtil.isArray(bean)) {
                return ArrayUtil.sub(bean, start, end, step);
            }
            return null;
        } else if (StrUtil.contains((CharSequence) expression, ',')) {
            List<String> keys = StrUtil.splitTrim((CharSequence) expression, ',');
            if (bean instanceof Collection) {
                return CollUtil.getAny((Collection) bean, (int[]) Convert.convert((Class<Object>) int[].class, (Object) keys));
            }
            if (ArrayUtil.isArray(bean)) {
                return ArrayUtil.getAny(bean, (int[]) Convert.convert((Class<Object>) int[].class, (Object) keys));
            }
            String[] unWrappedKeys = new String[keys.size()];
            for (int i = 0; i < unWrappedKeys.length; i++) {
                unWrappedKeys[i] = StrUtil.unWrap(keys.get(i), CharPool.SINGLE_QUOTE);
            }
            if (bean instanceof Map) {
                return MapUtil.getAny((Map) bean, unWrappedKeys);
            }
            Map<String, Object> map = BeanUtil.beanToMap(bean);
            return MapUtil.getAny(map, unWrappedKeys);
        } else {
            return BeanUtil.getFieldValue(bean, expression);
        }
    }

    private void init(String expression) {
        List<String> localPatternParts = new ArrayList<>();
        int length = expression.length();
        StrBuilder builder = StrUtil.strBuilder();
        boolean isNumStart = false;
        for (int i = 0; i < length; i++) {
            char c = expression.charAt(i);
            if (i == 0 && '$' == c) {
                this.isStartWith = true;
            } else if (ArrayUtil.contains(EXP_CHARS, c)) {
                if (']' == c) {
                    if (!isNumStart) {
                        throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find ']' but no '[' !", expression, Integer.valueOf(i)));
                    }
                    isNumStart = false;
                } else if (isNumStart) {
                    throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, Integer.valueOf(i)));
                } else {
                    if ('[' == c) {
                        isNumStart = true;
                    }
                }
                if (builder.length() > 0) {
                    localPatternParts.add(unWrapIfPossible(builder));
                }
                builder.reset();
            } else {
                builder.append(c);
            }
        }
        if (isNumStart) {
            throw new IllegalArgumentException(StrUtil.format("Bad expression '{}':{}, we find '[' but no ']' !", expression, Integer.valueOf(length - 1)));
        }
        if (builder.length() > 0) {
            localPatternParts.add(unWrapIfPossible(builder));
        }
        this.patternParts = Collections.unmodifiableList(localPatternParts);
    }

    private static String unWrapIfPossible(CharSequence expression) {
        if (StrUtil.containsAny(expression, " = ", " > ", " < ", " like ", ",")) {
            return expression.toString();
        }
        return StrUtil.unWrap(expression, CharPool.SINGLE_QUOTE);
    }
}
