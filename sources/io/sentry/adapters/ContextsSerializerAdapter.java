package io.sentry.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.protocol.Contexts;
import java.lang.reflect.Type;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class ContextsSerializerAdapter implements JsonSerializer<Contexts> {
    @NotNull
    private final SentryOptions options;

    public ContextsSerializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    @Override // com.google.gson.JsonSerializer
    @Nullable
    public JsonElement serialize(@Nullable Contexts src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        JsonObject object = new JsonObject();
        for (Map.Entry<String, Object> entry : src.entrySet()) {
            try {
                JsonElement element = context.serialize(entry.getValue(), Object.class);
                if (element != null) {
                    object.add(entry.getKey(), element);
                }
            } catch (JsonParseException e) {
                this.options.getLogger().log(SentryLevel.ERROR, "%s context key isn't serializable.", new Object[0]);
            }
        }
        return object;
    }
}
