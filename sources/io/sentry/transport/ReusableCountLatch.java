package io.sentry.transport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class ReusableCountLatch {
    @NotNull
    private final Sync sync;

    public ReusableCountLatch(int initialCount) {
        if (initialCount < 0) {
            throw new IllegalArgumentException("negative initial count '" + initialCount + "' is not allowed");
        }
        this.sync = new Sync(initialCount);
    }

    public ReusableCountLatch() {
        this(0);
    }

    public int getCount() {
        return this.sync.getCount();
    }

    public void decrement() {
        this.sync.decrement();
    }

    public void increment() {
        this.sync.increment();
    }

    public void waitTillZero() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public boolean waitTillZero(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    /* loaded from: classes2.dex */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 5970133580157457018L;

        Sync(int count) {
            setState(count);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getCount() {
            return getState();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void increment() {
            int oldCount;
            int newCount;
            do {
                oldCount = getState();
                newCount = oldCount + 1;
            } while (!compareAndSetState(oldCount, newCount));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void decrement() {
            releaseShared(1);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        public int tryAcquireShared(int acquires) {
            return getState() == 0 ? 1 : -1;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        public boolean tryReleaseShared(int releases) {
            int oldCount;
            int newCount;
            do {
                oldCount = getState();
                if (oldCount == 0) {
                    return false;
                }
                newCount = oldCount - 1;
            } while (!compareAndSetState(oldCount, newCount));
            return newCount == 0;
        }
    }
}
