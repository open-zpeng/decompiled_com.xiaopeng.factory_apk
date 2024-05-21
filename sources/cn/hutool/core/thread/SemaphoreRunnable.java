package cn.hutool.core.thread;

import java.util.concurrent.Semaphore;
/* loaded from: classes.dex */
public class SemaphoreRunnable implements Runnable {
    private final Runnable runnable;
    private final Semaphore semaphore;

    public SemaphoreRunnable(Runnable runnable, Semaphore semaphore) {
        this.runnable = runnable;
        this.semaphore = semaphore;
    }

    public Semaphore getSemaphore() {
        return this.semaphore;
    }

    @Override // java.lang.Runnable
    public void run() {
        Semaphore semaphore = this.semaphore;
        if (semaphore != null) {
            try {
                semaphore.acquire();
                this.runnable.run();
                this.semaphore.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
