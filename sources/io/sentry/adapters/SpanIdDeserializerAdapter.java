package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SpanId;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SpanIdDeserializerAdapter implements JsonDeserializer<SpanId> {
    @NotNull
    private final SentryOptions options;

    public SpanIdDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public SpanId deserialize(@Nullable JsonElement json, @Nullable Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        try {
            return new SpanId(json.getAsString());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing SpanId", e);
            return null;
        }
    }
}
