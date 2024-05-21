package io.sentry;

import io.sentry.hints.Cached;
import io.sentry.hints.Flushable;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class DirectoryProcessor {
    private final long flushTimeoutMillis;
    @NotNull
    private final ILogger logger;

    protected abstract boolean isRelevantFileName(String str);

    protected abstract void processFile(@NotNull File file, @Nullable Object obj);

    /* JADX INFO: Access modifiers changed from: package-private */
    public DirectoryProcessor(@NotNull ILogger logger, long flushTimeoutMillis) {
        this.logger = logger;
        this.flushTimeoutMillis = flushTimeoutMillis;
    }

    public void processDirectory(@NotNull File directory) {
        try {
            this.logger.log(SentryLevel.DEBUG, "Processing dir. %s", directory.getAbsolutePath());
            if (!directory.exists()) {
                this.logger.log(SentryLevel.WARNING, "Directory '%s' doesn't exist. No cached events to send.", directory.getAbsolutePath());
            } else if (!directory.isDirectory()) {
                this.logger.log(SentryLevel.ERROR, "Cache dir %s is not a directory.", directory.getAbsolutePath());
            } else {
                File[] listFiles = directory.listFiles();
                if (listFiles == null) {
                    this.logger.log(SentryLevel.ERROR, "Cache dir %s is null.", directory.getAbsolutePath());
                    return;
                }
                File[] filteredListFiles = directory.listFiles(new FilenameFilter() { // from class: io.sentry.-$$Lambda$DirectoryProcessor$huMukvPbAyZ13k2-si51ba_uHRc
                    @Override // java.io.FilenameFilter
                    public final boolean accept(File file, String str) {
                        return DirectoryProcessor.this.lambda$processDirectory$0$DirectoryProcessor(file, str);
                    }
                });
                ILogger iLogger = this.logger;
                SentryLevel sentryLevel = SentryLevel.DEBUG;
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(filteredListFiles != null ? filteredListFiles.length : 0);
                objArr[1] = directory.getAbsolutePath();
                iLogger.log(sentryLevel, "Processing %d items from cache dir %s", objArr);
                for (File file : listFiles) {
                    if (!file.isFile()) {
                        this.logger.log(SentryLevel.DEBUG, "File %s is not a File.", file.getAbsolutePath());
                    } else {
                        this.logger.log(SentryLevel.DEBUG, "Processing file: %s", file.getAbsolutePath());
                        SendCachedEnvelopeHint hint = new SendCachedEnvelopeHint(this.flushTimeoutMillis, this.logger);
                        processFile(file, hint);
                    }
                }
            }
        } catch (Throwable e) {
            this.logger.log(SentryLevel.ERROR, e, "Failed processing '%s'", directory.getAbsolutePath());
        }
    }

    public /* synthetic */ boolean lambda$processDirectory$0$DirectoryProcessor(File d, String name) {
        return isRelevantFileName(name);
    }

    /* loaded from: classes2.dex */
    private static final class SendCachedEnvelopeHint implements Cached, Retryable, SubmissionResult, Flushable {
        private final long flushTimeoutMillis;
        @NotNull
        private final ILogger logger;
        boolean retry = false;
        boolean succeeded = false;
        private final CountDownLatch latch = new CountDownLatch(1);

        public SendCachedEnvelopeHint(long flushTimeoutMillis, @NotNull ILogger logger) {
            this.flushTimeoutMillis = flushTimeoutMillis;
            this.logger = logger;
        }

        @Override // io.sentry.hints.Retryable
        public boolean isRetry() {
            return this.retry;
        }

        @Override // io.sentry.hints.Retryable
        public void setRetry(boolean retry) {
            this.retry = retry;
        }

        @Override // io.sentry.hints.Flushable
        public boolean waitFlush() {
            try {
                return this.latch.await(this.flushTimeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.logger.log(SentryLevel.ERROR, "Exception while awaiting on lock.", e);
                return false;
            }
        }

        @Override // io.sentry.hints.SubmissionResult
        public void setResult(boolean succeeded) {
            this.succeeded = succeeded;
            this.latch.countDown();
        }

        @Override // io.sentry.hints.SubmissionResult
        public boolean isSuccess() {
            return this.succeeded;
        }
    }
}
