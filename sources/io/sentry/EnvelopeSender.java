package io.sentry;

import io.sentry.cache.EnvelopeCache;
import io.sentry.hints.Flushable;
import io.sentry.hints.Retryable;
import io.sentry.util.LogUtils;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class EnvelopeSender extends DirectoryProcessor implements IEnvelopeSender {
    @NotNull
    private final IHub hub;
    @NotNull
    private final ILogger logger;
    @NotNull
    private final ISerializer serializer;

    @Override // io.sentry.DirectoryProcessor
    public /* bridge */ /* synthetic */ void processDirectory(@NotNull File file) {
        super.processDirectory(file);
    }

    public EnvelopeSender(@NotNull IHub hub, @NotNull ISerializer serializer, @NotNull ILogger logger, long flushTimeoutMillis) {
        super(logger, flushTimeoutMillis);
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required.");
        this.serializer = (ISerializer) Objects.requireNonNull(serializer, "Serializer is required.");
        this.logger = (ILogger) Objects.requireNonNull(logger, "Logger is required.");
    }

    @Override // io.sentry.DirectoryProcessor
    protected void processFile(@NotNull File file, @Nullable Object hint) {
        ILogger iLogger;
        SentryLevel sentryLevel;
        Object[] objArr;
        ILogger iLogger2;
        SentryLevel sentryLevel2;
        Object[] objArr2;
        if (!file.isFile()) {
            this.logger.log(SentryLevel.DEBUG, "'%s' is not a file.", file.getAbsolutePath());
        } else if (!isRelevantFileName(file.getName())) {
            this.logger.log(SentryLevel.DEBUG, "File '%s' doesn't match extension expected.", file.getAbsolutePath());
        } else {
            try {
                if (!file.getParentFile().canWrite()) {
                    this.logger.log(SentryLevel.WARNING, "File '%s' cannot be deleted so it will not be processed.", file.getAbsolutePath());
                    return;
                }
                try {
                    InputStream is = new BufferedInputStream(new FileInputStream(file));
                    SentryEnvelope envelope = this.serializer.deserializeEnvelope(is);
                    if (envelope == null) {
                        this.logger.log(SentryLevel.ERROR, "Failed to deserialize cached envelope %s", file.getAbsolutePath());
                    } else {
                        this.hub.captureEnvelope(envelope, hint);
                    }
                    if (!(hint instanceof Flushable)) {
                        LogUtils.logIfNotFlushable(this.logger, hint);
                    } else if (!((Flushable) hint).waitFlush()) {
                        this.logger.log(SentryLevel.WARNING, "Timed out waiting for envelope submission.", new Object[0]);
                    }
                    is.close();
                } catch (FileNotFoundException e) {
                    this.logger.log(SentryLevel.ERROR, e, "File '%s' cannot be found.", file.getAbsolutePath());
                    if (hint instanceof Retryable) {
                        if (((Retryable) hint).isRetry()) {
                            iLogger = this.logger;
                            sentryLevel = SentryLevel.INFO;
                            objArr = new Object[]{file.getAbsolutePath()};
                        } else {
                            safeDelete(file, "after trying to capture it");
                            iLogger2 = this.logger;
                            sentryLevel2 = SentryLevel.DEBUG;
                            objArr2 = new Object[]{file.getAbsolutePath()};
                        }
                    }
                } catch (IOException e2) {
                    this.logger.log(SentryLevel.ERROR, e2, "I/O on file '%s' failed.", file.getAbsolutePath());
                    if (hint instanceof Retryable) {
                        if (((Retryable) hint).isRetry()) {
                            iLogger = this.logger;
                            sentryLevel = SentryLevel.INFO;
                            objArr = new Object[]{file.getAbsolutePath()};
                        } else {
                            safeDelete(file, "after trying to capture it");
                            iLogger2 = this.logger;
                            sentryLevel2 = SentryLevel.DEBUG;
                            objArr2 = new Object[]{file.getAbsolutePath()};
                        }
                    }
                } catch (Throwable e3) {
                    this.logger.log(SentryLevel.ERROR, e3, "Failed to capture cached envelope %s", file.getAbsolutePath());
                    if (hint instanceof Retryable) {
                        ((Retryable) hint).setRetry(false);
                        this.logger.log(SentryLevel.INFO, e3, "File '%s' won't retry.", file.getAbsolutePath());
                    } else {
                        LogUtils.logIfNotRetryable(this.logger, hint);
                    }
                    if (hint instanceof Retryable) {
                        if (((Retryable) hint).isRetry()) {
                            iLogger = this.logger;
                            sentryLevel = SentryLevel.INFO;
                            objArr = new Object[]{file.getAbsolutePath()};
                        } else {
                            safeDelete(file, "after trying to capture it");
                            iLogger2 = this.logger;
                            sentryLevel2 = SentryLevel.DEBUG;
                            objArr2 = new Object[]{file.getAbsolutePath()};
                        }
                    }
                }
                if (hint instanceof Retryable) {
                    if (((Retryable) hint).isRetry()) {
                        iLogger = this.logger;
                        sentryLevel = SentryLevel.INFO;
                        objArr = new Object[]{file.getAbsolutePath()};
                        iLogger.log(sentryLevel, "File not deleted since retry was marked. %s.", objArr);
                        return;
                    }
                    safeDelete(file, "after trying to capture it");
                    iLogger2 = this.logger;
                    sentryLevel2 = SentryLevel.DEBUG;
                    objArr2 = new Object[]{file.getAbsolutePath()};
                    iLogger2.log(sentryLevel2, "Deleted file %s.", objArr2);
                    return;
                }
                LogUtils.logIfNotRetryable(this.logger, hint);
            } catch (Throwable th) {
                if (!(hint instanceof Retryable)) {
                    LogUtils.logIfNotRetryable(this.logger, hint);
                } else if (((Retryable) hint).isRetry()) {
                    this.logger.log(SentryLevel.INFO, "File not deleted since retry was marked. %s.", file.getAbsolutePath());
                } else {
                    safeDelete(file, "after trying to capture it");
                    this.logger.log(SentryLevel.DEBUG, "Deleted file %s.", file.getAbsolutePath());
                }
                throw th;
            }
        }
    }

    @Override // io.sentry.DirectoryProcessor
    protected boolean isRelevantFileName(@NotNull String fileName) {
        return fileName.endsWith(EnvelopeCache.SUFFIX_ENVELOPE_FILE);
    }

    @Override // io.sentry.IEnvelopeSender
    public void processEnvelopeFile(@NotNull String path, @Nullable Object hint) {
        Objects.requireNonNull(path, "Path is required.");
        processFile(new File(path), hint);
    }

    private void safeDelete(@NotNull File file, @NotNull String errorMessageSuffix) {
        try {
            if (!file.delete()) {
                this.logger.log(SentryLevel.ERROR, "Failed to delete '%s' %s", file.getAbsolutePath(), errorMessageSuffix);
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed to delete '%s' %s", file.getAbsolutePath(), errorMessageSuffix);
        }
    }
}
