package cn.hutool.core.map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class MapUtil {
    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty()) ? false : true;
    }

    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> set) {
        return set == null ? Collections.emptyMap() : set;
    }

    public static <T extends Map<K, V>, K, V> T defaultIfEmpty(T map, T defaultMap) {
        return isEmpty(map) ? defaultMap : map;
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> HashMap<K, V> newHashMap(int size, boolean isOrder) {
        int initialCapacity = ((int) (size / 0.75f)) + 1;
        return isOrder ? new LinkedHashMap(initialCapacity) : new HashMap<>(initialCapacity);
    }

    public static <K, V> HashMap<K, V> newHashMap(int size) {
        return newHashMap(size, false);
    }

    public static <K, V> HashMap<K, V> newHashMap(boolean isOrder) {
        return newHashMap(16, isOrder);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
        return new TreeMap<>(comparator);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
        TreeMap<K, V> treeMap = new TreeMap<>(comparator);
        if (!isEmpty(map)) {
            treeMap.putAll(map);
        }
        return treeMap;
    }

    public static <K, V> Map<K, V> newIdentityMap(int size) {
        return new IdentityHashMap(size);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>(16);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
        int initCapacity = size <= 0 ? 16 : size;
        return new ConcurrentHashMap<>(initCapacity);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<K, V> map) {
        if (isEmpty(map)) {
            return new ConcurrentHashMap<>(16);
        }
        return new ConcurrentHashMap<>(map);
    }

    public static <K, V> Map<K, V> createMap(Class<?> mapType) {
        if (mapType.isAssignableFrom(AbstractMap.class)) {
            return new HashMap();
        }
        return (Map) ReflectUtil.newInstance(mapType, new Object[0]);
    }

    public static <K, V> HashMap<K, V> of(K key, V value) {
        return of(key, value, false);
    }

    public static <K, V> HashMap<K, V> of(K key, V value, boolean isOrder) {
        HashMap<K, V> map = newHashMap(isOrder);
        map.put(key, value);
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> of(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    public static HashMap<Object, Object> of(Object[] array) {
        if (array == null) {
            return null;
        }
        HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5d));
        for (int i = 0; i < array.length; i++) {
            Object object = array[i];
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) object;
                map.put(entry.getKey(), entry.getValue());
            } else if (object instanceof Object[]) {
                Object[] entry2 = (Object[]) object;
                if (entry2.length > 1) {
                    map.put(entry2[0], entry2[1]);
                }
            } else if (object instanceof Iterable) {
                Iterator iter = ((Iterable) object).iterator();
                if (iter.hasNext()) {
                    Object key = iter.next();
                    if (iter.hasNext()) {
                        Object value = iter.next();
                        map.put(key, value);
                    }
                }
            } else if (object instanceof Iterator) {
                Iterator iter2 = (Iterator) object;
                if (iter2.hasNext()) {
                    Object key2 = iter2.next();
                    if (iter2.hasNext()) {
                        Object value2 = iter2.next();
                        map.put(key2, value2);
                    }
                }
            } else {
                throw new IllegalArgumentException(StrUtil.format("Array element {}, '{}', is not type of Map.Entry or Array or Iterable or Iterator", Integer.valueOf(i), object));
            }
        }
        return map;
    }

    public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
        HashMap<K, List<V>> resultMap = new HashMap<>();
        if (CollUtil.isEmpty(mapList)) {
            return resultMap;
        }
        for (Map<K, V> map : mapList) {
            Set<Map.Entry<K, V>> entrySet = map.entrySet();
            for (Map.Entry<K, V> entry : entrySet) {
                K key = entry.getKey();
                List<V> valueList = resultMap.get(key);
                if (valueList == null) {
                    resultMap.put(key, CollUtil.newArrayList(entry.getValue()));
                } else {
                    valueList.add(entry.getValue());
                }
            }
        }
        return resultMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
        boolean isEnd;
        List<Map<K, V>> resultList = new ArrayList<>();
        if (isEmpty(listMap)) {
            return resultList;
        }
        int index = 0;
        do {
            isEnd = true;
            Map<K, V> map = new HashMap<>();
            for (Map.Entry<K, ? extends Iterable<V>> entry : listMap.entrySet()) {
                List<V> vList = CollUtil.newArrayList(entry.getValue());
                int vListSize = vList.size();
                if (index < vListSize) {
                    map.put(entry.getKey(), vList.get(index));
                    if (index != vListSize - 1) {
                        isEnd = false;
                    }
                }
            }
            if (!map.isEmpty()) {
                resultList.add(map);
            }
            index++;
        } while (!isEnd);
        return resultList;
    }

    public static <K, V> Map<K, V> toCamelCaseMap(Map<K, V> map) {
        return map instanceof LinkedHashMap ? new CamelCaseLinkedMap(map) : new CamelCaseMap(map);
    }

    public static Object[][] toObjectArray(Map<?, ?> map) {
        if (map == null) {
            return null;
        }
        Object[][] result = (Object[][]) Array.newInstance(Object.class, map.size(), 2);
        if (map.isEmpty()) {
            return result;
        }
        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            result[index][0] = entry.getKey();
            result[index][1] = entry.getValue();
            index++;
        }
        return result;
    }

    public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, String... otherParams) {
        return join(map, separator, keyValueSeparator, false, otherParams);
    }

    public static String sortJoin(Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        return join(sort(params), separator, keyValueSeparator, isIgnoreNull, otherParams);
    }

    public static <K, V> String joinIgnoreNull(Map<K, V> map, String separator, String keyValueSeparator, String... otherParams) {
        return join(map, separator, keyValueSeparator, true, otherParams);
    }

    public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
        StringBuilder strBuilder = StrUtil.builder();
        boolean isFirst = true;
        if (isNotEmpty(map)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                if (!isIgnoreNull || (entry.getKey() != null && entry.getValue() != null)) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        strBuilder.append(separator);
                    }
                    strBuilder.append(Convert.toStr(entry.getKey()));
                    strBuilder.append(keyValueSeparator);
                    strBuilder.append(Convert.toStr(entry.getValue()));
                }
            }
        }
        if (ArrayUtil.isNotEmpty((Object[]) otherParams)) {
            for (String otherParam : otherParams) {
                strBuilder.append(otherParam);
            }
        }
        return strBuilder.toString();
    }

    public static <K, V> Map<K, V> edit(Map<K, V> map, Editor<Map.Entry<K, V>> editor) {
        if (map == null || editor == null) {
            return map;
        }
        Map<K, V> map2 = (Map) ObjectUtil.clone(map);
        if (map2 == null) {
            map2 = new HashMap(map.size(), 1.0f);
        }
        if (isEmpty(map2)) {
            return map2;
        }
        try {
            map2.clear();
        } catch (UnsupportedOperationException e) {
            map2 = new HashMap<>(map.size(), 1.0f);
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Map.Entry<K, V> modified = editor.edit(entry);
            if (modified != null) {
                map2.put(modified.getKey(), modified.getValue());
            }
        }
        return map2;
    }

    public static <K, V> Map<K, V> filter(Map<K, V> map, final Filter<Map.Entry<K, V>> filter) {
        if (map == null || filter == null) {
            return map;
        }
        return edit(map, new Editor() { // from class: cn.hutool.core.map.-$$Lambda$MapUtil$wfkDS1HIFc4x7VhXIU_A9MhDGlY
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return MapUtil.lambda$filter$0(Filter.this, (Map.Entry) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Map.Entry lambda$filter$0(Filter filter, Map.Entry t) {
        if (filter.accept(t)) {
            return t;
        }
        return null;
    }

    public static <K, V> Map<K, V> filter(Map<K, V> map, K... keys) {
        if (map == null || keys == null) {
            return map;
        }
        Map<K, V> map2 = (Map) ObjectUtil.clone(map);
        if (map2 == null) {
            map2 = new HashMap(map.size(), 1.0f);
        }
        if (isEmpty(map2)) {
            return map2;
        }
        try {
            map2.clear();
        } catch (UnsupportedOperationException e) {
            map2 = new HashMap<>();
        }
        for (K key : keys) {
            if (map.containsKey(key)) {
                map2.put(key, map.get(key));
            }
        }
        return map2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Map.Entry lambda$reverse$1(final Map.Entry t) {
        return new Map.Entry<T, T>() { // from class: cn.hutool.core.map.MapUtil.1
            @Override // java.util.Map.Entry
            public T getKey() {
                return (T) t.getValue();
            }

            @Override // java.util.Map.Entry
            public T getValue() {
                return (T) t.getKey();
            }

            @Override // java.util.Map.Entry
            public T setValue(T t2) {
                throw new UnsupportedOperationException("Unsupported setValue method !");
            }
        };
    }

    public static <T> Map<T, T> reverse(Map<T, T> map) {
        return edit(map, new Editor() { // from class: cn.hutool.core.map.-$$Lambda$MapUtil$cTzM5uR4xF2DEAvlcmKiCVuT4SA
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return MapUtil.lambda$reverse$1((Map.Entry) obj);
            }
        });
    }

    public static <K, V> Map<V, K> inverse(Map<K, V> map) {
        final Map<V, K> result = createMap(map.getClass());
        map.forEach(new BiConsumer() { // from class: cn.hutool.core.map.-$$Lambda$MapUtil$sOSS551Qicva-yRSCtT_xRfSs_E
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                result.put(obj2, obj);
            }
        });
        return result;
    }

    public static <K, V> TreeMap<K, V> sort(Map<K, V> map) {
        return sort(map, null);
    }

    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        if (map == null) {
            return null;
        }
        if (map instanceof TreeMap) {
            TreeMap<K, V> result = (TreeMap) map;
            if (comparator == null || comparator.equals(result.comparator())) {
                return result;
            }
        }
        return newTreeMap(map, comparator);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        final Map<K, V> result = new LinkedHashMap<>();
        Comparator<? super Map.Entry<K, V>> comparingByValue = Map.Entry.comparingByValue();
        if (isDesc) {
            comparingByValue = comparingByValue.reversed();
        }
        map.entrySet().stream().sorted(comparingByValue).forEachOrdered(new Consumer() { // from class: cn.hutool.core.map.-$$Lambda$MapUtil$i8t3g6sigDruY7Pko9mOAbrcHUI
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                MapUtil.lambda$sortByValue$3(result, (Map.Entry) obj);
            }
        });
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sortByValue$3(Map result, Map.Entry e) {
        Comparable comparable = (Comparable) result.put(e.getKey(), e.getValue());
    }

    public static MapProxy createProxy(Map<?, ?> map) {
        return MapProxy.create(map);
    }

    public static <K, V> MapWrapper<K, V> wrap(Map<K, V> map) {
        return new MapWrapper<>(map);
    }

    public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return builder(new HashMap());
    }

    public static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    public static <K, V> MapBuilder<K, V> builder(K k, V v) {
        return builder(new HashMap()).put(k, v);
    }

    public static <K, V> Map<K, V> getAny(Map<K, V> map, final K... keys) {
        return filter(map, new Filter() { // from class: cn.hutool.core.map.-$$Lambda$MapUtil$N1IEP6d_uh7thF4Gz15zy2dOAlQ
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                boolean contains;
                contains = ArrayUtil.contains(keys, ((Map.Entry) obj).getKey());
                return contains;
            }
        });
    }

    public static <K, V> Map<K, V> removeAny(Map<K, V> map, K... keys) {
        for (K key : keys) {
            map.remove(key);
        }
        return map;
    }

    public static String getStr(Map<?, ?> map, Object key) {
        return (String) get(map, key, String.class);
    }

    public static String getStr(Map<?, ?> map, Object key, String defaultValue) {
        return (String) get(map, key, String.class, defaultValue);
    }

    public static Integer getInt(Map<?, ?> map, Object key) {
        return (Integer) get(map, key, Integer.class);
    }

    public static Integer getInt(Map<?, ?> map, Object key, Integer defaultValue) {
        return (Integer) get(map, key, Integer.class, defaultValue);
    }

    public static Double getDouble(Map<?, ?> map, Object key) {
        return (Double) get(map, key, Double.class);
    }

    public static Double getDouble(Map<?, ?> map, Object key, Double defaultValue) {
        return (Double) get(map, key, Double.class, defaultValue);
    }

    public static Float getFloat(Map<?, ?> map, Object key) {
        return (Float) get(map, key, Float.class);
    }

    public static Float getFloat(Map<?, ?> map, Object key, Float defaultValue) {
        return (Float) get(map, key, Float.class, defaultValue);
    }

    public static Short getShort(Map<?, ?> map, Object key) {
        return (Short) get(map, key, Short.class);
    }

    public static Short getShort(Map<?, ?> map, Object key, Short defaultValue) {
        return (Short) get(map, key, Short.class, defaultValue);
    }

    public static Boolean getBool(Map<?, ?> map, Object key) {
        return (Boolean) get(map, key, Boolean.class);
    }

    public static Boolean getBool(Map<?, ?> map, Object key, Boolean defaultValue) {
        return (Boolean) get(map, key, Boolean.class, defaultValue);
    }

    public static Character getChar(Map<?, ?> map, Object key) {
        return (Character) get(map, key, Character.class);
    }

    public static Character getChar(Map<?, ?> map, Object key, Character defaultValue) {
        return (Character) get(map, key, Character.class, defaultValue);
    }

    public static Long getLong(Map<?, ?> map, Object key) {
        return (Long) get(map, key, Long.class);
    }

    public static Long getLong(Map<?, ?> map, Object key, Long defaultValue) {
        return (Long) get(map, key, Long.class, defaultValue);
    }

    public static Date getDate(Map<?, ?> map, Object key) {
        return (Date) get(map, key, Date.class);
    }

    public static Date getDate(Map<?, ?> map, Object key, Date defaultValue) {
        return (Date) get(map, key, Date.class, defaultValue);
    }

    public static <T> T get(Map<?, ?> map, Object key, Class<T> type) {
        return (T) get(map, key, type, (Object) null);
    }

    public static <T> T get(Map<?, ?> map, Object key, Class<T> type, T defaultValue) {
        return map == null ? defaultValue : (T) Convert.convert((Class) type, map.get(key), (Object) defaultValue);
    }

    public static <T> T getQuietly(Map<?, ?> map, Object key, Class<T> type, T defaultValue) {
        return map == null ? defaultValue : (T) Convert.convertQuietly(type, map.get(key), defaultValue);
    }

    public static <T> T get(Map<?, ?> map, Object key, TypeReference<T> type) {
        return (T) get(map, key, type, (Object) null);
    }

    public static <T> T get(Map<?, ?> map, Object key, TypeReference<T> type, T defaultValue) {
        return map == null ? defaultValue : (T) Convert.convert(type, map.get(key), defaultValue);
    }

    public static <T> T getQuietly(Map<?, ?> map, Object key, TypeReference<T> type, T defaultValue) {
        return map == null ? defaultValue : (T) Convert.convertQuietly(type, map.get(key), defaultValue);
    }

    public static <K, V> Map<K, V> renameKey(Map<K, V> map, K oldKey, K newKey) {
        if (isNotEmpty(map) && map.containsKey(oldKey)) {
            if (map.containsKey(newKey)) {
                throw new IllegalArgumentException(StrUtil.format("The key '{}' exist !", newKey));
            }
            map.put(newKey, map.remove(oldKey));
        }
        return map;
    }

    public static <K, V> Map<K, V> removeNullValue(Map<K, V> map) {
        if (isEmpty(map)) {
            return map;
        }
        Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<K, V> entry = iter.next();
            if (entry.getValue() == null) {
                iter.remove();
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> empty() {
        return Collections.emptyMap();
    }

    public static <K, V, T extends Map<K, V>> T empty(Class<?> mapClass) {
        if (mapClass == null) {
            return (T) Collections.emptyMap();
        }
        if (NavigableMap.class == mapClass) {
            return Collections.emptyNavigableMap();
        }
        if (SortedMap.class == mapClass) {
            return Collections.emptySortedMap();
        }
        if (Map.class == mapClass) {
            return (T) Collections.emptyMap();
        }
        throw new IllegalArgumentException(StrUtil.format("[{}] is not support to get empty!", mapClass));
    }

    public static void clear(Map<?, ?>... maps) {
        for (Map<?, ?> map : maps) {
            if (isNotEmpty(map)) {
                map.clear();
            }
        }
    }
}
