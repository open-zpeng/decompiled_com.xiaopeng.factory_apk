package cn.hutool.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class EnumUtil {
    public static boolean isEnum(Class<?> clazz) {
        Assert.notNull(clazz);
        return clazz.isEnum();
    }

    public static boolean isEnum(Object obj) {
        Assert.notNull(obj);
        return obj.getClass().isEnum();
    }

    public static String toString(Enum<?> e) {
        if (e != null) {
            return e.name();
        }
        return null;
    }

    public static <E extends Enum<E>> E getEnumAt(Class<E> enumClass, int index) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (index < 0 || index >= enumConstants.length) {
            return null;
        }
        return enumConstants[index];
    }

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value) {
        return (E) Enum.valueOf(enumClass, value);
    }

    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value, E defaultValue) {
        return (E) ObjectUtil.defaultIfNull(fromStringQuietly(enumClass, value), defaultValue);
    }

    public static <E extends Enum<E>> E fromStringQuietly(Class<E> enumClass, String value) {
        if (enumClass == null || StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return (E) fromString(enumClass, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static <E extends Enum<E>> E likeValueOf(Class<E> enumClass, Object value) {
        if (value instanceof CharSequence) {
            value = value.toString().trim();
        }
        Field[] fields = ReflectUtil.getFields(enumClass);
        E[] enumConstants = enumClass.getEnumConstants();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (!field.getType().isEnum() && !"ENUM$VALUES".equals(fieldName) && !"ordinal".equals(fieldName)) {
                for (E e : enumConstants) {
                    if (ObjectUtil.equal(value, ReflectUtil.getFieldValue(e, field))) {
                        return e;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static List<String> getNames(Class<? extends Enum<?>> clazz) {
        Enum<?>[] enums = (Enum[]) clazz.getEnumConstants();
        if (enums == null) {
            return null;
        }
        List<String> list = new ArrayList<>(enums.length);
        for (Enum<?> e : enums) {
            list.add(e.name());
        }
        return list;
    }

    public static List<Object> getFieldValues(Class<? extends Enum<?>> clazz, String fieldName) {
        Enum<?>[] enums = (Enum[]) clazz.getEnumConstants();
        if (enums == null) {
            return null;
        }
        List<Object> list = new ArrayList<>(enums.length);
        for (Enum<?> e : enums) {
            list.add(ReflectUtil.getFieldValue(e, fieldName));
        }
        return list;
    }

    public static List<String> getFieldNames(Class<? extends Enum<?>> clazz) {
        List<String> names = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            String name = field.getName();
            if (!field.getType().isEnum() && !name.contains("$VALUES") && !"ordinal".equals(name) && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    public static <E extends Enum<E>> LinkedHashMap<String, E> getEnumMap(Class<E> enumClass) {
        E[] enumConstants;
        LinkedHashMap<String, E> map = new LinkedHashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }

    public static Map<String, Object> getNameFieldMap(Class<? extends Enum<?>> clazz, String fieldName) {
        Enum<?>[] enums = (Enum[]) clazz.getEnumConstants();
        if (enums == null) {
            return null;
        }
        Map<String, Object> map = MapUtil.newHashMap(enums.length, true);
        for (Enum<?> e : enums) {
            map.put(e.name(), ReflectUtil.getFieldValue(e, fieldName));
        }
        return map;
    }

    public static <E extends Enum<E>> boolean contains(Class<E> enumClass, String val) {
        return getEnumMap(enumClass).containsKey(val);
    }

    public static <E extends Enum<E>> boolean notContains(Class<E> enumClass, String val) {
        return !contains(enumClass, val);
    }

    public static boolean equalsIgnoreCase(Enum<?> e, String val) {
        return StrUtil.equalsIgnoreCase(toString(e), val);
    }

    public static boolean equals(Enum<?> e, String val) {
        return StrUtil.equals(toString(e), val);
    }
}
