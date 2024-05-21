package cn.hutool.core.map;

import java.util.Map;
import java.util.function.Function;
/* loaded from: classes.dex */
public class FuncKeyMap<K, V> extends CustomKeyMap<K, V> {
    private static final long serialVersionUID = 1;
    private final Function<Object, K> keyFunc;

    public FuncKeyMap(Map<K, V> m, Function<Object, K> keyFunc) {
        super(m);
        this.keyFunc = keyFunc;
    }

    @Override // cn.hutool.core.map.CustomKeyMap
    protected Object customKey(Object key) {
        Function<Object, K> function = this.keyFunc;
        if (function != null) {
            return function.apply(key);
        }
        return key;
    }
}
