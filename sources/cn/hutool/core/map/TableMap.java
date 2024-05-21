package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.util.ObjectUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class TableMap<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
    private static final long serialVersionUID = 1;
    private final List<K> keys;
    private final List<V> values;

    public TableMap(int size) {
        this.keys = new ArrayList(size);
        this.values = new ArrayList(size);
    }

    public TableMap(K[] keys, V[] values) {
        this.keys = CollUtil.toList(keys);
        this.values = CollUtil.toList(values);
    }

    @Override // java.util.Map
    public int size() {
        return this.keys.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return CollUtil.isEmpty((Collection<?>) this.keys);
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.keys.contains(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.values.contains(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        int index = this.keys.indexOf(key);
        if (index > -1 && index < this.values.size()) {
            return this.values.get(index);
        }
        return null;
    }

    public K getKey(V value) {
        int index = this.values.indexOf(value);
        if (index > -1 && index < this.keys.size()) {
            return this.keys.get(index);
        }
        return null;
    }

    public List<V> getValues(final K key) {
        return CollUtil.getAny(this.values, ListUtil.indexOfAll(this.keys, new Matcher() { // from class: cn.hutool.core.map.-$$Lambda$TableMap$QHfCvinkEgFyMzT6-xG2CFX4aUg
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equal;
                equal = ObjectUtil.equal(obj, key);
                return equal;
            }
        }));
    }

    public List<K> getKeys(final V value) {
        return CollUtil.getAny(this.keys, ListUtil.indexOfAll(this.values, new Matcher() { // from class: cn.hutool.core.map.-$$Lambda$TableMap$4nb7QwfKABbd_xVa4WMyV2mKUSU
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equal;
                equal = ObjectUtil.equal(obj, value);
                return equal;
            }
        }));
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        this.keys.add(key);
        this.values.add(value);
        return null;
    }

    @Override // java.util.Map
    public V remove(Object key) {
        int index = this.keys.indexOf(key);
        if (index > -1) {
            this.keys.remove(index);
            if (index < this.values.size()) {
                this.values.remove(index);
                return null;
            }
            return null;
        }
        return null;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.keys.clear();
        this.values.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return new HashSet(this.keys);
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return Collections.unmodifiableList(this.values);
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> hashSet = new LinkedHashSet<>();
        for (int i = 0; i < size(); i++) {
            hashSet.add(new Entry<>(this.keys.get(i), this.values.get(i)));
        }
        return hashSet;
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return new Iterator<Map.Entry<K, V>>() { // from class: cn.hutool.core.map.TableMap.1
            private final Iterator<K> keysIter;
            private final Iterator<V> valuesIter;

            {
                this.keysIter = TableMap.this.keys.iterator();
                this.valuesIter = TableMap.this.values.iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.keysIter.hasNext() && this.valuesIter.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return new Entry(this.keysIter.next(), this.valuesIter.next());
            }

            @Override // java.util.Iterator
            public void remove() {
                this.keysIter.remove();
                this.valuesIter.remove();
            }
        };
    }

    public String toString() {
        return "TableMap{keys=" + this.keys + ", values=" + this.values + '}';
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            throw new UnsupportedOperationException("setValue not supported.");
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                return Objects.equals(this.key, e.getKey()) && Objects.equals(this.value, e.getValue());
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return Objects.hashCode(this.key) ^ Objects.hashCode(this.value);
        }
    }
}
