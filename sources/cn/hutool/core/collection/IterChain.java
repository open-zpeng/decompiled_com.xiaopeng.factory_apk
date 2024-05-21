package cn.hutool.core.collection;

import cn.hutool.core.lang.Chain;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public class IterChain<T> implements Iterator<T>, Chain<Iterator<T>, IterChain<T>> {
    protected final List<Iterator<T>> allIterators = new ArrayList();
    protected int currentIter = -1;

    @Override // cn.hutool.core.lang.Chain
    public /* bridge */ /* synthetic */ Object addChain(Object obj) {
        return addChain((Iterator) ((Iterator) obj));
    }

    public IterChain() {
    }

    @SafeVarargs
    public IterChain(Iterator<T>... iterators) {
        for (Iterator<T> iterator : iterators) {
            addChain((Iterator) iterator);
        }
    }

    public IterChain<T> addChain(Iterator<T> iterator) {
        if (this.allIterators.contains(iterator)) {
            throw new IllegalArgumentException("Duplicate iterator");
        }
        this.allIterators.add(iterator);
        return this;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        if (this.currentIter == -1) {
            this.currentIter = 0;
        }
        int size = this.allIterators.size();
        for (int i = this.currentIter; i < size; i++) {
            Iterator<T> iterator = this.allIterators.get(i);
            if (iterator.hasNext()) {
                this.currentIter = i;
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Iterator
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return this.allIterators.get(this.currentIter).next();
    }

    @Override // java.util.Iterator
    public void remove() {
        int i = this.currentIter;
        if (-1 == i) {
            throw new IllegalStateException("next() has not yet been called");
        }
        this.allIterators.get(i).remove();
    }

    @Override // java.lang.Iterable
    public Iterator<Iterator<T>> iterator() {
        return this.allIterators.iterator();
    }
}
