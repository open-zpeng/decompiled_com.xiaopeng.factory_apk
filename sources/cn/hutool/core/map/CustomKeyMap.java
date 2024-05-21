package cn.hutool.core.map;

import java.util.Map;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public abstract class CustomKeyMap<K, V> extends MapWrapper<K, V> {
    private static final long serialVersionUID = 4043263744224569870L;

    protected abstract Object customKey(Object obj);

    public CustomKeyMap(Map<K, V> m) {
        super(m);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V get(Object key) {
        return (V) super.get(customKey(key));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V put(K key, V value) {
        return (V) super.put(customKey(key), value);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(new BiConsumer() { // from class: cn.hutool.core.map.-$$Lambda$M_3GE4Z_EmSOGQuYa7MGOy6lRQQ
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CustomKeyMap.this.put(obj, obj2);
            }
        });
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public boolean containsKey(Object key) {
        return super.containsKey(customKey(key));
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V remove(Object key) {
        return (V) super.remove(customKey(key));
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public boolean remove(Object key, Object value) {
        return super.remove(customKey(key), value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        return super.replace(customKey(key), oldValue, newValue);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V replace(K key, V value) {
        return (V) super.replace(customKey(key), value);
    }
}
