package cn.hutool.core.thread;

import cn.hutool.core.builder.Builder;
import cn.hutool.core.util.StrUtil;
import java.lang.Thread;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: classes.dex */
public class ThreadFactoryBuilder implements Builder<ThreadFactory> {
    private static final long serialVersionUID = 1;
    private ThreadFactory backingThreadFactory;
    private Boolean daemon;
    private String namePrefix;
    private Integer priority;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static ThreadFactoryBuilder create() {
        return new ThreadFactoryBuilder();
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
        this.backingThreadFactory = backingThreadFactory;
        return this;
    }

    public ThreadFactoryBuilder setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = Boolean.valueOf(daemon);
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        if (priority < 1) {
            throw new IllegalArgumentException(StrUtil.format("Thread priority ({}) must be >= {}", Integer.valueOf(priority), 1));
        }
        if (priority > 10) {
            throw new IllegalArgumentException(StrUtil.format("Thread priority ({}) must be <= {}", Integer.valueOf(priority), 10));
        }
        this.priority = Integer.valueOf(priority);
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        return this;
    }

    @Override // cn.hutool.core.builder.Builder
    public ThreadFactory build() {
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        ThreadFactory threadFactory = builder.backingThreadFactory;
        final ThreadFactory backingThreadFactory = threadFactory != null ? threadFactory : Executors.defaultThreadFactory();
        final String namePrefix = builder.namePrefix;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final Thread.UncaughtExceptionHandler handler = builder.uncaughtExceptionHandler;
        final AtomicLong count = namePrefix == null ? null : new AtomicLong();
        return new ThreadFactory() { // from class: cn.hutool.core.thread.-$$Lambda$ThreadFactoryBuilder$xmhewiFJBHbeezrzKxlphLe96Ag
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return ThreadFactoryBuilder.lambda$build$0(backingThreadFactory, namePrefix, count, daemon, priority, handler, runnable);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Thread lambda$build$0(ThreadFactory backingThreadFactory, String namePrefix, AtomicLong count, Boolean daemon, Integer priority, Thread.UncaughtExceptionHandler handler, Runnable r) {
        Thread thread = backingThreadFactory.newThread(r);
        if (namePrefix != null) {
            thread.setName(namePrefix + count.getAndIncrement());
        }
        if (daemon != null) {
            thread.setDaemon(daemon.booleanValue());
        }
        if (priority != null) {
            thread.setPriority(priority.intValue());
        }
        if (handler != null) {
            thread.setUncaughtExceptionHandler(handler);
        }
        return thread;
    }
}
