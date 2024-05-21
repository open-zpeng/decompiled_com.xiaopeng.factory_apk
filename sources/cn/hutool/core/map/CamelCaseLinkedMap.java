package cn.hutool.core.map;

import cn.hutool.core.util.StrUtil;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CamelCaseLinkedMap<K, V> extends CustomKeyMap<K, V> {
    private static final long serialVersionUID = 4043263744224569870L;

    public CamelCaseLinkedMap() {
        this(16);
    }

    public CamelCaseLinkedMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CamelCaseLinkedMap(Map<? extends K, ? extends V> m) {
        this(0.75f, m);
    }

    public CamelCaseLinkedMap(float loadFactor, Map<? extends K, ? extends V> m) {
        this(m.size(), loadFactor);
        putAll(m);
    }

    public CamelCaseLinkedMap(int initialCapacity, float loadFactor) {
        super(new LinkedHashMap(initialCapacity, loadFactor));
    }

    @Override // cn.hutool.core.map.CustomKeyMap
    protected Object customKey(Object key) {
        if (key instanceof CharSequence) {
            return StrUtil.toCamelCase(key.toString());
        }
        return key;
    }
}
