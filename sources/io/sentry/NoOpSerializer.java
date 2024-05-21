package io.sentry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class NoOpSerializer implements ISerializer {
    private static final NoOpSerializer instance = new NoOpSerializer();

    public static NoOpSerializer getInstance() {
        return instance;
    }

    private NoOpSerializer() {
    }

    @Override // io.sentry.ISerializer
    @Nullable
    public <T> T deserialize(@NotNull Reader reader, @NotNull Class<T> clazz) {
        return null;
    }

    @Override // io.sentry.ISerializer
    @Nullable
    public SentryEnvelope deserializeEnvelope(@NotNull InputStream inputStream) {
        return null;
    }

    @Override // io.sentry.ISerializer
    public <T> void serialize(@NotNull T entity, @NotNull Writer writer) throws IOException {
    }

    @Override // io.sentry.ISerializer
    public void serialize(@NotNull SentryEnvelope envelope, @NotNull OutputStream outputStream) throws Exception {
    }

    @Override // io.sentry.ISerializer
    @NotNull
    public String serialize(@NotNull Map<String, Object> data) throws Exception {
        return "";
    }
}
