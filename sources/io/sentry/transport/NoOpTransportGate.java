package io.sentry.transport;
/* loaded from: classes2.dex */
public final class NoOpTransportGate implements ITransportGate {
    private static final NoOpTransportGate instance = new NoOpTransportGate();

    public static NoOpTransportGate getInstance() {
        return instance;
    }

    private NoOpTransportGate() {
    }

    @Override // io.sentry.transport.ITransportGate
    public boolean isConnected() {
        return true;
    }
}
