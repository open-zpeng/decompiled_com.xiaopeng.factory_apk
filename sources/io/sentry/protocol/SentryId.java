package io.sentry.protocol;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryId {
    public static final SentryId EMPTY_ID = new SentryId(new UUID(0, 0));
    @NotNull
    private final UUID uuid;

    public SentryId() {
        this((UUID) null);
    }

    public SentryId(@Nullable UUID uuid) {
        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    }

    public SentryId(@NotNull String sentryIdString) {
        this.uuid = fromStringSentryId(sentryIdString);
    }

    public String toString() {
        return this.uuid.toString().replace("-", "");
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SentryId sentryId = (SentryId) o;
        if (this.uuid.compareTo(sentryId.uuid) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }

    @NotNull
    private UUID fromStringSentryId(@NotNull String sentryIdString) {
        if (sentryIdString.length() == 32) {
            sentryIdString = new StringBuilder(sentryIdString).insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString();
        }
        if (sentryIdString.length() != 36) {
            throw new IllegalArgumentException("String representation of SentryId has either 32 (UUID no dashes) or 36 characters long (completed UUID). Received: " + sentryIdString);
        }
        return UUID.fromString(sentryIdString);
    }
}
