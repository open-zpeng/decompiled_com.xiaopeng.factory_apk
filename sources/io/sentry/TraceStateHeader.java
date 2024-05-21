package io.sentry;

import cn.hutool.core.text.StrPool;
import io.sentry.vendor.Base64;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
@ApiStatus.Experimental
/* loaded from: classes2.dex */
public final class TraceStateHeader {
    public static final String TRACE_STATE_HEADER = "tracestate";
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    @NotNull
    private final String value;

    @NotNull
    public static TraceStateHeader fromTraceState(@NotNull TraceState traceState, @NotNull ISerializer serializer, @NotNull ILogger logger) {
        return new TraceStateHeader(base64encode(toJson(traceState, serializer, logger)));
    }

    public TraceStateHeader(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String getName() {
        return TRACE_STATE_HEADER;
    }

    @NotNull
    public String getValue() {
        return this.value;
    }

    @NotNull
    private static String toJson(@NotNull TraceState traceState, @NotNull ISerializer serializer, @NotNull ILogger logger) {
        StringWriter stringWriter = new StringWriter();
        try {
            serializer.serialize((ISerializer) traceState, (Writer) stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            logger.log(SentryLevel.ERROR, "Failed to serialize trace state header", e);
            return StrPool.EMPTY_JSON;
        }
    }

    @VisibleForTesting
    @NotNull
    static String base64encode(@NotNull String input) {
        return Base64.encodeToString(input.getBytes(UTF8_CHARSET), 3);
    }

    @VisibleForTesting
    @NotNull
    static String base64decode(@NotNull String input) {
        return new String(Base64.decode(input, 3), UTF8_CHARSET);
    }
}
