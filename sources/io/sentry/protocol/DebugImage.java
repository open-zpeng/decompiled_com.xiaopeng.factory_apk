package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class DebugImage implements IUnknownPropertiesConsumer {
    public static final String PROGUARD = "proguard";
    @Nullable
    private String arch;
    @Nullable
    private String codeFile;
    @Nullable
    private String codeId;
    @Nullable
    private String debugFile;
    @Nullable
    private String debugId;
    @Nullable
    private String imageAddr;
    @Nullable
    private Long imageSize;
    @Nullable
    private String type;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private String uuid;

    @Nullable
    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(@Nullable String uuid) {
        this.uuid = uuid;
    }

    @Nullable
    public String getType() {
        return this.type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getDebugId() {
        return this.debugId;
    }

    public void setDebugId(@Nullable String debugId) {
        this.debugId = debugId;
    }

    @Nullable
    public String getDebugFile() {
        return this.debugFile;
    }

    public void setDebugFile(@Nullable String debugFile) {
        this.debugFile = debugFile;
    }

    @Nullable
    public String getCodeFile() {
        return this.codeFile;
    }

    public void setCodeFile(@Nullable String codeFile) {
        this.codeFile = codeFile;
    }

    @Nullable
    public String getImageAddr() {
        return this.imageAddr;
    }

    public void setImageAddr(@Nullable String imageAddr) {
        this.imageAddr = imageAddr;
    }

    @Nullable
    public Long getImageSize() {
        return this.imageSize;
    }

    public void setImageSize(@Nullable Long imageSize) {
        this.imageSize = imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = Long.valueOf(imageSize);
    }

    @Nullable
    public String getArch() {
        return this.arch;
    }

    public void setArch(@Nullable String arch) {
        this.arch = arch;
    }

    @Nullable
    public String getCodeId() {
        return this.codeId;
    }

    public void setCodeId(@Nullable String codeId) {
        this.codeId = codeId;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }
}
