package io.sentry.instrumentation.file;

import io.sentry.ISpan;
import java.io.File;
import java.io.FileOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class FileOutputStreamInitData {
    final boolean append;
    @NotNull
    final FileOutputStream delegate;
    @Nullable
    final File file;
    final boolean isSendDefaultPii;
    @Nullable
    final ISpan span;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileOutputStreamInitData(@Nullable File file, boolean append, @Nullable ISpan span, @NotNull FileOutputStream delegate, boolean isSendDefaultPii) {
        this.file = file;
        this.append = append;
        this.span = span;
        this.delegate = delegate;
        this.isSendDefaultPii = isSendDefaultPii;
    }
}
