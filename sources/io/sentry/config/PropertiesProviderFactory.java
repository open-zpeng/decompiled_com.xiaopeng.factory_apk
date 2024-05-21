package io.sentry.config;

import io.sentry.ILogger;
import io.sentry.SystemOutLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class PropertiesProviderFactory {
    @NotNull
    public static PropertiesProvider create() {
        Properties properties;
        Properties properties2;
        ILogger logger = new SystemOutLogger();
        List<PropertiesProvider> providers = new ArrayList<>();
        providers.add(new SystemPropertyPropertiesProvider());
        providers.add(new EnvironmentVariablePropertiesProvider());
        String systemPropertyLocation = System.getProperty("sentry.properties.file");
        if (systemPropertyLocation != null && (properties2 = new FilesystemPropertiesLoader(systemPropertyLocation, logger).load()) != null) {
            providers.add(new SimplePropertiesProvider(properties2));
        }
        String environmentVariablesLocation = System.getenv("SENTRY_PROPERTIES_FILE");
        if (environmentVariablesLocation != null && (properties = new FilesystemPropertiesLoader(environmentVariablesLocation, logger).load()) != null) {
            providers.add(new SimplePropertiesProvider(properties));
        }
        Properties properties3 = new ClasspathPropertiesLoader(logger).load();
        if (properties3 != null) {
            providers.add(new SimplePropertiesProvider(properties3));
        }
        Properties runDirectoryProperties = new FilesystemPropertiesLoader("sentry.properties", logger).load();
        if (runDirectoryProperties != null) {
            providers.add(new SimplePropertiesProvider(runDirectoryProperties));
        }
        return new CompositePropertiesProvider(providers);
    }
}
