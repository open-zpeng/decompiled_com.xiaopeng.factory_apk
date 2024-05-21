package io.sentry;

import io.sentry.SentryOptions;
import io.sentry.protocol.Contexts;
import io.sentry.protocol.Request;
import io.sentry.protocol.User;
import io.sentry.util.CollectionUtils;
import io.sentry.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class Scope {
    @NotNull
    private List<Attachment> attachments;
    @NotNull
    private Queue<Breadcrumb> breadcrumbs;
    @NotNull
    private Contexts contexts;
    @NotNull
    private List<EventProcessor> eventProcessors;
    @NotNull
    private Map<String, Object> extra;
    @NotNull
    private List<String> fingerprint;
    @Nullable
    private SentryLevel level;
    @NotNull
    private final SentryOptions options;
    @Nullable
    private Request request;
    @Nullable
    private volatile Session session;
    @NotNull
    private final Object sessionLock;
    @NotNull
    private Map<String, String> tags;
    @Nullable
    private ITransaction transaction;
    @NotNull
    private final Object transactionLock;
    @Nullable
    private String transactionName;
    @Nullable
    private User user;

    /* loaded from: classes2.dex */
    interface IWithSession {
        void accept(@Nullable Session session);
    }

    @ApiStatus.Internal
    /* loaded from: classes2.dex */
    public interface IWithTransaction {
        void accept(@Nullable ITransaction iTransaction);
    }

    public Scope(@NotNull SentryOptions options) {
        this.fingerprint = new ArrayList();
        this.tags = new ConcurrentHashMap();
        this.extra = new ConcurrentHashMap();
        this.eventProcessors = new CopyOnWriteArrayList();
        this.sessionLock = new Object();
        this.transactionLock = new Object();
        this.contexts = new Contexts();
        this.attachments = new CopyOnWriteArrayList();
        this.options = (SentryOptions) Objects.requireNonNull(options, "SentryOptions is required.");
        this.breadcrumbs = createBreadcrumbsList(this.options.getMaxBreadcrumbs());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Scope(@NotNull Scope scope) {
        this.fingerprint = new ArrayList();
        this.tags = new ConcurrentHashMap();
        this.extra = new ConcurrentHashMap();
        this.eventProcessors = new CopyOnWriteArrayList();
        this.sessionLock = new Object();
        this.transactionLock = new Object();
        this.contexts = new Contexts();
        this.attachments = new CopyOnWriteArrayList();
        this.transaction = scope.transaction;
        this.transactionName = scope.transactionName;
        this.session = scope.session;
        this.options = scope.options;
        this.level = scope.level;
        User userRef = scope.user;
        this.user = userRef != null ? new User(userRef) : null;
        Request requestRef = scope.request;
        this.request = requestRef != null ? new Request(requestRef) : null;
        this.fingerprint = new ArrayList(scope.fingerprint);
        this.eventProcessors = new CopyOnWriteArrayList(scope.eventProcessors);
        Queue<Breadcrumb> breadcrumbsRef = scope.breadcrumbs;
        Queue<Breadcrumb> breadcrumbsClone = createBreadcrumbsList(scope.options.getMaxBreadcrumbs());
        for (Breadcrumb item : breadcrumbsRef) {
            Breadcrumb breadcrumbClone = new Breadcrumb(item);
            breadcrumbsClone.add(breadcrumbClone);
        }
        this.breadcrumbs = breadcrumbsClone;
        Map<String, String> tagsRef = scope.tags;
        Map<String, String> tagsClone = new ConcurrentHashMap<>();
        for (Map.Entry<String, String> item2 : tagsRef.entrySet()) {
            if (item2 != null) {
                tagsClone.put(item2.getKey(), item2.getValue());
            }
        }
        this.tags = tagsClone;
        Map<String, Object> extraRef = scope.extra;
        Map<String, Object> extraClone = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> item3 : extraRef.entrySet()) {
            if (item3 != null) {
                extraClone.put(item3.getKey(), item3.getValue());
            }
        }
        this.extra = extraClone;
        this.contexts = new Contexts(scope.contexts);
        this.attachments = new CopyOnWriteArrayList(scope.attachments);
    }

    @Nullable
    public SentryLevel getLevel() {
        return this.level;
    }

    public void setLevel(@Nullable SentryLevel level) {
        this.level = level;
    }

    @Nullable
    public String getTransactionName() {
        ITransaction tx = this.transaction;
        return tx != null ? tx.getName() : this.transactionName;
    }

    public void setTransaction(@NotNull String transaction) {
        if (transaction != null) {
            ITransaction tx = this.transaction;
            if (tx != null) {
                tx.setName(transaction);
            }
            this.transactionName = transaction;
            return;
        }
        this.options.getLogger().log(SentryLevel.WARNING, "Transaction cannot be null", new Object[0]);
    }

    @Nullable
    public ISpan getSpan() {
        Span span;
        ITransaction tx = this.transaction;
        if (tx != null && (span = tx.getLatestActiveSpan()) != null) {
            return span;
        }
        return tx;
    }

    public void setTransaction(@Nullable ITransaction transaction) {
        synchronized (this.transactionLock) {
            this.transaction = transaction;
        }
    }

    @Nullable
    public User getUser() {
        return this.user;
    }

    public void setUser(@Nullable User user) {
        this.user = user;
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setUser(user);
            }
        }
    }

    @Nullable
    public Request getRequest() {
        return this.request;
    }

    public void setRequest(@Nullable Request request) {
        this.request = request;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public List<String> getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(@NotNull List<String> fingerprint) {
        if (fingerprint == null) {
            return;
        }
        this.fingerprint = new ArrayList(fingerprint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public Queue<Breadcrumb> getBreadcrumbs() {
        return this.breadcrumbs;
    }

    @Nullable
    private Breadcrumb executeBeforeBreadcrumb(@NotNull SentryOptions.BeforeBreadcrumbCallback callback, @NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        try {
            return callback.execute(breadcrumb, hint);
        } catch (Throwable e) {
            this.options.getLogger().log(SentryLevel.ERROR, "The BeforeBreadcrumbCallback callback threw an exception. Exception details will be added to the breadcrumb.", e);
            if (e.getMessage() != null) {
                breadcrumb.setData("sentry:message", e.getMessage());
                return breadcrumb;
            }
            return breadcrumb;
        }
    }

    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        if (breadcrumb == null) {
            return;
        }
        SentryOptions.BeforeBreadcrumbCallback callback = this.options.getBeforeBreadcrumb();
        if (callback != null) {
            breadcrumb = executeBeforeBreadcrumb(callback, breadcrumb, hint);
        }
        if (breadcrumb != null) {
            this.breadcrumbs.add(breadcrumb);
            if (this.options.isEnableScopeSync()) {
                for (IScopeObserver observer : this.options.getScopeObservers()) {
                    observer.addBreadcrumb(breadcrumb);
                }
                return;
            }
            return;
        }
        this.options.getLogger().log(SentryLevel.INFO, "Breadcrumb was dropped by beforeBreadcrumb", new Object[0]);
    }

    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb) {
        addBreadcrumb(breadcrumb, null);
    }

    public void clearBreadcrumbs() {
        this.breadcrumbs.clear();
    }

    public void clearTransaction() {
        synchronized (this.transactionLock) {
            this.transaction = null;
        }
        this.transactionName = null;
    }

    @Nullable
    public ITransaction getTransaction() {
        return this.transaction;
    }

    public void clear() {
        this.level = null;
        this.user = null;
        this.request = null;
        this.fingerprint.clear();
        clearBreadcrumbs();
        this.tags.clear();
        this.extra.clear();
        this.eventProcessors.clear();
        clearTransaction();
        clearAttachments();
    }

    @ApiStatus.Internal
    @NotNull
    public Map<String, String> getTags() {
        return CollectionUtils.newConcurrentHashMap(this.tags);
    }

    public void setTag(@NotNull String key, @NotNull String value) {
        this.tags.put(key, value);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setTag(key, value);
            }
        }
    }

    public void removeTag(@NotNull String key) {
        this.tags.remove(key);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.removeTag(key);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public Map<String, Object> getExtras() {
        return this.extra;
    }

    public void setExtra(@NotNull String key, @NotNull String value) {
        this.extra.put(key, value);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.setExtra(key, value);
            }
        }
    }

    public void removeExtra(@NotNull String key) {
        this.extra.remove(key);
        if (this.options.isEnableScopeSync()) {
            for (IScopeObserver observer : this.options.getScopeObservers()) {
                observer.removeExtra(key);
            }
        }
    }

    @NotNull
    public Contexts getContexts() {
        return this.contexts;
    }

    public void setContexts(@NotNull String key, @NotNull Object value) {
        this.contexts.put(key, value);
    }

    public void setContexts(@NotNull String key, @NotNull Boolean value) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull String value) {
        Map<String, String> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull Number value) {
        Map<String, Number> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull Collection<?> value) {
        Map<String, Collection<?>> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull Object[] value) {
        Map<String, Object[]> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void setContexts(@NotNull String key, @NotNull Character value) {
        Map<String, Character> map = new HashMap<>();
        map.put("value", value);
        setContexts(key, map);
    }

    public void removeContexts(@NotNull String key) {
        this.contexts.remove(key);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public List<Attachment> getAttachments() {
        return new CopyOnWriteArrayList(this.attachments);
    }

    public void addAttachment(@NotNull Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void clearAttachments() {
        this.attachments.clear();
    }

    @NotNull
    private Queue<Breadcrumb> createBreadcrumbsList(int maxBreadcrumb) {
        return SynchronizedQueue.synchronizedQueue(new CircularFifoQueue(maxBreadcrumb));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NotNull
    public List<EventProcessor> getEventProcessors() {
        return this.eventProcessors;
    }

    public void addEventProcessor(@NotNull EventProcessor eventProcessor) {
        this.eventProcessors.add(eventProcessor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Session withSession(@NotNull IWithSession sessionCallback) {
        Session cloneSession = null;
        synchronized (this.sessionLock) {
            sessionCallback.accept(this.session);
            if (this.session != null) {
                cloneSession = this.session.m126clone();
            }
        }
        return cloneSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public SessionPair startSession() {
        SessionPair pair = null;
        synchronized (this.sessionLock) {
            if (this.session != null) {
                this.session.end();
            }
            Session previousSession = this.session;
            if (this.options.getRelease() != null) {
                this.session = new Session(this.options.getDistinctId(), this.user, this.options.getEnvironment(), this.options.getRelease());
                Session previousClone = previousSession != null ? previousSession.m126clone() : null;
                pair = new SessionPair(this.session.m126clone(), previousClone);
            } else {
                this.options.getLogger().log(SentryLevel.WARNING, "Release is not set on SentryOptions. Session could not be started", new Object[0]);
            }
        }
        return pair;
    }

    /* loaded from: classes2.dex */
    static final class SessionPair {
        @NotNull
        private final Session current;
        @Nullable
        private final Session previous;

        public SessionPair(@NotNull Session current, @Nullable Session previous) {
            this.current = current;
            this.previous = previous;
        }

        @Nullable
        public Session getPrevious() {
            return this.previous;
        }

        @NotNull
        public Session getCurrent() {
            return this.current;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public Session endSession() {
        Session previousSession = null;
        synchronized (this.sessionLock) {
            if (this.session != null) {
                this.session.end();
                previousSession = this.session.m126clone();
                this.session = null;
            }
        }
        return previousSession;
    }

    @ApiStatus.Internal
    public void withTransaction(@NotNull IWithTransaction callback) {
        synchronized (this.transactionLock) {
            callback.accept(this.transaction);
        }
    }
}
