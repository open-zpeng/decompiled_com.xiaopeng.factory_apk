package cn.hutool.core.collection;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
/* loaded from: classes.dex */
public class IterUtil {
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        char c;
        String implMethodName = lambda.getImplMethodName();
        switch (implMethodName.hashCode()) {
            case -448210889:
                if (implMethodName.equals("lambda$fieldValueMap$a3f4a90f$1")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -278891634:
                if (implMethodName.equals("lambda$fieldValueAsMap$f61513e$1")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 591690217:
                if (implMethodName.equals("lambda$fieldValueAsMap$ceda202c$1")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 2114720463:
                if (implMethodName.equals("lambda$toMap$ed1d981b$1")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                    if (c == 3 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func1") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/collection/IterUtil") && lambda.getImplMethodSignature().equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;")) {
                        return new $$Lambda$IterUtil$sLe587d0pE0dmyDkVXZxDeLlLjc((String) lambda.getCapturedArg(0));
                    }
                } else if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func1") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/collection/IterUtil") && lambda.getImplMethodSignature().equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;")) {
                    return new $$Lambda$IterUtil$xtCieSIwCCjsHo7uiO12HtKm6s((String) lambda.getCapturedArg(0));
                }
            } else if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func1") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/collection/IterUtil") && lambda.getImplMethodSignature().equals("(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;")) {
                return new $$Lambda$IterUtil$WysTxkrykicZeNkWHTfsvuGKpBg((String) lambda.getCapturedArg(0));
            }
        } else if (lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func1") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/collection/IterUtil") && lambda.getImplMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;")) {
            return $$Lambda$IterUtil$P5tPp2I11ZmvUhD1X8jJOwGnNZI.INSTANCE;
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    public static <T> Iterator<T> getIter(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        return iterable.iterator();
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return iterable == null || isEmpty(iterable.iterator());
    }

    public static boolean isEmpty(Iterator<?> Iterator) {
        return Iterator == null || !Iterator.hasNext();
    }

    public static boolean isNotEmpty(Iterable<?> iterable) {
        return iterable != null && isNotEmpty(iterable.iterator());
    }

    public static boolean isNotEmpty(Iterator<?> Iterator) {
        return Iterator != null && Iterator.hasNext();
    }

    public static boolean hasNull(Iterable<?> iter) {
        return hasNull(iter == null ? null : iter.iterator());
    }

    public static boolean hasNull(Iterator<?> iter) {
        if (iter == null) {
            return true;
        }
        while (iter.hasNext()) {
            if (iter.next() == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllNull(Iterable<?> iter) {
        return isAllNull(iter == null ? null : iter.iterator());
    }

    public static boolean isAllNull(Iterator<?> iter) {
        return getFirstNoneNull(iter) == null;
    }

    public static <T> Map<T, Integer> countMap(Iterator<T> iter) {
        HashMap<T, Integer> countMap = new HashMap<>();
        if (iter != null) {
            while (iter.hasNext()) {
                T t = iter.next();
                countMap.put(t, Integer.valueOf(countMap.getOrDefault(t, 0).intValue() + 1));
            }
        }
        return countMap;
    }

    public static <K, V> Map<K, V> fieldValueMap(Iterator<V> iter, String fieldName) {
        return toMap(iter, new HashMap(), new $$Lambda$IterUtil$sLe587d0pE0dmyDkVXZxDeLlLjc(fieldName));
    }

    public static <K, V> Map<K, V> fieldValueAsMap(Iterator<?> iter, String fieldNameForKey, String fieldNameForValue) {
        return toMap(iter, new HashMap(), new $$Lambda$IterUtil$xtCieSIwCCjsHo7uiO12HtKm6s(fieldNameForKey), new $$Lambda$IterUtil$WysTxkrykicZeNkWHTfsvuGKpBg(fieldNameForValue));
    }

    public static /* synthetic */ Object lambda$fieldValueAsMap$f61513e$1(String fieldNameForKey, Object value) throws Exception {
        return ReflectUtil.getFieldValue(value, fieldNameForKey);
    }

    public static <V> List<Object> fieldValueList(Iterable<V> iterable, String fieldName) {
        return fieldValueList(getIter(iterable), fieldName);
    }

    public static <V> List<Object> fieldValueList(Iterator<V> iter, String fieldName) {
        List<Object> result = new ArrayList<>();
        if (iter != null) {
            while (iter.hasNext()) {
                V value = iter.next();
                result.add(ReflectUtil.getFieldValue(value, fieldName));
            }
        }
        return result;
    }

    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        return StrJoiner.of(conjunction).append((Iterator) iterator).toString();
    }

    public static <T> String join(Iterator<T> iterator, CharSequence conjunction, String prefix, String suffix) {
        return StrJoiner.of(conjunction, prefix, suffix).setWrapElement(true).append((Iterator) iterator).toString();
    }

    public static <T> String join(Iterator<T> iterator, CharSequence conjunction, Function<T, ? extends CharSequence> func) {
        if (iterator == null) {
            return null;
        }
        return StrJoiner.of(conjunction).append(iterator, func).toString();
    }

    public static <K, V> HashMap<K, V> toMap(Iterable<Map.Entry<K, V>> entryIter) {
        HashMap<K, V> map = new HashMap<>();
        if (isNotEmpty(entryIter)) {
            for (Map.Entry<K, V> entry : entryIter) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values) {
        return toMap((Iterable) keys, (Iterable) values, false);
    }

    public static <K, V> Map<K, V> toMap(Iterable<K> keys, Iterable<V> values, boolean isOrder) {
        return toMap(keys == null ? null : keys.iterator(), values != null ? values.iterator() : null, isOrder);
    }

    public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values) {
        return toMap((Iterator) keys, (Iterator) values, false);
    }

    public static <K, V> Map<K, V> toMap(Iterator<K> keys, Iterator<V> values, boolean isOrder) {
        Map<K, V> resultMap = MapUtil.newHashMap(isOrder);
        if (isNotEmpty((Iterator<?>) keys)) {
            while (keys.hasNext()) {
                resultMap.put(keys.next(), (values == null || !values.hasNext()) ? null : values.next());
            }
        }
        return resultMap;
    }

    public static /* synthetic */ Object lambda$toListMap$0(Object v) {
        return v;
    }

    public static <K, V> Map<K, List<V>> toListMap(Iterable<V> iterable, Function<V, K> keyMapper) {
        return toListMap(iterable, keyMapper, new Function() { // from class: cn.hutool.core.collection.-$$Lambda$IterUtil$WZm2mMdntIqdCfuNuJb8CSqvv_E
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return IterUtil.lambda$toListMap$0(obj);
            }
        });
    }

    public static <T, K, V> Map<K, List<V>> toListMap(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toListMap(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
    }

    public static <T, K, V> Map<K, List<V>> toListMap(Map<K, List<V>> resultMap, Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (resultMap == null) {
            resultMap = MapUtil.newHashMap();
        }
        if (ObjectUtil.isNull(iterable)) {
            return resultMap;
        }
        for (T value : iterable) {
            resultMap.computeIfAbsent(keyMapper.apply(value), new Function() { // from class: cn.hutool.core.collection.-$$Lambda$IterUtil$RujHN9mngdiJ722SNpiDJ5DZHRc
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return IterUtil.lambda$toListMap$1(obj);
                }
            }).add(valueMapper.apply(value));
        }
        return resultMap;
    }

    public static /* synthetic */ List lambda$toListMap$1(Object k) {
        return new ArrayList();
    }

    public static /* synthetic */ Object lambda$toMap$2(Object v) {
        return v;
    }

    public static <K, V> Map<K, V> toMap(Iterable<V> iterable, Function<V, K> keyMapper) {
        return toMap(iterable, keyMapper, new Function() { // from class: cn.hutool.core.collection.-$$Lambda$IterUtil$40YZfUEgHh5BOFMqpriI0PpGSgw
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return IterUtil.lambda$toMap$2(obj);
            }
        });
    }

    public static <T, K, V> Map<K, V> toMap(Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMap(MapUtil.newHashMap(), iterable, keyMapper, valueMapper);
    }

    public static <T, K, V> Map<K, V> toMap(Map<K, V> resultMap, Iterable<T> iterable, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (resultMap == null) {
            resultMap = MapUtil.newHashMap();
        }
        if (ObjectUtil.isNull(iterable)) {
            return resultMap;
        }
        for (T value : iterable) {
            resultMap.put(keyMapper.apply(value), valueMapper.apply(value));
        }
        return resultMap;
    }

    public static <E> List<E> toList(Iterable<E> iter) {
        if (iter == null) {
            return null;
        }
        return toList(iter.iterator());
    }

    public static <E> List<E> toList(Iterator<E> iter) {
        List<E> list = new ArrayList<>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    public static <E> Iterator<E> asIterator(Enumeration<E> e) {
        return new EnumerationIter(e);
    }

    public static <E> Iterable<E> asIterable(final Iterator<E> iter) {
        return new Iterable() { // from class: cn.hutool.core.collection.-$$Lambda$IterUtil$rNnqO3Nt_FuMFdld72p5VRo3RJc
            @Override // java.lang.Iterable
            public final Iterator iterator() {
                return IterUtil.lambda$asIterable$3(iter);
            }
        };
    }

    public static /* synthetic */ Iterator lambda$asIterable$3(Iterator iter) {
        return iter;
    }

    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        return (T) getFirst(iterable.iterator());
    }

    public static <T> T getFirstNoneNull(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        return (T) getFirstNoneNull(iterable.iterator());
    }

    public static <T> T getFirst(Iterator<T> iterator) {
        if (iterator != null && iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static <T> T getFirstNoneNull(Iterator<T> iterator) {
        return (T) firstMatch(iterator, new Matcher() { // from class: cn.hutool.core.collection.-$$Lambda$SsMT4UTHNLvQLtc11KgBvvlp_eM
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Objects.nonNull(obj);
            }
        });
    }

    public static <T> T firstMatch(Iterator<T> iterator, Matcher<T> matcher) {
        Assert.notNull(matcher, "Matcher must be not null !", new Object[0]);
        if (iterator != null) {
            while (iterator.hasNext()) {
                T next = iterator.next();
                if (matcher.match(next)) {
                    return next;
                }
            }
            return null;
        }
        return null;
    }

    public static Class<?> getElementType(Iterable<?> iterable) {
        if (iterable != null) {
            Iterator<?> iterator = iterable.iterator();
            return getElementType(iterator);
        }
        return null;
    }

    public static Class<?> getElementType(Iterator<?> iterator) {
        Object t;
        Iterator<?> iter2 = new CopiedIter<>(iterator);
        if (iter2.hasNext() && (t = iter2.next()) != null) {
            return t.getClass();
        }
        return null;
    }

    public static <T> List<T> edit(Iterable<T> iter, Editor<T> editor) {
        List<T> result = new ArrayList<>();
        if (iter == null) {
            return result;
        }
        for (T t : iter) {
            T modified = editor == null ? t : editor.edit(t);
            if (modified != null) {
                result.add(t);
            }
        }
        return result;
    }

    public static <T extends Iterable<E>, E> T filter(T iter, Filter<E> filter) {
        if (iter == null) {
            return null;
        }
        filter(iter.iterator(), filter);
        return iter;
    }

    public static <E> Iterator<E> filter(Iterator<E> iter, Filter<E> filter) {
        if (iter == null || filter == null) {
            return iter;
        }
        while (iter.hasNext()) {
            if (!filter.accept(iter.next())) {
                iter.remove();
            }
        }
        return iter;
    }

    public static /* synthetic */ Object lambda$toMap$ed1d981b$1(Object value) throws Exception {
        return value;
    }

    public static <K, V> Map<K, V> toMap(Iterator<V> iterator, Map<K, V> map, Func1<V, K> keyFunc) {
        return toMap(iterator, map, keyFunc, $$Lambda$IterUtil$P5tPp2I11ZmvUhD1X8jJOwGnNZI.INSTANCE);
    }

    public static <K, V, E> Map<K, V> toMap(Iterator<E> iterator, Map<K, V> map, Func1<E, K> keyFunc, Func1<E, V> valueFunc) {
        if (iterator == null) {
            return map;
        }
        if (map == null) {
            map = MapUtil.newHashMap(true);
        }
        while (iterator.hasNext()) {
            E element = iterator.next();
            try {
                map.put(keyFunc.call(element), valueFunc.call(element));
            } catch (Exception e) {
                throw new UtilException(e);
            }
        }
        return map;
    }

    public static <T> Iterator<T> empty() {
        return Collections.emptyIterator();
    }

    public static <F, T> Iterator<T> trans(Iterator<F> iterator, Function<? super F, ? extends T> function) {
        return new TransIter(iterator, function);
    }

    public static int size(Iterable<?> iterable) {
        if (iterable == null) {
            return 0;
        }
        if (iterable instanceof Collection) {
            return ((Collection) iterable).size();
        }
        return size(iterable.iterator());
    }

    public static int size(Iterator<?> iterator) {
        int size = 0;
        if (iterator != null) {
            while (iterator.hasNext()) {
                iterator.next();
                size++;
            }
        }
        return size;
    }

    public static boolean isEqualList(Iterable<?> list1, Iterable<?> list2) {
        Object obj1;
        Object obj2;
        if (list1 == list2) {
            return true;
        }
        Iterator<?> it1 = list1.iterator();
        Iterator<?> it2 = list2.iterator();
        do {
            boolean z = false;
            if (it1.hasNext() && it2.hasNext()) {
                obj1 = it1.next();
                obj2 = it2.next();
            } else {
                if (it1.hasNext() || it2.hasNext()) {
                    z = true;
                }
                return true ^ z;
            }
        } while (Objects.equals(obj1, obj2));
        return false;
    }
}
