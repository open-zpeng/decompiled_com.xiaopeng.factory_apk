package cn.hutool.core.io;
/* loaded from: classes.dex */
public interface StreamProgress {
    void finish();

    void progress(long j);

    void start();
}
