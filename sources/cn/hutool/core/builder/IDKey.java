package cn.hutool.core.builder;

import java.io.Serializable;
/* loaded from: classes.dex */
final class IDKey implements Serializable {
    private static final long serialVersionUID = 1;
    private final int id;
    private final Object value;

    public IDKey(Object obj) {
        this.id = System.identityHashCode(obj);
        this.value = obj;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object other) {
        if (other instanceof IDKey) {
            IDKey idKey = (IDKey) other;
            return this.id == idKey.id && this.value == idKey.value;
        }
        return false;
    }
}
