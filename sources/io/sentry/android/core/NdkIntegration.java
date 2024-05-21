package io.sentry.android.core;

import com.lzy.okgo.model.HttpHeaders;
import io.sentry.IHub;
import io.sentry.Integration;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
public final class NdkIntegration implements Integration, Closeable {
    public static final String SENTRY_NDK_CLASS_NAME = "io.sentry.android.ndk.SentryNdk";
    @Nullable
    private SentryAndroidOptions options;
    @Nullable
    private final Class<?> sentryNdkClass;

    public NdkIntegration(@Nullable Class<?> sentryNdkClass) {
        this.sentryNdkClass = sentryNdkClass;
    }

    @Override // io.sentry.Integration
    public final void register(@NotNull IHub hub, @NotNull SentryOptions options) {
        Objects.requireNonNull(hub, "Hub is required");
        this.options = (SentryAndroidOptions) Objects.requireNonNull(options instanceof SentryAndroidOptions ? (SentryAndroidOptions) options : null, "SentryAndroidOptions is required");
        boolean enabled = this.options.isEnableNdk();
        this.options.getLogger().log(SentryLevel.DEBUG, "NdkIntegration enabled: %s", Boolean.valueOf(enabled));
        if (enabled && this.sentryNdkClass != null) {
            String cachedDir = this.options.getCacheDirPath();
            if (cachedDir == null || cachedDir.isEmpty()) {
                this.options.getLogger().log(SentryLevel.ERROR, "No cache dir path is defined in options.", new Object[0]);
                this.options.setEnableNdk(false);
                return;
            }
            try {
                Method method = this.sentryNdkClass.getMethod("init", SentryAndroidOptions.class);
                Object[] args = {this.options};
                method.invoke(null, args);
                this.options.getLogger().log(SentryLevel.DEBUG, "NdkIntegration installed.", new Object[0]);
                return;
            } catch (NoSuchMethodException e) {
                this.options.setEnableNdk(false);
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to invoke the SentryNdk.init method.", e);
                return;
            } catch (Throwable e2) {
                this.options.setEnableNdk(false);
                this.options.getLogger().log(SentryLevel.ERROR, "Failed to initialize SentryNdk.", e2);
                return;
            }
        }
        this.options.setEnableNdk(false);
    }

    @TestOnly
    @Nullable
    Class<?> getSentryNdkClass() {
        return this.sentryNdkClass;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Class<?> cls;
        SentryAndroidOptions sentryAndroidOptions = this.options;
        if (sentryAndroidOptions != null && sentryAndroidOptions.isEnableNdk() && (cls = this.sentryNdkClass) != null) {
            try {
                try {
                    Method method = cls.getMethod(HttpHeaders.HEAD_VALUE_CONNECTION_CLOSE, new Class[0]);
                    method.invoke(null, new Object[0]);
                    this.options.getLogger().log(SentryLevel.DEBUG, "NdkIntegration removed.", new Object[0]);
                } catch (NoSuchMethodException e) {
                    this.options.getLogger().log(SentryLevel.ERROR, "Failed to invoke the SentryNdk.close method.", e);
                }
                this.options.setEnableNdk(false);
            }
        }
    }
}
