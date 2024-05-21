package io.sentry;

import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public interface IUnknownPropertiesConsumer {
    void acceptUnknownProperties(@NotNull Map<String, Object> map);
}
