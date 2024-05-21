package cn.hutool.core.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
/* loaded from: classes.dex */
public class BoundedPriorityQueue<E> extends PriorityQueue<E> {
    private static final long serialVersionUID = 3794348988671694820L;
    private final int capacity;
    private final Comparator<? super E> comparator;

    public BoundedPriorityQueue(int capacity) {
        this(capacity, null);
    }

    public BoundedPriorityQueue(int capacity, final Comparator<? super E> comparator) {
        super(capacity, new Comparator() { // from class: cn.hutool.core.collection.-$$Lambda$BoundedPriorityQueue$ugZPD4uhBxcIW8fiL6fSrR9n6u0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return BoundedPriorityQueue.lambda$new$0(comparator, obj, obj2);
            }
        });
        this.capacity = capacity;
        this.comparator = comparator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$new$0(Comparator comparator, Object o1, Object o2) {
        int cResult;
        if (comparator != null) {
            cResult = comparator.compare(o1, o2);
        } else {
            Comparable<E> o1c = (Comparable) o1;
            cResult = o1c.compareTo(o2);
        }
        return -cResult;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.PriorityQueue, java.util.Queue
    public boolean offer(E e) {
        if (size() >= this.capacity) {
            E head = peek();
            if (comparator().compare(e, head) <= 0) {
                return true;
            }
            poll();
        }
        return super.offer(e);
    }

    public boolean addAll(E[] c) {
        return addAll(Arrays.asList(c));
    }

    public ArrayList<E> toList() {
        ArrayList<E> list = new ArrayList<>(this);
        list.sort(this.comparator);
        return list;
    }

    @Override // java.util.PriorityQueue, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return toList().iterator();
    }
}
