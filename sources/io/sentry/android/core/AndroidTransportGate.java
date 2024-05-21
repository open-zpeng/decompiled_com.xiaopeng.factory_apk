package io.sentry.android.core;

import android.content.Context;
import io.sentry.ILogger;
import io.sentry.android.core.internal.util.ConnectivityChecker;
import io.sentry.transport.ITransportGate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
/* loaded from: classes2.dex */
final class AndroidTransportGate implements ITransportGate {
    private final Context context;
    @NotNull
    private final ILogger logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AndroidTransportGate(@NotNull Context context, @NotNull ILogger logger) {
        this.context = context;
        this.logger = logger;
    }

    @Override // io.sentry.transport.ITransportGate
    public boolean isConnected() {
        return isConnected(ConnectivityChecker.getConnectionStatus(this.context, this.logger));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: io.sentry.android.core.AndroidTransportGate$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status = new int[ConnectivityChecker.Status.values().length];

        static {
            try {
                $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.Status.CONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.Status.UNKNOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[ConnectivityChecker.Status.NO_PERMISSION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    @TestOnly
    boolean isConnected(@NotNull ConnectivityChecker.Status status) {
        int i = AnonymousClass1.$SwitchMap$io$sentry$android$core$internal$util$ConnectivityChecker$Status[status.ordinal()];
        return i == 1 || i == 2 || i == 3;
    }
}
