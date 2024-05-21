package io.sentry;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public interface TransactionFinishedCallback {
    void execute(@NotNull ITransaction iTransaction);
}
