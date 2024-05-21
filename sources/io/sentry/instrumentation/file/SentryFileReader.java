package io.sentry.instrumentation.file;

import io.sentry.IHub;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SentryFileReader extends InputStreamReader {
    public SentryFileReader(@NotNull String fileName) throws FileNotFoundException {
        super(new SentryFileInputStream(fileName));
    }

    public SentryFileReader(@NotNull File file) throws FileNotFoundException {
        super(new SentryFileInputStream(file));
    }

    public SentryFileReader(@NotNull FileDescriptor fd) {
        super(new SentryFileInputStream(fd));
    }

    SentryFileReader(@NotNull File file, @NotNull IHub hub) throws FileNotFoundException {
        super(new SentryFileInputStream(file, hub));
    }
}
