package io.sentry.android.ndk;

import io.sentry.Breadcrumb;
import io.sentry.DateUtils;
import io.sentry.IScopeObserver;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.protocol.User;
import io.sentry.util.Objects;
import java.util.Locale;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class NdkScopeObserver implements IScopeObserver {
    @NotNull
    private final INativeScope nativeScope;
    @NotNull
    private final SentryOptions options;

    public NdkScopeObserver(@NotNull SentryOptions options) {
        this(options, new NativeScope());
    }

    NdkScopeObserver(@NotNull SentryOptions options, @NotNull INativeScope nativeScope) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryOptions object is required.");
        this.nativeScope = (INativeScope) Objects.requireNonNull(nativeScope, "The NativeScope object is required.");
    }

    @Override // io.sentry.IScopeObserver
    public void setUser(@Nullable User user) {
        try {
            if (user == null) {
                this.nativeScope.removeUser();
            } else {
                this.nativeScope.setUser(user.getId(), user.getEmail(), user.getIpAddress(), user.getUsername());
            }
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync setUser has an error.", new Object[0]);
        }
    }

    @Override // io.sentry.IScopeObserver
    public void addBreadcrumb(@NotNull Breadcrumb crumb) {
        String level = null;
        try {
            if (crumb.getLevel() != null) {
                level = crumb.getLevel().name().toLowerCase(Locale.ROOT);
            }
            String timestamp = DateUtils.getTimestamp(crumb.getTimestamp());
            String data = null;
            Map<String, Object> dataRef = crumb.getData();
            if (!dataRef.isEmpty()) {
                data = this.options.getSerializer().serialize(dataRef);
            }
            this.nativeScope.addBreadcrumb(level, crumb.getMessage(), crumb.getCategory(), crumb.getType(), timestamp, data);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync addBreadcrumb has an error.", new Object[0]);
        }
    }

    @Override // io.sentry.IScopeObserver
    public void setTag(@NotNull String key, @NotNull String value) {
        try {
            this.nativeScope.setTag(key, value);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync setTag(%s) has an error.", key);
        }
    }

    @Override // io.sentry.IScopeObserver
    public void removeTag(@NotNull String key) {
        try {
            this.nativeScope.removeTag(key);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync removeTag(%s) has an error.", key);
        }
    }

    @Override // io.sentry.IScopeObserver
    public void setExtra(@NotNull String key, @NotNull String value) {
        try {
            this.nativeScope.setExtra(key, value);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync setExtra(%s) has an error.", key);
        }
    }

    @Override // io.sentry.IScopeObserver
    public void removeExtra(@NotNull String key) {
        try {
            this.nativeScope.removeExtra(key);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, e, "Scope sync removeExtra(%s) has an error.", key);
        }
    }
}
