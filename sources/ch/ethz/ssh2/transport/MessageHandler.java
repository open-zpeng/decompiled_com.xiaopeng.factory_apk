package ch.ethz.ssh2.transport;

import java.io.IOException;
/* loaded from: classes.dex */
public interface MessageHandler {
    void handleMessage(byte[] bArr, int i) throws IOException;
}
