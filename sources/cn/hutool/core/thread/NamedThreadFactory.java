package cn.hutool.core.thread;

import cn.hutool.core.util.StrUtil;
import java.lang.Thread;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class NamedThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final Thread.UncaughtExceptionHandler handler;
    private final boolean isDaemon;
    private final String prefix;
    private final AtomicInteger threadNumber;

    public NamedThreadFactory(String prefix, boolean isDaemon) {
        this(prefix, null, isDaemon);
    }

    public NamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon) {
        this(prefix, threadGroup, isDaemon, null);
    }

    public NamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon, Thread.UncaughtExceptionHandler handler) {
        this.threadNumber = new AtomicInteger(1);
        this.prefix = StrUtil.isBlank(prefix) ? "Hutool" : prefix;
        this.group = threadGroup == null ? ThreadUtil.currentThreadGroup() : threadGroup;
        this.isDaemon = isDaemon;
        this.handler = handler;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, StrUtil.format("{}{}", this.prefix, Integer.valueOf(this.threadNumber.getAndIncrement())));
        if (!t.isDaemon()) {
            if (this.isDaemon) {
                t.setDaemon(true);
            }
        } else if (!this.isDaemon) {
            t.setDaemon(false);
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.handler;
        if (uncaughtExceptionHandler != null) {
            t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        if (5 != t.getPriority()) {
            t.setPriority(5);
        }
        return t;
    }
}
