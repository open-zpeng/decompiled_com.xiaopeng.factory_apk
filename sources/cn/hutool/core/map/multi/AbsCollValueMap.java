package cn.hutool.core.map.multi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapWrapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public abstract class AbsCollValueMap<K, V, C extends Collection<V>> extends MapWrapper<K, C> {
    protected static final int DEFAULT_COLLECTION_INITIAL_CAPACITY = 3;
    private static final long serialVersionUID = 1;

    protected abstract C createCollection();

    public AbsCollValueMap() {
        this(16);
    }

    public AbsCollValueMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public AbsCollValueMap(Map<? extends K, C> m) {
        this(0.75f, m);
    }

    public AbsCollValueMap(float loadFactor, Map<? extends K, C> m) {
        this(m.size(), loadFactor);
        putAll(m);
    }

    public AbsCollValueMap(int initialCapacity, float loadFactor) {
        super(new HashMap(initialCapacity, loadFactor));
    }

    public void putAllValues(Map<? extends K, ? extends Collection<V>> m) {
        if (m != null) {
            m.forEach(new BiConsumer() { // from class: cn.hutool.core.map.multi.-$$Lambda$AbsCollValueMap$k3GhU65rs-7lgvUIcZfECxt64ZU
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    AbsCollValueMap.this.lambda$putAllValues$1$AbsCollValueMap(obj, (Collection) obj2);
                }
            });
        }
    }

    public /* synthetic */ void lambda$putAllValues$1$AbsCollValueMap(final Object key, Collection valueColl) {
        if (valueColl != null) {
            valueColl.forEach(new Consumer() { // from class: cn.hutool.core.map.multi.-$$Lambda$AbsCollValueMap$wsIX4fo9rO_YcYOYof_03Ns2MYo
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AbsCollValueMap.this.lambda$null$0$AbsCollValueMap(key, obj);
                }
            });
        }
    }

    /* renamed from: putValue */
    public void lambda$null$0$AbsCollValueMap(K key, V value) {
        Collection collection = (Collection) get(key);
        if (collection == null) {
            collection = createCollection();
            put(key, collection);
        }
        collection.add(value);
    }

    public V get(K key, int index) {
        Collection<V> collection = (Collection) get(key);
        return (V) CollUtil.get(collection, index);
    }
}
