package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.protocol.SentryId;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryIdDeserializerAdapter implements JsonDeserializer<SentryId> {
    @NotNull
    private final SentryOptions options;

    public SentryIdDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public SentryId deserialize(@Nullable JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        try {
            return new SentryId(json.getAsString());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing SentryId", e);
            return null;
        }
    }
}
