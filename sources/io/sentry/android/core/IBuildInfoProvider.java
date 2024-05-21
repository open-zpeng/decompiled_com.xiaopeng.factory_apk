package io.sentry.android.core;

import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public interface IBuildInfoProvider {
    @Nullable
    String getBuildTags();

    int getSdkInfoVersion();
}
