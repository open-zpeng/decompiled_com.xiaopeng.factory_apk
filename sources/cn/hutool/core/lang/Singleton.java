package cn.hutool.core.lang;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.invoke.SerializedLambda;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class Singleton {
    private static final SimpleCache<String, Object> POOL = new SimpleCache<>(new HashMap());

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        char c;
        String implMethodName = lambda.getImplMethodName();
        int hashCode = implMethodName.hashCode();
        if (hashCode != -424253257) {
            if (hashCode == 3045982 && implMethodName.equals(NotificationCompat.CATEGORY_CALL)) {
                c = 0;
            }
            c = 65535;
        } else {
            if (implMethodName.equals("lambda$get$3f3ed817$1")) {
                c = 1;
            }
            c = 65535;
        }
        if (c != 0) {
            if (c == 1 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/lang/Singleton") && lambda.getImplMethodSignature().equals("(Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;")) {
                return new $$Lambda$Singleton$pmvIQdpuDPUaHxw82hpH0SAyVoE((Class) lambda.getCapturedArg(0), (Object[]) lambda.getCapturedArg(1));
            }
        } else if (lambda.getImplMethodKind() == 9 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getImplMethodSignature().equals("()Ljava/lang/Object;")) {
            return new $$Lambda$3T_NFUWZkl5q22SA3pCX3hB2R3I((Func0) lambda.getCapturedArg(0));
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    private Singleton() {
    }

    public static <T> T get(Class<T> clazz, Object... params) {
        Assert.notNull(clazz, "Class must be not null !", new Object[0]);
        String key = buildKey(clazz.getName(), params);
        return (T) get(key, new $$Lambda$Singleton$pmvIQdpuDPUaHxw82hpH0SAyVoE(clazz, params));
    }

    public static <T> T get(String key, Func0<T> supplier) {
        SimpleCache<String, Object> simpleCache = POOL;
        supplier.getClass();
        return (T) simpleCache.get(key, new $$Lambda$3T_NFUWZkl5q22SA3pCX3hB2R3I(supplier));
    }

    public static <T> T get(String className, Object... params) {
        Assert.notBlank(className, "Class name must be not blank !", new Object[0]);
        Class<T> clazz = ClassUtil.loadClass(className);
        return (T) get(clazz, params);
    }

    public static void put(Object obj) {
        Assert.notNull(obj, "Bean object must be not null !", new Object[0]);
        put(obj.getClass().getName(), obj);
    }

    public static void put(String key, Object obj) {
        POOL.put(key, obj);
    }

    public static void remove(Class<?> clazz) {
        if (clazz != null) {
            remove(clazz.getName());
        }
    }

    public static void remove(String key) {
        POOL.remove(key);
    }

    public static void destroy() {
        POOL.clear();
    }

    private static String buildKey(String className, Object... params) {
        return ArrayUtil.isEmpty(params) ? className : StrUtil.format("{}#{}", className, ArrayUtil.join(params, (CharSequence) "_"));
    }
}
