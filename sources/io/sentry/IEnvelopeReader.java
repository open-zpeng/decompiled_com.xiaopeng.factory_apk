package io.sentry;

import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface IEnvelopeReader {
    @Nullable
    SentryEnvelope read(@NotNull InputStream inputStream) throws IOException;
}
