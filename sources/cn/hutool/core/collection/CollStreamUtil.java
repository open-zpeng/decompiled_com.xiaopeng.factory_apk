package cn.hutool.core.collection;

import cn.hutool.core.map.MapUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class CollStreamUtil {
    public static <V, K> Map<K, V> toIdentityMap(Collection<V> collection, Function<V, K> key) {
        if (CollUtil.isEmpty((Collection<?>) collection)) {
            return Collections.emptyMap();
        }
        return (Map) collection.stream().collect(Collectors.toMap(key, Function.identity()));
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> key, Function<E, V> value) {
        if (CollUtil.isEmpty((Collection<?>) collection)) {
            return Collections.emptyMap();
        }
        return (Map) collection.stream().collect(Collectors.toMap(key, value));
    }

    public static <E, K> Map<K, List<E>> groupByKey(Collection<E> collection, Function<E, K> key) {
        if (CollUtil.isEmpty((Collection<?>) collection)) {
            return Collections.emptyMap();
        }
        return (Map) collection.stream().collect(Collectors.groupingBy(key, Collectors.toList()));
    }

    public static <E, K, U> Map<K, Map<U, List<E>>> groupBy2Key(Collection<E> collection, Function<E, K> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty((Collection<?>) collection)) {
            return Collections.emptyMap();
        }
        return (Map) collection.stream().collect(Collectors.groupingBy(key1, Collectors.groupingBy(key2, Collectors.toList())));
    }

    public static <E, T, U> Map<T, Map<U, E>> group2Map(Collection<E> collection, Function<E, T> key1, Function<E, U> key2) {
        if (CollUtil.isEmpty((Collection<?>) collection) || key1 == null || key2 == null) {
            return Collections.emptyMap();
        }
        return (Map) collection.stream().collect(Collectors.groupingBy(key1, Collectors.toMap(key2, Function.identity())));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E, T> List<T> toList(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty((Collection<?>) collection)) {
            return Collections.emptyList();
        }
        return (List) collection.stream().map(function).filter($$Lambda$wemGins1JBTOa6vBYK6EDLxj9Ys.INSTANCE).collect(Collectors.toList());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E, T> Set<T> toSet(Collection<E> collection, Function<E, T> function) {
        if (CollUtil.isEmpty((Collection<?>) collection) || function == 0) {
            return Collections.emptySet();
        }
        return (Set) collection.stream().map(function).filter($$Lambda$wemGins1JBTOa6vBYK6EDLxj9Ys.INSTANCE).collect(Collectors.toSet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, X, Y, V> Map<K, V> merge(Map<K, X> map1, Map<K, Y> map2, BiFunction<X, Y, V> merge) {
        if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
            return Collections.emptyMap();
        }
        if (MapUtil.isEmpty(map1)) {
            map1 = Collections.emptyMap();
        } else if (MapUtil.isEmpty(map2)) {
            map2 = Collections.emptyMap();
        }
        Set<K> key = new HashSet<>();
        key.addAll(map1.keySet());
        key.addAll(map2.keySet());
        Map<K, V> map = new HashMap<>();
        for (K t : key) {
            X x = map1.get(t);
            Y y = map2.get(t);
            V z = merge.apply(x, y);
            if (z != null) {
                map.put(t, z);
            }
        }
        return map;
    }
}
