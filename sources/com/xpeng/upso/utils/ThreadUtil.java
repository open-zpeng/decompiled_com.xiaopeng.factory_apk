package com.xpeng.upso.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public class ThreadUtil {
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static int count = 0;
    private static ExecutorService mExecutor = new ThreadPoolExecutor(2, 4, 5000, TimeUnit.MICROSECONDS, new LinkedBlockingQueue(), new ThreadFactory() { // from class: com.xpeng.upso.utils.ThreadUtil.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "Background Runner " + ThreadUtil.access$008());
        }
    });

    static /* synthetic */ int access$008() {
        int i = count;
        count = i + 1;
        return i;
    }

    public static void init() {
    }

    public static void runOnMainThread(Runnable runner) {
        if (runner == null) {
            return;
        }
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runner.run();
        } else {
            mMainHandler.post(runner);
        }
    }

    public static void runOnMainThreadDelay(Runnable runnable, long delayMs) {
        if (runnable == null) {
            return;
        }
        mMainHandler.postDelayed(runnable, delayMs);
    }

    public static void removeRunnable(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        mMainHandler.removeCallbacks(runnable);
    }

    public static void runOnChildThread(Runnable runner) {
        if (runner == null) {
            return;
        }
        new Thread(runner).start();
    }

    public static void runOnChildThreadDelay(final Runnable runner, long delayMs) {
        if (runner == null) {
            return;
        }
        mMainHandler.postDelayed(new Runnable() { // from class: com.xpeng.upso.utils.ThreadUtil.2
            @Override // java.lang.Runnable
            public void run() {
                if (ThreadUtil.mExecutor != null) {
                    ThreadUtil.mExecutor.submit(runner);
                }
            }
        }, delayMs);
    }

    public static void runInBackground(Runnable runnable) {
        ExecutorService executorService = mExecutor;
        if (executorService != null) {
            executorService.submit(runnable);
        }
    }
}
