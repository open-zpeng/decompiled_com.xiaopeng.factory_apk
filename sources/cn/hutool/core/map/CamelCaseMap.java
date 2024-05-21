package cn.hutool.core.map;

import cn.hutool.core.util.StrUtil;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CamelCaseMap<K, V> extends CustomKeyMap<K, V> {
    private static final long serialVersionUID = 4043263744224569870L;

    public CamelCaseMap() {
        this(16);
    }

    public CamelCaseMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CamelCaseMap(Map<? extends K, ? extends V> m) {
        this(0.75f, m);
    }

    public CamelCaseMap(float loadFactor, Map<? extends K, ? extends V> m) {
        this(m.size(), loadFactor);
        putAll(m);
    }

    public CamelCaseMap(int initialCapacity, float loadFactor) {
        super(new HashMap(initialCapacity, loadFactor));
    }

    @Override // cn.hutool.core.map.CustomKeyMap
    protected Object customKey(Object key) {
        if (key instanceof CharSequence) {
            return StrUtil.toCamelCase(key.toString());
        }
        return key;
    }
}
