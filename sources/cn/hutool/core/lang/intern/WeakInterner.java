package cn.hutool.core.lang.intern;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.lang.SimpleCache;
import java.lang.invoke.SerializedLambda;
/* loaded from: classes.dex */
public class WeakInterner<T> implements Interner<T> {
    private final SimpleCache<T, T> cache = new SimpleCache<>();

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        String implMethodName = lambda.getImplMethodName();
        if (((implMethodName.hashCode() == -654612031 && implMethodName.equals("lambda$intern$cf951c4b$1")) ? (char) 0 : (char) 65535) == 0 && lambda.getImplMethodKind() == 6 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("cn/hutool/core/lang/intern/WeakInterner") && lambda.getImplMethodSignature().equals("(Ljava/lang/Object;)Ljava/lang/Object;")) {
            return new $$Lambda$WeakInterner$022e5Yd_EN4uwBWsP559doIl5dA(lambda.getCapturedArg(0));
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    @Override // cn.hutool.core.lang.intern.Interner
    public T intern(T sample) {
        if (sample == null) {
            return null;
        }
        return this.cache.get(sample, new $$Lambda$WeakInterner$022e5Yd_EN4uwBWsP559doIl5dA(sample));
    }

    public static /* synthetic */ Object lambda$intern$cf951c4b$1(Object sample) throws Exception {
        return sample;
    }
}
