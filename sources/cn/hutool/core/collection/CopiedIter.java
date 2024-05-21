package cn.hutool.core.collection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class CopiedIter<E> implements Iterator<E>, Iterable<E>, Serializable {
    private static final long serialVersionUID = 1;
    private final Iterator<E> listIterator;

    public static <V> CopiedIter<V> copyOf(Iterator<V> iterator) {
        return new CopiedIter<>(iterator);
    }

    public CopiedIter(Iterator<E> iterator) {
        List<E> eleList = CollUtil.newArrayList(iterator);
        this.listIterator = eleList.iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.listIterator.hasNext();
    }

    @Override // java.util.Iterator
    public E next() {
        return this.listIterator.next();
    }

    @Override // java.util.Iterator
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This is a read-only iterator.");
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        return this;
    }
}
