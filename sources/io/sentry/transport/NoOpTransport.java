package io.sentry.transport;

import io.sentry.SentryEnvelope;
import java.io.IOException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class NoOpTransport implements ITransport {
    private static final NoOpTransport instance = new NoOpTransport();

    @NotNull
    public static NoOpTransport getInstance() {
        return instance;
    }

    private NoOpTransport() {
    }

    @Override // io.sentry.transport.ITransport
    public void send(@NotNull SentryEnvelope envelope, @Nullable Object hint) throws IOException {
    }

    @Override // io.sentry.transport.ITransport
    public void flush(long timeoutMillis) {
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
