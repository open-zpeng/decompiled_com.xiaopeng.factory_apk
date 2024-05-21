package cn.hutool.core.thread;

import java.lang.Thread;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class ThreadUtil {
    public static ExecutorService newExecutor(int corePoolSize) {
        ExecutorBuilder builder = ExecutorBuilder.create();
        if (corePoolSize > 0) {
            builder.setCorePoolSize(corePoolSize);
        }
        return builder.build();
    }

    public static ExecutorService newExecutor() {
        return ExecutorBuilder.create().useSynchronousQueue().build();
    }

    public static ExecutorService newSingleExecutor() {
        return ExecutorBuilder.create().setCorePoolSize(1).setMaxPoolSize(1).setKeepAliveTime(0L).buildFinalizable();
    }

    public static ThreadPoolExecutor newExecutor(int corePoolSize, int maximumPoolSize) {
        return ExecutorBuilder.create().setCorePoolSize(corePoolSize).setMaxPoolSize(maximumPoolSize).build();
    }

    public static ExecutorService newExecutor(int corePoolSize, int maximumPoolSize, int maximumQueueSize) {
        return ExecutorBuilder.create().setCorePoolSize(corePoolSize).setMaxPoolSize(maximumPoolSize).setWorkQueue(new LinkedBlockingQueue(maximumQueueSize)).build();
    }

    public static ThreadPoolExecutor newExecutorByBlockingCoefficient(float blockingCoefficient) {
        if (blockingCoefficient < 1.0f && blockingCoefficient >= 0.0f) {
            int poolSize = (int) (Runtime.getRuntime().availableProcessors() / (1.0f - blockingCoefficient));
            return ExecutorBuilder.create().setCorePoolSize(poolSize).setMaxPoolSize(poolSize).setKeepAliveTime(0L).build();
        }
        throw new IllegalArgumentException("[blockingCoefficient] must between 0 and 1, or equals 0.");
    }

    public static void execute(Runnable runnable) {
        GlobalThreadPool.execute(runnable);
    }

    public static Runnable execAsync(Runnable runnable, boolean isDaemon) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(isDaemon);
        thread.start();
        return runnable;
    }

    public static <T> Future<T> execAsync(Callable<T> task) {
        return GlobalThreadPool.submit(task);
    }

    public static Future<?> execAsync(Runnable runnable) {
        return GlobalThreadPool.submit(runnable);
    }

    public static <T> CompletionService<T> newCompletionService() {
        return new ExecutorCompletionService(GlobalThreadPool.getExecutor());
    }

    public static <T> CompletionService<T> newCompletionService(ExecutorService executor) {
        return new ExecutorCompletionService(executor);
    }

    public static CountDownLatch newCountDownLatch(int threadCount) {
        return new CountDownLatch(threadCount);
    }

    public static Thread newThread(Runnable runnable, String name) {
        Thread t = newThread(runnable, name, false);
        if (t.getPriority() != 5) {
            t.setPriority(5);
        }
        return t;
    }

    public static Thread newThread(Runnable runnable, String name, boolean isDaemon) {
        Thread t = new Thread(null, runnable, name);
        t.setDaemon(isDaemon);
        return t;
    }

    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean sleep(Number millis) {
        if (millis == null) {
            return true;
        }
        return sleep(millis.longValue());
    }

    public static boolean sleep(long millis) {
        if (millis > 0) {
            try {
                Thread.sleep(millis);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    public static boolean safeSleep(Number millis) {
        if (millis == null) {
            return true;
        }
        return safeSleep(millis.longValue());
    }

    public static boolean safeSleep(long millis) {
        long done = 0;
        while (done >= 0 && done < millis) {
            long before = System.currentTimeMillis();
            if (!sleep(millis - done)) {
                return false;
            }
            long spendTime = System.currentTimeMillis() - before;
            if (spendTime > 0) {
                done += spendTime;
            } else {
                return true;
            }
        }
        return true;
    }

    public static StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    public static StackTraceElement getStackTraceElement(int i) {
        StackTraceElement[] stackTrace = getStackTrace();
        if (i < 0) {
            i += stackTrace.length;
        }
        return stackTrace[i];
    }

    public static <T> ThreadLocal<T> createThreadLocal(boolean isInheritable) {
        if (isInheritable) {
            return new InheritableThreadLocal();
        }
        return new ThreadLocal<>();
    }

    public static <T> ThreadLocal<T> createThreadLocal(Supplier<? extends T> supplier) {
        return ThreadLocal.withInitial(supplier);
    }

    public static ThreadFactoryBuilder createThreadFactoryBuilder() {
        return ThreadFactoryBuilder.create();
    }

    public static void interrupt(Thread thread, boolean isJoin) {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
            if (isJoin) {
                waitForDie(thread);
            }
        }
    }

    public static void waitForDie() {
        waitForDie(Thread.currentThread());
    }

    public static void waitForDie(Thread thread) {
        if (thread == null) {
            return;
        }
        boolean dead = false;
        do {
            try {
                thread.join();
                dead = true;
                continue;
            } catch (InterruptedException e) {
                continue;
            }
        } while (!dead);
    }

    public static Thread[] getThreads() {
        return getThreads(Thread.currentThread().getThreadGroup().getParent());
    }

    public static Thread[] getThreads(ThreadGroup group) {
        Thread[] slackList = new Thread[group.activeCount() * 2];
        int actualSize = group.enumerate(slackList);
        Thread[] result = new Thread[actualSize];
        System.arraycopy(slackList, 0, result, 0, actualSize);
        return result;
    }

    public static Thread getMainThread() {
        Thread[] threads;
        for (Thread thread : getThreads()) {
            if (thread.getId() == 1) {
                return thread;
            }
        }
        return null;
    }

    public static ThreadGroup currentThreadGroup() {
        SecurityManager s = System.getSecurityManager();
        return s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, boolean isDaemon) {
        return new NamedThreadFactory(prefix, isDaemon);
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon) {
        return new NamedThreadFactory(prefix, threadGroup, isDaemon);
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDaemon, Thread.UncaughtExceptionHandler handler) {
        return new NamedThreadFactory(prefix, threadGroup, isDaemon, handler);
    }

    public static void sync(Object obj) {
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public static ConcurrencyTester concurrencyTest(int threadSize, Runnable runnable) {
        return new ConcurrencyTester(threadSize).test(runnable);
    }

    public static ScheduledThreadPoolExecutor createScheduledExecutor(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }

    public static ScheduledThreadPoolExecutor schedule(ScheduledThreadPoolExecutor executor, Runnable command, long initialDelay, long period, boolean fixedRateOrFixedDelay) {
        return schedule(executor, command, initialDelay, period, TimeUnit.MILLISECONDS, fixedRateOrFixedDelay);
    }

    public static ScheduledThreadPoolExecutor schedule(ScheduledThreadPoolExecutor executor, Runnable command, long initialDelay, long period, TimeUnit timeUnit, boolean fixedRateOrFixedDelay) {
        if (executor == null) {
            executor = createScheduledExecutor(2);
        }
        if (fixedRateOrFixedDelay) {
            executor.scheduleAtFixedRate(command, initialDelay, period, timeUnit);
        } else {
            executor.scheduleWithFixedDelay(command, initialDelay, period, timeUnit);
        }
        return executor;
    }
}
