package io.sentry;

import io.sentry.util.Objects;
import java.security.SecureRandom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class TracesSampler {
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final SecureRandom random;

    public TracesSampler(@NotNull SentryOptions options) {
        this((SentryOptions) Objects.requireNonNull(options, "options are required"), new SecureRandom());
    }

    @TestOnly
    TracesSampler(@NotNull SentryOptions options, @NotNull SecureRandom random) {
        this.options = options;
        this.random = random;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean sample(@NotNull SamplingContext samplingContext) {
        Double samplerResult;
        if (samplingContext.getTransactionContext().getSampled() != null) {
            return samplingContext.getTransactionContext().getSampled().booleanValue();
        }
        if (this.options.getTracesSampler() != null && (samplerResult = this.options.getTracesSampler().sample(samplingContext)) != null) {
            return sample(samplerResult);
        }
        if (samplingContext.getTransactionContext().getParentSampled() != null) {
            return samplingContext.getTransactionContext().getParentSampled().booleanValue();
        }
        if (this.options.getTracesSampleRate() != null) {
            return sample(this.options.getTracesSampleRate());
        }
        return false;
    }

    private boolean sample(@NotNull Double aDouble) {
        return aDouble.doubleValue() >= this.random.nextDouble();
    }
}
