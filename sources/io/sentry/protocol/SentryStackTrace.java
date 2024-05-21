package io.sentry.protocol;

import io.sentry.IUnknownPropertiesConsumer;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryStackTrace implements IUnknownPropertiesConsumer {
    @Nullable
    private List<SentryStackFrame> frames;
    @Nullable
    private Map<String, String> registers;
    @Nullable
    private Boolean snapshot;
    @Nullable
    private Map<String, Object> unknown;

    public SentryStackTrace() {
    }

    public SentryStackTrace(@Nullable List<SentryStackFrame> frames) {
        this.frames = frames;
    }

    @Nullable
    public List<SentryStackFrame> getFrames() {
        return this.frames;
    }

    public void setFrames(@Nullable List<SentryStackFrame> frames) {
        this.frames = frames;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @Nullable
    public Map<String, String> getRegisters() {
        return this.registers;
    }

    public void setRegisters(@Nullable Map<String, String> registers) {
        this.registers = registers;
    }

    @Nullable
    public Boolean getSnapshot() {
        return this.snapshot;
    }

    public void setSnapshot(@Nullable Boolean snapshot) {
        this.snapshot = snapshot;
    }
}
