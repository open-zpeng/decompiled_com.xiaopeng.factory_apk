package io.sentry.instrumentation.file;

import io.sentry.IHub;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SentryFileWriter extends OutputStreamWriter {
    public SentryFileWriter(@NotNull String fileName) throws FileNotFoundException {
        super(new SentryFileOutputStream(fileName));
    }

    public SentryFileWriter(@NotNull String fileName, boolean append) throws FileNotFoundException {
        super(new SentryFileOutputStream(fileName, append));
    }

    public SentryFileWriter(@NotNull File file) throws FileNotFoundException {
        super(new SentryFileOutputStream(file));
    }

    public SentryFileWriter(@NotNull File file, boolean append) throws FileNotFoundException {
        super(new SentryFileOutputStream(file, append));
    }

    public SentryFileWriter(@NotNull FileDescriptor fd) {
        super(new SentryFileOutputStream(fd));
    }

    SentryFileWriter(@NotNull File file, @NotNull IHub hub) throws FileNotFoundException {
        super(new SentryFileOutputStream(file, hub));
    }
}
