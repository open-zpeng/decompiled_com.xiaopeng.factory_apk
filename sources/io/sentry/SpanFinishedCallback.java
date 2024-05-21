package io.sentry;

import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public interface SpanFinishedCallback {
    void execute(@NotNull Span span);
}
