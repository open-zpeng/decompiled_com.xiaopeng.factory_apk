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
public interface ISerializer {
    @Nullable
    <T> T deserialize(@NotNull Reader reader, @NotNull Class<T> cls);

    @Nullable
    SentryEnvelope deserializeEnvelope(@NotNull InputStream inputStream);

    @NotNull
    String serialize(@NotNull Map<String, Object> map) throws Exception;

    void serialize(@NotNull SentryEnvelope sentryEnvelope, @NotNull OutputStream outputStream) throws Exception;

    <T> void serialize(@NotNull T t, @NotNull Writer writer) throws IOException;
}
