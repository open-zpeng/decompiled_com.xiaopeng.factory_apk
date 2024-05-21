package cn.hutool.core.thread;

import java.util.concurrent.ExecutorService;
/* loaded from: classes.dex */
public class FinalizableDelegatedExecutorService extends DelegatedExecutorService {
    /* JADX INFO: Access modifiers changed from: package-private */
    public FinalizableDelegatedExecutorService(ExecutorService executor) {
        super(executor);
    }

    protected void finalize() {
        super.shutdown();
    }
}
