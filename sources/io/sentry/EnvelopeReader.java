package io.sentry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.charset.Charset;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class EnvelopeReader implements IEnvelopeReader {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final Gson gson = new GsonBuilder().registerTypeAdapter(SentryEnvelopeHeader.class, new SentryEnvelopeHeaderAdapter()).registerTypeAdapter(SentryEnvelopeItemHeader.class, new SentryEnvelopeItemHeaderAdapter()).disableHtmlEscaping().create();

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0096, code lost:
        r1 = new io.sentry.SentryEnvelope(r8, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x009b, code lost:
        r4.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x009e, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0106, code lost:
        throw new java.lang.IllegalArgumentException("Item header at index '" + r12.size() + "' is null or empty.");
     */
    @Override // io.sentry.IEnvelopeReader
    @org.jetbrains.annotations.Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public io.sentry.SentryEnvelope read(@org.jetbrains.annotations.NotNull java.io.InputStream r18) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 344
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.sentry.EnvelopeReader.read(java.io.InputStream):io.sentry.SentryEnvelope");
    }

    @Nullable
    private SentryEnvelopeHeader deserializeEnvelopeHeader(@NotNull byte[] buffer, int offset, int length) {
        String json = new String(buffer, offset, length, UTF_8);
        return (SentryEnvelopeHeader) this.gson.fromJson(json, (Class<Object>) SentryEnvelopeHeader.class);
    }

    @Nullable
    private SentryEnvelopeItemHeader deserializeEnvelopeItemHeader(@NotNull byte[] buffer, int offset, int length) {
        String json = new String(buffer, offset, length, UTF_8);
        return (SentryEnvelopeItemHeader) this.gson.fromJson(json, (Class<Object>) SentryEnvelopeItemHeader.class);
    }
}
