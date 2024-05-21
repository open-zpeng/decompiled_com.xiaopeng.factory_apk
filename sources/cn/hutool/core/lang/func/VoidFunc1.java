package cn.hutool.core.lang.func;

import java.io.Serializable;
@FunctionalInterface
/* loaded from: classes.dex */
public interface VoidFunc1<P> extends Serializable {
    void call(P p) throws Exception;

    default void callWithRuntimeException(P parameter) {
        try {
            call(parameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
