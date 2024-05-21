package cn.hutool.core.lang;

import cn.hutool.core.lang.func.Func0;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class SimpleCache<K, V> implements Iterable<Map.Entry<K, V>>, Serializable {
    private static final long serialVersionUID = 1;
    private final Map<K, V> cache;
    protected final Map<K, Lock> keyLockMap;
    private final ReentrantReadWriteLock lock;

    public SimpleCache() {
        this(new WeakHashMap());
    }

    public SimpleCache(Map<K, V> initMap) {
        this.lock = new ReentrantReadWriteLock();
        this.keyLockMap = new ConcurrentHashMap();
        this.cache = initMap;
    }

    public V get(K key) {
        this.lock.readLock().lock();
        try {
            return this.cache.get(key);
        } finally {
            this.lock.readLock().unlock();
        }
    }

    public V get(K key, Func0<V> supplier) {
        return get(key, null, supplier);
    }

    public V get(K key, Predicate<V> validPredicate, Func0<V> supplier) {
        V v = get(key);
        if (v == null && supplier != null) {
            Lock keyLock = this.keyLockMap.computeIfAbsent(key, new Function() { // from class: cn.hutool.core.lang.-$$Lambda$SimpleCache$WD8vg11-aUckGNZ8CjPPACOh_iE
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return SimpleCache.lambda$get$0(obj);
                }
            });
            keyLock.lock();
            try {
                v = this.cache.get(key);
                if (v == null || (validPredicate != null && !validPredicate.test(v))) {
                    try {
                        v = supplier.call();
                        put(key, v);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } finally {
                keyLock.unlock();
                this.keyLockMap.remove(key);
            }
        }
        return v;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Lock lambda$get$0(Object k) {
        return new ReentrantLock();
    }

    public V put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            this.cache.put(key, value);
            return value;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public V remove(K key) {
        this.lock.writeLock().lock();
        try {
            return this.cache.remove(key);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public void clear() {
        this.lock.writeLock().lock();
        try {
            this.cache.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.cache.entrySet().iterator();
    }
}
