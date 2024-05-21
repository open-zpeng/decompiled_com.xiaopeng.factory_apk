package io.sentry.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface PropertiesProvider {
    @NotNull
    Map<String, String> getMap(@NotNull String str);

    @Nullable
    String getProperty(@NotNull String str);

    @NotNull
    default List<String> getList(@NotNull String property) {
        String value = getProperty(property);
        return value != null ? Arrays.asList(value.split(",")) : Collections.emptyList();
    }

    @NotNull
    default String getProperty(@NotNull String property, @NotNull String defaultValue) {
        String result = getProperty(property);
        return result != null ? result : defaultValue;
    }

    @Nullable
    default Boolean getBooleanProperty(@NotNull String property) {
        String result = getProperty(property);
        if (result != null) {
            return Boolean.valueOf(result);
        }
        return null;
    }

    @Nullable
    default Double getDoubleProperty(@NotNull String property) {
        String result = getProperty(property);
        if (result != null) {
            return Double.valueOf(result);
        }
        return null;
    }
}
