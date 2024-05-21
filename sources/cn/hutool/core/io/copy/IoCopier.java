package cn.hutool.core.io.copy;

import cn.hutool.core.io.StreamProgress;
/* loaded from: classes.dex */
public abstract class IoCopier<S, T> {
    protected final int bufferSize;
    protected final long count;
    protected StreamProgress progress;

    public abstract long copy(S s, T t);

    public IoCopier(int bufferSize, long count, StreamProgress progress) {
        this.bufferSize = bufferSize > 0 ? bufferSize : 8192;
        this.count = count <= 0 ? Long.MAX_VALUE : count;
        this.progress = progress;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int bufferSize(long count) {
        return (int) Math.min(this.bufferSize, count);
    }
}
