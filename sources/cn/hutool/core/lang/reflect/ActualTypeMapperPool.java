package cn.hutool.core.lang.reflect;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.TypeUtil;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class ActualTypeMapperPool {
    private static final SimpleCache<Type, Map<Type, Type>> CACHE = new SimpleCache<>();

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        String implMethodName = lambda.getImplMethodName();
        if (((implMethodName.hashCode() == 1806479530 && implMethodName.equals("lambda$get$2c794883$1")) ? (char) 0 : (char) 65535) == 0 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/lang/reflect/ActualTypeMapperPool") && lambda.getImplMethodSignature().equals("(Ljava/lang/reflect/Type;)Ljava/util/Map;")) {
            return new $$Lambda$ActualTypeMapperPool$iMVT3XDJ_FdxEgDlF6iWDexAJA((Type) lambda.getCapturedArg(0));
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    public static Map<Type, Type> get(Type type) {
        return CACHE.get(type, new $$Lambda$ActualTypeMapperPool$iMVT3XDJ_FdxEgDlF6iWDexAJA(type));
    }

    public static Type getActualType(Type type, TypeVariable<?> typeVariable) {
        Map<Type, Type> typeTypeMap = get(type);
        Type result = typeTypeMap.get(typeVariable);
        while (true) {
            Type result2 = result;
            if (result2 instanceof TypeVariable) {
                result = typeTypeMap.get(result2);
            } else {
                return result2;
            }
        }
    }

    public static Type[] getActualTypes(Type type, Type... typeVariables) {
        Type[] result = new Type[typeVariables.length];
        for (int i = 0; i < typeVariables.length; i++) {
            result[i] = typeVariables[i] instanceof TypeVariable ? getActualType(type, (TypeVariable) typeVariables[i]) : typeVariables[i];
        }
        return result;
    }

    public static Map<Type, Type> createTypeMap(Type type) {
        Map<Type, Type> typeMap = new HashMap<>();
        while (type != null) {
            ParameterizedType parameterizedType = TypeUtil.toParameterizedType(type);
            if (parameterizedType == null) {
                break;
            }
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Class<?> rawType = (Class) parameterizedType.getRawType();
            Type[] typeParameters = rawType.getTypeParameters();
            for (int i = 0; i < typeParameters.length; i++) {
                typeMap.put(typeParameters[i], typeArguments[i]);
            }
            type = rawType;
        }
        return typeMap;
    }
}
