package io.sentry;

import io.sentry.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SamplingContext {
    @Nullable
    private final CustomSamplingContext customSamplingContext;
    @NotNull
    private final TransactionContext transactionContext;

    public SamplingContext(@NotNull TransactionContext transactionContext, @Nullable CustomSamplingContext customSamplingContext) {
        this.transactionContext = (TransactionContext) Objects.requireNonNull(transactionContext, "transactionContexts is required");
        this.customSamplingContext = customSamplingContext;
    }

    @Nullable
    public CustomSamplingContext getCustomSamplingContext() {
        return this.customSamplingContext;
    }

    @NotNull
    public TransactionContext getTransactionContext() {
        return this.transactionContext;
    }
}
