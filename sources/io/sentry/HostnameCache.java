package io.sentry;

import io.sentry.util.Objects;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class HostnameCache {
    @Nullable
    private static HostnameCache INSTANCE;
    private final long cacheDuration;
    @NotNull
    private final ExecutorService executorService;
    private volatile long expirationTimestamp;
    @NotNull
    private final Callable<InetAddress> getLocalhost;
    @Nullable
    private volatile String hostname;
    @NotNull
    private final AtomicBoolean updateRunning;
    private static final long HOSTNAME_CACHE_DURATION = TimeUnit.HOURS.toMillis(5);
    private static final long GET_HOSTNAME_TIMEOUT = TimeUnit.SECONDS.toMillis(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public static HostnameCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HostnameCache();
        }
        return INSTANCE;
    }

    private HostnameCache() {
        this(HOSTNAME_CACHE_DURATION);
    }

    HostnameCache(long cacheDuration) {
        this(cacheDuration, new Callable() { // from class: io.sentry.-$$Lambda$HostnameCache$0Rf2wMKlMTm1paSpRhBPXF4RBZk
            @Override // java.util.concurrent.Callable
            public final Object call() {
                InetAddress localHost;
                localHost = InetAddress.getLocalHost();
                return localHost;
            }
        });
    }

    HostnameCache(long cacheDuration, @NotNull Callable<InetAddress> getLocalhost) {
        this.updateRunning = new AtomicBoolean(false);
        this.executorService = Executors.newSingleThreadExecutor(new HostnameCacheThreadFactory());
        this.cacheDuration = cacheDuration;
        this.getLocalhost = (Callable) Objects.requireNonNull(getLocalhost, "getLocalhost is required");
        updateCache();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.executorService.shutdown();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isClosed() {
        return this.executorService.isShutdown();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public String getHostname() {
        if (this.expirationTimestamp < System.currentTimeMillis() && this.updateRunning.compareAndSet(false, true)) {
            updateCache();
        }
        return this.hostname;
    }

    private void updateCache() {
        Callable<Void> hostRetriever = new Callable() { // from class: io.sentry.-$$Lambda$HostnameCache$6_ZMJnpaNsMFsRhx_jb3BDmP53E
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return HostnameCache.this.lambda$updateCache$1$HostnameCache();
            }
        };
        try {
            Future<Void> futureTask = this.executorService.submit(hostRetriever);
            futureTask.get(GET_HOSTNAME_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            handleCacheUpdateFailure();
        } catch (RuntimeException | ExecutionException | TimeoutException e2) {
            handleCacheUpdateFailure();
        }
    }

    public /* synthetic */ Void lambda$updateCache$1$HostnameCache() throws Exception {
        try {
            this.hostname = this.getLocalhost.call().getCanonicalHostName();
            this.expirationTimestamp = System.currentTimeMillis() + this.cacheDuration;
            this.updateRunning.set(false);
            return null;
        } catch (Throwable th) {
            this.updateRunning.set(false);
            throw th;
        }
    }

    private void handleCacheUpdateFailure() {
        this.expirationTimestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1L);
    }

    /* loaded from: classes2.dex */
    private static final class HostnameCacheThreadFactory implements ThreadFactory {
        private int cnt;

        private HostnameCacheThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        @NotNull
        public Thread newThread(@NotNull Runnable r) {
            StringBuilder sb = new StringBuilder();
            sb.append("SentryHostnameCache-");
            int i = this.cnt;
            this.cnt = i + 1;
            sb.append(i);
            Thread ret = new Thread(r, sb.toString());
            ret.setDaemon(true);
            return ret;
        }
    }
}
