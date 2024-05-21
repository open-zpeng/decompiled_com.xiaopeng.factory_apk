package io.sentry;

import io.sentry.protocol.SentryId;
import io.sentry.protocol.User;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@ApiStatus.Experimental
/* loaded from: classes2.dex */
public final class TraceState {
    @Nullable
    private String environment;
    @NotNull
    private String publicKey;
    @Nullable
    private String release;
    @NotNull
    private SentryId traceId;
    @Nullable
    private String transaction;
    @Nullable
    private TraceStateUser user;

    /* JADX INFO: Access modifiers changed from: package-private */
    public TraceState(@NotNull SentryId traceId, @NotNull String publicKey) {
        this(traceId, publicKey, null, null, null, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TraceState(@NotNull SentryId traceId, @NotNull String publicKey, @Nullable String release, @Nullable String environment, @Nullable TraceStateUser user, @Nullable String transaction) {
        this.traceId = traceId;
        this.publicKey = publicKey;
        this.release = release;
        this.environment = environment;
        this.user = user;
        this.transaction = transaction;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TraceState(@NotNull ITransaction transaction, @Nullable User user, @NotNull SentryOptions sentryOptions) {
        this(transaction.getSpanContext().getTraceId(), new Dsn(sentryOptions.getDsn()).getPublicKey(), sentryOptions.getRelease(), sentryOptions.getEnvironment(), user != null ? new TraceStateUser(user) : null, transaction.getName());
    }

    @NotNull
    public SentryId getTraceId() {
        return this.traceId;
    }

    @NotNull
    public String getPublicKey() {
        return this.publicKey;
    }

    @Nullable
    public String getRelease() {
        return this.release;
    }

    @Nullable
    public String getEnvironment() {
        return this.environment;
    }

    @Nullable
    public TraceStateUser getUser() {
        return this.user;
    }

    @Nullable
    public String getTransaction() {
        return this.transaction;
    }

    /* loaded from: classes2.dex */
    static final class TraceStateUser {
        @Nullable
        private String id;
        @Nullable
        private String segment;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TraceStateUser(@Nullable String id, @Nullable String segment) {
            this.id = id;
            this.segment = segment;
        }

        public TraceStateUser(@Nullable User protocolUser) {
            if (protocolUser != null) {
                this.id = protocolUser.getId();
                this.segment = getSegment(protocolUser);
            }
        }

        @Nullable
        private static String getSegment(@NotNull User user) {
            Map<String, String> others = user.getOthers();
            if (others != null) {
                return others.get("segment");
            }
            return null;
        }

        @Nullable
        public String getId() {
            return this.id;
        }

        @Nullable
        public String getSegment() {
            return this.segment;
        }
    }
}
