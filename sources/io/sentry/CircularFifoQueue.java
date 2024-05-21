package io.sentry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class CircularFifoQueue<E> extends AbstractCollection<E> implements Queue<E>, Serializable {
    private static final long serialVersionUID = -8423413834657610406L;
    @NotNull
    private transient E[] elements;
    private transient int end;
    private transient boolean full;
    private final int maxElements;
    private transient int start;

    public CircularFifoQueue() {
        this(32);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CircularFifoQueue(int size) {
        this.start = 0;
        this.end = 0;
        this.full = false;
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than 0");
        }
        this.elements = (E[]) new Object[size];
        this.maxElements = this.elements.length;
    }

    public CircularFifoQueue(@NotNull Collection<? extends E> coll) {
        this(coll.size());
        addAll(coll);
    }

    private void writeObject(@NotNull ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(size());
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e = it.next();
            out.writeObject(e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(@NotNull ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.elements = (E[]) new Object[this.maxElements];
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            this.elements[i] = in.readObject();
        }
        this.start = 0;
        this.full = size == this.maxElements;
        if (this.full) {
            this.end = 0;
        } else {
            this.end = size;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        int i = this.end;
        int i2 = this.start;
        if (i < i2) {
            int size = (this.maxElements - i2) + i;
            return size;
        } else if (i == i2) {
            int size2 = this.full ? this.maxElements : 0;
            return size2;
        } else {
            int size3 = i - i2;
            return size3;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFull() {
        return false;
    }

    public boolean isAtFullCapacity() {
        return size() == this.maxElements;
    }

    public int maxSize() {
        return this.maxElements;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.full = false;
        this.start = 0;
        this.end = 0;
        Arrays.fill(this.elements, (Object) null);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Queue
    public boolean add(@NotNull E element) {
        if (element == null) {
            throw new NullPointerException("Attempted to add null object to queue");
        }
        if (isAtFullCapacity()) {
            remove();
        }
        E[] eArr = this.elements;
        int i = this.end;
        this.end = i + 1;
        eArr[i] = element;
        if (this.end >= this.maxElements) {
            this.end = 0;
        }
        if (this.end == this.start) {
            this.full = true;
        }
        return true;
    }

    @NotNull
    public E get(int index) {
        int sz = size();
        if (index < 0 || index >= sz) {
            throw new NoSuchElementException(String.format("The specified index (%1$d) is outside the available range [0, %2$d)", Integer.valueOf(index), Integer.valueOf(sz)));
        }
        int idx = (this.start + index) % this.maxElements;
        return this.elements[idx];
    }

    @Override // java.util.Queue
    public boolean offer(@NotNull E element) {
        return add(element);
    }

    @Override // java.util.Queue
    @Nullable
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return remove();
    }

    @Override // java.util.Queue
    @Nullable
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        return peek();
    }

    @Override // java.util.Queue
    @Nullable
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return this.elements[this.start];
    }

    @Override // java.util.Queue
    @NotNull
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue is empty");
        }
        E[] eArr = this.elements;
        int i = this.start;
        E element = eArr[i];
        if (element != null) {
            this.start = i + 1;
            eArr[i] = null;
            if (this.start >= this.maxElements) {
                this.start = 0;
            }
            this.full = false;
        }
        return element;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int increment(int index) {
        int index2 = index + 1;
        if (index2 >= this.maxElements) {
            return 0;
        }
        return index2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int decrement(int index) {
        int index2 = index - 1;
        if (index2 < 0) {
            int index3 = this.maxElements - 1;
            return index3;
        }
        return index2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    @NotNull
    public Iterator<E> iterator() {
        return new Iterator<E>() { // from class: io.sentry.CircularFifoQueue.1
            private int index;
            private boolean isFirst;
            private int lastReturnedIndex = -1;

            {
                this.index = CircularFifoQueue.this.start;
                this.isFirst = CircularFifoQueue.this.full;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.isFirst || this.index != CircularFifoQueue.this.end;
            }

            @Override // java.util.Iterator
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.isFirst = false;
                int i = this.index;
                this.lastReturnedIndex = i;
                this.index = CircularFifoQueue.this.increment(i);
                return (E) CircularFifoQueue.this.elements[this.lastReturnedIndex];
            }

            @Override // java.util.Iterator
            public void remove() {
                int i = this.lastReturnedIndex;
                if (i != -1) {
                    if (i == CircularFifoQueue.this.start) {
                        CircularFifoQueue.this.remove();
                        this.lastReturnedIndex = -1;
                        return;
                    }
                    int pos = this.lastReturnedIndex + 1;
                    if (CircularFifoQueue.this.start >= this.lastReturnedIndex || pos >= CircularFifoQueue.this.end) {
                        while (pos != CircularFifoQueue.this.end) {
                            if (pos >= CircularFifoQueue.this.maxElements) {
                                CircularFifoQueue.this.elements[pos - 1] = CircularFifoQueue.this.elements[0];
                                pos = 0;
                            } else {
                                CircularFifoQueue.this.elements[CircularFifoQueue.this.decrement(pos)] = CircularFifoQueue.this.elements[pos];
                                pos = CircularFifoQueue.this.increment(pos);
                            }
                        }
                    } else {
                        System.arraycopy(CircularFifoQueue.this.elements, pos, CircularFifoQueue.this.elements, this.lastReturnedIndex, CircularFifoQueue.this.end - pos);
                    }
                    this.lastReturnedIndex = -1;
                    CircularFifoQueue circularFifoQueue = CircularFifoQueue.this;
                    circularFifoQueue.end = circularFifoQueue.decrement(circularFifoQueue.end);
                    CircularFifoQueue.this.elements[CircularFifoQueue.this.end] = null;
                    CircularFifoQueue.this.full = false;
                    this.index = CircularFifoQueue.this.decrement(this.index);
                    return;
                }
                throw new IllegalStateException();
            }
        };
    }
}
