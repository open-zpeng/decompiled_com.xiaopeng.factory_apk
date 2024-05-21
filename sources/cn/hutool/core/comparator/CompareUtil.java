package cn.hutool.core.comparator;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
/* loaded from: classes.dex */
public class CompareUtil {
    public static <T> int compare(T c1, T c2, Comparator<T> comparator) {
        if (comparator == null) {
            return compare((Comparable) c1, (Comparable) c2);
        }
        return comparator.compare(c1, c2);
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
        return compare((Comparable) c1, (Comparable) c2, false);
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean isNullGreater) {
        if (c1 == c2) {
            return 0;
        }
        if (c1 == null) {
            return isNullGreater ? 1 : -1;
        } else if (c2 == null) {
            return isNullGreater ? -1 : 1;
        } else {
            return c1.compareTo(c2);
        }
    }

    public static <T> int compare(T o1, T o2, boolean isNullGreater) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return isNullGreater ? 1 : -1;
        } else if (o2 == null) {
            return isNullGreater ? -1 : 1;
        } else if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
            return ((Comparable) o1).compareTo(o2);
        } else {
            if (o1.equals(o2)) {
                return 0;
            }
            int result = Integer.compare(o1.hashCode(), o2.hashCode());
            if (result == 0) {
                return compare(o1.toString(), o2.toString());
            }
            return result;
        }
    }

    public static <T> Comparator<T> comparingPinyin(Function<T, String> keyExtractor) {
        return comparingPinyin(keyExtractor, false);
    }

    public static <T> Comparator<T> comparingPinyin(final Function<T, String> keyExtractor, boolean reverse) {
        Objects.requireNonNull(keyExtractor);
        final PinyinComparator pinyinComparator = new PinyinComparator();
        if (reverse) {
            return new Comparator() { // from class: cn.hutool.core.comparator.-$$Lambda$CompareUtil$mDcJIyWXVUB-MuNSv9HUq2JOrPY
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = PinyinComparator.this.compare((String) r1.apply(obj2), (String) keyExtractor.apply(obj));
                    return compare;
                }
            };
        }
        return new Comparator() { // from class: cn.hutool.core.comparator.-$$Lambda$CompareUtil$948TztkgdPMil6YObqO5o2CrdFc
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = PinyinComparator.this.compare((String) r1.apply(obj), (String) keyExtractor.apply(obj2));
                return compare;
            }
        };
    }
}
