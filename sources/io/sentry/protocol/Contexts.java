package io.sentry.protocol;

import io.sentry.SpanContext;
import io.sentry.util.Objects;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Contexts extends ConcurrentHashMap<String, Object> {
    private static final long serialVersionUID = 252445813254943011L;

    public Contexts() {
    }

    public Contexts(@NotNull Contexts contexts) {
        for (Map.Entry<String, Object> entry : contexts.entrySet()) {
            if (entry != null) {
                Object value = entry.getValue();
                if (App.TYPE.equals(entry.getKey()) && (value instanceof App)) {
                    setApp(new App((App) value));
                } else if (Browser.TYPE.equals(entry.getKey()) && (value instanceof Browser)) {
                    setBrowser(new Browser((Browser) value));
                } else if ("device".equals(entry.getKey()) && (value instanceof Device)) {
                    setDevice(new Device((Device) value));
                } else if (OperatingSystem.TYPE.equals(entry.getKey()) && (value instanceof OperatingSystem)) {
                    setOperatingSystem(new OperatingSystem((OperatingSystem) value));
                } else if (SentryRuntime.TYPE.equals(entry.getKey()) && (value instanceof SentryRuntime)) {
                    setRuntime(new SentryRuntime((SentryRuntime) value));
                } else if (Gpu.TYPE.equals(entry.getKey()) && (value instanceof Gpu)) {
                    setGpu(new Gpu((Gpu) value));
                } else if (SpanContext.TYPE.equals(entry.getKey()) && (value instanceof SpanContext)) {
                    setTrace(new SpanContext((SpanContext) value));
                } else {
                    put(entry.getKey(), value);
                }
            }
        }
    }

    @Nullable
    private <T> T toContextType(@NotNull String key, @NotNull Class<T> clazz) {
        Object item = get(key);
        if (clazz.isInstance(item)) {
            return clazz.cast(item);
        }
        return null;
    }

    @Nullable
    public SpanContext getTrace() {
        return (SpanContext) toContextType(SpanContext.TYPE, SpanContext.class);
    }

    public void setTrace(@Nullable SpanContext traceContext) {
        Objects.requireNonNull(traceContext, "traceContext is required");
        put(SpanContext.TYPE, traceContext);
    }

    @Nullable
    public App getApp() {
        return (App) toContextType(App.TYPE, App.class);
    }

    public void setApp(@NotNull App app) {
        put(App.TYPE, app);
    }

    @Nullable
    public Browser getBrowser() {
        return (Browser) toContextType(Browser.TYPE, Browser.class);
    }

    public void setBrowser(@NotNull Browser browser) {
        put(Browser.TYPE, browser);
    }

    @Nullable
    public Device getDevice() {
        return (Device) toContextType("device", Device.class);
    }

    public void setDevice(@NotNull Device device) {
        put("device", device);
    }

    @Nullable
    public OperatingSystem getOperatingSystem() {
        return (OperatingSystem) toContextType(OperatingSystem.TYPE, OperatingSystem.class);
    }

    public void setOperatingSystem(@NotNull OperatingSystem operatingSystem) {
        put(OperatingSystem.TYPE, operatingSystem);
    }

    @Nullable
    public SentryRuntime getRuntime() {
        return (SentryRuntime) toContextType(SentryRuntime.TYPE, SentryRuntime.class);
    }

    public void setRuntime(@NotNull SentryRuntime runtime) {
        put(SentryRuntime.TYPE, runtime);
    }

    @Nullable
    public Gpu getGpu() {
        return (Gpu) toContextType(Gpu.TYPE, Gpu.class);
    }

    public void setGpu(@NotNull Gpu gpu) {
        put(Gpu.TYPE, gpu);
    }
}
