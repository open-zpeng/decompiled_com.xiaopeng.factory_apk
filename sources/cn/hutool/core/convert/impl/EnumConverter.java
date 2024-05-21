package cn.hutool.core.convert.impl;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.lang.EnumItem;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class EnumConverter extends AbstractConverter<Object> {
    private static final SimpleCache<Class<?>, Map<Class<?>, Method>> VALUE_OF_METHOD_CACHE = new SimpleCache<>();
    private static final long serialVersionUID = 1;
    private final Class enumClass;

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        String implMethodName = lambda.getImplMethodName();
        if (((implMethodName.hashCode() == 1588861462 && implMethodName.equals("lambda$getMethodMap$73a18c6f$1")) ? (char) 0 : (char) 65535) == 0 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/convert/impl/EnumConverter") && lambda.getImplMethodSignature().equals("(Ljava/lang/Class;)Ljava/util/Map;")) {
            return new $$Lambda$EnumConverter$59zA9wjeJlMLBn8h9fttavSck((Class) lambda.getCapturedArg(0));
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    public EnumConverter(Class enumClass) {
        this.enumClass = enumClass;
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    protected Object convertInternal(Object value) {
        Enum enumValue = tryConvertEnum(value, this.enumClass);
        if (enumValue == null && !(value instanceof String)) {
            enumValue = Enum.valueOf(this.enumClass, convertToStr(value));
        }
        if (enumValue != null) {
            return enumValue;
        }
        throw new ConvertException("Can not convert {} to {}", value, this.enumClass);
    }

    @Override // cn.hutool.core.convert.AbstractConverter
    public Class<Object> getTargetType() {
        return this.enumClass;
    }

    protected static Enum tryConvertEnum(Object value, Class enumClass) {
        EnumItem first;
        if (value == null) {
            return null;
        }
        if (EnumItem.class.isAssignableFrom(enumClass) && (first = (EnumItem) EnumUtil.getEnumAt(enumClass, 0)) != null) {
            if (value instanceof Integer) {
                return (Enum) first.fromInt((Integer) value);
            }
            if (value instanceof String) {
                return (Enum) first.fromStr(value.toString());
            }
        }
        try {
            Map<Class<?>, Method> methodMap = getMethodMap(enumClass);
            if (MapUtil.isNotEmpty(methodMap)) {
                Class<?> valueClass = value.getClass();
                for (Map.Entry<Class<?>, Method> entry : methodMap.entrySet()) {
                    if (ClassUtil.isAssignable(entry.getKey(), valueClass)) {
                        return (Enum) ReflectUtil.invokeStatic(entry.getValue(), value);
                    }
                }
            }
        } catch (Exception e) {
        }
        if (value instanceof Integer) {
            Enum enumResult = EnumUtil.getEnumAt(enumClass, ((Integer) value).intValue());
            return enumResult;
        } else if (!(value instanceof String)) {
            return null;
        } else {
            try {
                Enum enumResult2 = Enum.valueOf(enumClass, (String) value);
                return enumResult2;
            } catch (IllegalArgumentException e2) {
                return null;
            }
        }
    }

    private static Map<Class<?>, Method> getMethodMap(Class<?> enumClass) {
        return VALUE_OF_METHOD_CACHE.get(enumClass, new $$Lambda$EnumConverter$59zA9wjeJlMLBn8h9fttavSck(enumClass));
    }

    public static /* synthetic */ boolean lambda$null$0(Class enumClass, Method m) {
        return m.getReturnType() == enumClass;
    }

    public static /* synthetic */ boolean lambda$null$1(Method m) {
        return m.getParameterCount() == 1;
    }

    public static /* synthetic */ boolean lambda$null$2(Method m) {
        return !"valueOf".equals(m.getName());
    }

    public static /* synthetic */ Map lambda$getMethodMap$73a18c6f$1(final Class enumClass) throws Exception {
        return (Map) Arrays.stream(enumClass.getMethods()).filter(new Predicate() { // from class: cn.hutool.core.convert.impl.-$$Lambda$V7NQ2dNZuWCxFFK1E_6DXH3-sHk
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ModifierUtil.isStatic((Method) obj);
            }
        }).filter(new Predicate() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$evVv0ZZPzmE6I0ME_VxErSwi_zg
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return EnumConverter.lambda$null$0(enumClass, (Method) obj);
            }
        }).filter(new Predicate() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$dbsJucHnqKqD1D0w2QNHynxQ67I
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return EnumConverter.lambda$null$1((Method) obj);
            }
        }).filter(new Predicate() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$qFwwclPslGPXay8ijUhXfqvLFFk
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return EnumConverter.lambda$null$2((Method) obj);
            }
        }).collect(Collectors.toMap(new Function() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$0KCXTY38ZzPOEV8FuDkVDTrdr0Q
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return EnumConverter.lambda$null$3((Method) obj);
            }
        }, new Function() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$zzvKe2HDGl46mojgkqFDBeUnftA
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return EnumConverter.lambda$null$4((Method) obj);
            }
        }, new BinaryOperator() { // from class: cn.hutool.core.convert.impl.-$$Lambda$EnumConverter$seES5BZmNWttPrxHBcZ7kf8Jiww
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return EnumConverter.lambda$null$5((Method) obj, (Method) obj2);
            }
        }));
    }

    public static /* synthetic */ Class lambda$null$3(Method m) {
        return m.getParameterTypes()[0];
    }

    public static /* synthetic */ Method lambda$null$4(Method m) {
        return m;
    }

    public static /* synthetic */ Method lambda$null$5(Method k1, Method k2) {
        return k1;
    }
}
