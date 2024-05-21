package io.sentry;

import org.apache.commons.net.nntp.NNTPReply;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public enum SpanStatus {
    OK(200, 299),
    CANCELLED(499),
    INTERNAL_ERROR(500),
    UNKNOWN(500),
    UNKNOWN_ERROR(500),
    INVALID_ARGUMENT(NNTPReply.SERVICE_DISCONTINUED),
    DEADLINE_EXCEEDED(504),
    NOT_FOUND(404),
    ALREADY_EXISTS(409),
    PERMISSION_DENIED(403),
    RESOURCE_EXHAUSTED(429),
    FAILED_PRECONDITION(NNTPReply.SERVICE_DISCONTINUED),
    ABORTED(409),
    OUT_OF_RANGE(NNTPReply.SERVICE_DISCONTINUED),
    UNIMPLEMENTED(501),
    UNAVAILABLE(503),
    DATA_LOSS(500),
    UNAUTHENTICATED(401);
    
    private final int maxHttpStatusCode;
    private final int minHttpStatusCode;

    SpanStatus(int httpStatusCode) {
        this.minHttpStatusCode = httpStatusCode;
        this.maxHttpStatusCode = httpStatusCode;
    }

    SpanStatus(int minHttpStatusCode, int maxHttpStatusCode) {
        this.minHttpStatusCode = minHttpStatusCode;
        this.maxHttpStatusCode = maxHttpStatusCode;
    }

    @Nullable
    public static SpanStatus fromHttpStatusCode(int httpStatusCode) {
        SpanStatus[] values;
        for (SpanStatus status : values()) {
            if (status.matches(httpStatusCode)) {
                return status;
            }
        }
        return null;
    }

    @NotNull
    public static SpanStatus fromHttpStatusCode(@Nullable Integer httpStatusCode, @NotNull SpanStatus defaultStatus) {
        SpanStatus spanStatus = httpStatusCode != null ? fromHttpStatusCode(httpStatusCode.intValue()) : defaultStatus;
        return spanStatus != null ? spanStatus : defaultStatus;
    }

    private boolean matches(int httpStatusCode) {
        return httpStatusCode >= this.minHttpStatusCode && httpStatusCode <= this.maxHttpStatusCode;
    }
}
