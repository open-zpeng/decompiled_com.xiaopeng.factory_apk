package io.sentry;

import io.sentry.exception.ExceptionMechanismException;
import io.sentry.protocol.Mechanism;
import io.sentry.protocol.SentryException;
import io.sentry.protocol.SentryStackFrame;
import io.sentry.protocol.SentryStackTrace;
import io.sentry.util.Objects;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class SentryExceptionFactory {
    @NotNull
    private final SentryStackTraceFactory sentryStackTraceFactory;

    public SentryExceptionFactory(@NotNull SentryStackTraceFactory sentryStackTraceFactory) {
        this.sentryStackTraceFactory = (SentryStackTraceFactory) Objects.requireNonNull(sentryStackTraceFactory, "The SentryStackTraceFactory is required.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public List<SentryException> getSentryExceptions(@NotNull Throwable throwable) {
        return getSentryExceptions(extractExceptionQueue(throwable));
    }

    @NotNull
    private List<SentryException> getSentryExceptions(@NotNull Deque<SentryException> exceptions) {
        return new ArrayList(exceptions);
    }

    @NotNull
    private SentryException getSentryException(@NotNull Throwable throwable, @Nullable Mechanism exceptionMechanism, @Nullable Thread thread, boolean snapshot) {
        String exceptionClassName;
        Package exceptionPackage = throwable.getClass().getPackage();
        String fullClassName = throwable.getClass().getName();
        SentryException exception = new SentryException();
        String exceptionMessage = throwable.getMessage();
        if (exceptionPackage != null) {
            exceptionClassName = fullClassName.replace(exceptionPackage.getName() + ".", "");
        } else {
            exceptionClassName = fullClassName;
        }
        String exceptionPackageName = exceptionPackage != null ? exceptionPackage.getName() : null;
        List<SentryStackFrame> frames = this.sentryStackTraceFactory.getStackFrames(throwable.getStackTrace());
        if (frames != null && !frames.isEmpty()) {
            SentryStackTrace sentryStackTrace = new SentryStackTrace(frames);
            if (snapshot) {
                sentryStackTrace.setSnapshot(true);
            }
            exception.setStacktrace(sentryStackTrace);
        }
        if (thread != null) {
            exception.setThreadId(Long.valueOf(thread.getId()));
        }
        exception.setType(exceptionClassName);
        exception.setMechanism(exceptionMechanism);
        exception.setModule(exceptionPackageName);
        exception.setValue(exceptionMessage);
        return exception;
    }

    @TestOnly
    @NotNull
    Deque<SentryException> extractExceptionQueue(@NotNull Throwable throwable) {
        Mechanism exceptionMechanism;
        Thread thread;
        Deque<SentryException> exceptions = new ArrayDeque<>();
        Set<Throwable> circularityDetector = new HashSet<>();
        Throwable currentThrowable = throwable;
        while (currentThrowable != null && circularityDetector.add(currentThrowable)) {
            boolean snapshot = false;
            if (currentThrowable instanceof ExceptionMechanismException) {
                ExceptionMechanismException exceptionMechanismThrowable = (ExceptionMechanismException) currentThrowable;
                exceptionMechanism = exceptionMechanismThrowable.getExceptionMechanism();
                currentThrowable = exceptionMechanismThrowable.getThrowable();
                thread = exceptionMechanismThrowable.getThread();
                snapshot = exceptionMechanismThrowable.isSnapshot();
            } else {
                exceptionMechanism = null;
                thread = Thread.currentThread();
            }
            SentryException exception = getSentryException(currentThrowable, exceptionMechanism, thread, snapshot);
            exceptions.addFirst(exception);
            currentThrowable = currentThrowable.getCause();
        }
        return exceptions;
    }
}
