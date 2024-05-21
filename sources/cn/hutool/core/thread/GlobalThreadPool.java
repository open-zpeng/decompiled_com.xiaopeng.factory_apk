package cn.hutool.core.thread;

import cn.hutool.core.exceptions.UtilException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class GlobalThreadPool {
    private static ExecutorService executor;

    private GlobalThreadPool() {
    }

    static {
        init();
    }

    public static synchronized void init() {
        synchronized (GlobalThreadPool.class) {
            if (executor != null) {
                executor.shutdownNow();
            }
            executor = ExecutorBuilder.create().useSynchronousQueue().build();
        }
    }

    public static synchronized void shutdown(boolean isNow) {
        synchronized (GlobalThreadPool.class) {
            if (executor != null) {
                if (isNow) {
                    executor.shutdownNow();
                } else {
                    executor.shutdown();
                }
            }
        }
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            throw new UtilException(e, "Exception when running task!", new Object[0]);
        }
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    public static Future<?> submit(Runnable runnable) {
        return executor.submit(runnable);
    }
}
