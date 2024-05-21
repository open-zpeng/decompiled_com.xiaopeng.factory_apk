package cn.hutool.core.util;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.map.MapUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class ReflectUtil {
    private static final SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
    private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        Constructor<T>[] constructors;
        if (clazz == null) {
            return null;
        }
        for (Constructor<T> constructor : getConstructors(clazz)) {
            Class<?>[] pts = constructor.getParameterTypes();
            if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
                setAccessible(constructor);
                return constructor;
            }
        }
        return null;
    }

    public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        Constructor<?>[] constructors = CONSTRUCTORS_CACHE.get(beanClass);
        if (constructors != null) {
            return (Constructor<T>[]) constructors;
        }
        return (Constructor<T>[]) CONSTRUCTORS_CACHE.put(beanClass, getConstructorsDirectly(beanClass));
    }

    public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
        Assert.notNull(beanClass);
        return beanClass.getDeclaredConstructors();
    }

    public static boolean hasField(Class<?> beanClass, String name) throws SecurityException {
        return getField(beanClass, name) != null;
    }

    public static String getFieldName(Field field) {
        if (field == null) {
            return null;
        }
        Alias alias = (Alias) field.getAnnotation(Alias.class);
        if (alias != null) {
            return alias.value();
        }
        return field.getName();
    }

    public static Field getField(Class<?> beanClass, final String name) throws SecurityException {
        Field[] fields = getFields(beanClass);
        return (Field) ArrayUtil.firstMatch(new Matcher() { // from class: cn.hutool.core.util.-$$Lambda$ReflectUtil$VCGrepkqW0o2O8cBzFz4qAWeG3o
            @Override // cn.hutool.core.lang.Matcher
            public final boolean match(Object obj) {
                boolean equals;
                equals = name.equals(ReflectUtil.getFieldName((Field) obj));
                return equals;
            }
        }, fields);
    }

    public static Map<String, Field> getFieldMap(Class<?> beanClass) {
        Field[] fields = getFields(beanClass);
        HashMap<String, Field> map = MapUtil.newHashMap(fields.length, true);
        for (Field field : fields) {
            map.put(field.getName(), field);
        }
        return map;
    }

    public static Field[] getFields(Class<?> beanClass) throws SecurityException {
        Field[] allFields = FIELDS_CACHE.get(beanClass);
        if (allFields != null) {
            return allFields;
        }
        return FIELDS_CACHE.put(beanClass, getFieldsDirectly(beanClass, true));
    }

    public static Field[] getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
        Assert.notNull(beanClass);
        Field[] allFields = null;
        Class<?> searchType = beanClass;
        while (searchType != null) {
            Field[] declaredFields = searchType.getDeclaredFields();
            if (allFields == null) {
                allFields = declaredFields;
            } else {
                allFields = (Field[]) ArrayUtil.append((Object[]) allFields, (Object[]) declaredFields);
            }
            searchType = withSuperClassFields ? searchType.getSuperclass() : null;
        }
        return allFields;
    }

    public static Object getFieldValue(Object obj, String fieldName) throws UtilException {
        if (obj == null || StrUtil.isBlank(fieldName)) {
            return null;
        }
        return getFieldValue(obj, getField(obj instanceof Class ? (Class) obj : obj.getClass(), fieldName));
    }

    public static Object getStaticFieldValue(Field field) throws UtilException {
        return getFieldValue((Object) null, field);
    }

    public static Object getFieldValue(Object obj, Field field) throws UtilException {
        if (field == null) {
            return null;
        }
        if (obj instanceof Class) {
            obj = null;
        }
        setAccessible(field);
        try {
            Object result = field.get(obj);
            return result;
        } catch (IllegalAccessException e) {
            throw new UtilException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
        }
    }

    public static Object[] getFieldsValue(Object obj) {
        if (obj != null) {
            Field[] fields = getFields(obj instanceof Class ? (Class) obj : obj.getClass());
            if (fields != null) {
                Object[] values = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    values[i] = getFieldValue(obj, fields[i]);
                }
                return values;
            }
            return null;
        }
        return null;
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws UtilException {
        Assert.notNull(obj);
        Assert.notBlank(fieldName);
        Field field = getField(obj instanceof Class ? (Class) obj : obj.getClass(), fieldName);
        Assert.notNull(field, "Field [{}] is not exist in [{}]", fieldName, obj.getClass().getName());
        setFieldValue(obj, field, value);
    }

    public static void setFieldValue(Object obj, Field field, Object value) throws UtilException {
        Object targetValue;
        Assert.notNull(field, "Field in [{}] not exist !", obj);
        Class<?> fieldType = field.getType();
        if (value != null) {
            if (!fieldType.isAssignableFrom(value.getClass()) && (targetValue = Convert.convert((Class<Object>) fieldType, value)) != null) {
                value = targetValue;
            }
        } else {
            value = ClassUtil.getDefaultValue(fieldType);
        }
        setAccessible(field);
        try {
            field.set(obj instanceof Class ? null : obj, value);
        } catch (IllegalAccessException e) {
            throw new UtilException(e, "IllegalAccess for {}.{}", obj, field.getName());
        }
    }

    public static Set<String> getPublicMethodNames(Class<?> clazz) {
        HashSet<String> methodSet = new HashSet<>();
        Method[] methodArray = getPublicMethods(clazz);
        if (ArrayUtil.isNotEmpty((Object[]) methodArray)) {
            for (Method method : methodArray) {
                methodSet.add(method.getName());
            }
        }
        return methodSet;
    }

    public static Method[] getPublicMethods(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return clazz.getMethods();
    }

    public static List<Method> getPublicMethods(Class<?> clazz, Filter<Method> filter) {
        if (clazz == null) {
            return null;
        }
        Method[] methods = getPublicMethods(clazz);
        if (filter != null) {
            List<Method> methodList = new ArrayList<>();
            for (Method method : methods) {
                if (filter.accept(method)) {
                    methodList.add(method);
                }
            }
            return methodList;
        }
        return CollUtil.newArrayList(methods);
    }

    public static List<Method> getPublicMethods(Class<?> clazz, Method... excludeMethods) {
        final HashSet<Method> excludeMethodSet = CollUtil.newHashSet(excludeMethods);
        return getPublicMethods(clazz, new Filter() { // from class: cn.hutool.core.util.-$$Lambda$ReflectUtil$tfANQ1_nxX7tN0Ucg7UNRT7ILwc
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return ReflectUtil.lambda$getPublicMethods$1(excludeMethodSet, (Method) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getPublicMethods$1(HashSet excludeMethodSet, Method method) {
        return !excludeMethodSet.contains(method);
    }

    public static List<Method> getPublicMethods(Class<?> clazz, String... excludeMethodNames) {
        final HashSet<String> excludeMethodNameSet = CollUtil.newHashSet(excludeMethodNames);
        return getPublicMethods(clazz, new Filter() { // from class: cn.hutool.core.util.-$$Lambda$ReflectUtil$8AY7jhdl2IEqH4O3kMJRrVQGBz0
            @Override // cn.hutool.core.lang.Filter
            public final boolean accept(Object obj) {
                return ReflectUtil.lambda$getPublicMethods$2(excludeMethodNameSet, (Method) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getPublicMethods$2(HashSet excludeMethodNameSet, Method method) {
        return !excludeMethodNameSet.contains(method.getName());
    }

    public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethodOfObj(Object obj, String methodName, Object... args) throws SecurityException {
        if (obj == null || StrUtil.isBlank(methodName)) {
            return null;
        }
        return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
    }

    public static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
        return getMethod(clazz, true, methodName, paramTypes);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
        return getMethod(clazz, false, methodName, paramTypes);
    }

    public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) throws SecurityException {
        if (clazz == null || StrUtil.isBlank(methodName)) {
            return null;
        }
        Method[] methods = getMethods(clazz);
        if (ArrayUtil.isNotEmpty((Object[]) methods)) {
            for (Method method : methods) {
                if (StrUtil.equals(methodName, method.getName(), ignoreCase) && ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Method getMethodByName(Class<?> clazz, String methodName) throws SecurityException {
        return getMethodByName(clazz, false, methodName);
    }

    public static Method getMethodByNameIgnoreCase(Class<?> clazz, String methodName) throws SecurityException {
        return getMethodByName(clazz, true, methodName);
    }

    public static Method getMethodByName(Class<?> clazz, boolean ignoreCase, String methodName) throws SecurityException {
        if (clazz == null || StrUtil.isBlank(methodName)) {
            return null;
        }
        Method[] methods = getMethods(clazz);
        if (ArrayUtil.isNotEmpty((Object[]) methods)) {
            for (Method method : methods) {
                if (StrUtil.equals(methodName, method.getName(), ignoreCase)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Set<String> getMethodNames(Class<?> clazz) throws SecurityException {
        HashSet<String> methodSet = new HashSet<>();
        Method[] methods = getMethods(clazz);
        for (Method method : methods) {
            methodSet.add(method.getName());
        }
        return methodSet;
    }

    public static Method[] getMethods(Class<?> clazz, Filter<Method> filter) throws SecurityException {
        if (clazz == null) {
            return null;
        }
        return (Method[]) ArrayUtil.filter(getMethods(clazz), filter);
    }

    public static Method[] getMethods(Class<?> beanClass) throws SecurityException {
        Method[] allMethods = METHODS_CACHE.get(beanClass);
        if (allMethods != null) {
            return allMethods;
        }
        return METHODS_CACHE.put(beanClass, getMethodsDirectly(beanClass, true));
    }

    public static Method[] getMethodsDirectly(Class<?> beanClass, boolean withSuperClassMethods) throws SecurityException {
        Assert.notNull(beanClass);
        Method[] allMethods = null;
        Class<?> searchType = beanClass;
        while (searchType != null) {
            Method[] declaredMethods = searchType.getDeclaredMethods();
            if (allMethods == null) {
                allMethods = declaredMethods;
            } else {
                allMethods = (Method[]) ArrayUtil.append((Object[]) allMethods, (Object[]) declaredMethods);
            }
            searchType = withSuperClassMethods ? searchType.getSuperclass() : null;
        }
        return allMethods;
    }

    public static boolean isEqualsMethod(Method method) {
        if (method == null || !"equals".equals(method.getName())) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return 1 == paramTypes.length && paramTypes[0] == Object.class;
    }

    public static boolean isHashCodeMethod(Method method) {
        return method != null && "hashCode".equals(method.getName()) && isEmptyParam(method);
    }

    public static boolean isToStringMethod(Method method) {
        return method != null && "toString".equals(method.getName()) && isEmptyParam(method);
    }

    public static boolean isEmptyParam(Method method) {
        return method.getParameterTypes().length == 0;
    }

    public static <T> T newInstance(String clazz) throws UtilException {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new UtilException(e, "Instance class [{}] error!", clazz);
        }
    }

    public static <T> T newInstance(Class<T> clazz, Object... params) throws UtilException {
        if (ArrayUtil.isEmpty(params)) {
            try {
                return getConstructor(clazz, new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                throw new UtilException(e, "Instance class [{}] error!", clazz);
            }
        }
        Class<?>[] paramTypes = ClassUtil.getClasses(params);
        Constructor<T> constructor = getConstructor(clazz, paramTypes);
        if (constructor == null) {
            throw new UtilException("No Constructor matched for parameter types: [{}]", paramTypes);
        }
        try {
            return constructor.newInstance(params);
        } catch (Exception e2) {
            throw new UtilException(e2, "Instance class [{}] error!", clazz);
        }
    }

    public static <T> T newInstanceIfPossible(Class<T> beanClass) {
        Assert.notNull(beanClass);
        if (beanClass.isAssignableFrom(AbstractMap.class)) {
            beanClass = (Class<T>) HashMap.class;
        } else if (beanClass.isAssignableFrom(List.class)) {
            beanClass = (Class<T>) ArrayList.class;
        } else if (beanClass.isAssignableFrom(Set.class)) {
            beanClass = (Class<T>) HashSet.class;
        }
        try {
            return (T) newInstance(beanClass, new Object[0]);
        } catch (Exception e) {
            Constructor<T>[] constructors = getConstructors(beanClass);
            for (Constructor<T> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length != 0) {
                    setAccessible(constructor);
                    try {
                        return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
                    } catch (Exception e2) {
                    }
                }
            }
            return null;
        }
    }

    public static <T> T invokeStatic(Method method, Object... args) throws UtilException {
        return (T) invoke((Object) null, method, args);
    }

    public static <T> T invokeWithCheck(Object obj, Method method, Object... args) throws UtilException {
        Class<?>[] types = method.getParameterTypes();
        if (args != null) {
            Assert.isTrue(args.length == types.length, "Params length [{}] is not fit for param length [{}] of method !", Integer.valueOf(args.length), Integer.valueOf(types.length));
            for (int i = 0; i < args.length; i++) {
                Class<?> type = types[i];
                if (type.isPrimitive() && args[i] == null) {
                    args[i] = ClassUtil.getDefaultValue(type);
                }
            }
        }
        return (T) invoke(obj, method, args);
    }

    public static <T> T invoke(Object obj, Method method, Object... args) throws UtilException {
        setAccessible(method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] actualArgs = new Object[parameterTypes.length];
        Object obj2 = null;
        if (args != null) {
            for (int i = 0; i < actualArgs.length; i++) {
                if (i >= args.length || args[i] == null) {
                    actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
                } else if (args[i] instanceof NullWrapperBean) {
                    actualArgs[i] = null;
                } else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                    Object targetValue = Convert.convert((Class<Object>) parameterTypes[i], args[i]);
                    if (targetValue != null) {
                        actualArgs[i] = targetValue;
                    }
                } else {
                    actualArgs[i] = args[i];
                }
            }
        }
        if (method.isDefault()) {
            return (T) MethodHandleUtil.invokeSpecial(obj, method, args);
        }
        try {
            if (!ClassUtil.isStatic(method)) {
                obj2 = obj;
            }
            return (T) method.invoke(obj2, actualArgs);
        } catch (Exception e) {
            throw new UtilException(e);
        }
    }

    public static <T> T invoke(Object obj, String methodName, Object... args) throws UtilException {
        Assert.notNull(obj, "Object to get method must be not null!", new Object[0]);
        Assert.notBlank(methodName, "Method name must be not blank!", new Object[0]);
        Method method = getMethodOfObj(obj, methodName, args);
        if (method == null) {
            throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
        }
        return (T) invoke(obj, method, args);
    }

    public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
        if (accessibleObject != null && !accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
        return accessibleObject;
    }
}
