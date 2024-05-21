package io.sentry;

import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class DuplicateEventDetectionEventProcessor implements EventProcessor {
    @NotNull
    private final Map<Throwable, Object> capturedObjects = Collections.synchronizedMap(new WeakHashMap());
    @NotNull
    private final SentryOptions options;

    public DuplicateEventDetectionEventProcessor(@NotNull SentryOptions options) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "options are required");
    }

    @Override // io.sentry.EventProcessor
    @Nullable
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        if (this.options.isEnableDeduplication()) {
            Throwable throwable = event.getThrowable();
            if (throwable != null) {
                if (this.capturedObjects.containsKey(throwable) || containsAnyKey(this.capturedObjects, allCauses(throwable))) {
                    this.options.getLogger().log(SentryLevel.DEBUG, "Duplicate Exception detected. Event %s will be discarded.", event.getEventId());
                    return null;
                }
                this.capturedObjects.put(throwable, null);
            }
        } else {
            this.options.getLogger().log(SentryLevel.DEBUG, "Event deduplication is disabled.", new Object[0]);
        }
        return event;
    }

    private static <T> boolean containsAnyKey(@NotNull Map<T, Object> map, @NotNull List<T> list) {
        for (T entry : list) {
            if (map.containsKey(entry)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private static List<Throwable> allCauses(@NotNull Throwable throwable) {
        List<Throwable> causes = new ArrayList<>();
        for (Throwable ex = throwable; ex.getCause() != null; ex = ex.getCause()) {
            causes.add(ex.getCause());
        }
        return causes;
    }
}
