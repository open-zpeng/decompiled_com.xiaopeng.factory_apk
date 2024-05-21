package io.sentry.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.sentry.SentryLevel;
import io.sentry.SentryOptions;
import io.sentry.SpanContext;
import io.sentry.protocol.App;
import io.sentry.protocol.Browser;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Device;
import io.sentry.protocol.Gpu;
import io.sentry.protocol.OperatingSystem;
import io.sentry.protocol.SentryRuntime;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class ContextsDeserializerAdapter implements JsonDeserializer<Contexts> {
    @NotNull
    private final SentryOptions options;

    public ContextsDeserializerAdapter(@NotNull SentryOptions options) {
        this.options = options;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    @Nullable
    public Contexts deserialize(@Nullable JsonElement json, @NotNull Type typeOfT, @NotNull JsonDeserializationContext context) throws JsonParseException {
        if (json != null) {
            try {
                if (!json.isJsonNull()) {
                    Contexts contexts = new Contexts();
                    JsonObject jsonObject = json.getAsJsonObject();
                    if (jsonObject != null && !jsonObject.isJsonNull()) {
                        for (String key : jsonObject.keySet()) {
                            char c = 65535;
                            switch (key.hashCode()) {
                                case -1335157162:
                                    if (key.equals("device")) {
                                        c = 2;
                                        break;
                                    }
                                    break;
                                case 3556:
                                    if (key.equals(OperatingSystem.TYPE)) {
                                        c = 3;
                                        break;
                                    }
                                    break;
                                case 96801:
                                    if (key.equals(App.TYPE)) {
                                        c = 0;
                                        break;
                                    }
                                    break;
                                case 102572:
                                    if (key.equals(Gpu.TYPE)) {
                                        c = 5;
                                        break;
                                    }
                                    break;
                                case 110620997:
                                    if (key.equals(SpanContext.TYPE)) {
                                        c = 6;
                                        break;
                                    }
                                    break;
                                case 150940456:
                                    if (key.equals(Browser.TYPE)) {
                                        c = 1;
                                        break;
                                    }
                                    break;
                                case 1550962648:
                                    if (key.equals(SentryRuntime.TYPE)) {
                                        c = 4;
                                        break;
                                    }
                                    break;
                            }
                            switch (c) {
                                case 0:
                                    App app = (App) parseObject(context, jsonObject, key, App.class);
                                    if (app != null) {
                                        contexts.setApp(app);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 1:
                                    Browser browser = (Browser) parseObject(context, jsonObject, key, Browser.class);
                                    if (browser != null) {
                                        contexts.setBrowser(browser);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 2:
                                    Device device = (Device) parseObject(context, jsonObject, key, Device.class);
                                    if (device != null) {
                                        contexts.setDevice(device);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 3:
                                    OperatingSystem os = (OperatingSystem) parseObject(context, jsonObject, key, OperatingSystem.class);
                                    if (os != null) {
                                        contexts.setOperatingSystem(os);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 4:
                                    SentryRuntime runtime = (SentryRuntime) parseObject(context, jsonObject, key, SentryRuntime.class);
                                    if (runtime != null) {
                                        contexts.setRuntime(runtime);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 5:
                                    Gpu gpu = (Gpu) parseObject(context, jsonObject, key, Gpu.class);
                                    if (gpu != null) {
                                        contexts.setGpu(gpu);
                                        break;
                                    } else {
                                        break;
                                    }
                                case 6:
                                    SpanContext trace = (SpanContext) parseObject(context, jsonObject, key, SpanContext.class);
                                    if (trace != null) {
                                        contexts.setTrace(trace);
                                        break;
                                    } else {
                                        break;
                                    }
                                default:
                                    JsonElement element = jsonObject.get(key);
                                    if (element != null && !element.isJsonNull()) {
                                        try {
                                            Object object = context.deserialize(element, Object.class);
                                            contexts.put(key, object);
                                            break;
                                        } catch (JsonParseException e) {
                                            this.options.getLogger().log(SentryLevel.ERROR, e, "Error when deserializing the %s key.", key);
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                    return contexts;
                }
                return null;
            } catch (Throwable e2) {
                this.options.getLogger().log(SentryLevel.ERROR, "Error when deserializing Contexts", e2);
                return null;
            }
        }
        return null;
    }

    @Nullable
    private <T> T parseObject(@NotNull JsonDeserializationContext context, @NotNull JsonObject jsonObject, @NotNull String key, @NotNull Class<T> clazz) throws JsonParseException {
        JsonObject object = jsonObject.getAsJsonObject(key);
        if (object != null && !object.isJsonNull()) {
            return (T) context.deserialize(object, clazz);
        }
        return null;
    }
}
