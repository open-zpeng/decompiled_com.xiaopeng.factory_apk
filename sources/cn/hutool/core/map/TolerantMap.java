package cn.hutool.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class TolerantMap<K, V> extends MapWrapper<K, V> {
    private static final long serialVersionUID = -4158133823263496197L;
    private final V defaultValue;

    public TolerantMap(V defaultValue) {
        this(new HashMap(), defaultValue);
    }

    public TolerantMap(int initialCapacity, float loadFactor, V defaultValue) {
        this(new HashMap(initialCapacity, loadFactor), defaultValue);
    }

    public TolerantMap(int initialCapacity, V defaultValue) {
        this(new HashMap(initialCapacity), defaultValue);
    }

    public TolerantMap(Map<K, V> map, V defaultValue) {
        super(map);
        this.defaultValue = defaultValue;
    }

    public static <K, V> TolerantMap<K, V> of(Map<K, V> map, V defaultValue) {
        return new TolerantMap<>(map, defaultValue);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public V get(Object key) {
        return getOrDefault(key, this.defaultValue);
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }
        TolerantMap<?, ?> that = (TolerantMap) o;
        if (getRaw().equals(that.getRaw()) && Objects.equals(this.defaultValue, that.defaultValue)) {
            return true;
        }
        return false;
    }

    @Override // cn.hutool.core.map.MapWrapper, java.util.Map
    public int hashCode() {
        return Objects.hash(getRaw(), this.defaultValue);
    }

    @Override // cn.hutool.core.map.MapWrapper
    public String toString() {
        return "TolerantMap{map=" + getRaw() + ", defaultValue=" + this.defaultValue + '}';
    }
}
