package cn.hutool.core.comparator;

import cn.hutool.core.lang.Chain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class ComparatorChain<E> implements Chain<Comparator<E>, ComparatorChain<E>>, Comparator<E>, Serializable {
    private static final long serialVersionUID = -2426725788913962429L;
    private final List<Comparator<E>> chain;
    private boolean lock;
    private final BitSet orderingBits;

    @Override // cn.hutool.core.lang.Chain
    public /* bridge */ /* synthetic */ Object addChain(Object obj) {
        return addChain((Comparator) ((Comparator) obj));
    }

    public static <E> ComparatorChain<E> of(Comparator<E> comparator) {
        return of((Comparator) comparator, false);
    }

    public static <E> ComparatorChain<E> of(Comparator<E> comparator, boolean reverse) {
        return new ComparatorChain<>(comparator, reverse);
    }

    @SafeVarargs
    public static <E> ComparatorChain<E> of(Comparator<E>... comparators) {
        return of(Arrays.asList(comparators));
    }

    public static <E> ComparatorChain<E> of(List<Comparator<E>> comparators) {
        return new ComparatorChain<>(comparators);
    }

    public static <E> ComparatorChain<E> of(List<Comparator<E>> comparators, BitSet bits) {
        return new ComparatorChain<>(comparators, bits);
    }

    public ComparatorChain() {
        this(new ArrayList(), new BitSet());
    }

    public ComparatorChain(Comparator<E> comparator) {
        this((Comparator) comparator, false);
    }

    public ComparatorChain(Comparator<E> comparator, boolean reverse) {
        this.lock = false;
        this.chain = new ArrayList(1);
        this.chain.add(comparator);
        this.orderingBits = new BitSet(1);
        if (reverse) {
            this.orderingBits.set(0);
        }
    }

    public ComparatorChain(List<Comparator<E>> list) {
        this(list, new BitSet(list.size()));
    }

    public ComparatorChain(List<Comparator<E>> list, BitSet bits) {
        this.lock = false;
        this.chain = list;
        this.orderingBits = bits;
    }

    public ComparatorChain<E> addComparator(Comparator<E> comparator) {
        return addComparator(comparator, false);
    }

    public ComparatorChain<E> addComparator(Comparator<E> comparator, boolean reverse) {
        checkLocked();
        this.chain.add(comparator);
        if (reverse) {
            this.orderingBits.set(this.chain.size() - 1);
        }
        return this;
    }

    public ComparatorChain<E> setComparator(int index, Comparator<E> comparator) throws IndexOutOfBoundsException {
        return setComparator(index, comparator, false);
    }

    public ComparatorChain<E> setComparator(int index, Comparator<E> comparator, boolean reverse) {
        checkLocked();
        this.chain.set(index, comparator);
        if (reverse) {
            this.orderingBits.set(index);
        } else {
            this.orderingBits.clear(index);
        }
        return this;
    }

    public ComparatorChain<E> setForwardSort(int index) {
        checkLocked();
        this.orderingBits.clear(index);
        return this;
    }

    public ComparatorChain<E> setReverseSort(int index) {
        checkLocked();
        this.orderingBits.set(index);
        return this;
    }

    public int size() {
        return this.chain.size();
    }

    public boolean isLocked() {
        return this.lock;
    }

    @Override // java.lang.Iterable
    public Iterator<Comparator<E>> iterator() {
        return this.chain.iterator();
    }

    public ComparatorChain<E> addChain(Comparator<E> element) {
        return addComparator(element);
    }

    @Override // java.util.Comparator
    public int compare(E o1, E o2) throws UnsupportedOperationException {
        if (!this.lock) {
            checkChainIntegrity();
            this.lock = true;
        }
        int comparatorIndex = 0;
        for (Comparator<E> comparator : this.chain) {
            int retval = comparator.compare(o1, o2);
            if (retval == 0) {
                comparatorIndex++;
            } else if (true == this.orderingBits.get(comparatorIndex)) {
                return retval > 0 ? -1 : 1;
            } else {
                return retval;
            }
        }
        return 0;
    }

    public int hashCode() {
        List<Comparator<E>> list = this.chain;
        int hash = list != null ? 0 ^ list.hashCode() : 0;
        BitSet bitSet = this.orderingBits;
        if (bitSet != null) {
            return hash ^ bitSet.hashCode();
        }
        return hash;
    }

    @Override // java.util.Comparator
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !object.getClass().equals(getClass())) {
            return false;
        }
        ComparatorChain<?> otherChain = (ComparatorChain) object;
        if (Objects.equals(this.orderingBits, otherChain.orderingBits) && this.chain.equals(otherChain.chain)) {
            return true;
        }
        return false;
    }

    private void checkLocked() {
        if (this.lock) {
            throw new UnsupportedOperationException("Comparator ordering cannot be changed after the first comparison is performed");
        }
    }

    private void checkChainIntegrity() {
        if (this.chain.size() == 0) {
            throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
        }
    }
}
