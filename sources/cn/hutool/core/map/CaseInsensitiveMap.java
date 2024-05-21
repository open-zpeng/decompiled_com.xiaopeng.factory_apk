package cn.hutool.core.map;

import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CaseInsensitiveMap<K, V> extends CustomKeyMap<K, V> {
    private static final long serialVersionUID = 4043263744224569870L;

    public CaseInsensitiveMap() {
        this(16);
    }

    public CaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CaseInsensitiveMap(Map<? extends K, ? extends V> m) {
        this(0.75f, m);
    }

    public CaseInsensitiveMap(float loadFactor, Map<? extends K, ? extends V> m) {
        this(m.size(), loadFactor);
        putAll(m);
    }

    public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
        super(new HashMap(initialCapacity, loadFactor));
    }

    @Override // cn.hutool.core.map.CustomKeyMap
    protected Object customKey(Object key) {
        if (key instanceof CharSequence) {
            return key.toString().toLowerCase();
        }
        return key;
    }
}
