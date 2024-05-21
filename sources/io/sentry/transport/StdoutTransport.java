package io.sentry.transport;

import io.sentry.ISerializer;
import io.sentry.SentryEnvelope;
import io.sentry.util.Objects;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class StdoutTransport implements ITransport {
    @NotNull
    private final ISerializer serializer;

    public StdoutTransport(@NotNull ISerializer serializer) {
        this.serializer = (ISerializer) Objects.requireNonNull(serializer, "Serializer is required");
    }

    @Override // io.sentry.transport.ITransport
    public void send(@NotNull SentryEnvelope envelope, @Nullable Object hint) throws IOException {
        Objects.requireNonNull(envelope, "SentryEnvelope is required");
        try {
            this.serializer.serialize(envelope, System.out);
        } catch (Throwable th) {
        }
    }

    @Override // io.sentry.transport.ITransport
    public void flush(long timeoutMillis) {
        System.out.println("Flushing");
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
