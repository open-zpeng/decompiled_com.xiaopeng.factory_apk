package cn.hutool.core.util;

import cn.hutool.core.lang.ParameterizedTypeImpl;
import cn.hutool.core.lang.reflect.ActualTypeMapperPool;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;
/* loaded from: classes.dex */
public class TypeUtil {
    public static Class<?> getClass(Type type) {
        if (type != null) {
            if (type instanceof Class) {
                return (Class) type;
            }
            if (type instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) type).getRawType();
            }
            if (type instanceof TypeVariable) {
                return (Class) ((TypeVariable) type).getBounds()[0];
            }
            if (type instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) type).getUpperBounds();
                if (upperBounds.length == 1) {
                    return getClass(upperBounds[0]);
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public static Type getType(Field field) {
        if (field == null) {
            return null;
        }
        return field.getGenericType();
    }

    public static Type getFieldType(Class<?> clazz, String fieldName) {
        return getType(ReflectUtil.getField(clazz, fieldName));
    }

    public static Class<?> getClass(Field field) {
        if (field == null) {
            return null;
        }
        return field.getType();
    }

    public static Type getFirstParamType(Method method) {
        return getParamType(method, 0);
    }

    public static Class<?> getFirstParamClass(Method method) {
        return getParamClass(method, 0);
    }

    public static Type getParamType(Method method, int index) {
        Type[] types = getParamTypes(method);
        if (types != null && types.length > index) {
            return types[index];
        }
        return null;
    }

    public static Class<?> getParamClass(Method method, int index) {
        Class<?>[] classes = getParamClasses(method);
        if (classes != null && classes.length > index) {
            return classes[index];
        }
        return null;
    }

    public static Type[] getParamTypes(Method method) {
        if (method == null) {
            return null;
        }
        return method.getGenericParameterTypes();
    }

    public static Class<?>[] getParamClasses(Method method) {
        if (method == null) {
            return null;
        }
        return method.getParameterTypes();
    }

    public static Type getReturnType(Method method) {
        if (method == null) {
            return null;
        }
        return method.getGenericReturnType();
    }

    public static Class<?> getReturnClass(Method method) {
        if (method == null) {
            return null;
        }
        return method.getReturnType();
    }

    public static Type getTypeArgument(Type type) {
        return getTypeArgument(type, 0);
    }

    public static Type getTypeArgument(Type type, int index) {
        Type[] typeArguments = getTypeArguments(type);
        if (typeArguments != null && typeArguments.length > index) {
            return typeArguments[index];
        }
        return null;
    }

    public static Type[] getTypeArguments(Type type) {
        ParameterizedType parameterizedType;
        if (type == null || (parameterizedType = toParameterizedType(type)) == null) {
            return null;
        }
        return parameterizedType.getActualTypeArguments();
    }

    public static ParameterizedType toParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType result = (ParameterizedType) type;
            return result;
        } else if (!(type instanceof Class)) {
            return null;
        } else {
            Class<?> clazz = (Class) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (genericSuper == null || Object.class.equals(genericSuper)) {
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (ArrayUtil.isNotEmpty((Object[]) genericInterfaces)) {
                    genericSuper = genericInterfaces[0];
                }
            }
            ParameterizedType result2 = toParameterizedType(genericSuper);
            return result2;
        }
    }

    public static boolean isUnknown(Type type) {
        return type == null || (type instanceof TypeVariable);
    }

    public static boolean hasTypeVariable(Type... types) {
        for (Type type : types) {
            if (type instanceof TypeVariable) {
                return true;
            }
        }
        return false;
    }

    public static Map<Type, Type> getTypeMap(Class<?> clazz) {
        return ActualTypeMapperPool.get(clazz);
    }

    public static Type getActualType(Type type, Field field) {
        if (field == null) {
            return null;
        }
        return getActualType((Type) ObjectUtil.defaultIfNull(type, field.getDeclaringClass()), field.getGenericType());
    }

    public static Type getActualType(Type type, Type typeVariable) {
        if (typeVariable instanceof ParameterizedType) {
            return getActualType(type, (ParameterizedType) typeVariable);
        }
        if (typeVariable instanceof TypeVariable) {
            return ActualTypeMapperPool.getActualType(type, (TypeVariable) typeVariable);
        }
        return typeVariable;
    }

    public static Type getActualType(Type type, ParameterizedType parameterizedType) {
        if (hasTypeVariable(parameterizedType.getActualTypeArguments())) {
            Type[] actualTypeArguments = getActualTypes(type, parameterizedType.getActualTypeArguments());
            if (ArrayUtil.isNotEmpty((Object[]) actualTypeArguments)) {
                return new ParameterizedTypeImpl(actualTypeArguments, parameterizedType.getOwnerType(), parameterizedType.getRawType());
            }
            return parameterizedType;
        }
        return parameterizedType;
    }

    public static Type[] getActualTypes(Type type, Type... typeVariables) {
        return ActualTypeMapperPool.getActualTypes(type, typeVariables);
    }
}
