package io.sentry.protocol;

import com.google.gson.annotations.SerializedName;
import io.sentry.IUnknownPropertiesConsumer;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class SentryStackFrame implements IUnknownPropertiesConsumer {
    @SerializedName("native")
    @Nullable
    private Boolean _native;
    @SerializedName("package")
    @Nullable
    private String _package;
    @Nullable
    private String absPath;
    @Nullable
    private Integer colno;
    @Nullable
    private String contextLine;
    @Nullable
    private String filename;
    @Nullable
    private List<Integer> framesOmitted;
    @Nullable
    private String function;
    @Nullable
    private String imageAddr;
    @Nullable
    private Boolean inApp;
    @Nullable
    private String instructionAddr;
    @Nullable
    private Integer lineno;
    @Nullable
    private String module;
    @Nullable
    private String platform;
    @Nullable
    private List<String> postContext;
    @Nullable
    private List<String> preContext;
    @Nullable
    private String rawFunction;
    @Nullable
    private String symbolAddr;
    @Nullable
    private Map<String, Object> unknown;
    @Nullable
    private Map<String, String> vars;

    @Nullable
    public List<String> getPreContext() {
        return this.preContext;
    }

    public void setPreContext(@Nullable List<String> preContext) {
        this.preContext = preContext;
    }

    @Nullable
    public List<String> getPostContext() {
        return this.postContext;
    }

    public void setPostContext(@Nullable List<String> postContext) {
        this.postContext = postContext;
    }

    @Nullable
    public Map<String, String> getVars() {
        return this.vars;
    }

    public void setVars(@Nullable Map<String, String> vars) {
        this.vars = vars;
    }

    @Nullable
    public List<Integer> getFramesOmitted() {
        return this.framesOmitted;
    }

    public void setFramesOmitted(@Nullable List<Integer> framesOmitted) {
        this.framesOmitted = framesOmitted;
    }

    @Nullable
    public String getFilename() {
        return this.filename;
    }

    public void setFilename(@Nullable String filename) {
        this.filename = filename;
    }

    @Nullable
    public String getFunction() {
        return this.function;
    }

    public void setFunction(@Nullable String function) {
        this.function = function;
    }

    @Nullable
    public String getModule() {
        return this.module;
    }

    public void setModule(@Nullable String module) {
        this.module = module;
    }

    @Nullable
    public Integer getLineno() {
        return this.lineno;
    }

    public void setLineno(@Nullable Integer lineno) {
        this.lineno = lineno;
    }

    @Nullable
    public Integer getColno() {
        return this.colno;
    }

    public void setColno(@Nullable Integer colno) {
        this.colno = colno;
    }

    @Nullable
    public String getAbsPath() {
        return this.absPath;
    }

    public void setAbsPath(@Nullable String absPath) {
        this.absPath = absPath;
    }

    @Nullable
    public String getContextLine() {
        return this.contextLine;
    }

    public void setContextLine(@Nullable String contextLine) {
        this.contextLine = contextLine;
    }

    @Nullable
    public Boolean isInApp() {
        return this.inApp;
    }

    public void setInApp(@Nullable Boolean inApp) {
        this.inApp = inApp;
    }

    @Nullable
    public String getPackage() {
        return this._package;
    }

    public void setPackage(@Nullable String _package) {
        this._package = _package;
    }

    @Nullable
    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(@Nullable String platform) {
        this.platform = platform;
    }

    @Nullable
    public String getImageAddr() {
        return this.imageAddr;
    }

    public void setImageAddr(@Nullable String imageAddr) {
        this.imageAddr = imageAddr;
    }

    @Nullable
    public String getSymbolAddr() {
        return this.symbolAddr;
    }

    public void setSymbolAddr(@Nullable String symbolAddr) {
        this.symbolAddr = symbolAddr;
    }

    @Nullable
    public String getInstructionAddr() {
        return this.instructionAddr;
    }

    public void setInstructionAddr(@Nullable String instructionAddr) {
        this.instructionAddr = instructionAddr;
    }

    @Nullable
    public Boolean isNative() {
        return this._native;
    }

    public void setNative(@Nullable Boolean _native) {
        this._native = _native;
    }

    @Override // io.sentry.IUnknownPropertiesConsumer
    @ApiStatus.Internal
    public void acceptUnknownProperties(@NotNull Map<String, Object> unknown) {
        this.unknown = unknown;
    }

    @Nullable
    public String getRawFunction() {
        return this.rawFunction;
    }

    public void setRawFunction(@Nullable String rawFunction) {
        this.rawFunction = rawFunction;
    }
}
