package io.sentry;

import io.sentry.protocol.SdkVersion;
import io.sentry.protocol.SentryId;
import io.sentry.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelope {
    @NotNull
    private final SentryEnvelopeHeader header;
    @NotNull
    private final Iterable<SentryEnvelopeItem> items;

    @NotNull
    public Iterable<SentryEnvelopeItem> getItems() {
        return this.items;
    }

    @NotNull
    public SentryEnvelopeHeader getHeader() {
        return this.header;
    }

    public SentryEnvelope(@NotNull SentryEnvelopeHeader header, @NotNull Iterable<SentryEnvelopeItem> items) {
        this.header = (SentryEnvelopeHeader) Objects.requireNonNull(header, "SentryEnvelopeHeader is required.");
        this.items = (Iterable) Objects.requireNonNull(items, "SentryEnvelope items are required.");
    }

    public SentryEnvelope(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion, @NotNull Iterable<SentryEnvelopeItem> items) {
        this.header = new SentryEnvelopeHeader(eventId, sdkVersion);
        this.items = (Iterable) Objects.requireNonNull(items, "SentryEnvelope items are required.");
    }

    public SentryEnvelope(@Nullable SentryId eventId, @Nullable SdkVersion sdkVersion, @NotNull SentryEnvelopeItem item) {
        Objects.requireNonNull(item, "SentryEnvelopeItem is required.");
        this.header = new SentryEnvelopeHeader(eventId, sdkVersion);
        List<SentryEnvelopeItem> items = new ArrayList<>(1);
        items.add(item);
        this.items = items;
    }

    @NotNull
    public static SentryEnvelope from(@NotNull ISerializer serializer, @NotNull Session session, @Nullable SdkVersion sdkVersion) throws IOException {
        Objects.requireNonNull(serializer, "Serializer is required.");
        Objects.requireNonNull(session, "session is required.");
        return new SentryEnvelope((SentryId) null, sdkVersion, SentryEnvelopeItem.fromSession(serializer, session));
    }

    @NotNull
    public static SentryEnvelope from(@NotNull ISerializer serializer, @NotNull SentryBaseEvent event, @Nullable SdkVersion sdkVersion) throws IOException {
        Objects.requireNonNull(serializer, "Serializer is required.");
        Objects.requireNonNull(event, "item is required.");
        return new SentryEnvelope(event.getEventId(), sdkVersion, SentryEnvelopeItem.fromEvent(serializer, event));
    }
}
