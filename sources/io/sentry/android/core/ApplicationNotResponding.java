package io.sentry.android.core;

import io.sentry.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
final class ApplicationNotResponding extends RuntimeException {
    private static final long serialVersionUID = 252541144579117016L;
    @NotNull
    private final Thread thread;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ApplicationNotResponding(@Nullable String message, @NotNull Thread thread) {
        super(message);
        this.thread = (Thread) Objects.requireNonNull(thread, "Thread must be provided.");
        setStackTrace(this.thread.getStackTrace());
    }

    @NotNull
    public Thread getThread() {
        return this.thread;
    }
}
