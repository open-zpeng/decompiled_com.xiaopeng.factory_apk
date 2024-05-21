package io.sentry;

import cn.hutool.core.text.CharPool;
import io.sentry.protocol.SentryId;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class UserFeedback {
    @Nullable
    private String comments;
    @Nullable
    private String email;
    private final SentryId eventId;
    @Nullable
    private String name;

    public UserFeedback(SentryId eventId) {
        this(eventId, null, null, null);
    }

    public UserFeedback(SentryId eventId, @Nullable String name, @Nullable String email, @Nullable String comments) {
        this.eventId = eventId;
        this.name = name;
        this.email = email;
        this.comments = comments;
    }

    public SentryId getEventId() {
        return this.eventId;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    @Nullable
    public String getComments() {
        return this.comments;
    }

    public void setComments(@Nullable String comments) {
        this.comments = comments;
    }

    public String toString() {
        return "UserFeedback{eventId=" + this.eventId + ", name='" + this.name + CharPool.SINGLE_QUOTE + ", email='" + this.email + CharPool.SINGLE_QUOTE + ", comments='" + this.comments + CharPool.SINGLE_QUOTE + '}';
    }
}
