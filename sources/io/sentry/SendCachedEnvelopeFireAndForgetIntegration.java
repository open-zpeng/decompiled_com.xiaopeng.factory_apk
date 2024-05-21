package io.sentry;

import io.sentry.SendCachedEnvelopeFireAndForgetIntegration;
import io.sentry.util.Objects;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SendCachedEnvelopeFireAndForgetIntegration implements Integration {
    @NotNull
    private final SendFireAndForgetFactory factory;

    /* loaded from: classes2.dex */
    public interface SendFireAndForget {
        void send();
    }

    /* loaded from: classes2.dex */
    public interface SendFireAndForgetDirPath {
        @Nullable
        String getDirPath();
    }

    /* loaded from: classes2.dex */
    public interface SendFireAndForgetFactory {
        @Nullable
        SendFireAndForget create(@NotNull IHub iHub, @NotNull SentryOptions sentryOptions);

        default boolean hasValidPath(@Nullable String dirPath, @NotNull ILogger logger) {
            if (dirPath == null || dirPath.isEmpty()) {
                logger.log(SentryLevel.INFO, "No cached dir path is defined in options.", new Object[0]);
                return false;
            }
            return true;
        }

        @NotNull
        default SendFireAndForget processDir(@NotNull final DirectoryProcessor directoryProcessor, @NotNull final String dirPath, @NotNull final ILogger logger) {
            final File dirFile = new File(dirPath);
            return new SendFireAndForget() { // from class: io.sentry.-$$Lambda$SendCachedEnvelopeFireAndForgetIntegration$SendFireAndForgetFactory$fw_M6vxYehvhSeLyExDmpVukEZU
                @Override // io.sentry.SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForget
                public final void send() {
                    SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetFactory.lambda$processDir$0(ILogger.this, dirPath, directoryProcessor, dirFile);
                }
            };
        }

        static /* synthetic */ void lambda$processDir$0(ILogger logger, String dirPath, DirectoryProcessor directoryProcessor, File dirFile) {
            logger.log(SentryLevel.DEBUG, "Started processing cached files from %s", dirPath);
            directoryProcessor.processDirectory(dirFile);
            logger.log(SentryLevel.DEBUG, "Finished processing cached files from %s", dirPath);
        }
    }

    public SendCachedEnvelopeFireAndForgetIntegration(@NotNull SendFireAndForgetFactory factory) {
        this.factory = (SendFireAndForgetFactory) Objects.requireNonNull(factory, "SendFireAndForgetFactory is required");
    }

    @Override // io.sentry.Integration
    public final void register(@NotNull IHub hub, @NotNull final SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        Objects.requireNonNull(options, "SentryOptions is required");
        String cachedDir = options.getCacheDirPath();
        if (!this.factory.hasValidPath(cachedDir, options.getLogger())) {
            options.getLogger().log(SentryLevel.ERROR, "No cache dir path is defined in options.", new Object[0]);
            return;
        }
        final SendFireAndForget sender = this.factory.create(hub, options);
        if (sender == null) {
            options.getLogger().log(SentryLevel.ERROR, "SendFireAndForget factory is null.", new Object[0]);
            return;
        }
        try {
            options.getExecutorService().submit(new Runnable() { // from class: io.sentry.-$$Lambda$SendCachedEnvelopeFireAndForgetIntegration$-yc-vZFUWWVM7GTAiUeYZz2iHz4
                @Override // java.lang.Runnable
                public final void run() {
                    SendCachedEnvelopeFireAndForgetIntegration.lambda$register$0(SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForget.this, options);
                }
            });
            options.getLogger().log(SentryLevel.DEBUG, "SendCachedEventFireAndForgetIntegration installed.", new Object[0]);
        } catch (Throwable e) {
            options.getLogger().log(SentryLevel.ERROR, "Failed to call the executor. Cached events will not be sent", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$register$0(SendFireAndForget sender, SentryOptions options) {
        try {
            sender.send();
        } catch (Throwable e) {
            options.getLogger().log(SentryLevel.ERROR, "Failed trying to send cached events.", e);
        }
    }
}
