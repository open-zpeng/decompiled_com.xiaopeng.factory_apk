package cn.hutool.core.comparator;

import java.io.Serializable;
import java.util.Comparator;
/* loaded from: classes.dex */
public class ReverseComparator<E> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = 8083701245147495562L;
    private final Comparator<? super E> comparator;

    public ReverseComparator(Comparator<? super E> comparator) {
        this.comparator = comparator == null ? ComparableComparator.INSTANCE : comparator;
    }

    @Override // java.util.Comparator
    public int compare(E o1, E o2) {
        return this.comparator.compare(o2, o1);
    }

    public int hashCode() {
        return "ReverseComparator".hashCode() ^ this.comparator.hashCode();
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !object.getClass().equals(getClass())) {
            return false;
        }
        ReverseComparator<?> thatrc = (ReverseComparator) object;
        return this.comparator.equals(thatrc.comparator);
    }
}
