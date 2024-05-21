package io.sentry;

import io.sentry.cache.EnvelopeCache;
import java.io.File;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class SentryCrashLastRunState {
    private static final SentryCrashLastRunState INSTANCE = new SentryCrashLastRunState();
    @Nullable
    private Boolean crashedLastRun;
    @NotNull
    private final Object crashedLastRunLock = new Object();
    private boolean readCrashedLastRun;

    private SentryCrashLastRunState() {
    }

    public static SentryCrashLastRunState getInstance() {
        return INSTANCE;
    }

    @Nullable
    public Boolean isCrashedLastRun(@Nullable String cacheDirPath, boolean deleteFile) {
        synchronized (this.crashedLastRunLock) {
            if (this.readCrashedLastRun) {
                return this.crashedLastRun;
            } else if (cacheDirPath == null) {
                return null;
            } else {
                this.readCrashedLastRun = true;
                File javaMarker = new File(cacheDirPath, EnvelopeCache.CRASH_MARKER_FILE);
                File nativeMarker = new File(cacheDirPath, EnvelopeCache.NATIVE_CRASH_MARKER_FILE);
                boolean exists = false;
                try {
                    if (javaMarker.exists()) {
                        exists = true;
                        javaMarker.delete();
                    } else if (nativeMarker.exists()) {
                        exists = true;
                        if (deleteFile) {
                            nativeMarker.delete();
                        }
                    }
                } catch (Throwable th) {
                }
                this.crashedLastRun = Boolean.valueOf(exists);
                return this.crashedLastRun;
            }
        }
    }

    public void setCrashedLastRun(boolean crashedLastRun) {
        synchronized (this.crashedLastRunLock) {
            if (!this.readCrashedLastRun) {
                this.crashedLastRun = Boolean.valueOf(crashedLastRun);
                this.readCrashedLastRun = true;
            }
        }
    }

    @TestOnly
    public void reset() {
        synchronized (this.crashedLastRunLock) {
            this.readCrashedLastRun = false;
            this.crashedLastRun = null;
        }
    }
}
