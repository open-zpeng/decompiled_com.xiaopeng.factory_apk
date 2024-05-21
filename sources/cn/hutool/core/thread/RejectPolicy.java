package cn.hutool.core.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
/* loaded from: classes.dex */
public enum RejectPolicy {
    ABORT(new ThreadPoolExecutor.AbortPolicy()),
    DISCARD(new ThreadPoolExecutor.DiscardPolicy()),
    DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),
    CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy());
    
    private final RejectedExecutionHandler value;

    RejectPolicy(RejectedExecutionHandler handler) {
        this.value = handler;
    }

    public RejectedExecutionHandler getValue() {
        return this.value;
    }
}
