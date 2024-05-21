package io.sentry.config;

import io.sentry.ILogger;
import io.sentry.SentryLevel;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class FilesystemPropertiesLoader implements PropertiesLoader {
    @NotNull
    private final String filePath;
    @NotNull
    private final ILogger logger;

    public FilesystemPropertiesLoader(@NotNull String filePath, @NotNull ILogger logger) {
        this.filePath = filePath;
        this.logger = logger;
    }

    @Override // io.sentry.config.PropertiesLoader
    @Nullable
    public Properties load() {
        try {
            File f = new File(this.filePath);
            if (!f.isFile() || !f.canRead()) {
                return null;
            }
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            Properties properties = new Properties();
            properties.load(is);
            is.close();
            return properties;
        } catch (IOException e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed to load Sentry configuration from file: %s", this.filePath);
            return null;
        }
    }
}
