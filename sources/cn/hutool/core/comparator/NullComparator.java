package cn.hutool.core.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
/* loaded from: classes.dex */
public class NullComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 1;
    protected final Comparator<T> comparator;
    protected final boolean nullGreater;

    /* JADX WARN: Multi-variable type inference failed */
    public NullComparator(boolean nullGreater, Comparator<? super T> comparator) {
        this.nullGreater = nullGreater;
        this.comparator = comparator;
    }

    @Override // java.util.Comparator
    public int compare(T a, T b) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return this.nullGreater ? 1 : -1;
        } else if (b == null) {
            return this.nullGreater ? -1 : 1;
        } else {
            return doCompare(a, b);
        }
    }

    @Override // java.util.Comparator
    public Comparator<T> thenComparing(Comparator<? super T> other) {
        Objects.requireNonNull(other);
        boolean z = this.nullGreater;
        Comparator<T> comparator = this.comparator;
        return new NullComparator(z, comparator == null ? other : comparator.thenComparing(other));
    }

    @Override // java.util.Comparator
    public Comparator<T> reversed() {
        boolean z = !this.nullGreater;
        Comparator<T> comparator = this.comparator;
        return new NullComparator(z, comparator == null ? null : comparator.reversed());
    }

    protected int doCompare(T a, T b) {
        Comparator<T> comparator = this.comparator;
        if (comparator == null) {
            if ((a instanceof Comparable) && (b instanceof Comparable)) {
                return ((Comparable) a).compareTo(b);
            }
            return 0;
        }
        return comparator.compare(a, b);
    }
}
