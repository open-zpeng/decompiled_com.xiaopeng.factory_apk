package cn.hutool.core.comparator;

import cn.hutool.core.util.ObjectUtil;
import java.util.function.Function;
/* loaded from: classes.dex */
public class FuncComparator<T> extends NullComparator<T> {
    private static final long serialVersionUID = 1;
    private final Function<T, Comparable<?>> func;

    public FuncComparator(boolean nullGreater, Function<T, Comparable<?>> func) {
        super(nullGreater, null);
        this.func = func;
    }

    @Override // cn.hutool.core.comparator.NullComparator
    protected int doCompare(T a, T b) {
        try {
            Comparable<?> v1 = this.func.apply(a);
            Comparable<?> v2 = this.func.apply(b);
            return compare(a, b, v1, v2);
        } catch (Exception e) {
            throw new ComparatorException(e);
        }
    }

    private int compare(T o1, T o2, Comparable v1, Comparable v2) {
        int result = ObjectUtil.compare(v1, v2);
        if (result == 0) {
            return CompareUtil.compare(o1, o2, this.nullGreater);
        }
        return result;
    }
}
