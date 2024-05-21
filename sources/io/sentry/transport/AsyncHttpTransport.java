package io.sentry.transport;

import io.sentry.ILogger;
import io.sentry.RequestDetails;
import io.sentry.SentryEnvelope;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.cache.IEnvelopeCache;
import io.sentry.hints.Cached;
import io.sentry.hints.DiskFlushNotification;
import io.sentry.hints.Retryable;
import io.sentry.hints.SubmissionResult;
import io.sentry.util.LogUtils;
import io.sentry.util.Objects;
import java.io.IOException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class AsyncHttpTransport implements ITransport {
    @NotNull
    private final HttpConnection connection;
    @NotNull
    private final IEnvelopeCache envelopeCache;
    @NotNull
    private final QueuedThreadPoolExecutor executor;
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final RateLimiter rateLimiter;
    @NotNull
    private final ITransportGate transportGate;

    public AsyncHttpTransport(@NotNull SentryOptions options, @NotNull RateLimiter rateLimiter, @NotNull ITransportGate transportGate, @NotNull RequestDetails requestDetails) {
        this(initExecutor(options.getMaxQueueSize(), options.getEnvelopeDiskCache(), options.getLogger()), options, rateLimiter, transportGate, new HttpConnection(options, requestDetails, rateLimiter));
    }

    public AsyncHttpTransport(@NotNull QueuedThreadPoolExecutor executor, @NotNull SentryOptions options, @NotNull RateLimiter rateLimiter, @NotNull ITransportGate transportGate, @NotNull HttpConnection httpConnection) {
        this.executor = (QueuedThreadPoolExecutor) Objects.requireNonNull(executor, "executor is required");
        this.envelopeCache = (IEnvelopeCache) Objects.requireNonNull(options.getEnvelopeDiskCache(), "envelopeCache is required");
        this.options = (SentryOptions) Objects.requireNonNull(options, "options is required");
        this.rateLimiter = (RateLimiter) Objects.requireNonNull(rateLimiter, "rateLimiter is required");
        this.transportGate = (ITransportGate) Objects.requireNonNull(transportGate, "transportGate is required");
        this.connection = (HttpConnection) Objects.requireNonNull(httpConnection, "httpConnection is required");
    }

    @Override // io.sentry.transport.ITransport
    public void send(@NotNull SentryEnvelope envelope, @Nullable Object hint) throws IOException {
        IEnvelopeCache currentEnvelopeCache = this.envelopeCache;
        boolean cached = false;
        if (hint instanceof Cached) {
            currentEnvelopeCache = NoOpEnvelopeCache.getInstance();
            cached = true;
            this.options.getLogger().log(SentryLevel.DEBUG, "Captured Envelope is already cached", new Object[0]);
        }
        SentryEnvelope filteredEnvelope = this.rateLimiter.filter(envelope, hint);
        if (filteredEnvelope == null) {
            if (cached) {
                this.envelopeCache.discard(envelope);
                return;
            }
            return;
        }
        this.executor.submit(new EnvelopeSender(filteredEnvelope, hint, currentEnvelopeCache));
    }

    @Override // io.sentry.transport.ITransport
    public void flush(long timeoutMillis) {
        this.executor.waitTillIdle(timeoutMillis);
    }

    private static QueuedThreadPoolExecutor initExecutor(int maxQueueSize, @NotNull final IEnvelopeCache envelopeCache, @NotNull final ILogger logger) {
        RejectedExecutionHandler storeEvents = new RejectedExecutionHandler() { // from class: io.sentry.transport.-$$Lambda$AsyncHttpTransport$6Q77f17yLf-qOKI5FV6AeklhwZQ
            @Override // java.util.concurrent.RejectedExecutionHandler
            public final void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                AsyncHttpTransport.lambda$initExecutor$0(IEnvelopeCache.this, logger, runnable, threadPoolExecutor);
            }
        };
        return new QueuedThreadPoolExecutor(1, maxQueueSize, new AsyncConnectionThreadFactory(), storeEvents, logger);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$initExecutor$0(IEnvelopeCache envelopeCache, ILogger logger, Runnable r, ThreadPoolExecutor executor) {
        if (r instanceof EnvelopeSender) {
            EnvelopeSender envelopeSender = (EnvelopeSender) r;
            if (!(envelopeSender.hint instanceof Cached)) {
                envelopeCache.store(envelopeSender.envelope, envelopeSender.hint);
            }
            markHintWhenSendingFailed(envelopeSender.hint, true);
            logger.log(SentryLevel.WARNING, "Envelope rejected", new Object[0]);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.executor.shutdown();
        this.options.getLogger().log(SentryLevel.DEBUG, "Shutting down", new Object[0]);
        try {
            if (!this.executor.awaitTermination(1L, TimeUnit.MINUTES)) {
                this.options.getLogger().log(SentryLevel.WARNING, "Failed to shutdown the async connection async sender within 1 minute. Trying to force it now.", new Object[0]);
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.options.getLogger().log(SentryLevel.DEBUG, "Thread interrupted while closing the connection.", new Object[0]);
            Thread.currentThread().interrupt();
        }
    }

    private static void markHintWhenSendingFailed(@Nullable Object hint, boolean retry) {
        if (hint instanceof SubmissionResult) {
            ((SubmissionResult) hint).setResult(false);
        }
        if (hint instanceof Retryable) {
            ((Retryable) hint).setRetry(retry);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class AsyncConnectionThreadFactory implements ThreadFactory {
        private int cnt;

        private AsyncConnectionThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        @NotNull
        public Thread newThread(@NotNull Runnable r) {
            StringBuilder sb = new StringBuilder();
            sb.append("SentryAsyncConnection-");
            int i = this.cnt;
            this.cnt = i + 1;
            sb.append(i);
            Thread ret = new Thread(r, sb.toString());
            ret.setDaemon(true);
            return ret;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class EnvelopeSender implements Runnable {
        @NotNull
        private final SentryEnvelope envelope;
        @NotNull
        private final IEnvelopeCache envelopeCache;
        private final TransportResult failedResult = TransportResult.error();
        @Nullable
        private final Object hint;

        EnvelopeSender(@NotNull SentryEnvelope envelope, @Nullable Object hint, @NotNull IEnvelopeCache envelopeCache) {
            this.envelope = (SentryEnvelope) Objects.requireNonNull(envelope, "Envelope is required.");
            this.hint = hint;
            this.envelopeCache = (IEnvelopeCache) Objects.requireNonNull(envelopeCache, "EnvelopeCache is required.");
        }

        @Override // java.lang.Runnable
        public void run() {
            TransportResult result = this.failedResult;
            try {
                result = flush();
                AsyncHttpTransport.this.options.getLogger().log(SentryLevel.DEBUG, "Envelope flushed", new Object[0]);
                if (this.hint instanceof SubmissionResult) {
                    AsyncHttpTransport.this.options.getLogger().log(SentryLevel.DEBUG, "Marking envelope submission result: %s", Boolean.valueOf(result.isSuccess()));
                    ((SubmissionResult) this.hint).setResult(result.isSuccess());
                }
            } catch (Throwable e) {
                try {
                    AsyncHttpTransport.this.options.getLogger().log(SentryLevel.ERROR, e, "Envelope submission failed", new Object[0]);
                    throw e;
                } catch (Throwable e2) {
                    if (this.hint instanceof SubmissionResult) {
                        AsyncHttpTransport.this.options.getLogger().log(SentryLevel.DEBUG, "Marking envelope submission result: %s", Boolean.valueOf(result.isSuccess()));
                        ((SubmissionResult) this.hint).setResult(result.isSuccess());
                    }
                    throw e2;
                }
            }
        }

        @NotNull
        private TransportResult flush() {
            TransportResult result = this.failedResult;
            this.envelopeCache.store(this.envelope, this.hint);
            Object obj = this.hint;
            if (obj instanceof DiskFlushNotification) {
                ((DiskFlushNotification) obj).markFlushed();
                AsyncHttpTransport.this.options.getLogger().log(SentryLevel.DEBUG, "Disk flush envelope fired", new Object[0]);
            }
            if (AsyncHttpTransport.this.transportGate.isConnected()) {
                try {
                    result = AsyncHttpTransport.this.connection.send(this.envelope);
                    if (result.isSuccess()) {
                        this.envelopeCache.discard(this.envelope);
                    } else {
                        String message = "The transport failed to send the envelope with response code " + result.getResponseCode();
                        AsyncHttpTransport.this.options.getLogger().log(SentryLevel.ERROR, message, new Object[0]);
                        throw new IllegalStateException(message);
                    }
                } catch (IOException e) {
                    Object obj2 = this.hint;
                    if (!(obj2 instanceof Retryable)) {
                        LogUtils.logIfNotRetryable(AsyncHttpTransport.this.options.getLogger(), this.hint);
                    } else {
                        ((Retryable) obj2).setRetry(true);
                    }
                    throw new IllegalStateException("Sending the event failed.", e);
                }
            } else {
                Object obj3 = this.hint;
                if (!(obj3 instanceof Retryable)) {
                    LogUtils.logIfNotRetryable(AsyncHttpTransport.this.options.getLogger(), this.hint);
                } else {
                    ((Retryable) obj3).setRetry(true);
                }
            }
            return result;
        }
    }
}
