package io.sentry;

import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelopeHeader {
    @Nullable
    private final SentryId eventId;
    @Nullable
    private final SdkVersion sdkVersion;
    @Nullable
    private final TraceState trace;

    public SentryEnvelopeHeader(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion) {
        this(eventId, sdkVersion, null);
    }

    public SentryEnvelopeHeader(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion, @Nullable TraceState trace) {
        this.eventId = eventId;
        this.sdkVersion = sdkVersion;
        this.trace = trace;
    }

    public SentryEnvelopeHeader(@Nullable SentryId eventId) {
        this(eventId, null);
    }

    public SentryEnvelopeHeader() {
        this(new SentryId());
    }

    @Nullable
    public SentryId getEventId() {
        return this.eventId;
    }

    @Nullable
    public SdkVersion getSdkVersion() {
        return this.sdkVersion;
    }

    @Nullable
    public TraceState getTrace() {
        return this.trace;
    }
}
