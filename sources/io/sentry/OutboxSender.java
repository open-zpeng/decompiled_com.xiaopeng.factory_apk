package io.sentry;

import io.sentry.cache.EnvelopeCache;
import io.sentry.hints.Flushable;
import io.sentry.hints.Retryable;
import io.sentry.protocol.SentryId;
import io.sentry.util.LogUtils;
import io.sentry.util.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class OutboxSender extends DirectoryProcessor implements IEnvelopeSender {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    @NotNull
    private final IEnvelopeReader envelopeReader;
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

    public OutboxSender(@NotNull IHub hub, @NotNull IEnvelopeReader envelopeReader, @NotNull ISerializer serializer, @NotNull ILogger logger, long flushTimeoutMillis) {
        super(logger, flushTimeoutMillis);
        this.hub = (IHub) Objects.requireNonNull(hub, "Hub is required.");
        this.envelopeReader = (IEnvelopeReader) Objects.requireNonNull(envelopeReader, "Envelope reader is required.");
        this.serializer = (ISerializer) Objects.requireNonNull(serializer, "Serializer is required.");
        this.logger = (ILogger) Objects.requireNonNull(logger, "Logger is required.");
    }

    @Override // io.sentry.DirectoryProcessor
    protected void processFile(@NotNull File file, @Nullable Object hint) {
        ILogger iLogger;
        SentryLevel sentryLevel;
        Object[] objArr;
        Objects.requireNonNull(file, "File is required.");
        try {
            if (!isRelevantFileName(file.getName())) {
                this.logger.log(SentryLevel.DEBUG, "File '%s' should be ignored.", file.getAbsolutePath());
                return;
            }
            try {
                InputStream stream = new BufferedInputStream(new FileInputStream(file));
                try {
                    SentryEnvelope envelope = this.envelopeReader.read(stream);
                    if (envelope == null) {
                        this.logger.log(SentryLevel.ERROR, "Stream from path %s resulted in a null envelope.", file.getAbsolutePath());
                    } else {
                        processEnvelope(envelope, hint);
                        this.logger.log(SentryLevel.DEBUG, "File '%s' is done.", file.getAbsolutePath());
                    }
                    stream.close();
                    if (hint instanceof Retryable) {
                        if (((Retryable) hint).isRetry()) {
                            return;
                        }
                        try {
                            if (file.delete()) {
                                return;
                            }
                            this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                            return;
                        } catch (RuntimeException e) {
                            e = e;
                            iLogger = this.logger;
                            sentryLevel = SentryLevel.ERROR;
                            objArr = new Object[]{file.getAbsolutePath()};
                            iLogger.log(sentryLevel, e, "Failed to delete: %s", objArr);
                            return;
                        }
                    }
                } catch (Throwable th) {
                    try {
                        stream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e2) {
                this.logger.log(SentryLevel.ERROR, "Error processing envelope.", e2);
                if (hint instanceof Retryable) {
                    if (((Retryable) hint).isRetry()) {
                        return;
                    }
                    try {
                        if (file.delete()) {
                            return;
                        }
                        this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                        return;
                    } catch (RuntimeException e3) {
                        e = e3;
                        iLogger = this.logger;
                        sentryLevel = SentryLevel.ERROR;
                        objArr = new Object[]{file.getAbsolutePath()};
                        iLogger.log(sentryLevel, e, "Failed to delete: %s", objArr);
                        return;
                    }
                }
            }
            LogUtils.logIfNotRetryable(this.logger, hint);
        } catch (Throwable th3) {
            if (!(hint instanceof Retryable)) {
                LogUtils.logIfNotRetryable(this.logger, hint);
            } else if (!((Retryable) hint).isRetry()) {
                try {
                    if (!file.delete()) {
                        this.logger.log(SentryLevel.ERROR, "Failed to delete: %s", file.getAbsolutePath());
                    }
                } catch (RuntimeException e4) {
                    this.logger.log(SentryLevel.ERROR, e4, "Failed to delete: %s", file.getAbsolutePath());
                }
            }
            throw th3;
        }
    }

    @Override // io.sentry.DirectoryProcessor
    protected boolean isRelevantFileName(@Nullable String fileName) {
        return (fileName == null || fileName.startsWith(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE)) ? false : true;
    }

    @Override // io.sentry.IEnvelopeSender
    public void processEnvelopeFile(@NotNull String path, @Nullable Object hint) {
        Objects.requireNonNull(path, "Path is required.");
        processFile(new File(path), hint);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x0024 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0207 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void processEnvelope(@org.jetbrains.annotations.NotNull io.sentry.SentryEnvelope r11, @org.jetbrains.annotations.Nullable java.lang.Object r12) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 528
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.OutboxSender.processEnvelope(io.sentry.SentryEnvelope, java.lang.Object):void");
    }

    private void logEnvelopeItemNull(@NotNull SentryEnvelopeItem item, int itemIndex) {
        this.logger.log(SentryLevel.ERROR, "Item %d of type %s returned null by the parser.", Integer.valueOf(itemIndex), item.getHeader().getType());
    }

    private void logUnexpectedEventId(@NotNull SentryEnvelope envelope, @Nullable SentryId eventId, int itemIndex) {
        this.logger.log(SentryLevel.ERROR, "Item %d of has a different event id (%s) to the envelope header (%s)", Integer.valueOf(itemIndex), envelope.getHeader().getEventId(), eventId);
    }

    private void logItemCaptured(int itemIndex) {
        this.logger.log(SentryLevel.DEBUG, "Item %d is being captured.", Integer.valueOf(itemIndex));
    }

    private void logTimeout(@Nullable SentryId eventId) {
        this.logger.log(SentryLevel.WARNING, "Timed out waiting for event id submission: %s", eventId);
    }

    private boolean waitFlush(@Nullable Object hint) {
        if (hint instanceof Flushable) {
            return ((Flushable) hint).waitFlush();
        }
        LogUtils.logIfNotFlushable(this.logger, hint);
        return true;
    }
}
