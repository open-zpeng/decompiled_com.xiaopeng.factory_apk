package io.sentry.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class CollectionUtils {

    /* loaded from: classes2.dex */
    public interface Predicate<T> {
        boolean test(T t);
    }

    private CollectionUtils() {
    }

    public static int size(@NotNull Iterable<?> data) {
        if (data instanceof Collection) {
            return ((Collection) data).size();
        }
        int counter = 0;
        Iterator<?> it = data.iterator();
        while (it.hasNext()) {
            it.next();
            counter++;
        }
        return counter;
    }

    @Nullable
    public static <K, V> Map<K, V> newConcurrentHashMap(@Nullable Map<K, V> map) {
        if (map != null) {
            return new ConcurrentHashMap(map);
        }
        return null;
    }

    @Nullable
    public static <K, V> Map<K, V> newHashMap(@Nullable Map<K, V> map) {
        if (map != null) {
            return new HashMap(map);
        }
        return null;
    }

    @Nullable
    public static <T> List<T> newArrayList(@Nullable List<T> list) {
        if (list != null) {
            return new ArrayList(list);
        }
        return null;
    }

    @NotNull
    public static <K, V> Map<K, V> filterMapEntries(@NotNull Map<K, V> map, @NotNull Predicate<Map.Entry<K, V>> predicate) {
        Map<K, V> filteredMap = new HashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.test(entry)) {
                filteredMap.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredMap;
    }
}
