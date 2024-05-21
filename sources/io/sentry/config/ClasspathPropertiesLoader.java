package io.sentry.config;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class ClasspathPropertiesLoader implements PropertiesLoader {
    @NotNull
    private final ClassLoader classLoader;
    @NotNull
    private final String fileName;
    @NotNull
    private final ILogger logger;

    public ClasspathPropertiesLoader(@NotNull String fileName, @NotNull ClassLoader classLoader, @NotNull ILogger logger) {
        this.fileName = fileName;
        this.classLoader = classLoader;
        this.logger = logger;
    }

    public ClasspathPropertiesLoader(@NotNull ILogger logger) {
        this("sentry.properties", ClasspathPropertiesLoader.class.getClassLoader(), logger);
    }

    @Override // io.sentry.config.PropertiesLoader
    @Nullable
    public Properties load() {
        try {
            InputStream inputStream = this.classLoader.getResourceAsStream(this.fileName);
            if (inputStream != null) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Properties properties = new Properties();
                properties.load(bufferedInputStream);
                bufferedInputStream.close();
                inputStream.close();
                return properties;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return null;
        } catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed to load Sentry configuration from classpath resource: %s", this.fileName);
            return null;
        }
    }
}
