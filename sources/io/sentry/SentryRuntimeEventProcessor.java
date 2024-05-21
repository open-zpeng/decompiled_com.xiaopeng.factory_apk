package io.sentry;

import io.sentry.protocol.SentryRuntime;
import io.sentry.protocol.SentryTransaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SentryRuntimeEventProcessor implements EventProcessor {
    @Nullable
    private final String javaVendor;
    @Nullable
    private final String javaVersion;

    public SentryRuntimeEventProcessor(@Nullable String javaVersion, @Nullable String javaVendor) {
        this.javaVersion = javaVersion;
        this.javaVendor = javaVendor;
    }

    public SentryRuntimeEventProcessor() {
        this(System.getProperty("java.version"), System.getProperty("java.vendor"));
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        return (SentryEvent) process(event);
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryTransaction process(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        return (SentryTransaction) process(transaction);
    }

    @NotNull
    private <T extends SentryBaseEvent> T process(@NotNull T event) {
        if (event.getContexts().getRuntime() == null) {
            event.getContexts().setRuntime(new SentryRuntime());
        }
        SentryRuntime runtime = event.getContexts().getRuntime();
        if (runtime != null && runtime.getName() == null && runtime.getVersion() == null) {
            runtime.setName(this.javaVendor);
            runtime.setVersion(this.javaVersion);
        }
        return event;
    }
}
