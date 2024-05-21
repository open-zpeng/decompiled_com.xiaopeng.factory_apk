package cn.hutool.core.lang.func;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
/* loaded from: classes.dex */
public class LambdaUtil {
    private static final SimpleCache<String, SerializedLambda> cache = new SimpleCache<>();

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        String implMethodName = lambda.getImplMethodName();
        if (((implMethodName.hashCode() == 2038608353 && implMethodName.equals("lambda$_resolve$4be13135$1")) ? (char) 0 : (char) 65535) == 0 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/lang/func/LambdaUtil") && lambda.getImplMethodSignature().equals("(Ljava/io/Serializable;)Ljava/lang/invoke/SerializedLambda;")) {
            return new $$Lambda$LambdaUtil$X2Hzp8zMc4V54U0BlMGGQTzwvls((Serializable) lambda.getCapturedArg(0));
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    public static <T> SerializedLambda resolve(Func1<T, ?> func) {
        return _resolve(func);
    }

    public static <T> String getMethodName(Func1<T, ?> func) {
        return resolve(func).getImplMethodName();
    }

    public static <T> String getFieldName(Func1<T, ?> func) throws IllegalArgumentException {
        String methodName = getMethodName(func);
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            return StrUtil.removePreAndLowerFirst(methodName, 3);
        }
        if (methodName.startsWith("is")) {
            return StrUtil.removePreAndLowerFirst(methodName, 2);
        }
        throw new IllegalArgumentException("Invalid Getter or Setter name: " + methodName);
    }

    private static <T> SerializedLambda _resolve(Serializable func) {
        return cache.get(func.getClass().getName(), new $$Lambda$LambdaUtil$X2Hzp8zMc4V54U0BlMGGQTzwvls(func));
    }

    public static /* synthetic */ SerializedLambda lambda$_resolve$4be13135$1(Serializable func) throws Exception {
        return (SerializedLambda) ReflectUtil.invoke(func, "writeReplace", new Object[0]);
    }
}
