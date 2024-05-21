package io.sentry;

import java.io.File;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Attachment {
    private static final String DEFAULT_ATTACHMENT_TYPE = "event.attachment";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private final boolean addToTransactions;
    @Nullable
    private String attachmentType;
    @Nullable
    private byte[] bytes;
    @NotNull
    private final String contentType;
    @NotNull
    private final String filename;
    @Nullable
    private String pathname;

    public Attachment(@NotNull byte[] bytes, @NotNull String filename) {
        this(bytes, filename, "application/octet-stream");
    }

    public Attachment(@NotNull byte[] bytes, @NotNull String filename, @NotNull String contentType) {
        this(bytes, filename, contentType, false);
    }

    public Attachment(@NotNull byte[] bytes, @NotNull String filename, @NotNull String contentType, boolean addToTransactions) {
        this.attachmentType = DEFAULT_ATTACHMENT_TYPE;
        this.bytes = bytes;
        this.filename = filename;
        this.contentType = contentType;
        this.addToTransactions = addToTransactions;
    }

    public Attachment(@NotNull String pathname) {
        this(pathname, new File(pathname).getName());
    }

    public Attachment(@NotNull String pathname, @NotNull String filename) {
        this(pathname, filename, "application/octet-stream");
    }

    public Attachment(@NotNull String pathname, @NotNull String filename, @NotNull String contentType) {
        this(pathname, filename, contentType, false);
    }

    public Attachment(@NotNull String pathname, @NotNull String filename, @NotNull String contentType, boolean addToTransactions) {
        this.attachmentType = DEFAULT_ATTACHMENT_TYPE;
        this.pathname = pathname;
        this.filename = filename;
        this.contentType = contentType;
        this.addToTransactions = addToTransactions;
    }

    public Attachment(@NotNull String pathname, @NotNull String filename, @NotNull String contentType, boolean addToTransactions, @Nullable String attachmentType) {
        this.attachmentType = DEFAULT_ATTACHMENT_TYPE;
        this.pathname = pathname;
        this.filename = filename;
        this.contentType = contentType;
        this.addToTransactions = addToTransactions;
        this.attachmentType = attachmentType;
    }

    @Nullable
    public byte[] getBytes() {
        return this.bytes;
    }

    @Nullable
    public String getPathname() {
        return this.pathname;
    }

    @NotNull
    public String getFilename() {
        return this.filename;
    }

    @NotNull
    public String getContentType() {
        return this.contentType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAddToTransactions() {
        return this.addToTransactions;
    }

    @Nullable
    public String getAttachmentType() {
        return this.attachmentType;
    }
}
