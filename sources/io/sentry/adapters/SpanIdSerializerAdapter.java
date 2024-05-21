package io.sentry.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SpanId;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SpanIdSerializerAdapter implements JsonSerializer<SpanId> {
    @NotNull
    private final SentryOptions options;

    public SpanIdSerializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    @Override // com.google.gson.JsonSerializer
    @Nullable
    public JsonElement serialize(@Nullable SpanId src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        try {
            return new JsonPrimitive(src.toString());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error when serializing SpanId", e);
            return null;
        }
    }
}