package cn.hutool.core.collection;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.comparator.PropertyComparator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.hash.Hash32;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class CollUtil {

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface Consumer<T> extends Serializable {
        void accept(T t, int i);
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface KVConsumer<K, V> extends Serializable {
        void accept(K k, V v, int i);
    }

    public static <T> Set<T> emptyIfNull(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T> Collection<T> union(Collection<T> coll1, Collection<T> coll2) {
        if (isEmpty((Collection<?>) coll1)) {
            return new ArrayList(coll2);
        }
        if (isEmpty((Collection<?>) coll2)) {
            return new ArrayList(coll1);
        }
        ArrayList arrayList = new ArrayList(Math.max(coll1.size(), coll2.size()));
        Map<T, Integer> map1 = countMap(coll1);
        Map<T, Integer> map2 = countMap(coll2);
        Set<T> elts = newHashSet(coll2);
        elts.addAll(coll1);
        for (T t : elts) {
            int m = Math.max(Convert.toInt(map1.get(t), 0).intValue(), Convert.toInt(map2.get(t), 0).intValue());
            for (int i = 0; i < m; i++) {
                arrayList.add(t);
            }
        }
        return arrayList;
    }

    @SafeVarargs
    public static <T> Collection<T> union(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        Collection<T> union = union(coll1, coll2);
        for (Collection<T> coll : otherColls) {
            union = union(union, coll);
        }
        return union;
    }

    @SafeVarargs
    public static <T> Set<T> unionDistinct(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        Set<T> result;
        if (isEmpty((Collection<?>) coll1)) {
            result = new LinkedHashSet<>();
        } else {
            result = new LinkedHashSet<>((Collection<? extends T>) coll1);
        }
        if (isNotEmpty((Collection<?>) coll2)) {
            result.addAll(coll2);
        }
        if (ArrayUtil.isNotEmpty((Object[]) otherColls)) {
            for (Collection<T> otherColl : otherColls) {
                result.addAll(otherColl);
            }
        }
        return result;
    }

    @SafeVarargs
    public static <T> List<T> unionAll(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        List<T> result;
        if (isEmpty((Collection<?>) coll1)) {
            result = new ArrayList<>();
        } else {
            result = new ArrayList<>((Collection<? extends T>) coll1);
        }
        if (isNotEmpty((Collection<?>) coll2)) {
            result.addAll(coll2);
        }
        if (ArrayUtil.isNotEmpty((Object[]) otherColls)) {
            for (Collection<T> otherColl : otherColls) {
                result.addAll(otherColl);
            }
        }
        return result;
    }

    public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2) {
        if (isNotEmpty((Collection<?>) coll1) && isNotEmpty((Collection<?>) coll2)) {
            ArrayList arrayList = new ArrayList(Math.min(coll1.size(), coll2.size()));
            Map<T, Integer> map1 = countMap(coll1);
            Map<T, Integer> map2 = countMap(coll2);
            Set<T> elts = newHashSet(coll2);
            for (T t : elts) {
                int m = Math.min(Convert.toInt(map1.get(t), 0).intValue(), Convert.toInt(map2.get(t), 0).intValue());
                for (int i = 0; i < m; i++) {
                    arrayList.add(t);
                }
            }
            return arrayList;
        }
        return new ArrayList();
    }

    @SafeVarargs
    public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        Collection<T> intersection = intersection(coll1, coll2);
        if (isEmpty((Collection<?>) intersection)) {
            return intersection;
        }
        for (Collection<T> coll : otherColls) {
            intersection = intersection(intersection, coll);
            if (isEmpty((Collection<?>) intersection)) {
                return intersection;
            }
        }
        return intersection;
    }

    @SafeVarargs
    public static <T> Set<T> intersectionDistinct(Collection<T> coll1, Collection<T> coll2, Collection<T>... otherColls) {
        if (isEmpty((Collection<?>) coll1) || isEmpty((Collection<?>) coll2)) {
            return new LinkedHashSet();
        }
        Set<T> result = new LinkedHashSet<>((Collection<? extends T>) coll1);
        if (ArrayUtil.isNotEmpty((Object[]) otherColls)) {
            for (Collection<T> otherColl : otherColls) {
                if (isNotEmpty((Collection<?>) otherColl)) {
                    result.retainAll(otherColl);
                } else {
                    return new LinkedHashSet();
                }
            }
        }
        result.retainAll(coll2);
        return result;
    }

    public static <T> Collection<T> disjunction(Collection<T> coll1, Collection<T> coll2) {
        if (isEmpty((Collection<?>) coll1)) {
            return coll2;
        }
        if (isEmpty((Collection<?>) coll2)) {
            return coll1;
        }
        ArrayList arrayList = new ArrayList();
        Map<T, Integer> map1 = countMap(coll1);
        Map<T, Integer> map2 = countMap(coll2);
        Set<T> elts = newHashSet(coll2);
        elts.addAll(coll1);
        for (T t : elts) {
            int m = Math.abs(Convert.toInt(map1.get(t), 0).intValue() - Convert.toInt(map2.get(t), 0).intValue());
            for (int i = 0; i < m; i++) {
                arrayList.add(t);
            }
        }
        return arrayList;
    }

    public static <T> Collection<T> subtract(Collection<T> coll1, Collection<T> coll2) {
        Collection<T> result = (Collection) ObjectUtil.clone(coll1);
        if (result == null) {
            result = create(coll1.getClass());
            result.addAll(coll1);
        }
        result.removeAll(coll2);
        return result;
    }

    public static <T> List<T> subtractToList(Collection<T> coll1, Collection<T> coll2) {
        if (isEmpty((Collection<?>) coll1)) {
            return ListUtil.empty();
        }
        if (isEmpty((Collection<?>) coll2)) {
            return ListUtil.list(true, (Collection) coll1);
        }
        List<T> result = new LinkedList<>();
        Set<T> set = new HashSet<>((Collection<? extends T>) coll2);
        for (T t : coll1) {
            if (!set.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static boolean contains(Collection<?> collection, Object value) {
        return isNotEmpty(collection) && collection.contains(value);
    }

    public static <T> boolean contains(Collection<T> collection, Predicate<? super T> containFunc) {
        if (isEmpty((Collection<?>) collection)) {
            return false;
        }
        for (T t : collection) {
            if (containFunc.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object2 : coll2) {
                if (coll1.contains(object2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1)) {
            return isEmpty(coll2);
        }
        if (isEmpty(coll2)) {
            return true;
        }
        if (coll1.size() < coll2.size()) {
            return false;
        }
        for (Object object : coll2) {
            if (!coll1.contains(object)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Map<T, Integer> countMap(Iterable<T> collection) {
        return IterUtil.countMap(collection == null ? null : collection.iterator());
    }

    public static <T> String join(Iterable<T> iterable, CharSequence conjunction, Function<T, ? extends CharSequence> func) {
        if (iterable == null) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction, func);
    }

    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        if (iterable == null) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction);
    }

    public static <T> String join(Iterable<T> iterable, CharSequence conjunction, String prefix, String suffix) {
        if (iterable == null) {
            return null;
        }
        return IterUtil.join(iterable.iterator(), conjunction, prefix, suffix);
    }

    @Deprecated
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        return IterUtil.join(iterator, conjunction);
    }

    public static <T> List<T> popPart(Stack<T> surplusAlaDatas, int partSize) {
        if (isEmpty((Collection<?>) surplusAlaDatas)) {
            return ListUtil.empty();
        }
        List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i2 = 0; i2 < size; i2++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    public static <T> List<T> popPart(Deque<T> surplusAlaDatas, int partSize) {
        if (isEmpty((Collection<?>) surplusAlaDatas)) {
            return ListUtil.empty();
        }
        List<T> currentAlaDatas = new ArrayList<>();
        int size = surplusAlaDatas.size();
        if (size > partSize) {
            for (int i = 0; i < partSize; i++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        } else {
            for (int i2 = 0; i2 < size; i2++) {
                currentAlaDatas.add(surplusAlaDatas.pop());
            }
        }
        return currentAlaDatas;
    }

    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... ts) {
        return set(false, ts);
    }

    @SafeVarargs
    public static <T> LinkedHashSet<T> newLinkedHashSet(T... ts) {
        return (LinkedHashSet) set(true, ts);
    }

    @SafeVarargs
    public static <T> HashSet<T> set(boolean isSorted, T... ts) {
        if (ts == null) {
            return isSorted ? new LinkedHashSet() : new HashSet<>();
        }
        int initialCapacity = Math.max(((int) (ts.length / 0.75f)) + 1, 16);
        HashSet<T> set = isSorted ? new LinkedHashSet<>(initialCapacity) : new HashSet<>(initialCapacity);
        Collections.addAll(set, ts);
        return set;
    }

    public static <T> HashSet<T> newHashSet(Collection<T> collection) {
        return newHashSet(false, (Collection) collection);
    }

    public static <T> HashSet<T> newHashSet(boolean isSorted, Collection<T> collection) {
        return isSorted ? new LinkedHashSet(collection) : new HashSet<>((Collection<? extends T>) collection);
    }

    public static <T> HashSet<T> newHashSet(boolean isSorted, Iterator<T> iter) {
        if (iter == null) {
            return set(isSorted, null);
        }
        HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (iter.hasNext()) {
            set.add(iter.next());
        }
        return set;
    }

    public static <T> HashSet<T> newHashSet(boolean isSorted, Enumeration<T> enumeration) {
        if (enumeration == null) {
            return set(isSorted, null);
        }
        HashSet<T> set = isSorted ? new LinkedHashSet<>() : new HashSet<>();
        while (enumeration.hasMoreElements()) {
            set.add(enumeration.nextElement());
        }
        return set;
    }

    public static <T> List<T> list(boolean isLinked) {
        return ListUtil.list(isLinked);
    }

    @SafeVarargs
    public static <T> List<T> list(boolean isLinked, T... values) {
        return ListUtil.list(isLinked, values);
    }

    public static <T> List<T> list(boolean isLinked, Collection<T> collection) {
        return ListUtil.list(isLinked, (Collection) collection);
    }

    public static <T> List<T> list(boolean isLinked, Iterable<T> iterable) {
        return ListUtil.list(isLinked, iterable);
    }

    public static <T> List<T> list(boolean isLinked, Iterator<T> iter) {
        return ListUtil.list(isLinked, iter);
    }

    public static <T> List<T> list(boolean isLinked, Enumeration<T> enumeration) {
        return ListUtil.list(isLinked, enumeration);
    }

    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... values) {
        return ListUtil.toList(values);
    }

    @SafeVarargs
    public static <T> ArrayList<T> toList(T... values) {
        return ListUtil.toList(values);
    }

    public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
        return ListUtil.toList((Collection) collection);
    }

    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable) {
        return ListUtil.toList(iterable);
    }

    public static <T> ArrayList<T> newArrayList(Iterator<T> iterator) {
        return ListUtil.toList(iterator);
    }

    public static <T> ArrayList<T> newArrayList(Enumeration<T> enumeration) {
        return ListUtil.toList(enumeration);
    }

    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(T... values) {
        return ListUtil.toLinkedList(values);
    }

    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Collection<T> collection) {
        return ListUtil.toCopyOnWriteArrayList(collection);
    }

    public static <T> BlockingQueue<T> newBlockingQueue(int capacity, boolean isLinked) {
        if (isLinked) {
            BlockingQueue<T> queue = new LinkedBlockingDeque<>(capacity);
            return queue;
        }
        BlockingQueue<T> queue2 = new ArrayBlockingQueue<>(capacity);
        return queue2;
    }

    public static <T> Collection<T> create(Class<?> collectionType) {
        if (collectionType.isAssignableFrom(AbstractCollection.class)) {
            Collection<T> list = new ArrayList<>();
            return list;
        } else if (collectionType.isAssignableFrom(HashSet.class)) {
            Collection<T> list2 = new HashSet<>();
            return list2;
        } else if (collectionType.isAssignableFrom(LinkedHashSet.class)) {
            Collection<T> list3 = new LinkedHashSet<>();
            return list3;
        } else if (collectionType.isAssignableFrom(TreeSet.class)) {
            Collection<T> list4 = new TreeSet<>(new Comparator() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$_EcfVDyLOv-YruGvoYSsMFhV28I
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    return CollUtil.lambda$create$0(obj, obj2);
                }
            });
            return list4;
        } else if (collectionType.isAssignableFrom(EnumSet.class)) {
            Collection<T> list5 = EnumSet.noneOf(ClassUtil.getTypeArgument(collectionType));
            return list5;
        } else if (collectionType.isAssignableFrom(ArrayList.class)) {
            Collection<T> list6 = new ArrayList<>();
            return list6;
        } else if (collectionType.isAssignableFrom(LinkedList.class)) {
            Collection<T> list7 = new LinkedList<>();
            return list7;
        } else {
            try {
                Collection<T> list8 = (Collection) ReflectUtil.newInstance(collectionType, new Object[0]);
                return list8;
            } catch (Exception e) {
                Class<?> superclass = collectionType.getSuperclass();
                if (superclass != null && collectionType != superclass) {
                    return create(superclass);
                }
                throw new UtilException(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$create$0(Object o1, Object o2) {
        if (o1 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        }
        return CompareUtil.compare(o1.toString(), o2.toString());
    }

    public static <T> ArrayList<T> distinct(Collection<T> collection) {
        if (isEmpty((Collection<?>) collection)) {
            return new ArrayList<>();
        }
        if (collection instanceof Set) {
            return new ArrayList<>((Collection<? extends T>) collection);
        }
        return new ArrayList<>(new LinkedHashSet(collection));
    }

    public static <T> List<T> sub(List<T> list, int start, int end) {
        return ListUtil.sub(list, start, end);
    }

    public static <T> List<T> sub(List<T> list, int start, int end, int step) {
        return ListUtil.sub(list, start, end, step);
    }

    public static <T> List<T> sub(Collection<T> collection, int start, int end) {
        return sub(collection, start, end, 1);
    }

    public static <T> List<T> sub(Collection<T> collection, int start, int end, int step) {
        if (isEmpty((Collection<?>) collection)) {
            return ListUtil.empty();
        }
        List<T> list = collection instanceof List ? (List) collection : ListUtil.toList((Collection) collection);
        return sub((List) list, start, end, step);
    }

    @Deprecated
    public static <T> List<List<T>> splitList(List<T> list, int size) {
        return ListUtil.partition(list, size);
    }

    public static <T> List<List<T>> split(Collection<T> collection, int size) {
        List<List<T>> result = new ArrayList<>();
        if (isEmpty((Collection<?>) collection)) {
            return result;
        }
        ArrayList<T> subList = new ArrayList<>(size);
        for (T t : collection) {
            if (subList.size() >= size) {
                result.add(subList);
                subList = new ArrayList<>(size);
            }
            subList.add(t);
        }
        result.add(subList);
        return result;
    }

    public static <T> Collection<T> edit(Collection<T> collection, Editor<T> editor) {
        if (collection == null || editor == null) {
            return collection;
        }
        Collection<T> collection2 = (Collection) ObjectUtil.clone(collection);
        if (collection2 == null) {
            collection2 = create(collection.getClass());
        }
        if (isEmpty((Collection<?>) collection2)) {
            return collection2;
        }
        try {
            collection2.clear();
        } catch (UnsupportedOperationException e) {
            collection2 = new ArrayList<>();
        }
        for (T t : collection) {
            T modified = editor.edit(t);
            if (modified != null) {
                collection2.add(modified);
            }
        }
        return collection2;
    }

    public static <T> Collection<T> filterNew(Collection<T> collection, final Filter<T> filter) {
        if (collection == null || filter == null) {
            return collection;
        }
        return edit(collection, new Editor() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$LgTUN5OobLHWXmnK45_Mghxcx9g
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return CollUtil.lambda$filterNew$1(Filter.this, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$filterNew$1(Filter filter, Object t) {
        if (filter.accept(t)) {
            return t;
        }
        return null;
    }

    public static <T extends Collection<E>, E> T removeAny(T collection, E... elesRemoved) {
        collection.removeAll(newHashSet(elesRemoved));
        return collection;
    }

    public static <T extends Collection<E>, E> T filter(T collection, Filter<E> filter) {
        return (T) IterUtil.filter(collection, filter);
    }

    public static <T extends Collection<E>, E> T removeNull(T collection) {
        return (T) filter(collection, new Filter() { // from class: cn.hutool.core.collection.-$$Lambda$An-bsRXUd7eOA1o5SomBFqKIVZE
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return Objects.nonNull(obj);
            }
        });
    }

    public static <T extends Collection<E>, E extends CharSequence> T removeEmpty(T collection) {
        return (T) filter(collection, new Filter() { // from class: cn.hutool.core.collection.-$$Lambda$QI51b42SJhF8tIbIKFHCHRa65zg
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CharSequenceUtil.isNotEmpty((CharSequence) obj);
            }
        });
    }

    public static <T extends Collection<E>, E extends CharSequence> T removeBlank(T collection) {
        return (T) filter(collection, new Filter() { // from class: cn.hutool.core.collection.-$$Lambda$5fyZGSzcEmQV7agOitOinPffspo
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CharSequenceUtil.isNotBlank((CharSequence) obj);
            }
        });
    }

    public static List<Object> extract(Iterable<?> collection, Editor<Object> editor) {
        return extract(collection, editor, false);
    }

    public static List<Object> extract(Iterable<?> collection, final Editor<Object> editor, boolean ignoreNull) {
        editor.getClass();
        return map(collection, new Function() { // from class: cn.hutool.core.collection.-$$Lambda$RpLfI4tSLzywprkKAOr_tYM-sFI
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Editor.this.edit(obj);
            }
        }, ignoreNull);
    }

    public static <T, R> List<R> map(Iterable<T> collection, Function<? super T, ? extends R> func, boolean ignoreNull) {
        List<R> fieldValueList = new ArrayList<>();
        if (collection == null) {
            return fieldValueList;
        }
        for (T t : collection) {
            if (t != null || !ignoreNull) {
                R value = func.apply(t);
                if (value != null || !ignoreNull) {
                    fieldValueList.add(value);
                }
            }
        }
        return fieldValueList;
    }

    public static List<Object> getFieldValues(Iterable<?> collection, String fieldName) {
        return getFieldValues(collection, fieldName, false);
    }

    public static List<Object> getFieldValues(Iterable<?> collection, final String fieldName, boolean ignoreNull) {
        return map(collection, new Function() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$HoNNRVy79HQ72Q0V4uRLEVJGvgc
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return CollUtil.lambda$getFieldValues$2(fieldName, obj);
            }
        }, ignoreNull);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$getFieldValues$2(String fieldName, Object bean) {
        if (bean instanceof Map) {
            return ((Map) bean).get(fieldName);
        }
        return ReflectUtil.getFieldValue(bean, fieldName);
    }

    public static <T> List<T> getFieldValues(Iterable<?> collection, String fieldName, Class<T> elementType) {
        List<Object> fieldValues = getFieldValues(collection, fieldName);
        return Convert.toList(elementType, fieldValues);
    }

    public static <K, V> Map<K, V> fieldValueMap(Iterable<V> iterable, String fieldName) {
        return IterUtil.fieldValueMap(IterUtil.getIter(iterable), fieldName);
    }

    public static <K, V> Map<K, V> fieldValueAsMap(Iterable<?> iterable, String fieldNameForKey, String fieldNameForValue) {
        return IterUtil.fieldValueAsMap(IterUtil.getIter(iterable), fieldNameForKey, fieldNameForValue);
    }

    public static <T> T findOne(Iterable<T> collection, Filter<T> filter) {
        if (collection != null) {
            for (T t : collection) {
                if (filter.accept(t)) {
                    return t;
                }
            }
            return null;
        }
        return null;
    }

    public static <T> T findOneByField(Iterable<T> collection, final String fieldName, final Object fieldValue) {
        return (T) findOne(collection, new Filter() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$M73E-WKYBhV8WF8SI01Ic0VCJgI
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CollUtil.lambda$findOneByField$3(fieldName, fieldValue, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$findOneByField$3(String fieldName, Object fieldValue, Object t) {
        if (t instanceof Map) {
            Map<?, ?> map = (Map) t;
            Object value = map.get(fieldName);
            return ObjectUtil.equal(value, fieldValue);
        }
        Object value2 = ReflectUtil.getFieldValue(t, fieldName);
        return ObjectUtil.equal(value2, fieldValue);
    }

    public static <T> int count(Iterable<T> iterable, Matcher<T> matcher) {
        int count = 0;
        if (iterable != null) {
            for (T t : iterable) {
                if (matcher == null || matcher.match(t)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static <T> int indexOf(Collection<T> collection, Matcher<T> matcher) {
        if (isNotEmpty((Collection<?>) collection)) {
            int index = 0;
            for (T t : collection) {
                if (matcher == null || matcher.match(t)) {
                    return index;
                }
                index++;
            }
            return -1;
        }
        return -1;
    }

    public static <T> int lastIndexOf(Collection<T> collection, Matcher<T> matcher) {
        if (collection instanceof List) {
            return ListUtil.lastIndexOf((List) collection, matcher);
        }
        int matchIndex = -1;
        if (isNotEmpty((Collection<?>) collection)) {
            int index = collection.size();
            for (T t : collection) {
                if (matcher == null || matcher.match(t)) {
                    matchIndex = index;
                }
                index--;
            }
        }
        return matchIndex;
    }

    public static <T> int[] indexOfAll(Collection<T> collection, Matcher<T> matcher) {
        List<Integer> indexList = new ArrayList<>();
        if (collection != null) {
            int index = 0;
            for (T t : collection) {
                if (matcher == null || matcher.match(t)) {
                    indexList.add(Integer.valueOf(index));
                }
                index++;
            }
        }
        return (int[]) Convert.convert((Class<Object>) int[].class, (Object) indexList);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T extends Collection<E>, E> T defaultIfEmpty(T collection, T defaultCollection) {
        return isEmpty((Collection<?>) collection) ? defaultCollection : collection;
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return IterUtil.isEmpty(iterable);
    }

    public static boolean isEmpty(Iterator<?> Iterator) {
        return IterUtil.isEmpty(Iterator);
    }

    public static boolean isEmpty(Enumeration<?> enumeration) {
        return enumeration == null || !enumeration.hasMoreElements();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtil.isEmpty(map);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return IterUtil.isNotEmpty(iterable);
    }

    public static boolean isNotEmpty(Iterator<?> Iterator) {
        return IterUtil.isNotEmpty(Iterator);
    }

    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return enumeration != null && enumeration.hasMoreElements();
    }

    public static boolean hasNull(Iterable<?> iterable) {
        return IterUtil.hasNull(iterable);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtil.isNotEmpty(map);
    }

    public static Map<String, String> zip(String keys, String values, String delimiter, boolean isOrder) {
        return ArrayUtil.zip(StrUtil.splitToArray(keys, delimiter), StrUtil.splitToArray(values, delimiter), isOrder);
    }

    public static Map<String, String> zip(String keys, String values, String delimiter) {
        return zip(keys, values, delimiter, false);
    }

    public static <K, V> Map<K, V> zip(Collection<K> keys, Collection<V> values) {
        if (isEmpty((Collection<?>) keys) || isEmpty((Collection<?>) values)) {
            return MapUtil.empty();
        }
        int entryCount = Math.min(keys.size(), values.size());
        Map<K, V> map = MapUtil.newHashMap(entryCount);
        Iterator<K> keyIterator = keys.iterator();
        Iterator<V> valueIterator = values.iterator();
        while (entryCount > 0) {
            map.put(keyIterator.next(), valueIterator.next());
            entryCount--;
        }
        return map;
    }

    public static <K, V> HashMap<K, V> toMap(Iterable<Map.Entry<K, V>> entryIter) {
        return IterUtil.toMap(entryIter);
    }

    public static HashMap<Object, Object> toMap(Object[] array) {
        return MapUtil.of(array);
    }

    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
        TreeSet<T> treeSet = new TreeSet<>((Comparator<? super T>) comparator);
        treeSet.addAll(collection);
        return treeSet;
    }

    public static <E> Enumeration<E> asEnumeration(Iterator<E> iter) {
        return new IteratorEnumeration(iter);
    }

    public static <E> Iterator<E> asIterator(Enumeration<E> e) {
        return IterUtil.asIterator(e);
    }

    public static <E> Iterable<E> asIterable(Iterator<E> iter) {
        return IterUtil.asIterable(iter);
    }

    public static <E> Collection<E> toCollection(Iterable<E> iterable) {
        return iterable instanceof Collection ? (Collection) iterable : newArrayList(iterable.iterator());
    }

    public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
        return MapUtil.toListMap(mapList);
    }

    public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
        return MapUtil.toMapList(listMap);
    }

    public static <K, V> Map<K, V> toMap(Iterable<V> values, Map<K, V> map, Func1<V, K> keyFunc) {
        return IterUtil.toMap(values == null ? null : values.iterator(), map, keyFunc);
    }

    public static <K, V, E> Map<K, V> toMap(Iterable<E> values, Map<K, V> map, Func1<E, K> keyFunc, Func1<E, V> valueFunc) {
        return IterUtil.toMap(values == null ? null : values.iterator(), map, keyFunc, valueFunc);
    }

    public static <T> Collection<T> addAll(Collection<T> collection, Object value) {
        return addAll(collection, value, TypeUtil.getTypeArgument(collection.getClass()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Collection<T> addAll(Collection<T> collection, Object value, Type elementType) {
        Iterator iter;
        if (collection == 0 || value == null) {
            return collection;
        }
        if (TypeUtil.isUnknown(elementType)) {
            elementType = Object.class;
        }
        if (value instanceof Iterator) {
            iter = (Iterator) value;
        } else if (value instanceof Iterable) {
            iter = ((Iterable) value).iterator();
        } else if (value instanceof Enumeration) {
            iter = new EnumerationIter((Enumeration) value);
        } else if (ArrayUtil.isArray(value)) {
            iter = new ArrayIter(value);
        } else if (value instanceof CharSequence) {
            String ArrayStr = StrUtil.unWrap((CharSequence) value, '[', ']');
            iter = StrUtil.splitTrim((CharSequence) ArrayStr, ',').iterator();
        } else {
            iter = newArrayList(value).iterator();
        }
        ConverterRegistry convert = ConverterRegistry.getInstance();
        while (iter.hasNext()) {
            collection.add(convert.convert(elementType, iter.next()));
        }
        return collection;
    }

    public static <T> Collection<T> addAll(Collection<T> collection, Iterator<T> iterator) {
        if (collection != null && iterator != null) {
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        return collection;
    }

    public static <T> Collection<T> addAll(Collection<T> collection, Iterable<T> iterable) {
        if (iterable == null) {
            return collection;
        }
        return addAll((Collection) collection, (Iterator) iterable.iterator());
    }

    public static <T> Collection<T> addAll(Collection<T> collection, Enumeration<T> enumeration) {
        if (collection != null && enumeration != null) {
            while (enumeration.hasMoreElements()) {
                collection.add(enumeration.nextElement());
            }
        }
        return collection;
    }

    public static <T> Collection<T> addAll(Collection<T> collection, T[] values) {
        if (collection != null && values != null) {
            Collections.addAll(collection, values);
        }
        return collection;
    }

    public static <T> List<T> addAllIfNotContains(List<T> list, List<T> otherList) {
        for (T t : otherList) {
            if (!list.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> T get(Collection<T> collection, int index) {
        int size;
        if (collection == null || (size = collection.size()) == 0) {
            return null;
        }
        if (index < 0) {
            index += size;
        }
        if (index >= size || index < 0) {
            return null;
        }
        if (collection instanceof List) {
            List<T> list = (List) collection;
            return list.get(index);
        }
        int i = 0;
        for (T t : collection) {
            if (i > index) {
                break;
            } else if (i == index) {
                return t;
            } else {
                i++;
            }
        }
        return null;
    }

    public static <T> List<T> getAny(Collection<T> collection, int... indexes) {
        int size = collection.size();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        if (collection instanceof List) {
            List<T> list = (List) collection;
            int length = indexes.length;
            while (i < length) {
                int index = indexes[i];
                if (index < 0) {
                    index += size;
                }
                arrayList.add(list.get(index));
                i++;
            }
        } else {
            Object[] array = collection.toArray();
            int length2 = indexes.length;
            while (i < length2) {
                int index2 = indexes[i];
                if (index2 < 0) {
                    index2 += size;
                }
                arrayList.add(array[index2]);
                i++;
            }
        }
        return arrayList;
    }

    public static <T> T getFirst(Iterable<T> iterable) {
        return (T) IterUtil.getFirst(iterable);
    }

    public static <T> T getFirst(Iterator<T> iterator) {
        return (T) IterUtil.getFirst(iterator);
    }

    public static <T> T getLast(Collection<T> collection) {
        return (T) get(collection, -1);
    }

    public static Class<?> getElementType(Iterable<?> iterable) {
        return IterUtil.getElementType(iterable);
    }

    public static Class<?> getElementType(Iterator<?> iterator) {
        return IterUtil.getElementType(iterator);
    }

    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, K... keys) {
        ArrayList<V> values = new ArrayList<>();
        for (K k : keys) {
            values.add(map.get(k));
        }
        return values;
    }

    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterable<K> keys) {
        return valuesOfKeys(map, keys.iterator());
    }

    public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterator<K> keys) {
        ArrayList<V> values = new ArrayList<>();
        while (keys.hasNext()) {
            values.add(map.get(keys.next()));
        }
        return values;
    }

    @SafeVarargs
    public static <T> List<T> sortPageAll(int pageNo, int pageSize, Comparator<T> comparator, Collection<T>... colls) {
        List<T> list = new ArrayList<>(pageNo * pageSize);
        for (Collection<T> coll : colls) {
            list.addAll(coll);
        }
        if (comparator != null) {
            list.sort(comparator);
        }
        return page(pageNo, pageSize, list);
    }

    public static <T> List<T> page(int pageNo, int pageSize, List<T> list) {
        return ListUtil.page(pageNo, pageSize, list);
    }

    public static <T> List<T> sort(Collection<T> collection, Comparator<? super T> comparator) {
        List<T> list = new ArrayList<>((Collection<? extends T>) collection);
        list.sort(comparator);
        return list;
    }

    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        return ListUtil.sort(list, c);
    }

    public static <T> List<T> sortByProperty(Collection<T> collection, String property) {
        return sort(collection, new PropertyComparator(property));
    }

    public static <T> List<T> sortByProperty(List<T> list, String property) {
        return ListUtil.sortByProperty(list, property);
    }

    public static List<String> sortByPinyin(Collection<String> collection) {
        return sort(collection, new PinyinComparator());
    }

    public static List<String> sortByPinyin(List<String> list) {
        return ListUtil.sortByPinyin(list);
    }

    public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
        TreeMap<K, V> result = new TreeMap<>(comparator);
        result.putAll(map);
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> LinkedHashMap<K, V> sortToMap(Collection<Map.Entry<K, V>> entryCollection, Comparator<Map.Entry<K, V>> comparator) {
        List<Map.Entry<K, V>> list = new LinkedList<>(entryCollection);
        list.sort(comparator);
        LinkedHashMap<K, V> result = (LinkedHashMap<K, V>) new LinkedHashMap();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K, V> LinkedHashMap<K, V> sortByEntry(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return sortToMap(map.entrySet(), comparator);
    }

    public static <K, V> List<Map.Entry<K, V>> sortEntryToList(Collection<Map.Entry<K, V>> collection) {
        List<Map.Entry<K, V>> list = new LinkedList<>(collection);
        list.sort(new Comparator() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$GaVBOXBawQv46Eu7LKjEXcPZIA4
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return CollUtil.lambda$sortEntryToList$4((Map.Entry) obj, (Map.Entry) obj2);
            }
        });
        return list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$sortEntryToList$4(Map.Entry o1, Map.Entry o2) {
        Object value = o1.getValue();
        Object value2 = o2.getValue();
        if (value instanceof Comparable) {
            return ((Comparable) value).compareTo(value2);
        }
        return value.toString().compareTo(value2.toString());
    }

    public static <T> void forEach(Iterable<T> iterable, Consumer<T> consumer) {
        if (iterable == null) {
            return;
        }
        forEach(iterable.iterator(), consumer);
    }

    public static <T> void forEach(Iterator<T> iterator, Consumer<T> consumer) {
        if (iterator == null) {
            return;
        }
        int index = 0;
        while (iterator.hasNext()) {
            consumer.accept(iterator.next(), index);
            index++;
        }
    }

    public static <T> void forEach(Enumeration<T> enumeration, Consumer<T> consumer) {
        if (enumeration == null) {
            return;
        }
        int index = 0;
        while (enumeration.hasMoreElements()) {
            consumer.accept(enumeration.nextElement(), index);
            index++;
        }
    }

    public static <K, V> void forEach(Map<K, V> map, KVConsumer<K, V> kvConsumer) {
        if (map == null) {
            return;
        }
        int index = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            kvConsumer.accept(entry.getKey(), entry.getValue(), index);
            index++;
        }
    }

    public static <T> List<List<T>> group(Collection<T> collection, Hash32<T> hash) {
        List<List<T>> result = new ArrayList<>();
        if (isEmpty((Collection<?>) collection)) {
            return result;
        }
        if (hash == null) {
            hash = new Hash32() { // from class: cn.hutool.core.collection.-$$Lambda$CollUtil$LkCL8fSj0dGEXoStZMP2FaE9dvs
                @Override // cn.hutool.core.lang.hash.Hash32
                public final int hash32(Object obj) {
                    return CollUtil.lambda$group$5(obj);
                }
            };
        }
        for (T t : collection) {
            int index = hash.hash32(t);
            if (result.size() - 1 < index) {
                while (result.size() - 1 < index) {
                    result.add(null);
                }
                result.set(index, newArrayList(t));
            } else {
                List<T> subList = result.get(index);
                if (subList == null) {
                    result.set(index, newArrayList(t));
                } else {
                    subList.add(t);
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$group$5(Object t) {
        if (t == null) {
            return 0;
        }
        return t.hashCode();
    }

    public static <T> List<List<T>> groupByField(Collection<T> collection, final String fieldName) {
        return group(collection, new Hash32<T>() { // from class: cn.hutool.core.collection.CollUtil.1
            private final List<Object> fieldNameList = new ArrayList();

            @Override // cn.hutool.core.lang.hash.Hash32
            public int hash32(T t) {
                if (t == null || !BeanUtil.isBean(t.getClass())) {
                    return 0;
                }
                Object value = ReflectUtil.getFieldValue(t, fieldName);
                int hash = this.fieldNameList.indexOf(value);
                if (hash < 0) {
                    this.fieldNameList.add(value);
                    return this.fieldNameList.size() - 1;
                }
                return hash;
            }
        });
    }

    public static <T> List<T> reverse(List<T> list) {
        return ListUtil.reverse(list);
    }

    public static <T> List<T> reverseNew(List<T> list) {
        return ListUtil.reverseNew(list);
    }

    public static <T> List<T> setOrAppend(List<T> list, int index, T element) {
        return ListUtil.setOrAppend(list, index, element);
    }

    public static <K> Set<K> keySet(Collection<Map<K, ?>> mapCollection) {
        if (isEmpty((Collection<?>) mapCollection)) {
            return new HashSet();
        }
        HashSet<K> set = new HashSet<>(mapCollection.size() * 16);
        for (Map<K, ?> map : mapCollection) {
            set.addAll(map.keySet());
        }
        return set;
    }

    public static <V> List<V> values(Collection<Map<?, V>> mapCollection) {
        List<V> values = new ArrayList<>();
        for (Map<?, V> map : mapCollection) {
            values.addAll(map.values());
        }
        return values;
    }

    public static <T extends Comparable<? super T>> T max(Collection<T> coll) {
        return (T) Collections.max(coll);
    }

    public static <T extends Comparable<? super T>> T min(Collection<T> coll) {
        return (T) Collections.min(coll);
    }

    public static <T> Collection<T> unmodifiable(Collection<? extends T> c) {
        return Collections.unmodifiableCollection(c);
    }

    public static <E, T extends Collection<E>> T empty(Class<?> collectionClass) {
        if (collectionClass == null) {
            return Collections.emptyList();
        }
        if (Set.class.isAssignableFrom(collectionClass)) {
            if (NavigableSet.class == collectionClass) {
                return Collections.emptyNavigableSet();
            }
            if (SortedSet.class == collectionClass) {
                return Collections.emptySortedSet();
            }
            return Collections.emptySet();
        } else if (List.class.isAssignableFrom(collectionClass)) {
            return Collections.emptyList();
        } else {
            throw new IllegalArgumentException(StrUtil.format("[{}] is not support to get empty!", collectionClass));
        }
    }

    public static void clear(Collection<?>... collections) {
        for (Collection<?> collection : collections) {
            if (isNotEmpty(collection)) {
                collection.clear();
            }
        }
    }

    public static <T> void padLeft(List<T> list, int minLen, T padObj) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            padRight(list, minLen, padObj);
            return;
        }
        for (int i = list.size(); i < minLen; i++) {
            list.add(0, padObj);
        }
    }

    public static <T> void padRight(Collection<T> list, int minLen, T padObj) {
        Objects.requireNonNull(list);
        for (int i = list.size(); i < minLen; i++) {
            list.add(padObj);
        }
    }

    public static <F, T> Collection<T> trans(Collection<F> collection, Function<? super F, ? extends T> function) {
        return new TransCollection(collection, function);
    }

    public static int size(Object object) {
        if (object == null) {
            return 0;
        }
        int total = 0;
        if (object instanceof Map) {
            int total2 = ((Map) object).size();
            return total2;
        } else if (object instanceof Collection) {
            int total3 = ((Collection) object).size();
            return total3;
        } else if (object instanceof Iterable) {
            int total4 = IterUtil.size((Iterable) object);
            return total4;
        } else if (object instanceof Iterator) {
            int total5 = IterUtil.size((Iterator) object);
            return total5;
        } else if (object instanceof Enumeration) {
            Enumeration<?> it = (Enumeration) object;
            while (it.hasMoreElements()) {
                total++;
                it.nextElement();
            }
            return total;
        } else if (ArrayUtil.isArray(object)) {
            int total6 = ArrayUtil.length(object);
            return total6;
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }

    public static boolean isEqualList(Collection<?> list1, Collection<?> list2) {
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }
        return IterUtil.isEqualList(list1, list2);
    }
}
