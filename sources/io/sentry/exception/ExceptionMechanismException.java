package io.sentry.exception;

import io.sentry.protocol.Mechanism;
import io.sentry.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class ExceptionMechanismException extends RuntimeException {
    private static final long serialVersionUID = 142345454265713915L;
    @NotNull
    private final Mechanism exceptionMechanism;
    private final boolean snapshot;
    @NotNull
    private final Thread thread;
    @NotNull
    private final Throwable throwable;

    public ExceptionMechanismException(@NotNull Mechanism mechanism, @NotNull Throwable throwable, @NotNull Thread thread, boolean snapshot) {
        this.exceptionMechanism = (Mechanism) Objects.requireNonNull(mechanism, "Mechanism is required.");
        this.throwable = (Throwable) Objects.requireNonNull(throwable, "Throwable is required.");
        this.thread = (Thread) Objects.requireNonNull(thread, "Thread is required.");
        this.snapshot = snapshot;
    }

    public ExceptionMechanismException(@NotNull Mechanism mechanism, @NotNull Throwable throwable, @NotNull Thread thread) {
        this(mechanism, throwable, thread, false);
    }

    @NotNull
    public Mechanism getExceptionMechanism() {
        return this.exceptionMechanism;
    }

    @NotNull
    public Throwable getThrowable() {
        return this.throwable;
    }

    @NotNull
    public Thread getThread() {
        return this.thread;
    }

    public boolean isSnapshot() {
        return this.snapshot;
    }
}
