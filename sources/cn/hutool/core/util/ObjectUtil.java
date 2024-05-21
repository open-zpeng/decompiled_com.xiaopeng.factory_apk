package cn.hutool.core.util;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class ObjectUtil {
    public static boolean equals(Object obj1, Object obj2) {
        return equal(obj1, obj2);
    }

    public static boolean equal(Object obj1, Object obj2) {
        if ((obj1 instanceof BigDecimal) && (obj2 instanceof BigDecimal)) {
            return NumberUtil.equals((BigDecimal) obj1, (BigDecimal) obj2);
        }
        return Objects.equals(obj1, obj2);
    }

    public static boolean notEqual(Object obj1, Object obj2) {
        return !equal(obj1, obj2);
    }

    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map) obj).size();
        }
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator) obj;
            int count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        } else if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration) obj;
            int count2 = 0;
            while (enumeration.hasMoreElements()) {
                count2++;
                enumeration.nextElement();
            }
            return count2;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        } else {
            return -1;
        }
    }

    public static boolean contains(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        } else if (obj instanceof Collection) {
            return ((Collection) obj).contains(element);
        } else {
            if (obj instanceof Map) {
                return ((Map) obj).containsValue(element);
            }
            if (obj instanceof Iterator) {
                Iterator<?> iter = (Iterator) obj;
                while (iter.hasNext()) {
                    Object o = iter.next();
                    if (equal(o, element)) {
                        return true;
                    }
                }
                return false;
            } else if (obj instanceof Enumeration) {
                Enumeration<?> enumeration = (Enumeration) obj;
                while (enumeration.hasMoreElements()) {
                    Object o2 = enumeration.nextElement();
                    if (equal(o2, element)) {
                        return true;
                    }
                }
                return false;
            } else {
                if (obj.getClass().isArray()) {
                    int len = Array.getLength(obj);
                    for (int i = 0; i < len; i++) {
                        Object o3 = Array.get(obj, i);
                        if (equal(o3, element)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    public static boolean isNull(Object obj) {
        return obj == null || obj.equals(null);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return StrUtil.isEmpty((CharSequence) obj);
        }
        if (obj instanceof Map) {
            return MapUtil.isEmpty((Map) obj);
        }
        if (obj instanceof Iterable) {
            return IterUtil.isEmpty((Iterable) obj);
        }
        if (obj instanceof Iterator) {
            return IterUtil.isEmpty((Iterator) obj);
        }
        if (ArrayUtil.isArray(obj)) {
            return ArrayUtil.isEmpty(obj);
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static <T> T defaultIfNull(Object source, Supplier<? extends T> handle, T defaultValue) {
        if (Objects.nonNull(source)) {
            return handle.get();
        }
        return defaultValue;
    }

    public static <T> T defaultIfEmpty(String str, Supplier<? extends T> handle, T defaultValue) {
        if (StrUtil.isNotEmpty(str)) {
            return handle.get();
        }
        return defaultValue;
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultValue) {
        return StrUtil.isEmpty(str) ? defaultValue : str;
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultValue) {
        return StrUtil.isBlank(str) ? defaultValue : str;
    }

    public static <T> T clone(T obj) {
        T result = (T) ArrayUtil.clone(obj);
        if (result == null) {
            if (obj instanceof Cloneable) {
                return (T) ReflectUtil.invoke(obj, "clone", new Object[0]);
            }
            return (T) cloneByStream(obj);
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cloneIfPossible(T obj) {
        T clone = null;
        try {
            clone = clone(obj);
        } catch (Exception e) {
        }
        return clone == null ? obj : clone;
    }

    public static <T> T cloneByStream(T obj) {
        return (T) SerializeUtil.clone(obj);
    }

    public static <T> byte[] serialize(T obj) {
        return SerializeUtil.serialize(obj);
    }

    public static <T> T deserialize(byte[] bytes) {
        return (T) SerializeUtil.deserialize(bytes);
    }

    public static boolean isBasicType(Object object) {
        return ClassUtil.isBasicType(object.getClass());
    }

    public static boolean isValidIfNumber(Object obj) {
        if (obj instanceof Number) {
            return NumberUtil.isValidNumber((Number) obj);
        }
        return true;
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
        return CompareUtil.compare(c1, c2);
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean nullGreater) {
        return CompareUtil.compare((Comparable) c1, (Comparable) c2, nullGreater);
    }

    public static Class<?> getTypeArgument(Object obj) {
        return getTypeArgument(obj, 0);
    }

    public static Class<?> getTypeArgument(Object obj, int index) {
        return ClassUtil.getTypeArgument(obj.getClass(), index);
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return CharSequenceUtil.NULL;
        }
        if (obj instanceof Map) {
            return obj.toString();
        }
        return Convert.toStr(obj);
    }

    public static int emptyCount(Object... objs) {
        return ArrayUtil.emptyCount(objs);
    }

    public static boolean hasNull(Object... objs) {
        return ArrayUtil.hasNull(objs);
    }

    public static boolean hasEmpty(Object... objs) {
        return ArrayUtil.hasEmpty(objs);
    }

    public static boolean isAllEmpty(Object... objs) {
        return ArrayUtil.isAllEmpty(objs);
    }

    public static boolean isAllNotEmpty(Object... objs) {
        return ArrayUtil.isAllNotEmpty(objs);
    }
}
