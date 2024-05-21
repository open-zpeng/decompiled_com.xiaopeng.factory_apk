package io.sentry.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class CollectionAdapter implements JsonSerializer<Collection<?>> {
    @Override // com.google.gson.JsonSerializer
    @Nullable
    public JsonElement serialize(@Nullable Collection<?> src, @NotNull Type typeOfSrc, @NotNull JsonSerializationContext context) {
        if (src == null || src.isEmpty()) {
            return null;
        }
        JsonArray array = new JsonArray();
        for (Object child : src) {
            JsonElement element = context.serialize(child);
            array.add(element);
        }
        return array;
    }
}
