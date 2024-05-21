package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import io.sentry.DateUtils;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import java.lang.reflect.Type;
import java.util.Date;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class DateDeserializerAdapter implements JsonDeserializer<Date> {
    @NotNull
    private final SentryOptions options;

    public DateDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public Date deserialize(@Nullable JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json == null) {
            return null;
        }
        try {
            return DateUtils.getDateTime(json.getAsString());
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Error when deserializing UTC timestamp format, it might be millis timestamp format.", e);
            try {
                return DateUtils.getDateTimeWithMillisPrecision(json.getAsString());
            } catch (Throwable e2) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing millis timestamp format.", e2);
                return null;
            }
        }
    }
}
