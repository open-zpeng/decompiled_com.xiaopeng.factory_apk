package io.sentry.instrumentation.file;

import io.sentry.ISpan;
import java.io.File;
import java.io.FileInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class FileInputStreamInitData {
    @NotNull
    final FileInputStream delegate;
    @Nullable
    final File file;
    final boolean isSendDefaultPii;
    @Nullable
    final ISpan span;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileInputStreamInitData(@Nullable File file, @Nullable ISpan span, @NotNull FileInputStream delegate, boolean isSendDefaultPii) {
        this.file = file;
        this.span = span;
        this.delegate = delegate;
        this.isSendDefaultPii = isSendDefaultPii;
    }
}
