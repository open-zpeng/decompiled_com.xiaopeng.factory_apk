package io.sentry.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class ExceptionUtils {
    @NotNull
    public static Throwable findRootCause(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable, "throwable cannot be null");
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
