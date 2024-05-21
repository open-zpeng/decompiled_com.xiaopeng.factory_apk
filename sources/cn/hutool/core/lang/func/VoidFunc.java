package cn.hutool.core.lang.func;

import java.io.Serializable;
@FunctionalInterface
/* loaded from: classes.dex */
public interface VoidFunc<P> extends Serializable {
    void call(P... pArr) throws Exception;

    default void callWithRuntimeException(P... parameters) {
        try {
            call(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
