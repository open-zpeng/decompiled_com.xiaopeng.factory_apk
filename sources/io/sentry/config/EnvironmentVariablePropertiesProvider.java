package io.sentry.config;

import com.xiaopeng.commonfunc.Constant;
import io.sentry.util.StringUtils;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class EnvironmentVariablePropertiesProvider implements PropertiesProvider {
    private static final String PREFIX = "SENTRY";

    @Override // io.sentry.config.PropertiesProvider
    @Nullable
    public String getProperty(@NotNull String property) {
        return StringUtils.removeSurrounding(System.getenv(propertyToEnvironmentVariableName(property)), Constant.DOUBLE_QUOTA);
    }

    @Override // io.sentry.config.PropertiesProvider
    @NotNull
    public Map<String, String> getMap(@NotNull String property) {
        String value;
        String prefix = propertyToEnvironmentVariableName(property) + "_";
        Map<String, String> result = new ConcurrentHashMap<>();
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(prefix) && (value = StringUtils.removeSurrounding(entry.getValue(), Constant.DOUBLE_QUOTA)) != null) {
                result.put(key.substring(prefix.length()).toLowerCase(Locale.ROOT), value);
            }
        }
        return result;
    }

    @NotNull
    private String propertyToEnvironmentVariableName(@NotNull String property) {
        return "SENTRY_" + property.replace(".", "_").replace("-", "_").toUpperCase(Locale.ROOT);
    }
}
