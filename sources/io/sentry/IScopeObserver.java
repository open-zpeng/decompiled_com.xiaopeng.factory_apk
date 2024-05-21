package io.sentry;

import io.sentry.protocol.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface IScopeObserver {
    void addBreadcrumb(@NotNull Breadcrumb breadcrumb);

    void removeExtra(@NotNull String str);

    void removeTag(@NotNull String str);

    void setExtra(@NotNull String str, @NotNull String str2);

    void setTag(@NotNull String str, @NotNull String str2);

    void setUser(@Nullable User user);
}
