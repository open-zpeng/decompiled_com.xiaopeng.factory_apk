package io.sentry.transport;

import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public abstract class TransportResult {
    public abstract int getResponseCode();

    public abstract boolean isSuccess();

    @NotNull
    public static TransportResult success() {
        return SuccessTransportResult.INSTANCE;
    }

    @NotNull
    public static TransportResult error(int responseCode) {
        return new ErrorTransportResult(responseCode);
    }

    @NotNull
    public static TransportResult error() {
        return error(-1);
    }

    private TransportResult() {
    }

    /* loaded from: classes2.dex */
    private static final class SuccessTransportResult extends TransportResult {
        static final SuccessTransportResult INSTANCE = new SuccessTransportResult();

        private SuccessTransportResult() {
            super();
        }

        @Override // io.sentry.transport.TransportResult
        public boolean isSuccess() {
            return true;
        }

        @Override // io.sentry.transport.TransportResult
        public int getResponseCode() {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class ErrorTransportResult extends TransportResult {
        private final int responseCode;

        ErrorTransportResult(int responseCode) {
            super();
            this.responseCode = responseCode;
        }

        @Override // io.sentry.transport.TransportResult
        public boolean isSuccess() {
            return false;
        }

        @Override // io.sentry.transport.TransportResult
        public int getResponseCode() {
            return this.responseCode;
        }
    }
}
