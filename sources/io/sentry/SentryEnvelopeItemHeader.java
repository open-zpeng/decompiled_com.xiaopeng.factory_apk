package io.sentry;

import io.sentry.util.Objects;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryEnvelopeItemHeader {
    @Nullable
    private final String attachmentType;
    @Nullable
    private final String contentType;
    @Nullable
    private final String fileName;
    @Nullable
    private final Callable<Integer> getLength;
    private final int length;
    @NotNull
    private final SentryItemType type;

    @NotNull
    public SentryItemType getType() {
        return this.type;
    }

    public int getLength() {
        Callable<Integer> callable = this.getLength;
        if (callable != null) {
            try {
                return callable.call().intValue();
            } catch (Throwable th) {
                return -1;
            }
        }
        return this.length;
    }

    @Nullable
    public String getContentType() {
        return this.contentType;
    }

    @Nullable
    public String getFileName() {
        return this.fileName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryEnvelopeItemHeader(@NotNull SentryItemType type, int length, @Nullable String contentType, @Nullable String fileName, @Nullable String attachmentType) {
        this.type = (SentryItemType) Objects.requireNonNull(type, "type is required");
        this.contentType = contentType;
        this.length = length;
        this.fileName = fileName;
        this.getLength = null;
        this.attachmentType = attachmentType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryEnvelopeItemHeader(@NotNull SentryItemType type, @Nullable Callable<Integer> getLength, @Nullable String contentType, @Nullable String fileName, @Nullable String attachmentType) {
        this.type = (SentryItemType) Objects.requireNonNull(type, "type is required");
        this.contentType = contentType;
        this.length = -1;
        this.fileName = fileName;
        this.getLength = getLength;
        this.attachmentType = attachmentType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SentryEnvelopeItemHeader(@NotNull SentryItemType type, @Nullable Callable<Integer> getLength, @Nullable String contentType, @Nullable String fileName) {
        this(type, getLength, contentType, fileName, (String) null);
    }

    @Nullable
    public String getAttachmentType() {
        return this.attachmentType;
    }
}
