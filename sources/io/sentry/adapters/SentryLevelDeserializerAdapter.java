package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import java.lang.reflect.Type;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryLevelDeserializerAdapter implements JsonDeserializer<SentryLevel> {
    @NotNull
    private final SentryOptions options;

    public SentryLevelDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public SentryLevel deserialize(@Nullable JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        try {
            return SentryLevel.valueOf(json.getAsString().toUpperCase(Locale.ROOT));
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing SentryLevel", e);
            return null;
        }
    }
}
