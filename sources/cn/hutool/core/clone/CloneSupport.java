package cn.hutool.core.clone;
/* loaded from: classes.dex */
public class CloneSupport<T> implements Cloneable<T> {
    @Override // cn.hutool.core.clone.Cloneable
    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneRuntimeException(e);
        }
    }
}
