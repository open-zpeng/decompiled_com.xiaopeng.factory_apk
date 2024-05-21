package io.sentry.android.core.internal.util;

import io.sentry.protocol.Device;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class DeviceOrientations {
    private DeviceOrientations() {
    }

    @Nullable
    public static Device.DeviceOrientation getOrientation(int orientation) {
        if (orientation != 1) {
            if (orientation == 2) {
                return Device.DeviceOrientation.LANDSCAPE;
            }
            return null;
        }
        return Device.DeviceOrientation.PORTRAIT;
    }
}
