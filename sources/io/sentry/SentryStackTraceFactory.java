package io.sentry;

import io.sentry.protocol.SentryStackFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class SentryStackTraceFactory {
    @Nullable
    private final List<String> inAppExcludes;
    @Nullable
    private final List<String> inAppIncludes;

    public SentryStackTraceFactory(@Nullable List<String> inAppExcludes, @Nullable List<String> inAppIncludes) {
        this.inAppExcludes = inAppExcludes;
        this.inAppIncludes = inAppIncludes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public List<SentryStackFrame> getStackFrames(@Nullable StackTraceElement[] elements) {
        List<SentryStackFrame> sentryStackFrames = null;
        if (elements != null && elements.length > 0) {
            sentryStackFrames = new ArrayList<>();
            for (StackTraceElement item : elements) {
                if (item != null) {
                    String className = item.getClassName();
                    if (!className.startsWith("io.sentry.") || className.startsWith("io.sentry.samples.") || className.startsWith("io.sentry.mobile.")) {
                        SentryStackFrame sentryStackFrame = new SentryStackFrame();
                        sentryStackFrame.setInApp(Boolean.valueOf(isInApp(className)));
                        sentryStackFrame.setModule(className);
                        sentryStackFrame.setFunction(item.getMethodName());
                        sentryStackFrame.setFilename(item.getFileName());
                        if (item.getLineNumber() >= 0) {
                            sentryStackFrame.setLineno(Integer.valueOf(item.getLineNumber()));
                        }
                        sentryStackFrame.setNative(Boolean.valueOf(item.isNativeMethod()));
                        sentryStackFrames.add(sentryStackFrame);
                    }
                }
            }
            Collections.reverse(sentryStackFrames);
        }
        return sentryStackFrames;
    }

    @TestOnly
    boolean isInApp(@Nullable String className) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        List<String> list = this.inAppIncludes;
        if (list != null) {
            for (String include : list) {
                if (className.startsWith(include)) {
                    return true;
                }
            }
        }
        List<String> list2 = this.inAppExcludes;
        if (list2 != null) {
            for (String exclude : list2) {
                if (className.startsWith(exclude)) {
                    return false;
                }
            }
        }
        return false;
    }
}
