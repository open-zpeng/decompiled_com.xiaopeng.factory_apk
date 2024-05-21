package cn.hutool.core.thread;

import cn.hutool.core.date.TimeInterval;
/* loaded from: classes.dex */
public class ConcurrencyTester {
    private long interval;
    private final SyncFinisher sf;
    private final TimeInterval timeInterval = new TimeInterval();

    public ConcurrencyTester(int threadSize) {
        this.sf = new SyncFinisher(threadSize);
    }

    public ConcurrencyTester test(Runnable runnable) {
        this.sf.clearWorker();
        this.timeInterval.start();
        this.sf.addRepeatWorker(runnable).setBeginAtSameTime(true).start();
        this.interval = this.timeInterval.interval();
        return this;
    }

    public ConcurrencyTester reset() {
        this.sf.clearWorker();
        this.timeInterval.restart();
        return this;
    }

    public long getInterval() {
        return this.interval;
    }
}
