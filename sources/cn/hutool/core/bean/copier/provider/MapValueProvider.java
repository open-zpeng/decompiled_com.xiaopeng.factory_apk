package cn.hutool.core.bean.copier.provider;

import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Type;
import java.util.Map;
/* loaded from: classes.dex */
public class MapValueProvider implements ValueProvider<String> {
    private final boolean ignoreError;
    private final Map<?, ?> map;

    public MapValueProvider(Map<?, ?> map, boolean ignoreCase) {
        this(map, ignoreCase, false);
    }

    public MapValueProvider(Map<?, ?> map, boolean ignoreCase, boolean ignoreError) {
        if (!ignoreCase || (map instanceof CaseInsensitiveMap)) {
            this.map = map;
        } else {
            this.map = new CaseInsensitiveMap(map);
        }
        this.ignoreError = ignoreError;
    }

    @Override // cn.hutool.core.bean.copier.ValueProvider
    public Object value(String key, Type valueType) {
        String key1 = getKey(key, valueType);
        if (key1 == null) {
            return null;
        }
        return Convert.convertWithCheck(valueType, this.map.get(key1), null, this.ignoreError);
    }

    @Override // cn.hutool.core.bean.copier.ValueProvider
    public boolean containsKey(String key) {
        return getKey(key, null) != null;
    }

    private String getKey(String key, Type valueType) {
        if (this.map.containsKey(key)) {
            return key;
        }
        String customKey = StrUtil.toUnderlineCase(key);
        if (this.map.containsKey(customKey)) {
            return customKey;
        }
        if (valueType == null || Boolean.class == valueType || Boolean.TYPE == valueType) {
            String customKey2 = StrUtil.upperFirstAndAddPre(key, "is");
            if (this.map.containsKey(customKey2)) {
                return customKey2;
            }
            String customKey3 = StrUtil.toUnderlineCase(customKey2);
            if (this.map.containsKey(customKey3)) {
                return customKey3;
            }
            return null;
        }
        return null;
    }
}
