package io.sentry;

import io.sentry.SendCachedEnvelopeFireAndForgetIntegration;
import io.sentry.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SendFireAndForgetOutboxSender implements SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetFactory {
    @NotNull
    private final SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath sendFireAndForgetDirPath;

    public SendFireAndForgetOutboxSender(@NotNull SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath sendFireAndForgetDirPath) {
        this.sendFireAndForgetDirPath = (SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetDirPath) Objects.requireNonNull(sendFireAndForgetDirPath, "SendFireAndForgetDirPath is required");
    }

    @Override // io.sentry.SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForgetFactory
    @Nullable
    public SendCachedEnvelopeFireAndForgetIntegration.SendFireAndForget create(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        Objects.requireNonNull(options, "SentryOptions is required");
        String dirPath = this.sendFireAndForgetDirPath.getDirPath();
        if (dirPath == null || !hasValidPath(dirPath, options.getLogger())) {
            options.getLogger().log(SentryLevel.ERROR, "No outbox dir path is defined in options.", new Object[0]);
            return null;
        }
        OutboxSender outboxSender = new OutboxSender(hub, options.getEnvelopeReader(), options.getSerializer(), options.getLogger(), options.getFlushTimeoutMillis());
        return processDir(outboxSender, dirPath, options.getLogger());
    }
}
