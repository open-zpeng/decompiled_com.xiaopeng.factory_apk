package cn.hutool.core.map.multi;

import androidx.core.app.NotificationCompat;
import cn.hutool.core.lang.func.Func0;
import java.lang.invoke.SerializedLambda;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CollectionValueMap<K, V> extends AbsCollValueMap<K, V, Collection<V>> {
    private static final long serialVersionUID = 9012989578038102983L;
    private final Func0<Collection<V>> collectionCreateFunc;

    private static /* synthetic */ Object $deserializeLambda$(SerializedLambda lambda) {
        String implMethodName = lambda.getImplMethodName();
        if (((implMethodName.hashCode() == 1818100338 && implMethodName.equals("<init>")) ? (char) 0 : (char) 65535) == 0 && ((lambda.getImplMethodKind() == 8 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("java/util/ArrayList") && lambda.getImplMethodSignature().equals("()V")) || (lambda.getImplMethodKind() == 8 && lambda.getFunctionalInterfaceClass().equals("cn/hutool/core/lang/func/Func0") && lambda.getFunctionalInterfaceMethodName().equals(NotificationCompat.CATEGORY_CALL) && lambda.getFunctionalInterfaceMethodSignature().equals("()Ljava/lang/Object;") && lambda.getImplClass().equals("java/util/ArrayList") && lambda.getImplMethodSignature().equals("()V")))) {
            return $$Lambda$i3LPfHC0s9RREJD4nFNNRFbs6pM.INSTANCE;
        }
        throw new IllegalArgumentException("Invalid lambda deserialization");
    }

    public CollectionValueMap() {
        this(16);
    }

    public CollectionValueMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CollectionValueMap(Map<? extends K, ? extends Collection<V>> m) {
        this(0.75f, m);
    }

    public CollectionValueMap(float loadFactor, Map<? extends K, ? extends Collection<V>> m) {
        this(loadFactor, m, $$Lambda$i3LPfHC0s9RREJD4nFNNRFbs6pM.INSTANCE);
    }

    public CollectionValueMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, $$Lambda$i3LPfHC0s9RREJD4nFNNRFbs6pM.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CollectionValueMap(float loadFactor, Map<? extends K, ? extends Collection<V>> m, Func0<Collection<V>> collectionCreateFunc) {
        this(m.size(), loadFactor, collectionCreateFunc);
        putAll(m);
    }

    public CollectionValueMap(int initialCapacity, float loadFactor, Func0<Collection<V>> collectionCreateFunc) {
        super(new HashMap(initialCapacity, loadFactor));
        this.collectionCreateFunc = collectionCreateFunc;
    }

    @Override // cn.hutool.core.map.multi.AbsCollValueMap
    protected Collection<V> createCollection() {
        return this.collectionCreateFunc.callWithRuntimeException();
    }
}
