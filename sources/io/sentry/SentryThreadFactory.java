package io.sentry;

import io.sentry.protocol.SentryStackFrame;
import io.sentry.protocol.SentryStackTrace;
import io.sentry.protocol.SentryThread;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SentryThreadFactory {
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final SentryStackTraceFactory sentryStackTraceFactory;

    public SentryThreadFactory(@NotNull SentryStackTraceFactory sentryStackTraceFactory, @NotNull SentryOptions options) {
        this.sentryStackTraceFactory = (SentryStackTraceFactory) Objects.requireNonNull(sentryStackTraceFactory, "The SentryStackTraceFactory is required.");
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryOptions is required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public List<SentryThread> getCurrentThread() {
        Map<Thread, StackTraceElement[]> threads = new HashMap<>();
        Thread currentThread = Thread.currentThread();
        threads.put(currentThread, currentThread.getStackTrace());
        return getCurrentThreads(threads, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public List<SentryThread> getCurrentThreads(@Nullable List<Long> mechanismThreadIds) {
        return getCurrentThreads(Thread.getAllStackTraces(), mechanismThreadIds);
    }

    @TestOnly
    @Nullable
    List<SentryThread> getCurrentThreads(@NotNull Map<Thread, StackTraceElement[]> threads, @Nullable List<Long> mechanismThreadIds) {
        List<SentryThread> result = null;
        Thread currentThread = Thread.currentThread();
        if (!threads.isEmpty()) {
            result = new ArrayList<>();
            if (!threads.containsKey(currentThread)) {
                threads.put(currentThread, currentThread.getStackTrace());
            }
            for (Map.Entry<Thread, StackTraceElement[]> item : threads.entrySet()) {
                Thread thread = item.getKey();
                boolean crashed = thread == currentThread || (mechanismThreadIds != null && mechanismThreadIds.contains(Long.valueOf(thread.getId())));
                result.add(getSentryThread(crashed, item.getValue(), item.getKey()));
            }
        }
        return result;
    }

    @NotNull
    private SentryThread getSentryThread(boolean crashed, @NotNull StackTraceElement[] stackFramesElements, @NotNull Thread thread) {
        SentryThread sentryThread = new SentryThread();
        sentryThread.setName(thread.getName());
        sentryThread.setPriority(Integer.valueOf(thread.getPriority()));
        sentryThread.setId(Long.valueOf(thread.getId()));
        sentryThread.setDaemon(Boolean.valueOf(thread.isDaemon()));
        sentryThread.setState(thread.getState().name());
        sentryThread.setCrashed(Boolean.valueOf(crashed));
        List<SentryStackFrame> frames = this.sentryStackTraceFactory.getStackFrames(stackFramesElements);
        if (this.options.isAttachStacktrace() && frames != null && !frames.isEmpty()) {
            SentryStackTrace sentryStackTrace = new SentryStackTrace(frames);
            sentryStackTrace.setSnapshot(true);
            sentryThread.setStacktrace(sentryStackTrace);
        }
        return sentryThread;
    }
}
