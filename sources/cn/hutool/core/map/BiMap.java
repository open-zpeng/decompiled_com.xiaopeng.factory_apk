package cn.hutool.core.map;

import java.util.Map;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class BiMap<K, V> extends MapWrapper<K, V> {
    private static final long serialVersionUID = 1;
    private Map<V, K> inverse;

    public BiMap(Map<K, V> raw) {
        super(raw);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V put(K key, V value) {
        Map<V, K> map = this.inverse;
        if (map != null) {
            map.put(value, key);
        }
        return (V) super.put(key, value);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        if (this.inverse != null) {
            m.forEach(new BiConsumer() { // from class: cn.hutool.core.map.-$$Lambda$BiMap$le6PmiHOOIvbUodQ1TzvLLEjpzM
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    BiMap.this.lambda$putAll$0$BiMap(obj, obj2);
                }
            });
        }
    }

    public /* synthetic */ void lambda$putAll$0$BiMap(Object key, Object value) {
        this.inverse.put(value, key);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public void clear() {
        super.clear();
        this.inverse = null;
    }

    public Map<V, K> getInverse() {
        if (this.inverse == null) {
            this.inverse = MapUtil.inverse(getRaw());
        }
        return this.inverse;
    }

    public K getKey(V value) {
        return getInverse().get(value);
    }
}
