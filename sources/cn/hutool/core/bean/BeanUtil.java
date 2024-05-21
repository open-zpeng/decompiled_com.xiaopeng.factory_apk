package cn.hutool.core.bean;

import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class BeanUtil {
    public static boolean isReadableBean(Class<?> clazz) {
        return hasGetter(clazz) || hasPublicField(clazz);
    }

    public static boolean isBean(Class<?> clazz) {
        return hasSetter(clazz) || hasPublicField(clazz);
    }

    public static boolean hasSetter(Class<?> clazz) {
        if (ClassUtil.isNormalClass(clazz)) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGetter(Class<?> clazz) {
        Method[] methods;
        if (ClassUtil.isNormalClass(clazz)) {
            for (Method method : clazz.getMethods()) {
                if (method.getParameterTypes().length == 0 && (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPublicField(Class<?> clazz) {
        Field[] fields;
        if (ClassUtil.isNormalClass(clazz)) {
            for (Field field : clazz.getFields()) {
                if (ModifierUtil.isPublic(field) && !ModifierUtil.isStatic(field)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static DynaBean createDynaBean(Object bean) {
        return new DynaBean(bean);
    }

    public static PropertyEditor findEditor(Class<?> type) {
        return PropertyEditorManager.findEditor(type);
    }

    public static BeanDesc getBeanDesc(Class<?> clazz) {
        return BeanDescCache.INSTANCE.getBeanDesc(clazz, new $$Lambda$BeanUtil$o3ZAuKvIgMzea1X_fc93swvy8k(clazz));
    }

    public static /* synthetic */ BeanDesc lambda$getBeanDesc$e7c7684d$1(Class clazz) throws Exception {
        return new BeanDesc(clazz);
    }

    public static void descForEach(Class<?> clazz, Consumer<? super PropDesc> action) {
        getBeanDesc(clazz).getProps().forEach(action);
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeanException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            return (PropertyDescriptor[]) ArrayUtil.filter(beanInfo.getPropertyDescriptors(), new Filter() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$V7hPV3dOcWvjAKcBdEYKCOqnyQE
                @Override // cn.hutool.core.lang.Filter
                public final boolean accept(Object obj) {
                    return BeanUtil.lambda$getPropertyDescriptors$0((PropertyDescriptor) obj);
                }
            });
        } catch (IntrospectionException e) {
            throw new BeanException((Throwable) e);
        }
    }

    public static /* synthetic */ boolean lambda$getPropertyDescriptors$0(PropertyDescriptor t) {
        return !"class".equals(t.getName());
    }

    public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws BeanException {
        return BeanInfoCache.INSTANCE.getPropertyDescriptorMap(clazz, ignoreCase, new $$Lambda$BeanUtil$KdeCmJknWlbcd4T5U_P5sbAgqw(clazz, ignoreCase));
    }

    public static Map<String, PropertyDescriptor> internalGetPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws BeanException {
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
        Map<String, PropertyDescriptor> map = ignoreCase ? new CaseInsensitiveMap<>(propertyDescriptors.length, 1.0f) : new HashMap<>(propertyDescriptors.length, 1.0f);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            map.put(propertyDescriptor.getName(), propertyDescriptor);
        }
        return map;
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String fieldName) throws BeanException {
        return getPropertyDescriptor(clazz, fieldName, false);
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String fieldName, boolean ignoreCase) throws BeanException {
        Map<String, PropertyDescriptor> map = getPropertyDescriptorMap(clazz, ignoreCase);
        if (map == null) {
            return null;
        }
        return map.get(fieldName);
    }

    public static Object getFieldValue(Object bean, final String fieldNameOrIndex) {
        if (bean == null || fieldNameOrIndex == null) {
            return null;
        }
        if (bean instanceof Map) {
            return ((Map) bean).get(fieldNameOrIndex);
        }
        if (bean instanceof Collection) {
            try {
                return CollUtil.get((Collection) bean, Integer.parseInt(fieldNameOrIndex));
            } catch (NumberFormatException e) {
                return CollUtil.map((Collection) bean, new Function() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$gDmwrINTG80jgYsZOqqr772HMUg
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        Object fieldValue;
                        fieldValue = BeanUtil.getFieldValue(obj, fieldNameOrIndex);
                        return fieldValue;
                    }
                }, false);
            }
        } else if (ArrayUtil.isArray(bean)) {
            try {
                return ArrayUtil.get(bean, Integer.parseInt(fieldNameOrIndex));
            } catch (NumberFormatException e2) {
                return ArrayUtil.map(bean, Object.class, new Function() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$FB6NFMC-73cuYNY2Tc2TjSO0Eow
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        Object fieldValue;
                        fieldValue = BeanUtil.getFieldValue(obj, fieldNameOrIndex);
                        return fieldValue;
                    }
                });
            }
        } else {
            return ReflectUtil.getFieldValue(bean, fieldNameOrIndex);
        }
    }

    public static void setFieldValue(Object bean, String fieldNameOrIndex, Object value) {
        if (bean instanceof Map) {
            ((Map) bean).put(fieldNameOrIndex, value);
        } else if (bean instanceof List) {
            CollUtil.setOrAppend((List) bean, Convert.toInt(fieldNameOrIndex).intValue(), value);
        } else if (ArrayUtil.isArray(bean)) {
            ArrayUtil.setOrAppend(bean, Convert.toInt(fieldNameOrIndex).intValue(), value);
        } else {
            ReflectUtil.setFieldValue(bean, fieldNameOrIndex, value);
        }
    }

    public static <T> T getProperty(Object bean, String expression) {
        if (bean == null || StrUtil.isBlank(expression)) {
            return null;
        }
        return (T) BeanPath.create(expression).get(bean);
    }

    public static void setProperty(Object bean, String expression, Object value) {
        BeanPath.create(expression).set(bean, value);
    }

    @Deprecated
    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
        return (T) fillBeanWithMap(map, ReflectUtil.newInstanceIfPossible(beanClass), isIgnoreError);
    }

    @Deprecated
    public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
        return (T) fillBeanWithMapIgnoreCase(map, ReflectUtil.newInstanceIfPossible(beanClass), isIgnoreError);
    }

    @Deprecated
    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, CopyOptions copyOptions) {
        return (T) fillBeanWithMap(map, ReflectUtil.newInstanceIfPossible(beanClass), copyOptions);
    }

    public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isToCamelCase, CopyOptions copyOptions) {
        return (T) fillBeanWithMap(map, ReflectUtil.newInstanceIfPossible(beanClass), isToCamelCase, copyOptions);
    }

    public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isIgnoreError) {
        return (T) fillBeanWithMap(map, (Object) bean, false, isIgnoreError);
    }

    public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, boolean isIgnoreError) {
        return (T) fillBeanWithMap(map, bean, isToCamelCase, CopyOptions.create().setIgnoreError(isIgnoreError));
    }

    public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean, boolean isIgnoreError) {
        return (T) fillBeanWithMap(map, bean, CopyOptions.create().setIgnoreCase(true).setIgnoreError(isIgnoreError));
    }

    public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, CopyOptions copyOptions) {
        return (T) fillBeanWithMap(map, (Object) bean, false, copyOptions);
    }

    public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, CopyOptions copyOptions) {
        if (MapUtil.isEmpty(map)) {
            return bean;
        }
        if (isToCamelCase) {
            map = MapUtil.toCamelCaseMap(map);
        }
        copyProperties(map, bean, copyOptions);
        return bean;
    }

    public static <T> T toBean(Object source, Class<T> clazz) {
        return (T) toBean(source, clazz, (CopyOptions) null);
    }

    public static <T> T toBeanIgnoreError(Object source, Class<T> clazz) {
        return (T) toBean(source, clazz, CopyOptions.create().setIgnoreError(true));
    }

    public static <T> T toBeanIgnoreCase(Object source, Class<T> clazz, boolean ignoreError) {
        return (T) toBean(source, clazz, CopyOptions.create().setIgnoreCase(true).setIgnoreError(ignoreError));
    }

    public static <T> T toBean(Object source, Class<T> clazz, CopyOptions options) {
        if (source == null) {
            return null;
        }
        T target = (T) ReflectUtil.newInstanceIfPossible(clazz);
        copyProperties(source, target, options);
        return target;
    }

    public static <T> T toBean(Class<T> beanClass, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
        if (beanClass == null || valueProvider == null) {
            return null;
        }
        return (T) fillBean(ReflectUtil.newInstanceIfPossible(beanClass), valueProvider, copyOptions);
    }

    public static <T> T fillBean(T bean, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
        if (valueProvider == null) {
            return bean;
        }
        return (T) BeanCopier.create(valueProvider, bean, copyOptions).copy();
    }

    public static Map<String, Object> beanToMap(Object bean) {
        return beanToMap(bean, false, false);
    }

    public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        if (bean == null) {
            return null;
        }
        return beanToMap(bean, new LinkedHashMap(), isToUnderlineCase, ignoreNullValue);
    }

    public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, final boolean isToUnderlineCase, boolean ignoreNullValue) {
        if (bean == null) {
            return null;
        }
        return beanToMap(bean, targetMap, ignoreNullValue, new Editor() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$RcsOjEfziVXOOMas4liW_yH_P-o
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return BeanUtil.lambda$beanToMap$3(isToUnderlineCase, (String) obj);
            }
        });
    }

    public static /* synthetic */ String lambda$beanToMap$3(boolean isToUnderlineCase, String key) {
        return isToUnderlineCase ? StrUtil.toUnderlineCase(key) : key;
    }

    public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, boolean ignoreNullValue, Editor<String> keyEditor) {
        if (bean == null) {
            return null;
        }
        return (Map) BeanCopier.create(bean, targetMap, CopyOptions.create().setIgnoreNullValue(ignoreNullValue).setFieldNameEditor(keyEditor)).copy();
    }

    public static <T> T copyProperties(Object source, Class<T> tClass, String... ignoreProperties) {
        T target = (T) ReflectUtil.newInstanceIfPossible(tClass);
        copyProperties(source, target, CopyOptions.create().setIgnoreProperties(ignoreProperties));
        return target;
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        copyProperties(source, target, CopyOptions.create().setIgnoreProperties(ignoreProperties));
    }

    public static void copyProperties(Object source, Object target, boolean ignoreCase) {
        BeanCopier.create(source, target, CopyOptions.create().setIgnoreCase(ignoreCase)).copy();
    }

    public static void copyProperties(Object source, Object target, CopyOptions copyOptions) {
        BeanCopier.create(source, target, (CopyOptions) ObjectUtil.defaultIfNull(copyOptions, CopyOptions.create())).copy();
    }

    public static <T> List<T> copyToList(Collection<?> collection, final Class<T> targetType, final CopyOptions copyOptions) {
        if (collection == null) {
            return null;
        }
        if (collection.isEmpty()) {
            return new ArrayList(0);
        }
        return (List) collection.stream().map(new Function() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$_zEFV7Bq11z3Wf2s1uvDeVkzFLI
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return BeanUtil.lambda$copyToList$4(targetType, copyOptions, obj);
            }
        }).collect(Collectors.toList());
    }

    public static /* synthetic */ Object lambda$copyToList$4(Class targetType, CopyOptions copyOptions, Object source) {
        Object newInstanceIfPossible = ReflectUtil.newInstanceIfPossible(targetType);
        copyProperties(source, newInstanceIfPossible, copyOptions);
        return newInstanceIfPossible;
    }

    public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType) {
        return copyToList(collection, targetType, CopyOptions.create());
    }

    public static boolean isMatchName(Object bean, String beanClassName, boolean isSimple) {
        if (bean == null || StrUtil.isBlank(beanClassName)) {
            return false;
        }
        return ClassUtil.getClassName(bean, isSimple).equals(isSimple ? StrUtil.upperFirst(beanClassName) : beanClassName);
    }

    public static <T> T edit(T bean, Editor<Field> editor) {
        if (bean == null) {
            return null;
        }
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field field : fields) {
            if (!ModifierUtil.isStatic(field)) {
                editor.edit(field);
            }
        }
        return bean;
    }

    public static <T> T trimStrFields(final T bean, final String... ignoreFields) {
        return (T) edit(bean, new Editor() { // from class: cn.hutool.core.bean.-$$Lambda$BeanUtil$q-RZoB7kof2LrPkE91gkeBlv97A
            @Override // cn.hutool.core.lang.Editor
            public final Object edit(Object obj) {
                return BeanUtil.lambda$trimStrFields$5(ignoreFields, bean, (Field) obj);
            }
        });
    }

    public static /* synthetic */ Field lambda$trimStrFields$5(String[] ignoreFields, Object bean, Field field) {
        String val;
        if (ignoreFields != null && ArrayUtil.containsIgnoreCase(ignoreFields, field.getName())) {
            return field;
        }
        if (String.class.equals(field.getType()) && (val = (String) ReflectUtil.getFieldValue(bean, field)) != null) {
            String trimVal = StrUtil.trim(val);
            if (!val.equals(trimVal)) {
                ReflectUtil.setFieldValue(bean, field, trimVal);
            }
        }
        return field;
    }

    public static boolean isNotEmpty(Object bean, String... ignoreFiledNames) {
        return !isEmpty(bean, ignoreFiledNames);
    }

    public static boolean isEmpty(Object bean, String... ignoreFiledNames) {
        Field[] fields;
        if (bean != null) {
            for (Field field : ReflectUtil.getFields(bean.getClass())) {
                if (!ModifierUtil.isStatic(field) && !ArrayUtil.contains(ignoreFiledNames, field.getName()) && ReflectUtil.getFieldValue(bean, field) != null) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public static boolean hasNullField(Object bean, String... ignoreFiledNames) {
        Field[] fields;
        if (bean == null) {
            return true;
        }
        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            if (!ModifierUtil.isStatic(field) && !ArrayUtil.contains(ignoreFiledNames, field.getName()) && ReflectUtil.getFieldValue(bean, field) == null) {
                return true;
            }
        }
        return false;
    }
}
