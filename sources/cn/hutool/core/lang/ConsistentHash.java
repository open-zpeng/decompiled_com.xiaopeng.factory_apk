package cn.hutool.core.lang;

import cn.hutool.core.lang.hash.Hash32;
import cn.hutool.core.util.HashUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class ConsistentHash<T> implements Serializable {
    private static final long serialVersionUID = 1;
    private final SortedMap<Integer, T> circle;
    Hash32<Object> hashFunc;
    private final int numberOfReplicas;

    public ConsistentHash(int numberOfReplicas, Collection<T> nodes) {
        this.circle = new TreeMap();
        this.numberOfReplicas = numberOfReplicas;
        this.hashFunc = new Hash32() { // from class: cn.hutool.core.lang.-$$Lambda$ConsistentHash$zZwMJ9sDQ4eKDKgq9ShduzQe0RM
            @Override // cn.hutool.core.lang.hash.Hash32
            public final int hash32(Object obj) {
                int fnvHash;
                fnvHash = HashUtil.fnvHash(obj.toString());
                return fnvHash;
            }
        };
        for (T node : nodes) {
            add(node);
        }
    }

    public ConsistentHash(Hash32<Object> hashFunc, int numberOfReplicas, Collection<T> nodes) {
        this.circle = new TreeMap();
        this.numberOfReplicas = numberOfReplicas;
        this.hashFunc = hashFunc;
        for (T node : nodes) {
            add(node);
        }
    }

    public void add(T node) {
        for (int i = 0; i < this.numberOfReplicas; i++) {
            SortedMap<Integer, T> sortedMap = this.circle;
            Hash32<Object> hash32 = this.hashFunc;
            sortedMap.put(Integer.valueOf(hash32.hash32(node.toString() + i)), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < this.numberOfReplicas; i++) {
            SortedMap<Integer, T> sortedMap = this.circle;
            Hash32<Object> hash32 = this.hashFunc;
            sortedMap.remove(Integer.valueOf(hash32.hash32(node.toString() + i)));
        }
    }

    public T get(Object key) {
        if (this.circle.isEmpty()) {
            return null;
        }
        int hash = this.hashFunc.hash32(key);
        if (!this.circle.containsKey(Integer.valueOf(hash))) {
            SortedMap<Integer, T> tailMap = this.circle.tailMap(Integer.valueOf(hash));
            hash = (tailMap.isEmpty() ? this.circle.firstKey() : tailMap.firstKey()).intValue();
        }
        return this.circle.get(Integer.valueOf(hash));
    }
}
