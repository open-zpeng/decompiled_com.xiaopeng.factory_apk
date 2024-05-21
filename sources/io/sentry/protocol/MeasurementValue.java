package io.sentry.protocol;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.TestOnly;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class MeasurementValue {
    private final float value;

    public MeasurementValue(float value) {
        this.value = value;
    }

    @TestOnly
    public float getValue() {
        return this.value;
    }
}
