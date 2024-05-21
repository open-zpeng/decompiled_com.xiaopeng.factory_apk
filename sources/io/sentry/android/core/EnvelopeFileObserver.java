package io.sentry.android.core;

import android.os.FileObserver;
import io.sentry.IEnvelopeSender;
import io.sentry.ILogger;
import io.sentry.SentryLevel;
import io.sentry.hints.ApplyScopeData;
import io.sentry.hints.Cached;
import io.sentry.hints.Flushable;
import io.sentry.hints.Resettable;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import io.sentry.util.Objects;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class EnvelopeFileObserver extends FileObserver {
    private final IEnvelopeSender envelopeSender;
    private final long flushTimeoutMillis;
    @NotNull
    private final ILogger logger;
    private final String rootPath;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EnvelopeFileObserver(String path, IEnvelopeSender envelopeSender, @NotNull ILogger logger, long flushTimeoutMillis) {
        super(path);
        this.rootPath = path;
        this.envelopeSender = (IEnvelopeSender) Objects.requireNonNull(envelopeSender, "Envelope sender is required.");
        this.logger = (ILogger) Objects.requireNonNull(logger, "Logger is required.");
        this.flushTimeoutMillis = flushTimeoutMillis;
    }

    @Override // android.os.FileObserver
    public void onEvent(int eventType, @Nullable String relativePath) {
        if (relativePath == null || eventType != 8) {
            return;
        }
        this.logger.log(SentryLevel.DEBUG, "onEvent fired for EnvelopeFileObserver with event type %d on path: %s for file %s.", Integer.valueOf(eventType), this.rootPath, relativePath);
        CachedEnvelopeHint hint = new CachedEnvelopeHint(this.flushTimeoutMillis, this.logger);
        IEnvelopeSender iEnvelopeSender = this.envelopeSender;
        iEnvelopeSender.processEnvelopeFile(this.rootPath + File.separator + relativePath, hint);
    }

    /* loaded from: classes2.dex */
    private static final class CachedEnvelopeHint implements Cached, Retryable, SubmissionResult, Flushable, ApplyScopeData, Resettable {
        private final long flushTimeoutMillis;
        @NotNull
        private CountDownLatch latch;
        @NotNull
        private final ILogger logger;
        boolean retry;
        boolean succeeded;

        public CachedEnvelopeHint(long flushTimeoutMillis, @NotNull ILogger logger) {
            reset();
            this.flushTimeoutMillis = flushTimeoutMillis;
            this.logger = (ILogger) Objects.requireNonNull(logger, "ILogger is required.");
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

        @Override // io.sentry.hints.Retryable
        public boolean isRetry() {
            return this.retry;
        }

        @Override // io.sentry.hints.Retryable
        public void setRetry(boolean retry) {
            this.retry = retry;
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

        @Override // io.sentry.hints.Resettable
        public void reset() {
            this.latch = new CountDownLatch(1);
            this.retry = false;
            this.succeeded = false;
        }
    }
}
