package io.sentry.transport;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class QueuedThreadPoolExecutor extends ThreadPoolExecutor {
    @NotNull
    private final ILogger logger;
    private final int maxQueueSize;
    @NotNull
    private final ReusableCountLatch unfinishedTasksCount;

    public QueuedThreadPoolExecutor(int corePoolSize, int maxQueueSize, @NotNull ThreadFactory threadFactory, @NotNull RejectedExecutionHandler rejectedExecutionHandler, @NotNull ILogger logger) {
        super(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), threadFactory, rejectedExecutionHandler);
        this.unfinishedTasksCount = new ReusableCountLatch();
        this.maxQueueSize = maxQueueSize;
        this.logger = logger;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(@NotNull Runnable task) {
        if (isSchedulingAllowed()) {
            this.unfinishedTasksCount.increment();
            return super.submit(task);
        }
        this.logger.log(SentryLevel.WARNING, "Submit cancelled", new Object[0]);
        return new CancelledFuture();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    protected void afterExecute(@NotNull Runnable r, @Nullable Throwable t) {
        try {
            super.afterExecute(r, t);
        } finally {
            this.unfinishedTasksCount.decrement();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void waitTillIdle(long timeoutMillis) {
        try {
            this.unfinishedTasksCount.waitTillZero(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            this.logger.log(SentryLevel.ERROR, "Failed to wait till idle", e);
            Thread.currentThread().interrupt();
        }
    }

    private boolean isSchedulingAllowed() {
        return this.unfinishedTasksCount.getCount() < this.maxQueueSize;
    }

    /* loaded from: classes2.dex */
    private static final class CancelledFuture<T> implements Future<T> {
        private CancelledFuture() {
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return true;
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return true;
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return true;
        }

        @Override // java.util.concurrent.Future
        public T get() {
            throw new CancellationException();
        }

        @Override // java.util.concurrent.Future
        public T get(long timeout, @NotNull TimeUnit unit) {
            throw new CancellationException();
        }
    }
}
