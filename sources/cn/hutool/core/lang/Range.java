package cn.hutool.core.lang;

import cn.hutool.core.thread.lock.NoLock;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes.dex */
public class Range<T> implements Iterable<T>, Iterator<T>, Serializable {
    private static final long serialVersionUID = 1;
    private final T end;
    private final boolean includeEnd;
    private final boolean includeStart;
    private int index;
    private Lock lock;
    private T next;
    private final T start;
    private final Stepper<T> stepper;

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface Stepper<T> {
        T step(T t, T t2, int i);
    }

    public Range(T start, Stepper<T> stepper) {
        this(start, null, stepper);
    }

    public Range(T start, T end, Stepper<T> stepper) {
        this(start, end, stepper, true, true);
    }

    public Range(T start, T end, Stepper<T> stepper, boolean isIncludeStart, boolean isIncludeEnd) {
        this.lock = new ReentrantLock();
        this.index = 0;
        Assert.notNull(start, "First element must be not null!", new Object[0]);
        this.start = start;
        this.end = end;
        this.stepper = stepper;
        this.next = safeStep(this.start);
        this.includeStart = isIncludeStart;
        this.includeEnd = isIncludeEnd;
    }

    public Range<T> disableLock() {
        this.lock = new NoLock();
        return this;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        this.lock.lock();
        try {
            if (this.index == 0 && this.includeStart) {
                return true;
            }
            if (this.next == null) {
                return false;
            }
            if (!this.includeEnd) {
                if (this.next.equals(this.end)) {
                    return false;
                }
            }
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Iterator
    public T next() {
        this.lock.lock();
        try {
            if (!hasNext()) {
                throw new NoSuchElementException("Has no next range!");
            }
            return nextUncheck();
        } finally {
            this.lock.unlock();
        }
    }

    private T nextUncheck() {
        T current;
        int i = this.index;
        if (i == 0) {
            current = this.start;
            if (!this.includeStart) {
                this.index = i + 1;
                return nextUncheck();
            }
        } else {
            current = this.next;
            this.next = safeStep(this.next);
        }
        this.index++;
        return current;
    }

    private T safeStep(T base) {
        int index = this.index;
        try {
            T next = this.stepper.step(base, this.end, index);
            return next;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("Can not remove ranged element!");
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this;
    }

    public Range<T> reset() {
        this.lock.lock();
        try {
            this.index = 0;
            this.next = safeStep(this.start);
            return this;
        } finally {
            this.lock.unlock();
        }
    }
}
