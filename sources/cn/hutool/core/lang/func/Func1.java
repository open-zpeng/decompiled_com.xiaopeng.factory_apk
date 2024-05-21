package cn.hutool.core.lang.func;

import java.io.Serializable;
@FunctionalInterface
/* loaded from: classes.dex */
public interface Func1<P, R> extends Serializable {
    R call(P p) throws Exception;

    default R callWithRuntimeException(P parameter) {
        try {
            return call(parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
