package cn.hutool.core.lang.mutable;

import cn.hutool.core.text.CharSequenceUtil;
import java.io.Serializable;
/* loaded from: classes.dex */
public class MutableObj<T> implements Mutable<T>, Serializable {
    private static final long serialVersionUID = 1;
    private T value;

    public MutableObj() {
    }

    public MutableObj(T value) {
        this.value = value;
    }

    @Override // cn.hutool.core.lang.mutable.Mutable
    public T get() {
        return this.value;
    }

    @Override // cn.hutool.core.lang.mutable.Mutable
    public void set(T value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MutableObj<?> that = (MutableObj) obj;
        return this.value.equals(that.value);
    }

    public int hashCode() {
        T t = this.value;
        if (t == null) {
            return 0;
        }
        return t.hashCode();
    }

    public String toString() {
        T t = this.value;
        return t == null ? CharSequenceUtil.NULL : t.toString();
    }
}
