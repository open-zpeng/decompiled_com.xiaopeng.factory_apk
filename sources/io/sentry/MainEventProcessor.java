package io.sentry;

import io.sentry.hints.Cached;
import io.sentry.protocol.DebugImage;
import io.sentry.protocol.DebugMeta;
import io.sentry.protocol.SentryException;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import io.sentry.util.ApplyScopeUtils;
import io.sentry.util.Objects;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;
@ApiStatus.Internal
/* loaded from: classes2.dex */
public final class MainEventProcessor implements EventProcessor, Closeable {
    private static final String DEFAULT_ENVIRONMENT = "production";
    @Nullable
    private final HostnameCache hostnameCache;
    @NotNull
    private final SentryOptions options;
    @NotNull
    private final SentryExceptionFactory sentryExceptionFactory;
    @NotNull
    private final SentryThreadFactory sentryThreadFactory;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MainEventProcessor(@NotNull SentryOptions options) {
        this(options, options.isAttachServerName() ? HostnameCache.getInstance() : null);
    }

    MainEventProcessor(@NotNull SentryOptions options, @Nullable HostnameCache hostnameCache) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryOptions is required.");
        this.hostnameCache = hostnameCache;
        SentryStackTraceFactory sentryStackTraceFactory = new SentryStackTraceFactory(this.options.getInAppExcludes(), this.options.getInAppIncludes());
        this.sentryExceptionFactory = new SentryExceptionFactory(sentryStackTraceFactory);
        this.sentryThreadFactory = new SentryThreadFactory(sentryStackTraceFactory, this.options);
    }

    MainEventProcessor(@NotNull SentryOptions options, @NotNull SentryThreadFactory sentryThreadFactory, @NotNull SentryExceptionFactory sentryExceptionFactory, @NotNull HostnameCache hostnameCache) {
        this.options = (SentryOptions) Objects.requireNonNull(options, "The SentryOptions is required.");
        this.sentryThreadFactory = (SentryThreadFactory) Objects.requireNonNull(sentryThreadFactory, "The SentryThreadFactory is required.");
        this.sentryExceptionFactory = (SentryExceptionFactory) Objects.requireNonNull(sentryExceptionFactory, "The SentryExceptionFactory is required.");
        this.hostnameCache = (HostnameCache) Objects.requireNonNull(hostnameCache, "The HostnameCache is required");
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryEvent process(@NotNull SentryEvent event, @Nullable Object hint) {
        setCommons(event);
        setExceptions(event);
        setDebugMeta(event);
        if (shouldApplyScopeData(event, hint)) {
            processNonCachedEvent(event);
            setThreads(event, hint);
        }
        return event;
    }

    private void setDebugMeta(@NotNull SentryEvent event) {
        if (this.options.getProguardUuid() != null) {
            DebugMeta debugMeta = event.getDebugMeta();
            if (debugMeta == null) {
                debugMeta = new DebugMeta();
            }
            if (debugMeta.getImages() == null) {
                debugMeta.setImages(new ArrayList());
            }
            List<DebugImage> images = debugMeta.getImages();
            if (images != null) {
                DebugImage debugImage = new DebugImage();
                debugImage.setType(DebugImage.PROGUARD);
                debugImage.setUuid(this.options.getProguardUuid());
                images.add(debugImage);
                event.setDebugMeta(debugMeta);
            }
        }
    }

    private boolean shouldApplyScopeData(@NotNull SentryBaseEvent event, @Nullable Object hint) {
        if (ApplyScopeUtils.shouldApplyScopeData(hint)) {
            return true;
        }
        this.options.getLogger().log(SentryLevel.DEBUG, "Event was cached so not applying data relevant to the current app execution/version: %s", event.getEventId());
        return false;
    }

    private void processNonCachedEvent(@NotNull SentryBaseEvent event) {
        setRelease(event);
        setEnvironment(event);
        setServerName(event);
        setDist(event);
        setSdk(event);
        setTags(event);
        mergeUser(event);
    }

    @Override // io.sentry.EventProcessor
    @NotNull
    public SentryTransaction process(@NotNull SentryTransaction transaction, @Nullable Object hint) {
        setCommons(transaction);
        if (shouldApplyScopeData(transaction, hint)) {
            processNonCachedEvent(transaction);
        }
        return transaction;
    }

    private void setCommons(@NotNull SentryBaseEvent event) {
        setPlatform(event);
    }

    private void setPlatform(@NotNull SentryBaseEvent event) {
        if (event.getPlatform() == null) {
            event.setPlatform(SentryBaseEvent.DEFAULT_PLATFORM);
        }
    }

    private void setRelease(@NotNull SentryBaseEvent event) {
        if (event.getRelease() == null) {
            event.setRelease(this.options.getRelease());
        }
    }

    private void setEnvironment(@NotNull SentryBaseEvent event) {
        if (event.getEnvironment() == null) {
            event.setEnvironment(this.options.getEnvironment() != null ? this.options.getEnvironment() : DEFAULT_ENVIRONMENT);
        }
    }

    private void setServerName(@NotNull SentryBaseEvent event) {
        if (event.getServerName() == null) {
            event.setServerName(this.options.getServerName());
        }
        if (this.options.isAttachServerName() && this.hostnameCache != null && event.getServerName() == null) {
            event.setServerName(this.hostnameCache.getHostname());
        }
    }

    private void setDist(@NotNull SentryBaseEvent event) {
        if (event.getDist() == null) {
            event.setDist(this.options.getDist());
        }
    }

    private void setSdk(@NotNull SentryBaseEvent event) {
        if (event.getSdk() == null) {
            event.setSdk(this.options.getSdkVersion());
        }
    }

    private void setTags(@NotNull SentryBaseEvent event) {
        if (event.getTags() == null) {
            event.setTags(new HashMap(this.options.getTags()));
            return;
        }
        for (Map.Entry<String, String> item : this.options.getTags().entrySet()) {
            if (!event.getTags().containsKey(item.getKey())) {
                event.setTag(item.getKey(), item.getValue());
            }
        }
    }

    private void mergeUser(@NotNull SentryBaseEvent event) {
        if (this.options.isSendDefaultPii()) {
            if (event.getUser() == null) {
                User user = new User();
                user.setIpAddress("{{auto}}");
                event.setUser(user);
            } else if (event.getUser().getIpAddress() == null) {
                event.getUser().setIpAddress("{{auto}}");
            }
        }
    }

    private void setExceptions(@NotNull SentryEvent event) {
        Throwable throwable = event.getThrowableMechanism();
        if (throwable != null) {
            event.setExceptions(this.sentryExceptionFactory.getSentryExceptions(throwable));
        }
    }

    private void setThreads(@NotNull SentryEvent event, @Nullable Object hint) {
        if (event.getThreads() == null) {
            List<Long> mechanismThreadIds = null;
            List<SentryException> eventExceptions = event.getExceptions();
            if (eventExceptions != null && !eventExceptions.isEmpty()) {
                for (SentryException item : eventExceptions) {
                    if (item.getMechanism() != null && item.getThreadId() != null) {
                        if (mechanismThreadIds == null) {
                            mechanismThreadIds = new ArrayList<>();
                        }
                        mechanismThreadIds.add(item.getThreadId());
                    }
                }
            }
            if (this.options.isAttachThreads()) {
                event.setThreads(this.sentryThreadFactory.getCurrentThreads(mechanismThreadIds));
            } else if (this.options.isAttachStacktrace()) {
                if ((eventExceptions == null || eventExceptions.isEmpty()) && !isCachedHint(hint)) {
                    event.setThreads(this.sentryThreadFactory.getCurrentThread());
                }
            }
        }
    }

    private boolean isCachedHint(@Nullable Object hint) {
        return hint instanceof Cached;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        HostnameCache hostnameCache = this.hostnameCache;
        if (hostnameCache != null) {
            hostnameCache.close();
        }
    }

    boolean isClosed() {
        HostnameCache hostnameCache = this.hostnameCache;
        if (hostnameCache != null) {
            return hostnameCache.isClosed();
        }
        return true;
    }

    @VisibleForTesting
    @Nullable
    HostnameCache getHostnameCache() {
        return this.hostnameCache;
    }
}
