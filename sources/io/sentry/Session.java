package io.sentry;

import io.sentry.protocol.User;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Session {
    @Nullable
    private final String distinctId;
    @Nullable
    private Double duration;
    @Nullable
    private final String environment;
    @NotNull
    private final AtomicInteger errorCount;
    @Nullable
    private Boolean init;
    @Nullable
    private final String ipAddress;
    @NotNull
    private final String release;
    @Nullable
    private Long sequence;
    @Nullable
    private final UUID sessionId;
    @NotNull
    private final Object sessionLock;
    @NotNull
    private final Date started;
    @NotNull
    private State status;
    @Nullable
    private Date timestamp;
    @Nullable
    private String userAgent;

    /* loaded from: classes2.dex */
    public enum State {
        Ok,
        Exited,
        Crashed,
        Abnormal
    }

    public Session(@NotNull State status, @NotNull Date started, @Nullable Date timestamp, int errorCount, @Nullable String distinctId, @Nullable UUID sessionId, @Nullable Boolean init, @Nullable Long sequence, @Nullable Double duration, @Nullable String ipAddress, @Nullable String userAgent, @Nullable String environment, @NotNull String release) {
        this.sessionLock = new Object();
        this.status = status;
        this.started = started;
        this.timestamp = timestamp;
        this.errorCount = new AtomicInteger(errorCount);
        this.distinctId = distinctId;
        this.sessionId = sessionId;
        this.init = init;
        this.sequence = sequence;
        this.duration = duration;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.environment = environment;
        this.release = release;
    }

    public Session(@Nullable String distinctId, @Nullable User user, @Nullable String environment, @NotNull String release) {
        this(State.Ok, DateUtils.getCurrentDateTime(), DateUtils.getCurrentDateTime(), 0, distinctId, UUID.randomUUID(), true, null, null, user != null ? user.getIpAddress() : null, null, environment, release);
    }

    @Nullable
    public Date getStarted() {
        Date date = this.started;
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    @Nullable
    public String getDistinctId() {
        return this.distinctId;
    }

    @Nullable
    public UUID getSessionId() {
        return this.sessionId;
    }

    @Nullable
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Nullable
    public String getUserAgent() {
        return this.userAgent;
    }

    @Nullable
    public String getEnvironment() {
        return this.environment;
    }

    @NotNull
    public String getRelease() {
        return this.release;
    }

    @Nullable
    public Boolean getInit() {
        return this.init;
    }

    @ApiStatus.Internal
    public void setInitAsTrue() {
        this.init = true;
    }

    public int errorCount() {
        return this.errorCount.get();
    }

    @NotNull
    public State getStatus() {
        return this.status;
    }

    @Nullable
    public Long getSequence() {
        return this.sequence;
    }

    @Nullable
    public Double getDuration() {
        return this.duration;
    }

    @Nullable
    public Date getTimestamp() {
        Date timestampRef = this.timestamp;
        if (timestampRef != null) {
            return (Date) timestampRef.clone();
        }
        return null;
    }

    public void end() {
        end(DateUtils.getCurrentDateTime());
    }

    public void end(@Nullable Date timestamp) {
        synchronized (this.sessionLock) {
            this.init = null;
            if (this.status == State.Ok) {
                this.status = State.Exited;
            }
            if (timestamp != null) {
                this.timestamp = timestamp;
            } else {
                this.timestamp = DateUtils.getCurrentDateTime();
            }
            if (this.timestamp != null) {
                this.duration = Double.valueOf(calculateDurationTime(this.timestamp));
                this.sequence = Long.valueOf(getSequenceTimestamp(this.timestamp));
            }
        }
    }

    private double calculateDurationTime(@NotNull Date timestamp) {
        long diff = Math.abs(timestamp.getTime() - this.started.getTime());
        return diff / 1000.0d;
    }

    public boolean update(@Nullable State status, @Nullable String userAgent, boolean addErrorsCount) {
        boolean sessionHasBeenUpdated;
        synchronized (this.sessionLock) {
            sessionHasBeenUpdated = false;
            if (status != null) {
                try {
                    this.status = status;
                    sessionHasBeenUpdated = true;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (userAgent != null) {
                this.userAgent = userAgent;
                sessionHasBeenUpdated = true;
            }
            if (addErrorsCount) {
                this.errorCount.addAndGet(1);
                sessionHasBeenUpdated = true;
            }
            if (sessionHasBeenUpdated) {
                this.init = null;
                this.timestamp = DateUtils.getCurrentDateTime();
                if (this.timestamp != null) {
                    this.sequence = Long.valueOf(getSequenceTimestamp(this.timestamp));
                }
            }
        }
        return sessionHasBeenUpdated;
    }

    private long getSequenceTimestamp(@NotNull Date timestamp) {
        long sequence = timestamp.getTime();
        if (sequence < 0) {
            return Math.abs(sequence);
        }
        return sequence;
    }

    @NotNull
    /* renamed from: clone */
    public Session m126clone() {
        return new Session(this.status, this.started, this.timestamp, this.errorCount.get(), this.distinctId, this.sessionId, this.init, this.sequence, this.duration, this.ipAddress, this.userAgent, this.environment, this.release);
    }
}
