package io.sentry.android.core.internal.gestures;

import android.view.View;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
interface ViewTargetSelector {
    boolean select(@NotNull View view);

    default boolean skipChildren() {
        return false;
    }
}
