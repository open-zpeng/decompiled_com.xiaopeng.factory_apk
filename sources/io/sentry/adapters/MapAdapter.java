package io.sentry.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class MapAdapter implements JsonSerializer<Map<String, ?>> {
    @Override // com.google.gson.JsonSerializer
    @Nullable
    public JsonElement serialize(@Nullable Map<String, ?> src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, ?> entry : src.entrySet()) {
            JsonElement element = context.serialize(entry.getValue());
            jsonObject.add(entry.getKey(), element);
        }
        return jsonObject;
    }
}
