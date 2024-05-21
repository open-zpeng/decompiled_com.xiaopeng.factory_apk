package cn.hutool.core.net;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class LocalPortGenerater implements Serializable {
    private static final long serialVersionUID = 1;
    private final AtomicInteger alternativePort;

    public LocalPortGenerater(int beginPort) {
        this.alternativePort = new AtomicInteger(beginPort);
    }

    public int generate() {
        int validPort = this.alternativePort.get();
        while (!NetUtil.isUsableLocalPort(validPort)) {
            validPort = this.alternativePort.incrementAndGet();
        }
        return validPort;
    }
}
