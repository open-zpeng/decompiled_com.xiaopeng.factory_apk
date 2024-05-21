package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrJoiner;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class ArrayUtil extends PrimitiveArrayUtil {
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> T[] defaultIfEmpty(T[] array, T[] defaultArray) {
        return isEmpty((Object[]) array) ? defaultArray : array;
    }

    public static boolean isEmpty(Object array) {
        if (array != null) {
            return isArray(array) && Array.getLength(array) == 0;
        }
        return true;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return (array == null || array.length == 0) ? false : true;
    }

    public static boolean isNotEmpty(Object array) {
        return !isEmpty(array);
    }

    public static <T> boolean hasNull(T... array) {
        if (isNotEmpty((Object[]) array)) {
            for (T element : array) {
                if (element == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean isAllNull(T... array) {
        return firstNonNull(array) == null;
    }

    public static <T> T firstNonNull(T... array) {
        return (T) firstMatch(new Matcher() { // from class: cn.hutool.core.util.-$$Lambda$SsMT4UTHNLvQLtc11KgBvvlp_eM
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                return Objects.nonNull(obj);
            }
        }, array);
    }

    public static <T> T firstMatch(Matcher<T> matcher, T... array) {
        int index = matchIndex(matcher, array);
        if (index < 0) {
            return null;
        }
        return array[index];
    }

    public static <T> int matchIndex(Matcher<T> matcher, T... array) {
        return matchIndex(matcher, 0, array);
    }

    public static <T> int matchIndex(Matcher<T> matcher, int beginIndexInclude, T... array) {
        Assert.notNull(matcher, "Matcher must be not null !", new Object[0]);
        if (isNotEmpty((Object[]) array)) {
            for (int i = beginIndexInclude; i < array.length; i++) {
                if (matcher.match(array[i])) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static <T> T[] newArray(Class<?> componentType, int newSize) {
        return (T[]) ((Object[]) Array.newInstance(componentType, newSize));
    }

    public static Object[] newArray(int newSize) {
        return new Object[newSize];
    }

    public static Class<?> getComponentType(Object array) {
        if (array == null) {
            return null;
        }
        return array.getClass().getComponentType();
    }

    public static Class<?> getComponentType(Class<?> arrayClass) {
        if (arrayClass == null) {
            return null;
        }
        return arrayClass.getComponentType();
    }

    public static Class<?> getArrayType(Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }

    public static Object[] cast(Class<?> type, Object arrayObj) throws NullPointerException, IllegalArgumentException {
        if (arrayObj == null) {
            throw new NullPointerException("Argument [arrayObj] is null !");
        }
        if (!arrayObj.getClass().isArray()) {
            throw new IllegalArgumentException("Argument [arrayObj] is not array !");
        }
        if (type == null) {
            return (Object[]) arrayObj;
        }
        Class<?> componentType = type.isArray() ? type.getComponentType() : type;
        Object[] array = (Object[]) arrayObj;
        Object[] result = newArray(componentType, array.length);
        System.arraycopy(array, 0, result, 0, array.length);
        return result;
    }

    @SafeVarargs
    public static <T> T[] append(T[] buffer, T... newElements) {
        if (isEmpty((Object[]) buffer)) {
            return newElements;
        }
        return (T[]) insert((Object[]) buffer, buffer.length, (Object[]) newElements);
    }

    @SafeVarargs
    public static <T> Object append(Object array, T... newElements) {
        if (isEmpty(array)) {
            return newElements;
        }
        return insert(array, length(array), newElements);
    }

    public static <T> T[] setOrAppend(T[] buffer, int index, T value) {
        if (index < buffer.length) {
            Array.set(buffer, index, value);
            return buffer;
        }
        return (T[]) append((Object[]) buffer, value);
    }

    public static Object setOrAppend(Object array, int index, Object value) {
        if (index < length(array)) {
            Array.set(array, index, value);
            return array;
        }
        return append(array, value);
    }

    public static <T> T[] insert(T[] buffer, int index, T... newElements) {
        return (T[]) ((Object[]) insert((Object) buffer, index, (Object[]) newElements));
    }

    public static <T> Object insert(Object array, int index, T... newElements) {
        if (isEmpty((Object[]) newElements)) {
            return array;
        }
        if (isEmpty(array)) {
            return newElements;
        }
        int len = length(array);
        if (index < 0) {
            index = (index % len) + len;
        }
        Object[] newArray = newArray(array.getClass().getComponentType(), Math.max(len, index) + newElements.length);
        System.arraycopy(array, 0, newArray, 0, Math.min(len, index));
        System.arraycopy(newElements, 0, newArray, index, newElements.length);
        if (index < len) {
            System.arraycopy(array, index, newArray, newElements.length + index, len - index);
        }
        return newArray;
    }

    public static <T> T[] resize(T[] data, int newSize, Class<?> componentType) {
        if (newSize < 0) {
            return data;
        }
        T[] newArray = (T[]) newArray(componentType, newSize);
        if (newSize > 0 && isNotEmpty((Object[]) data)) {
            System.arraycopy(data, 0, newArray, 0, Math.min(data.length, newSize));
        }
        return newArray;
    }

    public static Object resize(Object array, int newSize) {
        if (newSize < 0) {
            return array;
        }
        if (array == null) {
            return null;
        }
        int length = length(array);
        Object newArray = Array.newInstance(array.getClass().getComponentType(), newSize);
        if (newSize > 0 && isNotEmpty(array)) {
            System.arraycopy(array, 0, newArray, 0, Math.min(length, newSize));
        }
        return newArray;
    }

    public static <T> T[] resize(T[] buffer, int newSize) {
        return (T[]) resize(buffer, newSize, buffer.getClass().getComponentType());
    }

    @SafeVarargs
    public static <T> T[] addAll(T[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }
        int length = 0;
        for (T[] array : arrays) {
            if (array != null) {
                length += array.length;
            }
        }
        T[] result = (T[]) newArray(arrays.getClass().getComponentType().getComponentType(), length);
        int length2 = 0;
        for (T[] array2 : arrays) {
            if (array2 != null) {
                System.arraycopy(array2, 0, result, length2, array2.length);
                length2 += array2.length;
            }
        }
        return result;
    }

    public static Object copy(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
        return dest;
    }

    public static Object copy(Object src, Object dest, int length) {
        System.arraycopy(src, 0, dest, 0, length);
        return dest;
    }

    public static <T> T[] clone(T[] array) {
        if (array == null) {
            return null;
        }
        return (T[]) ((Object[]) array.clone());
    }

    public static <T> T clone(T obj) {
        if (obj == null || !isArray(obj)) {
            return null;
        }
        Class<?> componentType = obj.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            int length = Array.getLength(obj);
            T t = (T) Array.newInstance(componentType, length);
            while (true) {
                int length2 = length - 1;
                if (length > 0) {
                    Array.set(t, length2, Array.get(obj, length2));
                    length = length2;
                } else {
                    return t;
                }
            }
        } else {
            return (T) ((Object[]) obj).clone();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T[] edit(T[] array, Editor<T> editor) {
        if (editor == null) {
            return array;
        }
        ArrayList<T> list = new ArrayList<>(array.length);
        for (T t : array) {
            T modified = editor.edit(t);
            if (modified != null) {
                list.add(modified);
            }
        }
        return (T[]) list.toArray(newArray(array.getClass().getComponentType(), list.size()));
    }

    public static <T> T[] filter(T[] array, final Filter<T> filter) {
        if (array == null || filter == null) {
            return array;
        }
        return (T[]) edit(array, new Editor() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$OHVO_H-OjCABD0mdoeRG2_pVMh4
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return ArrayUtil.lambda$filter$0(Filter.this, obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$filter$0(Filter filter, Object t) {
        if (filter.accept(t)) {
            return t;
        }
        return null;
    }

    public static <T> T[] removeNull(T[] array) {
        return (T[]) edit(array, new Editor() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$FvAmDn8L61-3YhFFYX1qSYbXmVQ
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return ArrayUtil.lambda$removeNull$1(obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Object lambda$removeNull$1(Object t) {
        return t;
    }

    public static <T extends CharSequence> T[] removeEmpty(T[] array) {
        return (T[]) ((CharSequence[]) filter(array, new Filter() { // from class: cn.hutool.core.util.-$$Lambda$QI51b42SJhF8tIbIKFHCHRa65zg
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CharSequenceUtil.isNotEmpty((CharSequence) obj);
            }
        }));
    }

    public static <T extends CharSequence> T[] removeBlank(T[] array) {
        return (T[]) ((CharSequence[]) filter(array, new Filter() { // from class: cn.hutool.core.util.-$$Lambda$5fyZGSzcEmQV7agOitOinPffspo
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return CharSequenceUtil.isNotBlank((CharSequence) obj);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String lambda$nullToEmpty$2(String t) {
        return t == null ? "" : t;
    }

    public static String[] nullToEmpty(String[] array) {
        return (String[]) edit(array, new Editor() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$i1XpivfSdgsH6iQjjMF-ohBIlIc
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return ArrayUtil.lambda$nullToEmpty$2((String) obj);
            }
        });
    }

    public static <K, V> Map<K, V> zip(K[] keys, V[] values, boolean isOrder) {
        if (isEmpty((Object[]) keys) || isEmpty((Object[]) values)) {
            return null;
        }
        int size = Math.min(keys.length, values.length);
        Map<K, V> map = MapUtil.newHashMap(size, isOrder);
        for (int i = 0; i < size; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    public static <K, V> Map<K, V> zip(K[] keys, V[] values) {
        return zip(keys, values, false);
    }

    public static <T> int indexOf(T[] array, final Object value, int beginIndexInclude) {
        return matchIndex(new Matcher() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$zy70Hs8HqB6devabA1-XPHlDI1o
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equal;
                equal = ObjectUtil.equal(value, obj);
                return equal;
            }
        }, beginIndexInclude, array);
    }

    public static <T> int indexOf(T[] array, final Object value) {
        return matchIndex(new Matcher() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$hGIpqxz3WBh24bdafyAUm6GWd1U
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equal;
                equal = ObjectUtil.equal(value, obj);
                return equal;
            }
        }, array);
    }

    public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (StrUtil.equalsIgnoreCase(array[i], value)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static <T> int lastIndexOf(T[] array, Object value) {
        if (isEmpty((Object[]) array)) {
            return -1;
        }
        return lastIndexOf(array, value, array.length - 1);
    }

    public static <T> int lastIndexOf(T[] array, Object value, int endInclude) {
        if (isNotEmpty((Object[]) array)) {
            for (int i = endInclude; i >= 0; i--) {
                if (ObjectUtil.equal(value, array[i])) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) > -1;
    }

    public static <T> boolean containsAny(T[] array, T... values) {
        for (T value : values) {
            if (contains(array, value)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean containsAll(T[] array, T... values) {
        for (T value : values) {
            if (!contains(array, value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsIgnoreCase(CharSequence[] array, CharSequence value) {
        return indexOfIgnoreCase(array, value) > -1;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004c, code lost:
        if (r3.equals("long") != false) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object[] wrap(java.lang.Object r6) {
        /*
            Method dump skipped, instructions count: 284
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.hutool.core.util.ArrayUtil.wrap(java.lang.Object):java.lang.Object[]");
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static <T> T get(Object array, int index) {
        if (array == null) {
            return null;
        }
        if (index < 0) {
            index += Array.getLength(array);
        }
        try {
            return (T) Array.get(array, index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T[] getAny(Object array, int... indexes) {
        if (array == null) {
            return null;
        }
        T[] result = (T[]) newArray(array.getClass().getComponentType(), indexes.length);
        for (int i : indexes) {
            result[i] = get(array, i);
        }
        return result;
    }

    public static <T> T[] sub(T[] array, int start, int end) {
        int length = length(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return (T[]) newArray(array.getClass().getComponentType(), 0);
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return (T[]) newArray(array.getClass().getComponentType(), 0);
            }
            end = length;
        }
        return (T[]) Arrays.copyOfRange(array, start, end);
    }

    public static Object[] sub(Object array, int start, int end) {
        return sub(array, start, end, 1);
    }

    public static Object[] sub(Object array, int start, int end, int step) {
        int length = length(array);
        if (start < 0) {
            start += length;
        }
        if (end < 0) {
            end += length;
        }
        if (start == length) {
            return new Object[0];
        }
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        if (end > length) {
            if (start >= length) {
                return new Object[0];
            }
            end = length;
        }
        if (step <= 1) {
            step = 1;
        }
        ArrayList<Object> list = new ArrayList<>();
        for (int i = start; i < end; i += step) {
            list.add(get(array, i));
        }
        return list.toArray();
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof long[]) {
            return Arrays.toString((long[]) obj);
        }
        if (obj instanceof int[]) {
            return Arrays.toString((int[]) obj);
        }
        if (obj instanceof short[]) {
            return Arrays.toString((short[]) obj);
        }
        if (obj instanceof char[]) {
            return Arrays.toString((char[]) obj);
        }
        if (obj instanceof byte[]) {
            return Arrays.toString((byte[]) obj);
        }
        if (obj instanceof boolean[]) {
            return Arrays.toString((boolean[]) obj);
        }
        if (obj instanceof float[]) {
            return Arrays.toString((float[]) obj);
        }
        if (obj instanceof double[]) {
            return Arrays.toString((double[]) obj);
        }
        if (isArray(obj)) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
            }
        }
        return obj.toString();
    }

    public static int length(Object array) throws IllegalArgumentException {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static <T> String join(T[] array, CharSequence conjunction) {
        return join(array, conjunction, null, null);
    }

    public static <T> String join(T[] array, CharSequence delimiter, String prefix, String suffix) {
        if (array == null) {
            return null;
        }
        return StrJoiner.of(delimiter, prefix, suffix).setWrapElement(true).append((Object[]) array).toString();
    }

    public static <T> String join(T[] array, CharSequence conjunction, final Editor<T> editor) {
        return StrJoiner.of(conjunction).append(array, new Function() { // from class: cn.hutool.core.util.-$$Lambda$ArrayUtil$ahoVpX2E-3my8SQIMkXO6vQnECE
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                CharSequence valueOf;
                valueOf = String.valueOf(Editor.this.edit(obj));
                return valueOf;
            }
        }).toString();
    }

    public static String join(Object array, CharSequence conjunction) {
        if (array == null) {
            return null;
        }
        if (!isArray(array)) {
            throw new IllegalArgumentException(StrUtil.format("[{}] is not a Array!", array.getClass()));
        }
        return StrJoiner.of(conjunction).append(array).toString();
    }

    public static byte[] toArray(ByteBuffer bytebuffer) {
        if (bytebuffer.hasArray()) {
            return Arrays.copyOfRange(bytebuffer.array(), bytebuffer.position(), bytebuffer.limit());
        }
        int oldPosition = bytebuffer.position();
        bytebuffer.position(0);
        int size = bytebuffer.limit();
        byte[] buffers = new byte[size];
        bytebuffer.get(buffers);
        bytebuffer.position(oldPosition);
        return buffers;
    }

    public static <T> T[] toArray(Iterator<T> iterator, Class<T> componentType) {
        return (T[]) toArray((Collection) CollUtil.newArrayList(iterator), (Class) componentType);
    }

    public static <T> T[] toArray(Iterable<T> iterable, Class<T> componentType) {
        return (T[]) toArray(CollectionUtil.toCollection(iterable), (Class) componentType);
    }

    public static <T> T[] toArray(Collection<T> collection, Class<T> componentType) {
        return (T[]) collection.toArray(newArray(componentType, 0));
    }

    public static <T> T[] remove(T[] array, int index) throws IllegalArgumentException {
        return (T[]) ((Object[]) remove((Object) array, index));
    }

    public static <T> T[] removeEle(T[] array, T element) throws IllegalArgumentException {
        return (T[]) remove((Object[]) array, indexOf(array, element));
    }

    public static <T> T[] reverse(T[] array, int startIndexInclusive, int endIndexExclusive) {
        if (isEmpty((Object[]) array)) {
            return array;
        }
        int j = Math.min(array.length, endIndexExclusive) - 1;
        for (int i = Math.max(startIndexInclusive, 0); j > i; i++) {
            T tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
        }
        return array;
    }

    public static <T> T[] reverse(T[] array) {
        return (T[]) reverse(array, 0, array.length);
    }

    public static <T extends Comparable<? super T>> T min(T[] numberArray) {
        return (T) min(numberArray, null);
    }

    public static <T extends Comparable<? super T>> T min(T[] numberArray, Comparator<T> comparator) {
        if (isEmpty((Object[]) numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        }
        T min = numberArray[0];
        for (T t : numberArray) {
            if (CompareUtil.compare(min, t, comparator) > 0) {
                min = t;
            }
        }
        return min;
    }

    public static <T extends Comparable<? super T>> T max(T[] numberArray) {
        return (T) max(numberArray, null);
    }

    public static <T extends Comparable<? super T>> T max(T[] numberArray, Comparator<T> comparator) {
        if (isEmpty((Object[]) numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        }
        T max = numberArray[0];
        for (int i = 1; i < numberArray.length; i++) {
            if (CompareUtil.compare(max, numberArray[i], comparator) < 0) {
                max = numberArray[i];
            }
        }
        return max;
    }

    public static <T> T[] shuffle(T[] array) {
        return (T[]) shuffle(array, RandomUtil.getRandom());
    }

    public static <T> T[] shuffle(T[] array, Random random) {
        if (array == null || random == null || array.length <= 1) {
            return array;
        }
        for (int i = array.length; i > 1; i--) {
            swap((Object[]) array, i - 1, random.nextInt(i));
        }
        return array;
    }

    public static <T> T[] swap(T[] array, int index1, int index2) {
        if (isEmpty((Object[]) array)) {
            throw new IllegalArgumentException("Array must not empty !");
        }
        T tmp = array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
        return array;
    }

    public static Object swap(Object array, int index1, int index2) {
        if (isEmpty(array)) {
            throw new IllegalArgumentException("Array must not empty !");
        }
        Object tmp = get(array, index1);
        Array.set(array, index1, Array.get(array, index2));
        Array.set(array, index2, tmp);
        return array;
    }

    public static int emptyCount(Object... args) {
        int count = 0;
        if (isNotEmpty(args)) {
            for (Object element : args) {
                if (ObjectUtil.isEmpty(element)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean hasEmpty(Object... args) {
        if (isNotEmpty(args)) {
            for (Object element : args) {
                if (ObjectUtil.isEmpty(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAllEmpty(Object... args) {
        return emptyCount(args) == args.length;
    }

    public static boolean isAllNotEmpty(Object... args) {
        return !hasEmpty(args);
    }

    public static <T> boolean isAllNotNull(T... array) {
        return !hasNull(array);
    }

    public static <T> T[] distinct(T[] array) {
        if (isEmpty((Object[]) array)) {
            return array;
        }
        Set<T> set = new LinkedHashSet<>(array.length, 1.0f);
        Collections.addAll(set, array);
        return (T[]) toArray((Collection) set, (Class) getComponentType(array));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T, R> R[] map(T[] array, Class<R> targetComponentType, Function<? super T, ? extends R> func) {
        R[] result = (R[]) newArray(targetComponentType, array.length);
        for (int i = 0; i < array.length; i++) {
            result[i] = func.apply(array[i]);
        }
        return result;
    }

    public static <T, R> R[] map(Object array, Class<R> targetComponentType, Function<? super T, ? extends R> func) {
        int length = length(array);
        R[] result = (R[]) newArray(targetComponentType, length);
        for (int i = 0; i < length; i++) {
            result[i] = func.apply((Object) get(array, i));
        }
        return result;
    }

    public static <T, R> List<R> map(T[] array, Function<? super T, ? extends R> func) {
        return (List) Arrays.stream(array).map(func).collect(Collectors.toList());
    }

    public static boolean equals(Object array1, Object array2) {
        if (array1 == array2) {
            return true;
        }
        if (hasNull(array1, array2)) {
            return false;
        }
        Assert.isTrue(isArray(array1), "First is not a Array !", new Object[0]);
        Assert.isTrue(isArray(array2), "Second is not a Array !", new Object[0]);
        if (array1 instanceof long[]) {
            return Arrays.equals((long[]) array1, (long[]) array2);
        }
        if (array1 instanceof int[]) {
            return Arrays.equals((int[]) array1, (int[]) array2);
        }
        if (array1 instanceof short[]) {
            return Arrays.equals((short[]) array1, (short[]) array2);
        }
        if (array1 instanceof char[]) {
            return Arrays.equals((char[]) array1, (char[]) array2);
        }
        if (array1 instanceof byte[]) {
            return Arrays.equals((byte[]) array1, (byte[]) array2);
        }
        if (array1 instanceof double[]) {
            return Arrays.equals((double[]) array1, (double[]) array2);
        }
        if (array1 instanceof float[]) {
            return Arrays.equals((float[]) array1, (float[]) array2);
        }
        if (array1 instanceof boolean[]) {
            return Arrays.equals((boolean[]) array1, (boolean[]) array2);
        }
        return Arrays.deepEquals((Object[]) array1, (Object[]) array2);
    }

    public static <T> boolean isSub(T[] array, T[] subArray) {
        return indexOfSub(array, subArray) > -1;
    }

    public static <T> int indexOfSub(T[] array, T[] subArray) {
        return indexOfSub(array, 0, subArray);
    }

    public static <T> int indexOfSub(T[] array, int beginInclude, T[] subArray) {
        int firstIndex;
        if (isEmpty((Object[]) array) || isEmpty((Object[]) subArray) || subArray.length > array.length || (firstIndex = indexOf(array, subArray[0], beginInclude)) < 0 || subArray.length + firstIndex > array.length) {
            return -1;
        }
        for (int i = 0; i < subArray.length; i++) {
            if (!ObjectUtil.equal(array[i + firstIndex], subArray[i])) {
                return indexOfSub(array, firstIndex + 1, subArray);
            }
        }
        return firstIndex;
    }

    public static <T> int lastIndexOfSub(T[] array, T[] subArray) {
        if (isEmpty((Object[]) array) || isEmpty((Object[]) subArray)) {
            return -1;
        }
        return lastIndexOfSub(array, array.length - 1, subArray);
    }

    public static <T> int lastIndexOfSub(T[] array, int endInclude, T[] subArray) {
        int firstIndex;
        if (isEmpty((Object[]) array) || isEmpty((Object[]) subArray) || subArray.length > array.length || endInclude < 0 || (firstIndex = lastIndexOf(array, subArray[0])) < 0 || subArray.length + firstIndex > array.length) {
            return -1;
        }
        for (int i = 0; i < subArray.length; i++) {
            if (!ObjectUtil.equal(array[i + firstIndex], subArray[i])) {
                return lastIndexOfSub(array, firstIndex - 1, subArray);
            }
        }
        return firstIndex;
    }

    public static <T> boolean isSorted(T[] array, Comparator<? super T> comparator) {
        if (array == null || comparator == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (comparator.compare(array[i], array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    public static <T extends Comparable<? super T>> boolean isSorted(T[] array) {
        return isSortedASC(array);
    }

    public static <T extends Comparable<? super T>> boolean isSortedASC(T[] array) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    public static <T extends Comparable<? super T>> boolean isSortedDESC(T[] array) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) < 0) {
                return false;
            }
        }
        return true;
    }
}
