package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import java.lang.reflect.Type;
import java.util.TimeZone;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class TimeZoneDeserializerAdapter implements JsonDeserializer<TimeZone> {
    @NotNull
    private final SentryOptions options;

    public TimeZoneDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public TimeZone deserialize(@Nullable JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        try {
            return TimeZone.getTimeZone(json.getAsString());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing TimeZone", e);
            return null;
        }
    }
}
