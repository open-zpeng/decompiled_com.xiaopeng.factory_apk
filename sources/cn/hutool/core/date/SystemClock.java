package cn.hutool.core.date;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class SystemClock {
    private volatile long now = System.currentTimeMillis();
    private final long period;

    public SystemClock(long period) {
        this.period = period;
        scheduleClockUpdating();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: cn.hutool.core.date.-$$Lambda$SystemClock$WENLa6tNocVHjdl-8kcZlbsKL44
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return SystemClock.lambda$scheduleClockUpdating$0(runnable);
            }
        });
        Runnable runnable = new Runnable() { // from class: cn.hutool.core.date.-$$Lambda$SystemClock$owYdEI0vECb-uneyMZISAf95iVI
            @Override // java.lang.Runnable
            public final void run() {
                SystemClock.this.lambda$scheduleClockUpdating$1$SystemClock();
            }
        };
        long j = this.period;
        scheduler.scheduleAtFixedRate(runnable, j, j, TimeUnit.MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ Thread lambda$scheduleClockUpdating$0(Runnable runnable) {
        Thread thread = new Thread(runnable, "System Clock");
        thread.setDaemon(true);
        return thread;
    }

    public /* synthetic */ void lambda$scheduleClockUpdating$1$SystemClock() {
        this.now = System.currentTimeMillis();
    }

    private long currentTimeMillis() {
        return this.now;
    }

    /* loaded from: classes.dex */
    private static class InstanceHolder {
        public static final SystemClock INSTANCE = new SystemClock(1);

        private InstanceHolder() {
        }
    }

    public static long now() {
        return InstanceHolder.INSTANCE.currentTimeMillis();
    }

    public static String nowDate() {
        return new Timestamp(InstanceHolder.INSTANCE.currentTimeMillis()).toString();
    }
}
