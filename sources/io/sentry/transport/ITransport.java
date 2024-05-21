package io.sentry.transport;

import io.sentry.SentryEnvelope;
import java.io.Closeable;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface ITransport extends Closeable {
    void flush(long j);

    void send(@NotNull SentryEnvelope sentryEnvelope, @Nullable Object obj) throws IOException;

    default void send(@NotNull SentryEnvelope envelope) throws IOException {
        send(envelope, null);
    }
}
