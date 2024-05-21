package io.sentry;

import io.sentry.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class SpanId {
    public static final SpanId EMPTY_ID = new SpanId(new UUID(0, 0).toString());
    @NotNull
    private final String value;

    public SpanId(@NotNull String value) {
        this.value = (String) Objects.requireNonNull(value, "value is required");
    }

    public SpanId() {
        this(UUID.randomUUID());
    }

    private SpanId(@NotNull UUID uuid) {
        this(uuid.toString().replace("-", "").substring(0, 16));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpanId spanId = (SpanId) o;
        return this.value.equals(spanId.value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return this.value;
    }
}
