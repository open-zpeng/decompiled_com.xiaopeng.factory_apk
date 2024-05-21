package io.sentry.instrumentation.file;

import io.sentry.IHub;
import io.sentry.ISpan;
import io.sentry.SpanStatus;
import io.sentry.util.Platform;
import io.sentry.util.StringUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class FileIOSpanManager {
    private long byteCount;
    @Nullable
    private final ISpan currentSpan;
    @Nullable
    private final File file;
    private final boolean isSendDefaultPii;
    @NotNull
    private SpanStatus spanStatus = SpanStatus.OK;

    @FunctionalInterface
    /* loaded from: classes2.dex */
    interface FileIOCallable<T> {
        T call() throws IOException;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public static ISpan startSpan(@NotNull IHub hub, @NotNull String op) {
        ISpan parent = hub.getSpan();
        if (parent != null) {
            return parent.startChild(op);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileIOSpanManager(@Nullable ISpan currentSpan, @Nullable File file, boolean isSendDefaultPii) {
        this.currentSpan = currentSpan;
        this.file = file;
        this.isSendDefaultPii = isSendDefaultPii;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> T performIO(@NotNull FileIOCallable<T> operation) throws IOException {
        try {
            T result = operation.call();
            if (result instanceof Integer) {
                int resUnboxed = ((Integer) result).intValue();
                if (resUnboxed != -1) {
                    this.byteCount += resUnboxed;
                }
            } else if (result instanceof Long) {
                long resUnboxed2 = ((Long) result).longValue();
                if (resUnboxed2 != -1) {
                    this.byteCount += resUnboxed2;
                }
            }
            return result;
        } catch (IOException exception) {
            this.spanStatus = SpanStatus.INTERNAL_ERROR;
            ISpan iSpan = this.currentSpan;
            if (iSpan != null) {
                iSpan.setThrowable(exception);
            }
            throw exception;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finish(@NotNull Closeable delegate) throws IOException {
        try {
            try {
                delegate.close();
            } catch (IOException exception) {
                this.spanStatus = SpanStatus.INTERNAL_ERROR;
                if (this.currentSpan != null) {
                    this.currentSpan.setThrowable(exception);
                }
                throw exception;
            }
        } finally {
            finishSpan();
        }
    }

    private void finishSpan() {
        if (this.currentSpan != null) {
            String byteCountToString = StringUtils.byteCountToString(this.byteCount);
            if (this.file != null) {
                String description = this.file.getName() + " (" + byteCountToString + ")";
                this.currentSpan.setDescription(description);
                if (Platform.isAndroid() || this.isSendDefaultPii) {
                    this.currentSpan.setData("file.path", this.file.getAbsolutePath());
                }
            } else {
                this.currentSpan.setDescription(byteCountToString);
            }
            this.currentSpan.setData("file.size", Long.valueOf(this.byteCount));
            this.currentSpan.finish(this.spanStatus);
        }
    }
}
