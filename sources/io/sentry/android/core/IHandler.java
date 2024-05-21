package io.sentry.android.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
@TestOnly
/* loaded from: classes2.dex */
interface IHandler {
    @NotNull
    Thread getThread();

    void post(@NotNull Runnable runnable);
}
