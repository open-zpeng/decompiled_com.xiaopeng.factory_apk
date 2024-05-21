package cn.hutool.core.lang.func;

import java.io.Serializable;
@FunctionalInterface
/* loaded from: classes.dex */
public interface Func<P, R> extends Serializable {
    R call(P... pArr) throws Exception;

    default R callWithRuntimeException(P... parameters) {
        try {
            return call(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
