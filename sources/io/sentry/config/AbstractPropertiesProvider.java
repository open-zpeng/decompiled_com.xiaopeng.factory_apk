package io.sentry.config;

import com.xiaopeng.commonfunc.Constant;
import io.sentry.util.Objects;
import io.sentry.util.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
abstract class AbstractPropertiesProvider implements PropertiesProvider {
    @NotNull
    private final String prefix;
    @NotNull
    private final Properties properties;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractPropertiesProvider(@NotNull String prefix, @NotNull Properties properties) {
        this.prefix = (String) Objects.requireNonNull(prefix, "prefix is required");
        this.properties = (Properties) Objects.requireNonNull(properties, "properties are required");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractPropertiesProvider(@NotNull Properties properties) {
        this("", properties);
    }

    @Override // io.sentry.config.PropertiesProvider
    @Nullable
    public String getProperty(@NotNull String property) {
        Properties properties = this.properties;
        return StringUtils.removeSurrounding(properties.getProperty(this.prefix + property), Constant.DOUBLE_QUOTA);
    }

    @Override // io.sentry.config.PropertiesProvider
    @NotNull
    public Map<String, String> getMap(@NotNull String property) {
        String prefix = this.prefix + property + ".";
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : this.properties.entrySet()) {
            if ((entry.getKey() instanceof String) && (entry.getValue() instanceof String)) {
                String key = (String) entry.getKey();
                if (key.startsWith(prefix)) {
                    String value = StringUtils.removeSurrounding((String) entry.getValue(), Constant.DOUBLE_QUOTA);
                    result.put(key.substring(prefix.length()), value);
                }
            }
        }
        return result;
    }
}
