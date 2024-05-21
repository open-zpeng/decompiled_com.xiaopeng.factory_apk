package io.sentry.config;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class CompositePropertiesProvider implements PropertiesProvider {
    @NotNull
    private final List<PropertiesProvider> providers;

    public CompositePropertiesProvider(@NotNull List<PropertiesProvider> providers) {
        this.providers = providers;
    }

    @Override // io.sentry.config.PropertiesProvider
    @Nullable
    public String getProperty(@NotNull String property) {
        for (PropertiesProvider provider : this.providers) {
            String result = provider.getProperty(property);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override // io.sentry.config.PropertiesProvider
    @NotNull
    public Map<String, String> getMap(@NotNull String property) {
        Map<String, String> result = new ConcurrentHashMap<>();
        for (PropertiesProvider provider : this.providers) {
            result.putAll(provider.getMap(property));
        }
        return result;
    }
}
